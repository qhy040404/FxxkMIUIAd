package com.qhy040404.fxxkmiuiad.utils

import android.content.Context
import android.content.pm.PackageManager
import com.qhy040404.fxxkmiuiad.NotAuthorized
import com.qhy040404.fxxkmiuiad.NotInstalled
import com.qhy040404.fxxkmiuiad.NotRunning
import com.qhy040404.fxxkmiuiad.Ok
import com.qhy040404.fxxkmiuiad.Outdated
import com.qhy040404.fxxkmiuiad.SHIZUKU
import com.qhy040404.fxxkmiuiad.utils.PackageUtils.isPackageInstalled
import rikka.shizuku.Shizuku
import rikka.sui.Sui

object ShizukuUtils {
    fun checkStatus(context: Context): Int {
        if (!context.packageManager.isPackageInstalled(SHIZUKU) && !Sui.isSui()) return NotInstalled
        if (!Shizuku.pingBinder()) return NotRunning
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission("moe.shizuku.manager.permission.API_V23") != PackageManager.PERMISSION_GRANTED) return NotAuthorized
        if (Shizuku.getVersion() < 10) return Outdated

        return Ok
    }
}

