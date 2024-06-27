package com.example.tiptracker.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tiptracker.data.DiningLogData
import com.example.tiptracker.data.UserStats
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LogViewModel : ViewModel() {
    var diningLogs = mutableStateListOf<DiningLogData>()
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        // will sometimes cause the app to break i will figure out why when i learn more abt databases
        loadLogs()
        UserStats.updateUserStats(diningLogs)
    }

    private fun saveLogs() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(diningLogs)
        editor.putString("dining_logs", json)
        editor.apply()
    }

    private fun loadLogs() {
        val gson = Gson()
        val json = sharedPreferences.getString("dining_logs", "")
        if(!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<DiningLogData>>() {}.type
            val logs: List<DiningLogData> = gson.fromJson(json, type)
            diningLogs.clear()
            diningLogs.addAll(logs)
        }
    }

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
    var restaurantName = mutableStateOf("")
        private set
    var restaurantDescription = mutableStateOf("")
        private set
    var date = mutableStateOf("")
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

    fun onRestaurantNameChange(newName: String) {
        restaurantName.value = newName
    }

    fun onRestaurantDescriptionChange(newDescription: String) {
        restaurantDescription.value = newDescription
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

    fun getCalculatedTotal(): Double {
        val billAmount = billAmount.value.toDoubleOrNull() ?: 0.0
        val tip = getCalculatedTip()
        return billAmount + tip
    }

    fun getCalculatedTotalPerPerson(): Double {
        val total = getCalculatedTotal()
        val personCount = personCount.value.toIntOrNull() ?: 1
        return total / personCount
    }

    fun checkFormValidity(): Boolean {
        val billAmountDouble = billAmount.value.toDoubleOrNull() ?: 0.0
        val tipPercentDouble = tipPercent.value.toDoubleOrNull() ?: 0.0
        // Ensure party size is an integer; default to 1 if left empty
        var partySize = personCount.value.toIntOrNull()
        if(personCount.value.isEmpty()) partySize = 1
        return billAmountDouble >= 0.0 && tipPercentDouble >= 0.0 && partySize != null
    }

    fun clearForm() {
        billAmount.value = ""
        tipPercent.value = ""
        personCount.value = ""
        roundUpTip.value = false
        roundUpTotal.value = false
        restaurantName.value = ""
        restaurantDescription.value = ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun logEntry() {
        if(date.value.isEmpty()) {
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
            date.value = currentDate.format(formatter)
        }
        val billAmountDouble = billAmount.value.toDoubleOrNull() ?: 0.0
        val tipPercentDouble = tipPercent.value.toDoubleOrNull() ?: 0.0
        val tipAmountDouble = getCalculatedTip()
        val totalAmountDouble = getCalculatedTotal()
        val totalPerPersonDouble = getCalculatedTotalPerPerson()
        val diningEntry = DiningLogData(
            billAmount = billAmountDouble,
            tipPercent = tipPercentDouble, // Assuming tipPercent should be a Double
            tipAmount = tipAmountDouble,
            roundUpTip = roundUpTip.value,
            roundUpTotal = roundUpTotal.value,
            personCount = personCount.value.toIntOrNull() ?: 1,
            totalAmount = totalAmountDouble,
            totalAmountPerPerson = totalPerPersonDouble,
            restaurantName = restaurantName.value,
            restaurantDescription = restaurantDescription.value,
            date = date.value
        )
        diningLogs.add(0, diningEntry)
        saveLogs()
        UserStats.updateUserStats(diningLogs)
        clearForm()
    }

    fun deleteEntry(index: Int) {
        diningLogs.removeAt(index)
        saveLogs()
        UserStats.updateUserStats(diningLogs)
    }
}