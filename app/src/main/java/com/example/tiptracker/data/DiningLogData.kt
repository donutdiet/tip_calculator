package com.example.tiptracker.data

data class DiningLogData(
    val billAmount: Double,
    val tipAmount: Double,
    val tipPercent: Double,
    val roundUpTip: Boolean,
    val roundUpTotal: Boolean,
    val personCount: Int,
    val totalAmount: Double,
    val totalAmountPerPerson: Double
)
