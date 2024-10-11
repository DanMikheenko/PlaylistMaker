package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.search.ui.view_model.State


class SearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModels { SearchViewModel.Factory }
    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var state: State

    companion object {
        const val KEY_EDIT_TEXT = "editTextValue"
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
        }


        val updateBtn = findViewById<View>(R.id.updateRequestBtn)
        updateBtn.setOnClickListener {
            search()
        }
        viewModel.readSearchHistory()
        trackAdapter = TrackAdapter(emptyList())

        viewModel.state.observe(this) { _state ->
            state = _state
            render(state)
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
            handler.removeCallbacks(searchRunnable)
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                search()
                true
            }
            false
        }
        binding.editText.setOnFocusChangeListener { view, hasFocus ->
            if (binding.editText.hasFocus() && binding.editText.text.isEmpty() && state == State.ShowEmptyTrackHistory) {
                handler.removeCallbacks(searchRunnable)
                viewModel.readSearchHistory()
            }
            if (binding.editText.hasFocus() && binding.editText.text.isEmpty() && state != State.ShowEmptyTrackHistory) {
                handler.removeCallbacks(searchRunnable)
                viewModel.readSearchHistory()
            }

        }

        binding.searchHistoryLayout.clearHistoryBtn.setOnClickListener {
            viewModel.clearHistory()
        }


    }

    private fun render(state: State) {
        when (state) {
            is State.LoadingSearchingTracks -> showProgressBar()
            is State.ShowEmptyResult -> showNothingFound()
            is State.ShowSearchResult -> {
                binding.recyclerView.adapter = TrackAdapter(state.data)
                showResult()
            }

            is State.ConnectionError -> {
                showConnectionError()
            }
            is State.Error -> {
            }
            is State.ShowEmptyTrackHistory -> {
                hideAll()
            }

            is State.ShowSearchingTrackHistory -> {
                binding.searchHistoryLayout.searchHistoryRecyclerView.adapter =
                    TrackAdapter(state.data)
                showHistory()
            }
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
        viewModel.search(binding.editText.text.toString())
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