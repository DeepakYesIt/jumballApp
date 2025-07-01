package com.yesitlabs.jumballapp.activity


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.database.team_dtl.TeamDatabaseHelper
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.ActivityPenalariesPlayBinding
import java.util.Locale
import kotlin.random.Random


class PenaltiesPlayActivity : AppCompatActivity(), View.OnClickListener {

    private val score = ArrayList<Int>()
    private var chance = 5
    private var userScore = 0
    private var cpuScore = 0
    private var cpuGuess=0
    private var clickEnable = true
    lateinit var sessionManager : SessionManager
    private lateinit var teamDbHelper: TeamDatabaseHelper
    private var userType = "USER"
    var isTimerFinish = false
    private lateinit var binding: ActivityPenalariesPlayBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenalariesPlayBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        sessionManager.changeMusic(20,1)
        binding.layBall.visibility = View.VISIBLE
        resetMatch()

        teamDbHelper = TeamDatabaseHelper(this)
        val allTeam = teamDbHelper.getAllTeams()
        for (data in allTeam) {
            if (data.teamID == 1) {
                binding.playerName1.text = data.captainName
            }

            if (data.teamID == sessionManager.getTeamDetails()) {
                binding.playerName2.text = data.captainName
            }
        }

        userType = if (intent.getStringExtra("userType")!!.uppercase(Locale.ROOT) == "CPU") {
            "CPU"
        }else{
            "USER"
        }


