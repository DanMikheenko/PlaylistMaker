package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.search.ui.view_model.SearchState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModelFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryInteractor: SearchHistoryInteractor

    companion object {
        const val KEY_EDIT_TEXT = "editTextValue"
        const val PLAY_LIST_MAKER_SHARE_PREFERENCES = "playListMakerSettings"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchTextView.setOnClickListener {
            finish()
        }

        binding.btnClear.setOnClickListener {
            resetSearchText()
            hideKeyboard()
            viewModel.clearTracks()
        }

        sharedPreferences = applicationContext.getSharedPreferences(
            PLAY_LIST_MAKER_SHARE_PREFERENCES, MODE_PRIVATE
        )

        searchHistoryInteractor = Creator.provideSearchHistoryInteractor(sharedPreferences)
        val updateBtn = findViewById<View>(R.id.updateRequestBtn)
        updateBtn.setOnClickListener {
            search()
        }

        val factory =
            SearchViewModelFactory(searchHistoryInteractor, Creator.provideTracksInteractor())
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)



        viewModel.readSearchHistory()

        // Наблюдение за изменениями LiveData
        viewModel.tracksHistory.observe(this) { tracksHistory ->
            binding.searchHistoryLayout.searchHistoryRecyclerView.adapter = TrackAdapter(tracksHistory, sharedPreferences)
        }

        trackAdapter = TrackAdapter(emptyList(), sharedPreferences)

        viewModel.tracks.observe(this) { foundTracks ->
            trackAdapter = TrackAdapter(foundTracks, sharedPreferences)
            binding.recyclerView.adapter = trackAdapter
        }

        viewModel.state.observe(this){ searchState ->
            render(searchState)
        }



        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotBlank() == true) {
                    binding.btnClear.isVisible = true
                } else {
                    binding.btnClear.isVisible = false
                }
                searchDebounce()

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        if (savedInstanceState != null) {
            // Восстановить текст из сохраненного состояния
            val savedText = savedInstanceState.getString(KEY_EDIT_TEXT, "")
            binding.editText.setText(savedText)

        }

        binding.editText.setOnEditorActionListener { v, actionId, event ->
            viewModel.clearTracks()
            handler.removeCallbacks(searchRunnable)
            binding.placeholderLayout.root.visibility = View.GONE
            binding.connectionErrorPlaceholder.root.visibility = View.GONE
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                binding.searchHistoryLayout.root.visibility = View.GONE
                search()
                true
            }
            false
        }
        binding.editText.setOnFocusChangeListener { view, hasFocus ->
            if (binding.editText.hasFocus() && binding.editText.text.isEmpty() && viewModel.isSearchHistoryEmpty()) {
                handler.removeCallbacks(searchRunnable)
                hideAll()
            }
            if (binding.editText.hasFocus() && binding.editText.text.isEmpty() && !viewModel.isSearchHistoryEmpty()) {
                handler.removeCallbacks(searchRunnable)
                showHistory()
            }

        }

        val clearHistory = findViewById<View>(R.id.clearHistoryBtn)
        clearHistory.setOnClickListener {
            searchHistoryInteractor.clear()
            searchHistoryInteractor.readSearchHistory(object :
                SearchHistoryInteractor.SearchHistoryConsumer {
                override fun consume(trackHistory: List<Track>) {
                    binding.searchHistoryLayout.searchHistoryRecyclerView.adapter = TrackAdapter(trackHistory, sharedPreferences)
                }
            })
            binding.searchHistoryLayout.root.visibility = View.GONE
        }


    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showProgressBar()
            is SearchState.NothingFound -> showNothingFound()
            is SearchState.Success -> showResult()
            is SearchState.ConnectionError -> showConnectionError()
            is SearchState.Error -> TODO()
        }
    }

    private fun resetSearchText() {
        binding.editText.text.clear()
        handler.removeCallbacks(searchRunnable)
        binding.placeholderLayout.root.visibility = View.GONE
        binding.connectionErrorPlaceholder.root.visibility = View.GONE
        binding.searchHistoryLayout.root.visibility = View.VISIBLE
    }

    private fun search() {
        showProgressBar()
        viewModel.clearTracks()
        viewModel.search(binding.editText.text.toString())
        showResult()
    }

    override fun onSaveInstanceState(outState: Bundle) {

        val currentText = binding.editText.text.toString()
        outState.putString(KEY_EDIT_TEXT, currentText)
        super.onSaveInstanceState(outState)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private val searchRunnable = Runnable { search() }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    override fun onResume() {
        super.onResume()
        viewModel.readSearchHistory()
        viewModel.tracksHistory.observe(this) { tracksHistory ->
            binding.searchHistoryLayout.searchHistoryRecyclerView.adapter = TrackAdapter(tracksHistory, sharedPreferences)
            binding.searchHistoryLayout.searchHistoryRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun hideAll() {
        binding.placeholderLayout.root.visibility = View.GONE
        binding.connectionErrorPlaceholder.root.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.searchHistoryLayout.root.visibility = View.GONE
    }

    private fun showHistory() {
        binding.placeholderLayout.root.visibility = View.GONE
        binding.connectionErrorPlaceholder.root.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.searchHistoryLayout.root.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        binding.placeholderLayout.root.visibility = View.GONE
        binding.connectionErrorPlaceholder.root.visibility = View.GONE
        binding.searchHistoryLayout.root.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showResult() {
        binding.placeholderLayout.root.visibility = View.GONE
        binding.connectionErrorPlaceholder.root.visibility = View.GONE
        binding.searchHistoryLayout.root.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }
    private fun showNothingFound() {
        binding.placeholderLayout.root.visibility = View.VISIBLE
        binding.connectionErrorPlaceholder.root.visibility = View.GONE
        binding.searchHistoryLayout.root.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun showConnectionError() {
        binding.placeholderLayout.root.visibility = View.GONE
        binding.connectionErrorPlaceholder.root.visibility = View.VISIBLE
        binding.searchHistoryLayout.root.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }


}