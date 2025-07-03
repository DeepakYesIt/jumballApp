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
import com.yesitlabs.jumballapp.database.player_dtl.CPUPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerModel
import com.yesitlabs.jumballapp.database.team_dtl.TeamDatabaseHelper
import com.yesitlabs.jumballapp.gameRule.SetGames
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.database.player_dtl.ExtraPlayerDatabaseHelper
import com.yesitlabs.jumballapp.databinding.FragmentShootBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.GuessPlayerListResp
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.viewmodeljumball.PlayerListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random


@AndroidEntryPoint
class ShootFragment : Fragment(), View.OnClickListener {

    var size: Int = 2
    private var userType: String = "USER"
    private var selectedPlayerNum: Int = 1
    private lateinit var viewmodel: PlayerListViewModel

    private lateinit var teamDbHelper: TeamDatabaseHelper
    private lateinit var cpuDbHelper: CPUPlayerDatabaseHelper
    private lateinit var myPlayerDbHelper: PlayerDatabaseHelper
    private lateinit var extraPLayerDbHelper: ExtraPlayerDatabaseHelper
    private var allCpuPlayer = ArrayList<PlayerModel>()
    private var allUserPlayer = ArrayList<PlayerModel>()
    lateinit var sessionManager: SessionManager
    private var setGames: SetGames = SetGames()

    private lateinit var binding: FragmentShootBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShootBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        viewmodel = ViewModelProvider(requireActivity())[PlayerListViewModel::class.java]

        cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
        myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
        teamDbHelper = TeamDatabaseHelper(requireContext())
        extraPLayerDbHelper = ExtraPlayerDatabaseHelper(requireContext())

        allCpuPlayer = cpuDbHelper.getAllPlayers()
        allUserPlayer = myPlayerDbHelper.getAllPlayers()


        backButton()
        size=requireArguments().getInt("size")

        sessionManager.saveMyPass(0)
        sessionManager.saveCpuPass(0)

        userType = requireArguments().getString("userType").toString().uppercase(Locale.ROOT)
        selectedPlayerNum = requireArguments().getInt("selected_player_num")

        Log.d("******","size :- ="+size)
        Log.d("******","selectBox assigne value :="+selectedPlayerNum)
        Log.e("shoot argument", requireArguments().toString())

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
            binding. r24g4.setOnClickListener(this)
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

    // This function is used for set structure of boxes in screen
    private fun setStructure(size: Int) {

        when (size) {
            2 -> {
                binding.r.weightSum = 1f
                binding.r2.visibility = View.GONE
                binding.r1g3.visibility = View.GONE
                binding.r1g4.visibility = View.GONE
                binding.r1.weightSum = 2f
            }

            3 -> {
                binding.r.weightSum = 1f
                binding.r2.visibility = View.GONE
                binding.r1g4.visibility = View.GONE
                binding.r1.weightSum = 3f
            }

            4 -> {
                binding.r.weightSum = 1f
                binding.r2.visibility = View.GONE
                binding.r1.weightSum = 4f
            }

            5 -> {
                binding.r.weightSum = 2f
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.VISIBLE
                binding.r22.visibility = View.GONE
                binding.r23.visibility = View.GONE
                binding.r24.visibility = View.GONE
            }

            6 -> {
                binding.r.weightSum = 2f
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.GONE
                binding.r22.visibility = View.VISIBLE
                binding.r23.visibility = View.GONE
                binding.r24.visibility = View.GONE

            }

            7 -> {
                binding.r.weightSum = 2f
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.GONE
                binding.r22.visibility = View.GONE
                binding.r23.visibility = View.VISIBLE
                binding.r24.visibility = View.GONE
            }

            8 -> {
                binding.r.weightSum = 2f
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.GONE
                binding.r22.visibility = View.GONE
                binding.r23.visibility = View.GONE
                binding.r24.visibility = View.VISIBLE
            }

            else -> {
                binding.r.weightSum = 2f
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.GONE
                binding.r22.visibility = View.GONE
                binding.r23.visibility = View.GONE
                binding.r24.visibility = View.VISIBLE
            }

        }

        setSelectColor()

    }

    // This function is used for reset the select box color
    private fun setSelectColor() {
        listOf(
            binding.r1g1Select,
            binding.r1g2Select,
            binding.r1g3Select,
            binding.r1g4Select,
            binding.r21g1Select,
            binding.r22g1Select,
            binding.r22g2Select,
            binding.r23g1Select,
            binding.r23g2Select,
            binding.r23g3Select,
            binding.r24g1Select,
            binding.r24g2Select,
            binding.r24g3Select,
            binding.r24g4Select
        ).forEach { it.visibility = View.GONE }
    }

