package com.example.tiptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptracker.ui.theme.TipTrackerTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipTrackerTheme {
                TipTrackerLayout()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipTrackerLayout() {
    var billAmountInput by remember { mutableStateOf("") }
    var tipPercentInput by remember { mutableStateOf("") }
    var personCountInput by remember { mutableStateOf("") }
    var roundUpTip by remember { mutableStateOf(false) }
    var roundUpTotal by remember { mutableStateOf(false) }

    val billAmount = billAmountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipPercentInput.toDoubleOrNull() ?: 0.0
    val personCount = personCountInput.toIntOrNull() ?: 1

    val tipAmount = calculateTip(billAmount, tipPercent, roundUpTip, roundUpTotal)
    val totalAmount = calculateTotal(billAmount, tipAmount)
    val totalPerPerson = calculateTotalPerPerson(totalAmount, personCount)

    val tipString = NumberFormat.getCurrencyInstance().format(tipAmount)
    val totalString = NumberFormat.getCurrencyInstance().format(totalAmount)
    val totalPerPersonString = NumberFormat.getCurrencyInstance().format(totalPerPerson)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.tip_tracker_app_logo),
                        contentDescription = "Tip Tracker App Logo",
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            })
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
                    .safeDrawingPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.calculate_tips),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(alignment = Alignment.Start)
                        .padding(bottom = 16.dp)
                )
                EditNumberField(
                    label = R.string.bill_amount,
                    leadingIcon = R.drawable.receipt_long,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    value = billAmountInput,
                    onValueChange = { billAmountInput = it },
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    EditNumberField(
                        label = R.string.tip_percentage,
                        leadingIcon = R.drawable.percent,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        value = tipPercentInput,
                        onValueChange = { tipPercentInput = it },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    EditNumberField(
                        label = R.string.people,
                        leadingIcon = R.drawable.person,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        value = personCountInput,
                        onValueChange = { personCountInput = it },
                        modifier = Modifier.weight(1f)
                    )
                }
                RoundUpRow(
                    label = R.string.round_up_tip,
                    roundUp = roundUpTip,
                    onRoundUpChanged = {
                        roundUpTip = it
                        if (roundUpTotal) {
                            roundUpTotal = false
                        }
                    },
                    modifier = Modifier.padding(horizontal = 40.dp)
                )
                RoundUpRow(
                    label = R.string.round_up_total,
                    roundUp = roundUpTotal,
                    onRoundUpChanged = {
                        roundUpTotal = it
                        if (roundUpTip) {
                            roundUpTip = false
                        }
                    },
                    modifier = Modifier.padding(horizontal = 40.dp)
                )
                Column(
                    modifier = Modifier.padding(horizontal = 40.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.tip),
                            style = MaterialTheme.typography.displayMedium
                        )
                        Text(
                            text = stringResource(R.string.money_string, tipString),
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.total),
                            style = MaterialTheme.typography.displayMedium
                        )
                        Text(
                            text = stringResource(R.string.money_string, totalString),
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                    if(personCount > 1) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.per_person),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = stringResource(R.string.money_string, totalPerPersonString),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 8.dp)
                ) {
                    Button(
                        onClick = {
                            billAmountInput = ""
                            tipPercentInput = ""
                            personCountInput = ""
                            roundUpTip = false
                            roundUpTotal = false
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1f)
                    ) {
                        Text(text = "Clear")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1f)
                    ) {
                        Text(text = "Log")
                    }
                }
            }
        }
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        label = {
            Text(
                stringResource(label),
                style = MaterialTheme.typography.labelSmall
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(leadingIcon),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .padding(0.dp)
            )
        },
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        singleLine = true,
        modifier = modifier.focusRequester(focusRequester),
    )
}

@Composable
fun RoundUpRow(
    @StringRes label: Int,
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelSmall
        )
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

@VisibleForTesting
internal fun calculateTip(
    bill: Double,
    tipPercent: Double,
    roundUpTip: Boolean,
    roundUpTotal: Boolean,
): Double {
    var tip = tipPercent / 100 * bill
    if (roundUpTip) {
        tip = kotlin.math.ceil(tip)
    } else if (roundUpTotal) {
        val total = calculateTotal(bill, tip)
        val roundedTotal = kotlin.math.ceil(total)
        tip = tip + roundedTotal - total
    }
    return tip
}

private fun calculateTotal(bill: Double, tip: Double): Double {
    return bill + tip
}

private fun calculateTotalPerPerson(total: Double, personCount: Int): Double {
    return total / personCount
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TipTrackerPreview() {
    TipTrackerTheme {
        TipTrackerLayout()
    }
}