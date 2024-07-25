package com.example.tiptracker.data

import kotlinx.coroutines.flow.Flow

interface ISettingsViewModel {
    val isDarkModeActive: Flow<Boolean>
    fun toggleDarkMode(isDarkModeActive: Boolean)
}