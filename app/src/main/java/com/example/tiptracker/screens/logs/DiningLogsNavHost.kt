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
                onEditButtonClicked = {
                    editLogViewModel.loadCurrentLogData(
                        diningLogs = logViewModel.diningLogs,
                        index = it
                    )
                    navController.navigate(DiningLogsScreens.EditLogs.name)
                },
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
                    logViewModel.updateEntry(
                        id = editLogViewModel.id,
                        newBillAmount = editLogViewModel.tempBillAmount.value,
                        newTipPercent = editLogViewModel.getCalculatedTipPercent(),
                        newTipAmount = editLogViewModel.tempTipAmount.value,
                        newPersonCount = editLogViewModel.tempPersonCount.value,
                        newTotalAmount = editLogViewModel.getCalculatedTotal(),
                        newTotalAmountPerPerson = editLogViewModel.getCalculatedTotalPerPerson(),
                        newRestaurantName = editLogViewModel.tempRestaurantName.value,
                        newRestaurantDescription = editLogViewModel.tempRestaurantDescription.value,
                        newDate = editLogViewModel.tempDate.value
                    )
                    editLogViewModel.clearForm()
                    navController.navigate(DiningLogsScreens.DiningLogs.name)
                },
                editLogViewModel = editLogViewModel,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}