package com.qhy040404.fxxkmiuiad

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.qhy040404.fxxkmiuiad.compat.PMCompat
import com.qhy040404.fxxkmiuiad.utils.PackageUtils.getApplicationEnableStateAsString
import rikka.shizuku.Shizuku
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    var running = true
    var permitted = false
    private val callback = Shizuku.OnRequestPermissionResultListener { _, _ ->
        this@MainActivity.check()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Shizuku.addRequestPermissionResultListener(callback)
        check()
        initView()
    }

    override fun onDestroy() {
        Shizuku.removeRequestPermissionResultListener(callback)
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val tv = findViewById<TextView>(R.id.tv)
        val enable = findViewById<Button>(R.id.enable)
        val disable = findViewById<Button>(R.id.disable)
        val install = findViewById<Button>(R.id.install)
        val runningTv = findViewById<TextView>(R.id.running)
        val permittedTv = findViewById<TextView>(R.id.permitted)

        check()

        runningTv.text = if (running) {
            "Shizuku 已运行"
        } else {
            "Shizuku 未运行"
        }
        permittedTv.text = if (permitted) {
            "Shizuku 已授权"
        } else {
            "Shizuku 未授权"
        }

        val a = PMCompat.getPackageInfo(packageManager, Constants.shizuku, 0)

        if (PMCompat.getPackageInfo(packageManager, Constants.shizuku, 0) == null) {
            tv.text = "Shizuku 未安装"
            enable.apply {
                visibility = View.INVISIBLE
                isClickable = false
            }
            disable.apply {
                visibility = View.INVISIBLE
                isClickable = false
            }
            install.visibility = View.VISIBLE
            install.setOnClickListener {
                if (PMCompat.getPackageInfo(packageManager, Constants.coolapk, 0) != null) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Constants.shizuku_coolapk.toUri()
                    }
                    runCatching { startActivity(intent) }
                } else {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Constants.shizuku_coolapk_url.toUri()
                    }
                    runCatching { startActivity(intent) }
                }
            }
            return
        }

        if (PMCompat.getPackageInfo(packageManager, Constants.ad, 0) == null) {
            tv.text = "Not MIUI"
            enable.isClickable = false
            disable.isClickable = false
            return
        }
        val hybridState = packageManager.getApplicationEnableStateAsString(Constants.hybrid)
        val adState = packageManager.getApplicationEnableStateAsString(Constants.ad)
        tv.text = """
            ${Constants.ad}: $adState
            ${Constants.hybrid}: $hybridState
        """.trimIndent()

        enable.setOnClickListener {
            if (permitted) {
                val p = Shizuku.newProcess(arrayOf("sh"), null, null)
                val out = p.outputStream
                out.write(generateCmd(generatePmEnableCmd(Constants.ad) + generatePmEnableCmd(
                    Constants.hybrid)).toByteArray())
                out.flush()
                out.close()
                thread {
                    Thread.sleep(200L)
                    runOnUiThread {
                        Toast.makeText(this, "已启用", Toast.LENGTH_SHORT).show()
                        recreate()
                    }
                }
            }
        }
        disable.setOnClickListener {
            if (permitted) {
                val p = Shizuku.newProcess(arrayOf("sh"), null, null)
                val out = p.outputStream
                out.write(generateCmd(generatePmDisableCmd(Constants.ad) + generatePmDisableCmd(
                    Constants.hybrid)).toByteArray())
                out.flush()
                out.close()
                thread {
                    Thread.sleep(200L)
                    runOnUiThread {
                        Toast.makeText(this, "已启用", Toast.LENGTH_SHORT).show()
                        recreate()
                    }
                }
            }
        }
    }

    private fun check() {
        runCatching {
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(0)
            } else {
                permitted = true
            }
        }.onFailure {
            if (checkSelfPermission("moe.shizuku.manager.permission.API_V23") == PackageManager.PERMISSION_GRANTED) {
                permitted = true
            }
            if (it.javaClass == java.lang.IllegalStateException::class.java) {
                running = false
            }
        }
    }

    private fun generatePmEnableCmd(pkgName: String): String {
        return "pm enable $pkgName;"
    }

    private fun generatePmDisableCmd(pkgName: String): String {
        return "pm disable-user $pkgName;"
    }

    private fun generateCmd(orig: String): String {
        return "$orig\nexit\n"
    }
}