package uz.instat.tasklist.busines.util

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController


fun ViewGroup.inflater(): LayoutInflater = LayoutInflater.from(this.context)


fun Fragment.navigate(destination: NavDirections) = with(findNavController()) {
    currentDestination?.getAction(destination.actionId)
        ?.let { navigate(destination) }
}

fun View?.visible() = this?.let {
    this.visibility = View.VISIBLE
}

fun View?.invisible() = this?.let {
    this.visibility = View.INVISIBLE
}

fun View?.gone() = this?.let {
    this.visibility = View.GONE
}

fun Fragment.toast(text: String?, length: Int = Toast.LENGTH_LONG) =
    Toast.makeText(this.context, text, length).show()

fun Activity.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
fun View?.toast(text: Any) = this?.let {
    Toast.makeText(this.context, text.toString(), Toast.LENGTH_LONG).show()
}

fun View?.hideKeyboard() = this?.let {
    val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun EditText.drawableColor(@ColorRes color: Int) {
    for (drawable in this.compoundDrawables) {
        if (drawable != null) {
            drawable.colorFilter =
                PorterDuffColorFilter(
                    ContextCompat.getColor(context, color),
                    PorterDuff.Mode.SRC_IN
                )
        }
    }
}

fun View?.focusable() = this?.apply {
    isFocusable = true
    isFocusableInTouchMode = true
}

fun View?.notFocusable() = this?.let {
    isFocusable = false
    isFocusableInTouchMode = false
}

fun View?.enable() = this?.apply {
    isEnabled = true
    isClickable = true
}

fun View?.disable() = this?.apply {
    isEnabled = false
    isClickable = false
}

fun EditText.text(): String? = this.text?.toString()
