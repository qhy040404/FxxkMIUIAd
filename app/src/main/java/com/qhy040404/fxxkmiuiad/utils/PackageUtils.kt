package com.qhy040404.fxxkmiuiad.utils

import android.content.Context
import android.content.Intent
import android.content.pm.IPackageManager
import android.content.pm.PackageManager
import com.qhy040404.fxxkmiuiad.BuildConfig
import com.qhy040404.fxxkmiuiad.compat.PackageManagerCompat.Companion.asCompat
import org.lsposed.hiddenapibypass.HiddenApiBypass
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper

object PackageUtils {
    fun PackageManager.getApplicationEnableStateAsString(pkg: String): String {
        return when (this.getApplicationEnabledSetting(pkg)) {
            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> {
                try {
                    if (isPackageSuspended(pkg)) {
                        "禁用 (suspend)"
                    } else {
                        "启用"
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    "未安装"
                }
            }

            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED -> "禁用"
            else -> throw IllegalStateException()
        }
    }

    fun startLaunchAppActivity(context: Context, packageName: String?) {
        if (packageName == null) {
            return
        }
        val launcherActivity: String
        val intent = Intent(Intent.ACTION_MAIN, null)
            .addCategory(Intent.CATEGORY_LAUNCHER)
            .setPackage(packageName)
        val info = context.packageManager.asCompat().queryIntentActivities(intent, 0)
        launcherActivity = info.getOrNull(0)?.activityInfo?.name.orEmpty()
        val launchIntent = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)
            .setClassName(packageName, launcherActivity)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
    }

    fun setApplicationEnabledSetting(packageName: String, state: Int) {
        IPackageManager.Stub.asInterface(
            ShizukuBinderWrapper(SystemServiceHelper.getSystemService("package"))
        ).setApplicationEnabledSetting(packageName, state, 0, 0, BuildConfig.APPLICATION_ID)
    }

    fun setPackagesSuspendedAsUser(packageName: String, suspended: Boolean) {
        HiddenApiBypass.addHiddenApiExemptions("")

        IPackageManager.Stub.asInterface(
            ShizukuBinderWrapper(SystemServiceHelper.getSystemService("package"))
        ).setPackagesSuspendedAsUser(
            arrayOf(packageName),
            suspended,
            null,
            null,
            null,
            "com.android.shell",
            0
        )
    }
}