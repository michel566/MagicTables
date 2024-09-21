package com.michelbarbosa.magictables.utils

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.Window
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


fun Fragment.closeApp() = activity?.finishAffinity()

fun Activity.setTransparencyStatusBar() = {
    ->
    val win: Window = window
    val winParams = win.attributes
    winParams.flags = winParams.flags
    win.attributes = winParams
}

fun Context.getTintDrawable(drawableRes: Int, colorRes: Int): Drawable? {
    var wrappedDrawable: Drawable? = null
    ContextCompat.getDrawable(this, drawableRes)?.let {
        DrawableCompat.wrap(it).let { wrap ->
            DrawableCompat.setTint(wrap, ContextCompat.getColor(this, colorRes))
            DrawableCompat.setTintMode(wrap, PorterDuff.Mode.SRC_IN)
            wrappedDrawable = wrap
        }
    }
    return wrappedDrawable
}

fun Context.getResDrawable(drawableRes: Int) =
    ResourcesCompat.getDrawable(resources, drawableRes, ContextThemeWrapper(this, R.style.Theme).theme)

fun Context.showColorPicker(onColorSelectedListener: (ColorEnvelope) -> Unit) {
    ColorPickerDialog.Builder(this)
        .setTitle("Escolha uma cor")
        .setPreferenceName("MyColorPickerDialog")
        .setPositiveButton(getString(R.string.ok),
            ColorEnvelopeListener { envelope, _ -> onColorSelectedListener.invoke(envelope) })
        .setNegativeButton(
            getString(R.string.cancel)
        ) { dialogInterface, i -> dialogInterface.dismiss() }
        .attachAlphaSlideBar(true) // the default value is true.
        .attachBrightnessSlideBar(true) // the default value is true.
        .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
        .show()
}