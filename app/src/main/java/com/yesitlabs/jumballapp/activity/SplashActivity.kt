package com.yesitlabs.jumballapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.ActivitySplashBinding
import com.yesitlabs.jumballapp.gameRule.SetGames


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {


    lateinit var sessionManager: SessionManager
    var totalTime = 2700000L
    var startTime = 0
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        //Comment By Shrawan
        sessionManager.changeMusic(0,1)
        changeSplashLogo()
    }

    // This function is used for change splash screen logo
    private fun changeSplashLogo() {
        binding.layLogo.setBackgroundResource(R.drawable.splashlogolunch)
        Handler(Looper.myLooper()!!).postDelayed({
            if (sessionManager.getCheckLogin().equals("yes")){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this,AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        },3000)
    }


}