package com.example.tiptracker.data

import androidx.annotation.DrawableRes
import com.example.tiptracker.R

enum class AppScreens {
    LogsScreen,
    HomeScreen,
    ProfileScreen,
}

data class BottomBarNavItem(
    val label: String,
    @DrawableRes val iconRes: Int,
    val route: String,
)

val navItemList = listOf(
    BottomBarNavItem(
        label = "Logs",
        iconRes = R.drawable.list,
        route = AppScreens.LogsScreen.name
    ),
    BottomBarNavItem(
        label = "New",
        iconRes = R.drawable.add,
        route = AppScreens.HomeScreen.name
    ),
    BottomBarNavItem(
        label = "Profile",
        iconRes = R.drawable.person,
        route = AppScreens.ProfileScreen.name
    )
)