package com.nikhilchauhan.cameraxcompose.utils

import android.content.Context
import com.nikhilchauhan.cameraxcompose.R
import java.io.File

object AppUtils {
  fun getOutputDirectory(context: Context): File {
    val appContext = context.applicationContext
    val mediaDir = context.externalMediaDirs.firstOrNull()?.let { nnFile ->
      File(nnFile, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
      mediaDir else appContext.filesDir
  }
}