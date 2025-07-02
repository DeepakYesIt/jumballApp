package com.yesitlabs.jumballapp.fragment.manfragment
import android.annotation.SuppressLint
import   android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.database.player_dtl.CPUPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerModel
import com.yesitlabs.jumballapp.database.team_dtl.TeamDatabaseHelper
import com.yesitlabs.jumballapp.gameRule.SetGames
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.ValueStore
import com.yesitlabs.jumballapp.activity.MainActivity
import com.yesitlabs.jumballapp.database.player_dtl.ExtraPlayerDatabaseHelper
import com.yesitlabs.jumballapp.network.viewModel.GetGuessPlayerListViewModel
import kotlinx.android.synthetic.main.fragment_play_screen.bt_pass
import kotlinx.android.synthetic.main.fragment_play_screen.bt_shoot
import kotlinx.android.synthetic.main.fragment_play_screen.cpu_score_tv
import kotlinx.android.synthetic.main.fragment_play_screen.cpu_screen
import kotlinx.android.synthetic.main.fragment_play_screen.cpur0p1
import kotlinx.android.synthetic.main.fragment_play_screen.cpur0p1_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur0p1_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur0p1_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur0p1_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur0p1_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur0p1_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p1
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p1_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p1_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p1_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p1_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p1_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p1_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p2
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p2_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p2_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p2_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p2_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p2_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p2_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p3
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p3_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p3_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p3_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p3_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p3_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p3_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p4
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p4_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p4_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p4_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p4_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p4_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p4_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p5
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p5_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p5_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p5_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p5_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p5_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur1p5_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p1
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p1_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p1_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p1_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p1_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p1_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p1_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p2
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p2_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p2_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p2_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p2_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p2_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p2_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p3
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p3_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p3_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p3_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p3_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p3_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p3_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p4
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p4_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p4_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p4_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p4_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p4_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p4_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p5
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p5_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p5_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p5_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p5_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p5_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur2p5_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p1
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p1_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p1_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p1_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p1_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p1_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p1_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p2
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p2_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p2_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p2_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p2_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p2_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p2_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p3
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p3_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p3_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p3_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p3_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p3_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p3_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p4
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p4_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p4_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p4_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p4_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p4_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p4_txt
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p5
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p5_id
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p5_img
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p5_name
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p5_number
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p5_select
import kotlinx.android.synthetic.main.fragment_play_screen.cpur3p5_txt
import kotlinx.android.synthetic.main.fragment_play_screen.lay_progess
import kotlinx.android.synthetic.main.fragment_play_screen.oppose_team_player_name
import kotlinx.android.synthetic.main.fragment_play_screen.r0p1
import kotlinx.android.synthetic.main.fragment_play_screen.r0p1_id
import kotlinx.android.synthetic.main.fragment_play_screen.r0p1_img
import kotlinx.android.synthetic.main.fragment_play_screen.r0p1_name
import kotlinx.android.synthetic.main.fragment_play_screen.r0p1_number
import kotlinx.android.synthetic.main.fragment_play_screen.r0p1_select
import kotlinx.android.synthetic.main.fragment_play_screen.r0p1_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r1
import kotlinx.android.synthetic.main.fragment_play_screen.r1p1
import kotlinx.android.synthetic.main.fragment_play_screen.r1p1_id
import kotlinx.android.synthetic.main.fragment_play_screen.r1p1_img
import kotlinx.android.synthetic.main.fragment_play_screen.r1p1_name
import kotlinx.android.synthetic.main.fragment_play_screen.r1p1_number
import kotlinx.android.synthetic.main.fragment_play_screen.r1p1_select
import kotlinx.android.synthetic.main.fragment_play_screen.r1p1_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r1p2
import kotlinx.android.synthetic.main.fragment_play_screen.r1p2_id
import kotlinx.android.synthetic.main.fragment_play_screen.r1p2_img
import kotlinx.android.synthetic.main.fragment_play_screen.r1p2_name
import kotlinx.android.synthetic.main.fragment_play_screen.r1p2_number
import kotlinx.android.synthetic.main.fragment_play_screen.r1p2_select
import kotlinx.android.synthetic.main.fragment_play_screen.r1p2_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r1p3
import kotlinx.android.synthetic.main.fragment_play_screen.r1p3_id
import kotlinx.android.synthetic.main.fragment_play_screen.r1p3_img
import kotlinx.android.synthetic.main.fragment_play_screen.r1p3_name
import kotlinx.android.synthetic.main.fragment_play_screen.r1p3_number
import kotlinx.android.synthetic.main.fragment_play_screen.r1p3_select
import kotlinx.android.synthetic.main.fragment_play_screen.r1p3_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r1p4
import kotlinx.android.synthetic.main.fragment_play_screen.r1p4_id
import kotlinx.android.synthetic.main.fragment_play_screen.r1p4_img
import kotlinx.android.synthetic.main.fragment_play_screen.r1p4_name
import kotlinx.android.synthetic.main.fragment_play_screen.r1p4_number
import kotlinx.android.synthetic.main.fragment_play_screen.r1p4_select
import kotlinx.android.synthetic.main.fragment_play_screen.r1p4_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r1p5
import kotlinx.android.synthetic.main.fragment_play_screen.r1p5_id
import kotlinx.android.synthetic.main.fragment_play_screen.r1p5_img
import kotlinx.android.synthetic.main.fragment_play_screen.r1p5_name
import kotlinx.android.synthetic.main.fragment_play_screen.r1p5_number
import kotlinx.android.synthetic.main.fragment_play_screen.r1p5_select
import kotlinx.android.synthetic.main.fragment_play_screen.r1p5_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r2
import kotlinx.android.synthetic.main.fragment_play_screen.r2p1
import kotlinx.android.synthetic.main.fragment_play_screen.r2p1_id
import kotlinx.android.synthetic.main.fragment_play_screen.r2p1_img
import kotlinx.android.synthetic.main.fragment_play_screen.r2p1_name
import kotlinx.android.synthetic.main.fragment_play_screen.r2p1_number
import kotlinx.android.synthetic.main.fragment_play_screen.r2p1_select
import kotlinx.android.synthetic.main.fragment_play_screen.r2p1_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r2p2
import kotlinx.android.synthetic.main.fragment_play_screen.r2p2_id
import kotlinx.android.synthetic.main.fragment_play_screen.r2p2_img
import kotlinx.android.synthetic.main.fragment_play_screen.r2p2_name
import kotlinx.android.synthetic.main.fragment_play_screen.r2p2_number
import kotlinx.android.synthetic.main.fragment_play_screen.r2p2_select
import kotlinx.android.synthetic.main.fragment_play_screen.r2p2_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r2p3
import kotlinx.android.synthetic.main.fragment_play_screen.r2p3_id
import kotlinx.android.synthetic.main.fragment_play_screen.r2p3_img
import kotlinx.android.synthetic.main.fragment_play_screen.r2p3_name
import kotlinx.android.synthetic.main.fragment_play_screen.r2p3_number
import kotlinx.android.synthetic.main.fragment_play_screen.r2p3_select
import kotlinx.android.synthetic.main.fragment_play_screen.r2p3_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r2p4
import kotlinx.android.synthetic.main.fragment_play_screen.r2p4_id
import kotlinx.android.synthetic.main.fragment_play_screen.r2p4_img
import kotlinx.android.synthetic.main.fragment_play_screen.r2p4_name
import kotlinx.android.synthetic.main.fragment_play_screen.r2p4_number
import kotlinx.android.synthetic.main.fragment_play_screen.r2p4_select
import kotlinx.android.synthetic.main.fragment_play_screen.r2p4_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r2p5
import kotlinx.android.synthetic.main.fragment_play_screen.r2p5_id
import kotlinx.android.synthetic.main.fragment_play_screen.r2p5_img
import kotlinx.android.synthetic.main.fragment_play_screen.r2p5_name
import kotlinx.android.synthetic.main.fragment_play_screen.r2p5_number
import kotlinx.android.synthetic.main.fragment_play_screen.r2p5_select
import kotlinx.android.synthetic.main.fragment_play_screen.r2p5_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r3
import kotlinx.android.synthetic.main.fragment_play_screen.r3p1
import kotlinx.android.synthetic.main.fragment_play_screen.r3p1_id
import kotlinx.android.synthetic.main.fragment_play_screen.r3p1_img
import kotlinx.android.synthetic.main.fragment_play_screen.r3p1_name
import kotlinx.android.synthetic.main.fragment_play_screen.r3p1_number
import kotlinx.android.synthetic.main.fragment_play_screen.r3p1_select
import kotlinx.android.synthetic.main.fragment_play_screen.r3p1_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r3p2
import kotlinx.android.synthetic.main.fragment_play_screen.r3p2_id
import kotlinx.android.synthetic.main.fragment_play_screen.r3p2_img
import kotlinx.android.synthetic.main.fragment_play_screen.r3p2_name
import kotlinx.android.synthetic.main.fragment_play_screen.r3p2_number
import kotlinx.android.synthetic.main.fragment_play_screen.r3p2_select
import kotlinx.android.synthetic.main.fragment_play_screen.r3p2_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r3p3
import kotlinx.android.synthetic.main.fragment_play_screen.r3p3_id
import kotlinx.android.synthetic.main.fragment_play_screen.r3p3_img
import kotlinx.android.synthetic.main.fragment_play_screen.r3p3_name
import kotlinx.android.synthetic.main.fragment_play_screen.r3p3_number
import kotlinx.android.synthetic.main.fragment_play_screen.r3p3_select
import kotlinx.android.synthetic.main.fragment_play_screen.r3p3_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r3p4
import kotlinx.android.synthetic.main.fragment_play_screen.r3p4_id
import kotlinx.android.synthetic.main.fragment_play_screen.r3p4_img
import kotlinx.android.synthetic.main.fragment_play_screen.r3p4_name
import kotlinx.android.synthetic.main.fragment_play_screen.r3p4_number
import kotlinx.android.synthetic.main.fragment_play_screen.r3p4_select
import kotlinx.android.synthetic.main.fragment_play_screen.r3p4_txt
import kotlinx.android.synthetic.main.fragment_play_screen.r3p5
import kotlinx.android.synthetic.main.fragment_play_screen.r3p5_id
import kotlinx.android.synthetic.main.fragment_play_screen.r3p5_img
import kotlinx.android.synthetic.main.fragment_play_screen.r3p5_name
import kotlinx.android.synthetic.main.fragment_play_screen.r3p5_number
import kotlinx.android.synthetic.main.fragment_play_screen.r3p5_select
import kotlinx.android.synthetic.main.fragment_play_screen.r3p5_txt
import kotlinx.android.synthetic.main.fragment_play_screen.root_button
import kotlinx.android.synthetic.main.fragment_play_screen.tv_count
import kotlinx.android.synthetic.main.fragment_play_screen.user_goal_tv
import kotlinx.android.synthetic.main.fragment_play_screen.user_name
import kotlinx.android.synthetic.main.fragment_play_screen.user_screen
import java.util.Locale
import kotlin.random.Random

class PlayScreenFragment : Fragment(R.layout.fragment_play_screen), View.OnClickListener {
    private var setGames: SetGames = SetGames()
    private lateinit var wholeTimer: CountDownTimer
    private var totalPlayerNameShow = 0
    private var cpuPlayerNameShow = 0
    private var playerSelectProcessComplete = false
    private var selectionPower = false
    private var isTimerRunning = true
    var isTimerFinish = false
    private var userR1 = 0
    private var userR2 = 0
    private var userR3 = 0
    private var cpuR1 = 0
    private var cpuR2 = 0
    private var cpuR3 = 0

    private var userType: String? = null
    var totalTime = 5400000L
    //    var startTime = 2670000
//    var startTime = 5300000
    var startTime = 0
    private var myPass = 0
    private var cpuPass = 0
    private lateinit var cpuDbHelper: CPUPlayerDatabaseHelper
    private lateinit var myPlayerDbHelper: PlayerDatabaseHelper
    private lateinit var teamDbHelper: TeamDatabaseHelper
    private var allCpuPlayer = ArrayList<PlayerModel>()
    private var allUserPlayer = ArrayList<PlayerModel>()
    lateinit var sessionManager: SessionManager
    private var selectedPlayerNum: TextView? = null
    private var playerIdUser = 0
    private var selectionView: View? = null
    var status=true
    private lateinit var getGuessPlayerListViewmodel: GetGuessPlayerListViewModel

    var token: String? = null
    private lateinit var extraPLayerDbHelper: ExtraPlayerDatabaseHelper

    private var isCpuActive = false
    private val handler = Handler(Looper.getMainLooper())

    //Shrawan
    private var  isGoalClick  = false

