package com.example.tiptracker.data

import java.util.UUID

data class DiningLogData(
    val id: UUID = UUID.randomUUID(),
    val billAmount: Double,
    val tipPercent: Double,
    val tipAmount: Double,
    val personCount: Int,
    val totalAmount: Double,
    val totalAmountPerPerson: Double,
    val restaurantName: String,
    val restaurantDescription: String,
    val date: String
)

object UserStats {
    var totalSpend: Double = 0.00
        private set
    var totalTips: Double = 0.00
        private set
    var totalVisits: Int = 0
        private set

    var averageBill: Double = 0.00
        private set
    var averageTip: Double = 0.00
        private set
    var averageTipPercent: Double = 0.00
        private set
    var averageSpend: Double = 0.00
        private set
    var averagePartySize: Double = 0.0
        private set

    fun updateUserStats(diningLogs: List<DiningLogData>) {
        val spend = diningLogs.sumOf { it.totalAmount }
        val tips = diningLogs.sumOf { it.tipAmount }
        val visits = diningLogs.size
        val avgBill = if (visits > 0) diningLogs.sumOf { it.billAmount } / visits else 0.0
        val avgTip = if (visits > 0) tips / visits else 0.0
        val avgSpend = if (visits > 0) spend / visits else 0.0
        val avgTipPercent = if (avgSpend > 0) avgTip / avgSpend * 100 else 0.0
        // cast to double to prevent integer division rounding
        val avgPartySize = if (visits > 0) diningLogs.sumOf { it.personCount } / visits.toDouble() else 0.0

        totalSpend = spend
        totalTips = tips
        totalVisits = visits
        averageBill = avgBill
        averageTip = avgTip
        averageTipPercent = avgTipPercent
        averageSpend = avgSpend
        averagePartySize = avgPartySize
    }
}