package ua.airweath.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import timber.log.Timber
import ua.airweath.MainActivity
import ua.airweath.R

@SuppressLint("ResourceAsColor", "MissingPermission")
fun setNotification(
    context: Context,
    title: String,
    message: String?,
    priority: Int,
    channelId: String,
    tag: String,
    largeIcon: Int? = null,
    style: NotificationCompat.Style? = null,
    idNotification: Int,
    silent: Boolean
) {

    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    /*val intent = Intent(context, SplashActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }*/
    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_stat_name)
        //.setColor(R.color.purple_200)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(priority)
        .setLargeIcon(largeIcon?.let { BitmapFactory.decodeResource(context.resources, it) })
        .setContentIntent(pendingIntent)
        .setStyle(style)
        .setAutoCancel(true)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setSilent(silent)
    with(NotificationManagerCompat.from(context)) {
        notify(tag, idNotification, builder.build())
    }
    Timber.d("Notification send")
}

fun removeNotification(context: Context, tag: String = "", id: Int) {
    NotificationManagerCompat.from(context).cancel(tag, id)
}

fun clearNotifications(context: Context) {
    NotificationManagerCompat.from(context).cancelAll()
}