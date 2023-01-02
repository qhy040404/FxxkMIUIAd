package com.qhy040404.fxxkmiuiad.utils

import android.content.pm.PackageManager

object PackageUtils {
    fun PackageManager.getApplicationEnableStateAsString(pkg: String): String {
        return when (this.getApplicationEnabledSetting(pkg)) {
            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> "启用"
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED -> "禁用"
            else -> throw IllegalStateException()
        }
    }
}