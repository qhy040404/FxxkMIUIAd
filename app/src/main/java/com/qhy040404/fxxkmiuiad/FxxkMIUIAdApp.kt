package com.qhy040404.fxxkmiuiad

import android.app.Application

class FxxkMIUIAdApp : Application() {
    override fun onCreate() {
        super.onCreate()
        app = this
    }

    companion object {
        lateinit var app: FxxkMIUIAdApp
    }
}