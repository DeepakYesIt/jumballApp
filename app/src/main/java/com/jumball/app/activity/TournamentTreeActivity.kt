package com.jumball.app.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.jumball.app.SessionManager
import com.jumball.app.database.team_dtl.TeamDatabaseHelper
import com.jumball.app.databinding.ActivityTurnamentTreeBinding
import com.jumball.app.model.teamListModel.TeamListModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class TournamentTreeActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    private var win = 0
    private var teamDetail: ArrayList<TeamListModel> = ArrayList()
    private lateinit var teamDbHelper: TeamDatabaseHelper
    private lateinit var binding: ActivityTurnamentTreeBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTurnamentTreeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        teamDbHelper = TeamDatabaseHelper(this)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d("*****", "Back Stop")
                }
            }
        onBackPressedDispatcher.addCallback(this, callback)
        sessionManager = SessionManager(this)
        sessionManager.changeMusic(3, 1)

        win = intent.getIntExtra("win", 0)
        setScreen()
        if (win !=0) {
            sessionManager.setGameWin(sessionManager.getGameWin()+1)
        }
        Log.e("Game Number", sessionManager.getGameNumber().toString())
        Log.e("Game Win", sessionManager.getGameWin().toString())
        if (win == 0) {
            // Deepak
            sessionManager.resetScore()
            sessionManager.resetGameNumberScore()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        } else {
            Log.e("Game Number", sessionManager.getGameNumber().toString())
            Log.e("Game Win", sessionManager.getGameWin().toString())
            when (sessionManager.getGameNumber()) {
                // Semi Final Match 1
                1 -> {
                    moveToNextScreen()
                }
                // Semi Final Match 2
                2 -> {
                    moveToNextScreen()
                }
                //  Final Match
                3 -> {
                    moveToNextScreen()
                }
                // Special Match 1  Assistant Manager
                4 -> {
                    moveToNextScreen()
                }
                // Special Match 2 Lizord Maskoct
                5 -> {
                    sessionManager.resetScore()
                    sessionManager.resetGameNumberScore()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else -> {
                    Log.e("Game Number", sessionManager.getGameNumber().toString())
                    Log.e("Game Win", sessionManager.getGameWin().toString())
                     sessionManager.resetScore()
                     sessionManager.resetGameNumberScore()
                     val intent = Intent(this, MainActivity::class.java)
                     startActivity(intent)
                     finish()
                }
            }
        }
    }

    private fun moveToNextScreen() {
        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 8000)
    }

    private fun setScreen() {

        val team = teamDbHelper.getAllTeams()

        // A1 & C2
        binding.tvTeamName1.text = team[0].captainName
        binding.tvTeamName2.text = team[9].captainName

        // A2 & C1
        binding.tvTeamName3.text = team[1].captainName
        binding.tvTeamName4.text = team[8].captainName

        // B1 & D2
        binding.tvTeamName5.text = team[4].captainName
        binding.tvTeamName6.text = team[13].captainName

        // B2 & D1
        binding.tvTeamName7.text = team[5].captainName
        binding.tvTeamName8.text = team[12].captainName

        //QF1 vs QF2
        if (win == 2) {
            binding.tvTeamName9.text = binding.tvTeamName1.text.toString()
        } else {
            binding.tvTeamName9.text = binding.tvTeamName2.text.toString()
        }

        if (Random.nextInt(1, 3) == 2) {
            binding.tvTeamName10.text = binding.tvTeamName3.text.toString()
        } else {
            binding.tvTeamName10.text = binding.tvTeamName4.text.toString()
        }

        if (Random.nextInt(1, 3) == 2) {
            binding.tvTeamName11.text = binding.tvTeamName5.text.toString()
        } else {
            binding.tvTeamName11.text = binding.tvTeamName6.text.toString()
        }

        if (Random.nextInt(1, 3) == 2) {
            binding.tvTeamName12.text = binding.tvTeamName7.text.toString()
        } else {
            binding.tvTeamName12.text = binding.tvTeamName8.text.toString()
        }


        //QF1 vs QF2
        if (win == 2) {
            binding.tvTeamName13.text = binding.tvTeamName10.text.toString()
        } else {
            binding.tvTeamName13.text = binding.tvTeamName9.text.toString()
        }

        if (Random.nextInt(1, 3) == 2) {
            binding.tvTeamName14.text = binding.tvTeamName11.text.toString()
        } else {
            binding.tvTeamName14.text = binding.tvTeamName12.text.toString()
        }


        // final
        if (win == 2) {
            binding.tvTeamName15.text = binding.tvTeamName14.text.toString()
        } else {
            binding.tvTeamName15.text = binding.tvTeamName13.text.toString()
        }

        // Opponent Team

        // A3 & C4
        binding.tvTeamName21.text = team[2].captainName
        binding.tvTeamName22.text = team[11].captainName

        // A4 & C3
        binding.tvTeamName23.text = team[3].captainName
        binding.tvTeamName24.text = team[10].captainName

        // B3 & D4
        binding.tvTeamName25.text = team[6].captainName
        binding.tvTeamName26.text = team[15].captainName

        // B4 & D3
        binding.tvTeamName27.text = team[7].captainName
        binding.tvTeamName28.text = team[14].captainName

        //QF1 vs QF2
        if (Random.nextInt(1, 3) == 2) {
            binding.tvTeamName29.text = binding.tvTeamName21.text.toString()
        } else {
            binding.tvTeamName29.text = binding.tvTeamName22.text.toString()
        }
        if (Random.nextInt(1, 3) == 2) {
            binding.tvTeamName210.text = binding.tvTeamName23.text.toString()
        } else {
            binding.tvTeamName210.text = binding.tvTeamName24.text.toString()
        }
        if (Random.nextInt(1, 3) == 2) {
            binding.tvTeamName211.text = binding.tvTeamName25.text.toString()
        } else {
            binding.tvTeamName211.text = binding.tvTeamName26.text.toString()
        }
        if (Random.nextInt(1, 3) == 2) {
            binding.tvTeamName212.text = binding.tvTeamName27.text.toString()
        } else {
            binding.tvTeamName212.text = binding.tvTeamName28.text.toString()
        }

        //QF1 vs QF2
        if (Random.nextInt(1, 3) == 2) {
            binding.tvTeamName213.text = binding.tvTeamName210.text.toString()
        } else {
            binding.tvTeamName213.text = binding.tvTeamName29.text.toString()
        }

        if (Random.nextInt(1, 3) == 2) {
            binding.tvTeamName214.text = binding.tvTeamName211.text.toString()
        } else {
            binding.tvTeamName214.text = binding.tvTeamName212.text.toString()
        }

        // final
        if (Random.nextInt(1, 3) == 2) {
            binding.tvTeamName215.text = binding.tvTeamName213.text.toString()
        } else {
            binding.tvTeamName215.text = binding.tvTeamName214.text.toString()
        }
    }

}