package com.qhy040404.fxxkmiuiad.utils

import androidx.annotation.StringRes
import com.qhy040404.fxxkmiuiad.FxxkMIUIAdApp

object ResourceUtils {
    fun getString(@StringRes resId: Int): String {
        return FxxkMIUIAdApp.app.getString(resId)
    }
}