package com.yesitlabs.jumballapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yesitlabs.jumballapp.MusicService
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.ValueStore
import com.yesitlabs.jumballapp.di.SessionEventBus
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager

    private var isDialogShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sessionManager = SessionManager(this)

        sessionManager.setExtraTimeUser("Normal")
        observeSessionExpiration()


    }

    private fun observeSessionExpiration() {
        lifecycleScope.launch {
            SessionEventBus.sessionExpiredFlow.collectLatest {
                if (!isDialogShown) {
                    isDialogShown = true
                    sessionManager.alertErrorSession(ErrorMessage.sessionError)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MusicService::class.java))
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("value",0)
        startService(intent)

    }

    override fun onPause() {
        super.onPause()
        stopService(Intent(this, MusicService::class.java))
    }



}