    private val runnable = object : Runnable {
        override fun run() {
            if (isCpuActive) {
                selectCpuButton()
                handler.postDelayed(this, 3000) // Repeat every 3 seconds
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        getGuessPlayerListViewmodel = ViewModelProvider(this)[GetGuessPlayerListViewModel::class.java]

        extraPLayerDbHelper = ExtraPlayerDatabaseHelper(requireContext())

        token = "Bearer " + sessionManager.fetchAuthToken()

        if (sessionManager.getExtraTime().equals("ExtraTime",true) || sessionManager.getExtraTime().equals("TimeHalf",true)){
            lay_progess.max = 900000
            totalTime=900000
            startTime=0
            if (sessionManager.getTimer() != 0) {
                startTime = sessionManager.getTimer()
                if (startTime>=900000){
                    totalTime=900000
                }else{
                    totalTime -= sessionManager.getTimer().toLong()
                }
            }
        }else{
            lay_progess.max = 2700000
            if (sessionManager.getTimer() != 0) {
                startTime = sessionManager.getTimer()
                if (startTime>=5400000){
                    totalTime=5400000
                }else{
                    totalTime -= sessionManager.getTimer().toLong()
                }
            }
        }

        Log.d("@@@Error ", "getExtraTime()"+sessionManager.getExtraTime())
        Log.d("@@@Error ", "sessionManager.getTimer()"+sessionManager.getTimer())
        Log.d("@@@Error ","match count"+sessionManager.getGameNumber())
        Log.d("@@@Error ","timer count"+sessionManager.getTimer())
        Log.d("@@@Error ", "startTime$startTime")
        Log.d("@@@Error ", "totalTime$totalTime")

        myPass = sessionManager.getMyPass()
        cpuPass = sessionManager.getCpuPass()

        if (arguments != null) {
            userType = requireArguments().getString("userType").toString().uppercase(Locale.ROOT)
            playerIdUser = requireArguments().getInt("id")
            isGoalClick = requireArguments().getBoolean("isGoalClick",false)//Shrawan
        } else {
            Log.e("Argument", "No")
        }

        Log.d("@@@Error ", "myPass$myPass")

        Log.d("@@@Error ", "cpuPass$cpuPass")

        Log.d("@@@Error ", "userType$userType")

        if (userType.equals("USER",true)) {
            sessionManager.setFirstGamgeStartUser(false)
            sessionManager.setFirstGamgeStartCPU(true)
        } else {
            sessionManager.setFirstGamgeStartCPU(false)
            sessionManager.setFirstGamgeStartUser(true)
        }


        cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
        myPlayerDbHelper = PlayerDatabaseHelper(requireContext())



        timerLogic()

//        attachTimer()
//        wholeTimer.start()

        backButton()

        setScreens()

        bt_shoot.setOnClickListener(this)


    }

    private fun timerLogic(){

//        startTime += 1000




        val min = startTime / 60000

        if (tv_count != null) {
            tv_count.text = "‘$min"
        }

        val timeForNoise = startTime / 60000

        Log.d("@@@Error", "***** $timeForNoise")
        Log.d("@@@Error", "***** show time tv_count  $min")

        if (timeForNoise >= 3 && timeForNoise % 3 == 0) {
            sessionManager.changeMusic(14, 0)
        }
        if (timeForNoise >= 5 && timeForNoise % 5 == 0) {
            sessionManager.changeMusic(1, 1)
        }

        if (timeForNoise >= 9 && timeForNoise % 9 == 0) {
            sessionManager.changeMusic(1, 1)
        }

        if (startTime / 60000 == 0 && startTime > 60000) {
            Log.e("Time ", min.toString())
        }

        Log.d("@@@Error ","******** "+sessionManager.getExtraTime())

        if (sessionManager.getExtraTime().equals("ExtraTime",true) || sessionManager.getExtraTime().equals("TimeHalf",true)){
            sessionManager.changeMusic(6, 0)
            if (sessionManager.getExtraTime().equals("ExtraTime",true)){
                Log.d("@@@Error","when TimeHalf time set")
                if (startTime >= 900000){
                    totalTime = 900000L
                    startTime = 0
                    status=false
                    setCondition("ExtraTime")
                }
            }

            if (sessionManager.getExtraTime().equals("TimeHalf",true)){
                if (startTime >= 900000){
                    Log.d("@@@Error","when FinalTime time set")
                    totalTime = 900000L
                    startTime = 0
                    sessionManager.setExtraTimeUser("TimeHalfEnd")
                    status=false
                    setCondition("TimeHalfEnd")
                }
            }

            if (startTime <= 900000) {
                lay_progess?.progress = startTime
            } else {
                lay_progess?.progress = startTime - 900000
            }

        }else{
            Log.e("@@@Error", "Full getValue "+ValueStore.getValue1())
            if (startTime >= 5400000 /*&& ValueStore.getValue1() ==0*/){
                status=false
                ValueStore.setValue1(1)
                Log.d("@@@Error","when FullTime time set")
                Log.e("@@@Error", "Full time")
                sessionManager.changeMusic(6, 0)
                isTimerFinish = true
                if (sessionManager.getGameNumber()==3){
                    totalTime = 900000L
                    startTime = 0
                    sessionManager.setExtraTimeUser("FullTime")
                    Log.e("@@@Error", "second")
                    playAlertBox(R.drawable.full_time_img, "FullTime")
                }else{
                    sessionManager.setExtraTimeUser("timeOver")
                    Log.e("@@@Error", "timeOver")
                    playAlertBox(R.drawable.full_time_img, "timeOver")
                }
            }else{
                if (startTime >= 2700000 && ValueStore.getValue() == 0) {
                    ValueStore.setValue(1)
                    status=false
                    Log.d("@@@Error","when half time start")
                    Log.e("Half Time", "Yaa hui")
                    sessionManager.changeMusic(6, 0)
                    sessionManager.setExtraTimeUser("halftime")
                    playAlertBox(R.drawable.half_time_img, "halftime")
                }
            }

            if(startTime <= 2700000) {
                lay_progess?.progress = startTime
            }else {
                lay_progess?.progress = startTime - 2700000
            }
        }

    }

    fun backButton(){
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onClick(item: View?) {

        val onSelectedScreen = if (userType.equals("USER",true)) {
            sessionManager.getUserScreenType()
            isTimerFinish = false
        } else {
            sessionManager.getCpuScreenType()
        }
        val itemSelect=Random.nextInt(1, 10)
        Log.d("****** itemSelect Random number :- ", "****** :- $itemSelect")
        Log.d("****** onSelectedScreen number :- ", "****** :- ${onSelectedScreen.toString()}")
        Log.d("****** LifeLine first :- ", "****** :- ${sessionManager.getLifeLine1()}")
        Log.d("****** LifeLine first1 :- ", "****** :- ${sessionManager.getLifeLine11()}")
        Log.d("****** getFirstGamgeStartUser :- ", "****** :- ${sessionManager.getFirstGamgeStartUser()}")

        Log.d("@Error ","user click")
        if (!isTimerFinish) {
            when (item!!.id) {
                /*R.id.r1p1_img -> {
                    if (sessionManager.getFirstGamgeStartUser()) {
                        playerClickWork(r1p1, r1p1_name, r1p1_number, r1p1_id, r1p1_txt, r1p1_select)
                    }
                }
                R.id.r1p2_img -> {
                    if (sessionManager.getFirstGamgeStartUser()) {
                        playerClickWork(r1p2, r1p2_name, r1p2_number, r1p2_id, r1p2_txt, r1p2_select)
                    }
                }
                R.id.r1p3_img -> {
                    if (sessionManager.getFirstGamgeStartUser()) {
                        playerClickWork(r1p3, r1p3_name, r1p3_number, r1p3_id, r1p3_txt, r1p3_select)
                    }
                }
                R.id.r1p4_img -> {
                    if (sessionManager.getFirstGamgeStartUser()) {
                        playerClickWork(r1p4, r1p4_name, r1p4_number, r1p4_id, r1p4_txt, r1p4_select)
                    }
                }
                R.id.r1p5_img -> {
                    if (sessionManager.getFirstGamgeStartUser()) {
                        playerClickWork(r1p5, r1p5_name, r1p5_number, r1p5_id, r1p5_txt, r1p5_select)
                    }
                }
                R.id.r2p1_img -> {
                    playerClickWork(r2p1, r2p1_name, r2p1_number, r2p1_id, r2p1_txt, r2p1_select)
                }
                R.id.r2p2_img -> {
                    playerClickWork(r2p2, r2p2_name, r2p2_number, r2p2_id, r2p2_txt, r2p2_select)
                }
                R.id.r2p3_img -> {
                    playerClickWork(r2p3, r2p3_name, r2p3_number, r2p3_id, r2p3_txt, r2p3_select)
                }
                R.id.r2p4_img -> {
                    playerClickWork(r2p4, r2p4_name, r2p4_number, r2p4_id, r2p4_txt, r2p4_select)
                }
                R.id.r2p5_img -> {
                    playerClickWork(r2p5, r2p5_name, r2p5_number, r2p5_id, r2p5_txt, r2p5_select)
                }
                R.id.r3p1_img -> {
                    if (sessionManager.getFirstGamgeStartUser()) {
                        playerClickWork(r3p1, r3p1_name, r3p1_number, r3p1_id, r3p1_txt, r3p1_select)
                    }
                }
                R.id.r3p2_img -> {
                    if (sessionManager.getFirstGamgeStartUser()) {
                        playerClickWork(r3p2, r3p2_name, r3p2_number, r3p2_id, r3p2_txt, r3p2_select)
                    }
                }
                R.id.r3p3_img -> {
                    if (sessionManager.getFirstGamgeStartUser()) {
                        playerClickWork(r3p3, r3p3_name, r3p3_number, r3p3_id, r3p3_txt, r3p3_select)
                    }
                }
                R.id.r3p4_img -> {
                    if (sessionManager.getFirstGamgeStartUser()) {
                        playerClickWork(r3p4, r3p4_name, r3p4_number, r3p4_id, r3p4_txt, r3p4_select)
                    }
                }
                R.id.r3p5_img -> {
                    if (sessionManager.getFirstGamgeStartUser()) {
                        playerClickWork(r3p5, r3p5_name, r3p5_number, r3p5_id, r3p5_txt, r3p5_select)
                    }
                }
                R.id.bt_shoot -> {
                    shootBall()
                }
                R.id.bt_pass -> {
                    userPassBall()
                }*/


                //Shrawan
                R.id.r0p1_img ->{
                    if (r0p1_select.visibility==View.VISIBLE) {
                        playerClickWork(r0p1, r0p1_name, r0p1_number, r0p1_id, r0p1_txt, r0p1_select)
                    }
                }
                R.id.r1p1_img -> {
                    if (r1p1_select.visibility==View.VISIBLE) {
                        playerClickWork(r1p1, r1p1_name, r1p1_number, r1p1_id, r1p1_txt, r1p1_select)
                    }
                }
                R.id.r1p2_img -> {
                    if (r1p2_select.visibility==View.VISIBLE) {
                        playerClickWork(r1p2, r1p2_name, r1p2_number, r1p2_id, r1p2_txt, r1p2_select)
                    }
                }
                R.id.r1p3_img -> {
                    if (r1p3_select.visibility==View.VISIBLE) {
                        playerClickWork(r1p3, r1p3_name, r1p3_number, r1p3_id, r1p3_txt, r1p3_select)
                    }
                }
                R.id.r1p4_img -> {
                    if (r1p4_select.visibility==View.VISIBLE) {
                        playerClickWork(r1p4, r1p4_name, r1p4_number, r1p4_id, r1p4_txt, r1p4_select)
                    }
                }
                R.id.r1p5_img -> {
                    if (r1p5_select.visibility==View.VISIBLE) {
                        playerClickWork(r1p5, r1p5_name, r1p5_number, r1p5_id, r1p5_txt, r1p5_select)
                    }
                }
                R.id.r2p1_img -> {
                    playerClickWork(r2p1, r2p1_name, r2p1_number, r2p1_id, r2p1_txt, r2p1_select)
                }
                R.id.r2p2_img -> {
                    playerClickWork(r2p2, r2p2_name, r2p2_number, r2p2_id, r2p2_txt, r2p2_select)
                }
                R.id.r2p3_img -> {
                    playerClickWork(r2p3, r2p3_name, r2p3_number, r2p3_id, r2p3_txt, r2p3_select)
                }
                R.id.r2p4_img -> {
                    playerClickWork(r2p4, r2p4_name, r2p4_number, r2p4_id, r2p4_txt, r2p4_select)
                }
                R.id.r2p5_img -> {
                    playerClickWork(r2p5, r2p5_name, r2p5_number, r2p5_id, r2p5_txt, r2p5_select)
                }
                R.id.r3p1_img -> {
                    if (r3p1_select.visibility==View.VISIBLE) {
                        playerClickWork(r3p1, r3p1_name, r3p1_number, r3p1_id, r3p1_txt, r3p1_select)
                    }
                }
                R.id.r3p2_img -> {
                    if (r3p2_select.visibility==View.VISIBLE) {
                        playerClickWork(r3p2, r3p2_name, r3p2_number, r3p2_id, r3p2_txt, r3p2_select)
                    }
                }
                R.id.r3p3_img -> {
                    if (r3p3_select.visibility==View.VISIBLE) {
                        playerClickWork(r3p3, r3p3_name, r3p3_number, r3p3_id, r3p3_txt, r3p3_select)
                    }
                }
                R.id.r3p4_img -> {
                    if (r3p4_select.visibility==View.VISIBLE) {
                        playerClickWork(r3p4, r3p4_name, r3p4_number, r3p4_id, r3p4_txt, r3p4_select)
                    }
                }
                R.id.r3p5_img -> {
                    if (r3p5_select.visibility==View.VISIBLE) {
                        playerClickWork(r3p5, r3p5_name, r3p5_number, r3p5_id, r3p5_txt, r3p5_select)
                    }
                }
                R.id.bt_shoot -> {
                    shootBall()
                }
                R.id.bt_pass -> {
                    userPassBall()
                }
            }
        }
    }
    //This function is used for attach the timer in play screen
//    private fun attachTimer() {
//        wholeTimer = object : CountDownTimer(totalTime, 500) {
//            @SuppressLint("SetTextI18n")
//            override fun onTick(time: Long) {
//                startTime += 1000
//
//                val min = startTime / 60000
//
//                if (tv_count != null) {
//                    tv_count.text = "‘$min"
//                }
//
//                val timeForNoise = startTime / 60000
//
//                Log.d("@@@Error", "***** $timeForNoise")
//                Log.d("@@@Error", "***** show time tv_count  $min")
//
//                if (timeForNoise >= 3 && timeForNoise % 3 == 0) {
//                    sessionManager.changeMusic(14, 0)
//                }
//                if (timeForNoise >= 5 && timeForNoise % 5 == 0) {
//                    sessionManager.changeMusic(1, 1)
//                }
//
//                if (timeForNoise >= 9 && timeForNoise % 9 == 0) {
//                    sessionManager.changeMusic(1, 1)
//                }
//
//                if (startTime / 60000 == 0 && startTime > 60000) {
//                    Log.e("Time ", min.toString())
//                }
//
//                Log.d("@@@Error ","******** "+sessionManager.getExtraTime())
//
//                if (sessionManager.getExtraTime().equals("ExtraTime",true) || sessionManager.getExtraTime().equals("TimeHalf",true)){
//                    sessionManager.changeMusic(6, 0)
//                    if (sessionManager.getExtraTime().equals("ExtraTime",true)){
//                        Log.d("@@@Error","when TimeHalf time set")
//                        if (startTime >= 900000){
//                            wholeTimer.cancel()
//                            totalTime = 900000L
//                            startTime = 0
//                            setCondition("ExtraTime")
//                        }
//                    }
//
//                    if (sessionManager.getExtraTime().equals("TimeHalf",true)){
//                        if (startTime >= 900000){
//                            Log.d("@@@Error","when FinalTime time set")
//                            wholeTimer.cancel()
//                            totalTime = 900000L
//                            startTime = 0
//                            sessionManager.setGameGameCondition(2)
//                            sessionManager.setExtraTimeUser("TimeHalfEnd")
//                            setCondition("TimeHalfEnd")
//                        }
//                    }
//
//                    if (startTime <= 900000) {
//                        lay_progess?.progress = startTime
//                    } else {
//                        lay_progess?.progress = startTime - 900000
//                    }
//
//                }else{
//                    Log.e("@@@Error", "Full getValue "+ValueStore.getValue1())
//                    if (startTime >= 5400000 && ValueStore.getValue1() ==0){
////                        wholeTimer.cancel()
//                        ValueStore.setValue1(1)
//                        Log.d("@@@Error","when FullTime time set")
//                        Log.e("@@@Error", "Full time")
//                        sessionManager.changeMusic(6, 0)
//                        isTimerFinish = true
//                        if (sessionManager.getGameNumber()==3){
//                            totalTime = 900000L
//                            startTime = 0
//                            sessionManager.setExtraTimeUser("FullTime")
//                            sessionManager.setGameGameCondition(2)
//                            Log.e("@@@Error", "second")
//                            playAlertBox(R.drawable.full_time_img, "FullTime")
//                        }else{
//                            sessionManager.setExtraTimeUser("timeOver")
//                            sessionManager.setGameGameCondition(2)
//                            Log.e("@@@Error", "timeOver")
//                            playAlertBox(R.drawable.full_time_img, "timeOver")
//                        }
//                    }else{
//                        if (startTime >= 2700000 && ValueStore.getValue() ==0) {
//                            ValueStore.setValue(1)
////                            wholeTimer.cancel() // Stop the timer
//                            Log.d("@@@Error","when half time start")
//                            Log.e("Half Time", "Yaa hui")
//                            sessionManager.changeMusic(6, 0)
//                            sessionManager.setExtraTimeUser("halftime")
//                            playAlertBox(R.drawable.half_time_img, "halftime")
//                        }
//                    }
//
//                    if(startTime <= 2700000) {
//                        lay_progess?.progress = startTime
//                    }else {
//                        lay_progess?.progress = startTime - 2700000
//                    }
//                }
//            }
//            override fun onFinish() {
//                Log.d("@@@Error","******* finish time "+sessionManager.getExtraTime())
//                Log.d("@@@Error","******* finish time getGameCondition "+sessionManager.getGameCondition())
//                Log.d("@@@Error","******* finish time startTime "+startTime)
//            }
//        }
//    }


    private fun setCondition(status:String){
        val cpuName = oppose_team_player_name.text.toString()
        val myName = user_name.text.toString()

        Log.e("@@@Error", "status $status")
        Log.e("@@@Error","getMyScore "+sessionManager.getMyScore())
        Log.e("@@@Error","getCpuScore "+sessionManager.getCpuScore())
        if (sessionManager.getMatchType().equals("worldcup",true)){
            if (status.equals("timeOver",true)){
                if (sessionManager.getMyScore() == sessionManager.getCpuScore()){
                    val bundle = Bundle()
                    bundle.putString("opposeTeamName", cpuName)
                    bundle.putString("myTeamName", myName)
                    findNavController().navigate(R.id.score_fragment, bundle)
                }else if (sessionManager.getMyScore() > sessionManager.getCpuScore()){
//                    val bundle = Bundle()
//                    bundle.putString("opposeTeamName", cpuName)
//                    bundle.putString("myTeamName", myName)
//                    findNavController().navigate(R.id.score_fragment, bundle)
                    if (sessionManager.getGameNumber()>=3){
                        winAlertBox(3)
                    }else{
                        val bundle = Bundle()
                        bundle.putString("opposeTeamName", cpuName)
                        bundle.putString("myTeamName", myName)
                        findNavController().navigate(R.id.score_fragment, bundle)
                    }

                }else{
//                    winAlertBox(0)
                    val bundle = Bundle()
                    bundle.putString("opposeTeamName", cpuName)
                    bundle.putString("myTeamName", myName)
                    findNavController().navigate(R.id.score_fragment, bundle)
                }
            }
            if (status.equals("ExtraTime",true)){
                if (sessionManager.getMyScore() == sessionManager.getCpuScore()){
                    sessionManager.setExtraTimeUser("TimeHalf")
                    playAlertBox(R.drawable.extra_time_ht_img,"TimeHalf")
                }else if (sessionManager.getMyScore() > sessionManager.getCpuScore()){
                    if (sessionManager.getGameNumber()>=3){
                        winAlertBox(3)
                    }else{
                        val bundle = Bundle()
                        bundle.putString("opposeTeamName", cpuName)
                        bundle.putString("myTeamName", myName)
                        findNavController().navigate(R.id.score_fragment, bundle)
                    }
                    /*val bundle = Bundle()
                    bundle.putString("opposeTeamName", cpuName)
                    bundle.putString("myTeamName", myName)
                    findNavController().navigate(R.id.score_fragment, bundle)*/
                    /* val bundle = Bundle()
                     bundle.putString("opposeTeamName", cpuName)
                     bundle.putString("myTeamName", myName)
                     findNavController().navigate(R.id.score_fragment, bundle)*/
                }else{
//                    winAlertBox(0)
                    val bundle = Bundle()
                    bundle.putString("opposeTeamName", cpuName)
                    bundle.putString("myTeamName", myName)
                    findNavController().navigate(R.id.score_fragment, bundle)
                }
            }
            if (status.equals("TimeHalfEnd",true)){
                if (sessionManager.getMyScore() == sessionManager.getCpuScore()){
                    winAlertBox(1)
                } else if (sessionManager.getMyScore() > sessionManager.getCpuScore()){
                    /*val bundle = Bundle()
                    bundle.putString("opposeTeamName", cpuName)
                    bundle.putString("myTeamName", myName)
                    findNavController().navigate(R.id.score_fragment, bundle)*/
//                    val bundle = Bundle()
//                    bundle.putString("opposeTeamName", cpuName)
//                    bundle.putString("myTeamName", myName)
//                    findNavController().navigate(R.id.score_fragment, bundle)
                    if (sessionManager.getGameNumber()>=3){
                        winAlertBox(3)
                    }else{
                        val bundle = Bundle()
                        bundle.putString("opposeTeamName", cpuName)
                        bundle.putString("myTeamName", myName)
                        findNavController().navigate(R.id.score_fragment, bundle)
                    }
                }else{
                    val bundle = Bundle()
                    bundle.putString("opposeTeamName", cpuName)
                    bundle.putString("myTeamName", myName)
                    findNavController().navigate(R.id.score_fragment, bundle)
                }
            }
        }else{
            if (sessionManager.getMyScore() <= sessionManager.getCpuScore()){
                /*winAlertBox(0)
                val bundle = Bundle()
                bundle.putString("opposeTeamName", cpuName)
                bundle.putString("myTeamName", myName)
                findNavController().navigate(R.id.score_fragment, bundle)*/
                val bundle = Bundle()
                bundle.putString("opposeTeamName", cpuName)
                bundle.putString("myTeamName", myName)
                findNavController().navigate(R.id.score_fragment, bundle)
            }else{
                winAlertBox(2)
            }
            /*val bundle = Bundle()
            bundle.putString("opposeTeamName", cpuName)
            bundle.putString("myTeamName", myName)
            findNavController().navigate(R.id.score_fragment, bundle)*/

        }
    }

    //This function is used for cpu button auto click (AI module)
    private fun autoButtonClick() {
        Log.e("CPU Button", "Auto Click")
        try {
            /*if (!isTimerFinish) {
                Handler(Looper.myLooper()!!).postDelayed({
                    selectCpuButton()
                }, 3000)
            }*/
            startCpuProcess()
        } catch (e: Exception) {
            Log.d("***** CPU Button", "Auto Click" + e.message)
        }
    }


    fun startCpuProcess() {
        isCpuActive = true
        handler.postDelayed(runnable, 3000) // Start delayed execution
    }

    fun stopCpuProcess() {
        isCpuActive = false
        handler.removeCallbacks(runnable) // Stop execution
    }

    //This function is used for click action cup button perform (AI module)
    private fun selectCpuButton() {
        Log.e("CPU", "Button CLick")
        if (!isTimerFinish) {
            val onSelectedScreen = if (userType.equals("USER",true)) {
                sessionManager.getUserScreenType()
                isTimerFinish = true
            } else {
                sessionManager.getCpuScreenType()
            }
            val itemSelect=Random.nextInt(1, 11)
            Log.d("****** itemSelect Random number :- ", "****** :CPU $itemSelect")
            Log.d("****** onSelectedScreen number :- ", "****** :- $onSelectedScreen")
            Log.d("****** LifeLine first :- ", "****** :- ${sessionManager.getLifeLine1()}")
            Log.d("****** LifeLine first1 :- ", "****** :- ${sessionManager.getLifeLine11()}")
            /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                when (onSelectedScreen) {
                    "5-2-3" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }
                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }

                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }



                            }
                            4 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }



                            }
                            5 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p5, cpur1p5_name, cpur1p5_number, cpur1p5_id, cpur1p5_txt, cpur1p5_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p5, cpur1p5_name, cpur1p5_number, cpur1p5_id, cpur1p5_txt, cpur1p5_select)
                                }
                            }

                            6 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                                }else{
                                    autoButtonClick()
                                }


                             }

                            7 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                                }else{
                                    autoButtonClick()
                                }
                             }

                            8 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur2p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }



                            }
                            9 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                            10 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p3, cpur3p3_name, cpur3p3_number, cpur3p3_id, cpur3p3_txt, cpur3p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }

                            }
                        }
                    }  // Done

                    "5-4-1" -> {
                        when(itemSelect){

                            1 -> {


                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }



                            }
                            2 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }



                            }
                            3 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }



                            }
                            4 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }



                            }
                            5 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p5, cpur1p5_name, cpur1p5_number, cpur1p5_id, cpur1p5_txt, cpur1p5_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p5, cpur1p5_name, cpur1p5_number, cpur1p5_id, cpur1p5_txt, cpur1p5_select)
                                }



                            }

                            6 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                                }else{
                                    autoButtonClick()
                                }
                             }
                            7 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                                }else{
                                    autoButtonClick()
                                }
                             }
                            8 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                                }else{
                                    autoButtonClick()
                                }
                             }
                            9 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
                                }else{
                                    autoButtonClick()
                                }
                             }

                            10 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }

                            }

                        }
                    }  // Done

                    "5-3-2" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }
                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }

                            }
                            3 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }
                            }
                            4 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }



                            }
                            5 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p5, cpur1p5_name, cpur1p5_number, cpur1p5_id, cpur1p5_txt, cpur1p5_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p5, cpur1p5_name, cpur1p5_number, cpur1p5_id, cpur1p5_txt, cpur1p5_select)
                                }
                            }

                            6 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)

                                }else{
                                    autoButtonClick()
                                }


                             }
                            7 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                                }else{
                                    autoButtonClick()
                                }
                             }
                            8 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                                }else{
                                    autoButtonClick()
                                }

                             }

                            9 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                            10 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                        }
                    }  // Done

                    "3-5-2" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }
                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }

                            }
                            3 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }
                            }

                            4 -> {


                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)

                                }else{
                                    autoButtonClick()
                                }


                             }
                            5 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)

                                }else{
                                    autoButtonClick()
                                }


                             }
                            6 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)

                                }else{
                                    autoButtonClick()
                                }

                             }
                            7 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
                                }else{
                                    autoButtonClick()
                                }

                             }
                            8 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p5, cpur2p5_name, cpur2p5_number, cpur2p5_id, cpur2p5_txt, cpur2p5_select)
                                }else{
                                    autoButtonClick()
                                }


                            }

                            9 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                            10 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                        }
                    }  // Done

                    "4-5-1" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }
                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }
                            }
                            4 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }
                            }

                            5 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                                }else{
                                    autoButtonClick()
                                }
                             }
                            6 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                                }else{
                                    autoButtonClick()
                                }
                            }
                            7 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                                }else{
                                    autoButtonClick()
                                }


                            }
                            8 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
                                }else{
                                    autoButtonClick()
                                }



                            }
                            9 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p5, cpur2p5_name, cpur2p5_number, cpur2p5_id, cpur2p5_txt, cpur2p5_select)
                                }else{
                                    autoButtonClick()
                                }
                            }

                            10 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                        }
                    }  // Done

                    "4-4-2" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }
                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }
                            }
                            4 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }
                            }

                            5 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                                }else{
                                    autoButtonClick()
                                }

                            }
                            6 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                                }else{
                                    autoButtonClick()
                                }
                            }
                            7 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                                }else{
                                    autoButtonClick()
                                }
                            }
                            8 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
                                }else{
                                    autoButtonClick()
                                }
                            }

                            9 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                            10 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }

                        }
                    }  // Done

                    "4-3-3" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }
                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }
                            }
                            4 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }
                            }

                            5 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                                }else{
                                    autoButtonClick()
                                }
                            }
                            6 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                                }else{
                                    autoButtonClick()
                                }
                            }
                            7 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                                }else{
                                    autoButtonClick()
                                }
                            }

                            8 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                            9 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                            10 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p3, cpur3p3_name, cpur3p3_number, cpur3p3_id, cpur3p3_txt, cpur3p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                        }
                    }  // Done

                    "3-4-3" -> {
                        when(itemSelect){


                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }
                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }
                            }


                            4 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                                }else{
                                    autoButtonClick()
                                }

                            }
                            5 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                                }else{
                                    autoButtonClick()
                                }

                            }
                            6 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                                }else{
                                    autoButtonClick()
                                }

                            }
                            7 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
                                }else{
                                    autoButtonClick()
                                }
                            }

                            8 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }

                            }
                            9 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                            10 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p3, cpur3p3_name, cpur3p3_number, cpur3p3_id, cpur3p3_txt, cpur3p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }

                            }
                        }
                    }  // Done

                    "4-2-4" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }
                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }
                            }
                            4 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }
                            }

                            5 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                                }else{
                                    autoButtonClick()
                                }
                            }
                            6 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                                }else{
                                    autoButtonClick()
                                }
                            }

                            7 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                            8 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                            9 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p3, cpur3p3_name, cpur3p3_number, cpur3p3_id, cpur3p3_txt, cpur3p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }
                            10 -> {

                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (sessionManager.getFirstGamgeStartCPU()) {
                                        cpuPlayerClickWork(cpur3p4, cpur3p4_name, cpur3p4_number, cpur3p4_id, cpur3p4_txt, cpur3p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    autoButtonClick()
                                }
                            }

                        }
                    }  // Done
                    else -> {
                        Toast.makeText(requireContext(), "Formation is not Exist $onSelectedScreen", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                try {
                    when (onSelectedScreen) {
                        "5-2-3" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility
                            cpur1p5_select.visibility=view!!.visibility
                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()

                        }  // Done

                        "5-4-1" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility
                            cpur1p5_select.visibility=view!!.visibility

                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()
                        }  // Done

                        "5-3-2" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility
                            cpur1p5_select.visibility=view!!.visibility

                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()

                        }  // Done

                        "3-5-2" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility

                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()
                        }  // Done

                        "4-5-1" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility

                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()

                        }  // Done

                        "4-4-2" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility

                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()


                        }  // Done

                        "4-3-3" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility

                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()


                        }  // Done

                        "3-4-3" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility

                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()

                        }  // Done

                        "4-2-4" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility

                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()


                        }  // Done

                        else -> {
                            Toast.makeText(requireContext(), "Formation is not Exist $onSelectedScreen", Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (e:Exception){
                    Log.d("******","Error :- "+e.message.toString())
                }
            }*/

            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                when (onSelectedScreen) {

                    "5-2-3" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }

                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p2_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p3_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }

                            }
                            4 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p4_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }

                            }
                            5 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p5_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p5, cpur1p5_name, cpur1p5_number, cpur1p5_id, cpur1p5_txt, cpur1p5_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p5, cpur1p5_name, cpur1p5_number, cpur1p5_id, cpur1p5_txt, cpur1p5_select)
                                }

                            }

                            6 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                            }

                            7 -> {
                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                                }else{
                                    autoButtonClick()
                                }*/
                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                            }

                            8 -> {
                                if (cpur3p1_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            9 -> {
                                if (cpur3p2_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            10 -> {
                                if (cpur3p3_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p3, cpur3p3_name, cpur3p3_number, cpur3p3_id, cpur3p3_txt, cpur3p3_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            //Shrawan
                            11 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur0p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                }

                            }
                        }
                    }  // Done

                    "5-4-1" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }

                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p2_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p3_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }

                            }
                            4 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p4_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }

                            }
                            5 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p5_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p5, cpur1p5_name, cpur1p5_number, cpur1p5_id, cpur1p5_txt, cpur1p5_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p5, cpur1p5_name, cpur1p5_number, cpur1p5_id, cpur1p5_txt, cpur1p5_select)
                                }

                            }

                            6 -> {
                                /* if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                     cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                                 }else{
                                     autoButtonClick()
                                 }*/
                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                            }
                            7 -> {
                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                                }else{
                                    autoButtonClick()
                                }*/
                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                            }
                            8 -> {
                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                                }else{
                                    autoButtonClick()
                                }*/
                                cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                            }
                            9 -> {
                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
                                }else{
                                    autoButtonClick()
                                }*/
                                cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
                            }

                            10 -> {
                                if (cpur3p1_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            //Shrawan
                            11->{
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur0p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                }
                            }
                        }
                    }  // Done

                    "5-3-2" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }

                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p2_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p3_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }

                            }
                            4 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p4_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }

                            }
                            5 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p5_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p5, cpur1p5_name, cpur1p5_number, cpur1p5_id, cpur1p5_txt, cpur1p5_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p5, cpur1p5_name, cpur1p5_number, cpur1p5_id, cpur1p5_txt, cpur1p5_select)
                                }

                            }

                            6 -> {
                                /* if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                     cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                                 }else{
                                     autoButtonClick()
                                 }*/
                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                            }
                            7 -> {
                                /* if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                     cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                                 }else{
                                     autoButtonClick()
                                 }*/
                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                            }
                            8 -> {
                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                                }else{
                                    autoButtonClick()
                                }*/
                                cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)

                            }

                            9 -> {
                                if (cpur3p1_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            10 -> {
                                if (cpur3p2_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            11 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur0p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                }

                            }
                        }
                    }  // Done

                    "3-5-2" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }

                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p2_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p3_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }

                            }

                            4 -> {
                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                                }else{
                                    autoButtonClick()
                                }*/
                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                            }
                            5 -> {
                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                                }else{
                                    autoButtonClick()
                                }*/
                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                            }
                            6 -> {
                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                                }else{
                                    autoButtonClick()
                                }*/
                                cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                            }
                            7 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
                            }
                            8 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p5, cpur2p5_name, cpur2p5_number, cpur2p5_id, cpur2p5_txt, cpur2p5_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p5, cpur2p5_name, cpur2p5_number, cpur2p5_id, cpur2p5_txt, cpur2p5_select)
                            }
                            9 -> {
                                if (cpur3p1_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            10 -> {
                                if (cpur3p2_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            11 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur0p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                }

                            }
                        }
                    }  // Done

                    "4-5-1" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }

                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p2_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p3_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }

                            }
                            4 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p4_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }

                            }

                            5 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                            }
                            6 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                            }
                            7 -> {

//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
//                            }else{
//                                autoButtonClick()
//                            }

                                cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                            }
                            8 -> {

                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
                                }else{
                                    autoButtonClick()
                                }*/
                                cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)

                            }
                            9 -> {
                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p5, cpur2p5_name, cpur2p5_number, cpur2p5_id, cpur2p5_txt, cpur2p5_select)
                                }else{
                                    autoButtonClick()
                                }*/
                                cpuPlayerClickWork(cpur2p5, cpur2p5_name, cpur2p5_number, cpur2p5_id, cpur2p5_txt, cpur2p5_select)
                            }
                            10 -> {
                                if (cpur3p1_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            11 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur0p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                }

                            }
                        }
                    }  // Done

                    "4-4-2" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }

                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p2_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p3_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }

                            }
                            4 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p4_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }

                            }
                            5 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)

                            }
                            6 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                            }
                            7 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                            }
                            8 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
                            }

                            9 -> {
                                if (cpur3p1_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            10 -> {
                                if (cpur3p2_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                } else {
                                    autoButtonClick()
                                }
                            }

                            11 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur0p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                }

                            }
                        }
                    }  // Done

                    "4-3-3" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }

                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p2_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p3_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }

                            }
                            4 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p4_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }

                            }

                            5 -> {
                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                                }else{
                                    autoButtonClick()
                                }*/
                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                            }
                            6 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                            }
                            7 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
                            }

                            8 -> {
                                if (cpur3p1_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            9 -> {
                                if (cpur3p2_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            10 -> {
                                if (cpur3p3_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p3, cpur3p3_name, cpur3p3_number, cpur3p3_id, cpur3p3_txt, cpur3p3_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            11 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur0p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                }

                            }
                        }
                    }  // Done

                    "3-4-3" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }

                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p2_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p3_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }

                            }
                            4 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)

                            }
                            5 -> {

//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)

                            }
                            6 -> {

//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p3, cpur2p3_name, cpur2p3_number, cpur2p3_id, cpur2p3_txt, cpur2p3_select)

                            }
                            7 -> {

//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p4, cpur2p4_name, cpur2p4_number, cpur2p4_id, cpur2p4_txt, cpur2p4_select)
                            }

                            8 -> {
                                if (cpur3p1_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            9 -> {
                                if (cpur3p2_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            10 -> {
                                if (cpur3p3_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p3, cpur3p3_name, cpur3p3_number, cpur3p3_id, cpur3p3_txt, cpur3p3_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            11 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur0p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                }

                            }
                        }
                    }  // Done

                    "4-2-4" -> {
                        when(itemSelect){
                            1 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p1, cpur1p1_name, cpur1p1_number, cpur1p1_id, cpur1p1_txt, cpur1p1_select)
                                }

                            }
                            2 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p2_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p2, cpur1p2_name, cpur1p2_number, cpur1p2_id, cpur1p2_txt, cpur1p2_select)
                                }
                            }
                            3 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p3_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p3, cpur1p3_name, cpur1p3_number, cpur1p3_id, cpur1p3_txt, cpur1p3_select)
                                }

                            }
                            4 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur1p4_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur1p4, cpur1p4_name, cpur1p4_number, cpur1p4_id, cpur1p4_txt, cpur1p4_select)
                                }

                            }

                            5 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p1, cpur2p1_name, cpur2p1_number, cpur2p1_id, cpur2p1_txt, cpur2p1_select)
                            }
                            6 -> {
//                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
//                            }else{
//                                autoButtonClick()
//                            }
                                cpuPlayerClickWork(cpur2p2, cpur2p2_name, cpur2p2_number, cpur2p2_id, cpur2p2_txt, cpur2p2_select)
                            }

                            7 -> {
                                if (cpur3p1_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p1, cpur3p1_name, cpur3p1_number, cpur3p1_id, cpur3p1_txt, cpur3p1_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            8 -> {
                                if (cpur3p2_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p2, cpur3p2_name, cpur3p2_number, cpur3p2_id, cpur3p2_txt, cpur3p2_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            9 -> {
                                if (cpur3p3_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p3, cpur3p3_name, cpur3p3_number, cpur3p3_id, cpur3p3_txt, cpur3p3_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            10 -> {
                                if (cpur3p4_select?.visibility == View.VISIBLE) {
                                    cpuPlayerClickWork(cpur3p4, cpur3p4_name, cpur3p4_number, cpur3p4_id, cpur3p4_txt, cpur3p4_select)
                                } else {
                                    autoButtonClick()
                                }
                            }
                            11 -> {
                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
                                    if (cpur0p1_select?.visibility == View.VISIBLE) {
                                        cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                    } else {
                                        autoButtonClick()
                                    }
                                }else{
                                    cpuPlayerClickWork(cpur0p1, cpur0p1_name, cpur0p1_number, cpur0p1_id, cpur0p1_txt, cpur0p1_select)
                                }

                            }

                        }
                    }  // Done
                    else -> {
                        Toast.makeText(requireContext(), "Formation is not Exist $onSelectedScreen", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                try {
                    when (onSelectedScreen) {
                        "5-2-3" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility
                            cpur1p5_select.visibility=view!!.visibility
                            cpur0p1_select.visibility=view!!.visibility //Shrawan
                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()
                        }  // Done

                        "5-4-1" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility
                            cpur1p5_select.visibility=view!!.visibility
                            cpur0p1_select.visibility=view!!.visibility //Shrawan
                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()
                        }  // Done

                        "5-3-2" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility
                            cpur1p5_select.visibility=view!!.visibility
                            cpur0p1_select.visibility=view!!.visibility //Shrawan
                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()

                        }  // Done

                        "3-5-2" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur0p1_select.visibility=view!!.visibility //Shrawan
                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()
                        }  // Done

                        "4-5-1" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility
                            cpur0p1_select.visibility=view!!.visibility //Shrawan
                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()
                        }  // Done

                        "4-4-2" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility
                            cpur0p1_select.visibility=view!!.visibility //Shrawan
                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()
                        }  // Done

                        "4-3-3" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility
                            cpur0p1_select.visibility=view!!.visibility //Shrawan
                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()
                        }  // Done

                        "3-4-3" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur0p1_select.visibility=view!!.visibility //Shrawan
                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()

                        }  // Done

                        "4-2-4" -> {
                            cpur1p1_select.visibility=view!!.visibility
                            cpur1p2_select.visibility=view!!.visibility
                            cpur1p3_select.visibility=view!!.visibility
                            cpur1p4_select.visibility=view!!.visibility
                            cpur0p1_select.visibility=view!!.visibility //Shrawan
                            sessionManager.disableLifeLine11(false)
                            autoButtonClick()
                        }  // Done
                        else -> {
                            Toast.makeText(requireContext(), "Formation is not Exist $onSelectedScreen", Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (e:Exception){
                    Log.d("******","Error :- "+e.message.toString())
                }
            }

        }
    }

    //This function is used for pass ball feature
    private fun userPassBall() {
        if (!isTimerFinish) {
//            sessionManager.saveMyPass(1)
            playerSelectProcessComplete = false
            selectionPower = true
            isGoalClick = false
            if (userType.equals("USER",true)) {
                selectPlayPlayer(sessionManager.getMySelectedTeamPlayerNum())
            } else {
                autoButtonClick()
            }
        }
    }

    //This function is used for shoot ball feature
    private fun shootBall() {
        if (!isTimerFinish) {
            getTestUserShoot()
            val playerPower: String = if (userType.equals("USER",true)) {
                allCpuPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].type.uppercase()
            } else {

                allUserPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].type.uppercase()
            }
            if (userType.equals("USER",true)) {
                cpuDbHelper.updatePlayerUse(allCpuPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].id.toLong(), "true")

                cpuDbHelper.updatePlayerAnswer(allCpuPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].id.toLong(), "true")


                Log.d("@@@Error ", "myPass move screen$myPass")
                Log.d("@@@Error ", "cpuPass move screen$cpuPass")


                val bundle = Bundle()
                bundle.putString("userType", "USER")
                bundle.putInt("selected_player_num", selectedPlayerNum!!.text.toString().toInt())
                val size = 0/*if (playerPower == "PURPLE") {
                    1
                } else {
                    0
                }*/
                for (data in allUserPlayer) {
                    if (data.id.toInt() == selectedPlayerNum!!.text.toString().toInt()) {
                        if (data.designation.uppercase() == "DF") {
                            bundle.putInt("size", size + 2 + myPass)
                        } else {
                            if (data.designation.uppercase() == "MF") {
                                bundle.putInt("size", size + 3 + myPass)
                            } else {
                                bundle.putInt("size", size + 4 + myPass)
                            }
                        }
                    }
                }
