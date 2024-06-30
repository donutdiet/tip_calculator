package com.example.tiptracker.screens.logs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.tiptracker.ui.EditLogViewModel
import com.example.tiptracker.ui.LogViewModel

enum class DiningLogsScreens {
    DiningLogs,
    EditLogs
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiningLogsNavHost(
    logViewModel: LogViewModel,
    editLogViewModel: EditLogViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DiningLogsScreens.DiningLogs.name,
        modifier = modifier
    ) {
        composable(route = DiningLogsScreens.DiningLogs.name) {
            DiningLogsScreen(
                diningLogs = logViewModel.diningLogs,
                logViewModel = logViewModel,
                editLogViewModel = editLogViewModel,
                onEditButtonClicked = { navController.navigate(DiningLogsScreens.EditLogs.name) },
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(route = DiningLogsScreens.EditLogs.name) {
            EditLogScreen(
                logViewModel = logViewModel,
                onCancelButtonClicked = {
                    editLogViewModel.clearForm()
                    navController.navigate(DiningLogsScreens.DiningLogs.name)
                },
                onSaveButtonClicked = {
                    navController.navigate(DiningLogsScreens.DiningLogs.name)
                },
                editLogViewModel = editLogViewModel,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}