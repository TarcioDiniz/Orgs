package com.tarciodiniz.orgs.extensions

import android.content.Context
import android.content.Intent
import android.view.View


fun Context.invokeActivity(clazz: Class<*>, intent: (Intent.() -> Unit)? = null) {
    Intent(this, clazz).apply {
        intent?.invoke(this)
        startActivity(this)
    }
}