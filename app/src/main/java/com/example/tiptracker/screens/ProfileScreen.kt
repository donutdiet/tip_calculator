package com.example.tiptracker.screens

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptracker.ui.theme.TipTrackerTheme
import com.example.tiptracker.data.UserStats
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance()
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Dining Stats",
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            UserStatRow(
                label = "Total Spend",
                value = currencyFormatter.format(UserStats.totalSpend)
            )
            UserStatRow(
                label = "Total Tips",
                value = currencyFormatter.format(UserStats.totalTips)
            )
            UserStatRow(
                label = "Total Visits",
                value = UserStats.totalVisits.toString()
            )
            Spacer(modifier = Modifier.height(16.dp))
            UserStatRow(
                label = "Avg Bill",
                value = currencyFormatter.format(UserStats.averageBill)
            )
            UserStatRow(
                label = "Avg Tip",
                value = currencyFormatter.format(UserStats.averageTip)
            )
            UserStatRow(
                label = "Avg Tip Percent",
                value = String.format(Locale.getDefault(), "%.1f", UserStats.averageTipPercent) + "%"
            )
            UserStatRow(
                label = "Avg Spend",
                value = currencyFormatter.format(UserStats.averageSpend)
            )
        }
    }
}

@Composable
fun UserStatRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    TipTrackerTheme {
        ProfileScreen()
    }
}