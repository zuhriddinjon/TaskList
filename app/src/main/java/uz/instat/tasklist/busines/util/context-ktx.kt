package uz.instat.tasklist.busines.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import uz.instat.tasklist.presentation.TaskApp

fun getString(@StringRes id: Int) = TaskApp.appResources.getString(id)
fun getStringArray(@ArrayRes id: Int): Array<String> = TaskApp.appResources.getStringArray(id)

fun getIntArray(@ArrayRes id: Int): IntArray = TaskApp.appResources.getIntArray(id)

@ColorInt
fun getManualColor(@ColorRes id: Int): Int = TaskApp.appContext.let {
    return ContextCompat.getColor(TaskApp.appContext, id)
}

fun Context.toast(text: String) {
    val toast = Toast.makeText(this, text, Toast.LENGTH_LONG)
    toast.setText(text)
    toast.duration = Toast.LENGTH_LONG
    toast.show()
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}


fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.getBitmapFromDrawable(@DrawableRes drawableId: Int): Bitmap {
    val drawable = AppCompatResources.getDrawable(this, drawableId)
    return if (drawable is BitmapDrawable) {
        (drawable as BitmapDrawable).bitmap
    } else if (drawable is VectorDrawableCompat || drawable is VectorDrawable) {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    } else {
        throw IllegalArgumentException("unsupported drawable type")
    }
}
