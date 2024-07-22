package com.example.tiptracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiptracker.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    val isDarkModeActive =
        userPreferencesRepository.userPreferencesFlow.map { it.isDarkModeActive }

    fun updateIsDarkModeActive(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateIsDarkModeActive(isDarkModeActive)
        }
    }
}