package uz.instat.tasklist.busines.enums

import uz.instat.tasklist.R
import uz.instat.tasklist.busines.util.getStringArray


enum class Errors(val code: Int) {
    WRONG_FORMAT(-6),
    REQUIRED_FIELD(-5),
    INVALID_ENUM(-4),
    NO_CONNECTION(-3),
    SERVER_ERROR(-2),
    EMPTY_DATA(-1);

    companion object {
        val Int?.error
            get() = values().find { it.code == this }

        val Errors?.message: String?
            get() = this?.let { getStringArray(R.array.errors)[ordinal] }
    }
}