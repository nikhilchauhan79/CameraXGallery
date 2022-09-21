package com.nikhilchauhan.cameraxcompose.ui.states

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nikhilchauhan.cameraxcompose.R
import com.nikhilchauhan.cameraxcompose.constants.AppConstants

sealed class NavigationItem(
  var route: String,
  @DrawableRes var icon: Int,
  @StringRes var title: Int
) {
  object Gallery : NavigationItem(
    AppConstants.NavItemRoutes.GALLERY, R.drawable.ic_outline_photo_24, R.string.title_photos
  )

  object CapturePhoto : NavigationItem(
    AppConstants.NavItemRoutes.CAPTURE_PHOTO, R.drawable.ic_outline_add_a_photo_24,
    R.string.title_capture_photo
  )
}