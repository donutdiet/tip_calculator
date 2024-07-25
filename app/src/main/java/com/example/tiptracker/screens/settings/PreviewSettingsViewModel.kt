package com.example.tiptracker.screens.settings

import androidx.lifecycle.ViewModel
import com.example.tiptracker.data.ISettingsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewSettingsViewModel : ViewModel(), ISettingsViewModel {
    private val _isDarkModeActive = MutableStateFlow(false)
    override val isDarkModeActive: StateFlow<Boolean> = _isDarkModeActive

    override fun toggleDarkMode(isDarkModeActive: Boolean) {
        _isDarkModeActive.value = !isDarkModeActive
    }
}