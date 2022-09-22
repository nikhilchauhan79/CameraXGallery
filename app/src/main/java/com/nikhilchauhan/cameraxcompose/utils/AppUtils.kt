package com.nikhilchauhan.cameraxcompose.utils

import android.content.Context
import com.nikhilchauhan.cameraxcompose.R
import com.nikhilchauhan.cameraxcompose.constants.AppConstants
import java.io.File
import java.text.SimpleDateFormat
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

  fun formatDate(time: Long): Pair<String, String> {
    val dateString: String =
      SimpleDateFormat(AppConstants.DATE_FORMAT, Locale.US).format(Date(time))
    val timeString: String =
      SimpleDateFormat(AppConstants.TIME_FORMAT, Locale.US).format(Date(time))
    return Pair(dateString, timeString)
  }
}