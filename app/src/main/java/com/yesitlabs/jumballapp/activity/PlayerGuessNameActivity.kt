package com.yesitlabs.jumballapp.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.adapter.AdpterNameHint
import com.yesitlabs.jumballapp.database.player_dtl.CPUPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.ExtraPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerModel
import com.yesitlabs.jumballapp.databinding.FragmentPlayerGuessNameBinding
import com.yesitlabs.jumballapp.gameRule.SetGames
import com.yesitlabs.jumballapp.model.guessName.GuessName
import com.yesitlabs.jumballapp.network.viewModel.GetGuessPlayerListViewModel
import java.util.Locale
import kotlin.random.Random

@SuppressLint("CustomSplashScreen")
class PlayerGuessNameActivity : AppCompatActivity(), View.OnClickListener {
    
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var adapterNameHint: AdpterNameHint
    private var hintList = ArrayList<GuessName>()
    private var answerList = ArrayList<GuessName>()

    private var playerName: String? = "Null"
    private var userType: String = "null"
    private var playerNum: String = "null"
    private var playerId: String = "null"

    private var quizTime = 50
    private var maxTime = 50

    private var isTimerRunning = true

    private lateinit var cpuDbHelper: CPUPlayerDatabaseHelper
    private lateinit var myPlayerDbHelper: PlayerDatabaseHelper
    private lateinit var extraPLayerDbHelper: ExtraPlayerDatabaseHelper

    private var allCpuPlayer = ArrayList<PlayerModel>()
    private var allUserPlayer = ArrayList<PlayerModel>()


    lateinit var sessionManager: SessionManager
    private lateinit var getGuessPlayerListViewmodel: GetGuessPlayerListViewModel
    var token: String? = null
    private var setGames: SetGames = SetGames()
    private lateinit var binding: FragmentPlayerGuessNameBinding

    private var playerPower = "NO"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPlayerGuessNameBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        getGuessPlayerListViewmodel =
            ViewModelProvider(this)[GetGuessPlayerListViewModel::class.java]

        playerName =intent.getStringExtra("Name").toString().uppercase(Locale.ROOT)
        playerNum = intent.getStringExtra("Num").toString().uppercase(Locale.ROOT)
        userType = intent.getStringExtra("userType").toString().uppercase(Locale.ROOT)
        playerId = intent.getStringExtra("id").toString().uppercase(Locale.ROOT)

        token = "Bearer " + sessionManager.fetchAuthToken()

        binding.tvNumber.text = playerNum
        binding.tvNumber.visibility = View.GONE

        attachTimer()
        countDownTimer.start()

        cpuDbHelper = CPUPlayerDatabaseHelper(this)
        myPlayerDbHelper = PlayerDatabaseHelper(this)
        extraPLayerDbHelper = ExtraPlayerDatabaseHelper(this)

        allCpuPlayer = cpuDbHelper.getAllPlayers()
        allUserPlayer = myPlayerDbHelper.getAllPlayers()


        Log.e("Guess Start", "Jai HO")

        hintList = setGames.shuffleName(playerName!!)
        answerList = setGames.getCharactersList(playerName!!)


        if (userType == "USER") {
            Toast.makeText(this, playerName.toString(), Toast.LENGTH_LONG).show()
            for (data in allCpuPlayer) {
                if (data.id == playerId) {
                    playerPower = data.type.uppercase()
                }
            }
        } else {
            for (data in allUserPlayer) {
                if (data.id == playerId) {
                    playerPower = data.type.uppercase()
                }
            }
        }



        when (playerPower.uppercase()) {
            "PURPLE" -> {
                val tintColor = ContextCompat.getColor(this, R.color.purpleBand)
                binding.tshirtBand.setColorFilter(tintColor)
            }

            "RED" -> {
                val tintColor = ContextCompat.getColor(this, R.color.redBand)
                binding.tshirtBand.setColorFilter(tintColor)
            }

            "GOLD" -> {
                val tintColor = ContextCompat.getColor(this, R.color.goldBand)
                binding.tshirtBand.setColorFilter(tintColor)
            }

            else -> {
                val tintColor = ContextCompat.getColor(this, R.color.white)
                binding.tshirtBand.setColorFilter(tintColor)
            }
        }


        // Remove this if condition if you want to show auto play its use for hide autoplay
        if (userType == "CPU") {
            // Show Directly Result
            when (setGames.getRandomNumber(5)) {
                1 -> {
                    rightAnswer()
                }

                2 -> {
                    rightAnswer()
                }

                3 -> {
                    wrongAnswer()
                }

                4 -> {
                    wrongAnswer()
                }

                5 -> {
                    wrongAnswer()
                }

                else -> {
                    wrongAnswer()
                }

            }

        } else {
            resetPage(userType)
        }

