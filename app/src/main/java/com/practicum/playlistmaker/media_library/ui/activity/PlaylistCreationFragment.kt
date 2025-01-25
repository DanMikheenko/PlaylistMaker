    package com.practicum.playlistmaker.media_library.ui.activity

    import android.graphics.Bitmap
    import android.graphics.BitmapFactory
    import android.net.Uri
    import android.os.Bundle
    import android.os.Environment
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.activity.result.PickVisualMediaRequest
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.fragment.app.Fragment
    import androidx.navigation.fragment.findNavController
    import com.practicum.playlistmaker.MainActivity
    import com.practicum.playlistmaker.R
    import com.practicum.playlistmaker.databinding.FragmentPlaylistCreationBinding
    import com.practicum.playlistmaker.media_library.domain.models.Playlist
    import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistCreationViewModel
    import org.koin.androidx.viewmodel.ext.android.viewModel
    import java.io.File
    import java.io.FileOutputStream


    class PlaylistCreationFragment : Fragment() {
        private lateinit var binding: FragmentPlaylistCreationBinding
        private val viewModel by viewModel<PlaylistCreationViewModel>()
        private lateinit var playlistImageUri: Uri

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding.newPlaylistHeader.setOnClickListener {
                findNavController().navigate(R.id.action_playlistCreationFragment_to_tabContainerFragment)
            }
            (activity as? MainActivity)?.hideBottomNav()

            val pickMedia =
                registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    //обрабатываем событие выбора пользователем фотографии
                    if (uri != null) {
                        playlistImageUri = uri
                        binding.imageView.setImageURI(uri)
                        saveImageToPrivateStorage(uri)
                    } else {
                        Log.d("PhotoPicker", "No media selected")
                    }
                }

            binding.imageView.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            binding.createButton.setOnClickListener {
                val filePath = File(
                    requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "selected_images"
                )
                if (!filePath.exists()) {
                    filePath.mkdirs()
                }

                val fileName = "image_${System.currentTimeMillis()}.jpg"
                val file = File(filePath, fileName)

                // Сохранение изображения в файл
                val inputStream = requireContext().contentResolver.openInputStream(playlistImageUri)
                val outputStream = FileOutputStream(file)
                BitmapFactory.decodeStream(inputStream)
                    .compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

                inputStream?.close()
                outputStream.close()

                val playlist = Playlist(
                    playlistId = 0,
                    playlistName = binding.playlistNameEditText.text.toString(),
                    playlistImagePath = file.absolutePath,
                    description = binding.playlistDescriptionEditText.text.toString(),
                    addedTracksId = "",
                    addedTracksCount = ""
                )

                viewModel.createPlaylist(playlist)
            }


        }

        private fun saveImageToPrivateStorage(uri: Uri) {
            //создаём экземпляр класса File, который указывает на нужный каталог
            val filePath =
                File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
            //создаем каталог, если он не создан
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            //создаём экземпляр класса File, который указывает на файл внутри каталога
            val file = File(filePath, "first_cover.jpg")
            // создаём входящий поток байтов из выбранной картинки
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            // создаём исходящий поток байтов в созданный выше файл
            val outputStream = FileOutputStream(file)
            // записываем картинку с помощью BitmapFactory
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        }

        private fun loadImageFromPrivateStorage(): Bitmap? {
            // Путь к файлу, где сохранена картинка
            val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
            val file = File(filePath, "first_cover.jpg")

            // Проверяем, существует ли файл
            if (file.exists()) {
                // Загрузка картинки в Bitmap
                return BitmapFactory.decodeFile(file.absolutePath)
            } else {
                // Файл не найден
                return null
            }
        }
        override fun onPause() {
            super.onPause()
            (activity as? MainActivity)?.showBottomNav()
        }

        override fun onResume() {
            super.onResume()
            (activity as? MainActivity)?.hideBottomNav()
        }
    }