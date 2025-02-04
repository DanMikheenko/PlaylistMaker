package com.practicum.playlistmaker.media_library.ui.activity

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistCreationBinding
import com.practicum.playlistmaker.media_library.domain.models.Playlist
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistEditingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class PlaylistEditingFragment : PlaylistCreationFragment() {
    private val viewModel by viewModel<PlaylistEditingViewModel>()
    private lateinit var binding: FragmentPlaylistCreationBinding
    private lateinit var playlist: Playlist


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlaylistCreationBinding.bind(view)

        // Изменяем текст кнопки и заголовка
        binding.createButton.text = getString(R.string.save)
        binding.newPlaylistHeader.text = getString(R.string.edit_playlist)

        // Получаем ID плейлиста из аргументов
        val playlistId = arguments?.getString("selectedPlaylist") ?: return
        viewModel.loadData(playlistId)

        // Наблюдаем за данными плейлиста
        viewModel.playlist.observe(viewLifecycleOwner) { _playlist ->
            if (_playlist != null) {
                playlist = _playlist
                renderPlaylistData(_playlist)
                // Устанавливаем текущий путь к изображению
                if (_playlist.playlistImagePath.isNotEmpty()) {
                    playlistImageUri = Uri.fromFile(File(_playlist.playlistImagePath))
                }
            }
        }

        // Логика выбора изображения
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                playlistImageUri = uri
                Glide.with(binding.imageView)
                    .load(uri)
                    .centerCrop()
                    .transform(RoundedCorners(10))
                    .placeholder(R.drawable.player_image_placeholder)
                    .error(R.drawable.player_image_placeholder)
                    .into(binding.imageView)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        binding.imageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Обработка нажатия на кнопку "Сохранить"
        binding.createButton.setOnClickListener {
            savePlaylistChanges()
        }
    }

    private fun renderPlaylistData(playlist: Playlist) {
        // Заполняем поля данными плейлиста
        binding.playlistNameEditText.setText(playlist.playlistName)
        binding.playlistDescriptionEditText.setText(playlist.description)

        // Загружаем изображение, если оно есть
        if (playlist.playlistImagePath.isNotEmpty()) {
            Glide.with(binding.imageView)
                .load(File(playlist.playlistImagePath))
                .centerCrop()
                .transform(RoundedCorners(10))
                .placeholder(R.drawable.player_image_placeholder)
                .error(R.drawable.player_image_placeholder)
                .into(binding.imageView)
        } else {
            binding.imageView.setImageResource(R.drawable.player_image_placeholder)
        }
    }

    private fun savePlaylistChanges() {
        val playlistId = arguments?.getString("selectedPlaylist") ?: return
        val playlistName = binding.playlistNameEditText.text.toString()
        val playlistDescription = binding.playlistDescriptionEditText.text.toString()

        // Если новое изображение не выбрано, используем текущий путь
        val playlistImagePath = if (playlistImageUri != null) {
            // Сохраняем новое изображение и получаем путь
            saveImageToPrivateStor(playlistImageUri!!)
        } else {
            // Используем текущий путь к изображению
            viewModel.playlist.value?.playlistImagePath ?: ""
        }

        val updatedPlaylist = Playlist(
            playlistId = playlistId.toInt(),
            playlistName = playlistName,
            playlistImagePath = playlistImagePath,
            description = playlistDescription,
            addedTracksId = playlist.addedTracksId,
            addedTracksCount = playlist.addedTracksCount
        )

        // Обновляем плейлист
        viewModel.createPlaylist(updatedPlaylist)
        Toast.makeText(requireContext(), "Плейлист успешно обновлён!", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    private fun saveImageToPrivateStor(uri: Uri): String {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val fileName = "cover_${System.currentTimeMillis()}.jpg"
        val file = File(filePath, fileName)

        try {
            // Используем ContentResolver для открытия InputStream из Uri
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)

            // Копируем данные из InputStream в FileOutputStream
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            // Возвращаем абсолютный путь к сохранённому файлу
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(),
                "Ошибка при сохранении изображения",
                Toast.LENGTH_SHORT
            ).show()
            return ""
        }
    }
}