package com.example.tiptracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiptracker.data.navItemList
import com.example.tiptracker.screens.ProfileScreen
import com.example.tiptracker.screens.addentry.AddEntryFormNavHost
import com.example.tiptracker.screens.logs.DiningLogsNavHost
import com.example.tiptracker.ui.EditLogViewModel
import com.example.tiptracker.ui.LogViewModel
import com.example.tiptracker.ui.theme.TipTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TipTrackerApp(
    logViewModel: LogViewModel = viewModel(),
    editLogViewModel: EditLogViewModel = viewModel(),
) {
    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 1)
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        logViewModel.initialize(context)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppTopBar() },
        bottomBar = {
            AppBottomBar(
                pagerState = pagerState,
                scope = scope
            )
        }
    ) { innerPadding ->
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> DiningLogsNavHost(
                    logViewModel = logViewModel,
                    editLogViewModel = editLogViewModel,
                    modifier = Modifier.padding(innerPadding)
                )

                1 -> AddEntryFormNavHost(
                    viewModel = logViewModel,
                    navigateToDiningLogsScreen = {
                        scope.launch {
                            pagerState.scrollToPage(0)
                        }
                    },
                    modifier = Modifier.padding(innerPadding)
                )
                2 -> ProfileScreen(
                    logViewModel = logViewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppBottomBar(
    pagerState: PagerState,
    scope: CoroutineScope
) {
    NavigationBar(
        containerColor = Color.Transparent,
        modifier = Modifier
            .height(104.dp)
    ) {
        navItemList.forEachIndexed { index, navItem ->
            NavigationBarItem(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
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
@Preview
@Composable
fun TipTrackerAppScaffoldPreview() {
    TipTrackerTheme {
        TipTrackerApp()
    }
}