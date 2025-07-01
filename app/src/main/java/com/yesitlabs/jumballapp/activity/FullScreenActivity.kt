package com.yesitlabs.jumballapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.ActivityFullScreenBinding


class FullScreenActivity : AppCompatActivity() {

    private var win = 0
    private var winType = ""
    lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityFullScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        sessionManager.changeMusic(0, 1)

        win = intent.getIntExtra("win",0)
        winType = intent.getStringExtra("winType").toString()

        if (winType.equals("Loss",true)){
            binding.imgload.setImageResource(R.drawable.eliminated_img)
        }else{
            binding.imgload.setImageResource(R.drawable.winner_img)
        }


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true ) {
                override fun handleOnBackPressed() {
                    Log.d("*****","Back Stop")
                }
            }

        onBackPressedDispatcher.addCallback(this, callback)


        Handler(Looper.getMainLooper()).postDelayed({
            if (sessionManager.getMatchType().equals("worldcup",true)) {
                val intent = if (winType.equals("Loss", ignoreCase = true)) {
                    sessionManager.resetScore()
                    sessionManager.resetGameNumberScore()
                    Intent(this@FullScreenActivity, MainActivity::class.java)
                } else {
                    sessionManager.resetScore()
                    Intent(this@FullScreenActivity, TournamentTreeActivity::class.java).apply {
                        putExtra("win", win)
                    }
                }
                startActivity(intent)
                finish()
            }else{
                sessionManager.resetScore()
                sessionManager.resetGameNumberScore()
                val intent = Intent(this@FullScreenActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)


    }
}