        // uncomment This function for Show auto Play
        //  resetPage(userType)
        
    }

    private fun setLifeLineBox() {
        if (!sessionManager.getLifeLine1()) {
            binding.lifeLine1.setBackgroundResource(R.drawable.rectangle_3__1_)
        } else {
            binding.lifeLine1.setBackgroundResource(R.drawable.rectangle_3)
        }

        if (!sessionManager.getLifeLine2()) {
            binding.lifeLine2.setBackgroundResource(R.drawable.rectangle_3__1_)
        } else {
            binding.lifeLine2.setBackgroundResource(R.drawable.rectangle_3)
        }

        if (!sessionManager.getLifeLine3()) {
            binding.lifeLine3.setBackgroundResource(R.drawable.rectangle_3__1_)
        } else {
            if (sessionManager.getSpecialPower()) {
                binding.lifeLine3.setBackgroundResource(R.drawable.rectangle_3__1_)
            } else {
                binding.lifeLine3.setBackgroundResource(R.drawable.rectangle_3)
            }
        }

    }

    // This is used for reset the page or reset button
    private fun resetPage(userType: String) {
        binding.tvName.text = ""

        for (data in hintList) {
            data.backgoung_color = R.color.hint_color_unselect
            data.text_color = R.color.black
            data.used = false
        }

        for (data in answerList) {
            data.backgoung_color = R.color.hint_color_unselect
            data.text_color = R.color.black
            data.used = false
        }

        val spanCount = if (hintList.size < 8) {
            hintList.size
        } else {
            8
        }


        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.justifyContent = JustifyContent.CENTER
        binding.rcyHint.layoutManager = layoutManager


        binding.rcyHint.layoutManager = layoutManager
        adapterNameHint = AdpterNameHint(hintList, answerList,this, userType)
        binding.rcyHint.adapter = adapterNameHint


        adapterNameHint.setOnItemClickListener(object : AdpterNameHint.OnItemClickListener {
            @SuppressLint("SetTextI18n")
            override fun onItemClick(item: String) {
                binding.tvName.text = binding.tvName.text.toString() + item
                checkName()
            }
        })

        if (userType == "CPU") {

            // Show Directly Result

            when (setGames.getRandomNumber(5)) {
                1 -> {
                    rightAnswer()
                }

                2 -> {
                    wrongAnswer()
                }

                3 -> {
                    wrongAnswer()
                }

                4 -> {
                    wrongAnswer()
                }

                5 -> {
                    wrongAnswer()
                }

                else -> {
                    wrongAnswer()
                }

            }

            // Display Auto Play Mode

            val num = setGames.getRandomGustedPlayerName(hintList, answerList)
            autoPlay(num, 0)
        } else {
            setLifeLineBox()
            binding.btReset.setOnClickListener(this)
            binding.lifeLine1.setOnClickListener(this)
            binding.lifeLine2.setOnClickListener(this)
            binding.lifeLine3.setOnClickListener(this)
        }


    }

    // This is used for attach the timer in screen
    private fun attachTimer() {
        countDownTimer = object : CountDownTimer(45000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                quizTime -= 1
                binding.timerProgress.max = maxTime
                binding.timerProgress.progress = quizTime
            }

            override fun onFinish() {
                countDownTimer.cancel()
                wrongAnswer()
            }
        }
    }


    override fun onClick(view: View?) {

        sessionManager.playClickSound()

        when (view?.id) {
            R.id.bt_reset -> {
                if (userType == "USER") {
                    resetPage(userType)
                }

            }

            R.id.life_line_1 -> {
                if (userType == "USER") {
                    if (sessionManager.getLifeLine1()) {
                        useLifeLine1()
                        sessionManager.disableLifeLine1()
                    }
                }
                setLifeLineBox()
            }

            R.id.life_line_2 -> {
                if (userType == "USER") {
                    if (sessionManager.getLifeLine2()) {
                        useLifeLine2()
                        sessionManager.disableLifeLine2()
                    }

                }
                setLifeLineBox()
            }

            R.id.life_line_3 -> {
                if (userType == "USER") {
                    if (sessionManager.getLifeLine3()) {
                        useLifeLine3()
                        sessionManager.disableLifeLine3()
                    } else {
                        if (sessionManager.getSpecialPower()) {
                            sessionManager.setSpecialPower(false)
                            useLifeLine3()
                        }
                    }
                }
                setLifeLineBox()

            }
        }


    }

    // This is used for Use of Lifeline 1
    private fun useLifeLine1() {

        sessionManager.changeMusic(5, 0)
        if (userType == "USER") {
            val row = setGames.setScreen(sessionManager.getCpuScreenType())
            val bundle = Bundle()
            bundle.putString("userType", "CPU")
            bundle.putInt("PlayerNum", setGames.getRandomNumber(row.r1))
//            findNavController().navigate(R.id.action_guessPlayerNameFragment_to_playScreenFragment, bundle)
        } else {
            val row = setGames.setScreen(sessionManager.getUserScreenType())
            val bundle = Bundle()
            bundle.putString("userType", "USER")
            bundle.putInt("PlayerNum", setGames.getRandomNumber(row.r1))
//            findNavController().navigate(
//                R.id.action_guessPlayerNameFragment_to_playScreenFragment,
//                bundle
//            )
        }
    }

    // This is used for Use of Lifeline 2
    private fun useLifeLine2() {

        sessionManager.changeMusic(5, 0)
        val lifeLinePlayerList = extraPLayerDbHelper.getAllPlayers()


        var position = 0

        for (i in 0 until lifeLinePlayerList.size) {
            if (lifeLinePlayerList[i].designation == allCpuPlayer[playerId.toInt() - 1].designation && lifeLinePlayerList[i].userType == "CPU" && lifeLinePlayerList[i].answer == "NO") {
                position = i
                extraPLayerDbHelper.updatePlayerAnswer(i.toLong(), "YES")
                Log.e("Change Position", position.toString())
                Log.e("Change Designation", lifeLinePlayerList[i].designation)
                break
            }
        }

        val data = lifeLinePlayerList[position]

        cpuDbHelper.updatePlayerDetails(
            playerId.toLong(),
            data.name,
            data.is_captain,
            data.country_id,
            data.type,
            data.designation,
            data.jersey_number,
            "false",
            "false"
        )
        playerName = lifeLinePlayerList[position].name
        playerNum = lifeLinePlayerList[position].jersey_number
        hintList = setGames.shuffleName(playerName!!)
        answerList = setGames.getCharactersList(playerName!!)
        resetPage("USER")
    }

    // This is used for Use of Lifeline 3
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun useLifeLine3() {
        sessionManager.changeMusic(5, 0)
        if (answerList.size in 2..5) {
            useAssistantManager(1)
        }

        if (answerList.size in 6..7) {
            useAssistantManager(2)
        }

        if (answerList.size >= 8) {
            useAssistantManager(3)
        }

    }

    // This is used for assistant manager lifeline 1
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun useAssistantManager(inputPower: Int) {

        var changePower = inputPower

        var selectText = binding.tvName.text.toString()

        Log.e("Select Player Length", selectText.length.toString())
        Log.e("answer_list Length", answerList.size.toString())


        if (selectText.isEmpty()) {
            for (power in 0 until changePower) {
                for (i in 0 until hintList.size) {
                    if (hintList[i] == answerList[power] && !hintList[i].used) {
                        binding.tvName.text = binding.tvName.text.toString() + hintList[i].hint
                        hintList[i].backgoung_color = R.color.hint_color_right
                        hintList[i].text_color = R.color.white
                        hintList[i].used = true
                        adapterNameHint.notifyItemChanged(i)
                        break
                    }
                }
            }
            Log.e("Entry Power", "1 ")
            checkName()

        } else {


            try {
                for (i in selectText.indices) {
                    if (selectText[i].toString() != answerList[i].hint) {
                        selectText = selectText.substring(0, i)
                        binding.tvName.text = selectText
                        Log.e("Reset String", selectText)
                        break
                    }
                }

                for (hintText in 0 until hintList.size) {
                    if (hintList[hintText].used && !selectText.contains(hintList[hintText].hint)) {
                        hintList[hintText].backgoung_color = R.color.hint_color_unselect
                        hintList[hintText].text_color = R.color.black
                        hintList[hintText].used = false
                        Log.e("Hint Reset", hintList[hintText].hint)
                        adapterNameHint.notifyItemChanged(hintText)
                    }
                }


                val sizeAns = selectText.length

                for (ans in sizeAns until answerList.size) {
                    for (hint in 0 until hintList.size) {
                        if (changePower > 0) {
                            if (hintList[hint].hint == answerList[ans].hint && !hintList[hint].used) {
                                selectText = binding.tvName.text.toString() + hintList[hint].hint
                                binding.tvName.text = selectText
                                hintList[hint].backgoung_color = R.color.hint_color_right
                                hintList[hint].text_color = R.color.white
                                hintList[hint].used = true
                                changePower -= 1
                                Log.e("Hint Put", hintList[hint].hint)
                                break
                            }
                        }
                    }
                }

                adapterNameHint.notifyDataSetChanged()

            } catch (e: Exception) {
                Log.e("Assent Manager Hint Error", e.toString())
            }

            checkName()

        }
    }

    // This is used for cpu auto play algorithm
    @SuppressLint("SetTextI18n")
    private fun autoPlay(numData: ArrayList<Int>, num: Int) {

        Handler(Looper.myLooper()!!).postDelayed({

            binding.tvName.text = binding.tvName.text.toString() + hintList[numData[num]].hint

            if (hintList[numData[num]].hint == answerList[binding.tvName.text.toString().length - 1].hint) {
                Log.e(
                    "Auto Selection",
                    hintList[numData[num]].hint + " " + answerList[binding.tvName.text.toString().length - 1].hint
                )
                hintList[numData[num]].backgoung_color = R.color.hint_color_right
                hintList[numData[num]].text_color = R.color.white
                hintList[numData[num]].used = true
                adapterNameHint.notifyItemChanged(numData[num])
            } else {
                hintList[numData[num]].backgoung_color = R.color.hint_color_worng
                hintList[numData[num]].text_color = R.color.white
                hintList[numData[num]].used = true
                adapterNameHint.notifyItemChanged(numData[num])
            }

            if (binding.tvName.text.toString().length == playerName!!.length) {

                if (binding.tvName.text.toString() == playerName) {
                    binding.tvNumber.visibility = View.VISIBLE

                    val countryID = allUserPlayer[playerId.toInt() - 1].country_id

                    val textColor = ContextCompat.getColor(
                        this,
                        setGames.getTShirtTextColor(countryID)
                    )


                    binding.tvTshirtImg.setImageResource(setGames.getTShirtImage(countryID))
                    binding.tvName.setTextColor(textColor)
                    binding.tvNumber.setTextColor(textColor)

                    if (playerPower.uppercase() != "NO") {
                        binding.tshirtBand.visibility = View.VISIBLE
                    } else {
                        binding.tshirtBand.visibility = View.GONE
                    }


                    rightAnswer()
                } else {
                    wrongAnswer()
                }
            } else {
                autoPlay(numData, num + 1)
            }

        }, 3000)

    }

    // This is used for check  name
    private fun checkName() {
        if (binding.tvName.text.toString().length == playerName!!.length) {

            if (binding.tvName.text.toString() == playerName) {

                val countryID = allCpuPlayer[playerId.toInt() - 1].country_id
                val textColor =
                    ContextCompat.getColor(this, setGames.getTShirtTextColor(countryID))


                binding.tvTshirtImg.setImageResource(setGames.getTShirtImage(countryID))
                binding.tvName.setTextColor(textColor)
                binding.tvNumber.setTextColor(textColor)
                binding.tvNumber.visibility = View.VISIBLE

                if (playerPower.uppercase() != "NO") {
                    binding.tshirtBand.visibility = View.VISIBLE
                } else {
                    binding.tshirtBand.visibility = View.GONE
                }

                rightAnswer()
                Log.e("Guessed Name", "Right Answer")
            } else {
                wrongAnswer()
                Log.e("Guessed Name", "Wrong Answer")
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()

        if (isTimerRunning) {
            countDownTimer.cancel()
            isTimerRunning = false
        }

    }

    override fun onStop() {
        super.onStop()

        if (isTimerRunning) {
            countDownTimer.cancel()
            isTimerRunning = false
        }
    }

    /*override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }*/

    // This is used for guess right name
    private fun rightAnswer() {
        if (userType == "USER") {
            sessionManager.setFirstGamgeStartUser(true)

        } else {
            sessionManager.setFirstGamgeStartCPU(true)
        }

        val bundle = Bundle()

        bundle.putString("userType", userType)
        bundle.putInt("PlayerNum", playerId.toInt())

        if (sessionManager.getGameCondition() == 0) {
            sessionManager.increaseTimer(60000)
            sessionManager.setGameGameCondition(1)
        } else {
            sessionManager.increaseTimer(60000)
//            if (sessionManager.getGameCondition() == 2) {
//                sessionManager.increaseTimer(60000)
//                sessionManager.setGameGameCondition(3)
//            } else {
//                sessionManager.increaseTimer(180000)
//            }
        }



        if (userType == "USER") {
            if (playerPower.uppercase() == "GOLD") {
                sessionManager.setSpecialPower(true)
            }
            sessionManager.saveMySelectedTeamPlayerNum(playerId.toInt())
            cpuDbHelper.updatePlayerAnswer(playerId.toLong(), "true")
        } else {
            sessionManager.saveSelectedTeamPlayerNum(playerId.toInt())
            myPlayerDbHelper.updatePlayerAnswer(playerId.toLong(), "true")
        }

        Log.e("Answer", "Right")

//        findNavController().navigate(R.id.action_guessPlayerNameFragment_to_playScreenFragment, bundle)

    }

    // This is used for guess wrong name
    fun wrongAnswer() {

        if (userType == "USER") {
            sessionManager.setFirstGamgeStartUser(false)

        } else {
            sessionManager.setFirstGamgeStartCPU(false)
        }


        when (Random.nextInt(1, 3)) {
            1 -> {
                sessionManager.changeMusic(22, 0)
                playAlertBox(R.drawable.bad_pass)
            }

            2 -> {
                sessionManager.changeMusic(22, 0)
                playAlertBox(R.drawable.clearance_img)
            }

            else -> {
                sessionManager.changeMusic(22, 0)
                playAlertBox(R.drawable.lost_possession)
            }

        }

        val screen = setGames.setScreen(sessionManager.getUserScreenType())

        val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())

        if (sessionManager.isNetworkAvailable()) {
            cpuDbHelper.deleteAllPlayers()
            myPlayerDbHelper.deleteAllPlayers()
            extraPLayerDbHelper.deleteAllPlayers()
            getGuessTeamList(
                screen.r1.toString(),
                screen.r2.toString(),
                screen.r3.toString(),
                cpuScreen.r1.toString(),
                cpuScreen.r2.toString(),
                cpuScreen.r3.toString()
            )
        } else {
            alertError(getString(R.string.no_internet))
        }


    }

    // This is used for display alert box of name guess successfully or not
    private fun playAlertBox(drawableImg: Int) {
        val dialog = Dialog(this, R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView = dialog.findViewById(R.id.img_change)
        imgChange.setImageResource(drawableImg)

        Handler(Looper.myLooper()!!).postDelayed({
            sessionManager.changeMusic(1, 1)
            dialog.dismiss()
        }, 3500)

        dialog.show()
    }


    // This function is used for get guess player list from database api
    private fun getGuessTeamList(
        defender: String,
        midfielder: String,
        attacker: String,
        cpuDefender: String,
        cpuMidFielder: String,
        cpuAttacker: String
    ) {

        getGuessPlayerListViewmodel.getGuessPlayerListResponse.observe(this) { response ->
            if (response != null) {


                if (response.isSuccessful) {
                    val teamResponse = response.body()

                    if (teamResponse != null) {
                        if (teamResponse.data != null) {

                            if (teamResponse.data.myplayer != null) {
                                sessionManager.increaseTimer(120000)

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


                            checkAllPlayer()
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

        val match_no = (sessionManager.getGameNumber()-1)
        getGuessPlayerListViewmodel.getGuessPlayerList("$token", defender, midfielder, attacker,
            "", "",match_no.toString())
    }

    // This function is used for check cpu and user team player list and verify
    private fun checkAllPlayer() {

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

        if (userType == "CPU") {
            bundle.putString("userType", "user")
        } else {
            bundle.putString("userType", "cpu")
        }

//        findNavController().navigate(
//            R.id.action_guessPlayerNameFragment_to_playScreenFragment,
//            bundle
//        )

    }

    // This function is used for Select CPU play screen formation [Auto]
    private fun selectCpuScreen(): String {
        val screen: String

        val randomNumber = Random.nextInt(1, 10)

        when (randomNumber) {

            1 -> {
                screen = "5-2-3"
            }

            2 -> {
                screen = "5-4-1"
            }

            3 -> {
                screen = "5-3-2"
            }

            4 -> {
                screen = "3-5-2"
            }

            5 -> {
                screen = "4-5-1"
            }

            6 -> {
                screen = "4-4-2"
            }

            7 -> {
                screen = "4-3-3"
            }

            8 -> {
                screen = "3-4-3"
            }

            else -> {
                screen = "4-2-4"
            }

        }


        return screen
    }

    @SuppressLint("SetTextI18n")
    fun alertError(msg: String) {
        val dialog = Dialog(this, R.style.BottomSheetDialog)
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
            wrongAnswer()
        })
        dialog.show()
    }

}