package com.example.tiptracker.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiptracker.R
import com.example.tiptracker.data.DiningLogData
import com.example.tiptracker.ui.LogViewModel
import com.example.tiptracker.ui.theme.TipTrackerTheme
import java.text.NumberFormat

@Composable
fun DiningLogsScreen(
    modifier: Modifier = Modifier,
    diningLogs: List<DiningLogData>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: LogViewModel
) {
    if (viewModel.diningLogs.size > 0) {
        LazyColumn(
            contentPadding = contentPadding,
            modifier = modifier
        ) {
            itemsIndexed(diningLogs) { index, log ->
                DiningEntry(
                    log = log,
                    index = index,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
                    viewModel = viewModel
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nothing logged",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun DiningEntry(
    log: DiningLogData,
    index: Int,
    modifier: Modifier = Modifier,
    viewModel: LogViewModel
) {
    val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance()
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Row {
                        Text(
                            text = log.restaurantName,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "Total: "
                            )
                            Text(
                                text = currencyFormatter.format(log.totalAmount),
                                modifier = Modifier.widthIn(min = 60.dp),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = log.date,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Icon(
                            painter = painterResource(R.drawable.person),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                        )
                        Text(
                            text = log.personCount.toString()
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "Tip: "
                            )
                            Text(
                                text = currencyFormatter.format(log.tipAmount),
                                modifier = Modifier.widthIn(min = 60.dp),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
                IconButton(
                    onClick = { viewModel.deleteEntry(index) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete entry button",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            if (!expanded) {
                Text(
                    text = log.restaurantDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(
                        start = 4.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 4.dp
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiningEntryPreview() {
    TipTrackerTheme {
        DiningEntry(
            log = DiningLogData(
                billAmount = 100.12,
                tipAmount = 2.01,
                tipPercent = 20.00,
                roundUpTip = true,
                roundUpTotal = false,
                personCount = 2,
                totalAmount = 100.12,
                totalAmountPerPerson = 35.96,
                restaurantName = "Sesame Sea Asian Bistro",
                restaurantDescription = "Very nice food w/ good service. Some options were not halal though, so be aware. Alhamdulillah",
                date = "June 24, 2024"
            ),
            index = 0,
            viewModel = viewModel()
        )
    }
}