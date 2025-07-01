package com.yesitlabs.jumballapp

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import java.io.File


@HiltAndroidApp
class BaseApplication : Application() {

    var status: Boolean = true

    lateinit var sessionManager: SessionManager

    companion object {
        @Volatile
        var instance: BaseApplication? = null
        fun getAppContext(): Context {
            return instance?.applicationContext
                ?: throw IllegalStateException("Application instance is null")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Firebase.initialize(this)
        FirebaseApp.initializeApp(this)
        sessionManager = SessionManager(this)

        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.andy.myapplication")
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)

        val intentFilter1 = IntentFilter()
        intentFilter1.addAction("com.example.andy.startstopservice")
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver1, intentFilter1)


        //Comment By Shrawan
        try {
            if (sessionManager.getMusic() == 1) {
                startService(Intent(this, MusicService::class.java))
            }

        } catch (e: Exception) {
            Log.e("Music Service Start Error", e.toString())
        }


    }


    fun startService() {
        status = true
    }


    fun stopService() {
        status = false
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            startService()
        }
    }

    private var broadcastReceiver1: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getBooleanExtra("finised", false)) {
                stopService()
            } else {
                startService()
            }
        }
    }


}