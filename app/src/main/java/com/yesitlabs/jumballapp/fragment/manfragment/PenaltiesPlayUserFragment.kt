package com.yesitlabs.jumballapp.fragment.manfragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.database.team_dtl.TeamDatabaseHelper
import com.yesitlabs.jumballapp.databinding.FragmentPenaltiesPlayUserBinding
import com.yesitlabs.jumballapp.network.viewModel.PenaltyScreenViewModel
import kotlin.random.Random


class PenaltiesPlayUserFragment : Fragment(R.layout.fragment_penalties_play_user) {

    private lateinit var viewModel : PenaltyScreenViewModel
    private var userType = "USER" // CPU
    lateinit var sessionManager : SessionManager
    private lateinit var teamDbHelper: TeamDatabaseHelper

    private lateinit var binding: FragmentPenaltiesPlayUserBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPenaltiesPlayUserBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(requireContext())
        sessionManager.changeMusic(20,1)
        teamDbHelper = TeamDatabaseHelper(requireContext())

        userType=arguments?.getString("userType","USER").toString()

        viewModel = ViewModelProvider(requireActivity())[PenaltyScreenViewModel::class.java]

        if (userType.equals("USER",true)){
            viewModel.currentChance =1
        }else{
            viewModel.currentChance =2
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d("****","Back Lock")
                }
            })

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allTeam = teamDbHelper.getAllTeams()
        for (data in allTeam) {
           if (data.teamID == 1) {
               binding.myName.text = data.captainName
           }
            if(sessionManager.getGameNumber() <= 3)  { //for first 3 match
                if (data.teamID == sessionManager.getTeamDetails()) {
                    binding.cpuName.text = data.captainName
                }
            }else if(sessionManager.getGameNumber() == 4){ // for 4th match
                binding.cpuName.text = "Asst. Manager"
            }else if(sessionManager.getGameNumber() == 5){ // for 5th match
                binding.cpuName.text = "Lizard mascot"
            }
        }
        startGame()
    }

    private fun startGame(){

        if(viewModel.currentChance ==1){
            Log.d("TESTING","CURRENT CHANCE IS "+ "USER")
            //UserChance
            resetSavePass()
            resetBall()
            binding.kickBallView.visibility =View.VISIBLE
            binding.saveBallView.visibility =View.GONE
            UserChance()
        }
        else{
            Log.d("TESTING","CURRENT CHANCE IS "+ "CPU")
            resetBall()
            resetSavePass()
            binding.kickBallView.visibility =View.GONE
            binding.saveBallView.visibility =View.VISIBLE
            CpuChance()
        }
    }


    private fun resetBall(){
        binding.ballBoxView1.visibility =View.GONE
        binding.ballBoxView2.visibility =View.GONE
        binding.ballBoxView3.visibility = View.GONE
        binding.ballBoxView4.visibility = View.GONE
    }


    private fun resetSavePass(){
        binding.ballBoxView11.visibility =View.GONE
        binding.ballBoxView22.visibility =View.GONE
        binding.ballBoxView33.visibility =View.GONE
        binding.ballBoxView44.visibility =View.GONE
    }

    private fun UserChance(){
        resetSavePass()
        resetBall()
        binding.kickBallView.visibility =View.VISIBLE
        binding.saveBallView.visibility =View.GONE
        val randomNumber = Random.nextInt(0, 4)
        Log.d("TESTING_random","wHEN cPU IS BALL SAVE IS "+randomNumber)
        callingSaveWork(randomNumber)
        callingUserSelectBallWork(randomNumber)
    }

    private fun resetAllBalls(){
        binding.cpuGoal55.setBackgroundResource(R.drawable.ic_gray_ball)
        binding.cpuGoal44.setBackgroundResource(R.drawable.ic_gray_ball)
        binding.cpuGoal33.setBackgroundResource(R.drawable.ic_gray_ball)
        binding.cpuGoal22.setBackgroundResource(R.drawable.ic_gray_ball)
        binding.cpuGoal11.setBackgroundResource(R.drawable.ic_gray_ball)

        binding.userGoal55.setBackgroundResource(R.drawable.ic_gray_ball)
        binding.userGoal44.setBackgroundResource(R.drawable.ic_gray_ball)
        binding.userGoal33.setBackgroundResource(R.drawable.ic_gray_ball)
        binding.userGoal22.setBackgroundResource(R.drawable.ic_gray_ball)
        binding.userGoal11.setBackgroundResource(R.drawable.ic_gray_ball)

    }

    private fun CpuChance(){
        resetBall()
        resetSavePass()
        binding.kickBallView.visibility =View.GONE
        binding.saveBallView.visibility =View.VISIBLE
        val randomNumber = Random.nextInt(0, 4)
        Log.d("@Error ","Cpu cpu is kicking ball "+randomNumber)
        callingCpuBallSelectWork(randomNumber)
        callingUserSaveSelect(randomNumber)
    }

    private fun callingUserSaveSelect(randomNum: Int){
        Log.d("@Error ","Cpu cpu is kicking ball inside..")
        binding.b1.setOnClickListener {
            binding. kickBallView.visibility =View.VISIBLE
            binding.saveBallView.visibility =View.GONE
            Handler().postDelayed({
                if(randomNum ==0){
                    cpuGuessWrong()
                }else{
                    cpuGuessRight()
                }
            },3000)
        }
        binding.b2.setOnClickListener {
            binding. kickBallView.visibility =View.VISIBLE
            binding.saveBallView.visibility =View.GONE
            Handler().postDelayed({
                if (randomNum == 1) {
                    cpuGuessWrong()
                } else {
                    cpuGuessRight()
                }
            },3000)
        }
        binding. b3.setOnClickListener {
            binding.  kickBallView.visibility =View.VISIBLE
            binding. saveBallView.visibility =View.GONE
            Handler().postDelayed({
                if (randomNum == 2) {
                    cpuGuessWrong()
                } else {
                    cpuGuessRight()
                }
            },3000)
        }
        binding. b4.setOnClickListener {
            binding. kickBallView.visibility = View.VISIBLE
            binding. saveBallView.visibility = View.GONE
            Handler().postDelayed({
                if (randomNum == 3) {
                    cpuGuessWrong()
                } else {
                    cpuGuessRight()
                }
            },3000)
        }
    }

    private fun cpuGuessRight(){
        Log.d("@Error ","Cpu Guess Correctly..")
        alertBoxAccordingToResponse(R.drawable.goal_alert,"correct")
        viewModel.cpuPoint+=1
        Handler().postDelayed({
            callingCpuGuessCorrectlyShowingBalls()
        },2000)
    }

    private fun cpuGuessWrong(){
        Log.d("@Error ","Cpu Guess Wrong...")
//        val boxesBg = listOf(R.drawable.goal_alert/*,R.drawable.wide, R.drawable.clearance_img, R.drawable.crossbar_img, R.drawable.over_img, R.drawable.post*/)
//        val img=boxesBg[ Random.nextInt(0, 5)]
//        alertBoxAccordingToResponse(img)
        alertBoxAccordingToResponse(R.drawable.saved_img,"wrong")
        Handler().postDelayed({
            callingCpuGuessInCorrectlyShowingBalls()
        },2000)
    }

    private fun callingUserSelectBallWork(randomNum :Int){
        Log.d("TESTING_random","Cpu save ball selected "+randomNum)
        binding.ballBox1.setOnClickListener {
            binding. kickBallView.visibility =View.GONE
            binding. saveBallView.visibility =View.VISIBLE
            Handler().postDelayed({
                if (randomNum == 0) {
                    callingUserGuessIncorrectly()
                } else {
                    callingUserGuessCorrectly()
                }
            },3000)

        }
        binding.ballBox22.setOnClickListener {
            binding.kickBallView.visibility =View.GONE
            binding. saveBallView.visibility =View.VISIBLE
            Handler().postDelayed({
                if(randomNum ==1){
                    callingUserGuessIncorrectly()
                }
                else{
                    callingUserGuessCorrectly()
                }
            },3000)
        }
        binding.ballBox33.setOnClickListener {
            binding. kickBallView.visibility =View.GONE
            binding. saveBallView.visibility =View.VISIBLE
            Handler().postDelayed({

                if(randomNum ==2){
                    callingUserGuessIncorrectly()
                }else{
                    callingUserGuessCorrectly()
                }
            },3000)


        }
        binding. ballBox44.setOnClickListener {
            binding. kickBallView.visibility =View.GONE
            binding.saveBallView.visibility =View.VISIBLE
            Handler().postDelayed({
                if(randomNum ==3){
                    callingUserGuessIncorrectly()
                }else{
                    callingUserGuessCorrectly()

                }
            },3000)
        }

    }

    fun callingCpuGuessCorrectlyShowingBalls(){
        if(viewModel.cpuCount ==0) {
            binding. cpuGoal55.setBackgroundResource(R.drawable.ball_2)
            viewModel.cpuList.add(0,true)
        }
        else if(viewModel.cpuCount ==1){
            binding.cpuGoal44.setBackgroundResource(R.drawable.ball_2)
            viewModel.cpuList.add(1,true)
        }
        else if(viewModel.cpuCount ==2){
            binding. cpuGoal33.setBackgroundResource(R.drawable.ball_2)
            viewModel.cpuList.add(2,true)
        }
        else if(viewModel.cpuCount ==3){
            binding. cpuGoal22.setBackgroundResource(R.drawable.ball_2)
            viewModel.cpuList.add(3,true)
        }
        else if(viewModel.cpuCount ==4){
            binding.cpuGoal11.setBackgroundResource(R.drawable.ball_2)
            viewModel.cpuList.add(4,true)
        }

        viewModel.cpuCount++;
        viewModel.loopCount++;

        if(viewModel.loopCount ==10){
            moveToScreen("User")
        }else{
            UserChance()
        }
    }

    private fun moveToScreen(type: String) {
        resetAllBalls()
        val bundle=Bundle()
        bundle.putString("userType",type)
        bundle.putString("player1",binding.myName.text.toString())
        bundle.putString("player2",binding.cpuName.text.toString())
        findNavController().navigate(R.id.suddenDeathFragment,bundle)
    }

    fun callingCpuGuessInCorrectlyShowingBalls(){
        if(viewModel.cpuCount ==0){
            binding.cpuGoal55.setBackgroundResource(R.drawable.ball_1)
            viewModel.cpuList.add(0,false)
        }
        else if(viewModel.cpuCount ==1){
            binding.cpuGoal44.setBackgroundResource(R.drawable.ball_1)
            viewModel.cpuList.add(1,false)
        }
        else if(viewModel.cpuCount ==2){
            binding.cpuGoal33.setBackgroundResource(R.drawable.ball_1)
            viewModel.cpuList.add(2,false)
        }
        else if(viewModel.cpuCount ==3){
            binding.cpuGoal22.setBackgroundResource(R.drawable.ball_1)
            viewModel.cpuList.add(3,false)
        }
        else if(viewModel.cpuCount ==4){
            binding.userGoal22.setBackgroundResource(R.drawable.ball_1)
            viewModel.cpuList.add(4,false)
        }

        viewModel.cpuCount++;
        viewModel.loopCount++;
        if(viewModel.loopCount ==10){
            moveToScreen("User")
        }else{
            UserChance()
        }
    }


    fun callingUserGuessCorrectly(){
        Log.d("@Error ","User Guess Correctly")
        alertBoxAccordingToResponse(R.drawable.goal_alert,"correct")
        viewModel.userPoint+=1
        Handler().postDelayed({
            if (viewModel.userCount == 0) {
                binding.userGoal11.setBackgroundResource(R.drawable.ball_2)
                viewModel.userlist.add(0,true)
            } else if (viewModel.userCount == 1) {
                binding. userGoal22.setBackgroundResource(R.drawable.ball_2)
                viewModel.userlist.add(1,true)
            } else if (viewModel.userCount == 2) {
                binding. userGoal33.setBackgroundResource(R.drawable.ball_2)
                viewModel.userlist.add(2,true)
            } else if (viewModel.userCount == 3) {
                binding.userGoal44.setBackgroundResource(R.drawable.ball_2)
                viewModel.userlist.add(3,true)
            } else if (viewModel.userCount == 4) {
                binding.userGoal55.setBackgroundResource(R.drawable.ball_2)
                viewModel.userlist.add(4,true)
            }

            viewModel.userCount++;
            viewModel.loopCount++;
            if (viewModel.loopCount == 10) {
                moveToScreen("CPU")
            } else {
                CpuChance()
            }
        },2000)

    }

    fun callingUserGuessIncorrectly(){
        Log.d("@Error ","User Guess not correct")
//        val boxesBg = listOf(R.drawable.goal_alert,R.drawable.wide, R.drawable.clearance_img, R.drawable.crossbar_img, R.drawable.over_img, R.drawable.post)
//        val img=boxesBg[ Random.nextInt(0, 5)]
//        alertBoxAccordingToResponse(img)

        alertBoxAccordingToResponse(R.drawable.saved_img,"wrong")
        Handler().postDelayed({
            if (viewModel.userCount == 0) {
                binding.userGoal11.setBackgroundResource(R.drawable.ball_1)
                viewModel.userlist.add(0,false)
            } else if (viewModel.userCount == 1) {
                binding.userGoal22.setBackgroundResource(R.drawable.ball_1)
                viewModel.userlist.add(1,false)
            } else if (viewModel.userCount == 2) {
                binding.userGoal33.setBackgroundResource(R.drawable.ball_1)
                viewModel.userlist.add(2,false)
            } else if (viewModel.userCount == 3) {
                binding.userGoal44.setBackgroundResource(R.drawable.ball_1)
                viewModel.userlist.add(3,false)
            } else if (viewModel.userCount == 4) {
                binding.userGoal55.setBackgroundResource(R.drawable.ball_1)
                viewModel.userlist.add(4,false)
            }
            viewModel.userCount++;
            viewModel.loopCount++;
            if (viewModel.loopCount == 10) {
                moveToScreen("CPU")
            } else {
                CpuChance()
            }
        },2000)

    }


    fun callingCpuBallSelectWork(randomNumber :Int){
        if(randomNumber ==0){
            binding.ballBoxView1.visibility =View.VISIBLE
        }
        else if(randomNumber ==1){

            binding.ballBoxView2.visibility =View.VISIBLE

        }
        else if(randomNumber ==2){
            binding.ballBoxView3.visibility =View.VISIBLE

        }
        else if(randomNumber ==3){
            binding.ballBoxView4.visibility =View.VISIBLE
        }

    }

    private fun  callingSaveWork(randomNumber :Int){
        if(randomNumber ==0){
            binding. ballBoxView11.visibility =View.VISIBLE
        }
        else if(randomNumber ==1){
            binding.ballBoxView22.visibility =View.VISIBLE
        }
        else if(randomNumber ==2){
            binding.ballBoxView33.visibility =View.VISIBLE
        }
        else if(randomNumber ==3){
            binding.ballBoxView44.visibility =View.VISIBLE
        }
    }

    private fun alertBoxAccordingToResponse(savedImg: Int,type:String){
        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView = dialog.findViewById(R.id.img_change)

        if (type.equals("correct",true)){
            sessionManager.changeMusic(21, 0)
        }else{
            sessionManager.changeMusic(17,0)
        }


        imgChange.setImageResource(savedImg)

        Handler(Looper.myLooper()!!).postDelayed({
            dialog.dismiss()
        }, 3000)

        dialog.show()
    }

}