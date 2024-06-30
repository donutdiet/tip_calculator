package com.example.tiptracker.screens.logs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.tiptracker.R
import com.example.tiptracker.data.DiningLogData
import com.example.tiptracker.ui.EditLogViewModel
import com.example.tiptracker.ui.LogViewModel
import com.example.tiptracker.ui.theme.TipTrackerTheme
import kotlinx.coroutines.launch
import java.text.NumberFormat
import kotlin.math.roundToInt

private enum class HorizontalDragValue { Settled, StartToEnd, EndToStart }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiningLogsScreen(
    modifier: Modifier = Modifier,
    diningLogs: List<DiningLogData>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    logViewModel: LogViewModel,
    editLogViewModel: EditLogViewModel,
    onEditButtonClicked: () -> Unit,
) {
    if (logViewModel.diningLogs.size > 0) {
        LazyColumn(
            contentPadding = contentPadding,
            modifier = modifier
        ) {
            itemsIndexed(diningLogs) { index, log ->
                var boxSize by remember { mutableFloatStateOf(0F) }
                val scope = rememberCoroutineScope()
                val anchors = DraggableAnchors {
                    HorizontalDragValue.Settled at 0f
                    HorizontalDragValue.StartToEnd at boxSize / 3
                    HorizontalDragValue.EndToStart at -boxSize * 2 / 5
                }
                val state = remember {
                    AnchoredDraggableState(
                        initialValue = HorizontalDragValue.Settled,
                        positionalThreshold = { distance -> distance * 0.3f },
                        velocityThreshold = { 0.3f },
                        animationSpec = tween()
                    )
                }
                SideEffect { state.updateAnchors(anchors) }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .animateItemPlacement()
                ) {
                    var cardHeight by remember { mutableIntStateOf(0) }

                    Box(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .height(with(LocalDensity.current) { cardHeight.toDp() } - 8.dp)
                            .fillMaxWidth(0.35f)
                            .background(MaterialTheme.colorScheme.primary)
                            .align(Alignment.CenterStart)
                            .clickable {
                                scope.launch { state.animateTo(HorizontalDragValue.Settled) }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .height(with(LocalDensity.current) { cardHeight.toDp() } - 8.dp)
                            .fillMaxWidth(0.4f)
                            .align(Alignment.CenterEnd),
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.tertiary)
                                .clickable {
                                    editLogViewModel.loadCurrentLogData(
                                        diningLogs = diningLogs,
                                        index = index
                                    )
                                    onEditButtonClicked()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.error)
                                .clickable {
                                    scope.launch { state.animateTo(HorizontalDragValue.Settled) }
                                    logViewModel.deleteEntry(index)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .graphicsLayer { boxSize = size.width }
                            .offset {
                                IntOffset(
                                    x = state
                                        .requireOffset()
                                        .roundToInt(), y = 0
                                )
                            }
                            .fillMaxWidth()
                            .anchoredDraggable(state, Orientation.Horizontal)
                            .onGloballyPositioned { coordinates ->
                                cardHeight = coordinates.size.height
                            }
                    ) {
                        DiningEntry(
                            log = log,
                            modifier = Modifier.padding(vertical = 4.dp),
                        )
                    }
                }
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
    modifier: Modifier = Modifier,
) {
    val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance()
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Row {
                        Text(
                            text = log.restaurantName,
                            style = MaterialTheme.typography.labelMedium,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "Total:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = currencyFormatter.format(log.totalAmount),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.widthIn(min = 64.dp),
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
                            modifier = Modifier.padding(start = 8.dp, end = 2.dp)
                        )
                        Text(
                            text = log.personCount.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "Tip:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = currencyFormatter.format(log.tipAmount),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.widthIn(min = 64.dp),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
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
                personCount = 2,
                totalAmount = 100.12,
                totalAmountPerPerson = 35.96,
                restaurantName = "Sesame Sea Asian Bistro",
                restaurantDescription = "Very nice food w/ good service. Some options were not halal though, so be aware. Alhamdulillah",
                date = "June 24, 2024"
            )
        )
    }
}