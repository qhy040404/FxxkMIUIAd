package com.qhy040404.fxxkmiuiad.compat

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.qhy040404.fxxkmiuiad.utils.OsUtils

class PackageManagerCompat(private val packageManager: PackageManager) {
    fun getPackageInfo(packageName: String, flags: Int): PackageInfo? {
        return runCatching {
            if (OsUtils.atLeastT()) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
            } else {
                packageManager.getPackageInfo(packageName, flags)
            }
        }.getOrNull()
    }

    fun queryIntentActivities(intent: Intent, flags: Int): List<ResolveInfo> {
        return if (OsUtils.atLeastT()) {
            packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(flags.toLong())
            )
        } else {
            packageManager.queryIntentActivities(intent, flags)
        }
    }

    companion object {
        fun PackageManager.asCompat(): PackageManagerCompat {
            return PackageManagerCompat(this)
        }
    }
}