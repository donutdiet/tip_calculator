package com.example.tiptracker.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiptracker.R
import com.example.tiptracker.ui.theme.TipTrackerTheme
import com.example.tiptracker.data.UserStats
import com.example.tiptracker.data.UserStats.highestPartySizeLogId
import com.example.tiptracker.data.UserStats.highestSpendLogId
import com.example.tiptracker.data.UserStats.highestTipPercentLogId
import com.example.tiptracker.screens.logs.DiningEntry
import com.example.tiptracker.ui.LogViewModel
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

@Composable
fun ProfileScreen(
    logViewModel: LogViewModel,
    modifier: Modifier = Modifier
) {
    val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance()
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Dining Stats",
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            UserStatRow(
                label = "Total Spend",
                value = currencyFormatter.format(UserStats.totalSpend),
                modifier = Modifier.padding(horizontal = 40.dp)
            )
            UserStatRow(
                label = "Total Tips",
                value = currencyFormatter.format(UserStats.totalTips),
                modifier = Modifier.padding(horizontal = 40.dp)
            )
            UserStatRow(
                label = "Total Visits",
                value = UserStats.totalVisits.toString(),
                modifier = Modifier.padding(horizontal = 40.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            UserStatRow(
                label = "Avg Bill",
                value = currencyFormatter.format(UserStats.averageBill),
                modifier = Modifier.padding(horizontal = 40.dp)
            )
            UserStatRow(
                label = "Avg Tip",
                value = currencyFormatter.format(UserStats.averageTip),
                modifier = Modifier.padding(horizontal = 40.dp)
            )
            UserStatRow(
                label = "Avg Tip Percent",
                value = String.format(
                    Locale.getDefault(),
                    "%.1f",
                    UserStats.averageTipPercent
                ) + "%",
                modifier = Modifier.padding(horizontal = 40.dp)
            )
            UserStatRow(
                label = "Avg Spend",
                value = currencyFormatter.format(UserStats.averageSpend),
                modifier = Modifier.padding(horizontal = 40.dp)
            )
            UserStatWithIconRow(
                label = "Avg Party Size",
                value = String.format(
                    Locale.getDefault(),
                    "%.1f",
                    UserStats.averagePartySize
                ),
                imageRes = R.drawable.person,
                modifier = Modifier.padding(horizontal = 40.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            DiningLogRecord(
                recordLabel = "Most expensive dining experience",
                logViewModel = logViewModel,
                logId = highestSpendLogId,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
            )
            DiningLogRecord(
                recordLabel = "Highest tip percent given",
                logViewModel = logViewModel,
                logId = highestTipPercentLogId,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
            )
            DiningLogRecord(
                recordLabel = "Largest party size",
                logViewModel = logViewModel,
                logId = highestPartySizeLogId,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
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

@Composable
fun UserStatWithIconRow(
    label: String,
    value: String,
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(imageRes),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun DiningLogRecord(
    recordLabel: String,
    logViewModel: LogViewModel,
    logId: UUID?,
    modifier: Modifier = Modifier
) {
    val log = remember(logViewModel.diningLogs, logId) {
        logViewModel.diningLogs.find { it.id == logId }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = recordLabel,
            style = MaterialTheme.typography.labelMedium
        )
        if (log != null) {
            DiningEntry(log = log, modifier = Modifier.padding(vertical = 8.dp))
        } else {
            Text(
                text = "No data available",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    TipTrackerTheme {
        ProfileScreen(
            logViewModel = viewModel()
        )
    }
}