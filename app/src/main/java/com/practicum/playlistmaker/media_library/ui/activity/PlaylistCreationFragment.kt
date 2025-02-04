package com.practicum.playlistmaker.media_library.ui.activity

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistCreationBinding
import com.practicum.playlistmaker.media_library.domain.models.Playlist
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistCreationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

open class PlaylistCreationFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistCreationBinding
    private val viewModel by viewModel<PlaylistCreationViewModel>()
    var playlistImageUri: Uri? = null
    private var isDataEntered = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    playlistImageUri = uri
                    binding.imageView.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                    isDataEntered = true // Устанавливаем флаг, так как выбрано изображение
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.imageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createButton.setOnClickListener {
            if (binding.playlistNameEditText.text.isEmpty()) {
                binding.playlistNameEditText.error = "Введите название плейлиста"
                return@setOnClickListener
            }

            val filePath: String? = if (playlistImageUri != null) {
                val dirPath = File(
                    requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "selected_images"
                )

                if (!dirPath.exists()) {
                    dirPath.mkdirs()
                }

                val fileName = "image_${System.currentTimeMillis()}.jpg"
                val file = File(dirPath, fileName)

                try {
                    val inputStream =
                        requireContext().contentResolver.openInputStream(playlistImageUri!!)
                    val outputStream = FileOutputStream(file)
                    BitmapFactory.decodeStream(inputStream)
                        .compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

                    inputStream?.close()
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        "Ошибка при сохранении изображения",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                file.absolutePath
            } else {
                null
            }

            val playlist = Playlist(
                playlistId = 0,
                playlistName = binding.playlistNameEditText.text.toString(),
                playlistImagePath = filePath ?: "",
                description = binding.playlistDescriptionEditText.text.toString(),
                addedTracksId = "",
                addedTracksCount = "0"
            )

            viewModel.createPlaylist(playlist)
            Toast.makeText(requireContext(), "Плейлист успешно создан!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        binding.playlistNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isDataEntered = !s.isNullOrEmpty()
                updateCreateButtonState(s)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            handleBackPress()
        }

        binding.newPlaylistHeader.setOnClickListener {
            handleBackPress()
        }
    }

    private fun updateCreateButtonState(s: CharSequence?) {
        if (!s.isNullOrEmpty()) {
            binding.createButton.setBackgroundResource(R.drawable.rounded_button_background_blue)
        } else {
            binding.createButton.setBackgroundResource(R.drawable.rounded_button_background)
        }
        binding.createButton.isEnabled = !s.isNullOrBlank()
    }

    private fun handleBackPress() {
        if (isDataEntered) {
            showExitConfirmationDialog()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setPositiveButton("Завершить") { _, _ ->
                findNavController().popBackStack()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum"
        )

        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "first_cover.jpg")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream).compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun loadImageFromPrivateStorage(): Bitmap? {
        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        val file = File(filePath, "first_cover.jpg")
        return if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
    }
}