        Log.d("@Error ", "******* $userType")

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true ) {
                override fun handleOnBackPressed() {
                    Log.d("****","Back Lock")
                }
            }

        onBackPressedDispatcher.addCallback(this, callback)


        loadTurn()

        binding.ballBox1.setOnClickListener(this)
        binding.ballBox2.setOnClickListener(this)
        binding.ballBox3.setOnClickListener(this)
        binding.ballBox4.setOnClickListener(this)

    }

    private fun loadTurn(){
        if (userType == "CPU"){
            binding.img1.setBackgroundResource(R.drawable.shoot_img)
            binding.img2.setBackgroundResource(R.drawable.shoot_img)
            binding.img3.setBackgroundResource(R.drawable.shoot_img_right)
            binding.img4.setBackgroundResource(R.drawable.shoot_img_right)
            isClickable(false)
            autoButtonClick()
        }else{
            if (cpuGuess!=0){

            }
            binding.img1.setBackgroundResource(R.drawable.football_img)
            binding.img2.setBackgroundResource(R.drawable.football_img)
            binding.img3.setBackgroundResource(R.drawable.football_img)
            binding.img4.setBackgroundResource(R.drawable.football_img)
            isClickable(true)
        }
    }


    private fun isClickable(status:Boolean){
        binding.ballBox1.isClickable=status
        binding.ballBox2.isClickable=status
        binding.ballBox3.isClickable=status
        binding.ballBox4.isClickable=status
        binding.ballBoxView1.visibility=View.GONE
        binding.ballBoxView2.visibility=View.GONE
        binding.ballBoxView3.visibility=View.GONE
        binding.ballBoxView4.visibility=View.GONE
    }

    private fun nextTurnCpu(selectBox: Int) {
            Log.d("@Error","******"+"CPU's Turn...")
            binding.ballBox1.isClickable=false
            binding.ballBox2.isClickable=false
            binding.ballBox3.isClickable=false
            binding.ballBox4.isClickable=false
            binding.ballBoxView1.visibility=View.GONE
            binding.ballBoxView2.visibility=View.GONE
            binding.ballBoxView3.visibility=View.GONE
            binding.ballBoxView4.visibility=View.GONE
            binding.img1.setBackgroundResource(R.drawable.shoot_img)
            binding.img2.setBackgroundResource(R.drawable.shoot_img)
            binding.img3.setBackgroundResource(R.drawable.shoot_img_right)
            binding.img4.setBackgroundResource(R.drawable.shoot_img_right)
            cpuGuess(selectBox)
    }

    private fun cpuGuess(selectBox: Int) {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("@Error","******"+"CPU is thinking...")
                if (userType == "CPU"){
                    binding.img1.setBackgroundResource(R.drawable.shoot_img)
                    binding.img2.setBackgroundResource(R.drawable.shoot_img)
                    binding.img3.setBackgroundResource(R.drawable.shoot_img_right)
                    binding.img4.setBackgroundResource(R.drawable.shoot_img_right)
                }else{
                    binding.img1.setBackgroundResource(R.drawable.football_img)
                    binding.img2.setBackgroundResource(R.drawable.football_img)
                    binding.img3.setBackgroundResource(R.drawable.football_img)
                    binding.img4.setBackgroundResource(R.drawable.football_img)
                }
            }

            override fun onFinish() {
                cpuGuess = Random.nextInt(1, 4)
                if (cpuGuess == selectBox) {
                    cpuScore++
                }
                if (cpuGuess==0){
                    binding.ballBoxView1.visibility=View.VISIBLE
                    binding.ballBoxView2.visibility=View.GONE
                    binding.ballBoxView3.visibility=View.GONE
                    binding.ballBoxView4.visibility=View.GONE
                }
                if (cpuGuess==1){
                    binding.ballBoxView1.visibility=View.GONE
                    binding.ballBoxView2.visibility=View.VISIBLE
                    binding.ballBoxView3.visibility=View.GONE
                    binding.ballBoxView4.visibility=View.GONE
                }
                if (cpuGuess==2){
                    binding.ballBoxView1.visibility=View.GONE
                    binding.ballBoxView2.visibility=View.GONE
                    binding.ballBoxView3.visibility=View.VISIBLE
                    binding.ballBoxView4.visibility=View.GONE
                }
                if (cpuGuess==3){
                    binding.ballBoxView1.visibility=View.GONE
                    binding.ballBoxView2.visibility=View.GONE
                    binding.ballBoxView3.visibility=View.GONE
                    binding.ballBoxView4.visibility=View.VISIBLE
                }
                userType = "CPU"
                updateScore()
                Handler(Looper.myLooper()!!).postDelayed({
                    loadTurn()
                }, 3000)
            }
        }.start()
    }


    //This function is used for cpu button auto click (AI module)
    private fun autoButtonClick() {
        Log.e("CPU Button", "Auto Click")
        try {
            if (!isTimerFinish) {
                Handler(Looper.myLooper()!!).postDelayed({
                    selectCpuButton()
                }, 3000)
            }
        } catch (e: Exception) {
            Log.d("***** CPU Button", "Auto Click" + e.message)
        }
    }

    //This function is used for click action cup button perform (AI module)
    private fun selectCpuButton() {
         isClickable(true)
         Log.d("@@Error","****** cpu turn.....")
         cpuGuess = Random.nextInt(1, 4)
        Log.d("@@Error", "****** cpuGuess.....$cpuGuess")
         isTimerFinish=true
         if (cpuGuess==1){
            binding.ballBoxView1.visibility=View.VISIBLE
            binding.ballBoxView2.visibility=View.GONE
            binding.ballBoxView3.visibility=View.GONE
            binding.ballBoxView4.visibility=View.GONE
        }
        if (cpuGuess==2){
            binding.ballBoxView1.visibility=View.GONE
            binding.ballBoxView2.visibility=View.VISIBLE
            binding.ballBoxView3.visibility=View.GONE
            binding.ballBoxView4.visibility=View.GONE
        }
        if (cpuGuess==3){
            binding.ballBoxView1.visibility=View.GONE
            binding.ballBoxView2.visibility=View.GONE
            binding.ballBoxView3.visibility=View.VISIBLE
            binding.ballBoxView4.visibility=View.GONE
        }
        if (cpuGuess==4){
            binding.ballBoxView1.visibility=View.GONE
            binding.ballBoxView2.visibility=View.GONE
            binding.ballBoxView3.visibility=View.GONE
            binding.ballBoxView4.visibility=View.VISIBLE
        }
        userType = "USER"
        updateScore()

        Handler(Looper.myLooper()!!).postDelayed({
            isClickable(true)
            loadTurn()
        }, 3000)
    }



    private fun updateScore() {
        Log.d("@Error ", "***** User: $userScore | CPU: $cpuScore")
    }


    override fun onClick(view: View?) {
        if (clickEnable) {
            clickEnable = false
            when (view!!.id) {
                R.id.ballBox1 -> {
                    if (userType.equals("USER",true)){
                        checkGoal(1, binding.ballBoxView1)
                    }else{
                        Log.d("@@Error", "****** cpuGuess.....$cpuGuess")
                    }
                }
                R.id.ballBox2 -> {
                    if (userType.equals("USER",true)) {
                        checkGoal(2, binding.ballBoxView2)
                    }else{
                        Log.d("@@Error", "****** cpuGuess.....$cpuGuess")
                    }
                }
                R.id.ballBox3 -> {
                    if (userType.equals("USER",true)) {
                        checkGoal(3, binding.ballBoxView3)
                    }else{
                        Log.d("@@Error", "****** cpuGuess.....$cpuGuess")
                    }
                }
                R.id.ballBox4 -> {
                    if (userType.equals("USER",true)) {
                        checkGoal(4, binding.ballBoxView4)
                    }else{
                        Log.d("@@Error", "****** cpuGuess.....$cpuGuess")
                    }
                }
            }
        }
    }

    // This function is used for check goal or not
    private fun checkGoal(selectBox: Int, view: View) {
        view.visibility = View.VISIBLE
        userType = "CPU"
        nextTurnCpu(selectBox)

    }

    // This function is used for set  match score
    private fun setScore(){
        //1 User Goal
        //0 CPU Miss

        if (score.size == 1) {
            if (score[0] == 1)
            {
                binding.userGoal1.setBackgroundResource(R.drawable.ball_2)
            }else{
                binding.cpuGoal1.setBackgroundResource(R.drawable.ball_1)
            }
        }

        if (score.size == 2) {
            if (score[1] == 1)
            {
                binding.userGoal2.setBackgroundResource(R.drawable.ball_2)
            }else{
                binding.cpuGoal2.setBackgroundResource(R.drawable.ball_1)
            }
        }


        if (score.size == 3) {
            if (score[2] == 1)
            {
                binding.userGoal3.setBackgroundResource(R.drawable.ball_2)
            }else{
                binding.cpuGoal3.setBackgroundResource(R.drawable.ball_1)
            }
        }


        if (score.size == 4) {
            if (score[3] == 1) {
                binding.userGoal4.setBackgroundResource(R.drawable.ball_2)
            }else{
                binding.cpuGoal4.setBackgroundResource(R.drawable.ball_1)
            }
        }

        if (score.size == 5) {
            if (score[4] == 1) {
                binding.userGoal5.setBackgroundResource(R.drawable.ball_2)
            }else{
                binding.cpuGoal5.setBackgroundResource(R.drawable.ball_1)
            }
        }

        if (chance > 0) {
            resetMatch()
        }else{
            openPenaltyScoreScreen()
        }

    }

    // This function is used for reset match
    private fun resetMatch() {
        binding.ballBoxView1.visibility = View.GONE
        binding.ballBoxView2.visibility = View.GONE
        binding.ballBoxView3.visibility = View.GONE
        binding.ballBoxView4.visibility = View.GONE
        clickEnable = true
    }

    // This function is used for open penalty score screen
    private fun openPenaltyScoreScreen() {
        val intent = Intent(this, SuddenDeathActivity::class.java)
        intent.putExtra("player1", binding.playerName1.text.toString())
        intent.putExtra("player2", binding.playerName2.text.toString())
        intent.putIntegerArrayListExtra("score", score)
        startActivity(intent)
        finish()
    }

}