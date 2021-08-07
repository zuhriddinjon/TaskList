package uz.instat.tasklist.presentation

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TaskApp : Application() {
    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate()
        appContext = this
        appResources = resources
    }

    companion object {
        lateinit var appContext: Context
        lateinit var appResources: Resources
    }
}