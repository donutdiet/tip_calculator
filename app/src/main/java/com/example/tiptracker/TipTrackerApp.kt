package com.example.tiptracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tiptracker.data.BottomBarNavItem
import com.example.tiptracker.screens.DiningLogsScreen
import com.example.tiptracker.screens.ProfileScreen
import com.example.tiptracker.screens.addentry.AddEntryScreen
import com.example.tiptracker.ui.LogViewModel
import com.example.tiptracker.ui.theme.TipTrackerTheme

enum class DiningLogScreen {
    BillInput,
    DescriptionInput
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.tip_tracker_app_logo),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayLarge
                )
            }
        },
        modifier = modifier
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentScreen(
    viewModel: LogViewModel,
    navController: NavHostController,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    when (selectedIndex) {
        0 -> DiningLogsScreen(
            diningLogs = viewModel.diningLogs,
            modifier = modifier.fillMaxSize(),
            viewModel = viewModel
        )

        1 -> AddEntryScreen(
            viewModel = viewModel,
            navController = navController,
            modifier = modifier
        )

        2 -> ProfileScreen(
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
fun AppBottomBar(
    navItemList: List<BottomBarNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.Transparent,
        modifier = Modifier
            .height(104.dp)
    ) {
        navItemList.forEachIndexed { index, navItem ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        painter = painterResource(navItem.iconRes),
                        contentDescription = navItem.label + " navigation tab"
                    )
                },
                label = {
                    Text(
                        text = navItem.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TipTrackerApp(
    viewModel: LogViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.initialize(context)
    }

    var selectedIndex by remember { mutableIntStateOf(1) }

    val navItemList = listOf(
        BottomBarNavItem("Logs", R.drawable.list),
        BottomBarNavItem("New", R.drawable.add),
        BottomBarNavItem("Profile", R.drawable.person)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppTopBar() },
        bottomBar = {
            AppBottomBar(
                navItemList = navItemList,
                selectedIndex = selectedIndex,
                onItemSelected = { index -> selectedIndex = index }
            )
        }
    ) { innerPadding ->
        ContentScreen(
            viewModel = viewModel,
            navController = navController,
            selectedIndex = selectedIndex,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun TipTrackerAppScaffoldPreview() {
    TipTrackerTheme {
        TipTrackerApp()
    }
}