package com.qhy040404.fxxkmiuiad.utils

import android.content.Context
import android.content.pm.PackageManager
import com.qhy040404.fxxkmiuiad.Constants
import com.qhy040404.fxxkmiuiad.compat.PackageManagerCompat.Companion.asCompat
import rikka.shizuku.Shizuku
import rikka.sui.Sui

object ShizukuUtils {
    fun checkStatus(context: Context): ShizukuStatus {
        if (context.packageManager.asCompat().getPackageInfo(
                Constants.SHIZUKU,
                0
            ) == null && !Sui.isSui()
        ) return ShizukuStatus.NotInstalled
        if (!Shizuku.pingBinder()) return ShizukuStatus.NotRunning
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission("moe.shizuku.manager.permission.API_V23") != PackageManager.PERMISSION_GRANTED) return ShizukuStatus.NotAuthorized
        if (Shizuku.getVersion() < 10) return ShizukuStatus.Outdated

        return ShizukuStatus.Ok
    }
}

enum class ShizukuStatus {
    Ok,
    Outdated,
    NotRunning,
    NotAuthorized,
    NotInstalled
}