package com.example.tiptracker.screens.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptracker.data.ISettingsViewModel
import com.example.tiptracker.ui.theme.TipTrackerTheme

@Composable
fun SettingsScreen(
    settingsViewModel: ISettingsViewModel,
    isDarkModeActive: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 40.dp)
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.displayMedium,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Dark Mode",
                    style = MaterialTheme.typography.labelMedium
                )
                Switch(
                    checked = isDarkModeActive,
                    onCheckedChange = {settingsViewModel.toggleDarkMode(it)}
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreenContentPreview() {
    val mockViewModel = PreviewSettingsViewModel()
    SettingsScreen(
        settingsViewModel = mockViewModel,
        isDarkModeActive = false
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun SettingsScreenPreview() {
    TipTrackerTheme {
        SettingsScreenContentPreview()
    }
}