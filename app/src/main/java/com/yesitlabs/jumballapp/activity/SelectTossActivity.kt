package com.yesitlabs.jumballapp.activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.ActivitySelectTossBinding



class SelectTossActivity : AppCompatActivity() {

    lateinit var sessionManager : SessionManager
    private lateinit var binding: ActivitySelectTossBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivitySelectTossBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        sessionManager.changeMusic(1,1)

        binding.btnHead.setOnClickListener {
            sessionManager.playClickSound()
            binding.card1.setBackgroundResource(R.drawable.active_bg)
            binding.card2.setBackgroundResource(R.drawable.in_active_bg)
            binding.tvHead.setTextColor(Color.parseColor("#FFFFFF"))
            binding.tvTails.setTextColor(Color.parseColor("#000000"))
            val intent  = Intent(this,TossPlayingActivity::class.java)
            intent.putExtra("toss","head")
            startActivity(intent)
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true ) {
                override fun handleOnBackPressed() {
                    val intent = Intent(this@SelectTossActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        onBackPressedDispatcher.addCallback(this, callback)


        binding.btnTails.setOnClickListener{
            sessionManager.playClickSound()
            binding.card2.setBackgroundResource(R.drawable.active_bg)
            binding.card1.setBackgroundResource(R.drawable.in_active_bg)
            binding.tvTails.setTextColor(Color.parseColor("#FFFFFF"))
            binding.tvHead.setTextColor(Color.parseColor("#000000"))
            val intent  = Intent(this,TossPlayingActivity::class.java)
            intent.putExtra("toss","tail")
            startActivity(intent)
        }


    }


}