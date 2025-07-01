package com.yesitlabs.jumballapp.fragment.manfragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.adapter.SubsItemViewPagerAdapter
import com.yesitlabs.jumballapp.database.player_dtl.CPUPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.ExtraPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.team_dtl.TeamModel
import com.yesitlabs.jumballapp.databinding.FragmentChooseYourFormationBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.gameRule.SetGames
import com.yesitlabs.jumballapp.model.GetGroupDetailResp
import com.yesitlabs.jumballapp.model.GuessPlayerListResp
import com.yesitlabs.jumballapp.model.ImageModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.network.viewModel.GetGuessPlayerListViewModel
import com.yesitlabs.jumballapp.viewmodeljumball.PlayerListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class ChooseYourFormationFragment : Fragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentChooseYourFormationBinding
    private var listDefensiveData = ArrayList<ImageModel>()
    private var listBalancedData = ArrayList<ImageModel>()
    private var listAttackingData = ArrayList<ImageModel>()
    private var screenType: String = "5-2-3"
    var type: String = "1"
    private lateinit var cpuDbHelper: CPUPlayerDatabaseHelper
    private lateinit var myPlayerDbHelper: PlayerDatabaseHelper
    private lateinit var extraPLayerDbHelper: ExtraPlayerDatabaseHelper

    lateinit var sessionManager: SessionManager
    private lateinit var getGuessPlayerListViewmodel: GetGuessPlayerListViewModel
    private lateinit var viewmodel: PlayerListViewModel
    var token: String? = null
    private var setGames: SetGames = SetGames()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChooseYourFormationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment

        sessionManager = SessionManager(requireContext())


        binding.card1.setBackgroundResource(R.drawable.active_bg)
        binding.card2.setBackgroundResource(R.drawable.in_active_bg)
        binding.card3.setBackgroundResource(R.drawable.in_active_bg)


        getGuessPlayerListViewmodel = ViewModelProvider(this)[GetGuessPlayerListViewModel::class.java]
        viewmodel = ViewModelProvider(requireActivity())[PlayerListViewModel::class.java]

        cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
        myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
        extraPLayerDbHelper = ExtraPlayerDatabaseHelper(requireContext())


        cpuDbHelper.deleteAllPlayers()
        myPlayerDbHelper.deleteAllPlayers()
        extraPLayerDbHelper.deleteAllPlayers()


        token = "Bearer " + sessionManager.fetchAuthToken()


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.dashBoardFragment)

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding.tvDefensive.setTextColor(Color.parseColor("#FFFFFF"))
        binding.tvBalanced.setTextColor(Color.parseColor("#000000"))
        binding.tvAttacking.setTextColor(Color.parseColor("#000000"))

        //list list-defensive

        val listDefensiveModel1 = ImageModel()
        listDefensiveModel1.Image = R.drawable.def_1
        listDefensiveModel1.status = "5-2-3"
        listDefensiveData.add(listDefensiveModel1)

        val listDefensiveModel2 = ImageModel()
        listDefensiveModel2.Image = R.drawable.def_2
        listDefensiveModel2.status = "5-4-1"
        listDefensiveData.add(listDefensiveModel2)

        val listDefensiveModel3 = ImageModel()
        listDefensiveModel3.Image = R.drawable.def_3
        listDefensiveModel3.status = "5-3-2"
        listDefensiveData.add(listDefensiveModel3)

        //list list balanced

        val listBalancedModel1 = ImageModel()
        listBalancedModel1.Image = R.drawable.bal_1
        listBalancedModel1.status = "3-5-2"
        listBalancedData.add(listBalancedModel1)

        val listBalancedModel2 = ImageModel()
        listBalancedModel2.Image = R.drawable.bal_2
        listBalancedModel2.status = "4-5-1"
        listBalancedData.add(listBalancedModel2)

        val listBalancedModel3 = ImageModel()
        listBalancedModel3.Image = R.drawable.bal_3
        listBalancedModel3.status = "4-4-2"
        listBalancedData.add(listBalancedModel3)

        //list list-attacking

        val listAttackingModel1 = ImageModel()
        listAttackingModel1.Image = R.drawable.att_1
        listAttackingModel1.status = "4-3-3"
        listAttackingData.add(listAttackingModel1)

        val listAttackingModel2 = ImageModel()
        listAttackingModel2.Image = R.drawable.att_2
        listAttackingModel2.status = "3-4-3"
        listAttackingData.add(listAttackingModel2)

        val listAttackingModel3 = ImageModel()
        listAttackingModel3.Image = R.drawable.att_3
        listAttackingModel3.status = "4-2-4"
        listAttackingData.add(listAttackingModel3)


        // show preview of page on left and right

        binding.viewPagerCyf.clipToPadding = false
        binding.viewPagerCyf.clipChildren = false
        binding.viewPagerCyf.offscreenPageLimit = 3

        binding.viewPagerCyf.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f

        }
        binding.viewPagerCyf.setPageTransformer(compositePageTransformer)


        setDataOnViewPager(listDefensiveData)
        binding.btnDefensive.setOnClickListener(this)
        binding.btnBalanced.setOnClickListener(this)
        binding.btnAttacking.setOnClickListener(this)
        binding.btnProceed.setOnClickListener(this)


    }

    // This function is used for set all formation in screen
    private fun setDataOnViewPager(listDefensive: ArrayList<ImageModel>) {
        binding.viewPagerCyf.adapter = SubsItemViewPagerAdapter(listDefensive)
        binding.viewPagerCyf.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    override fun onClick(item: View?) {

        sessionManager.playClickSound()

        when (item?.id) {
            R.id.btn_defensive -> {
                binding.card1.setBackgroundResource(R.drawable.active_bg)
                binding.card2.setBackgroundResource(R.drawable.in_active_bg)
                binding.card3.setBackgroundResource(R.drawable.in_active_bg)
                binding.tvDefensive.setTextColor(Color.parseColor("#FFFFFF"))
                binding.tvBalanced.setTextColor(Color.parseColor("#000000"))
                binding.tvAttacking.setTextColor(Color.parseColor("#000000"))
                screenType = "5-2-3"
                type = "1"
                setDataOnViewPager(listDefensiveData)

            }

            R.id.btn_balanced -> {
                binding.card1.setBackgroundResource(R.drawable.in_active_bg)
                binding.card2.setBackgroundResource(R.drawable.active_bg)
                binding.card3.setBackgroundResource(R.drawable.in_active_bg)

                binding.tvDefensive.setTextColor(Color.parseColor("#000000"))
                binding.tvBalanced.setTextColor(Color.parseColor("#FFFFFF"))
                binding.tvAttacking.setTextColor(Color.parseColor("#000000"))
                screenType = "3-5-2"
                type = "2"
                setDataOnViewPager(listBalancedData)

            }

            R.id.btn_attacking -> {
                binding.card1.setBackgroundResource(R.drawable.in_active_bg)
                binding.card2.setBackgroundResource(R.drawable.in_active_bg)
                binding.card3.setBackgroundResource(R.drawable.active_bg)

                binding.tvDefensive.setTextColor(Color.parseColor("#000000"))
                binding.tvBalanced.setTextColor(Color.parseColor("#000000"))
                binding.tvAttacking.setTextColor(Color.parseColor("#FFFFFF"))
                screenType = "4-3-3"
                type = "3"
                setDataOnViewPager(listAttackingData)

            }

            R.id.btn_proceed -> {
                if (type == "1") {
                    sessionManager.setUserScreenType(listDefensiveData[binding.viewPagerCyf.currentItem].status)
                    sessionManager.setCpuScreenType(listDefensiveData[binding.viewPagerCyf.currentItem].status)
                }
                if (type == "2") {
                    sessionManager.setUserScreenType(listBalancedData[binding.viewPagerCyf.currentItem].status)
                    sessionManager.setCpuScreenType(listBalancedData[binding.viewPagerCyf.currentItem].status)
                }
                if (type == "3") {
                    sessionManager.setUserScreenType(listAttackingData[binding.viewPagerCyf.currentItem].status)
                    sessionManager.setCpuScreenType(listAttackingData[binding.viewPagerCyf.currentItem].status)
                }

                val screen = setGames.setScreen(sessionManager.getUserScreenType())
                val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())

                if (sessionManager.isNetworkAvailable()) {
                    getGuessTeamList(screen.r1.toString(), screen.r2.toString(), screen.r3.toString(), cpuScreen.r1.toString(), cpuScreen.r2.toString(), cpuScreen.r3.toString())
                } else {
                    sessionManager.alertError(ErrorMessage.netWorkError)
                }

            }
        }

    }

    // This function is used for get guess player list from database api
    private fun getGuessTeamList(defender: String, midfielder: String, attacker: String, cpuDefender: String, cpuMidFielder: String, cpuAttacker: String) {
        val matchNo = (sessionManager.getGameNumber()-1)
        sessionManager.showMe(requireContext())
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
                                           Log.e("***** Player", myPlayerDbHelper.getAllPlayers().size.toString())
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
                                                               "false",
                                                               "false"
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
                                                               "false",
                                                               "false"
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
                                                               "false",
                                                               "false"
                                                           )
                                                       }
                                                   }
                                               }

                                               //Shrawan
                                               // Goalkeeper
                                               for (data in data.myplayer) {
                                                   if (data.is_captain == 1) {
                                                       sessionManager.setMyPlayerId(data.id)
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
                                               Log.e("***** My Player Database Error", e.toString())
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
                                                               "false",
                                                               "false"
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
                                                               "false",
                                                               "false"
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
                                                               "false",
                                                               "false"
                                                           )
                                                       }
                                                   }
                                               }

                                               //Shrawan
                                               // Goalkeeper
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
                                               Log.e("***** My Player Database Error", e.toString())
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
                                               Log.e("***** My Player Database Error", e.toString())
                                           }
                                       }
                                       checkAllPlayer()
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
    private fun checkAllPlayer() {

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

        Handler(Looper.myLooper()!!).postDelayed({
            Log.e("***** My Team :", myPlayerDbHelper.getAllPlayers().toString())
            Log.e("***** My Team Size :", myPlayerDbHelper.getAllPlayers().size.toString())
            Log.e("***** CPU Team :", cpuDbHelper.getAllPlayers().toString())
            Log.e("***** CPU Team Size :", cpuDbHelper.getAllPlayers().size.toString())
            Log.e("***** Extra Team :", extraPLayerDbHelper.getAllPlayers().toString())
            Log.e("***** Extra Team Size :", extraPLayerDbHelper.getAllPlayers().size.toString())
            val bundle = Bundle()
            bundle.putString("type", "dashboard")
            findNavController().navigate(R.id.selectTossFragment,bundle)
        }, 1000)

    }


}
