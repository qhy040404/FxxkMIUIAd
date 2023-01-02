package com.qhy040404.fxxkmiuiad.compat

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.qhy040404.fxxkmiuiad.utils.OsUtils

@Suppress("DEPRECATION")
object PMCompat {
    fun getPackageInfo(pm: PackageManager, packageName: String, flags: Int): PackageInfo? {
        return runCatching {
            if (OsUtils.atLeastT()) {
                pm.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
            } else {
                pm.getPackageInfo(packageName, flags)
            }
        }.getOrNull()
    }
}