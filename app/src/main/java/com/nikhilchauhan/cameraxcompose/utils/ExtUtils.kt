package com.nikhilchauhan.cameraxcompose.utils

import android.view.View

object ExtUtils {
  fun View.show(){
    visibility = View.VISIBLE
  }

  fun View.hide(){
    visibility = View.GONE
  }
}