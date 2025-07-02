//package com.yesitlabs.jumballapp.fragment.manfragment
//import android.annotation.SuppressLint
//import android.app.Dialog
//import android.content.Intent
//import android.os.Bundle
//import android.os.CountDownTimer
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.WindowManager
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.OnBackPressedCallback
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.navigation.fragment.findNavController
//import com.bumptech.glide.Glide
//import com.yesitlabs.jumballapp.R
//import com.yesitlabs.jumballapp.database.player_dtl.CPUPlayerDatabaseHelper
//import com.yesitlabs.jumballapp.database.player_dtl.PlayerDatabaseHelper
//import com.yesitlabs.jumballapp.database.player_dtl.PlayerModel
//import com.yesitlabs.jumballapp.database.team_dtl.TeamDatabaseHelper
//import com.yesitlabs.jumballapp.gameRule.SetGames
//import com.yesitlabs.jumballapp.SessionManager
//import com.yesitlabs.jumballapp.ValueStore
//import com.yesitlabs.jumballapp.activity.MainActivity
//import com.yesitlabs.jumballapp.database.player_dtl.ExtraPlayerDatabaseHelper
//import com.yesitlabs.jumballapp.databinding.FragmentPlayScreenBinding
//import com.yesitlabs.jumballapp.network.viewModel.GetGuessPlayerListViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import java.util.Locale
//import kotlin.random.Random
//
//
//
//@AndroidEntryPoint
//class PlayScreenLatestFragment : Fragment(), View.OnClickListener {
//
//    private lateinit var binding: FragmentPlayScreenBinding
//    private var setGames: SetGames = SetGames()
//    private lateinit var wholeTimer: CountDownTimer
//    private var totalPlayerNameShow = 0
//    private var cpuPlayerNameShow = 0
//    private var playerSelectProcessComplete = false
//    private var selectionPower = false
//    private var isTimerRunning = true
//    var isTimerFinish = false
//    private var userR1 = 0
//    private var userR2 = 0
//    private var userR3 = 0
//    private var cpuR1 = 0
//    private var cpuR2 = 0
//    private var cpuR3 = 0
//
//    private var userType: String? = null
//    var totalTime = 5400000L
//    //    var startTime = 2670000
////    var startTime = 5300000
//    var startTime = 0
//    private var myPass = 0
//    private var cpuPass = 0
//    private lateinit var cpuDbHelper: CPUPlayerDatabaseHelper
//    private lateinit var myPlayerDbHelper: PlayerDatabaseHelper
//    private lateinit var teamDbHelper: TeamDatabaseHelper
//    private var allCpuPlayer = ArrayList<PlayerModel>()
//    private var allUserPlayer = ArrayList<PlayerModel>()
//    lateinit var sessionManager: SessionManager
//    private var selectedPlayerNum: TextView? = null
//    private var playerIdUser = 0
//    private var selectionView: View? = null
//    var status=true
//    private lateinit var getGuessPlayerListViewmodel: GetGuessPlayerListViewModel
//
//    var token: String? = null
//    private lateinit var extraPLayerDbHelper: ExtraPlayerDatabaseHelper
//
//    private var isCpuActive = false
//    private val handler = Handler(Looper.getMainLooper())
//
//    //Shrawan
//    private var  isGoalClick  = false
//
//    private val runnable = object : Runnable {
//        override fun run() {
//            if (isCpuActive) {
//                selectCpuButton()
//                handler.postDelayed(this, 3000) // Repeat every 3 seconds
//            }
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        binding = FragmentPlayScreenBinding.inflate(inflater, container, false)
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        sessionManager = SessionManager(requireContext())
//        getGuessPlayerListViewmodel = ViewModelProvider(this)[GetGuessPlayerListViewModel::class.java]
//
//        extraPLayerDbHelper = ExtraPlayerDatabaseHelper(requireContext())
//
//        token = "Bearer " + sessionManager.fetchAuthToken()
//
//        if (sessionManager.getExtraTime().equals("ExtraTime",true) || sessionManager.getExtraTime().equals("TimeHalf",true)){
//            binding.layProgess.max = 900000
//            totalTime=900000
//            startTime=0
//            if (sessionManager.getTimer() != 0) {
//                startTime = sessionManager.getTimer()
//                if (startTime>=900000){
//                    totalTime=900000
//                }else{
//                    totalTime -= sessionManager.getTimer().toLong()
//                }
//            }
//        }else{
//            binding.layProgess.max = 2700000
//            if (sessionManager.getTimer() != 0) {
//                startTime = sessionManager.getTimer()
//                if (startTime>=5400000){
//                    totalTime=5400000
//                }else{
//                    totalTime -= sessionManager.getTimer().toLong()
//                }
//            }
//        }
//
//        Log.d("@@@Error ", "getExtraTime()"+sessionManager.getExtraTime())
//        Log.d("@@@Error ", "sessionManager.getTimer()"+sessionManager.getTimer())
//        Log.d("@@@Error ","match count"+sessionManager.getGameNumber())
//        Log.d("@@@Error ","timer count"+sessionManager.getTimer())
//        Log.d("@@@Error ", "startTime$startTime")
//        Log.d("@@@Error ", "totalTime$totalTime")
//
//        myPass = sessionManager.getMyPass()
//        cpuPass = sessionManager.getCpuPass()
//
//        if (arguments != null) {
//            userType = requireArguments().getString("userType").toString().uppercase(Locale.ROOT)
//            playerIdUser = requireArguments().getInt("id")
//            isGoalClick = requireArguments().getBoolean("isGoalClick",false)//Shrawan
//        } else {
//            Log.e("Argument", "No")
//        }
//
//        Log.d("@@@Error ", "myPass$myPass")
//
//        Log.d("@@@Error ", "cpuPass$cpuPass")
//
//        Log.d("@@@Error ", "userType$userType")
//
//        if (userType.equals("USER",true)) {
//            sessionManager.setFirstGamgeStartUser(false)
//            sessionManager.setFirstGamgeStartCPU(true)
//        } else {
//            sessionManager.setFirstGamgeStartCPU(false)
//            sessionManager.setFirstGamgeStartUser(true)
//        }
//
//
//        cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
//        myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
//
//
//
//        timerLogic()
//
////        attachTimer()
////        wholeTimer.start()
//
//        backButton()
//
//        setScreens()
//
//        binding.btShoot.setOnClickListener(this)
//
//
//    }
//
//    private fun timerLogic(){
////        startTime += 1000
//        val min = startTime / 60000
//        binding.tvCount.text = "‘$min"
//        val timeForNoise = startTime / 60000
//        Log.d("@@@Error", "***** $timeForNoise")
//        Log.d("@@@Error", "***** show time tv_count  $min")
//
//        if (timeForNoise >= 3 && timeForNoise % 3 == 0) {
//            sessionManager.changeMusic(14, 0)
//        }
//        if (timeForNoise >= 5 && timeForNoise % 5 == 0) {
//            sessionManager.changeMusic(1, 1)
//        }
//
//        if (timeForNoise >= 9 && timeForNoise % 9 == 0) {
//            sessionManager.changeMusic(1, 1)
//        }
//
//        if (startTime / 60000 == 0 && startTime > 60000) {
//            Log.e("Time ", min.toString())
//        }
//
//        Log.d("@@@Error ","******** "+sessionManager.getExtraTime())
//
//        if (sessionManager.getExtraTime().equals("ExtraTime",true) || sessionManager.getExtraTime().equals("TimeHalf",true)){
//            sessionManager.changeMusic(6, 0)
//            if (sessionManager.getExtraTime().equals("ExtraTime",true)){
//                Log.d("@@@Error","when TimeHalf time set")
//                if (startTime >= 900000){
//                    totalTime = 900000L
//                    startTime = 0
//                    status=false
//                    setCondition("ExtraTime")
//                }
//            }
//
//            if (sessionManager.getExtraTime().equals("TimeHalf",true)){
//                if (startTime >= 900000){
//                    Log.d("@@@Error","when FinalTime time set")
//                    totalTime = 900000L
//                    startTime = 0
//                    sessionManager.setExtraTimeUser("TimeHalfEnd")
//                    status=false
//                    setCondition("TimeHalfEnd")
//                }
//            }
//
//            if (startTime <= 900000) {
//                binding.layProgess.progress = startTime
//            } else {
//                binding.layProgess.progress = startTime - 900000
//            }
//
//        }else{
//            Log.e("@@@Error", "Full getValue "+ValueStore.getValue1())
//            if (startTime >= 5400000 /*&& ValueStore.getValue1() ==0*/){
//                status=false
//                ValueStore.setValue1(1)
//                Log.d("@@@Error","when FullTime time set")
//                Log.e("@@@Error", "Full time")
//                sessionManager.changeMusic(6, 0)
//                isTimerFinish = true
//                if (sessionManager.getGameNumber()==3){
//                    totalTime = 900000L
//                    startTime = 0
//                    sessionManager.setExtraTimeUser("FullTime")
//                    Log.e("@@@Error", "second")
//                    playAlertBox(R.drawable.full_time_img, "FullTime")
//                }else{
//                    sessionManager.setExtraTimeUser("timeOver")
//                    Log.e("@@@Error", "timeOver")
//                    playAlertBox(R.drawable.full_time_img, "timeOver")
//                }
//            }else{
//                if (startTime >= 2700000 && ValueStore.getValue() == 0) {
//                    ValueStore.setValue(1)
//                    status=false
//                    Log.d("@@@Error","when half time start")
//                    Log.e("Half Time", "Yaa hui")
//                    sessionManager.changeMusic(6, 0)
//                    sessionManager.setExtraTimeUser("halftime")
//                    playAlertBox(R.drawable.half_time_img, "halftime")
//                }
//            }
//
//            if(startTime <= 2700000) {
//                binding.layProgess.progress = startTime
//            }else {
//                binding.layProgess.progress = startTime - 2700000
//            }
//        }
//
//    }
//
//    fun backButton(){
//        val callback: OnBackPressedCallback =
//            object : OnBackPressedCallback(true /* enabled by default */) {
//                override fun handleOnBackPressed() {
//                    requireActivity().finish()
//                }
//            }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
//    }
//
//    override fun onClick(item: View?) {
//
//        val onSelectedScreen = if (userType.equals("USER",true)) {
//            sessionManager.getUserScreenType()
//            isTimerFinish = false
//        } else {
//            sessionManager.getCpuScreenType()
//        }
//        val itemSelect=Random.nextInt(1, 10)
//        Log.d("****** itemSelect Random number :- ", "****** :- $itemSelect")
//        Log.d("****** onSelectedScreen number :- ", "****** :- ${onSelectedScreen.toString()}")
//        Log.d("****** LifeLine first :- ", "****** :- ${sessionManager.getLifeLine1()}")
//        Log.d("****** LifeLine first1 :- ", "****** :- ${sessionManager.getLifeLine11()}")
//        Log.d("****** getFirstGamgeStartUser :- ", "****** :- ${sessionManager.getFirstGamgeStartUser()}")
//
//        Log.d("@Error ","user click")
//        if (!isTimerFinish) {
//            when (item!!.id) {
//                //Shrawan
//                R.id.r0p1_img ->{
//                    if (binding.r0p1Select.visibility==View.VISIBLE) {
//                        playerClickWork(binding.r0p1, binding.r0p1Name, binding.r0p1Number, binding.r0p1Id, binding.r0p1Txt, binding.r0p1Select)
//                    }
//                }
//                R.id.r1p1_img -> {
//                    if (binding.r1p1Select.visibility==View.VISIBLE) {
//                        playerClickWork(binding.r1p1, binding.r1p1Name, binding.r1p1Number, binding.r1p1Id, binding.r1p1Txt, binding.r1p1Select)
//                    }
//                }
//                R.id.r1p2_img -> {
//                    if (binding.r1p2Select.visibility==View.VISIBLE) {
//                        playerClickWork(binding.r1p2, binding.r1p2Name, binding.r1p2Number, binding.r1p2Id, binding.r1p2Txt, binding.r1p2Select)
//                    }
//                }
//                R.id.r1p3_img -> {
//                    if (binding.r1p3Select.visibility==View.VISIBLE) {
//                        playerClickWork(binding.r1p3, binding.r1p3Name, binding.r1p3Number, binding.r1p3Id, binding.r1p3Txt, binding.r1p3Select)
//                    }
//                }
//                R.id.r1p4_img -> {
//                    if (binding.r1p4Select.visibility==View.VISIBLE) {
//                        playerClickWork(binding.r1p4, binding.r1p4Name, binding.r1p4Number, binding.r1p4Id, binding.r1p4Txt, binding.r1p4Select)
//                    }
//                }
//                R.id.r1p5_img -> {
//                    if (binding.r1p5Select.visibility==View.VISIBLE) {
//                        playerClickWork(binding.r1p5, binding.r1p5Name, binding.r1p5Number, binding.r1p5Id, binding.r1p5Txt, binding.r1p5Select)
//                    }
//                }
//                R.id.r2p1_img -> {
//                    playerClickWork(binding.r2p1, binding.r2p1Name, binding.r2p1Number, binding.r2p1Id, binding.r2p1Txt, binding.r2p1Select)
//                }
//                R.id.r2p2_img -> {
//                    playerClickWork(binding.r2p2, binding.r2p2Name, binding.r2p2Number, binding.r2p2Id, binding.r2p2Txt, binding.r2p2Select)
//                }
//                R.id.r2p3_img -> {
//                    playerClickWork(binding.r2p3, binding.r2p3Name, binding.r2p3Number, binding.r2p3Id, binding.r2p3Txt, binding.r2p3Select)
//                }
//                R.id.r2p4_img -> {
//                    playerClickWork(binding.r2p4, binding.r2p4Name, binding.r2p4Number, binding.r2p4Id, binding.r2p4Txt, binding.r2p4Select)
//                }
//                R.id.r2p5_img -> {
//                    playerClickWork(binding.r2p5, binding.r2p5Name, binding.r2p5Number, binding.r2p5Id, binding.r2p5Txt, binding.r2p5Select)
//                }
//                R.id.r3p1_img -> {
//                    if (binding.r3p1Select.visibility==View.VISIBLE) {
//                        playerClickWork(binding.r3p1, binding.r3p1Name, binding.r3p1Number, binding.r3p1Id, binding.r3p1Txt, binding.r3p1Select)
//                    }
//                }
//                R.id.r3p2_img -> {
//                    if (binding.r3p2Select.visibility==View.VISIBLE) {
//                        playerClickWork(binding.r3p2, binding.r3p2Name, binding.r3p2Number, binding.r3p2Id, binding.r3p2Txt, binding.r3p2Select)
//                    }
//                }
//                R.id.r3p3_img -> {
//                    if (binding.r3p3Select.visibility==View.VISIBLE) {
//                        playerClickWork(binding.r3p3, binding.r3p3Name, binding.r3p3Number, binding.r3p3Id, binding.r3p3Txt, binding.r3p3Select)
//                    }
//                }
//                R.id.r3p4_img -> {
//                    if (binding.r3p4Select.visibility==View.VISIBLE) {
//                        playerClickWork(binding.r3p4, binding.r3p4Name, binding.r3p4Number, binding.r3p4Id, binding.r3p4Txt, binding.r3p4Select)
//                    }
//                }
//                R.id.r3p5_img -> {
//                    if (binding.r3p5Select.visibility==View.VISIBLE) {
//                        playerClickWork(binding.r3p5, binding.r3p5Name, binding.r3p5Number, binding.r3p5Id, binding.r3p5Txt, binding.r3p5Select)
//                    }
//                }
//                R.id.bt_shoot -> {
//                    shootBall()
//                }
//                R.id.bt_pass -> {
//                    userPassBall()
//                }
//            }
//        }
//    }
//    //This function is used for attach the timer in play screen
////    private fun attachTimer() {
////        wholeTimer = object : CountDownTimer(totalTime, 500) {
////            @SuppressLint("SetTextI18n")
////            override fun onTick(time: Long) {
////                startTime += 1000
////
////                val min = startTime / 60000
////
////                if (tv_count != null) {
////                    tv_count.text = "‘$min"
////                }
////
////                val timeForNoise = startTime / 60000
////
////                Log.d("@@@Error", "***** $timeForNoise")
////                Log.d("@@@Error", "***** show time tv_count  $min")
////
////                if (timeForNoise >= 3 && timeForNoise % 3 == 0) {
////                    sessionManager.changeMusic(14, 0)
////                }
////                if (timeForNoise >= 5 && timeForNoise % 5 == 0) {
////                    sessionManager.changeMusic(1, 1)
////                }
////
////                if (timeForNoise >= 9 && timeForNoise % 9 == 0) {
////                    sessionManager.changeMusic(1, 1)
////                }
////
////                if (startTime / 60000 == 0 && startTime > 60000) {
////                    Log.e("Time ", min.toString())
////                }
////
////                Log.d("@@@Error ","******** "+sessionManager.getExtraTime())
////
////                if (sessionManager.getExtraTime().equals("ExtraTime",true) || sessionManager.getExtraTime().equals("TimeHalf",true)){
////                    sessionManager.changeMusic(6, 0)
////                    if (sessionManager.getExtraTime().equals("ExtraTime",true)){
////                        Log.d("@@@Error","when TimeHalf time set")
////                        if (startTime >= 900000){
////                            wholeTimer.cancel()
////                            totalTime = 900000L
////                            startTime = 0
////                            setCondition("ExtraTime")
////                        }
////                    }
////
////                    if (sessionManager.getExtraTime().equals("TimeHalf",true)){
////                        if (startTime >= 900000){
////                            Log.d("@@@Error","when FinalTime time set")
////                            wholeTimer.cancel()
////                            totalTime = 900000L
////                            startTime = 0
////                            sessionManager.setGameGameCondition(2)
////                            sessionManager.setExtraTimeUser("TimeHalfEnd")
////                            setCondition("TimeHalfEnd")
////                        }
////                    }
////
////                    if (startTime <= 900000) {
////                        binding.layProgess?.progress = startTime
////                    } else {
////                        binding.layProgess?.progress = startTime - 900000
////                    }
////
////                }else{
////                    Log.e("@@@Error", "Full getValue "+ValueStore.getValue1())
////                    if (startTime >= 5400000 && ValueStore.getValue1() ==0){
//////                        wholeTimer.cancel()
////                        ValueStore.setValue1(1)
////                        Log.d("@@@Error","when FullTime time set")
////                        Log.e("@@@Error", "Full time")
////                        sessionManager.changeMusic(6, 0)
////                        isTimerFinish = true
////                        if (sessionManager.getGameNumber()==3){
////                            totalTime = 900000L
////                            startTime = 0
////                            sessionManager.setExtraTimeUser("FullTime")
////                            sessionManager.setGameGameCondition(2)
////                            Log.e("@@@Error", "second")
////                            playAlertBox(R.drawable.full_time_img, "FullTime")
////                        }else{
////                            sessionManager.setExtraTimeUser("timeOver")
////                            sessionManager.setGameGameCondition(2)
////                            Log.e("@@@Error", "timeOver")
////                            playAlertBox(R.drawable.full_time_img, "timeOver")
////                        }
////                    }else{
////                        if (startTime >= 2700000 && ValueStore.getValue() ==0) {
////                            ValueStore.setValue(1)
//////                            wholeTimer.cancel() // Stop the timer
////                            Log.d("@@@Error","when half time start")
////                            Log.e("Half Time", "Yaa hui")
////                            sessionManager.changeMusic(6, 0)
////                            sessionManager.setExtraTimeUser("halftime")
////                            playAlertBox(R.drawable.half_time_img, "halftime")
////                        }
////                    }
////
////                    if(startTime <= 2700000) {
////                        binding.layProgess?.progress = startTime
////                    }else {
////                        binding.layProgess?.progress = startTime - 2700000
////                    }
////                }
////            }
////            override fun onFinish() {
////                Log.d("@@@Error","******* finish time "+sessionManager.getExtraTime())
////                Log.d("@@@Error","******* finish time getGameCondition "+sessionManager.getGameCondition())
////                Log.d("@@@Error","******* finish time startTime "+startTime)
////            }
////        }
////    }
//
//
//    private fun setCondition(status:String){
//        val cpuName = binding.opposeTeamPlayerName.text.toString()
//        val myName = binding.userName.text.toString()
//
//        Log.e("@@@Error", "status $status")
//        Log.e("@@@Error","getMyScore "+sessionManager.getMyScore())
//        Log.e("@@@Error","getCpuScore "+sessionManager.getCpuScore())
//        if (sessionManager.getMatchType().equals("worldcup",true)){
//            if (status.equals("timeOver",true)){
//                if (sessionManager.getMyScore() == sessionManager.getCpuScore()){
//                    val bundle = Bundle()
//                    bundle.putString("opposeTeamName", cpuName)
//                    bundle.putString("myTeamName", myName)
//                    findNavController().navigate(R.id.score_fragment, bundle)
//                }else if (sessionManager.getMyScore() > sessionManager.getCpuScore()){
//                    if (sessionManager.getGameNumber()>=3){
//                        winAlertBox(3)
//                    }else{
//                        val bundle = Bundle()
//                        bundle.putString("opposeTeamName", cpuName)
//                        bundle.putString("myTeamName", myName)
//                        findNavController().navigate(R.id.score_fragment, bundle)
//                    }
//
//                }else{
//                    val bundle = Bundle()
//                    bundle.putString("opposeTeamName", cpuName)
//                    bundle.putString("myTeamName", myName)
//                    findNavController().navigate(R.id.score_fragment, bundle)
//                }
//            }
//            if (status.equals("ExtraTime",true)){
//                if (sessionManager.getMyScore() == sessionManager.getCpuScore()){
//                    sessionManager.setExtraTimeUser("TimeHalf")
//                    playAlertBox(R.drawable.extra_time_ht_img,"TimeHalf")
//                }else if (sessionManager.getMyScore() > sessionManager.getCpuScore()){
//                    if (sessionManager.getGameNumber()>=3){
//                        winAlertBox(3)
//                    }else{
//                        val bundle = Bundle()
//                        bundle.putString("opposeTeamName", cpuName)
//                        bundle.putString("myTeamName", myName)
//                        findNavController().navigate(R.id.score_fragment, bundle)
//                    }
//                }else{
//                    val bundle = Bundle()
//                    bundle.putString("opposeTeamName", cpuName)
//                    bundle.putString("myTeamName", myName)
//                    findNavController().navigate(R.id.score_fragment, bundle)
//                }
//            }
//            if (status.equals("TimeHalfEnd",true)){
//                if (sessionManager.getMyScore() == sessionManager.getCpuScore()){
//                    winAlertBox(1)
//                } else if (sessionManager.getMyScore() > sessionManager.getCpuScore()){
//                    if (sessionManager.getGameNumber()>=3){
//                        winAlertBox(3)
//                    }else{
//                        val bundle = Bundle()
//                        bundle.putString("opposeTeamName", cpuName)
//                        bundle.putString("myTeamName", myName)
//                        findNavController().navigate(R.id.score_fragment, bundle)
//                    }
//                }else{
//                    val bundle = Bundle()
//                    bundle.putString("opposeTeamName", cpuName)
//                    bundle.putString("myTeamName", myName)
//                    findNavController().navigate(R.id.score_fragment, bundle)
//                }
//            }
//        }else{
//            if (sessionManager.getMyScore() <= sessionManager.getCpuScore()){
//                val bundle = Bundle()
//                bundle.putString("opposeTeamName", cpuName)
//                bundle.putString("myTeamName", myName)
//                findNavController().navigate(R.id.score_fragment, bundle)
//            }else{
//                winAlertBox(2)
//            }
//        }
//    }
//
//    //This function is used for cpu button auto click (AI module)
//    private fun autoButtonClick() {
//        Log.e("CPU Button", "Auto Click")
//        try {
//            /*if (!isTimerFinish) {
//                Handler(Looper.myLooper()!!).postDelayed({
//                    selectCpuButton()
//                }, 3000)
//            }*/
//            startCpuProcess()
//        } catch (e: Exception) {
//            Log.d("***** CPU Button", "Auto Click" + e.message)
//        }
//    }
//
//
//    fun startCpuProcess() {
//        isCpuActive = true
//        handler.postDelayed(runnable, 3000) // Start delayed execution
//    }
//
//    fun stopCpuProcess() {
//        isCpuActive = false
//        handler.removeCallbacks(runnable) // Stop execution
//    }
//
//    //This function is used for click action cup button perform (AI module)
//    private fun selectCpuButton() {
//        Log.e("CPU", "Button CLick")
//        if (!isTimerFinish) {
//            val onSelectedScreen = if (userType.equals("USER",true)) {
//                sessionManager.getUserScreenType()
//                isTimerFinish = true
//            } else {
//                sessionManager.getCpuScreenType()
//            }
//            val itemSelect=Random.nextInt(1, 11)
//            Log.d("****** itemSelect Random number :- ", "****** :CPU $itemSelect")
//            Log.d("****** onSelectedScreen number :- ", "****** :- $onSelectedScreen")
//            Log.d("****** LifeLine first :- ", "****** :- ${sessionManager.getLifeLine1()}")
//            Log.d("****** LifeLine first1 :- ", "****** :- ${sessionManager.getLifeLine11()}")
//
//            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                when (onSelectedScreen) {
//                    "5-2-3" -> {
//                        when(itemSelect){
//                            1 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p1Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                }
//
//                            }
//                            2 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p2Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                }
//                            }
//                            3 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p3Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                }
//
//                            }
//                            4 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p4Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                }
//
//                            }
//                            5 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p5Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p5, binding.cpur1p5Name, binding.cpur1p5Number, binding.cpur1p5Id, binding.cpur1p5Txt, binding.cpur1p5Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p5, binding.cpur1p5Name, binding.cpur1p5Number, binding.cpur1p5Id, binding.cpur1p5Txt, binding.cpur1p5Select)
//                                }
//
//                            }
//
//                            6 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//                            }
//
//                            7 -> {
//                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    cpuPlayerClickWork(cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//                                }else{
//                                    autoButtonClick()
//                                }*/
//                                cpuPlayerClickWork(binding.cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//                            }
//
//                            8 -> {
//                                if (binding.cpur3p1Select?.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p1, binding.cpur3p1Name, binding.cpur3p1Number, binding.cpur3p1Id, binding.cpur3p1Txt, binding.cpur3p1Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            9 -> {
//                                if (binding.cpur3p2Select?.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p2, binding.cpur3p2Name, binding.cpur3p2Number, binding.cpur3p2Id, binding.cpur3p2Txt, binding.cpur3p2Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            10 -> {
//                                if (binding.cpur3p3Select?.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p3, binding.cpur3p3Name, binding.cpur3p3Number, binding.cpur3p3Id, binding.cpur3p3Txt, binding.cpur3p3Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            //Shrawan
//                            11 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur0p1Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                }
//
//                            }
//                        }
//                    }  // Done
//
//                    "5-4-1" -> {
//                        when(itemSelect){
//                            1 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p1Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                }
//
//                            }
//                            2 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p2Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                }
//                            }
//                            3 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p3Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                }
//
//                            }
//                            4 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p4Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                }
//
//                            }
//                            5 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p5Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p5, binding.cpur1p5Name, binding.cpur1p5Number, binding.cpur1p5Id, binding.cpur1p5Txt, binding.cpur1p5Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p5, binding.cpur1p5Name, binding.cpur1p5Number, binding.cpur1p5Id, binding.cpur1p5Txt, binding.cpur1p5Select)
//                                }
//
//                            }
//
//                            6 -> {
//                                /* if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                     cpuPlayerClickWork(cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//                                 }else{
//                                     autoButtonClick()
//                                 }*/
//                                cpuPlayerClickWork(binding.cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//                            }
//                            7 -> {
//                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    cpuPlayerClickWork(cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//                                }else{
//                                    autoButtonClick()
//                                }*/
//                                cpuPlayerClickWork(binding.cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//                            }
//                            8 -> {
//                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    cpuPlayerClickWork(cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
//                                }else{
//                                    autoButtonClick()
//                                }*/
//                                cpuPlayerClickWork(binding.cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
//                            }
//                            9 -> {
//                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    cpuPlayerClickWork(cpur2p4, binding.cpur2p4Name, binding.cpur2p4Number, binding.cpur2p4Id, binding.cpur2p4Txt, binding.cpur2p4Select)
//                                }else{
//                                    autoButtonClick()
//                                }*/
//                                cpuPlayerClickWork(binding.cpur2p4, binding.cpur2p4Name, binding.cpur2p4Number, binding.cpur2p4Id, binding.cpur2p4Txt, binding.cpur2p4Select)
//                            }
//
//                            10 -> {
//                                if (binding.cpur3p1Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p1, binding.cpur3p1Name, binding.cpur3p1Number, binding.cpur3p1Id, binding.cpur3p1Txt, binding.cpur3p1Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            //Shrawan
//                            11->{
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur0p1Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                }
//                            }
//                        }
//                    }  // Done
//
//                    "5-3-2" -> {
//                        when(itemSelect){
//                            1 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p1Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                }
//
//                            }
//                            2 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p2Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                }
//                            }
//                            3 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p3Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                }
//
//                            }
//                            4 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p4Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                }
//
//                            }
//                            5 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p5Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p5, binding.cpur1p5Name, binding.cpur1p5Number, binding.cpur1p5Id, binding.cpur1p5Txt, binding.cpur1p5Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p5, binding.cpur1p5Name, binding.cpur1p5Number, binding.cpur1p5Id, binding.cpur1p5Txt, binding.cpur1p5Select)
//                                }
//
//                            }
//
//                            6 -> {
//                                /* if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                     cpuPlayerClickWork(cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//                                 }else{
//                                     autoButtonClick()
//                                 }*/
//                                cpuPlayerClickWork(binding.cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//                            }
//                            7 -> {
//                                /* if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                     cpuPlayerClickWork(cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//                                 }else{
//                                     autoButtonClick()
//                                 }*/
//                                cpuPlayerClickWork(binding.cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//                            }
//                            8 -> {
//                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    cpuPlayerClickWork(cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
//                                }else{
//                                    autoButtonClick()
//                                }*/
//                                cpuPlayerClickWork(binding.cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
//
//                            }
//
//                            9 -> {
//                                if (binding.cpur3p1Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p1, binding.cpur3p1Name, binding.cpur3p1Number, binding.cpur3p1Id, binding.cpur3p1Txt, binding.cpur3p1Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            10 -> {
//                                if (binding.cpur3p2Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p2, binding.cpur3p2Name, binding.cpur3p2Number, binding.cpur3p2Id, binding.cpur3p2Txt, binding.cpur3p2Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            11 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur0p1Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                }
//
//                            }
//                        }
//                    }  // Done
//
//                    "3-5-2" -> {
//                        when(itemSelect){
//                            1 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p1Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                }
//
//                            }
//                            2 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p2Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                }
//                            }
//                            3 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p3Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                }
//
//                            }
//
//                            4 -> {
//                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    cpuPlayerClickWork(cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//                                }else{
//                                    autoButtonClick()
//                                }*/
//                                cpuPlayerClickWork(binding.cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//                            }
//                            5 -> {
//                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    cpuPlayerClickWork(cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//                                }else{
//                                    autoButtonClick()
//                                }*/
//                                cpuPlayerClickWork(binding.cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//                            }
//                            6 -> {
//                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    cpuPlayerClickWork(cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
//                                }else{
//                                    autoButtonClick()
//                                }*/
//                                cpuPlayerClickWork(binding.cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
//                            }
//                            7 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p4, binding.cpur2p4Name, binding.cpur2p4Number, binding.cpur2p4Id, binding.cpur2p4Txt, binding.cpur2p4Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p4, binding.cpur2p4Name, binding.cpur2p4Number, binding.cpur2p4Id, binding.cpur2p4Txt, binding.cpur2p4Select)
//                            }
//                            8 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p5, binding.cpur2p5Name, binding.cpur2p5Number, binding.cpur2p5Id, binding.cpur2p5Txt, binding.cpur2p5Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p5, binding.cpur2p5Name, binding.cpur2p5Number, binding.cpur2p5Id, binding.cpur2p5Txt, binding.cpur2p5Select)
//                            }
//                            9 -> {
//                                if (binding.cpur3p1Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p1, binding.cpur3p1Name, binding.cpur3p1Number, binding.cpur3p1Id, binding.cpur3p1Txt, binding.cpur3p1Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            10 -> {
//                                if (binding.cpur3p2Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p2, binding.cpur3p2Name, binding.cpur3p2Number, binding.cpur3p2Id, binding.cpur3p2Txt, binding.cpur3p2Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            11 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur0p1Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                }
//
//                            }
//                        }
//                    }  // Done
//
//                    "4-5-1" -> {
//                        when(itemSelect){
//                            1 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p1Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                }
//
//                            }
//                            2 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p2Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                }
//                            }
//                            3 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p3Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                }
//
//                            }
//                            4 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p4Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                }
//
//                            }
//
//                            5 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//                            }
//                            6 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//                            }
//                            7 -> {
//
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
////                            }else{
////                                autoButtonClick()
////                            }
//
//                                cpuPlayerClickWork(binding.cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
//                            }
//                            8 -> {
//
//                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    cpuPlayerClickWork(cpur2p4, binding.cpur2p4Name, binding.cpur2p4Number, binding.cpur2p4Id, binding.cpur2p4Txt, binding.cpur2p4Select)
//                                }else{
//                                    autoButtonClick()
//                                }*/
//                                cpuPlayerClickWork(binding.cpur2p4, binding.cpur2p4Name, binding.cpur2p4Number, binding.cpur2p4Id, binding.cpur2p4Txt, binding.cpur2p4Select)
//
//                            }
//                            9 -> {
//                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    cpuPlayerClickWork(cpur2p5, binding.cpur2p5Name, binding.cpur2p5Number, binding.cpur2p5Id, binding.cpur2p5Txt, binding.cpur2p5Select)
//                                }else{
//                                    autoButtonClick()
//                                }*/
//                                cpuPlayerClickWork(binding.cpur2p5, binding.cpur2p5Name, binding.cpur2p5Number, binding.cpur2p5Id, binding.cpur2p5Txt, binding.cpur2p5Select)
//                            }
//                            10 -> {
//                                if (binding.cpur3p1Select?.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p1, binding.cpur3p1Name, binding.cpur3p1Number, binding.cpur3p1Id, binding.cpur3p1Txt, binding.cpur3p1Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            11 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur0p1Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                }
//
//                            }
//                        }
//                    }  // Done
//
//                    "4-4-2" -> {
//                        when(itemSelect){
//                            1 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p1Select?.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                }
//
//                            }
//                            2 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p2Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                }
//                            }
//                            3 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p3Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                }
//
//                            }
//                            4 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p4Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                }
//
//                            }
//                            5 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//
//                            }
//                            6 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//                            }
//                            7 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
//                            }
//                            8 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p4, binding.cpur2p4Name, binding.cpur2p4Number, binding.cpur2p4Id, binding.cpur2p4Txt, binding.cpur2p4Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p4, binding.cpur2p4Name, binding.cpur2p4Number, binding.cpur2p4Id, binding.cpur2p4Txt, binding.cpur2p4Select)
//                            }
//
//                            9 -> {
//                                if (binding.cpur3p1Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p1, binding.cpur3p1Name, binding.cpur3p1Number, binding.cpur3p1Id, binding.cpur3p1Txt, binding.cpur3p1Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            10 -> {
//                                if (binding.cpur3p2Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p2, binding.cpur3p2Name, binding.cpur3p2Number, binding.cpur3p2Id, binding.cpur3p2Txt, binding.cpur3p2Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//
//                            11 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur0p1Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                }
//
//                            }
//                        }
//                    }  // Done
//
//                    "4-3-3" -> {
//                        when(itemSelect){
//                            1 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p1Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                }
//
//                            }
//                            2 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p2Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                }
//                            }
//                            3 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p3Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                }
//
//                            }
//                            4 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p4Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                }
//
//                            }
//
//                            5 -> {
//                                /*if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    cpuPlayerClickWork(cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//                                }else{
//                                    autoButtonClick()
//                                }*/
//                                cpuPlayerClickWork(binding.cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//                            }
//                            6 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//                            }
//                            7 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
//                            }
//
//                            8 -> {
//                                if (binding.cpur3p1Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p1, binding.cpur3p1Name, binding.cpur3p1Number, binding.cpur3p1Id, binding.cpur3p1Txt, binding.cpur3p1Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            9 -> {
//                                if (binding.cpur3p2Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p2, binding.cpur3p2Name, binding.cpur3p2Number, binding.cpur3p2Id, binding.cpur3p2Txt, binding.cpur3p2Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            10 -> {
//                                if (binding.cpur3p3Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p3, binding.cpur3p3Name, binding.cpur3p3Number, binding.cpur3p3Id, binding.cpur3p3Txt, binding.cpur3p3Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            11 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur0p1Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                }
//
//                            }
//                        }
//                    }  // Done
//
//                    "3-4-3" -> {
//                        when(itemSelect){
//                            1 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p1Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                }
//
//                            }
//                            2 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p2Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                }
//                            }
//                            3 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p3Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                }
//
//                            }
//                            4 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//
//                            }
//                            5 -> {
//
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//
//                            }
//                            6 -> {
//
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p3, binding.cpur2p3Name, binding.cpur2p3Number, binding.cpur2p3Id, binding.cpur2p3Txt, binding.cpur2p3Select)
//
//                            }
//                            7 -> {
//
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p4, binding.cpur2p4Name, binding.cpur2p4Number, binding.cpur2p4Id, binding.cpur2p4Txt, binding.cpur2p4Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p4, binding.cpur2p4Name, binding.cpur2p4Number, binding.cpur2p4Id, binding.cpur2p4Txt, binding.cpur2p4Select)
//                            }
//
//                            8 -> {
//                                if (binding.cpur3p1Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p1, binding.cpur3p1Name, binding.cpur3p1Number, binding.cpur3p1Id, binding.cpur3p1Txt, binding.cpur3p1Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            9 -> {
//                                if (binding.cpur3p2Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p2, binding.cpur3p2Name, binding.cpur3p2Number, binding.cpur3p2Id, binding.cpur3p2Txt, binding.cpur3p2Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            10 -> {
//                                if (binding.cpur3p3Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p3, binding.cpur3p3Name, binding.cpur3p3Number, binding.cpur3p3Id, binding.cpur3p3Txt, binding.cpur3p3Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            11 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur0p1Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                }
//
//                            }
//                        }
//                    }  // Done
//
//                    "4-2-4" -> {
//                        when(itemSelect){
//                            1 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p1Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p1, binding.cpur1p1Name, binding.cpur1p1Number, binding.cpur1p1Id, binding.cpur1p1Txt, binding.cpur1p1Select)
//                                }
//
//                            }
//                            2 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p2Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p2, binding.cpur1p2Name, binding.cpur1p2Number, binding.cpur1p2Id, binding.cpur1p2Txt, binding.cpur1p2Select)
//                                }
//                            }
//                            3 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p3Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p3, binding.cpur1p3Name, binding.cpur1p3Number, binding.cpur1p4Id, binding.cpur1p3Txt, binding.cpur1p3Select)
//                                }
//
//                            }
//                            4 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur1p4Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur1p4, binding.cpur1p4Name, binding.cpur1p4Number, binding.cpur1p4Id, binding.cpur1p4Txt, binding.cpur1p4Select)
//                                }
//
//                            }
//
//                            5 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p1, binding.cpur2p1Name, binding.cpur2p1Number, binding.cpur2p1Id, binding.cpur2p1Txt, binding.cpur2p1Select)
//                            }
//                            6 -> {
////                            if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
////                                cpuPlayerClickWork(cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
////                            }else{
////                                autoButtonClick()
////                            }
//                                cpuPlayerClickWork(binding.cpur2p2, binding.cpur2p2Name, binding.cpur2p2Number, binding.cpur2p2Id, binding.cpur2p2Txt, binding.cpur2p2Select)
//                            }
//
//                            7 -> {
//                                if (binding.cpur3p1Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p1, binding.cpur3p1Name, binding.cpur3p1Number, binding.cpur3p1Id, binding.cpur3p1Txt, binding.cpur3p1Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            8 -> {
//                                if (binding.cpur3p2Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p2, binding.cpur3p2Name, binding.cpur3p2Number, binding.cpur3p2Id, binding.cpur3p2Txt, binding.cpur3p2Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            9 -> {
//                                if (binding.cpur3p3Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p3, binding.cpur3p3Name, binding.cpur3p3Number, binding.cpur3p3Id, binding.cpur3p3Txt, binding.cpur3p3Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            10 -> {
//                                if (binding.cpur3p4Select.visibility == View.VISIBLE) {
//                                    cpuPlayerClickWork(binding.cpur3p4, binding.cpur3p4Name, binding.cpur3p4Number, binding.cpur3p4Id, binding.cpur3p4Txt, binding.cpur3p4Select)
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                            11 -> {
//                                if (sessionManager.getLifeLine1() == sessionManager.getLifeLine11()){
//                                    if (binding.cpur0p1Select.visibility == View.VISIBLE) {
//                                        cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                    } else {
//                                        autoButtonClick()
//                                    }
//                                }else{
//                                    cpuPlayerClickWork(binding.cpur0p1, binding.cpur0p1Name, binding.cpur0p1Number, binding.cpur0p1Id, binding.cpur0p1Txt, binding.cpur0p1Select)
//                                }
//
//                            }
//
//                        }
//                    }  // Done
//                    else -> {
//                        Toast.makeText(requireContext(), "Formation is not Exist $onSelectedScreen", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }else{
//                try {
//                    when (onSelectedScreen) {
//                        "5-2-3" -> {
//                            binding.cpur1p1Select.visibility= view!!.visibility
//                            binding.cpur1p2Select.visibility= view!!.visibility
//                            binding.cpur1p3Select.visibility= view!!.visibility
//                            binding.cpur1p4Select.visibility= view!!.visibility
//                            binding.cpur1p5Select.visibility= view!!.visibility
//                            binding.cpur0p1Select.visibility= view!!.visibility //Shrawan
//                            sessionManager.disableLifeLine11(false)
//                            autoButtonClick()
//                        }  // Done
//
//                        "5-4-1" -> {
//                            binding.cpur1p1Select.visibility=view!!.visibility
//                            binding.cpur1p2Select.visibility=view!!.visibility
//                            binding.cpur1p3Select.visibility=view!!.visibility
//                            binding.cpur1p4Select.visibility=view!!.visibility
//                            binding.cpur1p5Select.visibility=view!!.visibility
//                            binding.cpur0p1Select.visibility=view!!.visibility //Shrawan
//                            sessionManager.disableLifeLine11(false)
//                            autoButtonClick()
//                        }  // Done
//
//                        "5-3-2" -> {
//                            binding.cpur1p1Select.visibility=view!!.visibility
//                            binding.cpur1p2Select.visibility=view!!.visibility
//                            binding.cpur1p3Select.visibility=view!!.visibility
//                            binding.cpur1p4Select.visibility=view!!.visibility
//                            binding.cpur1p5Select.visibility=view!!.visibility
//                            binding.cpur0p1Select.visibility=view!!.visibility //Shrawan
//                            sessionManager.disableLifeLine11(false)
//                            autoButtonClick()
//
//                        }  // Done
//
//                        "3-5-2" -> {
//                            binding.cpur1p1Select.visibility=view!!.visibility
//                            binding.cpur1p2Select.visibility=view!!.visibility
//                            binding.cpur1p3Select.visibility=view!!.visibility
//                            binding.cpur0p1Select.visibility=view!!.visibility //Shrawan
//                            sessionManager.disableLifeLine11(false)
//                            autoButtonClick()
//                        }  // Done
//
//                        "4-5-1" -> {
//                            binding.cpur1p1Select.visibility=view!!.visibility
//                            binding.cpur1p2Select.visibility=view!!.visibility
//                            binding.cpur1p3Select.visibility=view!!.visibility
//                            binding.cpur1p4Select.visibility=view!!.visibility
//                            binding.cpur0p1Select.visibility=view!!.visibility //Shrawan
//                            sessionManager.disableLifeLine11(false)
//                            autoButtonClick()
//                        }  // Done
//
//                        "4-4-2" -> {
//                            binding.cpur1p1Select.visibility=view!!.visibility
//                            binding.cpur1p2Select.visibility=view!!.visibility
//                            binding.cpur1p3Select.visibility=view!!.visibility
//                            binding.cpur1p4Select.visibility=view!!.visibility
//                            binding.cpur0p1Select.visibility=view!!.visibility //Shrawan
//                            sessionManager.disableLifeLine11(false)
//                            autoButtonClick()
//                        }  // Done
//
//                        "4-3-3" -> {
//                            binding.cpur1p1Select.visibility=view!!.visibility
//                            binding.cpur1p2Select.visibility=view!!.visibility
//                            binding.cpur1p3Select.visibility=view!!.visibility
//                            binding.cpur1p4Select.visibility=view!!.visibility
//                            binding.cpur0p1Select.visibility=view!!.visibility //Shrawan
//                            sessionManager.disableLifeLine11(false)
//                            autoButtonClick()
//                        }  // Done
//
//                        "3-4-3" -> {
//                            binding.cpur1p1Select.visibility=view!!.visibility
//                            binding.cpur1p2Select.visibility=view!!.visibility
//                            binding.cpur1p3Select.visibility=view!!.visibility
//                            binding.cpur0p1Select.visibility=view!!.visibility //Shrawan
//                            sessionManager.disableLifeLine11(false)
//                            autoButtonClick()
//
//                        }  // Done
//
//                        "4-2-4" -> {
//                            binding.cpur1p1Select.visibility=view!!.visibility
//                            binding.cpur1p2Select.visibility=view!!.visibility
//                            binding.cpur1p3Select.visibility=view!!.visibility
//                            binding.cpur1p4Select.visibility=view!!.visibility
//                            binding.cpur0p1Select.visibility=view!!.visibility //Shrawan
//                            sessionManager.disableLifeLine11(false)
//                            autoButtonClick()
//                        }  // Done
//                        else -> {
//                            Toast.makeText(requireContext(), "Formation is not Exist $onSelectedScreen", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }catch (e:Exception){
//                    Log.d("******","Error :- "+e.message.toString())
//                }
//            }
//
//        }
//    }
//
//    //This function is used for pass ball feature
//    private fun userPassBall() {
//        if (!isTimerFinish) {
////            sessionManager.saveMyPass(1)
//            playerSelectProcessComplete = false
//            selectionPower = true
//            isGoalClick = false
//            if (userType.equals("USER",true)) {
//                selectPlayPlayer(sessionManager.getMySelectedTeamPlayerNum())
//            } else {
//                autoButtonClick()
//            }
//        }
//    }
//
//    //This function is used for shoot ball feature
//    private fun shootBall() {
//        if (!isTimerFinish) {
//            getTestUserShoot()
//            val playerPower: String = if (userType.equals("USER",true)) {
//                allCpuPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].type.uppercase()
//            } else {
//
//                allUserPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].type.uppercase()
//            }
//            if (userType.equals("USER",true)) {
//                cpuDbHelper.updatePlayerUse(allCpuPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].id.toLong(), "true")
//
//                cpuDbHelper.updatePlayerAnswer(allCpuPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].id.toLong(), "true")
//
//
//                Log.d("@@@Error ", "myPass move screen$myPass")
//                Log.d("@@@Error ", "cpuPass move screen$cpuPass")
//
//
//                val bundle = Bundle()
//                bundle.putString("userType", "USER")
//                bundle.putInt("selected_player_num", selectedPlayerNum!!.text.toString().toInt())
//                val size = 0/*if (playerPower == "PURPLE") {
//                    1
//                } else {
//                    0
//                }*/
//                for (data in allUserPlayer) {
//                    if (data.id.toInt() == selectedPlayerNum!!.text.toString().toInt()) {
//                        if (data.designation.uppercase() == "DF") {
//                            bundle.putInt("size", size + 2 + myPass)
//                        } else {
//                            if (data.designation.uppercase() == "MF") {
//                                bundle.putInt("size", size + 3 + myPass)
//                            } else {
//                                bundle.putInt("size", size + 4 + myPass)
//                            }
//                        }
//                    }
//                }
//                findNavController().navigate(R.id.shoot_Screen, bundle)
//                Log.e("Work", "Shoot Button")
//            } else {
//                myPlayerDbHelper.updatePlayerUse(allUserPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].id.toLong(), "true")
//                myPlayerDbHelper.updatePlayerAnswer(allUserPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].id.toLong(), "true")
//                var size = 0/*if (playerPower == "PURPLE") {
//                    1
//                } else {
//                    0
//                }*/
//
//                Log.d("@@@Error ", "myPass move screen$myPass")
//                Log.d("@@@Error ", "cpuPass move screen$cpuPass")
//
//                val bundle = Bundle()
//                bundle.putString("userType", "USER")
//                for (data in allCpuPlayer) {
//                    if (data.id.toInt() == selectedPlayerNum!!.text.toString().toInt()) {
//                        size = if (data.designation.uppercase() == "DF") {
//                            size + 2 + cpuPass
//                        } else {
//                            if (data.designation.uppercase() == "MF") {
//                                size + 3 + cpuPass
//                            } else {
//                                size + 4 + cpuPass
//                            }
//                        }
//                    }
//                }
//                val num = setGames.getRandomNumber(size)
//                Log.e("Gold Kick", num.toString())
//                Log.d("******", "play screen  :=" + num)
//                bundle.putInt("select_box", num)
//                bundle.putInt("size", size)
//                bundle.putInt("selected_player_num", selectedPlayerNum!!.text.toString().toInt())
//                Log.e("Shoot to Kick", bundle.toString())
//                findNavController().navigate(R.id.goal_keeper_Screen, bundle)
//            }
//        }
//    }
//
//    //This function is used for on select player action by user
//    private fun playerClickWork(playerBox: ConstraintLayout, playerName: TextView, playerNum: TextView, playerId: TextView, playerTxtBox: LinearLayout, selectCircle: View) {
//        if (!isTimerFinish) {
//            if (!playerSelectProcessComplete) {
//                if (playerBox.visibility == View.VISIBLE) {
//                    if (totalPlayerNameShow == 0) {
//                        val bundle = Bundle()
//                        bundle.putString("Name", playerName.text.toString())
//                        bundle.putString("userType", userType)
//                        bundle.putString("Num", playerNum.text.toString())
//                        bundle.putString("id", playerId.text.toString())
//                        selectedPlayerNum?.text = playerId.text.toString()
//                        Log.e("Send Detail of Quiz", userType + " " + playerName.text.toString() + " " + playerNum.text.toString())
//                        findNavController().navigate(R.id.player_name_guess, bundle)
//                        Log.e("Work", "Condition1")
//                    } else {
//                        if (selectionPower) {
//                            if (selectCircle.visibility == View.VISIBLE) {
//                                if (playerTxtBox.visibility == View.VISIBLE) {
//                                    selectionPower = false
//                                    playerSelectProcessComplete = true
//                                    selectedPlayerNum = playerId
//                                    selectionView = selectCircle
//                                    binding.rootButton.visibility = View.VISIBLE
//                                    Log.e("Work", "Condition3")
//                                } else {
//                                    val bundle = Bundle()
//                                    selectionPower = false
//                                    playerSelectProcessComplete = true
//                                    bundle.putString("Name", playerName.text.toString())
//                                    bundle.putString("userType", userType)
//                                    bundle.putString("Num", playerNum.text.toString())
//                                    bundle.putString("id", playerId.text.toString())
//                                    selectedPlayerNum = playerId
////                                    if (totalPlayerNameShow == 1) {
////                                        sessionManager.saveMyPass(1)
////                                    }
//                                    //Shrawan
//                                    Log.e("@@@@@@@@@@@", "${playerBox.id} ${binding.r0p1.id}")
//                                    if (playerBox.id==binding.r0p1.id){
//                                        isGoalClick = true
//                                    }else{
//                                        isGoalClick = false
//                                    }
//                                    bundle.putBoolean("isGoalClick",isGoalClick) //Shrawan
//                                    findNavController().navigate(R.id.player_name_guess, bundle)
//                                    Log.e("Send Detail of Quiz", userType + " " + playerName.text.toString() + " " + playerNum.text.toString())
//                                    Log.e("Work", "Condition4")
//                                }
//                            } else {
//                                Log.e("Work", "Player out of Range")
//                            }
//                        } else {
//                            if (playerTxtBox.visibility == View.VISIBLE) {
//                                selectionPower = true
//                                showPlayerReachPowerUser(selectCircle, playerId)
//                                Log.e("Work", "Condition2")
//                            } else {
//                                Toast.makeText(requireActivity(), "Player are not Locked!", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    //This function is used for on select player action by cpu
//    private fun cpuPlayerClickWork(playerBox: ConstraintLayout?, playerName: TextView?, playerNum: TextView?, playerId: TextView?, playerTxtBox: LinearLayout?, selectCircle: View?) {
//        try {
//            if (!isTimerFinish) {
//                if (!playerSelectProcessComplete) {
//                    if (playerBox?.visibility == View.VISIBLE) {
//                        if (cpuPlayerNameShow == 0) {
//                            val bundle = Bundle()
//                            bundle.putString("Name", playerName?.text.toString())
//                            bundle.putString("userType", userType)
//                            bundle.putString("Num", playerNum?.text.toString())
//                            bundle.putString("id", playerId?.text.toString())
//                            selectedPlayerNum = playerId
//                            Log.d("******", "selectedPlayerNum :- " + playerId?.text.toString())
//                            Log.e("Send Detail of Quiz", userType + " " + playerName?.text.toString() + " " + playerNum?.text.toString())
//                            Log.e("Work", "Condition1")
//                            findNavController().navigate(R.id.player_name_guess, bundle)
//
//
//                        } else {
//                            if (selectionPower) {
//                                if (selectCircle!!.visibility == View.VISIBLE) {
//                                    if (playerTxtBox!!.visibility == View.VISIBLE) {
//                                        selectionPower = false
//                                        playerSelectProcessComplete = true
//                                        selectedPlayerNum = playerId
//                                        selectionView = selectCircle
//                                        Log.d("******", "selectedPlayerNum :- " + playerId?.text.toString())
////                                        selectCircle.setBackgroundResource(R.drawable.selected_circle)
//                                        binding.rootButton.visibility = View.GONE
//                                        autoButtonSelectPassShoot()
//                                        Log.e("Work", "Condition3")
//                                    } else {
//                                        val bundle = Bundle()
//                                        selectionPower = false
//                                        playerSelectProcessComplete = true
//                                        bundle.putString("Name", playerName?.text.toString())
//                                        bundle.putString("userType", userType)
//                                        bundle.putString("Num", playerNum?.text.toString())
//                                        bundle.putString("id", playerId?.text.toString())
////                                        if (cpuPlayerNameShow == 1) {
////                                            sessionManager.saveCpuPass(1)
////                                        }
//                                        Log.e("Send Detail of Quiz", userType + " " + playerName?.text.toString() + " " + playerNum?.text.toString())
//                                        Log.d("******", "selectedPlayerNum :- " + playerId?.text.toString())
//                                        Log.e("Work", "Condition4")
//                                        findNavController().navigate(R.id.player_name_guess, bundle)
//                                    }
//                                } else {
//                                    if (cpuPlayerNameShow!=0){
//                                        val randomNumber = Random.nextInt(2, 10)
//                                        // Check if the number is even or odd
//                                        val result = if (randomNumber % 2 == 0) {
//                                            "even"
//                                        } else {
//                                            "odd"
//                                        }
//                                        if (result.equals("even",true)){
//                                            autoButtonSelectPassShoot()
//                                            Log.e("Shoot ", "player Shoot range")
//                                        }else{
//                                            autoButtonClick()
//                                            Log.e("Work", "player out off range")
//                                        }
//                                    }else{
//                                        autoButtonClick()
//                                        Log.e("Work", "player out off range")
//                                    }
//                                }
//                            } else {
//                                if (playerTxtBox?.visibility == View.VISIBLE) {
//                                    selectionPower = true
//                                    showPlayerReachPowerUser(selectCircle!!, playerId!!)
//                                    autoButtonClick()
//                                    Log.e("Work", "Condition2")
//                                } else {
//                                    autoButtonClick()
//                                }
//                            }
//                        }
//                    } else {
//                        Log.e("PLayer Button", "Not Visible")
//                        autoButtonClick()
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            Log.d("****", "error := " + e.message)
//        }
//    }
//
//    //This function is used for auto  select pass or shoot (AI Module) in cpu term
//    private fun autoButtonSelectPassShoot() {
//        if (!isTimerFinish) {
//            if (status){
//                when (Random.nextInt(1, 3)) {
//                    1 -> {
//                        /*shootBall*/
//                        //Shrawan
//                        if (!isGoalClick) {
//                            shootBall()
//                        }else{
//                            isGoalClick = false
//                        }
//                    }
//                    else -> {
//                        userPassBall()
//                    }
//                }
//            }
//
//        }
//    }
//
//    //This function is used for set ground formation
//    @SuppressLint("SetTextI18n")
//    private fun setScreens() {
//        allCpuPlayer = cpuDbHelper.getAllPlayers()
//        allUserPlayer = myPlayerDbHelper.getAllPlayers()
//        Log.e("All Cpu Player", allCpuPlayer.toString())
//        Log.e("All User Player", allUserPlayer.toString())
//        Log.e("All name ", sessionManager.getName().toString())
//        if (sessionManager.getMatchType() == "worldcup") {
//            teamDbHelper = TeamDatabaseHelper(requireContext())
//            val allTeam = teamDbHelper.getAllTeams()
//            for (data in allTeam) {
//                if (data.teamID == 1) {
//                    binding.userName.text = data.captainName
//                }
//                /*if (data.teamID == sessionManager.getTeamDetails()) {
//                    oppose_team_player_name.text = data.captainName
//                }*/
//                if(sessionManager.getGameNumber() <= 3)  { //for first 3 match
//                    binding.opposeTeamPlayerName.text = data.captainName
//                }else if(sessionManager.getGameNumber() == 4){ // for 4th match
//                    binding.opposeTeamPlayerName.text = "Asst. Manager"
//                }else if(sessionManager.getGameNumber() == 5){ // for 5th match
//                    binding.opposeTeamPlayerName.text = "Lizard mascot"
//                }
//            }
//
//        } else {
//            binding.userName.text = sessionManager.getName()?.split(" ")?.drop(1)?.joinToString(" ")
//            binding.opposeTeamPlayerName.text = "CPU"
//        }
//
//        binding.cpuScoreTv.text = sessionManager.getCpuScore().toString()
//        binding.userGoalTv.text = sessionManager.getMyScore().toString()
//        if (!isTimerFinish) {
//            val screen = setGames.setScreen(sessionManager.getUserScreenType())
//            userR1 = screen.r1
//            userR2 = screen.r2
//            userR3 = screen.r3
//            val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
//            cpuR1 = cpuScreen.r1
//            cpuR2 = cpuScreen.r2
//            cpuR3 = cpuScreen.r3
//            setRowOfScreens()
//        }
//    }
//
//    //This function is used for set the player in row
//    private fun setRowOfScreens() {
//        if (!isTimerFinish) {
//            when (userR1) {
//                4 -> {
//                    binding.r1.weightSum = 4f
//                    binding.r1p5.visibility = View.GONE
//                }
//                3 -> {
//                    binding.r1.weightSum = 3f
//                    binding.r1p5.visibility = View.GONE
//                    binding.r1p4.visibility = View.GONE
//                }
//                2 -> {
//                    binding.r1.weightSum = 2f
//                    binding.r1p5.visibility = View.GONE
//                    binding.r1p4.visibility = View.GONE
//                    binding.r1p3.visibility = View.GONE
//                }
//                1 -> {
//                    binding.r1.weightSum = 1f
//                    binding.r1p5.visibility = View.GONE
//                    binding.r1p4.visibility = View.GONE
//                    binding.r1p3.visibility = View.GONE
//                    binding.r1p2.visibility = View.GONE
//                }
//            }
//            when (userR2) {
//                4 -> {
//                    binding.r2.weightSum = 4f
//                    binding.r2p5.visibility = View.GONE
//                }
//                3 -> {
//                    binding.r2.weightSum = 3f
//                    binding.r2p5.visibility = View.GONE
//                    binding.r2p4.visibility = View.GONE
//                }
//                2 -> {
//                    binding.r2.weightSum = 2f
//                    binding.r2p5.visibility = View.GONE
//                    binding.r2p4.visibility = View.GONE
//                    binding.r2p3.visibility = View.GONE
//                }
//                1 -> {
//                    binding.r2.weightSum = 1f
//                    binding.r2p5.visibility = View.GONE
//                    binding.r2p4.visibility = View.GONE
//                    binding.r2p3.visibility = View.GONE
//                    binding.r2p2.visibility = View.GONE
//                }
//            }
//            when (userR3) {
//                4 -> {
//                    binding.r3.weightSum = 4f
//                    binding.r3p5.visibility = View.GONE
//                }
//                3 -> {
//                    binding.r3.weightSum = 3f
//                    binding.r3p5.visibility = View.GONE
//                    binding.r3p4.visibility = View.GONE
//                }
//                2 -> {
//                    binding.r3.weightSum = 2f
//                    binding.r3p5.visibility = View.GONE
//                    binding.r3p4.visibility = View.GONE
//                    binding.r3p3.visibility = View.GONE
//                }
//                1 -> {
//                    binding.r3.weightSum = 1f
//                    binding.r3p5.visibility = View.GONE
//                    binding.r3p4.visibility = View.GONE
//                    binding.r3p3.visibility = View.GONE
//                    binding.r3p2.visibility = View.GONE
//                }
//            }
//            when (cpuR1) {
//                4 -> {
//                    binding.cpur1.weightSum = 4f
//                    binding.cpur1p5.visibility = View.GONE
//                }
//                3 -> {
//                    binding.cpur1.weightSum = 3f
//                    binding.cpur1p5.visibility = View.GONE
//                    binding.cpur1p4.visibility = View.GONE
//                }
//                2 -> {
//                    binding.cpur1.weightSum = 2f
//                    binding.cpur1p5.visibility = View.GONE
//                    binding.cpur1p4.visibility = View.GONE
//                    binding.cpur1p3.visibility = View.GONE
//                }
//                1 -> {
//                    binding.cpur1.weightSum = 1f
//                    binding.cpur1p5.visibility = View.GONE
//                    binding.cpur1p4.visibility = View.GONE
//                    binding.cpur1p3.visibility = View.GONE
//                    binding.cpur1p2.visibility = View.GONE
//                }
//            }
//            when (cpuR2) {
//                4 -> {
//                    binding.cpur2.weightSum = 4f
//                    binding.cpur2p5.visibility = View.GONE
//                }
//                3 -> {
//                    binding.cpur2.weightSum = 3f
//                    binding.cpur2p5.visibility = View.GONE
//                    binding.cpur2p4.visibility = View.GONE
//                }
//                2 -> {
//                    binding.cpur2.weightSum = 2f
//                    binding.cpur2p5.visibility = View.GONE
//                    binding.cpur2p4.visibility = View.GONE
//                    binding.cpur2p3.visibility = View.GONE
//                }
//                1 -> {
//                    binding.cpur2.weightSum = 1f
//                    binding.cpur2p5.visibility = View.GONE
//                    binding.cpur2p4.visibility = View.GONE
//                    binding.cpur2p3.visibility = View.GONE
//                    binding.cpur2p2.visibility = View.GONE
//                }
//            }
//            when (cpuR3) {
//                4 -> {
//                    binding.cpur3.weightSum = 4f
//                    binding.cpur3p5.visibility = View.GONE
//                }
//                3 -> {
//                    binding.cpur3.weightSum = 3f
//                    binding.cpur3p5.visibility = View.GONE
//                    binding.cpur3p4.visibility = View.GONE
//                }
//                2 -> {
//                    binding.cpur3.weightSum = 2f
//                    binding.cpur3p5.visibility = View.GONE
//                    binding.cpur3p4.visibility = View.GONE
//                    binding.cpur3p3.visibility = View.GONE
//                }
//                1 -> {
//                    binding.cpur3.weightSum = 1f
//                    binding.cpur3p5.visibility = View.GONE
//                    binding.cpur3p4.visibility = View.GONE
//                    binding.cpur3p3.visibility = View.GONE
//                    binding.cpur3p2.visibility = View.GONE
//                }
//            }
//            hideAllPlayerName()
//        }
//    }
//
//    //This function is used for hide and unhidden player name according to gusse name
//    private fun hideAllPlayerName() {
//        if (!isTimerFinish) {
//            binding.r1p1Txt.visibility = View.GONE
//            binding.r1p2Txt.visibility = View.GONE
//            binding.r1p3Txt.visibility = View.GONE
//            binding.r1p4Txt.visibility = View.GONE
//            binding.r1p5Txt.visibility = View.GONE
//
//            binding.r2p1Txt.visibility = View.GONE
//            binding.r2p2Txt.visibility = View.GONE
//            binding.r2p3Txt.visibility = View.GONE
//            binding.r2p4Txt.visibility = View.GONE
//            binding.r2p5Txt.visibility = View.GONE
//
//            binding.r3p1Txt.visibility = View.GONE
//            binding.r3p2Txt.visibility = View.GONE
//            binding.r3p3Txt.visibility = View.GONE
//            binding.r3p4Txt.visibility = View.GONE
//            binding.r3p5Txt.visibility = View.GONE
//            binding.r0p1Txt.visibility = View.GONE //Shrawan
//
//            binding.cpur1p1Txt.visibility = View.GONE
//            binding.cpur1p2Txt.visibility = View.GONE
//            binding.cpur1p3Txt.visibility = View.GONE
//            binding.cpur1p4Txt.visibility = View.GONE
//            binding.cpur1p5Txt.visibility = View.GONE
//
//            binding.cpur2p1Txt.visibility = View.GONE
//            binding.cpur2p2Txt.visibility = View.GONE
//            binding.cpur2p3Txt.visibility = View.GONE
//            binding.cpur2p4Txt.visibility = View.GONE
//            binding.cpur2p5Txt.visibility = View.GONE
//
//            binding.cpur3p1Txt.visibility = View.GONE
//            binding.cpur3p2Txt.visibility = View.GONE
//            binding.cpur3p3Txt.visibility = View.GONE
//            binding.cpur3p4Txt.visibility = View.GONE
//            binding.cpur3p5Txt.visibility = View.GONE
//            binding.cpur0p1Txt.visibility = View.GONE //Shrawan
//
//            hideAllSelectCircle()
//        }
//    }
//
//
//    //This function is used for hide all select circle
//    private fun hideAllSelectCircle() {
//        if (!isTimerFinish) {
//            setImageCircleCPUAndUser("hideImage")
//            setUserScreenPlayerNameDtl()
//        }
//    }
//    // This function call for set circle background img and hide the all image
//    private fun setImageCircleCPUAndUser(type:String){
//
//        binding.r1p1Select.visibility = View.GONE
//        binding.r1p2Select.visibility = View.GONE
//        binding.r1p3Select.visibility = View.GONE
//        binding.r1p4Select.visibility = View.GONE
//        binding.r1p5Select.visibility = View.GONE
//
//        binding.r2p1Select.visibility = View.GONE
//        binding.r2p2Select.visibility = View.GONE
//        binding.r2p3Select.visibility = View.GONE
//        binding.r2p4Select.visibility = View.GONE
//        binding.r2p5Select.visibility = View.GONE
//
//        binding.r3p1Select.visibility = View.GONE
//        binding.r3p2Select.visibility = View.GONE
//        binding.r3p3Select.visibility = View.GONE
//        binding.r3p4Select.visibility = View.GONE
//        binding.r3p5Select.visibility = View.GONE
//        binding.r0p1Select.visibility = View.GONE //Shrawan
//
//        binding. cpur1p1Select.visibility = View.GONE
//        binding.cpur1p2Select.visibility = View.GONE
//        binding.cpur1p3Select.visibility = View.GONE
//        binding.cpur1p4Select.visibility = View.GONE
//        binding.cpur1p5Select.visibility = View.GONE
//
//        binding.cpur2p1Select.visibility = View.GONE
//        binding.cpur2p2Select.visibility = View.GONE
//        binding.cpur2p3Select.visibility = View.GONE
//        binding.cpur2p4Select.visibility = View.GONE
//        binding.cpur2p5Select.visibility = View.GONE
//
//        binding.cpur3p1Select.visibility = View.GONE
//        binding.cpur3p2Select.visibility = View.GONE
//        binding.cpur3p3Select.visibility = View.GONE
//        binding.cpur3p4Select.visibility = View.GONE
//        binding.cpur3p5Select.visibility = View.GONE
//        binding.cpur0p1Select.visibility = View.GONE //Shrawan
//
//        if (type.equals("showImage",true)){
//            binding.r1p1Select.setBackgroundResource(R.drawable.select_circle)
//            binding.r1p2Select.setBackgroundResource(R.drawable.select_circle)
//            binding.r1p3Select.setBackgroundResource(R.drawable.select_circle)
//            binding.r1p4Select.setBackgroundResource(R.drawable.select_circle)
//            binding.r1p5Select.setBackgroundResource(R.drawable.select_circle)
//
//            binding.r2p1Select.setBackgroundResource(R.drawable.select_circle)
//            binding.r2p2Select.setBackgroundResource(R.drawable.select_circle)
//            binding.r2p3Select.setBackgroundResource(R.drawable.select_circle)
//            binding.r2p4Select.setBackgroundResource(R.drawable.select_circle)
//            binding.r2p5Select.setBackgroundResource(R.drawable.select_circle)
//
//            binding.r3p1Select.setBackgroundResource(R.drawable.select_circle)
//            binding.r3p2Select.setBackgroundResource(R.drawable.select_circle)
//            binding.r3p3Select.setBackgroundResource(R.drawable.select_circle)
//            binding.r3p4Select.setBackgroundResource(R.drawable.select_circle)
//            binding.r3p5Select.setBackgroundResource(R.drawable.select_circle)
//
//            binding.r0p1Select.setBackgroundResource(R.drawable.select_circle) //Shrawan
//
//            binding.cpur1p1Select.setBackgroundResource(R.drawable.select_circle)
//            binding.cpur1p2Select.setBackgroundResource(R.drawable.select_circle)
//            binding.cpur1p3Select.setBackgroundResource(R.drawable.select_circle)
//            binding.cpur1p4Select.setBackgroundResource(R.drawable.select_circle)
//            binding.cpur1p5Select.setBackgroundResource(R.drawable.select_circle)
//
//            binding.cpur2p1Select.setBackgroundResource(R.drawable.select_circle)
//            binding.cpur2p2Select.setBackgroundResource(R.drawable.select_circle)
//            binding.cpur2p3Select.setBackgroundResource(R.drawable.select_circle)
//            binding.cpur2p4Select.setBackgroundResource(R.drawable.select_circle)
//            binding.cpur2p5Select.setBackgroundResource(R.drawable.select_circle)
//
//            binding.cpur3p1Select.setBackgroundResource(R.drawable.select_circle)
//            binding.cpur3p2Select.setBackgroundResource(R.drawable.select_circle)
//            binding.cpur3p3Select.setBackgroundResource(R.drawable.select_circle)
//            binding.cpur3p4Select.setBackgroundResource(R.drawable.select_circle)
//            binding.cpur3p5Select.setBackgroundResource(R.drawable.select_circle)
//
//            binding.cpur0p1Select.setBackgroundResource(R.drawable.select_circle) //Shrawan
//
//        }
//    }
//    //This function is used for userSelect
//    private fun showPlayerReachPowerUser(player: View, playerId: TextView) {
//        if (!isTimerFinish) {
//            setImageCircleCPUAndUser("showImage")
//            val onSelectedScreen = if (userType.equals("USER",true)) {
//                sessionManager.getUserScreenType()
//            } else {
//                sessionManager.getCpuScreenType()
//            }
//            val playerPower: String = if (userType.equals("USER",true)) {
//                allCpuPlayer[playerId.text.toString().toInt() - 1].type.uppercase()
//            } else {
//                allUserPlayer[playerId.text.toString().toInt() - 1].type.uppercase()
//            }
//            Log.d("@@@@@@@@@@@", "onSelectedScreen :- " + onSelectedScreen)
//            Log.d("@@@@@@@@@@@", "playerPower :- " + playerId.text.toString())
//            Log.d("@@@@@@@@@@@", "player :- " + player)
//            when (onSelectedScreen) {
//                "5-2-3" -> {
//                    when (player) {
//
//                        // User
//                        // Row 1
//                        binding.r3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r3p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//
//                        // Row 2
//                        binding.r2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.r2p2Select -> {
//
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // Row 3
//                        binding.r1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p1Select.visibility = View.VISIBLE
//                                }
//
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding. r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding. r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.r1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r1p5Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p5Select.visibility = View.VISIBLE
//                                }
//
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p5Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//
//                        //Shrawan
//                        binding.r0p1Select -> {
//                            Log.d("@@@@@@@@@@@", "player :- ${player.id}  ${binding.r0p1Select.id}")
//                            if (playerPower.equals("RED", true)) {
//                                Log.d("@@@@@@@@@@@", "player :- ${player.id}  ${binding.r0p1Select.id}  $playerPower")
//                                playerUserCpu5_2_3()
//                            }
//                            else {
//                                Log.d("@@@@@@@@@@@", "player :- ${player.id}  ${binding.r0p1Select.id}")
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding. r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding. r1p5Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p5Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // CPU
//                        // Row 1
//                        binding.cpur3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur3p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                binding.cpur3p2Select.visibility = View.VISIBLE
//                                binding.cpur2p2Select.visibility = View.VISIBLE
//
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // Row 2
//                        binding.cpur2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // Row 3
//                        binding.cpur1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur1p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p5Select.visibility = View.VISIBLE
//                                }
//
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur1p5Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            } else {
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//
//                        //Shrawan
//                        binding.cpur0p1Select -> {
//                            Log.d("*****", "player :- " + player)
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_2_3()
//                            }
//                            else {
//                                Log.d("*****", "player :- " + player)
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur1p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p5Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//
//
//                    }
//
//                } // Done
//
//                "5-4-1" -> {
//                    when (player) {
//                        // User
//                        // Row 1
//                        binding.r3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//
//                        // Row 2
//                        binding.r2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding. r1p1Select.visibility = View.VISIBLE
//                                } else {
//                                    binding. r1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.r2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding. r1p3Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding. r3p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//
//                        }
//                        binding.r2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding. r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//
//                        }
//                        binding.r2p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                } else {
//                                    binding. r1p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r1p5Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p5Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding. r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//
//                        // Row 3
//                        binding.r1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding. r1p2Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p1Select.visibility = View.VISIBLE
//                                }
//
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//                        binding.r1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding. r1p3Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r1p5Select.visibility = View.VISIBLE
//                                } else {
//                                    binding. r1p5Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding. r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p4Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p5Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding. r1p4Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding. r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p4Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding. r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.r0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding. r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding. r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding. r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding. r1p5Select.visibility = View.GONE
//                                } else {
//                                    binding. r1p5Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding. r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding. r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//
//                        // CPU
//                        // Row 1
//                        binding.cpur3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//
//                        // Row 2
//                        binding.cpur2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//
//                        }
//                        binding.cpur2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//
//                        }
//                        binding.cpur2p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur1p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p5Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//
//                        // Row 3
//                        binding.cpur1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//                        binding.cpur1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur1p5Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.cpur1p5Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p5Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            } else {
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.cpur0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_4_1()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur1p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p5Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                    }
//                } // Done
//                "5-3-2" -> {
//                    when (player) {
//                        // User
//                        // Row 3
//                        binding. r3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding. r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding. r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // Row 2
//
//                        binding.r2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding. r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding. r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding. r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding. r3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding. r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding. r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding. r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.r2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r1p5Select.visibility = View.GONE
//                                } else {
//                                    binding. r1p5Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding. r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // Row 1
//                        binding.r1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding. r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding. r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding. r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.r1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding. r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.r1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r1p5Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p5Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding. r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding. r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p5Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.r0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r1p5Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p5Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//
//
//                        // CPU
//                        // Row 3
//                        binding.cpur3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // Row 2
//                        binding.cpur2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur1p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p5Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // Row 1
//                        binding.cpur1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur1p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p5Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p5Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.cpur0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu5_3_2()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur1p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p5Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//
//                    }
//                } // Done
//
//                "3-5-2" -> {
//                    when (player) {
//
//                        // User
//                        // Row 1
//
//                        binding.r3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        binding.r3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//
//                        // Row 2
//                        binding.r2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        binding.r2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding. r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        binding.r2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding. r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p2Select.visibility = View.VISIBLE
//                                }
//
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//
//                        binding.r2p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p5Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p5Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//
//                        binding.r2p5Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding. r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p4Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // Row 3
//                        binding.r1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        binding.r1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding. r1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding. r1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding. r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding. r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//
//                        binding.r1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p5Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p5Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//
//                        //Shrawan
//                        binding.r0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding. r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p5Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p5Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//
//
//
//                        // CPU
//
//                        // Row 1
//
//                        binding.cpur3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        binding.cpur3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//
//                        // Row 2
//                        binding.cpur2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        binding.cpur2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        binding.cpur2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//
//                        binding.cpur2p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p5Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//
//                        binding.cpur2p5Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // Row 3
//                        binding.cpur1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        binding.cpur1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//
//                        binding.cpur1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p5Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//
//                        //Shrawan
//                        binding.cpur0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_5_2()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p5Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//
//                    }
//                } // Done
//
//
//                "4-5-1" -> {
//                    when (player) {
//                        // User
//                        // Row 1
//                        binding.r3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//
//                            }
//
//                        }
//                        // Row 2
//                        binding.r2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.r2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r2p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding. r1p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding. r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r2p5Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p5Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding. r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.r2p5Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//                        // Row 3
//                        binding.r1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.r1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding. r1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding. r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding. r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding. r0p1Select.visibility = View.VISIBLE
//                                }
//
//
//                            }
//
//                        }
//                        binding.r1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r2p5Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p5Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.r0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r2p5Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p5Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//
//                        // CPU
//                        // Row 1
//                        binding.cpur3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//
//                            }
//
//                        }
//                        // Row 2
//                        binding.cpur2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur2p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur2p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p5Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur2p5Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//                        // Row 3
//                        binding.cpur1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//
//
//                            }
//
//                        }
//                        binding.cpur1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur2p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p5Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.cpur0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_5_1()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur2p5Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p5Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//                    }
//                } // Done
//                "4-4-2" -> {
//                    when (player) {
//                        // User
//                        // Row 1
//                        binding.r3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding. r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//
//                        }
//                        // Row 2
//                        binding.r2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding. r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//                        binding.r2p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 3
//                        binding.r1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//
//                        }
//                        binding.r1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding. r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding. r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.r0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding. r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // CPU
//                        // Row 1
//                        binding.cpur3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//
//                        }
//                        // Row 2
//                        binding.cpur2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//                        binding.cpur2p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 3
//                        binding.cpur1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//
//                        }
//                        binding.cpur1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.cpur0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_4_2()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                    }
//                } // Done
//
//                "4-3-3" -> {
//                    when (player) {
//                        // User
//                        // Row 3
//                        binding.r3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.r3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r3p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 2
//                        binding.r2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 1
//                        binding.r1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.r1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.r0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // CPU
//                        // Row 3
//                        binding.cpur3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur3p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 2
//                        binding.cpur2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 1
//                        binding.cpur1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.cpur0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_3_3()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//
//                    }
//                } // Done
//                "3-4-3" -> {
//                    when (player) {
//                        // User
//                        // Row 3
//                        binding.r3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//                        binding.r3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r3p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//
//                            }
//
//                        }
//                        // Row 2
//                        binding.r2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//                        binding.r2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r2p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 1
//                        binding.r1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.r0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p4Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // Cpu
//                        // Row 3
//                        binding.cpur3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//                        binding.cpur3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur3p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 2
//                        binding.cpur2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//
//                            }
//                        }
//                        binding.cpur2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur2p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur2p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 1
//                        binding.cpur1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//                        binding.cpur1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//
//                        }
//
//                        //Shrawan
//                        binding.cpur0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu3_4_3()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur2p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p4Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                    }
//                } // Done
//                "4-2-4" -> {
//                    when (player) {
//                        // User
//                        // Row 1
//                        binding.r3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[6].answer.equals("true", true)) {
//                                    binding.r3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r3p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[9].answer.equals("true", true)) {
//                                    binding.r3p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p4Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r3p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 2
//                        binding.r2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[7].answer.equals("true", true)) {
//                                    binding.r3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[8].answer.equals("true", true)) {
//                                    binding.r3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 3
//                        binding.r1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.r1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.r0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.r0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.r1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.r1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.r1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.r1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.r1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.r2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.r2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.r2p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        // Cpu
//                        // Row 1
//                        binding.cpur3p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur3p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[6].answer.equals("true", true)) {
//                                    binding.cpur3p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur3p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[9].answer.equals("true", true)) {
//                                    binding.cpur3p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p4Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur3p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 2
//                        binding.cpur2p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[7].answer.equals("true", true)) {
//                                    binding.cpur3p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur2p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[8].answer.equals("true", true)) {
//                                    binding.cpur3p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur3p3Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        // Row 3
//                        binding.cpur1p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p2Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allUserPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p3Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allUserPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allUserPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//                        binding.cpur1p4Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            } else {
//                                if (allUserPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                //Shrawan
//                                if (allCpuPlayer[10].answer.equals("true", true)) {
//                                    binding.cpur0p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur0p1Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                        //Shrawan
//                        binding.cpur0p1Select -> {
//                            if (playerPower.equals("RED", true)) {
//                                playerUserCpu4_2_4()
//                            }
//                            else {
//                                if (allCpuPlayer[0].answer.equals("true", true)) {
//                                    binding.cpur1p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p1Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[1].answer.equals("true", true)) {
//                                    binding.cpur1p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p2Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[2].answer.equals("true", true)) {
//                                    binding.cpur1p3Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p3Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[3].answer.equals("true", true)) {
//                                    binding.cpur1p4Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur1p4Select.visibility = View.VISIBLE
//                                }
//                                if (allCpuPlayer[4].answer.equals("true", true)) {
//                                    binding.cpur2p1Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p1Select.visibility = View.VISIBLE
//                                }
//
//                                if (allCpuPlayer[5].answer.equals("true", true)) {
//                                    binding.cpur2p2Select.visibility = View.GONE
//                                } else {
//                                    binding.cpur2p2Select.visibility = View.VISIBLE
//                                }
//                            }
//                        }
//
//                    }
//                } // Done
//                else -> {
//                    Toast.makeText(requireContext(), "Formation is not Exist $onSelectedScreen", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//
//
//    // This function call for 5-2-3
//    private fun playerUserCpu5_2_3() {
//        // User
//        if (allCpuPlayer[0].answer.equals("true", true)) {
//            binding.r1p1Select.visibility = View.GONE
//        } else {
//            binding.r1p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[1].answer.equals("true", true)) {
//            binding.r1p2Select.visibility = View.GONE
//        } else {
//            binding.r1p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[2].answer.equals("true", true)) {
//            binding.r1p3Select.visibility = View.GONE
//        } else {
//            binding.r1p3Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[3].answer.equals("true", true)) {
//            binding.r1p4Select.visibility = View.GONE
//        } else {
//            binding.r1p4Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[4].answer.equals("true", true)) {
//            binding.r1p5Select.visibility = View.GONE
//        } else {
//            binding.r1p5Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[5].answer.equals("true", true)) {
//            binding.r2p1Select.visibility = View.GONE
//        } else {
//            binding.r2p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[6].answer.equals("true", true)) {
//            binding.r2p2Select.visibility = View.GONE
//        } else {
//            binding.r2p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[7].answer.equals("true", true)) {
//            binding.r3p1Select.visibility = View.GONE
//        } else {
//            binding.r3p1Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[8].answer.equals("true", true)) {
//            binding.r3p2Select.visibility = View.GONE
//        } else {
//            binding.r3p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[9].answer.equals("true", true)) {
//            binding.r3p3Select.visibility = View.GONE
//        } else {
//            binding.r3p3Select.visibility = View.VISIBLE
//        }
//
//        //Shrawan
//        if (allCpuPlayer[10].answer.equals("true", true)) {
//            binding.r0p1Select.visibility = View.GONE
//        } else {
//            binding.r0p1Select.visibility = View.VISIBLE
//        }
//
//        // Cpu
//        if (allUserPlayer[0].answer.equals("true", true)) {
//            binding.cpur1p1Select.visibility = View.GONE
//        } else {
//            binding.cpur1p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[1].answer.equals("true", true)) {
//            binding.cpur1p2Select.visibility = View.GONE
//        } else {
//            binding.cpur1p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[2].answer.equals("true", true)) {
//            binding.cpur1p3Select.visibility = View.GONE
//        } else {
//            binding.cpur1p3Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[3].answer.equals("true", true)) {
//            binding.cpur1p4Select.visibility = View.GONE
//        } else {
//            binding.cpur1p4Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[4].answer.equals("true", true)) {
//            binding.cpur1p5Select.visibility = View.GONE
//        } else {
//            binding.cpur1p5Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[5].answer.equals("true", true)) {
//            binding.cpur2p1Select.visibility = View.GONE
//        } else {
//            binding.cpur2p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[6].answer.equals("true", true)) {
//            binding.cpur2p2Select.visibility = View.GONE
//        } else {
//            binding.cpur2p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[7].answer.equals("true", true)) {
//            binding.cpur3p1Select.visibility = View.GONE
//        } else {
//            binding.cpur3p1Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[8].answer.equals("true", true)) {
//            binding.cpur3p2Select.visibility = View.GONE
//        } else {
//            binding.cpur3p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[9].answer.equals("true", true)) {
//            binding.cpur3p3Select.visibility = View.GONE
//        } else {
//            binding.cpur3p3Select.visibility = View.VISIBLE
//        }
//
//        //Shrawan
//        if (allUserPlayer[10].answer.equals("true", true)) {
//            binding.cpur0p1Select.visibility = View.GONE
//        } else {
//            binding.cpur0p1Select.visibility = View.VISIBLE
//        }
//    }
//    // This function call for 5-4-1
//    private fun playerUserCpu5_4_1() {
//        // User
//        if (allCpuPlayer[0].answer.equals("true", true)) {
//            binding.r1p1Select.visibility = View.GONE
//        } else {
//            binding.r1p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[1].answer.equals("true", true)) {
//            binding.r1p2Select.visibility = View.GONE
//        } else {
//            binding.r1p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[2].answer.equals("true", true)) {
//            binding.r1p3Select.visibility = View.GONE
//        } else {
//            binding.r1p3Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[3].answer.equals("true", true)) {
//            binding.r1p4Select.visibility = View.GONE
//        } else {
//            binding.r1p4Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[4].answer.equals("true", true)) {
//            binding.r1p5Select.visibility = View.GONE
//        } else {
//            binding.r1p5Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[5].answer.equals("true", true)) {
//            binding.r2p1Select.visibility = View.GONE
//        } else {
//            binding.r2p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[6].answer.equals("true", true)) {
//            binding.r2p2Select.visibility = View.GONE
//        } else {
//            binding.r2p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[7].answer.equals("true", true)) {
//            binding.r2p3Select.visibility = View.GONE
//        } else {
//            binding.r2p3Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[8].answer.equals("true", true)) {
//            binding.r2p4Select.visibility = View.GONE
//        } else {
//            binding.r2p4Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[9].answer.equals("true", true)) {
//            binding.r3p1Select.visibility = View.GONE
//        } else {
//            binding.r3p1Select.visibility = View.VISIBLE
//        }
//        //Shrawan
//        if (allCpuPlayer[10].answer.equals("true", true)) {
//            binding.r0p1Select.visibility = View.GONE
//        } else {
//            binding.r0p1Select.visibility = View.VISIBLE
//        }
//
//        // Cpu
//        if (allUserPlayer[0].answer.equals("true", true)) {
//            binding.cpur1p1Select.visibility = View.GONE
//        } else {
//            binding.cpur1p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[1].answer.equals("true", true)) {
//            binding.cpur1p2Select.visibility = View.GONE
//        } else {
//            binding.cpur1p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[2].answer.equals("true", true)) {
//            binding.cpur1p3Select.visibility = View.GONE
//        } else {
//            binding.cpur1p3Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[3].answer.equals("true", true)) {
//            binding.cpur1p4Select.visibility = View.GONE
//        } else {
//            binding.cpur1p4Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[4].answer.equals("true", true)) {
//            binding.cpur1p5Select.visibility = View.GONE
//        } else {
//            binding.cpur1p5Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[5].answer.equals("true", true)) {
//            binding.cpur2p1Select.visibility = View.GONE
//        } else {
//            binding.cpur2p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[6].answer.equals("true", true)) {
//            binding.cpur2p2Select.visibility = View.GONE
//        } else {
//            binding.cpur2p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[7].answer.equals("true", true)) {
//            binding.cpur2p3Select.visibility = View.GONE
//        } else {
//            binding.cpur2p3Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[8].answer.equals("true", true)) {
//            binding.cpur2p4Select.visibility = View.GONE
//        } else {
//            binding.cpur2p4Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[9].answer.equals("true", true)) {
//            binding.cpur3p1Select.visibility = View.GONE
//        } else {
//            binding.cpur3p1Select.visibility = View.VISIBLE
//        }
//        //Shrawan
//        if (allUserPlayer[10].answer.equals("true", true)) {
//            binding.cpur0p1Select.visibility = View.GONE
//        } else {
//            binding.cpur0p1Select.visibility = View.VISIBLE
//        }
//    }
//    // This function call for 5-3-2
//    private fun playerUserCpu5_3_2() {
//        // User
//        if (allCpuPlayer[0].answer.equals("true", true)) {
//            binding.r1p1Select.visibility = View.GONE
//        } else {
//            binding.r1p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[1].answer.equals("true", true)) {
//            binding.r1p2Select.visibility = View.GONE
//        } else {
//            binding.r1p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[2].answer.equals("true", true)) {
//            binding.r1p3Select.visibility = View.GONE
//        } else {
//            binding.r1p3Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[3].answer.equals("true", true)) {
//            binding.r1p4Select.visibility = View.GONE
//        } else {
//            binding.r1p4Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[4].answer.equals("true", true)) {
//            binding.r1p5Select.visibility = View.GONE
//        } else {
//            binding.r1p5Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[5].answer.equals("true", true)) {
//            binding.r2p1Select.visibility = View.GONE
//        } else {
//            binding.r2p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[6].answer.equals("true", true)) {
//            binding.r2p2Select.visibility = View.GONE
//        } else {
//            binding.r2p2Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[7].answer.equals("true", true)) {
//            binding.r2p3Select.visibility = View.GONE
//        } else {
//            binding.r2p3Select.visibility = View.VISIBLE
//        }
//
//
//        if (allCpuPlayer[8].answer.equals("true", true)) {
//            binding.r3p1Select.visibility = View.GONE
//        } else {
//            binding.r3p1Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[9].answer.equals("true", true)) {
//            binding.r3p2Select.visibility = View.GONE
//        } else {
//            binding.r3p2Select.visibility = View.VISIBLE
//        }
//        //Shrawan
//        if (allCpuPlayer[10].answer.equals("true", true)) {
//            binding.r0p1Select.visibility = View.GONE
//        } else {
//            binding.r0p1Select.visibility = View.VISIBLE
//        }
//
//
//        // Cpu
//        if (allUserPlayer[0].answer.equals("true", true)) {
//            binding.cpur1p1Select.visibility = View.GONE
//        } else {
//            binding.cpur1p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[1].answer.equals("true", true)) {
//            binding.cpur1p2Select.visibility = View.GONE
//        } else {
//            binding.cpur1p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[2].answer.equals("true", true)) {
//            binding.cpur1p3Select.visibility = View.GONE
//        } else {
//            binding.cpur1p3Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[3].answer.equals("true", true)) {
//            binding.cpur1p4Select.visibility = View.GONE
//        } else {
//            binding.cpur1p4Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[4].answer.equals("true", true)) {
//            binding.cpur1p5Select.visibility = View.GONE
//        } else {
//            binding.cpur1p5Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[5].answer.equals("true", true)) {
//            binding.cpur2p1Select.visibility = View.GONE
//        } else {
//            binding.cpur2p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[6].answer.equals("true", true)) {
//            binding.cpur2p2Select.visibility = View.GONE
//        } else {
//            binding.cpur2p2Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[7].answer.equals("true", true)) {
//            binding.cpur2p3Select.visibility = View.GONE
//        } else {
//            binding.cpur2p3Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[8].answer.equals("true", true)) {
//            binding.cpur3p1Select.visibility = View.GONE
//        } else {
//            binding.cpur3p1Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[9].answer.equals("true", true)) {
//            binding.cpur3p2Select.visibility = View.GONE
//        } else {
//            binding.cpur3p2Select.visibility = View.VISIBLE
//        }
//
//        //Shrawan
//        if (allUserPlayer[10].answer.equals("true", true)) {
//            binding.cpur0p1Select.visibility = View.GONE
//        } else {
//            binding.cpur0p1Select.visibility = View.VISIBLE
//        }
//
//
//    }
//    // This function call for 3-5-2
//    private fun playerUserCpu3_5_2() {
//
//        // User
//        if (allCpuPlayer[0].answer.equals("true", true)) {
//            binding.r1p1Select.visibility = View.GONE
//        } else {
//            binding.r1p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[1].answer.equals("true", true)) {
//            binding.r1p2Select.visibility = View.GONE
//        } else {
//            binding.r1p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[2].answer.equals("true", true)) {
//            binding.r1p3Select.visibility = View.GONE
//        } else {
//            binding.r1p3Select.visibility = View.VISIBLE
//        }
//
//
//        if (allCpuPlayer[3].answer.equals("true", true)) {
//            binding.r2p1Select.visibility = View.GONE
//        } else {
//            binding.r2p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[4].answer.equals("true", true)) {
//            binding.r2p2Select.visibility = View.GONE
//        } else {
//            binding.r2p2Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[5].answer.equals("true", true)) {
//            binding.r2p3Select.visibility = View.GONE
//        } else {
//            binding.r2p3Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[6].answer.equals("true", true)) {
//            binding.r2p4Select.visibility = View.GONE
//        } else {
//            binding.r2p4Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[7].answer.equals("true", true)) {
//            binding.r2p5Select.visibility = View.GONE
//        } else {
//            binding.r2p5Select.visibility = View.VISIBLE
//        }
//
//
//        if (allCpuPlayer[8].answer.equals("true", true)) {
//            binding.r3p1Select.visibility = View.GONE
//        } else {
//            binding.r3p1Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[9].answer.equals("true", true)) {
//            binding.r3p2Select.visibility = View.GONE
//        } else {
//            binding.r3p2Select.visibility = View.VISIBLE
//        }
//        //Shrawan
//        if (allCpuPlayer[10].answer.equals("true", true)) {
//            binding.r0p1Select.visibility = View.GONE
//        } else {
//            binding.r0p1Select.visibility = View.VISIBLE
//        }
//
//
//        // Cpu
//        if (allUserPlayer[0].answer.equals("true", true)) {
//            binding.cpur1p1Select.visibility = View.GONE
//        } else {
//            binding.cpur1p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[1].answer.equals("true", true)) {
//            binding.cpur1p2Select.visibility = View.GONE
//        } else {
//            binding.cpur1p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[2].answer.equals("true", true)) {
//            binding.cpur1p3Select.visibility = View.GONE
//        } else {
//            binding.cpur1p3Select.visibility = View.VISIBLE
//        }
//
//
//        if (allUserPlayer[3].answer.equals("true", true)) {
//            binding.cpur2p1Select.visibility = View.GONE
//        } else {
//            binding.cpur2p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[4].answer.equals("true", true)) {
//            binding.cpur2p2Select.visibility = View.GONE
//        } else {
//            binding.cpur2p2Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[5].answer.equals("true", true)) {
//            binding.cpur2p3Select.visibility = View.GONE
//        } else {
//            binding.cpur2p3Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[6].answer.equals("true", true)) {
//            binding.cpur2p4Select.visibility = View.GONE
//        } else {
//            binding.cpur2p4Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[7].answer.equals("true", true)) {
//            binding.cpur2p5Select.visibility = View.GONE
//        } else {
//            binding.cpur2p5Select.visibility = View.VISIBLE
//        }
//
//
//        if (allUserPlayer[8].answer.equals("true", true)) {
//            binding.cpur3p1Select.visibility = View.GONE
//        } else {
//            binding.cpur3p1Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[9].answer.equals("true", true)) {
//            binding.cpur3p2Select.visibility = View.GONE
//        } else {
//            binding.cpur3p2Select.visibility = View.VISIBLE
//        }
//        //Shrawan
//        if (allUserPlayer[10].answer.equals("true", true)) {
//            binding.cpur0p1Select.visibility = View.GONE
//        } else {
//            binding.cpur0p1Select.visibility = View.VISIBLE
//        }
//
//
//    }
//    // This function call for 4-5-1
//    private fun playerUserCpu4_5_1() {
//
//        // User
//        if (allCpuPlayer[0].answer.equals("true", true)) {
//            binding.r1p1Select.visibility = View.GONE
//        } else {
//            binding.r1p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[1].answer.equals("true", true)) {
//            binding.r1p2Select.visibility = View.GONE
//        } else {
//            binding.r1p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[2].answer.equals("true", true)) {
//            binding.r1p3Select.visibility = View.GONE
//        } else {
//            binding.r1p3Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[3].answer.equals("true", true)) {
//            binding.r1p4Select.visibility = View.GONE
//        } else {
//            binding.r1p4Select.visibility = View.VISIBLE
//        }
//
//
//        if (allCpuPlayer[4].answer.equals("true", true)) {
//            binding.r2p1Select.visibility = View.GONE
//        } else {
//            binding.r2p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[5].answer.equals("true", true)) {
//            binding.r2p2Select.visibility = View.GONE
//        } else {
//            binding.r2p2Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[6].answer.equals("true", true)) {
//            binding.r2p3Select.visibility = View.GONE
//        } else {
//            binding.r2p3Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[7].answer.equals("true", true)) {
//            binding.r2p4Select.visibility = View.GONE
//        } else {
//            binding.r2p4Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[8].answer.equals("true", true)) {
//            binding.r2p5Select.visibility = View.GONE
//        } else {
//            binding.r2p5Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[9].answer.equals("true", true)) {
//            binding.r3p1Select.visibility = View.GONE
//        } else {
//            binding.r3p1Select.visibility = View.VISIBLE
//        }
//
//        //Shrawan
//        if (allCpuPlayer[10].answer.equals("true", true)) {
//            binding.r0p1Select.visibility = View.GONE
//        } else {
//            binding.r0p1Select.visibility = View.VISIBLE
//        }
//
//        // Cpu
//        if (allUserPlayer[0].answer.equals("true", true)) {
//            binding.cpur1p1Select.visibility = View.GONE
//        } else {
//            binding.cpur1p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[1].answer.equals("true", true)) {
//            binding.cpur1p2Select.visibility = View.GONE
//        } else {
//            binding.cpur1p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[2].answer.equals("true", true)) {
//            binding.cpur1p3Select.visibility = View.GONE
//        } else {
//            binding.cpur1p3Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[3].answer.equals("true", true)) {
//            binding.cpur1p4Select.visibility = View.GONE
//        } else {
//            binding.cpur1p4Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[4].answer.equals("true", true)) {
//            binding.cpur2p1Select.visibility = View.GONE
//        } else {
//            binding.cpur2p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[5].answer.equals("true", true)) {
//            binding.cpur2p2Select.visibility = View.GONE
//        } else {
//            binding.cpur2p2Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[6].answer.equals("true", true)) {
//            binding.cpur2p3Select.visibility = View.GONE
//        } else {
//            binding.cpur2p3Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[7].answer.equals("true", true)) {
//            binding.cpur2p4Select.visibility = View.GONE
//        } else {
//            binding.cpur2p4Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[8].answer.equals("true", true)) {
//            binding.cpur2p5Select.visibility = View.GONE
//        } else {
//            binding.cpur2p5Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[9].answer.equals("true", true)) {
//            binding.cpur3p1Select.visibility = View.GONE
//        } else {
//            binding.cpur3p1Select.visibility = View.VISIBLE
//        }
//        //Shrawan
//        if (allUserPlayer[10].answer.equals("true", true)) {
//            binding.cpur0p1Select.visibility = View.GONE
//        } else {
//            binding.cpur0p1Select.visibility = View.VISIBLE
//        }
//    }
//    // This function call for 4-4-2
//    private fun playerUserCpu4_4_2() {
//
//        // User
//        if (allCpuPlayer[0].answer.equals("true", true)) {
//            binding.r1p1Select.visibility = View.GONE
//        } else {
//            binding.r1p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[1].answer.equals("true", true)) {
//            binding.r1p2Select.visibility = View.GONE
//        } else {
//            binding.r1p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[2].answer.equals("true", true)) {
//            binding.r1p3Select.visibility = View.GONE
//        } else {
//            binding.r1p3Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[3].answer.equals("true", true)) {
//            binding.r1p4Select.visibility = View.GONE
//        } else {
//            binding.r1p4Select.visibility = View.VISIBLE
//        }
//
//
//        if (allCpuPlayer[4].answer.equals("true", true)) {
//            binding.r2p1Select.visibility = View.GONE
//        } else {
//            binding.r2p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[5].answer.equals("true", true)) {
//            binding.r2p2Select.visibility = View.GONE
//        } else {
//            binding.r2p2Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[6].answer.equals("true", true)) {
//            binding.r2p3Select.visibility = View.GONE
//        } else {
//            binding.r2p3Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[7].answer.equals("true", true)) {
//            binding.r2p4Select.visibility = View.GONE
//        } else {
//            binding.r2p4Select.visibility = View.VISIBLE
//        }
//
//
//        if (allCpuPlayer[8].answer.equals("true", true)) {
//            binding.r3p1Select.visibility = View.GONE
//        } else {
//            binding.r3p1Select.visibility = View.VISIBLE
//        }
//
//        if (allCpuPlayer[9].answer.equals("true", true)) {
//            binding.r3p2Select.visibility = View.GONE
//        } else {
//            binding.r3p2Select.visibility = View.VISIBLE
//        }
//
//        //Shrawan
//        if (allCpuPlayer[10].answer.equals("true", true)) {
//            binding.r0p1Select.visibility = View.GONE
//        } else {
//            binding.r0p1Select.visibility = View.VISIBLE
//        }
//
//
//        // Cpu
//        if (allUserPlayer[0].answer.equals("true", true)) {
//            binding.cpur1p1Select.visibility = View.GONE
//        } else {
//            binding.cpur1p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[1].answer.equals("true", true)) {
//            binding.cpur1p2Select.visibility = View.GONE
//        } else {
//            binding.cpur1p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[2].answer.equals("true", true)) {
//            binding.cpur1p3Select.visibility = View.GONE
//        } else {
//            binding.cpur1p3Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[3].answer.equals("true", true)) {
//            binding.cpur1p4Select.visibility = View.GONE
//        } else {
//            binding.cpur1p4Select.visibility = View.VISIBLE
//        }
//
//
//        if (allUserPlayer[4].answer.equals("true", true)) {
//            binding.cpur2p1Select.visibility = View.GONE
//        } else {
//            binding.cpur2p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[5].answer.equals("true", true)) {
//            binding.cpur2p2Select.visibility = View.GONE
//        } else {
//            binding.cpur2p2Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[6].answer.equals("true", true)) {
//            binding.cpur2p3Select.visibility = View.GONE
//        } else {
//            binding.cpur2p3Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[7].answer.equals("true", true)) {
//            binding.cpur2p4Select.visibility = View.GONE
//        } else {
//            binding.cpur2p4Select.visibility = View.VISIBLE
//        }
//
//
//
//
//        if (allUserPlayer[8].answer.equals("true", true)) {
//            binding.cpur3p1Select.visibility = View.GONE
//        } else {
//            binding.cpur3p1Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[9].answer.equals("true", true)) {
//            binding.cpur3p2Select.visibility = View.GONE
//        } else {
//            binding.cpur3p2Select.visibility = View.VISIBLE
//        }
//
//        //Shrawan
//        if (allUserPlayer[10].answer.equals("true", true)) {
//            binding.cpur0p1Select.visibility = View.GONE
//        } else {
//            binding.cpur0p1Select.visibility = View.VISIBLE
//        }
//
//
//    }
//    // This function call for 4-3-3
//    private fun playerUserCpu4_3_3() {
//        // User
//        if (allCpuPlayer[0].answer.equals("true", true)) {
//            binding.r1p1Select.visibility = View.GONE
//        } else {
//            binding.r1p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[1].answer.equals("true", true)) {
//            binding.r1p2Select.visibility = View.GONE
//        } else {
//            binding.r1p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[2].answer.equals("true", true)) {
//            binding.r1p3Select.visibility = View.GONE
//        } else {
//            binding.r1p3Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[3].answer.equals("true", true)) {
//            binding.r1p4Select.visibility = View.GONE
//        } else {
//            binding.r1p4Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[4].answer.equals("true", true)) {
//            binding.r2p1Select.visibility = View.GONE
//        } else {
//            binding.r2p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[5].answer.equals("true", true)) {
//            binding.r2p2Select.visibility = View.GONE
//        } else {
//            binding.r2p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[6].answer.equals("true", true)) {
//            binding.r2p3Select.visibility = View.GONE
//        } else {
//            binding.r2p3Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[7].answer.equals("true", true)) {
//            binding.r3p1Select.visibility = View.GONE
//        } else {
//            binding.r3p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[8].answer.equals("true", true)) {
//            binding.r3p2Select.visibility = View.GONE
//        } else {
//            binding.r3p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[9].answer.equals("true", true)) {
//            binding.r3p3Select.visibility = View.GONE
//        } else {
//            binding.r3p3Select.visibility = View.VISIBLE
//        }
//
//        //Shrawan
//        if (allCpuPlayer[10].answer.equals("true", true)) {
//            binding.r0p1Select.visibility = View.GONE
//        } else {
//            binding.r0p1Select.visibility = View.VISIBLE
//        }
//
//
//        // Cpu
//        if (allUserPlayer[0].answer.equals("true", true)) {
//            binding.cpur1p1Select.visibility = View.GONE
//        } else {
//            binding.cpur1p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[1].answer.equals("true", true)) {
//            binding.cpur1p2Select.visibility = View.GONE
//        } else {
//            binding.cpur1p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[2].answer.equals("true", true)) {
//            binding.cpur1p3Select.visibility = View.GONE
//        } else {
//            binding.cpur1p3Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[3].answer.equals("true", true)) {
//            binding.cpur1p4Select.visibility = View.GONE
//        } else {
//            binding.cpur1p4Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[4].answer.equals("true", true)) {
//            binding.cpur2p1Select.visibility = View.GONE
//        } else {
//            binding.cpur2p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[5].answer.equals("true", true)) {
//            binding.cpur2p2Select.visibility = View.GONE
//        } else {
//            binding.cpur2p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[6].answer.equals("true", true)) {
//            binding.cpur2p3Select.visibility = View.GONE
//        } else {
//            binding.cpur2p3Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[7].answer.equals("true", true)) {
//            binding.cpur3p1Select.visibility = View.GONE
//        } else {
//            binding.cpur3p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[8].answer.equals("true", true)) {
//            binding.cpur3p2Select.visibility = View.GONE
//        } else {
//            binding.cpur3p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[9].answer.equals("true", true)) {
//            binding.cpur3p3Select.visibility = View.GONE
//        } else {
//            binding.cpur3p3Select.visibility = View.VISIBLE
//        }
//
//        //Shrawan
//        if (allUserPlayer[10].answer.equals("true", true)) {
//            binding.cpur0p1Select.visibility = View.GONE
//        } else {
//            binding.cpur0p1Select.visibility = View.VISIBLE
//        }
//
//    }
//    // This function call for 3-4-3
//    private fun playerUserCpu3_4_3() {
//        // User
//        if (allCpuPlayer[0].answer.equals("true", true)) {
//            binding.r1p1Select.visibility = View.GONE
//        } else {
//            binding.r1p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[1].answer.equals("true", true)) {
//            binding.r1p2Select.visibility = View.GONE
//        } else {
//            binding.r1p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[2].answer.equals("true", true)) {
//            binding.r1p3Select.visibility = View.GONE
//        } else {
//            binding.r1p3Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[3].answer.equals("true", true)) {
//            binding.r2p1Select.visibility = View.GONE
//        } else {
//            binding.r2p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[4].answer.equals("true", true)) {
//            binding.r2p2Select.visibility = View.GONE
//        } else {
//            binding.r2p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[5].answer.equals("true", true)) {
//            binding.r2p3Select.visibility = View.GONE
//        } else {
//            binding.r2p3Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[6].answer.equals("true", true)) {
//            binding.r2p4Select.visibility = View.GONE
//        } else {
//            binding.r2p4Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[7].answer.equals("true", true)) {
//            binding.r3p1Select.visibility = View.GONE
//        } else {
//            binding.r3p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[8].answer.equals("true", true)) {
//            binding.r3p2Select.visibility = View.GONE
//        } else {
//            binding.r3p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[9].answer.equals("true", true)) {
//            binding.r3p3Select.visibility = View.GONE
//        } else {
//            binding.r3p3Select.visibility = View.VISIBLE
//        }
//        //Shrawan
//        if (allCpuPlayer[10].answer.equals("true", true)) {
//            binding.r0p1Select.visibility = View.GONE
//        } else {
//            binding.r0p1Select.visibility = View.VISIBLE
//        }
//
//        // Cpu
//        if (allUserPlayer[0].answer.equals("true", true)) {
//            binding.cpur1p1Select.visibility = View.GONE
//        } else {
//            binding.cpur1p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[1].answer.equals("true", true)) {
//            binding.cpur1p2Select.visibility = View.GONE
//        } else {
//            binding.cpur1p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[2].answer.equals("true", true)) {
//            binding.cpur1p3Select.visibility = View.GONE
//        } else {
//            binding.cpur1p3Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[3].answer.equals("true", true)) {
//            binding.cpur2p1Select.visibility = View.GONE
//        } else {
//            binding.cpur2p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[4].answer.equals("true", true)) {
//            binding.cpur2p2Select.visibility = View.GONE
//        } else {
//            binding.cpur2p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[5].answer.equals("true", true)) {
//            binding.cpur2p3Select.visibility = View.GONE
//        } else {
//            binding.cpur2p3Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[6].answer.equals("true", true)) {
//            binding.cpur2p4Select.visibility = View.GONE
//        } else {
//            binding.cpur2p4Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[7].answer.equals("true", true)) {
//            binding.cpur3p1Select.visibility = View.GONE
//        } else {
//            binding.cpur3p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[8].answer.equals("true", true)) {
//            binding.cpur3p2Select.visibility = View.GONE
//        } else {
//            binding.cpur3p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[9].answer.equals("true", true)) {
//            binding.cpur3p3Select.visibility = View.GONE
//        } else {
//            binding.cpur3p3Select.visibility = View.VISIBLE
//        }
//        //Shrawan
//        if (allUserPlayer[10].answer.equals("true", true)) {
//            binding.cpur0p1Select.visibility = View.GONE
//        } else {
//            binding.cpur0p1Select.visibility = View.VISIBLE
//        }
//    }
//    // This function call for 4-2-4
//    private fun playerUserCpu4_2_4() {
//        // User
//        if (allCpuPlayer[0].answer.equals("true", true)) {
//            binding.r1p1Select.visibility = View.GONE
//        } else {
//            binding.r1p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[1].answer.equals("true", true)) {
//            binding.r1p2Select.visibility = View.GONE
//        } else {
//            binding.r1p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[2].answer.equals("true", true)) {
//            binding.r1p3Select.visibility = View.GONE
//        } else {
//            binding.r1p3Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[3].answer.equals("true", true)) {
//            binding.r1p4Select.visibility = View.GONE
//        } else {
//            binding.r1p4Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[4].answer.equals("true", true)) {
//            binding.r2p1Select.visibility = View.GONE
//        } else {
//            binding.r2p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[5].answer.equals("true", true)) {
//            binding.r2p2Select.visibility = View.GONE
//        } else {
//            binding.r2p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[6].answer.equals("true", true)) {
//            binding.r3p1Select.visibility = View.GONE
//        } else {
//            binding.r3p1Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[7].answer.equals("true", true)) {
//            binding.r3p2Select.visibility = View.GONE
//        } else {
//            binding.r3p2Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[8].answer.equals("true", true)) {
//            binding.r3p3Select.visibility = View.GONE
//        } else {
//            binding.r3p3Select.visibility = View.VISIBLE
//        }
//        if (allCpuPlayer[9].answer.equals("true", true)) {
//            binding.r3p4Select.visibility = View.GONE
//        } else {
//            binding.r3p4Select.visibility = View.VISIBLE
//        }
//        //Shrawan
//        if (allCpuPlayer[10].answer.equals("true", true)) {
//            binding.r0p1Select.visibility = View.GONE
//        } else {
//            binding.r0p1Select.visibility = View.VISIBLE
//        }
//        // Cpu
//        if (allUserPlayer[0].answer.equals("true", true)) {
//            binding.cpur1p1Select.visibility = View.GONE
//        } else {
//            binding.cpur1p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[1].answer.equals("true", true)) {
//            binding.cpur1p2Select.visibility = View.GONE
//        } else {
//            binding.cpur1p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[2].answer.equals("true", true)) {
//            binding.cpur1p3Select.visibility = View.GONE
//        } else {
//            binding.cpur1p3Select.visibility = View.VISIBLE
//        }
//
//        if (allUserPlayer[3].answer.equals("true", true)) {
//            binding.cpur1p4Select.visibility = View.GONE
//        } else {
//            binding.cpur1p4Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[4].answer.equals("true", true)) {
//            binding.cpur2p1Select.visibility = View.GONE
//        } else {
//            binding.cpur2p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[5].answer.equals("true", true)) {
//            binding.cpur2p2Select.visibility = View.GONE
//        } else {
//            binding.cpur2p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[6].answer.equals("true", true)) {
//            binding.cpur3p1Select.visibility = View.GONE
//        } else {
//            binding.cpur3p1Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[7].answer.equals("true", true)) {
//            binding.cpur3p2Select.visibility = View.GONE
//        } else {
//            binding.cpur3p2Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[8].answer.equals("true", true)) {
//            binding.cpur3p3Select.visibility = View.GONE
//        } else {
//            binding.cpur3p3Select.visibility = View.VISIBLE
//        }
//        if (allUserPlayer[9].answer.equals("true", true)) {
//            binding.cpur3p4Select.visibility = View.GONE
//        } else {
//            binding.cpur3p4Select.visibility = View.VISIBLE
//        }
//        //Shrawan
//        if (allUserPlayer[10].answer.equals("true", true)) {
//            binding.cpur0p1Select.visibility = View.GONE
//        } else {
//            binding.cpur0p1Select.visibility = View.VISIBLE
//        }
//    }
//
//    //This function is used for set player details on t-shirt (Name , number , id) of user
//    private fun setUserScreenPlayerNameDtl() {
//        if (!isTimerFinish) {
//            try {
//                var setName = 0
//
//                if (binding.r1p1.visibility == View.VISIBLE) {
//                    binding.r1p1Name.text = allCpuPlayer[setName].name
//                    binding.r1p1Number.text = allCpuPlayer[setName].jersey_number
//                    binding.r1p1Id.text = allCpuPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.r1p2.visibility == View.VISIBLE) {
//                    binding.r1p2Name.text = allCpuPlayer[setName].name
//                    binding.r1p2Number.text = allCpuPlayer[setName].jersey_number
//                    binding.r1p2Id.text = allCpuPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.r1p3.visibility == View.VISIBLE) {
//                    binding.r1p3Name.text = allCpuPlayer[setName].name
//                    binding.r1p3Number.text = allCpuPlayer[setName].jersey_number
//                    binding.r1p3Id.text = allCpuPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.r1p4.visibility == View.VISIBLE) {
//                    binding.r1p4Name.text = allCpuPlayer[setName].name
//                    binding.r1p4Number.text = allCpuPlayer[setName].jersey_number
//                    binding.r1p4Id.text = allCpuPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.r1p5.visibility == View.VISIBLE) {
//                    binding.r1p5Name.text = allCpuPlayer[setName].name
//                    binding.r1p5Number.text = allCpuPlayer[setName].jersey_number
//                    binding.r1p5Id.text = allCpuPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.r2p1.visibility == View.VISIBLE) {
//                    binding.r2p1Name.text = allCpuPlayer[setName].name
//                    binding.r2p1Number.text = allCpuPlayer[setName].jersey_number
//                    binding.r2p1Id.text = allCpuPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.r2p2.visibility == View.VISIBLE) {
//                    binding.r2p2Name.text = allCpuPlayer[setName].name
//                    binding.r2p2Number.text = allCpuPlayer[setName].jersey_number
//                    binding.r2p2Id.text = allCpuPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.r2p3.visibility == View.VISIBLE) {
//                    binding.r2p3Name.text = allCpuPlayer[setName].name
//                    binding.r2p3Number.text = allCpuPlayer[setName].jersey_number
//                    binding.r2p3Id.text = allCpuPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.r2p4.visibility == View.VISIBLE) {
//                    binding.r2p4Name.text = allCpuPlayer[setName].name
//                    binding.r2p4Number.text = allCpuPlayer[setName].jersey_number
//                    binding.r2p4Id.text = allCpuPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.r2p5.visibility == View.VISIBLE) {
//                    binding.r2p5Name.text = allCpuPlayer[setName].name
//                    binding.r2p5Number.text = allCpuPlayer[setName].jersey_number
//                    binding.r2p5Id.text = allCpuPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.r3p1.visibility == View.VISIBLE) {
//                    if (setName <= 9) {
//                        binding.r3p1Name.text = allCpuPlayer[setName].name
//                        binding.r3p1Number.text = allCpuPlayer[setName].jersey_number
//                        binding.r3p1Id.text = allCpuPlayer[setName].id
//                    }
//                    setName += 1
//                }
//                if (binding.r3p2.visibility == View.VISIBLE) {
//                    if (setName <= 9) {
//                        binding.r3p2Name.text = allCpuPlayer[setName].name
//                        binding.r3p2Number.text = allCpuPlayer[setName].jersey_number
//                        binding.r3p2Id.text = allCpuPlayer[setName].id
//                    }
//                    setName += 1
//                }
//                if (binding.r3p3.visibility == View.VISIBLE) {
//                    if (setName <= 9) {
//                        binding.r3p3Name.text = allCpuPlayer[setName].name
//                        binding.r3p3Number.text = allCpuPlayer[setName].jersey_number
//                        binding.r3p3Id.text = allCpuPlayer[setName].id
//                    }
//                    setName += 1
//                }
//                if (binding.r3p4.visibility == View.VISIBLE) {
//                    if (setName <= 9) {
//                        binding.r3p4Name.text = allCpuPlayer[setName].name
//                        binding.r3p4Number.text = allCpuPlayer[setName].jersey_number
//                        binding.r3p4Id.text = allCpuPlayer[setName].id
//                    }
//                    setName += 1
//                }
//                if (binding.r3p5.visibility == View.VISIBLE) {
//                    if (setName <= 9) {
//                        binding. r3p5Name.text = allCpuPlayer[setName].name
//                        binding.r3p5Number.text = allCpuPlayer[setName].jersey_number
//                        binding.r3p5Id.text = allCpuPlayer[setName].id
//                    }
//                    setName += 1
//                }
//
//                //Shrawan
//                if (binding.r0p1.visibility == View.VISIBLE) {
//                    if (setName <= 10) {
//                        binding.r0p1Name.text = allCpuPlayer[setName].name
//                        binding.r0p1Number.text = allCpuPlayer[setName].jersey_number
//                        binding.r0p1Id.text = allCpuPlayer[setName].id
//                    }
//                    setName += 1
//                }
//            }catch (e:Exception){
//                Log.d("Player Deatil :- ","**** error :- "+e.message)
//            }
//            setCpuScreenPlayerNameDtl()
//        }
//    }
//
//    //This function is used for set player details on t-shirt (Name , number , id) of cpu
//    private fun setCpuScreenPlayerNameDtl() {
//        if (!isTimerFinish) {
//            try {
//                var setName = 0
//                if (binding.cpur1p1.visibility == View.VISIBLE) {
//                    binding.cpur1p1Name.text = allUserPlayer[setName].name
//                    binding.cpur1p1Number.text = allUserPlayer[setName].jersey_number
//                    binding.cpur1p1Id.text = allUserPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.cpur1p2.visibility == View.VISIBLE) {
//                    binding.cpur1p2Name.text = allUserPlayer[setName].name
//                    binding.cpur1p2Number.text = allUserPlayer[setName].jersey_number
//                    binding.cpur1p2Id.text = allUserPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.cpur1p3.visibility == View.VISIBLE) {
//                    binding.cpur1p3Name.text = allUserPlayer[setName].name
//                    binding.cpur1p3Number.text = allUserPlayer[setName].jersey_number
//                    binding.cpur1p3Id.text = allUserPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.cpur1p4.visibility == View.VISIBLE) {
//                    binding.cpur1p4Name.text = allUserPlayer[setName].name
//                    binding.cpur1p4Number.text = allUserPlayer[setName].jersey_number
//                    binding.cpur1p4Id.text = allUserPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.cpur1p5.visibility == View.VISIBLE) {
//                    binding.cpur1p5Name.text = allUserPlayer[setName].name
//                    binding.cpur1p5Number.text = allUserPlayer[setName].jersey_number
//                    binding.cpur1p5Id.text = allUserPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.cpur2p1.visibility == View.VISIBLE) {
//                    binding.cpur2p1Name.text = allUserPlayer[setName].name
//                    binding.cpur2p1Number.text = allUserPlayer[setName].jersey_number
//                    binding.cpur2p1Id.text = allUserPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.cpur2p2.visibility == View.VISIBLE) {
//                    binding.cpur2p2Name.text = allUserPlayer[setName].name
//                    binding.cpur2p2Number.text = allUserPlayer[setName].jersey_number
//                    binding.cpur2p2Id.text = allUserPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.cpur2p3.visibility == View.VISIBLE) {
//                    binding.cpur2p3Name.text = allUserPlayer[setName].name
//                    binding.cpur2p3Number.text = allUserPlayer[setName].jersey_number
//                    binding.cpur2p3Id.text = allUserPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.cpur2p4.visibility == View.VISIBLE) {
//                    binding.cpur2p4Name.text = allUserPlayer[setName].name
//                    binding.cpur2p4Number.text = allUserPlayer[setName].jersey_number
//                    binding.cpur2p4Id.text = allUserPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.cpur2p5.visibility == View.VISIBLE) {
//                    binding.cpur2p5Name.text = allUserPlayer[setName].name
//                    binding.cpur2p5Number.text = allUserPlayer[setName].jersey_number
//                    binding.cpur2p5Id.text = allUserPlayer[setName].id
//                    setName += 1
//                }
//                if (binding.cpur3p1.visibility == View.VISIBLE) {
//                    if (setName <= 9) {
//                        binding.cpur3p1Name.text = allUserPlayer[setName].name
//                        binding.cpur3p1Number.text = allUserPlayer[setName].jersey_number
//                        binding.cpur3p1Id.text = allUserPlayer[setName].id
//                    }
//                    setName += 1
//                }
//                if (binding.cpur3p2.visibility == View.VISIBLE) {
//                    if (setName <= 9) {
//                        binding.cpur3p2Name.text = allUserPlayer[setName].name
//                        binding.cpur3p2Number.text = allUserPlayer[setName].jersey_number
//                        binding.cpur3p2Id.text = allUserPlayer[setName].id
//                    }
//                    setName += 1
//                }
//                if (binding.cpur3p3.visibility == View.VISIBLE) {
//                    if (setName <= 9) {
//                        binding.cpur3p3Name.text = allUserPlayer[setName].name
//                        binding.cpur3p3Number.text = allUserPlayer[setName].jersey_number
//                        binding.cpur3p3Id.text = allUserPlayer[setName].id
//                    }
//                    setName += 1
//                }
//                if (binding.cpur3p4.visibility == View.VISIBLE) {
//                    if (setName <= 9) {
//                        binding.cpur3p4Name.text = allUserPlayer[setName].name
//                        binding.cpur3p4Number.text = allUserPlayer[setName].jersey_number
//                        binding.cpur3p4Id.text = allUserPlayer[setName].id
//                    }
//                    setName += 1
//                }
//                if (binding.cpur3p5.visibility == View.VISIBLE) {
//                    if (setName <= 9) {
//                        binding. cpur3p5Name.text = allUserPlayer[setName].name
//                        binding.cpur3p5Number.text = allUserPlayer[setName].jersey_number
//                        binding.cpur3p5Id.text = allUserPlayer[setName].id
//                    }
//                    setName += 1
//                }
//
//                //Shrawan
//                if (binding.cpur0p1.visibility == View.VISIBLE) {
//                    if (setName <= 10) {
//                        binding. cpur0p1Name.text = allUserPlayer[setName].name
//                        binding.cpur0p1Number.text = allUserPlayer[setName].jersey_number
//                        binding.cpur0p1Id.text = allUserPlayer[setName].id
//                    }
//                    setName += 1
//                }
//            }catch (e:Exception){
//                Log.d("Cpu Name Detail :- ","****** error :- "+e.message)
//            }
//            showRightAnswerName()
//        }
//    }
//    //This function is used for show name which guessed by user or cpu
//    private fun showRightAnswerName() {
//        try {
//            if (!isTimerFinish) {
//                if (allCpuPlayer[binding.r1p1Id.text.toString().toInt() - 1].answer == "true" && binding.r1p1.visibility == View.VISIBLE) {
//                    totalPlayerNameShow += 1
//                    binding.r1p1Txt.visibility = View.VISIBLE
//                    val countryID = allCpuPlayer[binding.r1p1Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r1p1Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r1p1Name.setTextColor(textColor)
//                    binding.r1p1Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r1p2Id.text.toString().toInt() - 1].answer == "true" && binding.r1p2.visibility == View.VISIBLE) {
//                    totalPlayerNameShow += 1
//                    binding.r1p2Txt.visibility = View.VISIBLE
//                    val countryID = allCpuPlayer[binding.r1p2Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r1p2Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r1p2Name.setTextColor(textColor)
//                    binding.r1p2Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r1p3Id.text.toString().toInt() - 1].answer == "true" && binding.r1p3.visibility == View.VISIBLE) {
//                    totalPlayerNameShow += 1
//                    binding.r1p3Txt.visibility = View.VISIBLE
//                    val countryID = allCpuPlayer[binding.r1p3Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r1p3Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r1p3Name.setTextColor(textColor)
//                    binding.r1p3Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r1p4Id.text.toString().toInt() - 1].answer == "true" && binding.r1p4.visibility == View.VISIBLE) {
//                    binding.r1p4Txt.visibility = View.VISIBLE
//                    totalPlayerNameShow += 1
//                    val countryID = allCpuPlayer[binding.r1p4Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r1p4Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding. r1p4Name.setTextColor(textColor)
//                    binding.r1p4Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r1p5Id.text.toString().toInt() - 1].answer == "true" && binding.r1p5.visibility == View.VISIBLE) {
//                    binding.r1p5Txt.visibility = View.VISIBLE
//                    totalPlayerNameShow += 1
//                    val countryID = allCpuPlayer[binding.r1p5Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r1p5Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r1p5Name.setTextColor(textColor)
//                    binding.r1p5Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r2p1Id.text.toString().toInt() - 1].answer == "true" && binding.r2p1.visibility == View.VISIBLE) {
//                    binding.r2p1Txt.visibility = View.VISIBLE
//                    totalPlayerNameShow += 1
//                    val countryID = allCpuPlayer[binding.r2p1Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r2p1Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r2p1Name.setTextColor(textColor)
//                    binding.r2p1Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r2p2Id.text.toString().toInt() - 1].answer == "true" && binding.r2p2.visibility == View.VISIBLE) {
//                    totalPlayerNameShow += 1
//                    binding.r2p2Txt.visibility = View.VISIBLE
//                    val countryID = allCpuPlayer[binding.r2p2Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r2p2Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r2p2Name.setTextColor(textColor)
//                    binding.r2p2Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r2p3Id.text.toString().toInt() - 1].answer == "true" && binding.r2p3.visibility == View.VISIBLE) {
//                    totalPlayerNameShow += 1
//                    binding.r2p3Txt.visibility = View.VISIBLE
//                    val countryID = allCpuPlayer[binding.r2p3Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r2p3Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r2p3Name.setTextColor(textColor)
//                    binding. r2p3Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r2p4Id.text.toString().toInt() - 1].answer == "true" && binding.r2p4.visibility == View.VISIBLE) {
//                    totalPlayerNameShow += 1
//                    binding.r2p4Txt.visibility = View.VISIBLE
//                    val countryID = allCpuPlayer[binding.r2p4Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r2p4Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r2p4Name.setTextColor(textColor)
//                    binding.r2p4Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r2p5Id.text.toString().toInt() - 1].answer == "true" && binding.r2p5.visibility == View.VISIBLE) {
//                    totalPlayerNameShow += 1
//                    binding.r2p5Txt.visibility = View.VISIBLE
//                    val countryID = allCpuPlayer[binding.r2p5Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r2p5Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r2p5Name.setTextColor(textColor)
//                    binding.r2p5Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r3p1Id.text.toString().toInt() - 1].answer == "true" && binding.r3p1.visibility == View.VISIBLE) {
//                    totalPlayerNameShow += 1
//                    binding.r3p1Txt.visibility = View.VISIBLE
//                    val countryID = allCpuPlayer[binding.r3p1Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r3p1Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r3p1Name.setTextColor(textColor)
//                    binding.r3p1Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r3p2Id.text.toString().toInt() - 1].answer == "true" && binding.r3p2.visibility == View.VISIBLE) {
//                    binding.r3p2Txt.visibility = View.VISIBLE
//                    totalPlayerNameShow += 1
//                    val countryID = allCpuPlayer[binding.r3p2Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r3p2Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r3p2Name.setTextColor(textColor)
//                    binding.r3p2Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r3p3Id.text.toString().toInt() - 1].answer == "true" && binding.r3p3.visibility == View.VISIBLE) {
//                    totalPlayerNameShow += 1
//                    binding.r3p3Txt.visibility = View.VISIBLE
//                    val countryID = allCpuPlayer[binding.r3p3Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r3p3Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r3p3Name.setTextColor(textColor)
//                    binding.r3p3Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r3p4Id.text.toString().toInt() - 1].answer == "true" && binding.r3p4.visibility == View.VISIBLE) {
//                    totalPlayerNameShow += 1
//                    binding.r3p4Txt.visibility = View.VISIBLE
//                    val countryID = allCpuPlayer[binding.r3p4Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r3p4Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r3p4Name.setTextColor(textColor)
//                    binding.r3p4Number.setTextColor(textColor)
//                }
//                if (allCpuPlayer[binding.r3p5Id.text.toString().toInt() - 1].answer == "true" && binding.r3p5.visibility == View.VISIBLE) {
//                    totalPlayerNameShow += 1
//                    binding. r3p5Txt.visibility = View.VISIBLE
//                    val countryID = allCpuPlayer[binding.r3p5Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r3p5Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r3p5Name.setTextColor(textColor)
//                    binding.r3p5Number.setTextColor(textColor)
//                }
//
//                //Shrawan
//                if (allCpuPlayer[binding.r0p1Id.text.toString().toInt() - 1].answer == "true" && binding.r0p1.visibility == View.VISIBLE) {
//                    totalPlayerNameShow += 1
//                    binding.r0p1Txt.visibility = View.VISIBLE
//                    val countryID = allCpuPlayer[binding.r0p1Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.r0p1Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.r0p1Name.setTextColor(textColor)
//                    binding.r0p1Number.setTextColor(textColor)
//                }
//
//                // cpu
//                if (allUserPlayer[binding.cpur1p1Id.text.toString().toInt() - 1].answer == "true" && binding.cpur1p1.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur1p1Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur1p1Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur1p1Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding. cpur1p1Name.setTextColor(textColor)
//                    binding.cpur1p1Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur1p2Id.text.toString().toInt() - 1].answer == "true" && binding.cpur1p2.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding. cpur1p2Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur1p2Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur1p2Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur1p2Name.setTextColor(textColor)
//                    binding.cpur1p2Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur1p3Id.text.toString().toInt() - 1].answer == "true" && binding.cpur1p3.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur1p3Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur1p3Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur1p3Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur1p3Name.setTextColor(textColor)
//                    binding.cpur1p3Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur1p4Id.text.toString().toInt() - 1].answer == "true" && binding.cpur1p4.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur1p4Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur1p4Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur1p4Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur1p4Name.setTextColor(textColor)
//                    binding.cpur1p4Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur1p5Id.text.toString().toInt() - 1].answer == "true" && binding.cpur1p5.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur1p5Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur1p5Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur1p5Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur1p5Name.setTextColor(textColor)
//                    binding.cpur1p5Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur2p1Id.text.toString().toInt() - 1].answer == "true" && binding.cpur2p1.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur2p1Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur2p1Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur2p1Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur2p1Name.setTextColor(textColor)
//                    binding.cpur2p1Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur2p2Id.text.toString().toInt() - 1].answer == "true" && binding.cpur2p2.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur2p2Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur2p2Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur2p2Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur2p2Name.setTextColor(textColor)
//                    binding.cpur2p2Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur2p3Id.text.toString().toInt() - 1].answer == "true" && binding.cpur2p3.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur2p3Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur2p3Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding. cpur2p3Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur2p3Name.setTextColor(textColor)
//                    binding.cpur2p3Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur2p4Id.text.toString().toInt() - 1].answer == "true" && binding.cpur2p4.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur2p4Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur2p4Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur2p4Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur2p4Name.setTextColor(textColor)
//                    binding.cpur2p4Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur2p5Id.text.toString().toInt() - 1].answer == "true" && binding.cpur2p5.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur2p5Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur2p5Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur2p5Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur2p5Name.setTextColor(textColor)
//                    binding.cpur2p5Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur3p1Id.text.toString().toInt() - 1].answer == "true" && binding.cpur3p1.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur3p1Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur3p1Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur3p1Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur3p1Name.setTextColor(textColor)
//                    binding.cpur3p1Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur3p2Id.text.toString().toInt() - 1].answer == "true" && binding.cpur3p2.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur3p2Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur3p2Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur3p2Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur3p2Name.setTextColor(textColor)
//                    binding.cpur3p2Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur3p3Id.text.toString().toInt() - 1].answer == "true" && binding.cpur3p3.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur3p3Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur3p3Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding. cpur3p3Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur3p3Name.setTextColor(textColor)
//                    binding.cpur3p3Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur3p4Id.text.toString().toInt() - 1].answer == "true" && binding.cpur3p4.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur3p4Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur3p4Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur3p4Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur3p4Name.setTextColor(textColor)
//                    binding.cpur3p4Number.setTextColor(textColor)
//                }
//                if (allUserPlayer[binding.cpur3p5Id.text.toString().toInt() - 1].answer == "true" && binding.cpur3p5.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur3p5Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur3p5Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur3p5Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur3p5Name.setTextColor(textColor)
//                    binding.cpur3p5Number.setTextColor(textColor)
//                }
//
//                //Shrawan
//                if (allUserPlayer[binding.cpur0p1Id.text.toString().toInt() - 1].answer == "true" && binding.cpur0p1.visibility == View.VISIBLE) {
//                    cpuPlayerNameShow += 1
//                    binding.cpur0p1Txt.visibility = View.VISIBLE
//                    val countryID = allUserPlayer[binding.cpur0p1Id.text.toString().toInt() - 1].country_id
//                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
//                    binding.cpur0p1Img.setImageResource(setGames.getTShirtImage(countryID))
//                    binding.cpur0p1Name.setTextColor(textColor)
//                    binding.cpur0p1Number.setTextColor(textColor)
//                }
//
//                playerSelect()
//            }
//        }catch (e:Exception){
//            Log.d("Error ","********"+e.message)
//        }
//    }
//
//    //This function is used for select player which term (User or cpu)
//    private fun playerSelect() {
//        Log.d("@@@Error ","cpuPlayerNameShow"+cpuPlayerNameShow)
//        Log.d("@@@Error ","totalPlayerNameShow"+totalPlayerNameShow)
//        if (!isTimerFinish) {
//            if (userType.equals("USER",true))
//            {
//
//                binding.userScreen.visibility = View.VISIBLE
//                binding.cpuScreen.visibility = View.GONE
//                binding.userName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
//                binding.opposeTeamPlayerName.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
//
//                binding.r1p1Img.setOnClickListener(this)
//                binding.r1p2Img.setOnClickListener(this)
//                binding.r1p3Img.setOnClickListener(this)
//                binding.r1p4Img.setOnClickListener(this)
//                binding.r1p5Img.setOnClickListener(this)
//
//                binding.r2p1Img.setOnClickListener(this)
//                binding.r2p2Img.setOnClickListener(this)
//                binding.r2p3Img.setOnClickListener(this)
//                binding.r2p4Img.setOnClickListener(this)
//                binding.r2p5Img.setOnClickListener(this)
//
//                binding.r3p1Img.setOnClickListener(this)
//                binding.r3p2Img.setOnClickListener(this)
//                binding.r3p3Img.setOnClickListener(this)
//                binding.r3p4Img.setOnClickListener(this)
//                binding.r3p5Img.setOnClickListener(this)
//
//                binding.r0p1Img.setOnClickListener(this)//Shrawan
//
//                binding.btShoot.setOnClickListener(this)
//                binding.btPass.setOnClickListener(this)
//
//
//                if (totalPlayerNameShow != 0) {
////                    selectPlayPlayer(sessionManager.getMySelectedTeamPlayerNum())
//                    binding.rootButton.visibility = View.VISIBLE
//                    /* if (totalPlayerNameShow == 1) {
//                         bt_shoot.visibility = View.GONE
//                     }*/
//                    //Shrawan
//                    if (totalPlayerNameShow == 1 || isGoalClick) {
//                        binding.btShoot.visibility = View.GONE
//                        if (isGoalClick){
//                            isGoalClick = false
//                        }
//                    }
//                }
//
//            }
//            else {
//                binding.cpuScreen.visibility = View.VISIBLE
//                binding.userScreen.visibility = View.GONE
//                binding.opposeTeamPlayerName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
//                binding.userName.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
//
//
//                if (cpuPlayerNameShow != 0) {
//                    // Generate a random number between 0 and 100 (inclusive)
//                    if (cpuPlayerNameShow==1){
//                        selectCpuPlayPlayer(sessionManager.getSelectedTeamPlayerNum())
//                    }else{
//                        val randomNumber = Random.nextInt(2, 10)
//                        // Check if the number is even or odd
//                        val result = if (randomNumber % 2 == 0) {
//                            "even"
//                        } else {
//                            "odd"
//                        }
//                        if (result.equals("even",true)){
//                            autoButtonSelectPassShoot()
//                        }else{
//                            selectCpuPlayPlayer(sessionManager.getSelectedTeamPlayerNum())
//                        }
//                    }
//                } else {
//                    autoButtonClick()
//                }
//
//            }
//        }
//    }
//
//    //This function is used for select player which term (User or cpu)
//    private fun selectPlayPlayer(playerNum: Int) {
//        if (!isTimerFinish) {
//            if (binding.r1p1.visibility == View.VISIBLE && binding.r1p1Txt.visibility == View.VISIBLE && binding.r1p1Id.text.toString().toInt() == playerNum) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r1p1Select, binding.r1p1Id)
//            }
//            if (binding.r1p2.visibility == View.VISIBLE && binding.r1p2Txt.visibility == View.VISIBLE && binding.r1p2Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r1p2Select, binding.r1p2Id)
//            }
//            if (binding.r1p3.visibility == View.VISIBLE && binding.r1p3Txt.visibility == View.VISIBLE && binding.r1p3Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r1p3Select, binding.r1p3Id)
//            }
//            if (binding.r1p4.visibility == View.VISIBLE && binding.r1p4Txt.visibility == View.VISIBLE && binding.r1p4Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r1p4Select, binding.r1p4Id)
//            }
//            if (binding.r1p5.visibility == View.VISIBLE && binding.r1p5Txt.visibility == View.VISIBLE && binding.r1p5Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r1p5Select, binding.r1p5Id)
//            }
//            if (binding.r2p1.visibility == View.VISIBLE && binding.r2p1Txt.visibility == View.VISIBLE && binding.r2p1Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r2p1Select, binding.r2p1Id)
//            }
//            if (binding.r2p2.visibility == View.VISIBLE && binding.r2p2Txt.visibility == View.VISIBLE && binding.r2p2Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r2p2Select, binding.r2p2Id)
//            }
//            if (binding.r2p3.visibility == View.VISIBLE && binding.r2p3Txt.visibility == View.VISIBLE && binding.r2p3Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r2p3Select, binding.r2p3Id)
//            }
//            if (binding.r2p4.visibility == View.VISIBLE && binding.r2p4Txt.visibility == View.VISIBLE && binding.r2p4Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r2p4Select, binding.r2p4Id)
//            }
//            if (binding.r2p5.visibility == View.VISIBLE && binding.r2p5Txt.visibility == View.VISIBLE && binding.r2p5Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r2p5Select, binding.r2p5Id)
//            }
//            if (binding.r3p1.visibility == View.VISIBLE && binding.r3p1Txt.visibility == View.VISIBLE && binding.r3p1Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r3p1Select, binding.r3p1Id)
//            }
//            if (binding.r3p2.visibility == View.VISIBLE && binding.r3p2Txt.visibility == View.VISIBLE && binding.r3p2Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r3p2Select, binding.r3p2Id)
//            }
//            if (binding.r3p3.visibility == View.VISIBLE && binding.r3p3Txt.visibility == View.VISIBLE && binding.r3p3Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r3p3Select, binding.r3p3Id)
//            }
//            if (binding.r3p4.visibility == View.VISIBLE && binding.r3p4Txt.visibility == View.VISIBLE && binding.r3p4Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r3p4Select, binding.r3p4Id)
//            }
//            if (binding.r3p5.visibility == View.VISIBLE && binding.r3p5Txt.visibility == View.VISIBLE && binding.r3p5Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.r3p5Select, binding.r3p5Id)
//            }
//            //Shrawan
//            if (binding.r0p1.visibility == View.VISIBLE &&
//                binding.r0p1Txt.visibility == View.VISIBLE && binding.r0p1Id.text.toString().toInt() == playerNum) {
//                selectionPower = true
//                Log.d("@@@@@@@@@@@","${binding.r0p1Id.text.toString().toInt()}  $playerNum")
//                showPlayerReachPowerUser(binding.r0p1Select, binding.r0p1Id)
//            }
//
//        }
//    }
//
//    //This function is used for when cpu term than start auto playing
//    private fun selectCpuPlayPlayer(playerNum: Int) {
//        if (!isTimerFinish) {
//
//            if (binding.cpur1p1.visibility == View.VISIBLE && binding.cpur1p1Txt.visibility == View.VISIBLE && binding.cpur1p1Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur1p1Select, binding.cpur1p1Id)
//            }
//            if (binding.cpur1p2.visibility == View.VISIBLE && binding.cpur1p2Txt.visibility == View.VISIBLE && binding.cpur1p2Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur1p2Select, binding.cpur1p2Id)
//            }
//            if (binding.cpur1p3.visibility == View.VISIBLE && binding.cpur1p3Txt.visibility == View.VISIBLE && binding.cpur1p3Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur1p3Select, binding.cpur1p3Id)
//            }
//            if (binding.cpur1p4.visibility == View.VISIBLE && binding.cpur1p4Txt.visibility == View.VISIBLE && binding.cpur1p4Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur1p4Select,binding.cpur1p4Id)
//            }
//            if (binding.cpur1p5.visibility == View.VISIBLE && binding.cpur1p5Txt.visibility == View.VISIBLE && binding.cpur1p5Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur1p5Select, binding.cpur1p5Id)
//            }
//            if (binding.cpur2p1.visibility == View.VISIBLE && binding.cpur2p1Txt.visibility == View.VISIBLE && binding.cpur2p1Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur2p1Select, binding.cpur2p1Id)
//            }
//            if (binding.cpur2p2.visibility == View.VISIBLE && binding.cpur2p2Txt.visibility == View.VISIBLE && binding.cpur2p2Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur2p2Select, binding.cpur2p2Id)
//            }
//            if (binding.cpur2p3.visibility == View.VISIBLE && binding.cpur2p3Txt.visibility == View.VISIBLE && binding.cpur2p3Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur2p3Select, binding.cpur2p3Id)
//            }
//            if (binding.cpur2p4.visibility == View.VISIBLE && binding.cpur2p4Txt.visibility == View.VISIBLE && binding.cpur2p4Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur2p4Select, binding.cpur2p4Id)
//            }
//            if (binding.cpur2p5.visibility == View.VISIBLE && binding.cpur2p5Txt.visibility == View.VISIBLE &&  binding.cpur2p5Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur2p5Select, binding.cpur2p5Id)
//            }
//            if (binding.cpur3p1.visibility == View.VISIBLE && binding.cpur3p1Txt.visibility == View.VISIBLE && binding.cpur3p1Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur3p1Select, binding.cpur3p1Id)
//            }
//            if (binding.cpur3p2.visibility == View.VISIBLE && binding.cpur3p2Txt.visibility == View.VISIBLE && binding.cpur3p2Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur3p2Select, binding.cpur3p2Id)
//            }
//            if (binding.cpur3p3.visibility == View.VISIBLE && binding.cpur3p3Txt.visibility == View.VISIBLE && binding.cpur3p3Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur3p3Select, binding.cpur3p3Id)
//            }
//            if (binding.cpur3p4.visibility == View.VISIBLE && binding.cpur3p4Txt.visibility == View.VISIBLE && binding.cpur3p4Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur3p4Select, binding.cpur3p4Id)
//            }
//            if (binding.cpur3p5.visibility == View.VISIBLE && binding.cpur3p5Txt.visibility == View.VISIBLE && binding.cpur3p5Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur3p5Select, binding.cpur3p5Id)
//            }
//
//            //Shrawan
//            if (binding.cpur0p1.visibility == View.VISIBLE && binding.cpur0p1Txt.visibility == View.VISIBLE && binding.cpur0p1Id.text.toString()
//                    .toInt() == playerNum
//            ) {
//                selectionPower = true
//                showPlayerReachPowerUser(binding.cpur0p1Select, binding.cpur0p1Id)
//            }
//            autoButtonClick()
//        }
//    }
//
//    //This function is used for display alert box (halftime, time over etc.)
//    private fun playAlertBox(drawableImg: Int, action: String) {
//        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.show_image_box)
//        val imgChange: ImageView = dialog.findViewById(R.id.img_change)
//
//        Glide.with(requireContext())
//            .load(drawableImg)
//            .into(imgChange)
//
//        if (sessionManager.getMatchType().equals("worldcup",true)){
//            Log.e("@@@Error","My Score "+sessionManager.getMyScore().toString())
//            Log.e("@@@Error","Cpu Score "+sessionManager.getCpuScore().toString())
//            Log.e("type ", "worldcup")
//            if (action.equals("timeOver",true)){
//                Log.e("@@@Error","Cpu Score timeOver"+sessionManager.getCpuScore().toString())
//                Handler(Looper.myLooper()!!).postDelayed({
//                    dialog.dismiss()
//                    sessionManager.setExtraTimeUser("timeOver")
//                    setCondition("timeOver")
//                }, 3000)
//            }else{
//                if (action.equals("FullTime",true)){
//                    Handler(Looper.myLooper()!!).postDelayed({
//                        dialog.dismiss()
//                        sessionManager.setExtraTimeUser("ExtraTime")
//                        playAlertBox(R.drawable.extra_time_img, "ExtraTime")
//                    }, 3000)
//                }
//                if (action.equals("ExtraTime",true)){
//                    Handler(Looper.myLooper()!!).postDelayed({
//                        totalTime = 900000L
//                        startTime = 0
//                        val screen = setGames.setScreen(sessionManager.getUserScreenType())
//                        val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
//                        if (sessionManager.isNetworkAvailable()) {
//                            cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
//                            myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
//                            cpuDbHelper.deleteAllPlayers()
//                            myPlayerDbHelper.deleteAllPlayers()
//                            extraPLayerDbHelper.deleteAllPlayers()
//                            getGuessTeamList(dialog, screen.r1.toString(), screen.r2.toString(), screen.r3.toString(), cpuScreen.r1.toString(), cpuScreen.r2.toString(), cpuScreen.r3.toString())
//                        } else {
//                            alertError(getString(R.string.no_internet))
//                        }
//                    }, 3000)
//                }
//                if (action.equals("halftime",true)){
//                    Handler(Looper.myLooper()!!).postDelayed({
//                        val screen = setGames.setScreen(sessionManager.getUserScreenType())
//                        val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
//                        if (sessionManager.isNetworkAvailable()) {
//                            cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
//                            myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
//                            cpuDbHelper.deleteAllPlayers()
//                            myPlayerDbHelper.deleteAllPlayers()
//                            extraPLayerDbHelper.deleteAllPlayers()
//                            dialog.dismiss()
//                            getGuessTeamList(dialog, screen.r1.toString(), screen.r2.toString(), screen.r3.toString(), cpuScreen.r1.toString(), cpuScreen.r2.toString(), cpuScreen.r3.toString())
//                        } else {
//                            alertError(getString(R.string.no_internet))
//                        }
//                    }, 3000)
//                }
//                if (action.equals("TimeHalf",true)){
//                    Handler(Looper.myLooper()!!).postDelayed({
//                        val screen = setGames.setScreen(sessionManager.getUserScreenType())
//                        val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
//                        if (sessionManager.isNetworkAvailable()) {
//                            cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
//                            myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
//                            cpuDbHelper.deleteAllPlayers()
//                            myPlayerDbHelper.deleteAllPlayers()
//                            extraPLayerDbHelper.deleteAllPlayers()
//                            getGuessTeamList(dialog, screen.r1.toString(), screen.r2.toString(), screen.r3.toString(), cpuScreen.r1.toString(), cpuScreen.r2.toString(), cpuScreen.r3.toString())
//                        } else {
//                            alertError(getString(R.string.no_internet))
//                        }
//                    }, 3000)
//                }
//            }
//        }else{
//
//            Log.e("type ", "Frendly")
//
//            if (action.equals("timeOver",true)){
//                Log.e("My Score ", sessionManager.getMyScore().toString())
//                Log.e("Cpu Score ", sessionManager.getCpuScore().toString())
//                Handler(Looper.myLooper()!!).postDelayed({
//                    dialog.dismiss()
//                    sessionManager.setExtraTimeUser("timeOver")
//                    setCondition("timeOver")
//                }, 3000)
//            }
//
//            if (action.equals("halftime",true)){
//                Handler(Looper.myLooper()!!).postDelayed({
//                    val screen = setGames.setScreen(sessionManager.getUserScreenType())
//                    val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
//                    if (sessionManager.isNetworkAvailable()) {
//                        cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
//                        myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
//                        cpuDbHelper.deleteAllPlayers()
//                        myPlayerDbHelper.deleteAllPlayers()
//                        extraPLayerDbHelper.deleteAllPlayers()
//                        getGuessTeamList(dialog, screen.r1.toString(), screen.r2.toString(), screen.r3.toString(), cpuScreen.r1.toString(), cpuScreen.r2.toString(), cpuScreen.r3.toString())
//                    } else {
//                        alertError(getString(R.string.no_internet))
//                    }
//                }, 3000)
//            }
//
//        }
//        dialog.show()
//    }
//
//
//    // This function is used for get guess player list from database api
//    private fun getGuessTeamList(dialog: Dialog?, defender: String, midfielder: String, attacker: String, cpuDefender: String, cpuMidFielder: String, cpuAttacker: String) {
//        val match_no = (sessionManager.getGameNumber()-1)
//        getGuessPlayerListViewmodel.getGuessPlayerList("$token", defender, midfielder, attacker, "", "",
//            match_no.toString())
//        getGuessPlayerListViewmodel.getGuessPlayerListResponse.observe(this) { response ->
//            Log.d("@@@Error ","Api response "+response)
//            if (response != null) {
//                if (response.isSuccessful) {
//                    val teamResponse = response.body()
//                    if (teamResponse != null) {
//                        if (teamResponse.data != null) {
//                            if (teamResponse.data.myplayer != null) {
//                                if (sessionManager.getMatchType().equals("worldcup",true)){
//                                    if (sessionManager.getExtraTime().equals("ExtraTime",true) || sessionManager.getExtraTime().equals("TimeHalf",true)){
//                                        totalTime=900000
//                                        startTime=0
//                                        sessionManager.saveTimer(0)
//                                        sessionManager.increaseTimer(0)
//                                    }else{
//                                        sessionManager.increaseTimer(120000)
//                                    }
//                                }else{
//                                    sessionManager.increaseTimer(120000)
//                                }
//                                var df = defender.toInt()
//                                var mf = midfielder.toInt()
//                                var fw = attacker.toInt()
//                                Log.e("Player", myPlayerDbHelper.getAllPlayers().size.toString())
//                                try {
//                                    // Defender
//                                    for (data in teamResponse.data.myplayer) {
//                                        if (data.is_captain == 1) {
//                                            sessionManager.setMyPlayerId(data.id)
//                                        }
//                                        val surnames = try {
//                                            data.name!!.split(" ").last()
//                                        } catch (e: Exception) {
//                                            "SYSTEM"
//                                        }
//                                        if (data.designation == "DF") {
//                                            if (df > 0) {
//                                                df -= 1
//                                                myPlayerDbHelper.addPlayer(
//                                                    surnames,
//                                                    data.is_captain.toString(),
//                                                    data.country_id.toString(),
//                                                    data.type.toString(),
//                                                    data.designation.toString(),
//                                                    data.jersey_number.toString(),
//                                                    "false",
//                                                    "false",
//                                                )
//                                            }
//                                        }
//                                    }
//                                    // MidFielder
//                                    for (data in teamResponse.data.myplayer) {
//                                        if (data.is_captain == 1) {
//                                            sessionManager.setMyPlayerId(data.id)
//                                        }
//                                        val surnames = try {
//                                            data.name!!.split(" ").last()
//                                        } catch (e: Exception) {
//                                            "SYSTEM"
//                                        }
//                                        if (data.designation == "MF") {
//                                            if (mf > 0) {
//                                                mf -= 1
//                                                myPlayerDbHelper.addPlayer(
//                                                    surnames,
//                                                    data.is_captain.toString(),
//                                                    data.country_id.toString(),
//                                                    data.type.toString(),
//                                                    data.designation.toString(),
//                                                    data.jersey_number.toString(),
//                                                    "false",
//                                                    "false",
//                                                )
//                                            }
//                                        }
//                                    }
//                                    //Striker
//                                    for (data in teamResponse.data.myplayer) {
//                                        if (data.is_captain == 1) {
//                                            sessionManager.setMyPlayerId(data.id)
//                                        }
//                                        val surnames = try {
//                                            data.name!!.split(" ").last()
//                                        } catch (e: Exception) {
//                                            "SYSTEM"
//                                        }
//                                        if (data.designation == "FW") {
//                                            if (fw > 0) {
//                                                fw -= 1
//                                                myPlayerDbHelper.addPlayer(
//                                                    surnames,
//                                                    data.is_captain.toString(),
//                                                    data.country_id.toString(),
//                                                    data.type.toString(),
//                                                    data.designation.toString(),
//                                                    data.jersey_number.toString(),
//                                                    "false",
//                                                    "false",
//                                                )
//                                            }
//                                        }
//                                    }
//
//                                    // Goalkeeper
//                                    //Shrawan
//                                    for (data in teamResponse.data.myplayer) {
//                                        if (data.is_captain == 1) {
//                                            sessionManager.setCpuPlayerId(data.id)
//                                        }
//                                        val surnames = try {
//                                            data.name!!.split(" ").last()
//                                        } catch (e: Exception) {
//                                            "SYSTEM"
//                                        }
//
//                                        if (data.designation == "GK") {
//                                            myPlayerDbHelper.addPlayer(
//                                                surnames,
//                                                data.is_captain.toString(),
//                                                data.country_id.toString(),
//                                                data.type.toString(),
//                                                data.designation.toString(),
//                                                data.jersey_number.toString(),
//                                                "false",
//                                                "false"
//                                            )
//                                        }
//                                    }
//                                } catch (e: Exception) {
//                                    Log.e("My Player Database Error", e.toString())
//                                }
//                            }
//                            if (teamResponse.data.cpuplayer != null) {
//                                var df = cpuDefender.toInt()
//                                var mf = cpuMidFielder.toInt()
//                                var fw = cpuAttacker.toInt()
//                                try {
//                                    // Defender
//                                    for (data in teamResponse.data.cpuplayer) {
//                                        if (data.is_captain == 1) {
//                                            sessionManager.setCpuPlayerId(data.id)
//                                        }
//                                        val surnames = try {
//                                            data.name!!.split(" ").last()
//                                        } catch (e: Exception) {
//                                            "SYSTEM"
//                                        }
//                                        if (data.designation == "DF") {
//                                            if (df > 0) {
//                                                df -= 1
//                                                cpuDbHelper.addPlayer(
//                                                    surnames,
//                                                    data.is_captain.toString(),
//                                                    data.country_id.toString(),
//                                                    data.type.toString(),
//                                                    data.designation.toString(),
//                                                    data.jersey_number.toString(),
//                                                    "false",
//                                                    "false",
//                                                )
//                                            }
//                                        }
//                                    }
//                                    // MidFielder
//                                    for (data in teamResponse.data.cpuplayer) {
//                                        if (data.is_captain == 1) {
//                                            sessionManager.setCpuPlayerId(data.id)
//                                        }
//                                        val surnames = try {
//                                            data.name!!.split(" ").last()
//                                        } catch (e: Exception) {
//                                            "SYSTEM"
//                                        }
//                                        if (data.designation == "MF") {
//                                            if (mf > 0) {
//                                                mf -= 1
//                                                cpuDbHelper.addPlayer(
//                                                    surnames,
//                                                    data.is_captain.toString(),
//                                                    data.country_id.toString(),
//                                                    data.type.toString(),
//                                                    data.designation.toString(),
//                                                    data.jersey_number.toString(),
//                                                    "false",
//                                                    "false",
//                                                )
//                                            }
//                                        }
//                                    }
//                                    //Striker
//                                    for (data in teamResponse.data.cpuplayer) {
//                                        if (data.is_captain == 1) {
//                                            sessionManager.setCpuPlayerId(data.id)
//                                        }
//                                        val surnames = try {
//                                            data.name!!.split(" ").last()
//                                        } catch (e: Exception) {
//                                            "SYSTEM"
//                                        }
//                                        if (data.designation == "FW") {
//                                            if (fw > 0) {
//                                                fw -= 1
//                                                cpuDbHelper.addPlayer(
//                                                    surnames,
//                                                    data.is_captain.toString(),
//                                                    data.country_id.toString(),
//                                                    data.type.toString(),
//                                                    data.designation.toString(),
//                                                    data.jersey_number.toString(),
//                                                    "false",
//                                                    "false",
//                                                )
//                                            }
//                                        }
//                                    }
//
//                                    // Goalkeeper
//                                    //Shrawan
//                                    for (data in teamResponse.data.cpuplayer) {
//                                        if (data.is_captain == 1) {
//                                            sessionManager.setCpuPlayerId(data.id)
//                                        }
//                                        val surnames = try {
//                                            data.name!!.split(" ").last()
//                                        } catch (e: Exception) {
//                                            "SYSTEM"
//                                        }
//
//                                        if (data.designation == "GK") {
//                                            cpuDbHelper.addPlayer(
//                                                surnames,
//                                                data.is_captain.toString(),
//                                                data.country_id.toString(),
//                                                data.type.toString(),
//                                                data.designation.toString(),
//                                                data.jersey_number.toString(),
//                                                "false",
//                                                "false"
//                                            )
//                                        }
//                                    }
//                                } catch (e: Exception) {
//                                    Log.e("My Player Database Error", e.toString())
//                                }
//                            }
//                            if (teamResponse.data.SubtitutePlyer != null) {
//                                try {
//                                    // Extra Player
//                                    for (data in teamResponse.data.SubtitutePlyer) {
//                                        val surnames = try {
//                                            data.name!!.split(" ").last()
//                                        } catch (e: Exception) {
//                                            "SYSTEM"
//                                        }
//                                        extraPLayerDbHelper.addPlayer(
//                                            surnames,
//                                            data.is_captain.toString(),
//                                            data.country_id.toString(),
//                                            data.type.toString(),
//                                            data.designation.toString(),
//                                            data.jersey_number.toString(),
//                                            "false",
//                                            "USER"
//                                        )
//                                    }
//                                } catch (e: Exception) {
//                                    Log.e("My Player Database Error", e.toString())
//                                }
//                            }
//                            checkAllPlayer(dialog)
//                        } else {
//                            sessionManager.alertError("Player list not found !")
//                        }
//                    } else {
//                        sessionManager.alertError(response.message().toString())
//                    }
//                } else {
//                    sessionManager.alertError(response.errorBody().toString())
//                }
//
//            } else {
//                sessionManager.alertError("Check Your Internet Connection")
//            }
//        }
//    }
//
//    // This function is used for check cpu and user team player list and verify
//    private fun checkAllPlayer(dialog: Dialog?) {
//        if (myPlayerDbHelper.getAllPlayers().size < 10) {
//            val remain = 10 - myPlayerDbHelper.getAllPlayers().size
//            if (remain > 1) {
//                for (i in 0 until remain) {
//                    myPlayerDbHelper.addPlayer(
//                        "SYSTEM",
//                        "0",
//                        "ENG",
//                        "no",
//                        "MF",
//                        "10",
//                        "false",
//                        "false"
//                    )
//                }
//            } else {
//                myPlayerDbHelper.addPlayer("SYSTEM", "0", "ENG", "no", "MF", "10", "false", "false")
//            }
//        }
//        if (cpuDbHelper.getAllPlayers().size < 10) {
//            val remain = 10 - cpuDbHelper.getAllPlayers().size
//            if (remain > 1) {
//                for (i in 0 until remain) {
//                    cpuDbHelper.addPlayer("ANDROID", "0", "ENG", "no", "MF", "10", "false", "false")
//                }
//            } else {
//                cpuDbHelper.addPlayer("ANDROID", "0", "ENG", "no", "MF", "10", "false", "false")
//            }
//        }
//        Log.e("My Team :", myPlayerDbHelper.getAllPlayers().toString())
//        Log.e("My Team Size :", myPlayerDbHelper.getAllPlayers().size.toString())
//        Log.e("CPU Team :", cpuDbHelper.getAllPlayers().toString())
//        Log.e("CPU Team Size :", cpuDbHelper.getAllPlayers().size.toString())
//        Log.e("Extra Team :", extraPLayerDbHelper.getAllPlayers().toString())
//        Log.e("Extra Team Size :", extraPLayerDbHelper.getAllPlayers().size.toString())
//
//        val bundle = Bundle()
//        if (userType.equals("CPU",true)) {
//            bundle.putString("userType", "USER")
//        } else {
//            bundle.putString("userType", "CPU")
//        }
//        findNavController().navigate(R.id.playScreenFragment, bundle)
//        Handler(Looper.myLooper()!!).postDelayed({
//            dialog?.dismiss()
////            setScreens()
//        }, 2000)
//    }
//
//    @SuppressLint("SetTextI18n")
//    fun alertError(msg: String) {
//        val dialog = Dialog(requireContext(), R.style.BottomSheetDialog)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.alertbox_error)
//        val layoutParams = WindowManager.LayoutParams()
//        layoutParams.copyFrom(dialog.window!!.attributes)
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
//        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
//        dialog.window!!.attributes = layoutParams
//        val tvTitle: TextView = dialog.findViewById(R.id.tv_title)
//        val btnOk: LinearLayout = dialog.findViewById(R.id.btn_ok)
//        val btText: TextView = dialog.findViewById(R.id.btText)
//        btText.text = "Retry"
//        tvTitle.text = msg
//        btnOk.setOnClickListener(View.OnClickListener {
//            dialog.dismiss()
//        })
//        dialog.show()
//    }
//
//    // This function call get last player textview id
//    private fun getTestUserShoot() {
//        val onSelectedScreen = if (userType.equals("USER",true)) {
//            sessionManager.getUserScreenType()
//        } else {
//            sessionManager.getCpuScreenType()
//        }
//        when (onSelectedScreen) {
//            "5-2-3" -> {
//                val textViews: MutableList<TextView> = mutableListOf(
//                    binding.r1p1Id,
//                    binding.r1p2Id,
//                    binding.r1p3Id,
//                    binding.r1p4Id,
//                    binding.r1p5Id,
//                    binding.r2p1Id,
//                    binding.r2p2Id,
//                    binding.r3p1Id,
//                    binding.r3p2Id,
//                    binding.r3p3Id,
//                    binding.r0p1Id //Shrawan
//                )
//                val textViewsCPU: MutableList<TextView> = mutableListOf(
//                    binding.cpur1p1Id,
//                    binding.cpur1p2Id,
//                    binding.cpur1p3Id,
//                    binding.cpur1p4Id,
//                    binding.cpur1p5Id,
//                    binding.cpur2p1Id,
//                    binding.cpur2p2Id,
//                    binding.cpur3p1Id,
//                    binding.cpur3p2Id,
//                    binding.cpur3p3Id,
//                    binding.cpur0p1Id //Shrawan
//                )
//                selectedPlayerNum = if (userType.equals("USER",true)) {
//                    textViews[playerIdUser - 1]
//                } else {
//                    textViewsCPU[playerIdUser - 1]
//                }
//            }  // Done
//            "5-4-1" -> {
//                val textViews: MutableList<TextView> = mutableListOf(
//                    binding.r1p1Id,
//                    binding.r1p2Id,
//                    binding.r1p3Id,
//                    binding.r1p4Id,
//                    binding.r1p5Id,
//                    binding.r2p1Id,
//                    binding.r2p2Id,
//                    binding.r2p3Id,
//                    binding.r2p4Id,
//                    binding.r3p1Id,
//                    binding.r0p1Id //Shrawan
//                )
//                val textViewsCPU: MutableList<TextView> = mutableListOf(
//                    binding.cpur1p1Id,
//                    binding.cpur1p2Id,
//                    binding.cpur1p3Id,
//                    binding.cpur1p4Id,
//                    binding.cpur1p5Id,
//                    binding.cpur2p1Id,
//                    binding.cpur2p2Id,
//                    binding.cpur2p3Id,
//                    binding.cpur2p4Id,
//                    binding.cpur3p1Id,
//                    binding.cpur0p1Id //Shrawan
//                )
//                selectedPlayerNum = if (userType.equals("USER",true)) {
//                    textViews[playerIdUser - 1]
//                } else {
//                    textViewsCPU[playerIdUser - 1]
//                }
//            }  // Done
//            "5-3-2" -> {
//                val textViews: MutableList<TextView> = mutableListOf(
//                    binding.r1p1Id,
//                    binding.r1p2Id,
//                    binding.r1p3Id,
//                    binding.r1p4Id,
//                    binding.r1p5Id,
//                    binding.r2p1Id,
//                    binding.r2p2Id,
//                    binding.r2p3Id,
//                    binding.r3p1Id,
//                    binding.r3p2Id,
//                    binding.r0p1Id //Shrawan
//                )
//                val textViewsCPU: MutableList<TextView> = mutableListOf(
//                    binding.cpur1p1Id,
//                    binding.cpur1p2Id,
//                    binding.cpur1p3Id,
//                    binding.cpur1p4Id,
//                    binding.cpur1p5Id,
//                    binding.cpur2p1Id,
//                    binding.cpur2p2Id,
//                    binding.cpur2p3Id,
//                    binding.cpur3p1Id,
//                    binding.cpur3p2Id,
//                    binding.cpur0p1Id //Shrawan
//                )
//                selectedPlayerNum = if (userType.equals("USER",true)) {
//                    textViews[playerIdUser - 1]
//                } else {
//                    textViewsCPU[playerIdUser - 1]
//                }
//            }  // Done
//            "3-5-2" -> {
//                val textViews: MutableList<TextView> = mutableListOf(
//                    binding.r1p1Id,
//                    binding.r1p2Id,
//                    binding.r1p3Id,
//                    binding.r2p1Id,
//                    binding.r2p2Id,
//                    binding.r2p3Id,
//                    binding.r2p4Id,
//                    binding.r2p5Id,
//                    binding.r3p1Id,
//                    binding.r3p2Id,
//                    binding.r0p1Id //Shrawan
//                )
//                val textViewsCPU: MutableList<TextView> = mutableListOf(
//                    binding.cpur1p1Id,
//                    binding.cpur1p2Id,
//                    binding.cpur1p3Id,
//                    binding.cpur2p1Id,
//                    binding.cpur2p2Id,
//                    binding.cpur2p3Id,
//                    binding.cpur2p4Id,
//                    binding.cpur2p5Id,
//                    binding.cpur3p1Id,
//                    binding.cpur3p2Id,
//                    binding.cpur0p1Id //Shrawan
//                )
//                selectedPlayerNum = if (userType.equals("USER",true)) {
//                    textViews[playerIdUser - 1]
//                } else {
//                    textViewsCPU[playerIdUser - 1]
//                }
//            }  // Done
//            "4-5-1" -> {
//                val textViews: MutableList<TextView> = mutableListOf(
//                    binding.r1p1Id,
//                    binding.r1p2Id,
//                    binding.r1p3Id,
//                    binding.r1p4Id,
//                    binding.r2p1Id,
//                    binding.r2p2Id,
//                    binding.r2p3Id,
//                    binding.r2p4Id,
//                    binding.r2p5Id,
//                    binding.r3p1Id,
//                    binding.r0p1Id //Shrawan
//                )
//                val textViewsCPU: MutableList<TextView> = mutableListOf(
//                    binding.cpur1p1Id,
//                    binding.cpur1p2Id,
//                    binding.cpur1p3Id,
//                    binding.cpur1p4Id,
//                    binding.cpur2p1Id,
//                    binding.cpur2p2Id,
//                    binding.cpur2p3Id,
//                    binding.cpur2p4Id,
//                    binding.cpur2p5Id,
//                    binding.cpur3p1Id,
//                    binding.cpur0p1Id //Shrawan
//                )
//                selectedPlayerNum = if (userType.equals("USER",true)) {
//                    textViews[playerIdUser - 1]
//                } else {
//                    textViewsCPU[playerIdUser - 1]
//                }
//            }  // Done
//            "4-4-2" -> {
//                val textViews: MutableList<TextView> = mutableListOf(
//                    binding.r1p1Id,
//                    binding.r1p2Id,
//                    binding.r1p3Id,
//                    binding.r1p4Id,
//                    binding.r2p1Id,
//                    binding.r2p2Id,
//                    binding.r2p3Id,
//                    binding.r2p4Id,
//                    binding.r3p1Id,
//                    binding.r3p2Id,
//                    binding.r0p1Id //Shrawan
//                )
//                val textViewsCPU: MutableList<TextView> = mutableListOf(
//                    binding.cpur1p1Id,
//                    binding.cpur1p2Id,
//                    binding.cpur1p3Id,
//                    binding.cpur1p4Id,
//                    binding.cpur2p1Id,
//                    binding.cpur2p2Id,
//                    binding.cpur2p3Id,
//                    binding.cpur2p4Id,
//                    binding.cpur3p1Id,
//                    binding.cpur3p2Id,
//                    binding.cpur0p1Id //Shrawan
//                )
//                selectedPlayerNum = if (userType.equals("USER",true)) {
//                    textViews[playerIdUser - 1]
//                } else {
//                    textViewsCPU[playerIdUser - 1]
//                }
//            }  // Done
//            "4-3-3" -> {
//                val textViews: MutableList<TextView> = mutableListOf(
//                    binding.r1p1Id,
//                    binding.r1p2Id,
//                    binding.r1p3Id,
//                    binding.r1p4Id,
//                    binding.r2p1Id,
//                    binding.r2p2Id,
//                    binding.r2p3Id,
//                    binding.r3p1Id,
//                    binding.r3p2Id,
//                    binding.r3p3Id,
//                    binding.r0p1Id //Shrawan
//                )
//                val textViewsCPU: MutableList<TextView> = mutableListOf(
//                    binding.cpur1p1Id,
//                    binding.cpur1p2Id,
//                    binding.cpur1p3Id,
//                    binding.cpur1p4Id,
//                    binding.cpur2p1Id,
//                    binding.cpur2p2Id,
//                    binding.cpur2p3Id,
//                    binding.cpur3p1Id,
//                    binding.cpur3p2Id,
//                    binding.cpur3p3Id,
//                    binding.cpur0p1Id //Shrawan
//                )
//                selectedPlayerNum = if (userType.equals("USER",true)) {
//                    textViews[playerIdUser - 1]
//                } else {
//                    textViewsCPU[playerIdUser - 1]
//                }
//            }  // Done
//            "3-4-3" -> {
//                val textViews: MutableList<TextView> = mutableListOf(
//                    binding.r1p1Id,
//                    binding.r1p2Id,
//                    binding.r1p3Id,
//                    binding.r2p1Id,
//                    binding.r2p2Id,
//                    binding.r2p3Id,
//                    binding.r2p4Id,
//                    binding.r3p1Id,
//                    binding.r3p2Id,
//                    binding.r3p3Id,
//                    binding.r0p1Id //Shrawan
//                )
//                val textViewsCPU: MutableList<TextView> = mutableListOf(
//                    binding.cpur1p1Id,
//                    binding.cpur1p2Id,
//                    binding.cpur1p3Id,
//                    binding.cpur2p1Id,
//                    binding.cpur2p2Id,
//                    binding.cpur2p3Id,
//                    binding.cpur2p4Id,
//                    binding.cpur3p1Id,
//                    binding.cpur3p2Id,
//                    binding.cpur3p3Id,
//                    binding.cpur0p1Id //Shrawan
//                )
//                selectedPlayerNum = if (userType.equals("USER",true)) {
//                    textViews[playerIdUser - 1]
//                } else {
//                    textViewsCPU[playerIdUser - 1]
//                }
//            }  // Done
//            "4-2-4" -> {
//                val textViews: MutableList<TextView> = mutableListOf(
//                    binding.r1p1Id,
//                    binding.r1p2Id,
//                    binding.r1p3Id,
//                    binding.r1p4Id,
//                    binding.r2p1Id,
//                    binding.r2p2Id,
//                    binding.r3p1Id,
//                    binding.r3p2Id,
//                    binding.r3p3Id,
//                    binding.r3p4Id,
//                    binding.r0p1Id //Shrawan
//                )
//                val textViewsCPU: MutableList<TextView> = mutableListOf(
//                    binding.cpur1p1Id,
//                    binding.cpur1p2Id,
//                    binding.cpur1p3Id,
//                    binding.cpur1p4Id,
//                    binding.cpur2p1Id,
//                    binding.cpur2p2Id,
//                    binding.cpur3p1Id,
//                    binding.cpur3p2Id,
//                    binding.cpur3p3Id,
//                    binding.cpur3p4Id,
//                    binding.cpur0p1Id //Shrawan
//                )
//                selectedPlayerNum = if (userType.equals("USER",true)) {
//                    textViews[playerIdUser - 1]
//                } else {
//                    textViewsCPU[playerIdUser - 1]
//                }
//            }  // Done
//            else -> {
//                Toast.makeText(requireContext(), "Formation is not Exist $onSelectedScreen", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    // This function is used for display the alert box of world cup is won or loss
//    private fun winAlertBox(int: Int) {
//        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.show_image_box)
//        val imgChange: ImageView = dialog.findViewById(R.id.img_change)
//        sessionManager.changeMusic(21, 0)
//        if (int==0){
//            imgChange.setImageResource(R.drawable.eliminated_img)
//        }
//        if (int==2){
//            imgChange.setImageResource(R.drawable.winner_img)
//        }
//        if (int==1){
//            imgChange.setImageResource(R.drawable.penalties_img)
//        }
//        if (int==3){
//            imgChange.setImageResource(R.drawable.winner_img)
//        }
//        moveToScoreFragment(int,dialog)
//        dialog.show()
//    }
//
//    private fun moveToScoreFragment(moveType:Int,dialog:Dialog){
//        val cpuName = binding.opposeTeamPlayerName.text.toString()
//        val myName = binding.userName.text.toString()
//        Handler(Looper.myLooper()!!).postDelayed({
//            dialog.dismiss()
//            if (moveType==0){
//                if (sessionManager.getMatchType().equals("worldcup",true)){
//                    val bundle = Bundle()
//                    bundle.putString("opposeTeamName", cpuName)
//                    bundle.putString("myTeamName", myName)
//                    findNavController().navigate(R.id.score_fragment, bundle)
//                }else{
//                    sessionManager.resetScore()
//                    sessionManager.resetGameNumberScore()
//                    val intent=Intent(requireContext(),MainActivity::class.java)
//                    startActivity(intent)
//                    requireActivity().finish()
//                }
//            }
//            if (moveType==1){
//                val bundle = Bundle()
//                bundle.putString("type", "main")
//                findNavController().navigate(R.id.selectTossFragment, bundle)
//            }
//            if (moveType==2){
//                sessionManager.resetScore()
//                sessionManager.resetGameNumberScore()
//                val intent=Intent(requireContext(),MainActivity::class.java)
//                startActivity(intent)
//                requireActivity().finish()
//            }
//
//            if (moveType==3){
//                val bundle = Bundle()
//                bundle.putString("opposeTeamName", cpuName)
//                bundle.putString("myTeamName", myName)
//                findNavController().navigate(R.id.score_fragment, bundle)
//            }
//        }, 3000)
//
//    }
//    override fun onDestroy() {
//        super.onDestroy()
//        onDestroyAndOnStop()
//        Log.e("@@@Error","Distro Timer Hold")
//    }
//    override fun onStop() {
//        onDestroyAndOnStop()
//        Log.e("@@@Error","Stop TimerHold")
//        super.onStop()
//    }
//    private  fun onDestroyAndOnStop(){
//        if (isTimerRunning) {
//            Log.d("@@@Error", "stop or destroy$startTime")
//            sessionManager.saveTimer(startTime)
////            wholeTimer.cancel()
//            isTimerRunning = false
//            stopCpuProcess()
//        }
//    }
//}
