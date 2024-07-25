package com.example.tiptracker.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiptracker.data.UserPreferencesRepository
import com.example.tiptracker.data.ISettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel(), ISettingsViewModel {

    override val isDarkModeActive: Flow<Boolean> =
        userPreferencesRepository.userPreferencesFlow.map { it.isDarkModeActive }

    override fun toggleDarkMode(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateIsDarkModeActive(isDarkModeActive)
        }
    }
}