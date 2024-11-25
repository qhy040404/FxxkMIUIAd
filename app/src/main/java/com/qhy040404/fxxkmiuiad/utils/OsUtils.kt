package com.qhy040404.fxxkmiuiad.utils

import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.qhy040404.fxxkmiuiad.MIUI_ROM
import com.qhy040404.fxxkmiuiad.utils.PackageUtils.isPackageInstalled

object OsUtils {
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun atLeastV(): Boolean {
        return Build.VERSION.SDK_INT >= 35
    }

    fun isMiui(context: Context): Boolean {
        return context.packageManager.isPackageInstalled(MIUI_ROM)
    }
}