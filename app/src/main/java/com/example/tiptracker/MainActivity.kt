package com.example.tiptracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tiptracker.screens.settings.SettingsViewModel
import com.example.tiptracker.ui.theme.TipTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SettingsViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkModeActive by viewModel.isDarkModeActive.collectAsState(initial = false)
            TipTrackerTheme(
                darkTheme = isDarkModeActive
            ) {
                TipTrackerApp(
                    settingsViewModel = viewModel,
                    isDarkModeActive = isDarkModeActive
                )
            }
        }
    }
}