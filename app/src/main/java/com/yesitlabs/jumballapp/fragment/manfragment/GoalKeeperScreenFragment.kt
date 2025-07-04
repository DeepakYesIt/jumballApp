package com.yesitlabs.jumballapp.fragment.manfragment
import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.database.player_dtl.CPUPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.ExtraPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerModel
import com.yesitlabs.jumballapp.database.team_dtl.TeamDatabaseHelper
import com.yesitlabs.jumballapp.databinding.FragmentGoalkeaperBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.gameRule.SetGames
import com.yesitlabs.jumballapp.model.GuessPlayerListResp
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.viewmodeljumball.PlayerListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random

@AndroidEntryPoint
class GoalKeeperScreenFragment : Fragment(), View.OnClickListener {
    var size: Int = 2
    private var userType: String = "USER"
    private var selectBox = 0
    private var selectedPlayerNum = 0
    lateinit var sessionManager: SessionManager
    var token: String? = null
    private var setGames: SetGames = SetGames()
    private lateinit var teamDbHelper: TeamDatabaseHelper
    private lateinit var extraPLayerDbHelper: ExtraPlayerDatabaseHelper
    private lateinit var cpuDbHelper: CPUPlayerDatabaseHelper
    private lateinit var myPlayerDbHelper: PlayerDatabaseHelper
    private var allCpuPlayer = ArrayList<PlayerModel>()
    private var allUserPlayer = ArrayList<PlayerModel>()
    private lateinit var viewmodel: PlayerListViewModel
    private lateinit var binding: FragmentGoalkeaperBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGoalkeaperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel = ViewModelProvider(requireActivity())[PlayerListViewModel::class.java]
        sessionManager = SessionManager(requireContext())
        teamDbHelper = TeamDatabaseHelper(requireContext())
        cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
        myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
        extraPLayerDbHelper = ExtraPlayerDatabaseHelper(requireContext())
        allCpuPlayer = cpuDbHelper.getAllPlayers()
        allUserPlayer = myPlayerDbHelper.getAllPlayers()
        token = "Bearer " + sessionManager.fetchAuthToken()
        size = requireArguments().getInt("size")
        selectBox = requireArguments().getInt("select_box")
        userType = requireArguments().getString("userType").toString().uppercase(Locale.ROOT)
        selectedPlayerNum = requireArguments().getInt("selected_player_num")
        Log.e("Goalkeeper argument", requireArguments().toString())
        Log.d("******","size :- ="+size)
        Log.d("******","selectBox assigne value :="+selectBox)
        backButton()
        sessionManager.saveMyPass(0)
        sessionManager.saveCpuPass(0)
        setStructure(size)
        if (userType.equals("USER",true)) {
            binding.r1g1.setOnClickListener(this)
            binding.r1g2.setOnClickListener(this)
            binding.r1g3.setOnClickListener(this)
            binding.r1g4.setOnClickListener(this)
            binding.r21g1.setOnClickListener(this)
            binding.r22g1.setOnClickListener(this)
            binding.r22g2.setOnClickListener(this)
            binding.r23g1.setOnClickListener(this)
            binding.r23g2.setOnClickListener(this)
            binding.r23g3.setOnClickListener(this)
            binding.r24g1.setOnClickListener(this)
            binding.r24g2.setOnClickListener(this)
            binding.r24g3.setOnClickListener(this)
            binding.r24g4.setOnClickListener(this)
        }
    }
    private fun backButton(){
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true  /*enabled by default*/ ) {
                override fun handleOnBackPressed() {
                   Log.d("******","not back because this screen is required")
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    // This is used for set goal screen structure
    private fun setStructure(size: Int) {
        when (size) {
            2 -> {
                binding.r.weightSum = 1f
                binding.r2.visibility = View.GONE
                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img_right)
                binding.r1g3.visibility = View.GONE
                binding.r1g4.visibility = View.GONE
                binding.r1.weightSum = 2f
            }
            3 -> {
                binding.r2.visibility = View.GONE
                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding.r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g4.visibility = View.GONE
                binding.r1.weightSum = 3f
                binding.r.weightSum = 1f
            }
            4 -> {
                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding.r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g44.setImageResource(R.drawable.shoot_img_right)
                binding.r2.visibility = View.GONE
                binding.r1.weightSum = 4f
                binding.r.weightSum = 1f
            }
            5 -> {
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.VISIBLE
                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding.r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g44.setImageResource(R.drawable.shoot_img_right)
                binding.r21g11.setImageResource(R.drawable.shoot_img)
                binding.r22.visibility = View.GONE
                binding.r23.visibility = View.GONE
                binding.r24.visibility = View.GONE
                binding.r.weightSum = 2f
            }
            6 -> {
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.GONE
                binding.r22.visibility = View.VISIBLE
                binding.r23.visibility = View.GONE
                binding.r24.visibility = View.GONE
                binding.r.weightSum = 2f
                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding. r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g44.setImageResource(R.drawable.shoot_img_right)
                binding.r22g11.setImageResource(R.drawable.shoot_img)
                binding.r22g22.setImageResource(R.drawable.shoot_img_right)
            }
            7 -> {
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.GONE
                binding.r22.visibility = View.GONE
                binding.r23.visibility = View.VISIBLE
                binding.r24.visibility = View.GONE
                binding.r.weightSum = 2f
                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding.r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g44.setImageResource(R.drawable.shoot_img_right)
                binding.r23g11.setImageResource(R.drawable.shoot_img)
                binding.r23g22.setImageResource(R.drawable.shoot_img)
                binding.r23g33.setImageResource(R.drawable.shoot_img_right)
            }
            8 -> {
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.GONE
                binding.r22.visibility = View.GONE
                binding.r23.visibility = View.GONE
                binding.r24.visibility = View.VISIBLE
                binding.r.weightSum = 2f
                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding.r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g44.setImageResource(R.drawable.shoot_img_right)
                binding.r24g11.setImageResource(R.drawable.shoot_img)
                binding.r24g22.setImageResource(R.drawable.shoot_img)
                binding.r24g33.setImageResource(R.drawable.shoot_img_right)
                binding.r24g44.setImageResource(R.drawable.shoot_img_right)
            }
            else -> {
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.GONE
                binding.r22.visibility = View.GONE
                binding.r23.visibility = View.GONE
                binding.r24.visibility = View.VISIBLE
                binding. r.weightSum = 2f
                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding.r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g44.setImageResource(R.drawable.shoot_img_right)
                binding.r24g11.setImageResource(R.drawable.shoot_img)
                binding.r24g22.setImageResource(R.drawable.shoot_img)
                binding.r24g33.setImageResource(R.drawable.shoot_img_right)
                binding.r24g44.setImageResource(R.drawable.shoot_img_right)
            }
        }
        setSelectColor()
    }
    // This is used for reset  box color
    private fun setSelectColor() {
        val viewsToHide = listOf(binding.r1g1Select, binding.r1g2Select, binding.r1g3Select, binding.r1g4Select, binding.r21g1Select, binding.r22g1Select, binding.r22g2Select, binding.r23g1Select, binding.r23g2Select, binding.r23g3Select, binding.r24g1Select, binding.r24g2Select, binding.r24g3Select, binding.r24g4Select)
        viewsToHide.forEach { it.visibility = View.GONE }
    }
    override fun onClick(view: View?) {
      sessionManager.playClickSound()
        if (view != null) {
            when (view.id) {
                R.id.r1g1 -> {
                    selectKickGoalBox(1, binding.r1g1Select,binding.r1g1)
                }
                R.id.r1g2 -> {
                    selectKickGoalBox(2, binding.r1g2Select,binding.r1g2)
                }
                R.id.r1g3 -> {
                    selectKickGoalBox(3, binding.r1g3Select,binding.r1g3)
                }
                R.id.r1g4 -> {
                    selectKickGoalBox(4, binding.r1g4Select, binding.r1g4)
                }
                R.id.r21g1 -> {
                    selectKickGoalBox(5, binding.r21g1Select, binding.r21g1)
                }
                R.id.r22g1 -> {
                    selectKickGoalBox(5, binding.r22g1Select, binding.r22g1)
                }
                R.id.r22g2 -> {
                    selectKickGoalBox(6, binding.r22g2Select, binding.r22g2)
                }
                R.id.r23g1 -> {
                    selectKickGoalBox(5, binding.r23g1Select, binding.r23g1)
                }
                R.id.r23g2 -> {
                    selectKickGoalBox(6, binding.r23g2Select, binding.r23g2)
                }
                R.id.r23g3 -> {
                    selectKickGoalBox(7, binding.r23g3Select, binding.r23g3)
                }
                R.id.r24g1 -> {
                    selectKickGoalBox(5, binding.r24g1Select, binding.r24g1)
                }
                R.id.r24g2 -> {
                    selectKickGoalBox(6, binding.r24g2Select, binding.r24g2)
                }
                R.id.r24g3 -> {
                    selectKickGoalBox(7, binding.r24g3Select, binding.r24g3)
                }
                R.id.r24g4 -> {
                    selectKickGoalBox(8, binding.r24g4Select, binding.r24g4)
                }
            }
        }
    }
    // This is used for cpu select box working [Auto]
    private fun selectKickGoalBox(selectBoxValue: Int, r1g1Select: View?, r24g4: ConstraintLayout) {
        val views = listOf(binding.r1g1Select, binding.r1g2Select, binding.r1g3Select, binding.r1g4Select, binding.r21g1Select, binding.r22g1Select, binding.r22g2Select, binding.r23g1Select, binding.r23g2Select, binding.r23g3Select, binding.r24g1Select, binding.r24g2Select, binding.r24g3Select, binding.r24g4Select)
        views.forEach { view -> view.visibility = if (view == r1g1Select) View.VISIBLE else View.GONE }
        val selectViews = listOf(binding.r1g1, binding.r1g2, binding.r1g3, binding.r1g4, binding.r21g1, binding.r22g1, binding.r22g2, binding.r23g1, binding.r23g2, binding.r23g3, binding.r24g1, binding.r24g2, binding.r24g3, binding.r24g4)
        selectViews.forEach { view -> view.isEnabled=false }
        Log.d("******", "select user :- $selectBoxValue")
        Handler(Looper.myLooper()!!).postDelayed({
            if (selectBoxValue == selectBox) {
                goalSuccessfully()
            } else {
                goalFailed()
            }
        }, 3000)
    }
    // This is used for goal failed
    private fun goalFailed() {
        val screen = setGames.setScreen(sessionManager.getUserScreenType())
        val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
        if(sessionManager.isNetworkAvailable()){
            cpuDbHelper.deleteAllPlayers()
            myPlayerDbHelper.deleteAllPlayers()
            extraPLayerDbHelper.deleteAllPlayers()
            getGuessTeamList(false, screen.r1.toString(), screen.r2.toString(), screen.r3.toString(), cpuScreen.r1.toString(), cpuScreen.r2.toString(), cpuScreen.r3.toString())
        }else{
            alertError(ErrorMessage.netWorkError)
        }
    }
    // This is used for goal successful
    private fun goalSuccessfully() {
        sessionManager.saveTotalDefence(sessionManager.getTotalDefence()+1)
        sessionManager.saveSelectedTeamPlayerNum(selectedPlayerNum)

        if (sessionManager.getMatchType().equals("worldcup",true)){
            teamDbHelper.updateA(1,1)
        }
        sessionManager.putCpuScore(1)
        val screen = setGames.setScreen(sessionManager.getUserScreenType())
        val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
        if(sessionManager.isNetworkAvailable()){
            cpuDbHelper.deleteAllPlayers()
            myPlayerDbHelper.deleteAllPlayers()
            extraPLayerDbHelper.deleteAllPlayers()
            getGuessTeamList(true, screen.r1.toString(), screen.r2.toString(), screen.r3.toString(), cpuScreen.r1.toString(), cpuScreen.r2.toString(), cpuScreen.r3.toString())
        }else{
            alertError(ErrorMessage.netWorkError)
        }
    }
    // This is used for show alert box of goal is success or fail
    @SuppressLint("SuspiciousIndentation")
    private fun playAlertBox(drawableImg : Int, player : String) {
        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView =dialog.findViewById(R.id.img_change)
        imgChange.setImageResource(drawableImg)
        Handler(Looper.myLooper()!!).postDelayed({
            sessionManager.increaseTimer(120000)
            val bundle = Bundle()
            bundle.putString("userType", userType)
            sessionManager.changeMusic(1,1)
            findNavController().navigate(R.id.playerUserCPUFragment, bundle)
        },2000)
        Handler(Looper.myLooper()!!).postDelayed({
            dialog.dismiss()
        }, 3500)
        dialog.show()
    }
    // This function is used for get guess player list from database api
    private fun getGuessTeamList(win : Boolean, defender: String, midfielder: String, attacker: String, cpuDefender: String, cpuMidFielder: String, cpuAttacker: String) {
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
                                        checkAllPlayer(win)
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
    private fun checkAllPlayer(win :Boolean) {
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
        if (win){
            playAlertBox( R.drawable.goal_alert,"CPU")
        }else{
            when (Random.nextInt(1, /*10*/6)) {
                1 -> {
                    playAlertBox(R.drawable.saved_img, "CPU")
                }
                2 -> {
                    playAlertBox(R.drawable.wide, "CPU")
                }
                3 -> {
                    sessionManager.changeMusic(22, 0)
                    playAlertBox(R.drawable.clearance_img, "CPU")
                }
                4 -> {
                    playAlertBox(R.drawable.crossbar_img, "CPU")
                }
                5 -> {
                    sessionManager.changeMusic(21, 0)
                    playAlertBox(R.drawable.post, "CPU")
                }
                6 -> {
                    sessionManager.changeMusic(21, 0)
                    playAlertBox(R.drawable.over_img, "CPU")
                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    fun  alertError(msg:String){
        val dialog= Dialog(requireContext(), R.style.BottomSheetDialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alertbox_error)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams
        val tvTitle: TextView =dialog.findViewById(R.id.tv_title)
        val btnOk: LinearLayout =dialog.findViewById(R.id.btn_ok)
        val btText: TextView =dialog.findViewById(R.id.btText)
        btText.text = "Retry"
        tvTitle.text=msg
        btnOk.setOnClickListener {
            dialog.dismiss()
            goalFailed()
        }
        dialog.show()
    }
}