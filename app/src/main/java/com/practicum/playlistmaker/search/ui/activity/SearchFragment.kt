package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.activity.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.OnTrackClickListener
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.search.ui.view_model.State
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment(), OnTrackClickListener {
    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClear.setOnClickListener {
            resetSearchText()
            hideKeyboard()
        }

        binding.connectionErrorPlaceholder.updateRequestBtn.setOnClickListener {
            search()
        }


        viewModel.state.observe(viewLifecycleOwner) { _state ->
            render(_state)
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
                viewModel.searchDebounce(binding.editText.text.toString())
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
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                search()
            }
            false
        }
        binding.editText.setOnFocusChangeListener { view, hasFocus ->
            if (binding.editText.hasFocus() && binding.editText.text.isEmpty()) {
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
                binding.recyclerView.adapter = TrackAdapter(state.data, this)
                showResult()
            }

            is State.ConnectionError -> {
                showConnectionError()
            }

            is State.Error -> {
                showConnectionError()
            }

            is State.ShowEmptyTrackHistory -> {
                hideAll()
            }

            is State.ShowSearchingTrackHistory -> {
                binding.searchHistoryLayout.searchHistoryRecyclerView.adapter =
                    TrackAdapter(state.data, this)
                showHistory()
            }
        }
    }

    private fun resetSearchText() {
        binding.editText.text.clear()
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
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
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


    override fun onTrackClick(track: Track) {
        val intent = Intent(requireContext(), PlayerActivity::class.java)
        intent.putExtra("selectedTrack", Gson().toJson(track))
        startActivity(intent)
        viewModel.addTrackToSearchHistory(track)
    }

    override fun onResume() {
        super.onResume()
        viewModel.state.observe(viewLifecycleOwner) { _state ->
            render(_state)
        }
    }

    companion object {
        const val KEY_EDIT_TEXT = "editTextValue"
    }
}