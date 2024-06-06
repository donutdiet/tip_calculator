package com.example.tiptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptracker.ui.theme.TipTrackerTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TipTrackerLayout()
                }
            }
        }
    }
}

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.calculate_tip),
            modifier = Modifier
                .padding(top = 40.dp, bottom = 16.dp)
                .align(alignment = Alignment.Start)
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
                .padding(bottom = 20.dp)
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
                modifier = Modifier.weight(4f)
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
                modifier = Modifier.weight(3f)
            )
        }
        RoundUpRow(
            label = R.string.round_up_tip,
            roundUp = roundUpTip,
            onRoundUpChanged = {
                roundUpTip = it
                if(roundUpTotal) {
                    roundUpTotal = false
                }
            },
        )
        RoundUpRow(
            label = R.string.round_up_total,
            roundUp = roundUpTotal,
            onRoundUpChanged = {
                roundUpTotal = it
                if(roundUpTip) {
                    roundUpTip = false
                }
            },
            modifier = Modifier.padding(bottom = 28.dp)
        )
        Text(
            text = stringResource(R.string.tip_amount, tipString),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Text(
            text = stringResource(R.string.total_amount, totalString),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        if(personCount > 1) {
            Text(
                text = stringResource(R.string.per_person_amount, totalPerPersonString),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        }
//        Spacer(modifier = Modifier.height(100.dp))
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
                fontSize = 14.sp
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
            text = stringResource(label)
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

private fun calculateTip(
    bill: Double,
    tipPercent: Double,
    roundUpTip: Boolean,
    roundUpTotal: Boolean,
): Double {
    var tip = tipPercent / 100 * bill
    if(roundUpTip) {
        tip = kotlin.math.ceil(tip)
    } else if(roundUpTotal) {
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

@Preview(showBackground = true)
@Composable
fun TipTrackerPreview() {
    TipTrackerTheme {
        TipTrackerLayout()
    }
}