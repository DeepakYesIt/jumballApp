package com.yesitlabs.jumballapp.fragment.manfragment
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.Space
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.ValueStore
import com.yesitlabs.jumballapp.activity.MainActivity
import com.yesitlabs.jumballapp.database.player_dtl.CPUPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.ExtraPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerModel
import com.yesitlabs.jumballapp.database.team_dtl.TeamDatabaseHelper
import com.yesitlabs.jumballapp.databinding.FragmentPlayerUserCPUBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.gameRule.SetGames
import com.yesitlabs.jumballapp.model.GuessPlayerListResp
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.viewmodeljumball.PlayerListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

/*
 * Developed by: Deepak Kumar Agrahari
 * Purpose: Implements conditional navigation based on player attributes and quiz responses.
 */

@AndroidEntryPoint
class PlayerUserCPUFragment :Fragment() , View.OnClickListener{
    private lateinit var viewmodel: PlayerListViewModel
    private lateinit var binding: FragmentPlayerUserCPUBinding
    lateinit var sessionManager: SessionManager
    private lateinit var extraPLayerDbHelper: ExtraPlayerDatabaseHelper
    var totalTime = 5400000L
    var startTime = 0
    private var myPass = 0
    private var cpuPass = 0
    private var userType: String? = null
    private var playerIdUser = 0
    private var  isGoalClick  = false
    private lateinit var cpuDbHelper: CPUPlayerDatabaseHelper
    private lateinit var myPlayerDbHelper: PlayerDatabaseHelper
    private lateinit var teamDbHelper: TeamDatabaseHelper
    private var allCpuPlayer = ArrayList<PlayerModel>()
    private var allUserPlayer = ArrayList<PlayerModel>()
    private var setGames: SetGames = SetGames()
    private var isCpuActive = false
    // List to store clickable players and their views
    private val clickablePlayers = mutableListOf<Pair<View, PlayerModel>>()
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if (isCpuActive) {
                selectCpuButton()
                handler.postDelayed(this, 3000) // Repeat every 3 seconds
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlayerUserCPUBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        extraPLayerDbHelper = ExtraPlayerDatabaseHelper(requireContext())
        viewmodel = ViewModelProvider(requireActivity())[PlayerListViewModel::class.java]
        setTimerLogic()
        Log.d("@@@Error ", "getExtraTime"+sessionManager.getExtraTime())
        Log.d("@@@Error ", "sessionManager.getTimer"+sessionManager.getTimer())
        Log.d("@@@Error ", "match count"+sessionManager.getGameNumber())
        Log.d("@@@Error ", "timer count"+sessionManager.getTimer())
        Log.d("@@@Error ", "startTime$startTime")
        Log.d("@@@Error ", "totalTime$totalTime")
        Log.d("@@@Error ", "myPass$myPass")
        Log.d("@@@Error ", "cpuPass$cpuPass")
        Log.d("@@@Error ", "userType$userType")
        cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
        myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
        timerLogic()
        setPlayerScreens()
        backButton()
        binding.btShoot.setOnClickListener(this)
        binding.btPass.setOnClickListener(this)
    }
    private fun setTimerLogic(){
        myPass = sessionManager.getMyPass()
        cpuPass = sessionManager.getCpuPass()
        if (arguments != null) {
            userType = requireArguments().getString("userType").toString().uppercase(Locale.ROOT)
            playerIdUser = requireArguments().getInt("id")
            isGoalClick = requireArguments().getBoolean("isGoalClick",false)//Shrawan
        } else {
            Log.e("Argument", "No")
        }
        if (sessionManager.getExtraTime().equals("ExtraTime",true) || sessionManager.getExtraTime().equals("TimeHalf",true)){
            binding.layProgess.max = 900000
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
            binding.layProgess.max = 2700000
            if (sessionManager.getTimer() != 0) {
                startTime = sessionManager.getTimer()
                if (startTime>=5400000){
                    totalTime=5400000
                }else{
                    totalTime -= sessionManager.getTimer().toLong()
                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setPlayerScreens(){
        allCpuPlayer = cpuDbHelper.getAllPlayers()
        allUserPlayer = myPlayerDbHelper.getAllPlayers()
        Log.e("@@@Error ", "All Cpu Player $allCpuPlayer")
        Log.e("@@@Error ", "All User Player $allUserPlayer")
        Log.e("@@@Error ","All name "+sessionManager.getName().toString())
        if (sessionManager.getMatchType().equals("worldcup",true)) {
            teamDbHelper = TeamDatabaseHelper(requireContext())
            val allTeam = teamDbHelper.getAllTeams()
            for (data in allTeam) {
                if (data.teamID == 1) {
                    binding.userName.text = data.captainName
                }
                if(sessionManager.getGameNumber() <= 3)  { //for first 3 match
                    binding.opposeTeamPlayerName.text = data.captainName
                }else if(sessionManager.getGameNumber() == 4){ // for 4th match
                    binding.opposeTeamPlayerName.text = "Asst. Manager"
                }else if(sessionManager.getGameNumber() == 5){ // for 5th match
                    binding.opposeTeamPlayerName.text = "Lizard mascot"
                }
            }
        } else {
            binding.userName.text = sessionManager.getName()?.split(" ")?.drop(1)?.joinToString(" ")
            binding.opposeTeamPlayerName.text = "CPU"
        }
        binding.cpuScoreTv.text = sessionManager.getCpuScore().toString()
        binding.userGoalTv.text = sessionManager.getMyScore().toString()
        if (userType.equals("USER",true)){
            binding.userName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.opposeTeamPlayerName.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            stopCpuProcess()
            setupFootballFormation(sessionManager.getUserScreenType(),allCpuPlayer)
        }else{
            binding.opposeTeamPlayerName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.userName.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            if (sessionManager.getLifeLineStatus1().equals("Yes",true)){
                sessionManager.setLifeLineStatus1("No")
                allUserPlayer.forEach { player ->
                    val isDF = player.designation.equals("DF", ignoreCase = true)
                    player.use = isDF.toString()
                    player.answer = "false"
                }
            }
            startCpuProcess()
            setupFootballFormationCpu(sessionManager.getUserScreenType(),allUserPlayer)
        }
    }
    private fun selectCpuButton(){
        if (clickablePlayers.isNotEmpty()) {
            val countAnswer = allUserPlayer.count { it.answer.equals("true",true) }?:0
            val select = allUserPlayer.count { it.use.equals("true",true) }?:0
            val randomTarget =  (1..11).random().toString()
            val randomTargetLifeLine =  (1..5).random().toString()
            Log.d("@@@Error", "Random number$randomTarget")
            Log.d("@@@Error", "countAnswer $countAnswer")
            Log.d("@@@Error", "select $select")
            if (countAnswer==0){
                if (select == 0){
                    val targetPlayer = allUserPlayer.find { it.id == randomTarget && it.designation.equals("MF",true) }
                    if (targetPlayer != null) {
                        stopCpuProcess()
                        moveToPlayNameScreen(targetPlayer)
                    }
                }else{
                    val targetPlayer = allUserPlayer.find { it.id==randomTargetLifeLine }
                    if (targetPlayer != null) {
                        if (targetPlayer.use.equals("true",true)){
                            if (targetPlayer.answer.equals("false",true)){
                                moveToPlayNameScreen(targetPlayer)
                            }
                        }
                    }
                }
            }else{
                val countSelect = allUserPlayer.count { it.use.equals("true",true) }?:0
                if (countSelect==0){
                    stopCpuProcess()
                    cpuPassBall()
                }else{
                    if (countAnswer>1){
                        val result = if (randomTarget.toInt() % 2 == 0) { "even" } else { "odd" }
                        if (result.equals("even",true)){
                            val targetPlayer = allUserPlayer.find {
                                it.id==randomTarget
                            }
                            if (targetPlayer != null) {
                                Log.d("@@@Error", "******$targetPlayer")
                                if (targetPlayer.use.equals("true",true)){
                                    if (targetPlayer.answer.equals("false",true)){
                                        moveToPlayNameScreen(targetPlayer)
                                    }
                                }
                            }
                        }else{
                            cpuSelectLogic()
                        }
                    }else{
                        cpuSelectLogic()
                    }
                }
            }
        }
    }
    private fun moveToPlayNameScreen(targetPlayer: PlayerModel) {
        val bundle = Bundle()
        bundle.putString("Name", targetPlayer.name)
        bundle.putString("userType", userType)
        bundle.putString("Num", targetPlayer.jersey_number)
        bundle.putString("id", targetPlayer.id)
        Log.e("Send Detail of Quiz", userType + " " + targetPlayer.name + " " + targetPlayer.jersey_number)
        findNavController().navigate(R.id.player_name_guess, bundle)
    }
    private fun cpuSelectLogic(){
        stopCpuProcess()
        val playerId=sessionManager.getSelectedTeamPlayerNum()
        val player = allUserPlayer.find { it.id.toInt() == playerId }
        if (!player?.designation.equals("GK",true)){
            val bundle = Bundle()
            bundle.putString("userType", "USER")
            bundle.putInt("selected_player_num", playerId)
            var size = 0
            for (data in allUserPlayer) {
                if (data.id.toInt() == playerId) {
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
            Log.d("******", "play screen  :=$num")
            bundle.putInt("select_box", num)
            bundle.putInt("size", size)
            Log.e("Shoot to Kick", bundle.toString())
            findNavController().navigate(R.id.goal_keeper_Screen, bundle)
        }
    }
    private fun timerLogic(){
        val min = startTime / 60000
        binding.tvCount.text = "‘$min"
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
                    setCondition("ExtraTime")
                }
            }
            if (sessionManager.getExtraTime().equals("TimeHalf",true)){
                if (startTime >= 900000){
                    Log.d("@@@Error","when FinalTime time set")
                    totalTime = 900000L
                    startTime = 0
                    sessionManager.setExtraTimeUser("TimeHalfEnd")
                    setCondition("TimeHalfEnd")
                }
            }
            if (startTime <= 900000) {
                binding.layProgess.progress = startTime
            } else {
                binding.layProgess.progress = startTime - 900000
            }

        }else{
            Log.e("@@@Error", "Full getValue "+ ValueStore.getValue1())
            if (startTime >= 5400000 && ValueStore.getValue1() ==0){
                ValueStore.setValue1(1)
                Log.d("@@@Error","when FullTime time set")
                Log.e("@@@Error", "Full time")
                sessionManager.changeMusic(6, 0)
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
                    Log.d("@@@Error","when half time start")
                    Log.e("Half Time", "Yaa hui")
                    sessionManager.changeMusic(6, 0)
                    sessionManager.setExtraTimeUser("halftime")
                    playAlertBox(R.drawable.half_time_img, "halftime")
                }
            }
            if(startTime <= 2700000) {
                binding.layProgess.progress = startTime
            }else {
                binding.layProgess.progress = startTime - 2700000
            }
        }
    }
    private fun playAlertBox(drawableImg: Int, action: String) {
        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView = dialog.findViewById(R.id.img_change)
        Glide.with(requireContext())
            .load(drawableImg)
            .into(imgChange)
        Log.e("@@@Error","My Score "+sessionManager.getMyScore().toString())
        Log.e("@@@Error","Cpu Score "+sessionManager.getCpuScore().toString())
        if (sessionManager.getMatchType().equals("worldcup",true)){
            if (action.equals("timeOver",true)){
                Handler(Looper.myLooper()!!).postDelayed({
                    dialog.dismiss()
                    sessionManager.setExtraTimeUser("timeOver")
                    setCondition("timeOver")
                }, 3000)
            }else{
                when (action) {
                    "FullTime" -> {
                        Handler(Looper.myLooper()!!).postDelayed({
                            dialog.dismiss()
                            sessionManager.setExtraTimeUser("ExtraTime")
                            playAlertBox(R.drawable.extra_time_img, "ExtraTime")
                        }, 3000)
                    }
                    "ExtraTime" -> {
                        Handler(Looper.myLooper()!!).postDelayed({
                            totalTime = 900000L
                            startTime = 0
                            apiCall(dialog)
                        }, 3000)
                    }
                    "halftime", "TimeHalf" -> {
                        commonApi(dialog)
                    }
                }
            }
        }else{
            if (action.equals("timeOver",true)){
                Handler(Looper.myLooper()!!).postDelayed({
                    dialog.dismiss()
                    sessionManager.setExtraTimeUser("timeOver")
                    setCondition("timeOver")
                }, 3000)
            }
            if (action.equals("halftime",true)){
                commonApi(dialog)
            }
        }
        dialog.show()
    }
    private fun commonApi(dialog: Dialog) {
        Handler(Looper.myLooper()!!).postDelayed({
            apiCall(dialog)
        }, 3000)
    }
    private fun setCondition(status:String){
        Log.e("@@@Error", "status $status")
        Log.e("@@@Error","getMyScore "+sessionManager.getMyScore())
        Log.e("@@@Error","getCpuScore "+sessionManager.getCpuScore())
        if (sessionManager.getMatchType().equals("worldcup",true)){
            if (status.equals("timeOver",true)){
                if (sessionManager.getMyScore() == sessionManager.getCpuScore()){
                    moveToScoreScreen()
                }else if (sessionManager.getMyScore() > sessionManager.getCpuScore()){
                    if (sessionManager.getGameNumber()>=3){
                        winAlertBox(3)
                    }else{
                        moveToScoreScreen()
                    }
                }else{
                    moveToScoreScreen()
                }
            }
        }else{
            if (sessionManager.getMyScore() <= sessionManager.getCpuScore()){
                moveToScoreScreen()
            }else{
                winAlertBox(2)
            }
        }
    }
    private fun winAlertBox(int: Int) {
        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView = dialog.findViewById(R.id.img_change)
        sessionManager.changeMusic(21, 0)
        when (int) {
            0 -> imgChange.setImageResource(R.drawable.eliminated_img)
            1 -> imgChange.setImageResource(R.drawable.penalties_img)
            2, 3 -> imgChange.setImageResource(R.drawable.winner_img)
        }
        moveToScoreFragment(int,dialog)
        dialog.show()
    }
    private fun moveToScoreFragment(moveType:Int,dialog:Dialog){
        Handler(Looper.myLooper()!!).postDelayed({
            dialog.dismiss()
            if (moveType==0){
                if (sessionManager.getMatchType().equals("worldcup",true)){
                    moveToScoreScreen()
                }else{
                    sessionManager.resetScore()
                    sessionManager.resetGameNumberScore()
                    val intent= Intent(requireContext(), MainActivity::class.java)
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
                val intent= Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            if (moveType==3){
                moveToScoreScreen()
            }
        }, 3000)

    }
    private fun moveToScoreScreen(){
        val cpuName = binding.opposeTeamPlayerName.text.toString()
        val myName = binding.userName.text.toString()
        val bundle = Bundle()
        bundle.putString("opposeTeamName", cpuName)
        bundle.putString("myTeamName", myName)
        findNavController().navigate(R.id.score_fragment, bundle)
    }
    private fun apiCall(dialog: Dialog) {
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
            sessionManager.alertError(ErrorMessage.netWorkError)
        }
    }
    // This function is used for get guess player list from database api
    private fun getGuessTeamList(dialog: Dialog?, defender: String, midfielder: String, attacker: String, cpuDefender: String, cpuMidFielder: String, cpuAttacker: String) {
        val matchNo = (sessionManager.getGameNumber()-1)
//        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewmodel.getGuessPlayerList({
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val model = gson.fromJson(it.data, GuessPlayerListResp::class.java)
                            if (model.code == 200 && model.success) {
                                try {
                                    model.data?.let { data->
                                        if (data.myplayer != null) {
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
                                                for (data in data.myplayer) {
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
                                                            myPlayerDbHelper.addPlayer(surnames, data.is_captain.toString(), data.country_id.toString(), data.type.toString(), data.designation.toString(), data.jersey_number.toString(), "false", "false",)
                                                        }
                                                    }
                                                }
                                                // MidFielder
                                                for (data in data.myplayer) {
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
                                                            myPlayerDbHelper.addPlayer(surnames, data.is_captain.toString(), data.country_id.toString(), data.type.toString(), data.designation.toString(), data.jersey_number.toString(), "false", "false",)
                                                        }
                                                    }
                                                }
                                                //Striker
                                                for (data in data.myplayer) {
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
                                                            myPlayerDbHelper.addPlayer(surnames, data.is_captain.toString(), data.country_id.toString(), data.type.toString(), data.designation.toString(), data.jersey_number.toString(), "false", "false",)
                                                        }
                                                    }
                                                }

                                                // Goalkeeper
                                                //Shrawan
                                                for (data in data.myplayer) {
                                                    if (data.is_captain == 1) {
                                                        sessionManager.setCpuPlayerId(data.id)
                                                    }
                                                    val surnames = try {
                                                        data.name!!.split(" ").last()
                                                    } catch (e: Exception) {
                                                        "SYSTEM"
                                                    }
                                                    if (data.designation == "GK") {
                                                        myPlayerDbHelper.addPlayer(surnames, data.is_captain.toString(), data.country_id.toString(), data.type.toString(), data.designation.toString(), data.jersey_number.toString(), "false", "false")
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                Log.e("My Player Database Error", e.toString())
                                            }
                                        }
                                        if (data.cpuplayer != null) {
                                            var df = cpuDefender.toInt()
                                            var mf = cpuMidFielder.toInt()
                                            var fw = cpuAttacker.toInt()
                                            try {
                                                // Defender
                                                for (data in data.cpuplayer) {
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
                                                            cpuDbHelper.addPlayer(surnames, data.is_captain.toString(), data.country_id.toString(), data.type.toString(), data.designation.toString(), data.jersey_number.toString(), "false", "false",)
                                                        }
                                                    }
                                                }
                                                // MidFielder
                                                for (data in data.cpuplayer) {
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
                                                            cpuDbHelper.addPlayer(surnames, data.is_captain.toString(), data.country_id.toString(), data.type.toString(), data.designation.toString(), data.jersey_number.toString(), "false", "false",)
                                                        }
                                                    }
                                                }
                                                //Striker
                                                for (data in data.cpuplayer) {
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
                                                            cpuDbHelper.addPlayer(surnames, data.is_captain.toString(), data.country_id.toString(), data.type.toString(), data.designation.toString(), data.jersey_number.toString(), "false", "false",)
                                                        }
                                                    }
                                                }

                                                // Goalkeeper
                                                //Shrawan
                                                for (data in data.cpuplayer) {
                                                    if (data.is_captain == 1) {
                                                        sessionManager.setCpuPlayerId(data.id)
                                                    }
                                                    val surnames = try {
                                                        data.name!!.split(" ").last()
                                                    } catch (e: Exception) {
                                                        "SYSTEM"
                                                    }

                                                    if (data.designation == "GK") {
                                                        cpuDbHelper.addPlayer(surnames, data.is_captain.toString(), data.country_id.toString(), data.type.toString(), data.designation.toString(), data.jersey_number.toString(), "false", "false")
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                Log.e("My Player Database Error", e.toString())
                                            }
                                        }
                                        if (data.SubtitutePlyer != null) {
                                            try {
                                                // Extra Player
                                                for (data in data.SubtitutePlyer) {
                                                    val surnames = try {
                                                        data.name!!.split(" ").last()
                                                    } catch (e: Exception) {
                                                        "SYSTEM"
                                                    }
                                                    extraPLayerDbHelper.addPlayer(surnames, data.is_captain.toString(), data.country_id.toString(), data.type.toString(), data.designation.toString(), data.jersey_number.toString(), "false", "USER")
                                                }
                                            } catch (e: Exception) {
                                                Log.e("My Player Database Error", e.toString())
                                            }
                                        }
                                        checkAllPlayer(dialog)
                                    }
                                }catch (e:Exception){
                                    Log.d("signup","message:---"+e.message)
                                }
                            } else {
                                sessionManager.alertError(model.message)
                            }
                        }catch (e:Exception){
                            Log.d("signup","message:---"+e.message)
                        }
                    }
                    is NetworkResult.Error -> {
                        sessionManager.alertError(it.message.toString())
                    }
                    else -> {
                        sessionManager.alertError(it.message.toString())
                    }
                }
            }, defender, midfielder, attacker, "", "",matchNo.toString())
        }
    }
    // This function is used for check cpu and user team player list and verify
    private fun checkAllPlayer(dialog: Dialog?) {
        if (myPlayerDbHelper.getAllPlayers().size < 10) {
            val remain = 10 - myPlayerDbHelper.getAllPlayers().size
            if (remain > 1) {
                for (i in 0 until remain) {
                    myPlayerDbHelper.addPlayer("SYSTEM", "0", "ENG", "no", "MF", "10", "false", "false")
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
        userType = if (userType.equals("CPU",true)) { "USER" } else { "CPU" }
        setTimerLogic()
        timerLogic()
        setPlayerScreens()
        Handler(Looper.myLooper()!!).postDelayed({
            dialog?.dismiss()
        }, 2000)
    }
    private fun backButton(){
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true  /*enabled by default*/ ) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    @SuppressLint("SetTextI18n")
    private fun setupFootballFormation(formationType: String, players: List<PlayerModel>) {
        binding.formationContainer.removeAllViews()
        // Group players by position
        val groupedPlayers = listOf(
            "FW" to players.filter { it.designation == "FW" },
            "MF" to players.filter { it.designation == "MF" },
            "DF" to players.filter { it.designation == "DF" },
            "GK" to players.filter { it.designation == "GK" },
        )
        for ((position, playersInPosition) in groupedPlayers) {
            // Create a row for the position
            val row = LinearLayout(requireContext()).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_HORIZONTAL
            }
            // Add space before players to center them
            row.addView(Space(requireContext()), LayoutParams(0, LayoutParams.MATCH_PARENT, 1f))
            for ((index, player) in playersInPosition.withIndex()) {
                val playerView = LayoutInflater.from(requireContext()).inflate(R.layout.playerview, row, false)
                val rpSelect = playerView.findViewById<ImageView>(R.id.rp_select)
                val playerImage = playerView.findViewById<ImageView>(R.id.rp_img)
                val rpName = playerView.findViewById<TextView>(R.id.rp_name)
                val rpNumber = playerView.findViewById<TextView>(R.id.rp_number)
                // Set margin on playerView
                val layoutParams = LayoutParams(playerView.layoutParams.width, playerView.layoutParams.height).apply {
                    topMargin = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp) // or use a fixed value e.g., 16
                }
                playerView.layoutParams = layoutParams
                if (player.answer.equals("true",true)){
                    rpSelect.visibility = View.GONE
                    rpName.visibility = View.VISIBLE
                    rpNumber.visibility = View.VISIBLE
                    rpName.text = player.name
                    rpNumber.text = player.jersey_number
                    val countryID = player.country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    playerImage.setImageResource(setGames.getTShirtImage(countryID))
                    rpNumber.setTextColor(textColor)
                    val countAnswer = players.count { it.answer.equals("true",true) }?:0
                    if (countAnswer!=0){
                        binding.rootButton.visibility=View.VISIBLE
                        if (countAnswer>1){
                            binding.btShoot.visibility=View.VISIBLE
                            binding.btPass.visibility=View.VISIBLE
                        }else{
                            binding.btShoot.visibility=View.GONE
                            binding.btPass.visibility=View.VISIBLE
                        }
                    }else{
                        binding.rootButton.visibility=View.GONE
                    }
                }else{
                    playerImage.setImageResource(R.drawable.user_guess_white)
                    rpSelect.visibility = View.GONE
                    rpName.visibility = View.GONE
                    rpNumber.visibility = View.GONE
                    if (player.use.equals("true",true)){
                        rpSelect.visibility = View.VISIBLE
                    }else{
                        rpSelect.visibility = View.GONE
                    }
                    playerView.setOnClickListener {
                        val countAnswer = players.count { it.answer.equals("true",true) }?:0
                        val bundle = Bundle()
                        if (countAnswer==0){
                            if (player.designation.equals("MF",true)){
                                bundle.putString("Name", player.name)
                                bundle.putString("userType", userType)
                                bundle.putString("Num", player.jersey_number)
                                bundle.putString("id", player.id)
                                Log.e("Send Detail of Quiz", userType + " " + player.name + " " + player.jersey_number)
                                findNavController().navigate(R.id.player_name_guess, bundle)
                            }
                        }else{
                            if (player.use.equals("true",true)){
                                bundle.putString("Name", player.name)
                                bundle.putString("userType", userType)
                                bundle.putString("Num", player.jersey_number)
                                bundle.putString("id", player.id)
                                Log.e("Send Detail of Quiz", userType + " " + player.name + " " + player.jersey_number)
                                findNavController().navigate(R.id.player_name_guess, bundle)
                            }
                        }
                    }
                }
                row.addView(playerView)
                // Add space between players
                if (index < playersInPosition.size - 1) {
                    row.addView(Space(requireContext()), LayoutParams(0, LayoutParams.MATCH_PARENT, 1f))
                }
            }
            // Add space after players to center them
            row.addView(Space(requireContext()), LayoutParams(0, LayoutParams.MATCH_PARENT, 1f))
            // Add the row to the container
            binding.formationContainer.addView(row)
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setupFootballFormationCpu(players1: String, players: List<PlayerModel>) {
        binding.formationContainer.removeAllViews()
        // Group players by position
        val groupedPlayers = listOf(
            "FW" to players.filter { it.designation == "FW" },
            "MF" to players.filter { it.designation == "MF" },
            "DF" to players.filter { it.designation == "DF" },
            "GK" to players.filter { it.designation == "GK" },
        )
        for ((position, playersInPosition) in groupedPlayers) {
            // Create a row for the position
            val row = LinearLayout(requireContext()).apply {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    0,
                    1f
                )
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_HORIZONTAL
            }
            // Add space before players to center them
            row.addView(Space(requireContext()), LayoutParams(0, LayoutParams.MATCH_PARENT, 1f))
            for ((index, player) in playersInPosition.withIndex()) {
                val playerView = LayoutInflater.from(requireContext()).inflate(R.layout.playerview, row, false)
                val rpSelect = playerView.findViewById<ImageView>(R.id.rp_select)
                val playerImage = playerView.findViewById<ImageView>(R.id.rp_img)
                val rpName = playerView.findViewById<TextView>(R.id.rp_name)
                val rpNumber = playerView.findViewById<TextView>(R.id.rp_number)
                // Set margin on playerView
                val layoutParams = LayoutParams(
                    playerView.layoutParams.width,
                    playerView.layoutParams.height
                ).apply {
                    topMargin = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp) // or use a fixed value e.g., 16
                }
                playerView.layoutParams = layoutParams
                if (player.answer.equals("true",true)){
                    rpSelect.visibility = View.GONE
                    rpName.visibility = View.VISIBLE
                    rpNumber.visibility = View.VISIBLE
                    rpName.text = player.name
                    rpNumber.text = player.jersey_number
                    val countryID = player.country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    playerImage.setImageResource(setGames.getTShirtImage(countryID))
                    rpNumber.setTextColor(textColor)
                    binding.rootButton.visibility=View.GONE
                }else{
                    playerImage.setImageResource(R.drawable.user_guess_white)
                    rpSelect.visibility = View.GONE
                    rpName.visibility = View.GONE
                    rpNumber.visibility = View.GONE
                    if (player.use.equals("true",true)){
                        rpSelect.visibility = View.VISIBLE
                    }else{
                        rpSelect.visibility = View.GONE
                    }
                    clickablePlayers.add(Pair(playerView, player))
                }
                row.addView(playerView)
                // Add space between players
                if (index < playersInPosition.size - 1) {
                    row.addView(Space(requireContext()), LayoutParams(0, LayoutParams.MATCH_PARENT, 1f))
                }
            }
            // Add space after players to center them
            row.addView(Space(requireContext()), LayoutParams(0, LayoutParams.MATCH_PARENT, 1f))
            // Add the row to the container
            binding.formationContainer.addView(row)
        }
    }
    override fun onClick(item: View?) {
        when (item!!.id) {
            R.id.bt_pass -> {
                    userPassBall()
            }
            R.id.bt_shoot -> {
                    userShoot()
            }
        }
    }
    private fun userShoot(){
        val playerId=sessionManager.getMySelectedTeamPlayerNum()
        val player = allCpuPlayer.find { it.id.toInt() == playerId }
        if (!player?.designation.equals("GK",true)){
            val bundle = Bundle()
            bundle.putString("userType", "USER")
            bundle.putInt("selected_player_num", playerId)
            val size = 0
            for (data in allCpuPlayer) {
                if (data.id.toInt() == playerId) {
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
            findNavController().navigate(R.id.shoot_Screen, bundle)
        }
    }

    private fun userPassBall() {
            if (userType.equals("USER",true)) {
                val playerId=sessionManager.getMySelectedTeamPlayerNum()
                val player = allCpuPlayer.find { it.id.toInt() == playerId }
                if (player != null) {
                    if (player.is_captain.equals("0", ignoreCase = true)) {
                        val targetMap = getTargetMap()
                        val targetIds = targetMap[playerId] ?: emptySet()
                        allCpuPlayer = allCpuPlayer
                            .map { player ->
                                if (player.id.toInt()  in targetIds) {
                                    player.copy(use = "true")
                                } else {
                                    player.copy(use = "false")
                                }
                            }
                            .toCollection(ArrayList())
                    } else {
                        allCpuPlayer = allCpuPlayer
                            .map { player -> player.copy(use = "true") }
                            .toCollection(ArrayList())
                    }
                    setupFootballFormation("3-2-5-1",allCpuPlayer)
                }
            }
    }

    private fun cpuPassBall() {
        val playerId=sessionManager.getSelectedTeamPlayerNum()
        Log.d("@@@@Error", "****** plarerId$playerId")
        val player = allUserPlayer.find { it.id.toInt() == playerId }
        if (player != null) {
            if (player.is_captain.equals("0", ignoreCase = true)) {
                val targetMap = getTargetMap()
                val targetIds = targetMap[playerId] ?: emptySet()
                allUserPlayer = allUserPlayer
                    .map { player ->
                        if (player.id.toInt()  in targetIds) {
                            player.copy(use = "true")
                        } else {
                            player.copy(use = "false")
                        }
                    }
                    .toCollection(ArrayList())
            } else {
                allUserPlayer = allUserPlayer
                    .map { player -> player.copy(use = "true") }
                    .toCollection(ArrayList())
            }
            startCpuProcess()
            setupFootballFormationCpu("3-2-5-1",allUserPlayer)
        }
    }

    private fun getTargetMap(): Map<Int, Set<Int>> {
        val data = sessionManager.getUserScreenType().lowercase()
        // Defensive
        val map523 = mapOf(
            1 to setOf(2, 6),
            2 to setOf(1, 6, 3, 11),
            3 to setOf(2, 6, 7, 4, 11),
            4 to setOf(3, 5, 7, 11),
            5 to setOf(4, 7),
            6 to setOf(1, 2, 3, 7, 8, 9),
            7 to setOf(6, 3, 4, 5, 9, 10),
            8 to setOf(6, 9),
            9 to setOf(6, 7, 8, 10),
            10 to setOf(9, 7),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        val map541 = mapOf(
            1 to setOf(2, 6),
            2 to setOf(1, 3, 6, 11),
            3 to setOf(2, 4, 7, 8, 11),
            4 to setOf(3, 5, 8, 9, 11),
            5 to setOf(4, 9),
            6 to setOf(1, 2, 4),
            7 to setOf(10, 2, 3, 6, 8),
            8 to setOf(3, 4, 7, 9,10),
            9 to setOf(8, 5),
            10 to setOf(7, 8),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        val map532 = mapOf(
            1 to setOf(2, 6),
            2 to setOf(6, 3, 11),
            3 to setOf(2, 4, 6, 7, 8, 11),
            4 to setOf(3, 5, 7, 8, 11),
            5 to setOf(4, 8),
            6 to setOf(2, 3, 7, 9),
            7 to setOf(6, 8, 3, 2, 4),
            8 to setOf(7, 4, 5),
            9 to setOf(6, 7, 10),
            10 to setOf(9, 8, 7),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        // Balanced
        val map352 = mapOf(
            1 to setOf(2, 11, 4, 5, 6),
            2 to setOf(1, 3, 11, 5, 6, 7),
            3 to setOf(2, 11, 6, 7, 8),
            4 to setOf(1, 5),
            5 to setOf(1, 2, 4, 6, 9),
            6 to setOf(1, 2, 3, 5, 7, 9, 10),
            7 to setOf(6, 8, 2, 3, 10),
            8 to setOf(7, 3),
            9 to setOf(10, 5, 6),
            10 to setOf(9, 6, 7),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        val map451 = mapOf(
            1 to setOf(2, 5, 6),
            2 to setOf(1, 11, 3, 5, 6),
            3 to setOf(2, 4, 11, 7, 8),
            4 to setOf(3, 8, 9),
            5 to setOf(1, 6),
            6 to setOf(1, 2, 5, 7),
            7 to setOf(6, 8, 2, 3, 10),
            8 to setOf(3, 4, 7, 9, 10),
            9 to setOf(8, 4),
            10 to setOf(6, 7, 8),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        val map442 = mapOf(
            1 to setOf(2, 5),
            2 to setOf(1, 3, 6, 11),
            3 to setOf(2, 4, 11, 7),
            4 to setOf(3, 7, 8),
            5 to setOf(1, 6),
            6 to setOf(5, 2, 7, 9),
            7 to setOf(6, 8, 3, 10),
            8 to setOf(4, 7),
            9 to setOf(7, 6, 10),
            10 to setOf(7, 8, 9),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        // Attacking
        val map433 = mapOf(
            1 to setOf(2,3,11,5),
            2 to setOf(1, 3,11,5,6),
            3 to setOf(2,11,4,6,7),
            4 to setOf(3,7),
            5 to setOf(1,2,8),
            6 to setOf(5,7,2,3,9),
            7 to setOf(6,10,3,4),
            8 to setOf(5,9,6),
            9 to setOf(8,10,6),
            10 to setOf(9,7),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        val map343 = mapOf(
            1 to setOf(2,5,11),
            2 to setOf(1, 3,11,5,6),
            3 to setOf(2,11,6,7),
            4 to setOf(1,5,8),
            5 to setOf(4,6,1,2),
            6 to setOf(5,7,2,3,9,10),
            7 to setOf(3,6,10),
            8 to setOf(4,5,9),
            9 to setOf(8,10,5,6),
            10 to setOf(9,6,7),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        val map424 = mapOf(
            1 to setOf(2,11),
            2 to setOf(1, 3,11,5),
            3 to setOf(2,11,6,4),
            4 to setOf(3,11),
            5 to setOf(2,6,8),
            6 to setOf(5,3,9),
            7 to setOf(8,5),
            8 to setOf(7,9,5),
            9 to setOf(8,10,6),
            10 to setOf(9,6),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        return when (data) {
            "5-2-3" -> map523
            "5-4-1" -> map541
            "5-3-2" -> map532
            "3-5-2" -> map352
            "4-5-1" -> map451
            "4-4-2" -> map442
            "4-3-3" -> map433
            "3-4-3" -> map343
            "4-2-4" -> map424
            else -> emptyMap()  // return empty map if no match
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
        sessionManager.saveTimer(startTime)
        stopCpuProcess()
    }
}