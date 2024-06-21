package com.example.tiptracker.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tiptracker.data.DiningLogData
import kotlin.math.round

class LogViewModel : ViewModel() {
    var diningLogs =  mutableStateListOf<DiningLogData>()

    var billAmount = mutableStateOf("")
        private set
    var tipPercent = mutableStateOf("")
        private set
    var personCount = mutableStateOf("")
        private set
    var roundUpTip = mutableStateOf(false)
        private set
    var roundUpTotal = mutableStateOf(false)
        private set

    fun onBillAmountChange(newAmount: String) {
        billAmount.value = newAmount
    }

    fun onTipPercentChange(newPercent: String) {
        tipPercent.value = newPercent
    }

    fun onPersonCountChange(newCount: String) {
        personCount.value = newCount
    }

    fun onRoundUpTipChange(newOption: Boolean) {
        roundUpTip.value = newOption
        if(newOption) {
            roundUpTotal.value = false
        }
    }

    fun onRoundUpTotalChange(newOption: Boolean) {
        roundUpTotal.value = newOption
        if(newOption) {
            roundUpTip.value = false
        }
    }

    fun getCalculatedTip(): Double {
        val billAmount = billAmount.value.toDoubleOrNull() ?: 0.0
        val tipPercent = tipPercent.value.toDoubleOrNull() ?: 0.0
        var tip = billAmount * tipPercent / 100
        if(roundUpTip.value) {
            tip = kotlin.math.ceil(tip)
        } else if(roundUpTotal.value) {
            val total = billAmount + tip
            val roundedTotal = kotlin.math.ceil(total)
            tip = tip + roundedTotal - total
        }
        return tip
    }

    fun getCalculatedTotal():Double {
        val billAmount = billAmount.value.toDoubleOrNull() ?: 0.0
        val tip = getCalculatedTip()
        return billAmount + tip
    }

    fun getCalculatedTotalPerPerson(): Double {
        val total = getCalculatedTotal()
        val personCount = personCount.value.toIntOrNull() ?: 1
        return total / personCount
    }

    fun clearForm() {
        billAmount.value = ""
        tipPercent.value = ""
        personCount.value = ""
        roundUpTotal.value = false
        roundUpTotal.value = false
    }

    fun logEntry() {
        val billAmount = billAmount.value.toDoubleOrNull() ?: 0.0
        val tipPercent = tipPercent.value.toDoubleOrNull() ?: 0.0
        val personCount = personCount.value.toIntOrNull() ?: 1
        val diningEntry = DiningLogData(
            billAmount = billAmount,
            tipAmount = getCalculatedTip(),
            tipPercent = tipPercent,
            roundUpTip = roundUpTip.value,
            roundUpTotal = roundUpTotal.value,
            personCount = personCount,
            totalAmount = getCalculatedTotal(),
            totalAmountPerPerson = getCalculatedTotalPerPerson()
        )
        diningLogs.add(diningEntry)
        clearForm()
    }
}