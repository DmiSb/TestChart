package ru.dmisb.testchart.data.model

import java.util.*

class ChartData(
    val history: List<History>,
    val data: List<Data>,
    val kim: Double,
    val total: Int,
    val totalPeriod: Int,
    val timeStep: String
)

class History(
    val prodline_id: Int,
    val prodline_title: String,
    val data: List<Data>,
    val kim: Double,
    val total: Int,
    val totalWork: Int,
    val totalPeriod: Int
)

class Data(
    val time: Date,
    val value: Double,
    val total: Int,
    val totalPeriod: Int
)