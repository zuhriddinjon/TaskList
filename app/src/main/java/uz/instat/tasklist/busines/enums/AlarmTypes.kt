package uz.instat.tasklist.busines.enums

import uz.instat.tasklist.R
import uz.instat.tasklist.busines.util.getString


enum class AlarmTypes(val title: String, val value: Long) {
    BEFORE_30(getString(R.string._30_minutes_before), 30 * 60 * 1000),
    BEFORE_15(getString(R.string._15_minutes_before), 15 * 60 * 1000),
    BEFORE_10(getString(R.string._10_minutes_before), 10 * 60 * 1000),
    BEFORE_5(getString(R.string._5_minutes_before), 5 * 60 * 1000),
    BEFORE_0(getString(R.string._0_minutes_before), 0 * 60 * 1000);

    companion object {
        fun getByValue(value: Long) = values().find { it.value == value } ?: BEFORE_30
        fun getByTitle(title: String) = values().find { it.title == title } ?: BEFORE_30
    }
}