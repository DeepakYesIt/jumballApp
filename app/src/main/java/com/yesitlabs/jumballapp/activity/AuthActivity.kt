package com.yesitlabs.jumballapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.yesitlabs.jumballapp.MusicService
import com.yesitlabs.jumballapp.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

    }

    override fun onResume() {
        super.onResume()
        //Comment By Shrawan
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("value",0)
        startService(intent)
    }

    override fun onPause() {
        super.onPause()
        stopService(Intent(this, MusicService::class.java))
    }

}