package com.nikhilchauhan.cameraxcompose.utils

import android.content.Context
import android.util.Log
import com.nikhilchauhan.cameraxcompose.R
import com.nikhilchauhan.cameraxcompose.constants.AppConstants
import java.io.File
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale

object AppUtils {
  fun getOutputDirectory(context: Context): File {
    val appContext = context.applicationContext
    val mediaDir = context.externalMediaDirs.firstOrNull()?.let { nnFile ->
      File(nnFile, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
      mediaDir else appContext.filesDir
  }

  fun formatDate(time: Long): String {
    val current = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime()

    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    val formattedDate = current.format(formatter)
    Log.d("TAG", "formatDate: $formattedDate")
    val dateString: String =
      SimpleDateFormat(AppConstants.DATE_FORMAT, Locale.US).format(Date(time))

    return formattedDate ?: dateString
  }
}