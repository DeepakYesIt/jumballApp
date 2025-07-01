package com.yesitlabs.jumballapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.adapter.OnGroupAdapter
import com.yesitlabs.jumballapp.database.team_dtl.TeamDatabaseHelper
import com.yesitlabs.jumballapp.model.teamListModel.TeamListModel
import com.yesitlabs.jumballapp.SessionManager

import com.yesitlabs.jumballapp.databinding.ActivityGroupDetalsBinding
import kotlin.random.Random

class GroupDetailsActivity : AppCompatActivity() {

    var adapter: OnGroupAdapter? = null

    private var teamDetail : ArrayList<TeamListModel> = ArrayList()

    private lateinit var teamDbHelper: TeamDatabaseHelper

    private var win = 0
    private var winType = ""

    private lateinit var binding: ActivityGroupDetalsBinding
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupDetalsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        sessionManager.changeMusic(1,1)

        teamDbHelper = TeamDatabaseHelper(this)

        win = intent.getIntExtra("win",0)
        winType = intent.getStringExtra("winType").toString()

        setRandomDataInDatabase()
        setValueInList()


        setUpOnBoardingIndicator()
        currentOnBoardingIndicator(0)
        binding.viewpagerData.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentOnBoardingIndicator(position)
            }
        })

        binding.btnNext.setOnClickListener {
            sessionManager.playClickSound()
            Log.d("@Error","number :-  "+sessionManager.getGameNumber())
            if (winType.equals("Drow",true)){
                if (sessionManager.getMatchType().equals("worldcup",true)) {
                    sessionManager.resetScore()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    sessionManager.resetScore()
                    sessionManager.resetGameNumberScore()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }else{
                val intent = if (winType.equals("Loss", ignoreCase = true)) {
                    val intent = Intent(this, FullScreenActivity::class.java)
                    intent.putExtra("win",win)
                    intent.putExtra("winType",winType)
                } else {
                    sessionManager.resetScore()
                    Intent(this@GroupDetailsActivity, TournamentTreeActivity::class.java).apply {
                        putExtra("win", win)
                    }
                }
                startActivity(intent)
                finish()
            }
        }


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true ) {
                override fun handleOnBackPressed() {
                    Log.d("*****","Back Stop")
                }
            }

        onBackPressedDispatcher.addCallback(this, callback)

    }

    // This function is used for set Random Team Details in list
    private fun setValueInList() {

        val allTeam = teamDbHelper.getAllTeams()

        val teamID: ArrayList<Int> = ArrayList()
        val captainName: ArrayList<String> = ArrayList()
        val country: ArrayList<String> = ArrayList()
        val enable:  ArrayList<Int> = ArrayList()
        val pld:  ArrayList<Int> = ArrayList()
        val w:  ArrayList<Int> = ArrayList()
        val d:  ArrayList<Int> = ArrayList()
        val l:  ArrayList<Int> = ArrayList()
        val f:  ArrayList<Int> = ArrayList()
        val a:  ArrayList<Int> = ArrayList()
        val gd:  ArrayList<Int> = ArrayList()
        val pts:  ArrayList<Int> =  ArrayList()

        val teamID1: ArrayList<Int> = ArrayList()
        val captainName1: ArrayList<String> = ArrayList()
        val country1: ArrayList<String> = ArrayList()
        val enable1:  ArrayList<Int> = ArrayList()
        val pld1:  ArrayList<Int> = ArrayList()
        val w1:  ArrayList<Int> = ArrayList()
        val d1:  ArrayList<Int> = ArrayList()
        val l1:  ArrayList<Int> = ArrayList()
        val f1:  ArrayList<Int> = ArrayList()
        val a1:  ArrayList<Int> = ArrayList()
        val gd1:  ArrayList<Int> = ArrayList()
        val pts1:  ArrayList<Int> =  ArrayList()

        for (i in 0..7){
            val data = allTeam[i]
            teamID.add(data.teamID)
            captainName.add(data.captainName)
            country.add(data.country)
            enable.add(data.enable)
            pld.add(data.PLD)
            w.add(data.W)
            d.add(data.D)
            l.add(data.L)
            f.add(data.F)
            a.add(data.A)
            gd.add(data.F - data.A)
            pts.add(data.PLD + data.W + data.D + data.L +data.F + data.A)
        }

        for (i in 8..15){
            val data = allTeam[i]
            teamID1.add(data.teamID)
            captainName1.add(data.captainName)
            country1.add(data.country)
            enable1.add(data.enable)
            pld1.add(data.PLD)
            w1.add(data.W)
            d1.add(data.D)
            l1.add(data.L)
            f1.add(data.F)
            a1.add(data.A)
            gd1.add(data.F - data.A)
            pts1.add(data.PLD + data.W + data.D + data.L +data.F + data.A)
        }

        teamDetail.add(
            TeamListModel("D",
                teamID,
                captainName,
                country,
                enable,
                pld,
                w,
                d,
                l,
                f,
                a,
                gd,
                pts
            ))


        teamDetail.add(
            TeamListModel("D",
                teamID1,
                captainName1,
                country1,
                enable1,
                pld1,
                w1,
                d1,
                l1,
                f1,
                a1,
                gd1,
                pts1
            ))

        Log.e("Team ID" ,teamDetail.toString())
        


        adapter = OnGroupAdapter(teamDetail)
        binding.viewpagerData.adapter = adapter

    }

    // This function is used for create Random Team Details in database
    private fun setRandomDataInDatabase() {

        for (i in 2..teamDbHelper.getAllTeams().size){

            val winMatch = Random.nextInt(1, 4)

            teamDbHelper.updatePLD(i,3)
            teamDbHelper.updateW(i,winMatch * 1)
            teamDbHelper.updateD(i,0)
            teamDbHelper.updateL(i,3- winMatch)
            teamDbHelper.updateF(i,Random.nextInt(1, 15))
            teamDbHelper.updateA(i,Random.nextInt(1, 15))

        }


    }

    // This function is used for setup  Group Image
    private fun setUpOnBoardingIndicator() {
        val indicator = arrayOfNulls<ImageView>(2)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicator.indices) {
            indicator[i] = ImageView(applicationContext)
            indicator[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.indicator_inactive
                )
            )
            indicator[i]!!.layoutParams = layoutParams
            binding.layonboardingIndicator.addView(indicator[i])
        }
    }

   // This function is used for select current Group Image
    private fun currentOnBoardingIndicator(index: Int) {
        val childCount: Int = binding.layonboardingIndicator.childCount
        for (i in 0 until childCount) {
            val imageView = binding.layonboardingIndicator.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }


}