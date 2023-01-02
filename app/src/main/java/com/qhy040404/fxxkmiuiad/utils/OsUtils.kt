package com.qhy040404.fxxkmiuiad.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

object OsUtils {
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    fun atLeastT(): Boolean {
        return Build.VERSION.SDK_INT >= 33
    }
}