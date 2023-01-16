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
    private var running = true
    private var permitted = false
    private val callback = Shizuku.OnRequestPermissionResultListener { _, _ ->
        this@MainActivity.check(true)
    }

    /**
     * Fuck list
     *
     * 《毒瘤列表》
     */
    private val fkList = listOf(
        Constants.ad,
        Constants.hybrid
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Shizuku.addRequestPermissionResultListener(callback)
        check()
        initView()
    }

    override fun onResume() {
        super.onResume()
        check(true)
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

        val states = mutableListOf<String>()

        fkList.forEach {
            states.add(packageManager.getApplicationEnableStateAsString(it))
        }

        tv.text = buildString {
            for (i in fkList.indices) {
                append("${fkList[i]}: ${states[i]}\n")
            }
        }

        enable.setOnClickListener {
            @Suppress("KotlinConstantConditions")
            if (running && permitted) {
                val p = Shizuku.newProcess(arrayOf("sh"), null, null)
                val out = p.outputStream
                out.write(generateCmd(generatePmEnableCmd(fkList)).toByteArray())
                out.flush()
                out.close()
                thread {
                    Thread.sleep(200L)
                    runOnUiThread {
                        Toast.makeText(this, "已启用", Toast.LENGTH_SHORT).show()
                        recreate()
                    }
                }
            } else if (!running) {
                Toast.makeText(this, "Shizuku 未运行", Toast.LENGTH_SHORT).show()
            } else if (!permitted) {
                Toast.makeText(this, "Shizuku 未授权", Toast.LENGTH_SHORT).show()
            }
        }
        disable.setOnClickListener {
            @Suppress("KotlinConstantConditions")
            if (running && permitted) {
                val p = Shizuku.newProcess(arrayOf("sh"), null, null)
                val out = p.outputStream
                out.write(generateCmd(generatePmDisableCmd(fkList)).toByteArray())
                out.flush()
                out.close()
                thread {
                    Thread.sleep(200L)
                    runOnUiThread {
                        Toast.makeText(this, "已禁用", Toast.LENGTH_SHORT).show()
                        recreate()
                    }
                }
            } else if (!running) {
                Toast.makeText(this, "Shizuku 未运行", Toast.LENGTH_SHORT).show()
            } else if (!permitted) {
                Toast.makeText(this, "Shizuku 未授权", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun check(fromCallback: Boolean = false) {
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
        if (fromCallback) {
            findViewById<TextView>(R.id.running).text = if (running) {
                "Shizuku 已运行"
            } else {
                "Shizuku 未运行"
            }
            findViewById<TextView>(R.id.permitted).text = if (permitted) {
                "Shizuku 已授权"
            } else {
                "Shizuku 未授权"
            }
        }
    }

    private fun generatePmEnableCmd(pkgName: String): String {
        return "pm enable $pkgName;"
    }

    private fun generatePmEnableCmd(pkgNames: List<String>): String {
        return buildString {
            pkgNames.forEach {
                append(generatePmEnableCmd(it))
            }
        }
    }

    private fun generatePmDisableCmd(pkgName: String): String {
        return "pm disable-user $pkgName;"
    }

    private fun generatePmDisableCmd(pkgNames: List<String>): String {
        return buildString {
            pkgNames.forEach {
                append(generatePmDisableCmd(it))
            }
        }
    }

    private fun generateCmd(orig: String): String {
        return "$orig\nexit\n"
    }
}