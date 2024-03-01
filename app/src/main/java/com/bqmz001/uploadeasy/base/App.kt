package com.bqmz001.uploadeasy.base

import android.app.Application
import android.util.Log
import com.google.android.material.color.DynamicColors

import com.orhanobut.hawk.Hawk


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        DynamicColors.applyToActivitiesIfAvailable(this);

    }
}