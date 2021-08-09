package uz.instat.tasklist.busines.enums

import uz.instat.tasklist.R
import uz.instat.tasklist.busines.util.getManualColor

enum class TaskStatus(val value: Int, val color: Int, val isChecked: Boolean) {
    CREATE(0, getManualColor(R.color.blue), false),
    IN_PROGRESS(1, getManualColor(R.color.green), false),
    FINISH(2, getManualColor(R.color.red), true);

    companion object {
        fun getByValue(value: Int) = values().find { it.value == value } ?: CREATE
    }
}