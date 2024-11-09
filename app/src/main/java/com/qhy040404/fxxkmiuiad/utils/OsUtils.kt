package com.qhy040404.fxxkmiuiad.utils

import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.qhy040404.fxxkmiuiad.Constants
import com.qhy040404.fxxkmiuiad.compat.PackageManagerCompat.Companion.asCompat

object OsUtils {
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    fun atLeastT(): Boolean {
        return Build.VERSION.SDK_INT >= 33
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun atLeastV(): Boolean {
        return Build.VERSION.SDK_INT >= 35
    }

    fun isMiui(context: Context): Boolean {
        return context.packageManager.asCompat().getPackageInfo(Constants.MIUI_ROM, 0) != null
    }
}