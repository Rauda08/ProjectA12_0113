package com.example.finalucp_113

import android.app.Application
import com.example.finalucp_113.dependenciesinjection.AppContainer
import com.example.finalucp_113.dependenciesinjection.SiswaContainer

class LembagaKursusAplications : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container= SiswaContainer()
    }
}