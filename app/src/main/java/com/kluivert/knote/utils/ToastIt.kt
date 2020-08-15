package com.kluivert.knote.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast

private var toast: Toast? = null

inline fun <reified T : Activity> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}

fun Context.toastIt(message: String) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        .apply { show() }
}