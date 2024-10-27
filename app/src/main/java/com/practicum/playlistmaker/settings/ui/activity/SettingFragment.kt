package com.practicum.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.practicum.playlistmaker.databinding.FragmentSettingBinding
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {
    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    private fun setUI() {



        binding.shareTextView.setOnClickListener {
            viewModel.shareApp()
        }


        binding.writeToSpprtTextView.setOnClickListener {
            viewModel.openSupport()
        }


        binding.readAgreementTextView.setOnClickListener {
            viewModel.openTerms()
        }



        viewModel.isDarkThemeEnabled().observe(requireActivity(), Observer { isEnabled ->
            binding.themeSwitch.isChecked = isEnabled
        })

        binding.themeSwitch.setOnCheckedChangeListener { _, isEnabled ->
            viewModel.switchTheme(isEnabled)
        }
    }
}