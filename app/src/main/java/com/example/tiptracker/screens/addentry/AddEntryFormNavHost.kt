package com.example.tiptracker.screens.addentry

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tiptracker.ui.LogViewModel

enum class DiningInputScreens {
    BillInput,
    DescriptionInput
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEntryFormNavHost(
    viewModel: LogViewModel,
    navController: NavHostController,
    navigateToDiningLogsScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DiningInputScreens.BillInput.name,
        modifier = modifier
    ) {
        composable(route = DiningInputScreens.BillInput.name) {
            BillInputScreen(
                modifier = Modifier.fillMaxSize(),
                onClearButtonClicked = { viewModel.clearForm() },
                onLogButtonClicked = {
                    if(viewModel.checkFormValidity()) {
                        viewModel.updateCurrentDate()
                        navController.navigate(DiningInputScreens.DescriptionInput.name)
                    }
                },
                viewModel = viewModel
            )
        }
        composable(route = DiningInputScreens.DescriptionInput.name) {
            DiningDescriptionScreen(
                modifier = Modifier.fillMaxSize(),
                onCancelButtonClicked = {
                    navController.popBackStack(
                        DiningInputScreens.BillInput.name,
                        inclusive = false
                    )
                },
                onSaveButtonClicked = {
                    viewModel.logEntry()
                    navController.popBackStack(
                        DiningInputScreens.BillInput.name,
                        inclusive = false
                    )
                    navigateToDiningLogsScreen()
                },
                viewModel = viewModel
            )
        }
    }
}