package com.jumball.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jumball.app.MusicService
import com.jumball.app.R
import com.jumball.app.SessionManager
import com.jumball.app.ValueStore
import com.jumball.app.di.SessionEventBus
import com.jumball.app.errormassage.ErrorMessage
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
        ValueStore.setValue(0)
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