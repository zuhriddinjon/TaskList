package uz.instat.tasklist.framework.services.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import uz.instat.tasklist.R
import uz.instat.tasklist.busines.local.TaskLocal
import uz.instat.tasklist.framework.repo.MainRepository
import uz.instat.tasklist.presentation.ui.MainActivity
import javax.inject.Inject


const val TASK_NAME = "task_name"
const val TASK_BUNDLE = "task_bundle"

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: MainRepository

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {
        val taskBundle = intent.getParcelableExtra<Bundle>(TASK_BUNDLE)
        val task = taskBundle?.getParcelable(TASK_NAME) as TaskLocal?

        // Intent to launch the application when you click on notification
        val resultIntent = Intent(context, MainActivity::class.java)
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            task!!.id.toInt(),
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Set NotificationChannel for Android Oreo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationChannel = notificationManager.getNotificationChannel(
                NOTIFICATION_CHANNEL_ID
            )

            if (notificationChannel == null) {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.notification_channel),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableLights(true)
                    lightColor = Color.BLUE
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)
            }
        }

        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setContentTitle(task.title)
            setContentText(task.description)
            setStyle(NotificationCompat.BigTextStyle().bigText(task.description))
            color = ContextCompat.getColor(context, R.color.teal_700)
            setSmallIcon(R.drawable.ic_alarm)
            setDefaults(Notification.DEFAULT_ALL)
            setContentIntent(pendingIntent)
            setAutoCancel(false)
            setSound(soundUri)
            setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

        }
        notificationManager.notify(task.id.toInt(), notification.build())

        goAsync(GlobalScope, Dispatchers.Default) {
            val currentTime = System.currentTimeMillis()
            if (currentTime >= task.time)
                repository.inProgressTask(task.id).collect()
        }

    }

    private fun BroadcastReceiver.goAsync(
        coroutineScope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        block: suspend () -> Unit
    ) {
        val pendingResult = goAsync()
        coroutineScope.launch(dispatcher) {
            block()
            pendingResult.finish()
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1"
        const val GENERAL_NOTIFICATION_CHANNEL_ID = "2"
    }
}