//                findNavController().navigate(R.id.action_playScreenFragment_to_shoot_Screen, bundle)
                findNavController().navigate(R.id.shoot_Screen, bundle)
                Log.e("Work", "Shoot Button")
            } else {
                myPlayerDbHelper.updatePlayerUse(allUserPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].id.toLong(), "true")
                myPlayerDbHelper.updatePlayerAnswer(allUserPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].id.toLong(), "true")
                var size = 0/*if (playerPower == "PURPLE") {
                    1
                } else {
                    0
                }*/

                Log.d("@@@Error ", "myPass move screen$myPass")
                Log.d("@@@Error ", "cpuPass move screen$cpuPass")

                val bundle = Bundle()
                bundle.putString("userType", "USER")
                for (data in allCpuPlayer) {
                    if (data.id.toInt() == selectedPlayerNum!!.text.toString().toInt()) {
                        size = if (data.designation.uppercase() == "DF") {
                            size + 2 + cpuPass
                        } else {
                            if (data.designation.uppercase() == "MF") {
                                size + 3 + cpuPass
                            } else {
                                size + 4 + cpuPass
                            }
                        }
                    }
                }
                val num = setGames.getRandomNumber(size)
                Log.e("Gold Kick", num.toString())
                Log.d("******", "play screen  :=" + num)
                bundle.putInt("select_box", num)
                bundle.putInt("size", size)
                bundle.putInt("selected_player_num", selectedPlayerNum!!.text.toString().toInt())
                Log.e("Shoot to Kick", bundle.toString())
