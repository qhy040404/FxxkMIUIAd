package com.qhy040404.fxxkmiuiad.utils

import android.content.Context
import android.content.Intent
import android.content.pm.IPackageManager
import android.content.pm.PackageManager
import com.qhy040404.fxxkmiuiad.R
import org.lsposed.hiddenapibypass.HiddenApiBypass
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper

object PackageUtils {
    fun PackageManager.getApplicationEnableStateAsString(pkg: String): String {
        return when (this.getApplicationEnabledSetting(pkg)) {
            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> {
                try {
                    if (isPackageSuspended(pkg)) {
                        ResourceUtils.getString(R.string.package_disabled_by_suspend)
                    } else {
                        ResourceUtils.getString(R.string.package_enabled)
                    }
                } catch (_: PackageManager.NameNotFoundException) {
                    ResourceUtils.getString(R.string.package_not_found)
                }
            }

            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED -> ResourceUtils.getString(
                R.string.package_disabled
            )

            else -> throw IllegalStateException()
        }
    }

    fun PackageManager.isPackageInstalled(pkg: String): Boolean {
        return try {
            getPackageInfo(pkg, 0) != null
        } catch (_: PackageManager.NameNotFoundException) {
            false
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
        val info = context.packageManager.queryIntentActivities(intent, 0)
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
        ).setApplicationEnabledSetting(packageName, state, 0, 0, "com.qhy040404.fxxkmiuiad")
    }

    fun setPackagesSuspendedAsUser(packageName: String, suspended: Boolean) {
        HiddenApiBypass.addHiddenApiExemptions("")

        val mInterface = IPackageManager.Stub.asInterface(
            ShizukuBinderWrapper(SystemServiceHelper.getSystemService("package"))
        )

        if (OsUtils.atLeastV()) {
            mInterface.setPackagesSuspendedAsUser(
                arrayOf(packageName),
                suspended,
                null,
                null,
                null,
                0,
                "com.android.shell",
                0,
                0
            )
        } else {
            mInterface.setPackagesSuspendedAsUser(
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
}