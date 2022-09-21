package com.nikhilchauhan.cameraxcompose.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.nikhilchauhan.cameraxcompose.constants.AppConstants

object PermissionsUtils {
  val PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.READ_EXTERNAL_STORAGE,
  )

  fun checkPermissions(context: Context): Boolean {
    for (permission in PERMISSIONS) {
      if (ContextCompat.checkSelfPermission(
          context,
          permission

        ) == PackageManager.PERMISSION_GRANTED
      ) {
        Log.d("TAG", "checkPermissions: permissions already granted")
      } else {
        return false
      }
    }
    return true
  }
}