//                findNavController().navigate(R.id.action_playScreenFragment_to_fragment_goalkeaper, bundle)
                findNavController().navigate(R.id.goal_keeper_Screen, bundle)
            }
        }
    }

    //This function is used for on select player action by user
    private fun playerClickWork(playerBox: ConstraintLayout, playerName: TextView, playerNum: TextView, playerId: TextView, playerTxtBox: LinearLayout, selectCircle: View) {
        if (!isTimerFinish) {
            if (!playerSelectProcessComplete) {
                if (playerBox.visibility == View.VISIBLE) {
                    if (totalPlayerNameShow == 0) {
                        val bundle = Bundle()
                        bundle.putString("Name", playerName.text.toString())
                        bundle.putString("userType", userType)
                        bundle.putString("Num", playerNum.text.toString())
                        bundle.putString("id", playerId.text.toString())
                        selectedPlayerNum?.text = playerId.text.toString()
                        Log.e("Send Detail of Quiz", userType + " " + playerName.text.toString() + " " + playerNum.text.toString())
//                        findNavController().navigateSafe(R.id.action_playScreenFragment_to_guess_name, bundle)
                        findNavController().navigate(R.id.player_name_guess, bundle)
                        Log.e("Work", "Condition1")
                    } else {
                        if (selectionPower) {
                            if (selectCircle.visibility == View.VISIBLE) {
                                if (playerTxtBox.visibility == View.VISIBLE) {
                                    selectionPower = false
                                    playerSelectProcessComplete = true
                                    selectedPlayerNum = playerId
                                    selectionView = selectCircle
                                    root_button.visibility = View.VISIBLE
                                    Log.e("Work", "Condition3")
                                } else {
                                    val bundle = Bundle()
                                    selectionPower = false
                                    playerSelectProcessComplete = true
//                                    selectCircle.setBackgroundResource(R.drawable.selected_circle)
                                    bundle.putString("Name", playerName.text.toString())
                                    bundle.putString("userType", userType)
                                    bundle.putString("Num", playerNum.text.toString())
                                    bundle.putString("id", playerId.text.toString())
                                    selectedPlayerNum = playerId
//                                    if (totalPlayerNameShow == 1) {
//                                        sessionManager.saveMyPass(1)
//                                    }
                                    //Shrawan
                                    Log.e("@@@@@@@@@@@", "${playerBox.id} ${r0p1.id}")
                                    if (playerBox.id==r0p1.id){
                                        isGoalClick = true
                                    }else{
                                        isGoalClick = false
                                    }
                                    bundle.putBoolean("isGoalClick",isGoalClick) //Shrawan
                                    findNavController().navigate(R.id.player_name_guess, bundle)
//                                    findNavController().navigateSafe(R.id.action_playScreenFragment_to_guess_name, bundle)
                                    Log.e("Send Detail of Quiz", userType + " " + playerName.text.toString() + " " + playerNum.text.toString())
                                    Log.e("Work", "Condition4")
                                }
                            } else {
                                Log.e("Work", "Player out of Range")
                            }
                        } else {
                            if (playerTxtBox.visibility == View.VISIBLE) {
                                selectionPower = true
                                showPlayerReachPowerUser(selectCircle, playerId)
                                Log.e("Work", "Condition2")
                            } else {
                                Toast.makeText(requireActivity(), "Player are not Locked!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    //This function is used for on select player action by cpu
    private fun cpuPlayerClickWork(playerBox: ConstraintLayout?, playerName: TextView?, playerNum: TextView?, playerId: TextView?, playerTxtBox: LinearLayout?, selectCircle: View?) {
        try {
            if (!isTimerFinish) {
                if (!playerSelectProcessComplete) {
                    if (playerBox?.visibility == View.VISIBLE) {
                        if (cpuPlayerNameShow == 0) {
                            val bundle = Bundle()
                            bundle.putString("Name", playerName?.text.toString())
                            bundle.putString("userType", userType)
                            bundle.putString("Num", playerNum?.text.toString())
                            bundle.putString("id", playerId?.text.toString())
                            selectedPlayerNum = playerId
                            Log.d("******", "selectedPlayerNum :- " + playerId?.text.toString())
                            Log.e("Send Detail of Quiz", userType + " " + playerName?.text.toString() + " " + playerNum?.text.toString())
                            Log.e("Work", "Condition1")
//                            findNavController().navigateSafe(R.id.action_playScreenFragment_to_guess_name, bundle)
                            findNavController().navigate(R.id.player_name_guess, bundle)


                        } else {
                            if (selectionPower) {
                                if (selectCircle!!.visibility == View.VISIBLE) {
                                    if (playerTxtBox!!.visibility == View.VISIBLE) {
                                        selectionPower = false
                                        playerSelectProcessComplete = true
                                        selectedPlayerNum = playerId
                                        selectionView = selectCircle
                                        Log.d("******", "selectedPlayerNum :- " + playerId?.text.toString())
//                                        selectCircle.setBackgroundResource(R.drawable.selected_circle)
                                        root_button.visibility = View.GONE
                                        autoButtonSelectPassShoot()
                                        Log.e("Work", "Condition3")
                                    } else {
                                        val bundle = Bundle()
                                        selectionPower = false
                                        playerSelectProcessComplete = true
//                                        selectCircle.setBackgroundResource(R.drawable.selected_circle)
                                        bundle.putString("Name", playerName?.text.toString())
                                        bundle.putString("userType", userType)
                                        bundle.putString("Num", playerNum?.text.toString())
                                        bundle.putString("id", playerId?.text.toString())
//                                        if (cpuPlayerNameShow == 1) {
//                                            sessionManager.saveCpuPass(1)
//                                        }
                                        Log.e("Send Detail of Quiz", userType + " " + playerName?.text.toString() + " " + playerNum?.text.toString())
                                        Log.d("******", "selectedPlayerNum :- " + playerId?.text.toString())
                                        Log.e("Work", "Condition4")
                                        findNavController().navigate(R.id.player_name_guess, bundle)
                                    }
                                } else {
                                    if (cpuPlayerNameShow!=0){
                                        val randomNumber = Random.nextInt(2, 10)
                                        // Check if the number is even or odd
                                        val result = if (randomNumber % 2 == 0) {
                                            "even"
                                        } else {
                                            "odd"
                                        }
                                        if (result.equals("even",true)){
                                            autoButtonSelectPassShoot()
                                            Log.e("Shoot ", "player Shoot range")
                                        }else{
                                            autoButtonClick()
                                            Log.e("Work", "player out off range")
                                        }
                                    }else{
                                        autoButtonClick()
                                        Log.e("Work", "player out off range")
                                    }
                                }
                            } else {
                                if (playerTxtBox?.visibility == View.VISIBLE) {
                                    selectionPower = true
                                    showPlayerReachPowerUser(selectCircle!!, playerId!!)
                                    autoButtonClick()
                                    Log.e("Work", "Condition2")
                                } else {
                                    autoButtonClick()
                                }
                            }
                        }
                    } else {
                        Log.e("PLayer Button", "Not Visible")
                        autoButtonClick()
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("****", "error := " + e.message)
        }
    }

    //This function is used for auto  select pass or shoot (AI Module) in cpu term
    private fun autoButtonSelectPassShoot() {
        if (!isTimerFinish) {
            if (status){
                when (Random.nextInt(1, 3)) {
                    1 -> {
                        /*shootBall*/
                        //Shrawan
                        if (!isGoalClick) {
                            shootBall()
                        }else{
                            isGoalClick = false
                        }
                    }
                    else -> {
                        userPassBall()
                    }
                }
            }

        }
    }

    //This function is used for set ground formation
    @SuppressLint("SetTextI18n")
    private fun setScreens() {
        allCpuPlayer = cpuDbHelper.getAllPlayers()
        allUserPlayer = myPlayerDbHelper.getAllPlayers()
        Log.e("All Cpu Player", allCpuPlayer.toString())
        Log.e("All User Player", allUserPlayer.toString())
        Log.e("All name ", sessionManager.getName().toString())
        if (sessionManager.getMatchType() == "worldcup") {
            teamDbHelper = TeamDatabaseHelper(requireContext())
            val allTeam = teamDbHelper.getAllTeams()
            for (data in allTeam) {
                if (data.teamID == 1) {
                    user_name.text = data.captainName
                }
                /*if (data.teamID == sessionManager.getTeamDetails()) {
                    oppose_team_player_name.text = data.captainName
                }*/
                if(sessionManager.getGameNumber() <= 3)  { //for first 3 match
                    oppose_team_player_name.text = data.captainName
                }else if(sessionManager.getGameNumber() == 4){ // for 4th match
                    oppose_team_player_name.text = "Asst. Manager"
                }else if(sessionManager.getGameNumber() == 5){ // for 5th match
                    oppose_team_player_name.text = "Lizard mascot"
                }
            }

        } else {
            user_name.text = sessionManager.getName()?.split(" ")?.drop(1)?.joinToString(" ")
            oppose_team_player_name.text = "CPU"
        }

        cpu_score_tv.text = sessionManager.getCpuScore().toString()
        user_goal_tv.text = sessionManager.getMyScore().toString()
        if (!isTimerFinish) {
            val screen = setGames.setScreen(sessionManager.getUserScreenType())
            userR1 = screen.r1
            userR2 = screen.r2
            userR3 = screen.r3
            val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
            cpuR1 = cpuScreen.r1
            cpuR2 = cpuScreen.r2
            cpuR3 = cpuScreen.r3
            setRowOfScreens()
        }
    }

    //This function is used for set the player in row
    private fun setRowOfScreens() {
        if (!isTimerFinish) {
            when (userR1) {
                4 -> {
                    r1.weightSum = 4f
                    r1p5.visibility = View.GONE
                }
                3 -> {
                    r1.weightSum = 3f
                    r1p5.visibility = View.GONE
                    r1p4.visibility = View.GONE
                }
                2 -> {
                    r1.weightSum = 2f
                    r1p5.visibility = View.GONE
                    r1p4.visibility = View.GONE
                    r1p3.visibility = View.GONE
                }
                1 -> {
                    r1.weightSum = 1f
                    r1p5.visibility = View.GONE
                    r1p4.visibility = View.GONE
                    r1p3.visibility = View.GONE
                    r1p2.visibility = View.GONE
                }
            }
            when (userR2) {
                4 -> {
                    r2.weightSum = 4f
                    r2p5.visibility = View.GONE
                }
                3 -> {
                    r2.weightSum = 3f
                    r2p5.visibility = View.GONE
                    r2p4.visibility = View.GONE
                }
                2 -> {
                    r2.weightSum = 2f
                    r2p5.visibility = View.GONE
                    r2p4.visibility = View.GONE
                    r2p3.visibility = View.GONE
                }
                1 -> {
                    r2.weightSum = 1f
                    r2p5.visibility = View.GONE
                    r2p4.visibility = View.GONE
                    r2p3.visibility = View.GONE
                    r2p2.visibility = View.GONE
                }
            }
            when (userR3) {
                4 -> {
                    r3.weightSum = 4f
                    r3p5.visibility = View.GONE
                }
                3 -> {
                    r3.weightSum = 3f
                    r3p5.visibility = View.GONE
                    r3p4.visibility = View.GONE
                }
                2 -> {
                    r3.weightSum = 2f
                    r3p5.visibility = View.GONE
                    r3p4.visibility = View.GONE
                    r3p3.visibility = View.GONE
                }
                1 -> {
                    r3.weightSum = 1f
                    r3p5.visibility = View.GONE
                    r3p4.visibility = View.GONE
                    r3p3.visibility = View.GONE
                    r3p2.visibility = View.GONE
                }
            }
            when (cpuR1) {
                4 -> {
                    cpur1.weightSum = 4f
                    cpur1p5.visibility = View.GONE
                }
                3 -> {
                    cpur1.weightSum = 3f
                    cpur1p5.visibility = View.GONE
                    cpur1p4.visibility = View.GONE
                }
                2 -> {
                    cpur1.weightSum = 2f
                    cpur1p5.visibility = View.GONE
                    cpur1p4.visibility = View.GONE
                    cpur1p3.visibility = View.GONE
                }
                1 -> {
                    cpur1.weightSum = 1f
                    cpur1p5.visibility = View.GONE
                    cpur1p4.visibility = View.GONE
                    cpur1p3.visibility = View.GONE
                    cpur1p2.visibility = View.GONE
                }
            }
            when (cpuR2) {
                4 -> {
                    cpur2.weightSum = 4f
                    cpur2p5.visibility = View.GONE
                }
                3 -> {
                    cpur2.weightSum = 3f
                    cpur2p5.visibility = View.GONE
                    cpur2p4.visibility = View.GONE
                }
                2 -> {
                    cpur2.weightSum = 2f
                    cpur2p5.visibility = View.GONE
                    cpur2p4.visibility = View.GONE
                    cpur2p3.visibility = View.GONE
                }
                1 -> {
                    cpur2.weightSum = 1f
                    cpur2p5.visibility = View.GONE
                    cpur2p4.visibility = View.GONE
                    cpur2p3.visibility = View.GONE
                    cpur2p2.visibility = View.GONE
                }
            }
            when (cpuR3) {
                4 -> {
                    cpur3.weightSum = 4f
                    cpur3p5.visibility = View.GONE
                }
                3 -> {
                    cpur3.weightSum = 3f
                    cpur3p5.visibility = View.GONE
                    cpur3p4.visibility = View.GONE
                }
                2 -> {
                    cpur3.weightSum = 2f
                    cpur3p5.visibility = View.GONE
                    cpur3p4.visibility = View.GONE
                    cpur3p3.visibility = View.GONE
                }
                1 -> {
                    cpur3.weightSum = 1f
                    cpur3p5.visibility = View.GONE
                    cpur3p4.visibility = View.GONE
                    cpur3p3.visibility = View.GONE
                    cpur3p2.visibility = View.GONE
                }
            }
            hideAllPlayerName()
        }
    }

    //This function is used for hide and unhidden player name according to gusse name
    private fun hideAllPlayerName() {
        if (!isTimerFinish) {
            r1p1_txt.visibility = View.GONE
            r1p2_txt.visibility = View.GONE
            r1p3_txt.visibility = View.GONE
            r1p4_txt.visibility = View.GONE
            r1p5_txt.visibility = View.GONE

            r2p1_txt.visibility = View.GONE
            r2p2_txt.visibility = View.GONE
            r2p3_txt.visibility = View.GONE
            r2p4_txt.visibility = View.GONE
            r2p5_txt.visibility = View.GONE

            r3p1_txt.visibility = View.GONE
            r3p2_txt.visibility = View.GONE
            r3p3_txt.visibility = View.GONE
            r3p4_txt.visibility = View.GONE
            r3p5_txt.visibility = View.GONE
            r0p1_txt.visibility = View.GONE //Shrawan

            cpur1p1_txt.visibility = View.GONE
            cpur1p2_txt.visibility = View.GONE
            cpur1p3_txt.visibility = View.GONE
            cpur1p4_txt.visibility = View.GONE
            cpur1p5_txt.visibility = View.GONE

            cpur2p1_txt.visibility = View.GONE
            cpur2p2_txt.visibility = View.GONE
            cpur2p3_txt.visibility = View.GONE
            cpur2p4_txt.visibility = View.GONE
            cpur2p5_txt.visibility = View.GONE

            cpur3p1_txt.visibility = View.GONE
            cpur3p2_txt.visibility = View.GONE
            cpur3p3_txt.visibility = View.GONE
            cpur3p4_txt.visibility = View.GONE
            cpur3p5_txt.visibility = View.GONE
            cpur0p1_txt.visibility = View.GONE //Shrawan

            hideAllSelectCircle()
        }
    }


    //This function is used for hide all select circle
    private fun hideAllSelectCircle() {
        if (!isTimerFinish) {
            setImageCircleCPUAndUser("hideImage")
            setUserScreenPlayerNameDtl()
        }
    }
    // This function call for set circle background img and hide the all image
    private fun setImageCircleCPUAndUser(type:String){

        r1p1_select.visibility = View.GONE
        r1p2_select.visibility = View.GONE
        r1p3_select.visibility = View.GONE
        r1p4_select.visibility = View.GONE
        r1p5_select.visibility = View.GONE

        r2p1_select.visibility = View.GONE
        r2p2_select.visibility = View.GONE
        r2p3_select.visibility = View.GONE
        r2p4_select.visibility = View.GONE
        r2p5_select.visibility = View.GONE

        r3p1_select.visibility = View.GONE
        r3p2_select.visibility = View.GONE
        r3p3_select.visibility = View.GONE
        r3p4_select.visibility = View.GONE
        r3p5_select.visibility = View.GONE
        r0p1_select.visibility = View.GONE //Shrawan

        cpur1p1_select.visibility = View.GONE
        cpur1p2_select.visibility = View.GONE
        cpur1p3_select.visibility = View.GONE
        cpur1p4_select.visibility = View.GONE
        cpur1p5_select.visibility = View.GONE

        cpur2p1_select.visibility = View.GONE
        cpur2p2_select.visibility = View.GONE
        cpur2p3_select.visibility = View.GONE
        cpur2p4_select.visibility = View.GONE
        cpur2p5_select.visibility = View.GONE

        cpur3p1_select.visibility = View.GONE
        cpur3p2_select.visibility = View.GONE
        cpur3p3_select.visibility = View.GONE
        cpur3p4_select.visibility = View.GONE
        cpur3p5_select.visibility = View.GONE
        cpur0p1_select.visibility = View.GONE //Shrawan

        if (type.equals("showImage",true)){
            r1p1_select.setBackgroundResource(R.drawable.select_circle)
            r1p2_select.setBackgroundResource(R.drawable.select_circle)
            r1p3_select.setBackgroundResource(R.drawable.select_circle)
            r1p4_select.setBackgroundResource(R.drawable.select_circle)
            r1p5_select.setBackgroundResource(R.drawable.select_circle)

            r2p1_select.setBackgroundResource(R.drawable.select_circle)
            r2p2_select.setBackgroundResource(R.drawable.select_circle)
            r2p3_select.setBackgroundResource(R.drawable.select_circle)
            r2p4_select.setBackgroundResource(R.drawable.select_circle)
            r2p5_select.setBackgroundResource(R.drawable.select_circle)

            r3p1_select.setBackgroundResource(R.drawable.select_circle)
            r3p2_select.setBackgroundResource(R.drawable.select_circle)
            r3p3_select.setBackgroundResource(R.drawable.select_circle)
            r3p4_select.setBackgroundResource(R.drawable.select_circle)
            r3p5_select.setBackgroundResource(R.drawable.select_circle)

            r0p1_select.setBackgroundResource(R.drawable.select_circle) //Shrawan

            cpur1p1_select.setBackgroundResource(R.drawable.select_circle)
            cpur1p2_select.setBackgroundResource(R.drawable.select_circle)
            cpur1p3_select.setBackgroundResource(R.drawable.select_circle)
            cpur1p4_select.setBackgroundResource(R.drawable.select_circle)
            cpur1p5_select.setBackgroundResource(R.drawable.select_circle)

            cpur2p1_select.setBackgroundResource(R.drawable.select_circle)
            cpur2p2_select.setBackgroundResource(R.drawable.select_circle)
            cpur2p3_select.setBackgroundResource(R.drawable.select_circle)
            cpur2p4_select.setBackgroundResource(R.drawable.select_circle)
            cpur2p5_select.setBackgroundResource(R.drawable.select_circle)

            cpur3p1_select.setBackgroundResource(R.drawable.select_circle)
            cpur3p2_select.setBackgroundResource(R.drawable.select_circle)
            cpur3p3_select.setBackgroundResource(R.drawable.select_circle)
            cpur3p4_select.setBackgroundResource(R.drawable.select_circle)
            cpur3p5_select.setBackgroundResource(R.drawable.select_circle)

            cpur0p1_select.setBackgroundResource(R.drawable.select_circle) //Shrawan


        }
    }
    //This function is used for userSelect
    private fun showPlayerReachPowerUser(player: View, playerId: TextView) {
        if (!isTimerFinish) {
            setImageCircleCPUAndUser("showImage")
            val onSelectedScreen = if (userType.equals("USER",true)) {
                sessionManager.getUserScreenType()
            } else {
                sessionManager.getCpuScreenType()
            }
            val playerPower: String = if (userType.equals("USER",true)) {
                allCpuPlayer[playerId.text.toString().toInt() - 1].type.uppercase()
            } else {
                allUserPlayer[playerId.text.toString().toInt() - 1].type.uppercase()
            }
            Log.d("@@@@@@@@@@@", "onSelectedScreen :- " + onSelectedScreen)
            Log.d("@@@@@@@@@@@", "playerPower :- " + playerId.text.toString())
            Log.d("@@@@@@@@@@@", "player :- " + player)
            when (onSelectedScreen) {
                "5-2-3" -> {
                    when (player) {

                        // User
                        // Row 1
                        r3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p3_select.visibility = View.GONE
                                } else {
                                    r3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r3p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                            }
                        }


                        // Row 2
                        r2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        r2p2_select -> {

                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p3_select.visibility = View.GONE
                                } else {
                                    r3p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // Row 3
                        r1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }

                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }

                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        r1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r1p5_select.visibility = View.GONE
                                } else {
                                    r1p5_select.visibility = View.VISIBLE
                                }

                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p5_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }

                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }

                        //Shrawan
                        r0p1_select -> {
                            Log.d("@@@@@@@@@@@", "player :- ${player.id}  ${r0p1_select.id}")
                            if (playerPower.equals("RED", true)) {
                                Log.d("@@@@@@@@@@@", "player :- ${player.id}  ${r0p1_select.id}  $playerPower")
                                playerUserCpu5_2_3()
                            }
                            else {
                                Log.d("@@@@@@@@@@@", "player :- ${player.id}  ${r0p1_select.id}")
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r1p5_select.visibility = View.GONE
                                } else {
                                    r1p5_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // CPU
                        // Row 1
                        cpur3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p3_select.visibility = View.GONE
                                } else {
                                    cpur3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur3p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                cpur3p2_select.visibility = View.VISIBLE
                                cpur2p2_select.visibility = View.VISIBLE

                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // Row 2
                        cpur2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {

                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p3_select.visibility = View.GONE
                                } else {
                                    cpur3p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // Row 3
                        cpur1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }

                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {

                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {

                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur1p5_select.visibility = View.GONE
                                } else {
                                    cpur1p5_select.visibility = View.VISIBLE
                                }

                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur1p5_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            } else {
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }

                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }

                        //Shrawan
                        cpur0p1_select -> {
                            Log.d("*****", "player :- " + player)
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_2_3()
                            }
                            else {
                                Log.d("*****", "player :- " + player)
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    cpur1p5_select.visibility = View.GONE
                                } else {
                                    cpur1p5_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                            }

                        }


                    }

                } // Done

                "5-4-1" -> {
                    when (player) {
                        // User
                        // Row 1
                        r3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }

                            }
                        }

                        // Row 2
                        r2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {

                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.VISIBLE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        r2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {

                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.VISIBLE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }

                            }

                        }
                        r2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {

                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.VISIBLE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }

                            }

                        }
                        r2p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {

                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.VISIBLE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r1p5_select.visibility = View.GONE
                                } else {
                                    r1p5_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }

                            }
                        }

                        // Row 3
                        r1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {

                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.VISIBLE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }

                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }

                            }
                        }
                        r1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {

                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.VISIBLE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.VISIBLE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.VISIBLE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.VISIBLE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.VISIBLE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r1p5_select.visibility = View.VISIBLE
                                } else {
                                    r1p5_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p5_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.VISIBLE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        r0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r1p5_select.visibility = View.GONE
                                } else {
                                    r1p5_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                            }
                        }


                        // CPU
                        // Row 1
                        cpur3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }

                            }
                        }

                        // Row 2
                        cpur2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {

                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.VISIBLE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {

                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.VISIBLE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }

                            }

                        }
                        cpur2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {

                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.VISIBLE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }

                            }

                        }
                        cpur2p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {

                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.VISIBLE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur1p5_select.visibility = View.GONE
                                } else {
                                    cpur1p5_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }

                            }
                        }

                        // Row 3
                        cpur1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {

                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.VISIBLE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }

                            }
                        }
                        cpur1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {

                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.VISIBLE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.VISIBLE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.VISIBLE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.VISIBLE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.VISIBLE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur1p5_select.visibility = View.VISIBLE
                                } else {
                                    cpur1p5_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p5_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            } else {
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.VISIBLE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        cpur0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_4_1()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    cpur1p5_select.visibility = View.GONE
                                } else {
                                    cpur1p5_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                } // Done
                "5-3-2" -> {
                    when (player) {
                        // User
                        // Row 3
                        r3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // Row 2

                        r2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        r2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r1p5_select.visibility = View.GONE
                                } else {
                                    r1p5_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // Row 1
                        r1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        r1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        r1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r1p5_select.visibility = View.GONE
                                } else {
                                    r1p5_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p5_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        r0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r1p5_select.visibility = View.GONE
                                } else {
                                    r1p5_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                            }
                        }



                        // CPU
                        // Row 3
                        cpur3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // Row 2
                        cpur2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur1p5_select.visibility = View.GONE
                                } else {
                                    cpur1p5_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // Row 1
                        cpur1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur1p5_select.visibility = View.GONE
                                } else {
                                    cpur1p5_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p5_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        cpur0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu5_3_2()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    cpur1p5_select.visibility = View.GONE
                                } else {
                                    cpur1p5_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                            }
                        }


                    }
                } // Done

                "3-5-2" -> {
                    when (player) {

                        // User
                        // Row 1

                        r3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        r3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                            }

                        }

                        // Row 2
                        r2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        r2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        r2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {

                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }


                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }

                        }

                        r2p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {

                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }


                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p5_select.visibility = View.GONE
                                } else {
                                    r2p5_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }

                        }

                        r2p5_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // Row 3
                        r1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {

                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        r1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {

                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }

                            }
                        }

                        r1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p5_select.visibility = View.GONE
                                } else {
                                    r2p5_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }

                            }
                        }

                        //Shrawan
                        r0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p5_select.visibility = View.GONE
                                } else {
                                    r2p5_select.visibility = View.VISIBLE
                                }

                            }
                        }



                        // CPU

                        // Row 1

                        cpur3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        cpur3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                            }

                        }

                        // Row 2
                        cpur2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        cpur2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        cpur2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {

                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }


                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }

                        }

                        cpur2p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {

                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }


                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p5_select.visibility = View.GONE
                                } else {
                                    cpur2p5_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }

                        }

                        cpur2p5_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // Row 3
                        cpur1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {

                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        cpur1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {

                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }

                            }
                        }

                        cpur1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p5_select.visibility = View.GONE
                                } else {
                                    cpur2p5_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }

                            }
                        }

                        //Shrawan
                        cpur0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_5_2()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    cpur2p5_select.visibility = View.GONE
                                } else {
                                    cpur2p5_select.visibility = View.VISIBLE
                                }

                            }
                        }

                    }
                } // Done


                "4-5-1" -> {
                    when (player) {
                        // User
                        // Row 1
                        r3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }

                            }

                        }
                        // Row 2
                        r2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        r2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {

                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r2p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r2p5_select.visibility = View.GONE
                                } else {
                                    r2p5_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        r2p5_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }

                            }
                        }
                        // Row 3
                        r1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        r1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }


                            }

                        }
                        r1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r2p5_select.visibility = View.GONE
                                } else {
                                    r2p5_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        r0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r2p5_select.visibility = View.GONE
                                } else {
                                    r2p5_select.visibility = View.VISIBLE
                                }

                            }
                        }

                        // CPU
                        // Row 1
                        cpur3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }

                            }

                        }
                        // Row 2
                        cpur2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {

                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur2p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur2p5_select.visibility = View.GONE
                                } else {
                                    cpur2p5_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur2p5_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }

                            }
                        }
                        // Row 3
                        cpur1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }

                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }


                            }

                        }
                        cpur1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }

                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur2p5_select.visibility = View.GONE
                                } else {
                                    cpur2p5_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        cpur0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_5_1()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    cpur2p5_select.visibility = View.GONE
                                } else {
                                    cpur2p5_select.visibility = View.VISIBLE
                                }

                            }
                        }
                    }
                } // Done
                "4-4-2" -> {
                    when (player) {
                        // User
                        // Row 1
                        r3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }

                            }

                        }
                        // Row 2
                        r2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }

                            }
                        }
                        r2p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 3
                        r1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }

                            }

                        }
                        r1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        r0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // CPU
                        // Row 1
                        cpur3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }

                            }

                        }
                        // Row 2
                        cpur2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }

                            }
                        }
                        cpur2p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 3
                        cpur1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }

                            }

                        }
                        cpur1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        cpur0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_4_2()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                } // Done

                "4-3-3" -> {
                    when (player) {
                        // User
                        // Row 3
                        r3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        r3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p3_select.visibility = View.GONE
                                } else {
                                    r3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r3p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 2
                        r2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p3_select.visibility = View.GONE
                                } else {
                                    r3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 1
                        r1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        r1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        r0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // CPU
                        // Row 3
                        cpur3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p3_select.visibility = View.GONE
                                } else {
                                    cpur3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur3p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 2
                        cpur2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p3_select.visibility = View.GONE
                                } else {
                                    cpur3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 1
                        cpur1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        cpur0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_3_3()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                            }
                        }


                    }
                } // Done
                "3-4-3" -> {
                    when (player) {
                        // User
                        // Row 3
                        r3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }

                            }
                        }
                        r3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p3_select.visibility = View.GONE
                                } else {
                                    r3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r3p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }

                            }

                        }
                        // Row 2
                        r2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }

                            }
                        }
                        r2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p3_select.visibility = View.GONE
                                } else {
                                    r3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r2p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p3_select.visibility = View.GONE
                                } else {
                                    r3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 1
                        r1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        r0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p3_select.visibility = View.GONE
                                } else {
                                    r2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r2p4_select.visibility = View.GONE
                                } else {
                                    r2p4_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // Cpu
                        // Row 3
                        cpur3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }

                            }
                        }
                        cpur3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p3_select.visibility = View.GONE
                                } else {
                                    cpur3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur3p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 2
                        cpur2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }

                            }
                        }
                        cpur2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur2p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p3_select.visibility = View.GONE
                                } else {
                                    cpur3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur2p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p3_select.visibility = View.GONE
                                } else {
                                    cpur3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 1
                        cpur1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }
                        cpur1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }

                        }

                        //Shrawan
                        cpur0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu3_4_3()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    cpur2p3_select.visibility = View.GONE
                                } else {
                                    cpur2p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    cpur2p4_select.visibility = View.GONE
                                } else {
                                    cpur2p4_select.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                } // Done
                "4-2-4" -> {
                    when (player) {
                        // User
                        // Row 1
                        r3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[6].answer.equals("true", true)) {
                                    r3p1_select.visibility = View.GONE
                                } else {
                                    r3p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p3_select.visibility = View.GONE
                                } else {
                                    r3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r3p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[9].answer.equals("true", true)) {
                                    r3p4_select.visibility = View.GONE
                                } else {
                                    r3p4_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r3p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p3_select.visibility = View.GONE
                                } else {
                                    r3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 2
                        r2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[7].answer.equals("true", true)) {
                                    r3p2_select.visibility = View.GONE
                                } else {
                                    r3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[8].answer.equals("true", true)) {
                                    r3p3_select.visibility = View.GONE
                                } else {
                                    r3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 3
                        r1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        r1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    r0p1_select.visibility = View.GONE
                                } else {
                                    r0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        r0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    r1p1_select.visibility = View.GONE
                                } else {
                                    r1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    r1p2_select.visibility = View.GONE
                                } else {
                                    r1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    r1p3_select.visibility = View.GONE
                                } else {
                                    r1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    r1p4_select.visibility = View.GONE
                                } else {
                                    r1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    r2p1_select.visibility = View.GONE
                                } else {
                                    r2p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    r2p2_select.visibility = View.GONE
                                } else {
                                    r2p2_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        // Cpu
                        // Row 1
                        cpur3p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur3p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[6].answer.equals("true", true)) {
                                    cpur3p1_select.visibility = View.GONE
                                } else {
                                    cpur3p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p3_select.visibility = View.GONE
                                } else {
                                    cpur3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur3p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[9].answer.equals("true", true)) {
                                    cpur3p4_select.visibility = View.GONE
                                } else {
                                    cpur3p4_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur3p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p3_select.visibility = View.GONE
                                } else {
                                    cpur3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 2
                        cpur2p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[7].answer.equals("true", true)) {
                                    cpur3p2_select.visibility = View.GONE
                                } else {
                                    cpur3p2_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur2p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[8].answer.equals("true", true)) {
                                    cpur3p3_select.visibility = View.GONE
                                } else {
                                    cpur3p3_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        // Row 3
                        cpur1p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p2_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allUserPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p3_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allUserPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allUserPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }
                        cpur1p4_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            } else {
                                if (allUserPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                //Shrawan
                                if (allCpuPlayer[10].answer.equals("true", true)) {
                                    cpur0p1_select.visibility = View.GONE
                                } else {
                                    cpur0p1_select.visibility = View.VISIBLE
                                }
                            }
                        }

                        //Shrawan
                        cpur0p1_select -> {
                            if (playerPower.equals("RED", true)) {
                                playerUserCpu4_2_4()
                            }
                            else {
                                if (allCpuPlayer[0].answer.equals("true", true)) {
                                    cpur1p1_select.visibility = View.GONE
                                } else {
                                    cpur1p1_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[1].answer.equals("true", true)) {
                                    cpur1p2_select.visibility = View.GONE
                                } else {
                                    cpur1p2_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[2].answer.equals("true", true)) {
                                    cpur1p3_select.visibility = View.GONE
                                } else {
                                    cpur1p3_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[3].answer.equals("true", true)) {
                                    cpur1p4_select.visibility = View.GONE
                                } else {
                                    cpur1p4_select.visibility = View.VISIBLE
                                }
                                if (allCpuPlayer[4].answer.equals("true", true)) {
                                    cpur2p1_select.visibility = View.GONE
                                } else {
                                    cpur2p1_select.visibility = View.VISIBLE
                                }

                                if (allCpuPlayer[5].answer.equals("true", true)) {
                                    cpur2p2_select.visibility = View.GONE
                                } else {
                                    cpur2p2_select.visibility = View.VISIBLE
                                }
                            }
                        }

                    }
                } // Done
                else -> {
                    Toast.makeText(requireContext(), "Formation is not Exist $onSelectedScreen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // This function call for 5-2-3
    private fun playerUserCpu5_2_3() {
        // User
        if (allCpuPlayer[0].answer.equals("true", true)) {
            r1p1_select.visibility = View.GONE
        } else {
            r1p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[1].answer.equals("true", true)) {
            r1p2_select.visibility = View.GONE
        } else {
            r1p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[2].answer.equals("true", true)) {
            r1p3_select.visibility = View.GONE
        } else {
            r1p3_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[3].answer.equals("true", true)) {
            r1p4_select.visibility = View.GONE
        } else {
            r1p4_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[4].answer.equals("true", true)) {
            r1p5_select.visibility = View.GONE
        } else {
            r1p5_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[5].answer.equals("true", true)) {
            r2p1_select.visibility = View.GONE
        } else {
            r2p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[6].answer.equals("true", true)) {
            r2p2_select.visibility = View.GONE
        } else {
            r2p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[7].answer.equals("true", true)) {
            r3p1_select.visibility = View.GONE
        } else {
            r3p1_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[8].answer.equals("true", true)) {
            r3p2_select.visibility = View.GONE
        } else {
            r3p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[9].answer.equals("true", true)) {
            r3p3_select.visibility = View.GONE
        } else {
            r3p3_select.visibility = View.VISIBLE
        }

        //Shrawan
        if (allCpuPlayer[10].answer.equals("true", true)) {
            r0p1_select.visibility = View.GONE
        } else {
            r0p1_select.visibility = View.VISIBLE
        }

        // Cpu
        if (allUserPlayer[0].answer.equals("true", true)) {
            cpur1p1_select.visibility = View.GONE
        } else {
            cpur1p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[1].answer.equals("true", true)) {
            cpur1p2_select.visibility = View.GONE
        } else {
            cpur1p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[2].answer.equals("true", true)) {
            cpur1p3_select.visibility = View.GONE
        } else {
            cpur1p3_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[3].answer.equals("true", true)) {
            cpur1p4_select.visibility = View.GONE
        } else {
            cpur1p4_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[4].answer.equals("true", true)) {
            cpur1p5_select.visibility = View.GONE
        } else {
            cpur1p5_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[5].answer.equals("true", true)) {
            cpur2p1_select.visibility = View.GONE
        } else {
            cpur2p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[6].answer.equals("true", true)) {
            cpur2p2_select.visibility = View.GONE
        } else {
            cpur2p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[7].answer.equals("true", true)) {
            cpur3p1_select.visibility = View.GONE
        } else {
            cpur3p1_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[8].answer.equals("true", true)) {
            cpur3p2_select.visibility = View.GONE
        } else {
            cpur3p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[9].answer.equals("true", true)) {
            cpur3p3_select.visibility = View.GONE
        } else {
            cpur3p3_select.visibility = View.VISIBLE
        }

        //Shrawan
        if (allUserPlayer[10].answer.equals("true", true)) {
            cpur0p1_select.visibility = View.GONE
        } else {
            cpur0p1_select.visibility = View.VISIBLE
        }
    }
    // This function call for 5-4-1
    private fun playerUserCpu5_4_1() {
        // User
        if (allCpuPlayer[0].answer.equals("true", true)) {
            r1p1_select.visibility = View.GONE
        } else {
            r1p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[1].answer.equals("true", true)) {
            r1p2_select.visibility = View.GONE
        } else {
            r1p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[2].answer.equals("true", true)) {
            r1p3_select.visibility = View.GONE
        } else {
            r1p3_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[3].answer.equals("true", true)) {
            r1p4_select.visibility = View.GONE
        } else {
            r1p4_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[4].answer.equals("true", true)) {
            r1p5_select.visibility = View.GONE
        } else {
            r1p5_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[5].answer.equals("true", true)) {
            r2p1_select.visibility = View.GONE
        } else {
            r2p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[6].answer.equals("true", true)) {
            r2p2_select.visibility = View.GONE
        } else {
            r2p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[7].answer.equals("true", true)) {
            r2p3_select.visibility = View.GONE
        } else {
            r2p3_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[8].answer.equals("true", true)) {
            r2p4_select.visibility = View.GONE
        } else {
            r2p4_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[9].answer.equals("true", true)) {
            r3p1_select.visibility = View.GONE
        } else {
            r3p1_select.visibility = View.VISIBLE
        }
        //Shrawan
        if (allCpuPlayer[10].answer.equals("true", true)) {
            r0p1_select.visibility = View.GONE
        } else {
            r0p1_select.visibility = View.VISIBLE
        }

        // Cpu
        if (allUserPlayer[0].answer.equals("true", true)) {
            cpur1p1_select.visibility = View.GONE
        } else {
            cpur1p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[1].answer.equals("true", true)) {
            cpur1p2_select.visibility = View.GONE
        } else {
            cpur1p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[2].answer.equals("true", true)) {
            cpur1p3_select.visibility = View.GONE
        } else {
            cpur1p3_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[3].answer.equals("true", true)) {
            cpur1p4_select.visibility = View.GONE
        } else {
            cpur1p4_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[4].answer.equals("true", true)) {
            cpur1p5_select.visibility = View.GONE
        } else {
            cpur1p5_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[5].answer.equals("true", true)) {
            cpur2p1_select.visibility = View.GONE
        } else {
            cpur2p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[6].answer.equals("true", true)) {
            cpur2p2_select.visibility = View.GONE
        } else {
            cpur2p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[7].answer.equals("true", true)) {
            cpur2p3_select.visibility = View.GONE
        } else {
            cpur2p3_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[8].answer.equals("true", true)) {
            cpur2p4_select.visibility = View.GONE
        } else {
            cpur2p4_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[9].answer.equals("true", true)) {
            cpur3p1_select.visibility = View.GONE
        } else {
            cpur3p1_select.visibility = View.VISIBLE
        }
        //Shrawan
        if (allUserPlayer[10].answer.equals("true", true)) {
            cpur0p1_select.visibility = View.GONE
        } else {
            cpur0p1_select.visibility = View.VISIBLE
        }
    }
    // This function call for 5-3-2
    private fun playerUserCpu5_3_2() {
        // User
        if (allCpuPlayer[0].answer.equals("true", true)) {
            r1p1_select.visibility = View.GONE
        } else {
            r1p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[1].answer.equals("true", true)) {
            r1p2_select.visibility = View.GONE
        } else {
            r1p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[2].answer.equals("true", true)) {
            r1p3_select.visibility = View.GONE
        } else {
            r1p3_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[3].answer.equals("true", true)) {
            r1p4_select.visibility = View.GONE
        } else {
            r1p4_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[4].answer.equals("true", true)) {
            r1p5_select.visibility = View.GONE
        } else {
            r1p5_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[5].answer.equals("true", true)) {
            r2p1_select.visibility = View.GONE
        } else {
            r2p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[6].answer.equals("true", true)) {
            r2p2_select.visibility = View.GONE
        } else {
            r2p2_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[7].answer.equals("true", true)) {
            r2p3_select.visibility = View.GONE
        } else {
            r2p3_select.visibility = View.VISIBLE
        }


        if (allCpuPlayer[8].answer.equals("true", true)) {
            r3p1_select.visibility = View.GONE
        } else {
            r3p1_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[9].answer.equals("true", true)) {
            r3p2_select.visibility = View.GONE
        } else {
            r3p2_select.visibility = View.VISIBLE
        }
        //Shrawan
        if (allCpuPlayer[10].answer.equals("true", true)) {
            r0p1_select.visibility = View.GONE
        } else {
            r0p1_select.visibility = View.VISIBLE
        }


        // Cpu
        if (allUserPlayer[0].answer.equals("true", true)) {
            cpur1p1_select.visibility = View.GONE
        } else {
            cpur1p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[1].answer.equals("true", true)) {
            cpur1p2_select.visibility = View.GONE
        } else {
            cpur1p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[2].answer.equals("true", true)) {
            cpur1p3_select.visibility = View.GONE
        } else {
            cpur1p3_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[3].answer.equals("true", true)) {
            cpur1p4_select.visibility = View.GONE
        } else {
            cpur1p4_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[4].answer.equals("true", true)) {
            cpur1p5_select.visibility = View.GONE
        } else {
            cpur1p5_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[5].answer.equals("true", true)) {
            cpur2p1_select.visibility = View.GONE
        } else {
            cpur2p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[6].answer.equals("true", true)) {
            cpur2p2_select.visibility = View.GONE
        } else {
            cpur2p2_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[7].answer.equals("true", true)) {
            cpur2p3_select.visibility = View.GONE
        } else {
            cpur2p3_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[8].answer.equals("true", true)) {
            cpur3p1_select.visibility = View.GONE
        } else {
            cpur3p1_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[9].answer.equals("true", true)) {
            cpur3p2_select.visibility = View.GONE
        } else {
            cpur3p2_select.visibility = View.VISIBLE
        }

        //Shrawan
        if (allUserPlayer[10].answer.equals("true", true)) {
            cpur0p1_select.visibility = View.GONE
        } else {
            cpur0p1_select.visibility = View.VISIBLE
        }


    }
    // This function call for 3-5-2
    private fun playerUserCpu3_5_2() {

        // User
        if (allCpuPlayer[0].answer.equals("true", true)) {
            r1p1_select.visibility = View.GONE
        } else {
            r1p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[1].answer.equals("true", true)) {
            r1p2_select.visibility = View.GONE
        } else {
            r1p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[2].answer.equals("true", true)) {
            r1p3_select.visibility = View.GONE
        } else {
            r1p3_select.visibility = View.VISIBLE
        }


        if (allCpuPlayer[3].answer.equals("true", true)) {
            r2p1_select.visibility = View.GONE
        } else {
            r2p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[4].answer.equals("true", true)) {
            r2p2_select.visibility = View.GONE
        } else {
            r2p2_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[5].answer.equals("true", true)) {
            r2p3_select.visibility = View.GONE
        } else {
            r2p3_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[6].answer.equals("true", true)) {
            r2p4_select.visibility = View.GONE
        } else {
            r2p4_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[7].answer.equals("true", true)) {
            r2p5_select.visibility = View.GONE
        } else {
            r2p5_select.visibility = View.VISIBLE
        }


        if (allCpuPlayer[8].answer.equals("true", true)) {
            r3p1_select.visibility = View.GONE
        } else {
            r3p1_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[9].answer.equals("true", true)) {
            r3p2_select.visibility = View.GONE
        } else {
            r3p2_select.visibility = View.VISIBLE
        }
        //Shrawan
        if (allCpuPlayer[10].answer.equals("true", true)) {
            r0p1_select.visibility = View.GONE
        } else {
            r0p1_select.visibility = View.VISIBLE
        }


        // Cpu
        if (allUserPlayer[0].answer.equals("true", true)) {
            cpur1p1_select.visibility = View.GONE
        } else {
            cpur1p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[1].answer.equals("true", true)) {
            cpur1p2_select.visibility = View.GONE
        } else {
            cpur1p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[2].answer.equals("true", true)) {
            cpur1p3_select.visibility = View.GONE
        } else {
            cpur1p3_select.visibility = View.VISIBLE
        }


        if (allUserPlayer[3].answer.equals("true", true)) {
            cpur2p1_select.visibility = View.GONE
        } else {
            cpur2p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[4].answer.equals("true", true)) {
            cpur2p2_select.visibility = View.GONE
        } else {
            cpur2p2_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[5].answer.equals("true", true)) {
            cpur2p3_select.visibility = View.GONE
        } else {
            cpur2p3_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[6].answer.equals("true", true)) {
            cpur2p4_select.visibility = View.GONE
        } else {
            cpur2p4_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[7].answer.equals("true", true)) {
            cpur2p5_select.visibility = View.GONE
        } else {
            cpur2p5_select.visibility = View.VISIBLE
        }


        if (allUserPlayer[8].answer.equals("true", true)) {
            cpur3p1_select.visibility = View.GONE
        } else {
            cpur3p1_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[9].answer.equals("true", true)) {
            cpur3p2_select.visibility = View.GONE
        } else {
            cpur3p2_select.visibility = View.VISIBLE
        }
        //Shrawan
        if (allUserPlayer[10].answer.equals("true", true)) {
            cpur0p1_select.visibility = View.GONE
        } else {
            cpur0p1_select.visibility = View.VISIBLE
        }


    }
    // This function call for 4-5-1
    private fun playerUserCpu4_5_1() {

        // User
        if (allCpuPlayer[0].answer.equals("true", true)) {
            r1p1_select.visibility = View.GONE
        } else {
            r1p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[1].answer.equals("true", true)) {
            r1p2_select.visibility = View.GONE
        } else {
            r1p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[2].answer.equals("true", true)) {
            r1p3_select.visibility = View.GONE
        } else {
            r1p3_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[3].answer.equals("true", true)) {
            r1p4_select.visibility = View.GONE
        } else {
            r1p4_select.visibility = View.VISIBLE
        }


        if (allCpuPlayer[4].answer.equals("true", true)) {
            r2p1_select.visibility = View.GONE
        } else {
            r2p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[5].answer.equals("true", true)) {
            r2p2_select.visibility = View.GONE
        } else {
            r2p2_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[6].answer.equals("true", true)) {
            r2p3_select.visibility = View.GONE
        } else {
            r2p3_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[7].answer.equals("true", true)) {
            r2p4_select.visibility = View.GONE
        } else {
            r2p4_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[8].answer.equals("true", true)) {
            r2p5_select.visibility = View.GONE
        } else {
            r2p5_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[9].answer.equals("true", true)) {
            r3p1_select.visibility = View.GONE
        } else {
            r3p1_select.visibility = View.VISIBLE
        }

        //Shrawan
        if (allCpuPlayer[10].answer.equals("true", true)) {
            r0p1_select.visibility = View.GONE
        } else {
            r0p1_select.visibility = View.VISIBLE
        }

        // Cpu
        if (allUserPlayer[0].answer.equals("true", true)) {
            cpur1p1_select.visibility = View.GONE
        } else {
            cpur1p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[1].answer.equals("true", true)) {
            cpur1p2_select.visibility = View.GONE
        } else {
            cpur1p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[2].answer.equals("true", true)) {
            cpur1p3_select.visibility = View.GONE
        } else {
            cpur1p3_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[3].answer.equals("true", true)) {
            cpur1p4_select.visibility = View.GONE
        } else {
            cpur1p4_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[4].answer.equals("true", true)) {
            cpur2p1_select.visibility = View.GONE
        } else {
            cpur2p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[5].answer.equals("true", true)) {
            cpur2p2_select.visibility = View.GONE
        } else {
            cpur2p2_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[6].answer.equals("true", true)) {
            cpur2p3_select.visibility = View.GONE
        } else {
            cpur2p3_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[7].answer.equals("true", true)) {
            cpur2p4_select.visibility = View.GONE
        } else {
            cpur2p4_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[8].answer.equals("true", true)) {
            cpur2p5_select.visibility = View.GONE
        } else {
            cpur2p5_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[9].answer.equals("true", true)) {
            cpur3p1_select.visibility = View.GONE
        } else {
            cpur3p1_select.visibility = View.VISIBLE
        }
        //Shrawan
        if (allUserPlayer[10].answer.equals("true", true)) {
            cpur0p1_select.visibility = View.GONE
        } else {
            cpur0p1_select.visibility = View.VISIBLE
        }
    }
    // This function call for 4-4-2
    private fun playerUserCpu4_4_2() {

        // User
        if (allCpuPlayer[0].answer.equals("true", true)) {
            r1p1_select.visibility = View.GONE
        } else {
            r1p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[1].answer.equals("true", true)) {
            r1p2_select.visibility = View.GONE
        } else {
            r1p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[2].answer.equals("true", true)) {
            r1p3_select.visibility = View.GONE
        } else {
            r1p3_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[3].answer.equals("true", true)) {
            r1p4_select.visibility = View.GONE
        } else {
            r1p4_select.visibility = View.VISIBLE
        }


        if (allCpuPlayer[4].answer.equals("true", true)) {
            r2p1_select.visibility = View.GONE
        } else {
            r2p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[5].answer.equals("true", true)) {
            r2p2_select.visibility = View.GONE
        } else {
            r2p2_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[6].answer.equals("true", true)) {
            r2p3_select.visibility = View.GONE
        } else {
            r2p3_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[7].answer.equals("true", true)) {
            r2p4_select.visibility = View.GONE
        } else {
            r2p4_select.visibility = View.VISIBLE
        }


        if (allCpuPlayer[8].answer.equals("true", true)) {
            r3p1_select.visibility = View.GONE
        } else {
            r3p1_select.visibility = View.VISIBLE
        }

        if (allCpuPlayer[9].answer.equals("true", true)) {
            r3p2_select.visibility = View.GONE
        } else {
            r3p2_select.visibility = View.VISIBLE
        }

        //Shrawan
        if (allCpuPlayer[10].answer.equals("true", true)) {
            r0p1_select.visibility = View.GONE
        } else {
            r0p1_select.visibility = View.VISIBLE
        }


        // Cpu
        if (allUserPlayer[0].answer.equals("true", true)) {
            cpur1p1_select.visibility = View.GONE
        } else {
            cpur1p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[1].answer.equals("true", true)) {
            cpur1p2_select.visibility = View.GONE
        } else {
            cpur1p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[2].answer.equals("true", true)) {
            cpur1p3_select.visibility = View.GONE
        } else {
            cpur1p3_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[3].answer.equals("true", true)) {
            cpur1p4_select.visibility = View.GONE
        } else {
            cpur1p4_select.visibility = View.VISIBLE
        }


        if (allUserPlayer[4].answer.equals("true", true)) {
            cpur2p1_select.visibility = View.GONE
        } else {
            cpur2p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[5].answer.equals("true", true)) {
            cpur2p2_select.visibility = View.GONE
        } else {
            cpur2p2_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[6].answer.equals("true", true)) {
            cpur2p3_select.visibility = View.GONE
        } else {
            cpur2p3_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[7].answer.equals("true", true)) {
            cpur2p4_select.visibility = View.GONE
        } else {
            cpur2p4_select.visibility = View.VISIBLE
        }




        if (allUserPlayer[8].answer.equals("true", true)) {
            cpur3p1_select.visibility = View.GONE
        } else {
            cpur3p1_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[9].answer.equals("true", true)) {
            cpur3p2_select.visibility = View.GONE
        } else {
            cpur3p2_select.visibility = View.VISIBLE
        }

        //Shrawan
        if (allUserPlayer[10].answer.equals("true", true)) {
            cpur0p1_select.visibility = View.GONE
        } else {
            cpur0p1_select.visibility = View.VISIBLE
        }


    }
    // This function call for 4-3-3
    private fun playerUserCpu4_3_3() {
        // User
        if (allCpuPlayer[0].answer.equals("true", true)) {
            r1p1_select.visibility = View.GONE
        } else {
            r1p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[1].answer.equals("true", true)) {
            r1p2_select.visibility = View.GONE
        } else {
            r1p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[2].answer.equals("true", true)) {
            r1p3_select.visibility = View.GONE
        } else {
            r1p3_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[3].answer.equals("true", true)) {
            r1p4_select.visibility = View.GONE
        } else {
            r1p4_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[4].answer.equals("true", true)) {
            r2p1_select.visibility = View.GONE
        } else {
            r2p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[5].answer.equals("true", true)) {
            r2p2_select.visibility = View.GONE
        } else {
            r2p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[6].answer.equals("true", true)) {
            r2p3_select.visibility = View.GONE
        } else {
            r2p3_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[7].answer.equals("true", true)) {
            r3p1_select.visibility = View.GONE
        } else {
            r3p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[8].answer.equals("true", true)) {
            r3p2_select.visibility = View.GONE
        } else {
            r3p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[9].answer.equals("true", true)) {
            r3p3_select.visibility = View.GONE
        } else {
            r3p3_select.visibility = View.VISIBLE
        }

        //Shrawan
        if (allCpuPlayer[10].answer.equals("true", true)) {
            r0p1_select.visibility = View.GONE
        } else {
            r0p1_select.visibility = View.VISIBLE
        }


        // Cpu
        if (allUserPlayer[0].answer.equals("true", true)) {
            cpur1p1_select.visibility = View.GONE
        } else {
            cpur1p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[1].answer.equals("true", true)) {
            cpur1p2_select.visibility = View.GONE
        } else {
            cpur1p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[2].answer.equals("true", true)) {
            cpur1p3_select.visibility = View.GONE
        } else {
            cpur1p3_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[3].answer.equals("true", true)) {
            cpur1p4_select.visibility = View.GONE
        } else {
            cpur1p4_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[4].answer.equals("true", true)) {
            cpur2p1_select.visibility = View.GONE
        } else {
            cpur2p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[5].answer.equals("true", true)) {
            cpur2p2_select.visibility = View.GONE
        } else {
            cpur2p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[6].answer.equals("true", true)) {
            cpur2p3_select.visibility = View.GONE
        } else {
            cpur2p3_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[7].answer.equals("true", true)) {
            cpur3p1_select.visibility = View.GONE
        } else {
            cpur3p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[8].answer.equals("true", true)) {
            cpur3p2_select.visibility = View.GONE
        } else {
            cpur3p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[9].answer.equals("true", true)) {
            cpur3p3_select.visibility = View.GONE
        } else {
            cpur3p3_select.visibility = View.VISIBLE
        }

        //Shrawan
        if (allUserPlayer[10].answer.equals("true", true)) {
            cpur0p1_select.visibility = View.GONE
        } else {
            cpur0p1_select.visibility = View.VISIBLE
        }

    }
    // This function call for 3-4-3
    private fun playerUserCpu3_4_3() {
        // User
        if (allCpuPlayer[0].answer.equals("true", true)) {
            r1p1_select.visibility = View.GONE
        } else {
            r1p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[1].answer.equals("true", true)) {
            r1p2_select.visibility = View.GONE
        } else {
            r1p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[2].answer.equals("true", true)) {
            r1p3_select.visibility = View.GONE
        } else {
            r1p3_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[3].answer.equals("true", true)) {
            r2p1_select.visibility = View.GONE
        } else {
            r2p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[4].answer.equals("true", true)) {
            r2p2_select.visibility = View.GONE
        } else {
            r2p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[5].answer.equals("true", true)) {
            r2p3_select.visibility = View.GONE
        } else {
            r2p3_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[6].answer.equals("true", true)) {
            r2p4_select.visibility = View.GONE
        } else {
            r2p4_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[7].answer.equals("true", true)) {
            r3p1_select.visibility = View.GONE
        } else {
            r3p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[8].answer.equals("true", true)) {
            r3p2_select.visibility = View.GONE
        } else {
            r3p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[9].answer.equals("true", true)) {
            r3p3_select.visibility = View.GONE
        } else {
            r3p3_select.visibility = View.VISIBLE
        }
        //Shrawan
        if (allCpuPlayer[10].answer.equals("true", true)) {
            r0p1_select.visibility = View.GONE
        } else {
            r0p1_select.visibility = View.VISIBLE
        }

        // Cpu
        if (allUserPlayer[0].answer.equals("true", true)) {
            cpur1p1_select.visibility = View.GONE
        } else {
            cpur1p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[1].answer.equals("true", true)) {
            cpur1p2_select.visibility = View.GONE
        } else {
            cpur1p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[2].answer.equals("true", true)) {
            cpur1p3_select.visibility = View.GONE
        } else {
            cpur1p3_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[3].answer.equals("true", true)) {
            cpur2p1_select.visibility = View.GONE
        } else {
            cpur2p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[4].answer.equals("true", true)) {
            cpur2p2_select.visibility = View.GONE
        } else {
            cpur2p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[5].answer.equals("true", true)) {
            cpur2p3_select.visibility = View.GONE
        } else {
            cpur2p3_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[6].answer.equals("true", true)) {
            cpur2p4_select.visibility = View.GONE
        } else {
            cpur2p4_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[7].answer.equals("true", true)) {
            cpur3p1_select.visibility = View.GONE
        } else {
            cpur3p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[8].answer.equals("true", true)) {
            cpur3p2_select.visibility = View.GONE
        } else {
            cpur3p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[9].answer.equals("true", true)) {
            cpur3p3_select.visibility = View.GONE
        } else {
            cpur3p3_select.visibility = View.VISIBLE
        }
        //Shrawan
        if (allUserPlayer[10].answer.equals("true", true)) {
            cpur0p1_select.visibility = View.GONE
        } else {
            cpur0p1_select.visibility = View.VISIBLE
        }
    }
    // This function call for 4-2-4
    private fun playerUserCpu4_2_4() {
        // User
        if (allCpuPlayer[0].answer.equals("true", true)) {
            r1p1_select.visibility = View.GONE
        } else {
            r1p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[1].answer.equals("true", true)) {
            r1p2_select.visibility = View.GONE
        } else {
            r1p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[2].answer.equals("true", true)) {
            r1p3_select.visibility = View.GONE
        } else {
            r1p3_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[3].answer.equals("true", true)) {
            r1p4_select.visibility = View.GONE
        } else {
            r1p4_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[4].answer.equals("true", true)) {
            r2p1_select.visibility = View.GONE
        } else {
            r2p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[5].answer.equals("true", true)) {
            r2p2_select.visibility = View.GONE
        } else {
            r2p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[6].answer.equals("true", true)) {
            r3p1_select.visibility = View.GONE
        } else {
            r3p1_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[7].answer.equals("true", true)) {
            r3p2_select.visibility = View.GONE
        } else {
            r3p2_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[8].answer.equals("true", true)) {
            r3p3_select.visibility = View.GONE
        } else {
            r3p3_select.visibility = View.VISIBLE
        }
        if (allCpuPlayer[9].answer.equals("true", true)) {
            r3p4_select.visibility = View.GONE
        } else {
            r3p4_select.visibility = View.VISIBLE
        }
        //Shrawan
        if (allCpuPlayer[10].answer.equals("true", true)) {
            r0p1_select.visibility = View.GONE
        } else {
            r0p1_select.visibility = View.VISIBLE
        }
        // Cpu
        if (allUserPlayer[0].answer.equals("true", true)) {
            cpur1p1_select.visibility = View.GONE
        } else {
            cpur1p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[1].answer.equals("true", true)) {
            cpur1p2_select.visibility = View.GONE
        } else {
            cpur1p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[2].answer.equals("true", true)) {
            cpur1p3_select.visibility = View.GONE
        } else {
            cpur1p3_select.visibility = View.VISIBLE
        }

        if (allUserPlayer[3].answer.equals("true", true)) {
            cpur1p4_select.visibility = View.GONE
        } else {
            cpur1p4_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[4].answer.equals("true", true)) {
            cpur2p1_select.visibility = View.GONE
        } else {
            cpur2p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[5].answer.equals("true", true)) {
            cpur2p2_select.visibility = View.GONE
        } else {
            cpur2p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[6].answer.equals("true", true)) {
            cpur3p1_select.visibility = View.GONE
        } else {
            cpur3p1_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[7].answer.equals("true", true)) {
            cpur3p2_select.visibility = View.GONE
        } else {
            cpur3p2_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[8].answer.equals("true", true)) {
            cpur3p3_select.visibility = View.GONE
        } else {
            cpur3p3_select.visibility = View.VISIBLE
        }
        if (allUserPlayer[9].answer.equals("true", true)) {
            cpur3p4_select.visibility = View.GONE
        } else {
            cpur3p4_select.visibility = View.VISIBLE
        }
        //Shrawan
        if (allUserPlayer[10].answer.equals("true", true)) {
            cpur0p1_select.visibility = View.GONE
        } else {
            cpur0p1_select.visibility = View.VISIBLE
        }
    }

    //This function is used for set player details on t-shirt (Name , number , id) of user
    private fun setUserScreenPlayerNameDtl() {
        if (!isTimerFinish) {
            try {
                var setName = 0

                if (r1p1.visibility == View.VISIBLE) {
                    r1p1_name.text = allCpuPlayer[setName].name
                    r1p1_number.text = allCpuPlayer[setName].jersey_number
                    r1p1_id.text = allCpuPlayer[setName].id
                    setName += 1
                }
                if (r1p2.visibility == View.VISIBLE) {
                    r1p2_name.text = allCpuPlayer[setName].name
                    r1p2_number.text = allCpuPlayer[setName].jersey_number
                    r1p2_id.text = allCpuPlayer[setName].id
                    setName += 1
                }
                if (r1p3.visibility == View.VISIBLE) {
                    r1p3_name.text = allCpuPlayer[setName].name
                    r1p3_number.text = allCpuPlayer[setName].jersey_number
                    r1p3_id.text = allCpuPlayer[setName].id
                    setName += 1
                }
                if (r1p4.visibility == View.VISIBLE) {
                    r1p4_name.text = allCpuPlayer[setName].name
                    r1p4_number.text = allCpuPlayer[setName].jersey_number
                    r1p4_id.text = allCpuPlayer[setName].id
                    setName += 1
                }
                if (r1p5.visibility == View.VISIBLE) {
                    r1p5_name.text = allCpuPlayer[setName].name
                    r1p5_number.text = allCpuPlayer[setName].jersey_number
                    r1p5_id.text = allCpuPlayer[setName].id
                    setName += 1
                }
                if (r2p1.visibility == View.VISIBLE) {
                    r2p1_name.text = allCpuPlayer[setName].name
                    r2p1_number.text = allCpuPlayer[setName].jersey_number
                    r2p1_id.text = allCpuPlayer[setName].id
                    setName += 1
                }
                if (r2p2.visibility == View.VISIBLE) {
                    r2p2_name.text = allCpuPlayer[setName].name
                    r2p2_number.text = allCpuPlayer[setName].jersey_number
                    r2p2_id.text = allCpuPlayer[setName].id
                    setName += 1
                }
                if (r2p3.visibility == View.VISIBLE) {
                    r2p3_name.text = allCpuPlayer[setName].name
                    r2p3_number.text = allCpuPlayer[setName].jersey_number
                    r2p3_id.text = allCpuPlayer[setName].id
                    setName += 1
                }
                if (r2p4.visibility == View.VISIBLE) {
                    r2p4_name.text = allCpuPlayer[setName].name
                    r2p4_number.text = allCpuPlayer[setName].jersey_number
                    r2p4_id.text = allCpuPlayer[setName].id
                    setName += 1
                }
                if (r2p5.visibility == View.VISIBLE) {
                    r2p5_name.text = allCpuPlayer[setName].name
                    r2p5_number.text = allCpuPlayer[setName].jersey_number
                    r2p5_id.text = allCpuPlayer[setName].id
                    setName += 1
                }
                if (r3p1.visibility == View.VISIBLE) {
                    if (setName <= 9) {
                        r3p1_name.text = allCpuPlayer[setName].name
                        r3p1_number.text = allCpuPlayer[setName].jersey_number
                        r3p1_id.text = allCpuPlayer[setName].id
                    }
                    setName += 1
                }
                if (r3p2.visibility == View.VISIBLE) {
                    if (setName <= 9) {
                        r3p2_name.text = allCpuPlayer[setName].name
                        r3p2_number.text = allCpuPlayer[setName].jersey_number
                        r3p2_id.text = allCpuPlayer[setName].id
                    }
                    setName += 1
                }
                if (r3p3.visibility == View.VISIBLE) {
                    if (setName <= 9) {
                        r3p3_name.text = allCpuPlayer[setName].name
                        r3p3_number.text = allCpuPlayer[setName].jersey_number
                        r3p3_id.text = allCpuPlayer[setName].id
                    }
                    setName += 1
                }
                if (r3p4.visibility == View.VISIBLE) {
                    if (setName <= 9) {
                        r3p4_name.text = allCpuPlayer[setName].name
                        r3p4_number.text = allCpuPlayer[setName].jersey_number
                        r3p4_id.text = allCpuPlayer[setName].id
                    }
                    setName += 1
                }
                if (r3p5.visibility == View.VISIBLE) {
                    if (setName <= 9) {
                        r3p5_name.text = allCpuPlayer[setName].name
                        r3p5_number.text = allCpuPlayer[setName].jersey_number
                        r3p5_id.text = allCpuPlayer[setName].id
                    }
                    setName += 1
                }

                //Shrawan
                if (r0p1.visibility == View.VISIBLE) {
                    if (setName <= 10) {
                        r0p1_name.text = allCpuPlayer[setName].name
                        r0p1_number.text = allCpuPlayer[setName].jersey_number
                        r0p1_id.text = allCpuPlayer[setName].id
                    }
                    setName += 1
                }
            }catch (e:Exception){
                Log.d("Player Deatil :- ","**** error :- "+e.message)
            }
            setCpuScreenPlayerNameDtl()
        }
    }
    //This function is used for set player details on t-shirt (Name , number , id) of cpu
    private fun setCpuScreenPlayerNameDtl() {
        if (!isTimerFinish) {
            try {
                var setName = 0
                if (cpur1p1.visibility == View.VISIBLE) {
                    cpur1p1_name.text = allUserPlayer[setName].name
                    cpur1p1_number.text = allUserPlayer[setName].jersey_number
                    cpur1p1_id.text = allUserPlayer[setName].id
                    setName += 1
                }
                if (cpur1p2.visibility == View.VISIBLE) {
                    cpur1p2_name.text = allUserPlayer[setName].name
                    cpur1p2_number.text = allUserPlayer[setName].jersey_number
                    cpur1p2_id.text = allUserPlayer[setName].id
                    setName += 1
                }
                if (cpur1p3.visibility == View.VISIBLE) {
                    cpur1p3_name.text = allUserPlayer[setName].name
                    cpur1p3_number.text = allUserPlayer[setName].jersey_number
                    cpur1p3_id.text = allUserPlayer[setName].id
                    setName += 1
                }
                if (cpur1p4.visibility == View.VISIBLE) {
                    cpur1p4_name.text = allUserPlayer[setName].name
                    cpur1p4_number.text = allUserPlayer[setName].jersey_number
                    cpur1p4_id.text = allUserPlayer[setName].id
                    setName += 1
                }
                if (cpur1p5.visibility == View.VISIBLE) {
                    cpur1p5_name.text = allUserPlayer[setName].name
                    cpur1p5_number.text = allUserPlayer[setName].jersey_number
                    cpur1p5_id.text = allUserPlayer[setName].id
                    setName += 1
                }
                if (cpur2p1.visibility == View.VISIBLE) {
                    cpur2p1_name.text = allUserPlayer[setName].name
                    cpur2p1_number.text = allUserPlayer[setName].jersey_number
                    cpur2p1_id.text = allUserPlayer[setName].id
                    setName += 1
                }
                if (cpur2p2.visibility == View.VISIBLE) {
                    cpur2p2_name.text = allUserPlayer[setName].name
                    cpur2p2_number.text = allUserPlayer[setName].jersey_number
                    cpur2p2_id.text = allUserPlayer[setName].id
                    setName += 1
                }
                if (cpur2p3.visibility == View.VISIBLE) {
                    cpur2p3_name.text = allUserPlayer[setName].name
                    cpur2p3_number.text = allUserPlayer[setName].jersey_number
                    cpur2p3_id.text = allUserPlayer[setName].id
                    setName += 1
                }
                if (cpur2p4.visibility == View.VISIBLE) {
                    cpur2p4_name.text = allUserPlayer[setName].name
                    cpur2p4_number.text = allUserPlayer[setName].jersey_number
                    cpur2p4_id.text = allUserPlayer[setName].id
                    setName += 1
                }
                if (cpur2p5.visibility == View.VISIBLE) {
                    cpur2p5_name.text = allUserPlayer[setName].name
                    cpur2p5_number.text = allUserPlayer[setName].jersey_number
                    cpur2p5_id.text = allUserPlayer[setName].id
                    setName += 1
                }
                if (cpur3p1.visibility == View.VISIBLE) {
                    if (setName <= 9) {
                        cpur3p1_name.text = allUserPlayer[setName].name
                        cpur3p1_number.text = allUserPlayer[setName].jersey_number
                        cpur3p1_id.text = allUserPlayer[setName].id
                    }
                    setName += 1
                }
                if (cpur3p2.visibility == View.VISIBLE) {
                    if (setName <= 9) {
                        cpur3p2_name.text = allUserPlayer[setName].name
                        cpur3p2_number.text = allUserPlayer[setName].jersey_number
                        cpur3p2_id.text = allUserPlayer[setName].id
                    }
                    setName += 1
                }
                if (cpur3p3.visibility == View.VISIBLE) {
                    if (setName <= 9) {
                        cpur3p3_name.text = allUserPlayer[setName].name
                        cpur3p3_number.text = allUserPlayer[setName].jersey_number
                        cpur3p3_id.text = allUserPlayer[setName].id
                    }
                    setName += 1
                }
                if (cpur3p4.visibility == View.VISIBLE) {
                    if (setName <= 9) {
                        cpur3p4_name.text = allUserPlayer[setName].name
                        cpur3p4_number.text = allUserPlayer[setName].jersey_number
                        cpur3p4_id.text = allUserPlayer[setName].id
                    }
                    setName += 1
                }
                if (cpur3p5.visibility == View.VISIBLE) {
                    if (setName <= 9) {
                        cpur3p5_name.text = allUserPlayer[setName].name
                        cpur3p5_number.text = allUserPlayer[setName].jersey_number
                        cpur3p5_id.text = allUserPlayer[setName].id
                    }
                    setName += 1
                }

                //Shrawan
                if (cpur0p1.visibility == View.VISIBLE) {
                    if (setName <= 10) {
                        cpur0p1_name.text = allUserPlayer[setName].name
                        cpur0p1_number.text = allUserPlayer[setName].jersey_number
                        cpur0p1_id.text = allUserPlayer[setName].id
                    }
                    setName += 1
                }
            }catch (e:Exception){
                Log.d("Cpu Name Detail :- ","****** error :- "+e.message)
            }
            showRightAnswerName()
        }
    }
    //This function is used for show name which guessed by user or cpu
    private fun showRightAnswerName() {
        try {
            if (!isTimerFinish) {
                if (allCpuPlayer[r1p1_id.text.toString().toInt() - 1].answer == "true" && r1p1.visibility == View.VISIBLE) {
                    totalPlayerNameShow += 1
                    r1p1_txt.visibility = View.VISIBLE
                    val countryID = allCpuPlayer[r1p1_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r1p1_img.setImageResource(setGames.getTShirtImage(countryID))
                    r1p1_name.setTextColor(textColor)
                    r1p1_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r1p2_id.text.toString().toInt() - 1].answer == "true" && r1p2.visibility == View.VISIBLE) {
                    totalPlayerNameShow += 1
                    r1p2_txt.visibility = View.VISIBLE
                    val countryID = allCpuPlayer[r1p2_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r1p2_img.setImageResource(setGames.getTShirtImage(countryID))
                    r1p2_name.setTextColor(textColor)
                    r1p2_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r1p3_id.text.toString().toInt() - 1].answer == "true" && r1p3.visibility == View.VISIBLE) {
                    totalPlayerNameShow += 1
                    r1p3_txt.visibility = View.VISIBLE
                    val countryID = allCpuPlayer[r1p3_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r1p3_img.setImageResource(setGames.getTShirtImage(countryID))
                    r1p3_name.setTextColor(textColor)
                    r1p3_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r1p4_id.text.toString().toInt() - 1].answer == "true" && r1p4.visibility == View.VISIBLE) {
                    r1p4_txt.visibility = View.VISIBLE
                    totalPlayerNameShow += 1
                    val countryID = allCpuPlayer[r1p4_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r1p4_img.setImageResource(setGames.getTShirtImage(countryID))
                    r1p4_name.setTextColor(textColor)
                    r1p4_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r1p5_id.text.toString().toInt() - 1].answer == "true" && r1p5.visibility == View.VISIBLE) {
                    r1p5_txt.visibility = View.VISIBLE
                    totalPlayerNameShow += 1
                    val countryID = allCpuPlayer[r1p5_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r1p5_img.setImageResource(setGames.getTShirtImage(countryID))
                    r1p5_name.setTextColor(textColor)
                    r1p5_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r2p1_id.text.toString().toInt() - 1].answer == "true" && r2p1.visibility == View.VISIBLE) {
                    r2p1_txt.visibility = View.VISIBLE
                    totalPlayerNameShow += 1
                    val countryID = allCpuPlayer[r2p1_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r2p1_img.setImageResource(setGames.getTShirtImage(countryID))
                    r2p1_name.setTextColor(textColor)
                    r2p1_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r2p2_id.text.toString().toInt() - 1].answer == "true" && r2p2.visibility == View.VISIBLE) {
                    totalPlayerNameShow += 1
                    r2p2_txt.visibility = View.VISIBLE
                    val countryID = allCpuPlayer[r2p2_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r2p2_img.setImageResource(setGames.getTShirtImage(countryID))
                    r2p2_name.setTextColor(textColor)
                    r2p2_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r2p3_id.text.toString().toInt() - 1].answer == "true" && r2p3.visibility == View.VISIBLE) {
                    totalPlayerNameShow += 1
                    r2p3_txt.visibility = View.VISIBLE
                    val countryID = allCpuPlayer[r2p3_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r2p3_img.setImageResource(setGames.getTShirtImage(countryID))
                    r2p3_name.setTextColor(textColor)
                    r2p3_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r2p4_id.text.toString().toInt() - 1].answer == "true" && r2p4.visibility == View.VISIBLE) {
                    totalPlayerNameShow += 1
                    r2p4_txt.visibility = View.VISIBLE
                    val countryID = allCpuPlayer[r2p4_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r2p4_img.setImageResource(setGames.getTShirtImage(countryID))
                    r2p4_name.setTextColor(textColor)
                    r2p4_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r2p5_id.text.toString().toInt() - 1].answer == "true" && r2p5.visibility == View.VISIBLE) {
                    totalPlayerNameShow += 1
                    r2p5_txt.visibility = View.VISIBLE
                    val countryID = allCpuPlayer[r2p5_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r2p5_img.setImageResource(setGames.getTShirtImage(countryID))
                    r2p5_name.setTextColor(textColor)
                    r2p5_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r3p1_id.text.toString().toInt() - 1].answer == "true" && r3p1.visibility == View.VISIBLE) {
                    totalPlayerNameShow += 1
                    r3p1_txt.visibility = View.VISIBLE
                    val countryID = allCpuPlayer[r3p1_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r3p1_img.setImageResource(setGames.getTShirtImage(countryID))
                    r3p1_name.setTextColor(textColor)
                    r3p1_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r3p2_id.text.toString().toInt() - 1].answer == "true" && r3p2.visibility == View.VISIBLE) {
                    r3p2_txt.visibility = View.VISIBLE
                    totalPlayerNameShow += 1
                    val countryID = allCpuPlayer[r3p2_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r3p2_img.setImageResource(setGames.getTShirtImage(countryID))
                    r3p2_name.setTextColor(textColor)
                    r3p2_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r3p3_id.text.toString().toInt() - 1].answer == "true" && r3p3.visibility == View.VISIBLE) {
                    totalPlayerNameShow += 1
                    r3p3_txt.visibility = View.VISIBLE
                    val countryID = allCpuPlayer[r3p3_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r3p3_img.setImageResource(setGames.getTShirtImage(countryID))
                    r3p3_name.setTextColor(textColor)
                    r3p3_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r3p4_id.text.toString().toInt() - 1].answer == "true" && r3p4.visibility == View.VISIBLE) {
                    totalPlayerNameShow += 1
                    r3p4_txt.visibility = View.VISIBLE
                    val countryID = allCpuPlayer[r3p4_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r3p4_img.setImageResource(setGames.getTShirtImage(countryID))
                    r3p4_name.setTextColor(textColor)
                    r3p4_number.setTextColor(textColor)
                }
                if (allCpuPlayer[r3p5_id.text.toString().toInt() - 1].answer == "true" && r3p5.visibility == View.VISIBLE) {
                    totalPlayerNameShow += 1
                    r3p5_txt.visibility = View.VISIBLE
                    val countryID = allCpuPlayer[r3p5_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r3p5_img.setImageResource(setGames.getTShirtImage(countryID))
                    r3p5_name.setTextColor(textColor)
                    r3p5_number.setTextColor(textColor)
                }

                //Shrawan
                if (allCpuPlayer[r0p1_id.text.toString().toInt() - 1].answer == "true" && r0p1.visibility == View.VISIBLE) {
                    totalPlayerNameShow += 1
                    r0p1_txt.visibility = View.VISIBLE
                    val countryID = allCpuPlayer[r0p1_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    r0p1_img.setImageResource(setGames.getTShirtImage(countryID))
                    r0p1_name.setTextColor(textColor)
                    r0p1_number.setTextColor(textColor)
                }

                // cpu
                if (allUserPlayer[cpur1p1_id.text.toString().toInt() - 1].answer == "true" && cpur1p1.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur1p1_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur1p1_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur1p1_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur1p1_name.setTextColor(textColor)
                    cpur1p1_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur1p2_id.text.toString().toInt() - 1].answer == "true" && cpur1p2.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur1p2_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur1p2_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur1p2_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur1p2_name.setTextColor(textColor)
                    cpur1p2_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur1p3_id.text.toString().toInt() - 1].answer == "true" && cpur1p3.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur1p3_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur1p3_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur1p3_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur1p3_name.setTextColor(textColor)
                    cpur1p3_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur1p4_id.text.toString().toInt() - 1].answer == "true" && cpur1p4.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur1p4_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur1p4_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur1p4_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur1p4_name.setTextColor(textColor)
                    cpur1p4_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur1p5_id.text.toString().toInt() - 1].answer == "true" && cpur1p5.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur1p5_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur1p5_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur1p5_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur1p5_name.setTextColor(textColor)
                    cpur1p5_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur2p1_id.text.toString().toInt() - 1].answer == "true" && cpur2p1.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur2p1_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur2p1_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur2p1_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur2p1_name.setTextColor(textColor)
                    cpur2p1_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur2p2_id.text.toString().toInt() - 1].answer == "true" && cpur2p2.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur2p2_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur2p2_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur2p2_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur2p2_name.setTextColor(textColor)
                    cpur2p2_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur2p3_id.text.toString().toInt() - 1].answer == "true" && cpur2p3.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur2p3_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur2p3_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur2p3_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur2p3_name.setTextColor(textColor)
                    cpur2p3_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur2p4_id.text.toString().toInt() - 1].answer == "true" && cpur2p4.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur2p4_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur2p4_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur2p4_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur2p4_name.setTextColor(textColor)
                    cpur2p4_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur2p5_id.text.toString().toInt() - 1].answer == "true" && cpur2p5.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur2p5_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur2p5_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur2p5_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur2p5_name.setTextColor(textColor)
                    cpur2p5_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur3p1_id.text.toString().toInt() - 1].answer == "true" && cpur3p1.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur3p1_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur3p1_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur3p1_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur3p1_name.setTextColor(textColor)
                    cpur3p1_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur3p2_id.text.toString().toInt() - 1].answer == "true" && cpur3p2.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur3p2_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur3p2_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur3p2_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur3p2_name.setTextColor(textColor)
                    cpur3p2_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur3p3_id.text.toString().toInt() - 1].answer == "true" && cpur3p3.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur3p3_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur3p3_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur3p3_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur3p3_name.setTextColor(textColor)
                    cpur3p3_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur3p4_id.text.toString().toInt() - 1].answer == "true" && cpur3p4.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur3p4_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur3p4_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur3p4_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur3p4_name.setTextColor(textColor)
                    cpur3p4_number.setTextColor(textColor)
                }
                if (allUserPlayer[cpur3p5_id.text.toString().toInt() - 1].answer == "true" && cpur3p5.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur3p5_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur3p5_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur3p5_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur3p5_name.setTextColor(textColor)
                    cpur3p5_number.setTextColor(textColor)
                }

                //Shrawan
                if (allUserPlayer[cpur0p1_id.text.toString().toInt() - 1].answer == "true" && cpur0p1.visibility == View.VISIBLE) {
                    cpuPlayerNameShow += 1
                    cpur0p1_txt.visibility = View.VISIBLE
                    val countryID = allUserPlayer[cpur0p1_id.text.toString().toInt() - 1].country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    cpur0p1_img.setImageResource(setGames.getTShirtImage(countryID))
                    cpur0p1_name.setTextColor(textColor)
                    cpur0p1_number.setTextColor(textColor)
                }

                playerSelect()
            }
        }catch (e:Exception){
            Log.d("Error ","********"+e.message)
        }

    }
    //This function is used for select player which term (User or cpu)
    private fun playerSelect() {
        Log.d("@@@Error ","cpuPlayerNameShow"+cpuPlayerNameShow)
        Log.d("@@@Error ","totalPlayerNameShow"+totalPlayerNameShow)
        if (!isTimerFinish) {
            if (userType.equals("USER",true))
            {

                user_screen.visibility = View.VISIBLE
                cpu_screen.visibility = View.GONE
                user_name.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                oppose_team_player_name.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))

                r1p1_img.setOnClickListener(this)
                r1p2_img.setOnClickListener(this)
                r1p3_img.setOnClickListener(this)
                r1p4_img.setOnClickListener(this)
                r1p5_img.setOnClickListener(this)

                r2p1_img.setOnClickListener(this)
                r2p2_img.setOnClickListener(this)
                r2p3_img.setOnClickListener(this)
                r2p4_img.setOnClickListener(this)
                r2p5_img.setOnClickListener(this)

                r3p1_img.setOnClickListener(this)
                r3p2_img.setOnClickListener(this)
                r3p3_img.setOnClickListener(this)
                r3p4_img.setOnClickListener(this)
                r3p5_img.setOnClickListener(this)

                r0p1_img.setOnClickListener(this)//Shrawan

                bt_shoot.setOnClickListener(this)
                bt_pass.setOnClickListener(this)


                if (totalPlayerNameShow != 0) {
//                    selectPlayPlayer(sessionManager.getMySelectedTeamPlayerNum())
                    root_button.visibility = View.VISIBLE
                    /* if (totalPlayerNameShow == 1) {
                         bt_shoot.visibility = View.GONE
                     }*/
                    //Shrawan
                    if (totalPlayerNameShow == 1 || isGoalClick) {
                        bt_shoot.visibility = View.GONE
                        if (isGoalClick){
                            isGoalClick = false
                        }
                    }
                }

            }
            else {
                cpu_screen.visibility = View.VISIBLE
                user_screen.visibility = View.GONE
                oppose_team_player_name.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                user_name.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))


                if (cpuPlayerNameShow != 0) {
                    // Generate a random number between 0 and 100 (inclusive)
                    if (cpuPlayerNameShow==1){
                        selectCpuPlayPlayer(sessionManager.getSelectedTeamPlayerNum())
                    }else{
                        val randomNumber = Random.nextInt(2, 10)
                        // Check if the number is even or odd
                        val result = if (randomNumber % 2 == 0) {
                            "even"
                        } else {
                            "odd"
                        }
                        if (result.equals("even",true)){
                            autoButtonSelectPassShoot()
                        }else{
                            selectCpuPlayPlayer(sessionManager.getSelectedTeamPlayerNum())
                        }
                    }
                } else {
                    autoButtonClick()
                }

            }
        }
    }
    //This function is used for select player which term (User or cpu)
    private fun selectPlayPlayer(playerNum: Int) {
        if (!isTimerFinish) {
            if (r1p1.visibility == View.VISIBLE && r1p1_txt.visibility == View.VISIBLE && r1p1_id.text.toString().toInt() == playerNum) {
                selectionPower = true
                showPlayerReachPowerUser(r1p1_select, r1p1_id)
            }
            if (r1p2.visibility == View.VISIBLE && r1p2_txt.visibility == View.VISIBLE && r1p2_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r1p2_select, r1p2_id)
            }
            if (r1p3.visibility == View.VISIBLE && r1p3_txt.visibility == View.VISIBLE && r1p3_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r1p3_select, r1p3_id)
            }
            if (r1p4.visibility == View.VISIBLE && r1p4_txt.visibility == View.VISIBLE && r1p4_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r1p4_select, r1p4_id)
            }
            if (r1p5.visibility == View.VISIBLE && r1p5_txt.visibility == View.VISIBLE && r1p5_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r1p5_select, r1p5_id)
            }
            if (r2p1.visibility == View.VISIBLE && r2p1_txt.visibility == View.VISIBLE && r2p1_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r2p1_select, r2p1_id)
            }
            if (r2p2.visibility == View.VISIBLE && r2p2_txt.visibility == View.VISIBLE && r2p2_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r2p2_select, r2p2_id)
            }
            if (r2p3.visibility == View.VISIBLE && r2p3_txt.visibility == View.VISIBLE && r2p3_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r2p3_select, r2p3_id)
            }
            if (r2p4.visibility == View.VISIBLE && r2p4_txt.visibility == View.VISIBLE && r2p4_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r2p4_select, r2p4_id)
            }
            if (r2p5.visibility == View.VISIBLE && r2p5_txt.visibility == View.VISIBLE && r2p5_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r2p5_select, r2p5_id)
            }
            if (r3p1.visibility == View.VISIBLE && r3p1_txt.visibility == View.VISIBLE && r3p1_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r3p1_select, r3p1_id)
            }
            if (r3p2.visibility == View.VISIBLE && r3p2_txt.visibility == View.VISIBLE && r3p2_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r3p2_select, r3p2_id)
            }
            if (r3p3.visibility == View.VISIBLE && r3p3_txt.visibility == View.VISIBLE && r3p3_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r3p3_select, r3p3_id)
            }
            if (r3p4.visibility == View.VISIBLE && r3p4_txt.visibility == View.VISIBLE && r3p4_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r3p4_select, r3p4_id)
            }
            if (r3p5.visibility == View.VISIBLE && r3p5_txt.visibility == View.VISIBLE && r3p5_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(r3p5_select, r3p5_id)
            }
            //Shrawan
            if (r0p1.visibility == View.VISIBLE &&
                r0p1_txt.visibility == View.VISIBLE && r0p1_id.text.toString().toInt() == playerNum) {
                selectionPower = true
                Log.d("@@@@@@@@@@@","${r0p1_id.text.toString().toInt()}  $playerNum")
                showPlayerReachPowerUser(r0p1_select, r0p1_id)
            }

        }
    }
    //This function is used for when cpu term than start auto playing
    private fun selectCpuPlayPlayer(playerNum: Int) {
        if (!isTimerFinish) {

            if (cpur1p1.visibility == View.VISIBLE && cpur1p1_txt.visibility == View.VISIBLE && cpur1p1_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur1p1_select, cpur1p1_id)
            }
            if (cpur1p2.visibility == View.VISIBLE && cpur1p2_txt.visibility == View.VISIBLE && cpur1p2_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur1p2_select, cpur1p2_id)
            }
            if (cpur1p3.visibility == View.VISIBLE && cpur1p3_txt.visibility == View.VISIBLE && cpur1p3_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur1p3_select, cpur1p3_id)
            }
            if (cpur1p4.visibility == View.VISIBLE && cpur1p4_txt.visibility == View.VISIBLE && cpur1p4_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur1p4_select, cpur1p4_id)
            }
            if (cpur1p5.visibility == View.VISIBLE && cpur1p5_txt.visibility == View.VISIBLE && cpur1p5_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur1p5_select, cpur1p5_id)
            }
            if (cpur2p1.visibility == View.VISIBLE && cpur2p1_txt.visibility == View.VISIBLE && cpur2p1_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur2p1_select, cpur2p1_id)
            }
            if (cpur2p2.visibility == View.VISIBLE && cpur2p2_txt.visibility == View.VISIBLE && cpur2p2_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur2p2_select, cpur2p2_id)
            }
            if (cpur2p3.visibility == View.VISIBLE && cpur2p3_txt.visibility == View.VISIBLE && cpur2p3_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur2p3_select, cpur2p3_id)
            }
            if (cpur2p4.visibility == View.VISIBLE && cpur2p4_txt.visibility == View.VISIBLE && cpur2p4_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur2p4_select, cpur2p4_id)
            }
            if (cpur2p5.visibility == View.VISIBLE && cpur2p5_txt.visibility == View.VISIBLE && cpur2p5_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur2p5_select, cpur2p5_id)
            }
            if (cpur3p1.visibility == View.VISIBLE && cpur3p1_txt.visibility == View.VISIBLE && cpur3p1_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur3p1_select, cpur3p1_id)
            }
            if (cpur3p2.visibility == View.VISIBLE && cpur3p2_txt.visibility == View.VISIBLE && cpur3p2_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur3p2_select, cpur3p2_id)
            }
            if (cpur3p3.visibility == View.VISIBLE && cpur3p3_txt.visibility == View.VISIBLE && cpur3p3_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur3p3_select, cpur3p3_id)
            }
            if (cpur3p4.visibility == View.VISIBLE && cpur3p4_txt.visibility == View.VISIBLE && cpur3p4_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur3p4_select, cpur3p4_id)
            }
            if (cpur3p5.visibility == View.VISIBLE && cpur3p5_txt.visibility == View.VISIBLE && cpur3p5_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur3p5_select, cpur3p5_id)
            }

            //Shrawan
            if (cpur0p1.visibility == View.VISIBLE && cpur0p1_txt.visibility == View.VISIBLE && cpur0p1_id.text.toString()
                    .toInt() == playerNum
            ) {
                selectionPower = true
                showPlayerReachPowerUser(cpur0p1_select, cpur0p1_id)
            }
            autoButtonClick()
        }
    }

    //This function is used for display alert box (halftime, time over etc.)
    private fun playAlertBox(drawableImg: Int, action: String) {
        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView = dialog.findViewById(R.id.img_change)

        Glide.with(requireContext())
            .load(drawableImg)
            .into(imgChange)

        if (sessionManager.getMatchType().equals("worldcup",true)){
            Log.e("@@@Error","My Score "+sessionManager.getMyScore().toString())
            Log.e("@@@Error","Cpu Score "+sessionManager.getCpuScore().toString())
            Log.e("type ", "worldcup")
            if (action.equals("timeOver",true)){
                Log.e("@@@Error","Cpu Score timeOver"+sessionManager.getCpuScore().toString())
                Handler(Looper.myLooper()!!).postDelayed({
                    dialog.dismiss()
                    sessionManager.setExtraTimeUser("timeOver")
                    setCondition("timeOver")
                }, 3000)
            }else{
                if (action.equals("FullTime",true)){
                    Handler(Looper.myLooper()!!).postDelayed({
                        dialog.dismiss()
                        sessionManager.setExtraTimeUser("ExtraTime")
                        playAlertBox(R.drawable.extra_time_img, "ExtraTime")
                    }, 3000)
                }
                if (action.equals("ExtraTime",true)){
                    Handler(Looper.myLooper()!!).postDelayed({
                        totalTime = 900000L
                        startTime = 0
                        val screen = setGames.setScreen(sessionManager.getUserScreenType())
                        val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
                        if (sessionManager.isNetworkAvailable()) {
                            cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
                            myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
                            cpuDbHelper.deleteAllPlayers()
                            myPlayerDbHelper.deleteAllPlayers()
                            extraPLayerDbHelper.deleteAllPlayers()
                            getGuessTeamList(dialog, screen.r1.toString(), screen.r2.toString(), screen.r3.toString(), cpuScreen.r1.toString(), cpuScreen.r2.toString(), cpuScreen.r3.toString())
                        } else {
                            alertError(getString(R.string.no_internet))
                        }
                    }, 3000)
                }
                if (action.equals("halftime",true)){
                    Handler(Looper.myLooper()!!).postDelayed({
                        val screen = setGames.setScreen(sessionManager.getUserScreenType())
                        val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
                        if (sessionManager.isNetworkAvailable()) {
                            cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
                            myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
                            cpuDbHelper.deleteAllPlayers()
                            myPlayerDbHelper.deleteAllPlayers()
                            extraPLayerDbHelper.deleteAllPlayers()
                            dialog.dismiss()
                            getGuessTeamList(dialog, screen.r1.toString(), screen.r2.toString(), screen.r3.toString(), cpuScreen.r1.toString(), cpuScreen.r2.toString(), cpuScreen.r3.toString())
                        } else {
                            alertError(getString(R.string.no_internet))
                        }
                    }, 3000)
                }
                if (action.equals("TimeHalf",true)){
                    Handler(Looper.myLooper()!!).postDelayed({
                        val screen = setGames.setScreen(sessionManager.getUserScreenType())
                        val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
                        if (sessionManager.isNetworkAvailable()) {
                            cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
                            myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
                            cpuDbHelper.deleteAllPlayers()
                            myPlayerDbHelper.deleteAllPlayers()
                            extraPLayerDbHelper.deleteAllPlayers()
                            getGuessTeamList(dialog, screen.r1.toString(), screen.r2.toString(), screen.r3.toString(), cpuScreen.r1.toString(), cpuScreen.r2.toString(), cpuScreen.r3.toString())
                        } else {
                            alertError(getString(R.string.no_internet))
                        }
                    }, 3000)
                }
            }
        }else{

            Log.e("type ", "Frendly")

            if (action.equals("timeOver",true)){
                Log.e("My Score ", sessionManager.getMyScore().toString())
                Log.e("Cpu Score ", sessionManager.getCpuScore().toString())
//            moveToScoreFragment(0,dialog)
                Handler(Looper.myLooper()!!).postDelayed({
                    dialog.dismiss()
                    sessionManager.setExtraTimeUser("timeOver")
                    setCondition("timeOver")
                }, 3000)
            }

            if (action.equals("halftime",true)){
                Handler(Looper.myLooper()!!).postDelayed({
                    val screen = setGames.setScreen(sessionManager.getUserScreenType())
                    val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
                    if (sessionManager.isNetworkAvailable()) {
                        cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
                        myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
                        cpuDbHelper.deleteAllPlayers()
                        myPlayerDbHelper.deleteAllPlayers()
                        extraPLayerDbHelper.deleteAllPlayers()
                        getGuessTeamList(dialog, screen.r1.toString(), screen.r2.toString(), screen.r3.toString(), cpuScreen.r1.toString(), cpuScreen.r2.toString(), cpuScreen.r3.toString())
                    } else {
                        alertError(getString(R.string.no_internet))
                    }
                }, 3000)
            }

        }
        dialog.show()
    }


    // This function is used for get guess player list from database api
    private fun getGuessTeamList(dialog: Dialog?, defender: String, midfielder: String, attacker: String, cpuDefender: String, cpuMidFielder: String, cpuAttacker: String) {
        val match_no = (sessionManager.getGameNumber()-1)
        getGuessPlayerListViewmodel.getGuessPlayerList("$token", defender, midfielder, attacker, "", "",
            match_no.toString())
        getGuessPlayerListViewmodel.getGuessPlayerListResponse.observe(this) { response ->
            Log.d("@@@Error ","Api response "+response)
            if (response != null) {
                if (response.isSuccessful) {
                    val teamResponse = response.body()
                    if (teamResponse != null) {
                        if (teamResponse.data != null) {
                            if (teamResponse.data.myplayer != null) {
                                if (sessionManager.getMatchType().equals("worldcup",true)){
                                    if (sessionManager.getExtraTime().equals("ExtraTime",true) || sessionManager.getExtraTime().equals("TimeHalf",true)){
                                        totalTime=900000
                                        startTime=0
                                        sessionManager.saveTimer(0)
                                        sessionManager.increaseTimer(0)
                                    }else{
                                        sessionManager.increaseTimer(120000)
                                    }
                                }else{
                                    sessionManager.increaseTimer(120000)
                                }

                                var df = defender.toInt()
                                var mf = midfielder.toInt()
                                var fw = attacker.toInt()
                                Log.e("Player", myPlayerDbHelper.getAllPlayers().size.toString())
                                try {
                                    // Defender
                                    for (data in teamResponse.data.myplayer) {
                                        if (data.is_captain == 1) {
                                            sessionManager.setMyPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        if (data.designation == "DF") {
                                            if (df > 0) {
                                                df -= 1
                                                myPlayerDbHelper.addPlayer(
                                                    surnames,
                                                    data.is_captain.toString(),
                                                    data.country_id.toString(),
                                                    data.type.toString(),
                                                    data.designation.toString(),
                                                    data.jersey_number.toString(),
                                                    "false",
                                                    "false",
                                                )
                                            }
                                        }
                                    }
                                    // MidFielder
                                    for (data in teamResponse.data.myplayer) {
                                        if (data.is_captain == 1) {
                                            sessionManager.setMyPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        if (data.designation == "MF") {
                                            if (mf > 0) {
                                                mf -= 1
                                                myPlayerDbHelper.addPlayer(
                                                    surnames,
                                                    data.is_captain.toString(),
                                                    data.country_id.toString(),
                                                    data.type.toString(),
                                                    data.designation.toString(),
                                                    data.jersey_number.toString(),
                                                    "false",
                                                    "false",
                                                )
                                            }
                                        }
                                    }
                                    //Striker
                                    for (data in teamResponse.data.myplayer) {
                                        if (data.is_captain == 1) {
                                            sessionManager.setMyPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        if (data.designation == "FW") {
                                            if (fw > 0) {
                                                fw -= 1
                                                myPlayerDbHelper.addPlayer(
                                                    surnames,
                                                    data.is_captain.toString(),
                                                    data.country_id.toString(),
                                                    data.type.toString(),
                                                    data.designation.toString(),
                                                    data.jersey_number.toString(),
                                                    "false",
                                                    "false",
                                                )
                                            }
                                        }
                                    }

                                    // Goalkeeper
                                    //Shrawan
                                    for (data in teamResponse.data.myplayer) {
                                        if (data.is_captain == 1) {
                                            sessionManager.setCpuPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }

                                        if (data.designation == "GK") {
                                            myPlayerDbHelper.addPlayer(
                                                surnames,
                                                data.is_captain.toString(),
                                                data.country_id.toString(),
                                                data.type.toString(),
                                                data.designation.toString(),
                                                data.jersey_number.toString(),
                                                "false",
                                                "false"
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("My Player Database Error", e.toString())
                                }
                            }
                            if (teamResponse.data.cpuplayer != null) {
                                var df = cpuDefender.toInt()
                                var mf = cpuMidFielder.toInt()
                                var fw = cpuAttacker.toInt()
                                try {
                                    // Defender
                                    for (data in teamResponse.data.cpuplayer) {
                                        if (data.is_captain == 1) {
                                            sessionManager.setCpuPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        if (data.designation == "DF") {
                                            if (df > 0) {
                                                df -= 1
                                                cpuDbHelper.addPlayer(
                                                    surnames,
                                                    data.is_captain.toString(),
                                                    data.country_id.toString(),
                                                    data.type.toString(),
                                                    data.designation.toString(),
                                                    data.jersey_number.toString(),
                                                    "false",
                                                    "false",
                                                )
                                            }
                                        }
                                    }
                                    // MidFielder
                                    for (data in teamResponse.data.cpuplayer) {
                                        if (data.is_captain == 1) {
                                            sessionManager.setCpuPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        if (data.designation == "MF") {
                                            if (mf > 0) {
                                                mf -= 1
                                                cpuDbHelper.addPlayer(
                                                    surnames,
                                                    data.is_captain.toString(),
                                                    data.country_id.toString(),
                                                    data.type.toString(),
                                                    data.designation.toString(),
                                                    data.jersey_number.toString(),
                                                    "false",
                                                    "false",
                                                )
                                            }
                                        }
                                    }
                                    //Striker
                                    for (data in teamResponse.data.cpuplayer) {
                                        if (data.is_captain == 1) {
                                            sessionManager.setCpuPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        if (data.designation == "FW") {
                                            if (fw > 0) {
                                                fw -= 1
                                                cpuDbHelper.addPlayer(
                                                    surnames,
                                                    data.is_captain.toString(),
                                                    data.country_id.toString(),
                                                    data.type.toString(),
                                                    data.designation.toString(),
                                                    data.jersey_number.toString(),
                                                    "false",
                                                    "false",
                                                )
                                            }
                                        }
                                    }

                                    // Goalkeeper
                                    //Shrawan
                                    for (data in teamResponse.data.cpuplayer) {
                                        if (data.is_captain == 1) {
                                            sessionManager.setCpuPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }

                                        if (data.designation == "GK") {
                                            cpuDbHelper.addPlayer(
                                                surnames,
                                                data.is_captain.toString(),
                                                data.country_id.toString(),
                                                data.type.toString(),
                                                data.designation.toString(),
                                                data.jersey_number.toString(),
                                                "false",
                                                "false"
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("My Player Database Error", e.toString())
                                }
                            }
                            if (teamResponse.data.SubtitutePlyer != null) {
                                try {
                                    // Extra Player
                                    for (data in teamResponse.data.SubtitutePlyer) {
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        extraPLayerDbHelper.addPlayer(
                                            surnames,
                                            data.is_captain.toString(),
                                            data.country_id.toString(),
                                            data.type.toString(),
                                            data.designation.toString(),
                                            data.jersey_number.toString(),
                                            "false",
                                            "USER"
                                        )
                                    }
                                } catch (e: Exception) {
                                    Log.e("My Player Database Error", e.toString())
                                }
                            }
                            checkAllPlayer(dialog)
                        } else {
                            sessionManager.alertError("Player list not found !")
                        }
                    } else {
                        sessionManager.alertError(response.message().toString())
                    }
                } else {
                    sessionManager.alertError(response.errorBody().toString())
                }

            } else {
                sessionManager.alertError("Check Your Internet Connection")
            }
        }
    }

    // This function is used for check cpu and user team player list and verify
    private fun checkAllPlayer(dialog: Dialog?) {
        if (myPlayerDbHelper.getAllPlayers().size < 10) {
            val remain = 10 - myPlayerDbHelper.getAllPlayers().size
            if (remain > 1) {
                for (i in 0 until remain) {
                    myPlayerDbHelper.addPlayer(
                        "SYSTEM",
                        "0",
                        "ENG",
                        "no",
                        "MF",
                        "10",
                        "false",
                        "false"
                    )
                }
            } else {
                myPlayerDbHelper.addPlayer("SYSTEM", "0", "ENG", "no", "MF", "10", "false", "false")
            }
        }
        if (cpuDbHelper.getAllPlayers().size < 10) {
            val remain = 10 - cpuDbHelper.getAllPlayers().size
            if (remain > 1) {
                for (i in 0 until remain) {
                    cpuDbHelper.addPlayer("ANDROID", "0", "ENG", "no", "MF", "10", "false", "false")
                }
            } else {
                cpuDbHelper.addPlayer("ANDROID", "0", "ENG", "no", "MF", "10", "false", "false")
            }
        }
        Log.e("My Team :", myPlayerDbHelper.getAllPlayers().toString())
        Log.e("My Team Size :", myPlayerDbHelper.getAllPlayers().size.toString())
        Log.e("CPU Team :", cpuDbHelper.getAllPlayers().toString())
        Log.e("CPU Team Size :", cpuDbHelper.getAllPlayers().size.toString())
        Log.e("Extra Team :", extraPLayerDbHelper.getAllPlayers().toString())
        Log.e("Extra Team Size :", extraPLayerDbHelper.getAllPlayers().size.toString())

        val bundle = Bundle()
        if (userType.equals("CPU",true)) {
            bundle.putString("userType", "USER")
        } else {
            bundle.putString("userType", "CPU")
        }
        findNavController().navigate(R.id.playScreenFragment, bundle)
        Handler(Looper.myLooper()!!).postDelayed({
            dialog?.dismiss()
//            setScreens()
        }, 2000)
    }

    @SuppressLint("SetTextI18n")
    fun alertError(msg: String) {
        val dialog = Dialog(requireContext(), R.style.BottomSheetDialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alertbox_error)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams
        val tvTitle: TextView = dialog.findViewById(R.id.tv_title)
        val btnOk: LinearLayout = dialog.findViewById(R.id.btn_ok)
        val btText: TextView = dialog.findViewById(R.id.btText)
        btText.text = "Retry"
        tvTitle.text = msg
        btnOk.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
    }

    // This function call get last player textview id
    private fun getTestUserShoot() {
        val onSelectedScreen = if (userType.equals("USER",true)) {
            sessionManager.getUserScreenType()
        } else {
            sessionManager.getCpuScreenType()
        }
        when (onSelectedScreen) {
            "5-2-3" -> {
                val textViews: MutableList<TextView> = mutableListOf(
                    r1p1_id,
                    r1p2_id,
                    r1p3_id,
                    r1p4_id,
                    r1p5_id,
                    r2p1_id,
                    r2p2_id,
                    r3p1_id,
                    r3p2_id,
                    r3p3_id,
                    r0p1_id //Shrawan
                )
                val textViewsCPU: MutableList<TextView> = mutableListOf(
                    cpur1p1_id,
                    cpur1p2_id,
                    cpur1p3_id,
                    cpur1p4_id,
                    cpur1p5_id,
                    cpur2p1_id,
                    cpur2p2_id,
                    cpur3p1_id,
                    cpur3p2_id,
                    cpur3p3_id,
                    cpur0p1_id //Shrawan
                )
                selectedPlayerNum = if (userType.equals("USER",true)) {
                    textViews[playerIdUser - 1]
                } else {
                    textViewsCPU[playerIdUser - 1]
                }
            }  // Done
            "5-4-1" -> {
                val textViews: MutableList<TextView> = mutableListOf(
                    r1p1_id,
                    r1p2_id,
                    r1p3_id,
                    r1p4_id,
                    r1p5_id,
                    r2p1_id,
                    r2p2_id,
                    r2p3_id,
                    r2p4_id,
                    r3p1_id,
                    r0p1_id //Shrawan
                )
                val textViewsCPU: MutableList<TextView> = mutableListOf(
                    cpur1p1_id,
                    cpur1p2_id,
                    cpur1p3_id,
                    cpur1p4_id,
                    cpur1p5_id,
                    cpur2p1_id,
                    cpur2p2_id,
                    cpur2p3_id,
                    cpur2p4_id,
                    cpur3p1_id,
                    cpur0p1_id //Shrawan
                )
                selectedPlayerNum = if (userType.equals("USER",true)) {
                    textViews[playerIdUser - 1]
                } else {
                    textViewsCPU[playerIdUser - 1]
                }
            }  // Done
            "5-3-2" -> {
                val textViews: MutableList<TextView> = mutableListOf(
                    r1p1_id,
                    r1p2_id,
                    r1p3_id,
                    r1p4_id,
                    r1p5_id,
                    r2p1_id,
                    r2p2_id,
                    r2p3_id,
                    r3p1_id,
                    r3p2_id,
                    r0p1_id //Shrawan
                )
                val textViewsCPU: MutableList<TextView> = mutableListOf(
                    cpur1p1_id,
                    cpur1p2_id,
                    cpur1p3_id,
                    cpur1p4_id,
                    cpur1p5_id,
                    cpur2p1_id,
                    cpur2p2_id,
                    cpur2p3_id,
                    cpur3p1_id,
                    cpur3p2_id,
                    cpur0p1_id //Shrawan
                )
                selectedPlayerNum = if (userType.equals("USER",true)) {
                    textViews[playerIdUser - 1]
                } else {
                    textViewsCPU[playerIdUser - 1]
                }
            }  // Done
            "3-5-2" -> {
                val textViews: MutableList<TextView> = mutableListOf(
                    r1p1_id,
                    r1p2_id,
                    r1p3_id,
                    r2p1_id,
                    r2p2_id,
                    r2p3_id,
                    r2p4_id,
                    r2p5_id,
                    r3p1_id,
                    r3p2_id,
                    r0p1_id //Shrawan
                )
                val textViewsCPU: MutableList<TextView> = mutableListOf(
                    cpur1p1_id,
                    cpur1p2_id,
                    cpur1p3_id,
                    cpur2p1_id,
                    cpur2p2_id,
                    cpur2p3_id,
                    cpur2p4_id,
                    cpur2p5_id,
                    cpur3p1_id,
                    cpur3p2_id,
                    cpur0p1_id //Shrawan
                )
                selectedPlayerNum = if (userType.equals("USER",true)) {
                    textViews[playerIdUser - 1]
                } else {
                    textViewsCPU[playerIdUser - 1]
                }
            }  // Done
            "4-5-1" -> {
                val textViews: MutableList<TextView> = mutableListOf(
                    r1p1_id,
                    r1p2_id,
                    r1p3_id,
                    r1p4_id,
                    r2p1_id,
                    r2p2_id,
                    r2p3_id,
                    r2p4_id,
                    r2p5_id,
                    r3p1_id,
                    r0p1_id //Shrawan
                )
                val textViewsCPU: MutableList<TextView> = mutableListOf(
                    cpur1p1_id,
                    cpur1p2_id,
                    cpur1p3_id,
                    cpur1p4_id,
                    cpur2p1_id,
                    cpur2p2_id,
                    cpur2p3_id,
                    cpur2p4_id,
                    cpur2p5_id,
                    cpur3p1_id,
                    cpur0p1_id //Shrawan
                )
                selectedPlayerNum = if (userType.equals("USER",true)) {
                    textViews[playerIdUser - 1]
                } else {
                    textViewsCPU[playerIdUser - 1]
                }
            }  // Done
            "4-4-2" -> {
                val textViews: MutableList<TextView> = mutableListOf(
                    r1p1_id,
                    r1p2_id,
                    r1p3_id,
                    r1p4_id,
                    r2p1_id,
                    r2p2_id,
                    r2p3_id,
                    r2p4_id,
                    r3p1_id,
                    r3p2_id,
                    r0p1_id //Shrawan
                )
                val textViewsCPU: MutableList<TextView> = mutableListOf(
                    cpur1p1_id,
                    cpur1p2_id,
                    cpur1p3_id,
                    cpur1p4_id,
                    cpur2p1_id,
                    cpur2p2_id,
                    cpur2p3_id,
                    cpur2p4_id,
                    cpur3p1_id,
                    cpur3p2_id,
                    cpur0p1_id //Shrawan
                )
                selectedPlayerNum = if (userType.equals("USER",true)) {
                    textViews[playerIdUser - 1]
                } else {
                    textViewsCPU[playerIdUser - 1]
                }
            }  // Done
            "4-3-3" -> {
                val textViews: MutableList<TextView> = mutableListOf(
                    r1p1_id,
                    r1p2_id,
                    r1p3_id,
                    r1p4_id,
                    r2p1_id,
                    r2p2_id,
                    r2p3_id,
                    r3p1_id,
                    r3p2_id,
                    r3p3_id,
                    r0p1_id //Shrawan
                )
                val textViewsCPU: MutableList<TextView> = mutableListOf(
                    cpur1p1_id,
                    cpur1p2_id,
                    cpur1p3_id,
                    cpur1p4_id,
                    cpur2p1_id,
                    cpur2p2_id,
                    cpur2p3_id,
                    cpur3p1_id,
                    cpur3p2_id,
                    cpur3p3_id,
                    cpur0p1_id //Shrawan
                )
                selectedPlayerNum = if (userType.equals("USER",true)) {
                    textViews[playerIdUser - 1]
                } else {
                    textViewsCPU[playerIdUser - 1]
                }
            }  // Done
            "3-4-3" -> {
                val textViews: MutableList<TextView> = mutableListOf(
                    r1p1_id,
                    r1p2_id,
                    r1p3_id,
                    r2p1_id,
                    r2p2_id,
                    r2p3_id,
                    r2p4_id,
                    r3p1_id,
                    r3p2_id,
                    r3p3_id,
                    r0p1_id //Shrawan
                )
                val textViewsCPU: MutableList<TextView> = mutableListOf(
                    cpur1p1_id,
                    cpur1p2_id,
                    cpur1p3_id,
                    cpur2p1_id,
                    cpur2p2_id,
                    cpur2p3_id,
                    cpur2p4_id,
                    cpur3p1_id,
                    cpur3p2_id,
                    cpur3p3_id,
                    cpur0p1_id //Shrawan
                )
                selectedPlayerNum = if (userType.equals("USER",true)) {
                    textViews[playerIdUser - 1]
                } else {
                    textViewsCPU[playerIdUser - 1]
                }
            }  // Done
            "4-2-4" -> {
                val textViews: MutableList<TextView> = mutableListOf(
                    r1p1_id,
                    r1p2_id,
                    r1p3_id,
                    r1p4_id,
                    r2p1_id,
                    r2p2_id,
                    r3p1_id,
                    r3p2_id,
                    r3p3_id,
                    r3p4_id,
                    r0p1_id //Shrawan
                )
                val textViewsCPU: MutableList<TextView> = mutableListOf(
                    cpur1p1_id,
                    cpur1p2_id,
                    cpur1p3_id,
                    cpur1p4_id,
                    cpur2p1_id,
                    cpur2p2_id,
                    cpur3p1_id,
                    cpur3p2_id,
                    cpur3p3_id,
                    cpur3p4_id,
                    cpur0p1_id //Shrawan
                )
                selectedPlayerNum = if (userType.equals("USER",true)) {
                    textViews[playerIdUser - 1]
                } else {
                    textViewsCPU[playerIdUser - 1]
                }
            }  // Done
            else -> {
                Toast.makeText(requireContext(), "Formation is not Exist $onSelectedScreen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // This function is used for display the alert box of world cup is won or loss
    private fun winAlertBox(int: Int) {
        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView = dialog.findViewById(R.id.img_change)
        sessionManager.changeMusic(21, 0)
        if (int==0){
            imgChange.setImageResource(R.drawable.eliminated_img)
        }
        if (int==2){
            imgChange.setImageResource(R.drawable.winner_img)
        }
        if (int==1){
            imgChange.setImageResource(R.drawable.penalties_img)
        }
        if (int==3){
            imgChange.setImageResource(R.drawable.winner_img)
        }
        moveToScoreFragment(int,dialog)
        dialog.show()
    }

    private fun moveToScoreFragment(){

    }

    private fun moveToScoreFragment(moveType:Int,dialog:Dialog){
        val cpuName = oppose_team_player_name.text.toString()
        val myName = user_name.text.toString()
        Handler(Looper.myLooper()!!).postDelayed({
            dialog.dismiss()
            if (moveType==0){
                if (sessionManager.getMatchType().equals("worldcup",true)){
                    val bundle = Bundle()
                    bundle.putString("opposeTeamName", cpuName)
                    bundle.putString("myTeamName", myName)
                    findNavController().navigate(R.id.score_fragment, bundle)
                }else{
                    sessionManager.resetScore()
                    sessionManager.resetGameNumberScore()
                    val intent=Intent(requireContext(),MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
            if (moveType==1){
                val bundle = Bundle()
                bundle.putString("type", "main")
                findNavController().navigate(R.id.selectTossFragment, bundle)
            }
            if (moveType==2){
                sessionManager.resetScore()
                sessionManager.resetGameNumberScore()
                val intent=Intent(requireContext(),MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            if (moveType==3){
                val bundle = Bundle()
                bundle.putString("opposeTeamName", cpuName)
                bundle.putString("myTeamName", myName)
                findNavController().navigate(R.id.score_fragment, bundle)
            }
        }, 3000)

    }
    override fun onDestroy() {
        super.onDestroy()
        onDestroyAndOnStop()
        Log.e("@@@Error","Distro Timer Hold")
    }
    override fun onStop() {
        onDestroyAndOnStop()
        Log.e("@@@Error","Stop TimerHold")
        super.onStop()
    }
    private  fun onDestroyAndOnStop(){
        if (isTimerRunning) {
            Log.d("@@@Error", "stop or destroy$startTime")
            sessionManager.saveTimer(startTime)
//            wholeTimer.cancel()
            isTimerRunning = false
            stopCpuProcess()
        }
    }
}
