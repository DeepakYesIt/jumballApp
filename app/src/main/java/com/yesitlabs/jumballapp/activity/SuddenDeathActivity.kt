package com.yesitlabs.jumballapp.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.ActivitySuddenDeathBinding


class

SuddenDeathActivity : AppCompatActivity() {

    private var score = ArrayList<Int>()


    private var mySrc = 0
    private var cpuSrc = 0

    lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivitySuddenDeathBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuddenDeathBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d("*****","back lock")
                }
            }

        onBackPressedDispatcher.addCallback(this, callback)

        sessionManager = SessionManager(this)
        sessionManager.changeMusic(1, 1)

        score = intent.getIntegerArrayListExtra("score")!!


        Log.d("******", "score$score")


        binding.playerName1.text = intent.getStringExtra("player1").toString()
        binding.playerName2.text = intent.getStringExtra("player2").toString()

        for (data in score) {
            if (data == 1) {
                mySrc += 1
            } else {
                cpuSrc += 1
            }
        }

        binding.tvMynumber.text = ""+mySrc

        binding.tvCpunumber.text = ""+cpuSrc

        if (mySrc == cpuSrc){
            winAlertBox(2)
        }else{
            if (mySrc > cpuSrc) {
                winAlertBox(1)
            } else {
                winAlertBox(0)
            }
        }


    }


    // This function is used for display the alert box of world cup is won or loss
    private fun winAlertBox(int: Int) {
        val dialog = Dialog(this, R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView = dialog.findViewById(R.id.img_change)

        sessionManager.changeMusic(21, 0)

        if (int==0){
            imgChange.setImageResource(R.drawable.eliminated_img)
        }
        if (int==1){
            imgChange.setImageResource(R.drawable.winner_img)
        }
        if (int==2){
            imgChange.setImageResource(R.drawable.extra_time_img)
        }

        Handler(Looper.myLooper()!!).postDelayed({
            dialog.dismiss()
            if (int==0){
                sessionManager.resetScore()
                sessionManager.resetGameNumberScore()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            if (int==1){
                sessionManager.setGameNumber(sessionManager.getGameNumber() + 1)
                val intent = Intent(this, TournamentTreeActivity::class.java)
                if (mySrc > cpuSrc) {
                    intent.putExtra("win", 1)
                } else {
                    intent.putExtra("win", 2)
                }
                startActivity(intent)
                finish()
            }
            if (int==2){
                val winner = if (sessionManager.getFirstGamgeStartUser()) {
                    "user"
                } else {
                    "cpu"
                }
                val intent = Intent(this, PenaltiesPlayActivity::class.java)
                intent.putExtra("userType",winner)
                intent.putExtra("type","2")
                startActivity(intent)
                finish()
            }
        }, 3000)

        Handler(Looper.myLooper()!!).postDelayed({
            dialog.dismiss()
        }, 4000)

        dialog.show()
    }

}