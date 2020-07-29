package me.sujanpoudel.mvidemo

import android.app.NotificationManager
import android.os.Build
import org.threeten.bp.format.DateTimeFormatter

object Config {
    const val DATABASE_NAME = "${BuildConfig.APPLICATION_ID}.room.db"
    const val DATABASE_VERSION = 1
    val DISPLAY_DATE_TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM yyyy, hh:mm a")
}


