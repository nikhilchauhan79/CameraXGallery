package com.nikhilchauhan.cameraxcompose.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object AlertUtils {
  fun showToast(
    context: Context,
    @StringRes textRes: Int
  ) {
    Toast.makeText(context, textRes, Toast.LENGTH_LONG).show()
  }

  fun showToast(
    context: Context,
    textRes: String
  ) {
    Toast.makeText(context, textRes, Toast.LENGTH_LONG).show()
  }

}