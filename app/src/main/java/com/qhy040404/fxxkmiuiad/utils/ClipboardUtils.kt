package com.qhy040404.fxxkmiuiad.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import androidx.core.content.getSystemService
import com.qhy040404.fxxkmiuiad.FxxkMIUIAdApp

val cm by lazy { FxxkMIUIAdApp.app.getSystemService<ClipboardManager>()!! }

fun Any.copyToClipboard(activity: Activity? = null) = this.toString().also {
    cm.setPrimaryClip(
        ClipData.newPlainText("", it)
    )
    activity?.runOnUiThread {
        Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
    }
}

//fun Any.copyToClipboard() = cm.setPrimaryClip(
//    ClipData.newPlainText("", this.toString())
//)