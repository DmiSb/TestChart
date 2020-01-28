package ru.dmisb.testchart.data.model

enum class TimeStep(val value: String, val formatPattern: String) {
    HOUR ("hour", "HH:mm"),
    DAY ( "day", "dd.MM")
}