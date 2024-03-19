package com.example.memoapp

import android.app.Application

class MemoApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}