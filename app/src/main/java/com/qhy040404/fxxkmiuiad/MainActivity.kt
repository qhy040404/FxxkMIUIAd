package com.qhy040404.fxxkmiuiad

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.qhy040404.fxxkmiuiad.base.BaseActivity
import com.qhy040404.fxxkmiuiad.databinding.ActivityMainBinding
import com.qhy040404.fxxkmiuiad.utils.OsUtils
import com.qhy040404.fxxkmiuiad.utils.PackageUtils
import com.qhy040404.fxxkmiuiad.utils.PackageUtils.getApplicationEnableStateAsString
import com.qhy040404.fxxkmiuiad.utils.ShizukuStatus
import com.qhy040404.fxxkmiuiad.utils.ShizukuUtils
import rikka.shizuku.Shizuku
import kotlin.concurrent.thread

@SuppressLint("SetTextI18n")
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val callback = Shizuku.OnRequestPermissionResultListener { _, result ->
        if (result == PackageManager.PERMISSION_GRANTED) {
            this@MainActivity.refreshView()
        } else {
            runCatching {
                PackageUtils.startLaunchAppActivity(this, Constants.SHIZUKU)
                Toast.makeText(this, "授权失败，跳转到 Shizuku 手动授权", Toast.LENGTH_LONG).show()
            }.onFailure {
                Toast.makeText(this, "未检测到 Shizuku, 请手动前往 Sui 授权", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun init() {
        Shizuku.addRequestPermissionResultListener(callback)
        initView()
    }

    override fun onResume() {
        super.onResume()
        refreshView()
    }

    override fun onDestroy() {
        Shizuku.removeRequestPermissionResultListener(callback)
        super.onDestroy()
    }

    private fun refreshView() {
        if (!OsUtils.isMiui(this)) {
            binding.info.text = "非 MIUI 或 澎湃 OS"
            binding.enableBtn.apply {
                isClickable = false
                isVisible = false
            }
            binding.disableBtn.apply {
                isClickable = false
                isVisible = false
            }
            return
        }

        when (ShizukuUtils.checkStatus(this)) {
            ShizukuStatus.Ok -> {
                binding.runningInfo.text = "Shizuku 已运行"
                binding.permittedInfo.text = "Shizuku 已授权"

                binding.enableBtn.apply {
                    isClickable = true
                    isVisible = true
                }
                binding.disableBtn.apply {
                    isClickable = true
                    isVisible = true
                }

                binding.installBtn.isVisible = false
                binding.jumpBtn.isVisible = false
                binding.requestPermissionBtn.isVisible = false

                val states = mutableMapOf<String, String>()

                Constants.FUCKLIST.forEach {
                    states[it] = packageManager.getApplicationEnableStateAsString(it)
                }

                binding.info.text = buildString {
                    states.forEach { (name, state) ->
                        append("${name}: ${state}\n")
                    }
                }
            }

            ShizukuStatus.Outdated -> {
                binding.runningInfo.text = "Shizuku 已运行"
                binding.permittedInfo.text = "Shizuku 已授权"
                binding.info.text = "Shizuku 版本过低，请更新"

                binding.enableBtn.apply {
                    isClickable = false
                    isVisible = false
                }
                binding.disableBtn.apply {
                    isClickable = false
                    isVisible = false
                }

                binding.installBtn.isVisible = true
                binding.jumpBtn.isVisible = false
                binding.requestPermissionBtn.isVisible = false
            }

            ShizukuStatus.NotRunning -> {
                binding.runningInfo.text = "Shizuku 未运行"
                binding.permittedInfo.text = "Shizuku 未授权"

                binding.enableBtn.apply {
                    isClickable = false
                    isVisible = false
                }
                binding.disableBtn.apply {
                    isClickable = false
                    isVisible = false
                }

                binding.installBtn.isVisible = false
                binding.jumpBtn.isVisible = true
                binding.requestPermissionBtn.isVisible = false
            }

            ShizukuStatus.NotAuthorized -> {
                binding.runningInfo.text = "Shizuku 已运行"
                binding.permittedInfo.text = "Shizuku 未授权"

                binding.enableBtn.apply {
                    isClickable = false
                    isVisible = false
                }
                binding.disableBtn.apply {
                    isClickable = false
                    isVisible = false
                }

                binding.installBtn.isVisible = false
                binding.jumpBtn.isVisible = false
                binding.requestPermissionBtn.isVisible = true
            }

            ShizukuStatus.NotInstalled -> {
                binding.runningInfo.text = "Shizuku 未运行"
                binding.permittedInfo.text = "Shizuku 未授权"
                binding.info.text = "Shizuku 未安装"

                binding.enableBtn.apply {
                    isClickable = false
                    isVisible = false
                }
                binding.disableBtn.apply {
                    isClickable = false
                    isVisible = false
                }

                binding.installBtn.isVisible = true
                binding.jumpBtn.isVisible = false
                binding.requestPermissionBtn.isVisible = false
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.installBtn.setOnClickListener {
            runCatching { startActivity(Intent(Intent.ACTION_VIEW).apply { data = Constants.SHIZUKU_RELEASE.toUri() }) }
        }
        binding.jumpBtn.setOnClickListener {
            runCatching { PackageUtils.startLaunchAppActivity(this, Constants.SHIZUKU) }
        }
        binding.requestPermissionBtn.setOnClickListener {
            runCatching { Shizuku.requestPermission(0) }
        }
        binding.enableBtn.setOnClickListener {
            Constants.FUCKLIST.forEach {
                PackageUtils.setApplicationEnabledSetting(it, PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
            }
            thread {
                Thread.sleep(200L)
                runOnUiThread {
                    Toast.makeText(this, "已启用", Toast.LENGTH_SHORT).show()
                    refreshView()
                }
            }
        }
        binding.disableBtn.setOnClickListener {
            Constants.FUCKLIST.forEach {
                PackageUtils.setApplicationEnabledSetting(it, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER)
            }
            thread {
                Thread.sleep(200L)
                runOnUiThread {
                    Toast.makeText(this, "已禁用", Toast.LENGTH_SHORT).show()
                    refreshView()
                }
            }
        }
    }
}