    override fun onClick(view: View?) {

        sessionManager.changeMusic(16, 0)

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
                    selectKickGoalBox(4, binding.r1g4Select,binding.r1g4)
                }
                R.id.r21g1 -> {
                    selectKickGoalBox(5, binding.r21g1Select,binding.r21g1)
                }
                R.id.r22g1 -> {
                    selectKickGoalBox(5, binding.r22g1Select,binding.r22g1)
                }
                R.id.r22g2 -> {
                    selectKickGoalBox(6, binding.r22g2Select,binding.r22g2)
                }
                R.id.r23g1 -> {
                    selectKickGoalBox(5, binding.r23g1Select,binding.r23g1)
                }
                R.id.r23g2 -> {
                    selectKickGoalBox(6, binding.r23g2Select,binding.r23g2)
                }
                R.id.r23g3 -> {
                    selectKickGoalBox(7, binding.r23g3Select,binding.r23g3)
                }
                R.id.r24g1 -> {
                    selectKickGoalBox(5, binding.r24g1Select,binding.r24g1)
                }
                R.id.r24g2 -> {
                    selectKickGoalBox(6, binding.r24g2Select,binding.r24g2)
                }
                R.id.r24g3 -> {
                    selectKickGoalBox(7, binding.r24g3Select,binding.r24g3)
                }
                R.id.r24g4 -> {
                    selectKickGoalBox(8, binding.r24g4Select,binding.r24g4)
                }


            }
        }
    }

    // This function is used for working on select box
    private fun selectKickGoalBox(selectBox: Int, r1g1Select: View?, r24g4: ConstraintLayout) {
        val views = listOf(
            binding.r1g1Select,
            binding.r1g2Select,
            binding.r1g3Select,
            binding.r1g4Select,
            binding.r21g1Select,
            binding.r22g1Select,
            binding.r22g2Select,
            binding.r23g1Select,
            binding.r23g2Select,
            binding.r23g3Select,
            binding.r24g1Select,
            binding.r24g2Select,
            binding.r24g3Select,
            binding.r24g4Select
        )
        views.forEach { view ->
            view.visibility = if (view == r1g1Select) View.VISIBLE else View.GONE
        }
        val selectViews = listOf(
            binding.r1g1,
            binding.r1g2,
            binding.r1g3,
            binding.r1g4,
            binding.r21g1,
            binding.r22g1,
            binding.r22g2,
            binding.r23g1,
            binding.r23g2,
            binding.r23g3,
            binding.r24g1,
            binding.r24g2,
            binding.r24g3,
            binding.r24g4
        )
        selectViews.forEach { view ->
            view.isEnabled=false
        }
        Handler(Looper.myLooper()!!).postDelayed({
            val bundle = Bundle()
            if (userType.equals("USER",true)) {
                bundle.putString("userType", "CPU")
            } else {
                bundle.putString("userType", "USER")
            }
            bundle.putInt("select_box", selectBox)
            bundle.putInt("selected_player_num", selectedPlayerNum)
            bundle.putInt("size", size)
            Log.e("Shoot to Kick", bundle.toString())
            val num = setGames.getRandomNumber(size)
            checkGoal(num, selectBox)
        }, 3000)
    }

    // This function is used for check goal or not
    private fun checkGoal(cpuBox: Int, userBox: Int) {
        if (cpuBox == userBox) {
            goalSuccessfully()
        } else {
            goalFailed()
        }

    }

    // This is used for goal failed
    private fun goalFailed() {
        val screen = setGames.setScreen(sessionManager.getUserScreenType())
        val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())
        if (sessionManager.isNetworkAvailable()) {
            cpuDbHelper.deleteAllPlayers()
            myPlayerDbHelper.deleteAllPlayers()
            extraPLayerDbHelper.deleteAllPlayers()
            getGuessTeamList(
                false,
                screen.r1.toString(),
                screen.r2.toString(),
                screen.r3.toString(),
                cpuScreen.r1.toString(),
                cpuScreen.r2.toString(),
                cpuScreen.r3.toString()
            )
        } else {
            alertError(ErrorMessage.netWorkError)
        }
    }


    // This function is used for when goal is success
    private fun goalSuccessfully() {
        if (userType.equals("USER",true)) {
            sessionManager.setFirstGamgeStartUser(true)
            sessionManager.setFirstGamgeStartCPU(false)
        } else {
            sessionManager.setFirstGamgeStartCPU(true)
            sessionManager.setFirstGamgeStartUser(false)
        }

        sessionManager.saveSelectedTeamPlayerNum(selectedPlayerNum)

        if (sessionManager.getMatchType().equals("worldcup",true)) {
            teamDbHelper.updateF(1, 1)
        }

        sessionManager.putMyScore(1)

        val screen = setGames.setScreen(
            sessionManager.getUserScreenType()
        )

        val cpuScreen = setGames.setScreen(
            sessionManager.getCpuScreenType()
        )

        if (sessionManager.isNetworkAvailable()) {
            cpuDbHelper.deleteAllPlayers()
            myPlayerDbHelper.deleteAllPlayers()
            extraPLayerDbHelper.deleteAllPlayers()
            getGuessTeamList(
                true,
                screen.r1.toString(),
                screen.r2.toString(),
                screen.r3.toString(),
                cpuScreen.r1.toString(),
                cpuScreen.r2.toString(),
                cpuScreen.r3.toString()
            )

        } else {
            alertError(ErrorMessage.netWorkError)
        }


    }

    // This function is used for display the alert dialog of goal or not
    private fun playAlertBox(drawableImg: Int, player: String) {
        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView = dialog.findViewById(R.id.img_change)
        imgChange.setImageResource(drawableImg)

        Handler(Looper.myLooper()!!).postDelayed({
            sessionManager.increaseTimer(120000)
            val bundle = Bundle()

            if (userType == "USER") {
                bundle.putString("userType", "CPU")
            } else {
                bundle.putString("userType", "USER")
            }
            sessionManager.changeMusic(1,1)
//            findNavController().navigate(R.id.playScreenFragment, bundle)
            findNavController().navigate(R.id.playerUserCPUFragment, bundle)
        },2000)

        Handler(Looper.myLooper()!!).postDelayed({
            dialog.dismiss()
        }, 3500)

        dialog.show()
    }


    // This function is used for get guess player list from database api
    private fun getGuessTeamList(
        win: Boolean,
        defender: String,
        midfielder: String,
        attacker: String,
        cpuDefender: String,
        cpuMidFielder: String,
        cpuAttacker: String
    ) {
        val matchNo = (sessionManager.getGameNumber()-1)
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
                                                            myPlayerDbHelper.addPlayer(
                                                                surnames,
                                                                data.is_captain.toString(),
                                                                data.country_id.toString(),
                                                                data.type.toString(),
                                                                data.designation.toString(),
                                                                data.jersey_number.toString(),
                                                                "false", "false"
                                                            )
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
                                                            myPlayerDbHelper.addPlayer(
                                                                surnames,
                                                                data.is_captain.toString(),
                                                                data.country_id.toString(),
                                                                data.type.toString(),
                                                                data.designation.toString(),
                                                                data.jersey_number.toString(),
                                                                "false", "false"
                                                            )
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
                                                            myPlayerDbHelper.addPlayer(
                                                                surnames,
                                                                data.is_captain.toString(),
                                                                data.country_id.toString(),
                                                                data.type.toString(),
                                                                data.designation.toString(),
                                                                data.jersey_number.toString(),
                                                                "false", "false"
                                                            )
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
                                                            cpuDbHelper.addPlayer(
                                                                surnames,
                                                                data.is_captain.toString(),
                                                                data.country_id.toString(),
                                                                data.type.toString(),
                                                                data.designation.toString(),
                                                                data.jersey_number.toString(),
                                                                "false", "false"
                                                            )
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
                                                            cpuDbHelper.addPlayer(
                                                                surnames,
                                                                data.is_captain.toString(),
                                                                data.country_id.toString(),
                                                                data.type.toString(),
                                                                data.designation.toString(),
                                                                data.jersey_number.toString(),
                                                                "false", "false"
                                                            )
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
                                                            cpuDbHelper.addPlayer(
                                                                surnames,
                                                                data.is_captain.toString(),
                                                                data.country_id.toString(),
                                                                data.type.toString(),
                                                                data.designation.toString(),
                                                                data.jersey_number.toString(),
                                                                "false", "false"
                                                            )
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
                                        if (data.SubtitutePlyer != null) {
                                            try {
                                                // Extra Player
                                                for (data in data.SubtitutePlyer) {
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
    private fun checkAllPlayer(win: Boolean) {
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

        if (win) {
            playAlertBox(R.drawable.goal_alert, "USER")
        } else {
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
            goalFailed()
        })
        dialog.show()
    }

}