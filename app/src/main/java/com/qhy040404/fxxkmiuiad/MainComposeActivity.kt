package com.qhy040404.fxxkmiuiad

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.qhy040404.fxxkmiuiad.theme.DayNightTheme
import com.qhy040404.fxxkmiuiad.utils.OsUtils
import com.qhy040404.fxxkmiuiad.utils.PackageUtils
import com.qhy040404.fxxkmiuiad.utils.PackageUtils.getApplicationEnableStateAsString
import com.qhy040404.fxxkmiuiad.utils.PackageUtils.isPackageInstalled
import com.qhy040404.fxxkmiuiad.utils.ShizukuUtils
import rikka.shizuku.Shizuku

lateinit var packageList: List<String>

val trigger = mutableStateOf(false)

class MainComposeActivity : ComponentActivity() {
    private val callback = Shizuku.OnRequestPermissionResultListener { _, result ->
        if (result == PackageManager.PERMISSION_GRANTED) {
            trigger.value = !trigger.value
        } else {
            runCatching {
                PackageUtils.startLaunchAppActivity(this, SHIZUKU)
                Toast.makeText(this, "授权失败，跳转到 Shizuku 手动授权", Toast.LENGTH_LONG).show()
            }.onFailure {
                Toast.makeText(this, "未检测到 Shizuku, 请手动前往 Sui 授权", Toast.LENGTH_LONG).show()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shizuku.addRequestPermissionResultListener(callback)
        packageList = FUCKLIST.filter {
            packageManager.isPackageInstalled(it)
        }

        enableEdgeToEdge()
        setContent {
            DayNightTheme {
                Scaffold(topBar = {
                    TopAppBar(title = { Text("FxxkMIUIAd") })
                }) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), contentAlignment = Alignment.Center
                    ) {
                        val context = LocalContext.current
                        if (!OsUtils.isMiui(context)) {
                            Text(
                                "非 MIUI 或 澎湃 OS",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            return@Scaffold
                        }

                        ReloadTrigger(trigger.value)
                        val shizukuStatus = ShizukuUtils.checkStatus(context)
                        Column {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                Text(
                                    text = when (shizukuStatus) {
                                        Ok, Outdated, NotAuthorized -> "Shizuku 已运行"
                                        NotRunning, NotInstalled -> "Shizuku 未运行"
                                        else -> throw IllegalStateException()
                                    }, textAlign = TextAlign.Center
                                )

                                Text(
                                    text = when (shizukuStatus) {
                                        Ok, Outdated -> "Shizuku 已授权"
                                        NotRunning, NotAuthorized, NotInstalled -> "Shizuku 未授权"
                                        else -> throw IllegalStateException()
                                    }, textAlign = TextAlign.Center
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(300.dp, 120.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = when (shizukuStatus) {
                                    Ok -> packageList.associateWith {
                                        packageManager.getApplicationEnableStateAsString(it)
                                    }.entries.joinToString("\n") { (name, state) -> "$name: $state" }

                                    Outdated -> "Shizuku 版本过低，请更新"
                                    NotInstalled -> "Shizuku 未安装"
                                    NotRunning, NotAuthorized -> ""
                                    else -> throw IllegalStateException()
                                }, textAlign = TextAlign.Center)
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                when (shizukuStatus) {
                                    Ok -> {
                                        EnableBtn()
                                        DisableBtn()
                                    }

                                    Outdated, NotInstalled -> InstallBtn()
                                    NotRunning -> JumpBtn()
                                    NotAuthorized -> RequestPermissionBtn()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        trigger.value = !trigger.value
    }

    override fun onDestroy() {
        super.onDestroy()
        Shizuku.removeRequestPermissionResultListener(callback)
    }
}

@Composable
fun EnableBtn() {
    val context = LocalContext.current
    val showToast = remember { mutableStateOf(false) }

    Button(onClick = {
        packageList.forEach {
            PackageUtils.setApplicationEnabledSetting(it, PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
            PackageUtils.setPackagesSuspendedAsUser(it, false)
        }

        showToast.value = true
    }) {
        Text("启用")
    }

    LaunchedEffect(showToast.value) {
        if (showToast.value) {
            Toast.makeText(context, "已启用", Toast.LENGTH_SHORT).show()
            trigger.value = !trigger.value
            showToast.value = false
        }
    }
}

@Composable
fun DisableBtn() {
    val context = LocalContext.current
    val showToast = remember { mutableStateOf(false) }

    Button(onClick = {
        packageList.forEach { pkgName ->
            runCatching {
                PackageUtils.setApplicationEnabledSetting(
                    pkgName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                )
            }.onFailure {
                PackageUtils.setPackagesSuspendedAsUser(pkgName, true)
            }
        }

        showToast.value = true
    }) {
        Text("禁用")
    }

    LaunchedEffect(showToast.value) {
        if (showToast.value) {
            Toast.makeText(context, "已禁用", Toast.LENGTH_SHORT).show()
            trigger.value = !trigger.value
            showToast.value = false
        }
    }
}

@Composable
fun InstallBtn() {
    val context = LocalContext.current
    Button(onClick = {
        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = SHIZUKU_RELEASE.toUri()
            })
        }
    }) {
        Text("安装 Shizuku")
    }
}

@Composable
fun JumpBtn() {
    val context = LocalContext.current
    Button(onClick = {
        runCatching {
            PackageUtils.startLaunchAppActivity(context, SHIZUKU)
        }
    }) {
        Text("跳转到 Shizuku")
    }
}

@Composable
fun RequestPermissionBtn() {
    Button(onClick = {
        runCatching {
            Shizuku.requestPermission(0)
        }
    }) {
        Text("申请 Shizuku 授权")
    }
}

@Composable
fun ReloadTrigger(trigger: Any) {
}
