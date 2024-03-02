package com.qhy040404.fxxkmiuiad.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), IBinding<VB> {
    override lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflateBinding<VB>(layoutInflater).also {
            setContentView(it.root)
        }

        init()
    }

    protected abstract fun init()
}