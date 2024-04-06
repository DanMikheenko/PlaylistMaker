package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackService = retrofit.create(ITunesSearchAPI::class.java)
    private val tracks = ArrayList<Track>()
    val trackAdapter = TrackAdapter(tracks)

    private lateinit var editText: EditText

    companion object {
        const val KEY_EDIT_TEXT = "editTextValue"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        editText = findViewById(R.id.editText)

        val homeButton = findViewById<View>(R.id.searchTextView)
        homeButton.setOnClickListener {
            finish()
        }
        val buttonClear = findViewById<View>(R.id.btnClear)
        buttonClear.setOnClickListener {
            resetSearchText()
            hideKeyboard()
            tracks.clear()
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotBlank() == true) {
                    buttonClear.isVisible = true
                } else {
                    buttonClear.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        if (savedInstanceState != null) {
            // Восстановить текст из сохраненного состояния
            val savedText = savedInstanceState.getString(KEY_EDIT_TEXT, "")
            editText.setText(savedText)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = trackAdapter

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(editText.text.toString())
                trackAdapter.notifyDataSetChanged()
                true
            }
            false
        }


    }

    private fun resetSearchText() {
        editText.text.clear()
    }

    private fun search(request: String) {
        val call = trackService.search(request)
        val placeholderLayout = findViewById<LinearLayout>(R.id.placeholder_layout)
        call.enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.isSuccessful) {
                    val musicTracks = response.body()?.results
                    if (musicTracks != null) {
                        tracks.addAll(musicTracks)
                        trackAdapter.notifyDataSetChanged()
                    }
                    if (tracks.isEmpty()){
                        placeholderLayout.visibility = View.VISIBLE
                    }
                }
            }


            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
            }
        })


    }

    override fun onSaveInstanceState(outState: Bundle) {

        val currentText = editText.text.toString()
        outState.putString(KEY_EDIT_TEXT, currentText)
        super.onSaveInstanceState(outState)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

//        forecaService.getLocations("Bearer $token", queryInput.text.toString())
//            .enqueue(object : Callback<LocationsResponse> {
//                override fun onResponse(call: Call<LocationsResponse>,
//                                        response: Response<LocationsResponse>) {
//                    when (response.code()) {
//                        200 -> {
//                            if (response.body()?.locations?.isNotEmpty() == true) {
//                                locations.clear()
//                                locations.addAll(response.body()?.locations!!)
//                                adapter.notifyDataSetChanged()
//                                showMessage("", "")
//                            } else {
//                                showMessage(getString(R.string.nothing_found), "")
//                            }
//
//                        }
//                        401 -> authenticate()
//                        else -> showMessage(getString(R.string.something_went_wrong), response.code().toString())
//                    }
//
//                }
//
//                override fun onFailure(call: Call<LocationsResponse>, t: Throwable) {
//                    showMessage(getString(R.string.something_went_wrong), t.message.toString())
//                }
//
//            })
//    }
}