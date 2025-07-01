package com.yesitlabs.jumballapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import com.yesitlabs.jumballapp.activity.AuthActivity
import com.yesitlabs.jumballapp.model.penaltiesmodel.PenalitesUserCPUStore


class SessionManager(var context: Context) {

    private var dialog: Dialog? = null


    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    private var isLoginPrefs: SharedPreferences = context.getSharedPreferences("isLogin", Context.MODE_PRIVATE)
    private val isLoginEditor: SharedPreferences.Editor = isLoginPrefs.edit()

    private var settingPrefs : SharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)
    private val settingEditor: SharedPreferences.Editor = settingPrefs.edit()



    fun changeMusic( value : Int , repeatMusic : Int){
        val intent = Intent(context, MusicService::class.java)
        intent.putExtra("value",value)
        intent.putExtra("repeat",repeatMusic)
        context.startService(intent)
    }

    fun setMusic(i : Int){
        settingEditor.putInt("music",i)
        settingEditor.apply()
    }

    fun getMusic():Int{
        // 0 -> off
        // 1 -> on
       return settingPrefs.getInt("music",1)
    }

    fun setSoundEffect(i : Int){
        settingEditor.putInt("soundEffect",i)
        settingEditor.apply()
    }

    fun getSoundEffect():Int{
        // 0 -> off
        // 1 -> on
        return settingPrefs.getInt("soundEffect",1)
    }



    // user Details

    fun deleteWholeData() {
        isLoginEditor.clear()
        isLoginEditor.apply()
        settingEditor.putInt("soundEffect",1)
        settingEditor.apply()
        settingEditor.putInt("music",1)
        settingEditor.apply()
        editor.clear()
        editor.apply()
    }


    fun saveAuthToken(token: String) {
        isLoginEditor.putString("user_token", token)
        isLoginEditor.apply()
    }

    fun fetchAuthToken(): String? {
        return isLoginPrefs.getString("user_token", "")
    }

    fun setCheckLogin(data: String) {
        isLoginEditor.putString("check_login", data)
        isLoginEditor.apply()
    }

    fun getCheckLogin(): String? {
        return isLoginPrefs.getString("check_login", null)
    }




    fun setEmail(email: String?) {
        isLoginEditor.putString("Email", email)
        isLoginEditor.apply()
    }

    fun setName(name: String) {
        isLoginEditor.putString("Name", name)
        isLoginEditor.apply()
    }

    fun getName(): String? {
        return isLoginPrefs.getString("Name", null)
    }
    

    

    // Game Details
    fun saveTeamDetails(opposeTEAM: Int) {
        editor.putInt("opposeTEAM", opposeTEAM)
        editor.apply()
    }

    fun getTeamDetails(): Int {
        return prefs.getInt("opposeTEAM", 2)
    }

    fun saveSelectedTeamPlayerNum(playerNum: Int) {
        editor.putInt("lastPlayer", playerNum)
        editor.apply()
    }

    fun getSelectedTeamPlayerNum(): Int {
        return prefs.getInt("lastPlayer", 1)
    }

    fun saveMySelectedTeamPlayerNum(playerNum: Int) {
        editor.putInt("myLastPlayer", playerNum)
        editor.apply()
    }

    fun getMySelectedTeamPlayerNum(): Int {
        return prefs.getInt("myLastPlayer", 1)
    }

    fun putMyScore(score: Int) {
        val value = prefs.getInt("myScore", 0) + score
        editor.putInt("myScore", value)
        editor.apply()
    }

    fun getMyScore(): Int {
        return prefs.getInt("myScore", 0)
    }

    fun putCpuScore(score: Int) {
        val value = prefs.getInt("cpuScore", 0) + score
        editor.putInt("cpuScore", value)
        editor.apply()
    }


    fun getCpuScore(): Int {
        return prefs.getInt("cpuScore", 0)
    }

    fun resetScore() {
        editor.putInt("cpuScore", 0)
        editor.putInt("myScore", 0)
        editor.putInt("timer", 0)
        editor.putInt("myPass", 0)
        editor.putInt("cpuPass", 0)
//        editor.putInt("gameNumber", 1)
        editor.putInt("gameCondition", 0)
        editor.putBoolean("lifeline3", true)
        editor.putBoolean("lifeline2", true)
        editor.putBoolean("lifeline1", true)
        editor.putBoolean("lifeline11", true)
        editor.putBoolean("specialPower", false)
        editor.putString("ExtraTime", "Normal")
        editor.apply()
    }

    fun resetGameNumberScore() {
        editor.putInt("gameNumber", 1)
        editor.apply()
    }


    fun disableLifeLine3() {
        editor.putBoolean("lifeline3", false)
        editor.apply()
    }

    fun getLifeLine3(): Boolean {
        return prefs.getBoolean("lifeline3", true)
    }

    fun disableLifeLine2() {
        editor.putBoolean("lifeline2", false)
        editor.apply()
    }

    fun getLifeLine2(): Boolean {
        return prefs.getBoolean("lifeline2", true)
    }

    fun setSpecialPower(power : Boolean) {
        editor.putBoolean("specialPower", power)
        editor.apply()
    }

    fun getSpecialPower()  : Boolean{
        return prefs.getBoolean("specialPower", false)
    }

    fun disableLifeLine1() {
        editor.putBoolean("lifeline1", false)
        editor.apply()
    }

    fun disableLifeLine11(status:Boolean) {
        editor.putBoolean("lifeline11", status)
        editor.apply()
    }

    fun getLifeLine1(): Boolean {
        return prefs.getBoolean("lifeline1", true)
    }

    fun getLifeLine11(): Boolean {
        return prefs.getBoolean("lifeline11", true)
    }

    fun setMyPlayerId(id : Int) {
        editor.putInt("myCaptainId", id)
        editor.apply()
    }

    fun getMYPlayerId( ) : Int {
        return prefs.getInt("myCaptainId", 0)
    }

    fun setCpuPlayerId(id : Int) {
        editor.putInt("cpuCaptainId", id)
        editor.apply()
    }



    fun saveMyPass(myPass: Int) {
//        val pass = getMyPass() + myPass
        editor.putInt("myPass", myPass)
        editor.apply()
    }



    fun saveMyNameSuggessionPass(myPass: Int) {
        val pass = getMyNameSuggessionPass() + myPass
        editor.putInt("myNamePass", pass)
        editor.apply()
    }

    fun savecpuNameSuggessionPass(myPass: Int) {
        val pass = getcpuNameSuggessionPass() + myPass
        editor.putInt("cpuNamePass", pass)
        editor.apply()
    }

    fun saveTotalDefence(myPass: Int) {
        val pass = getTotalDefence() + myPass
        editor.putInt("TotalDefence", pass)
        editor.apply()
    }


    fun getMyNameSuggessionPass():Int {
      return  prefs.getInt("myNamePass", 0)

    }
    fun getTotalDefence():Int {
      return  prefs.getInt("TotalDefence", 0)

    }

    fun getcpuId(): String? {
      return  prefs.getString("cpuId", "")

    }

    fun getcpuNameSuggessionPass():Int {
        return  prefs.getInt("cpuNamePass", 0)
    }


    fun getMyPass(): Int {
        return prefs.getInt("myPass", 0)
    }

    fun saveCpuPass(cpuPass: Int) {
//        val pass = getCpuPass() + cpuPass
        editor.putInt("cpuPass", cpuPass)
        editor.apply()
    }

    fun getCpuPass(): Int {
        return prefs.getInt("cpuPass", 0)
    }


    fun saveTimer(startTime: Int) {
        editor.putInt("timer", startTime)
        editor.apply()
    }

    fun increaseTimer(increaseTime: Int) {
        val time = increaseTime + getTimer()
        editor.putInt("timer", time)
        editor.apply()
    }

    fun getTimer(): Int {
        return prefs.getInt("timer", 0)
    }

    fun setGameGameCondition(i: Int) {
        editor.putInt("gameCondition", i)
        editor.apply()
    }

    fun setFirstGamgeStartUser(status : Boolean){
        editor.putBoolean("firstStartUser",status)
        editor.apply()
    }

    fun setExtraTimeUser(status :String){
        editor.putString("ExtraTime",status)
        editor.apply()
    }


    fun setFirstGamgeStartCPU(status : Boolean){
        editor.putBoolean("firstStartCPU",status)
        editor.apply()
    }

    fun getGameCondition(): Int {
        return prefs.getInt("gameCondition", 0)
    }

    fun getFirstGamgeStartUser(): Boolean {
        return prefs.getBoolean("firstStartUser", false)
    }

    fun getFirstGamgeStartCPU(): Boolean {
        return prefs.getBoolean("firstStartCPU", false)
    }

    fun getExtraTime():String? {
        return prefs.getString("ExtraTime", "Normal")
    }

    fun setGameNumber(i: Int) {
        editor.putInt("gameNumber", i)
        editor.apply()
    }

    fun getGameNumber(): Int {
        return prefs.getInt("gameNumber", 1)
    }

    fun setMatchType(type: String) {
        editor.putString("matchType", type)
        editor.apply()
    }

    fun getMatchType(): String? {
        // friendly
        // worldcup
        return prefs.getString("matchType", null)
    }

    fun setGameWin(win: Int) {
        val i = getGameWin() + win
        editor.putInt("gameWin", i)
        editor.apply()
    }

    fun getGameWin(): Int {
        return prefs.getInt("gameWin", 0)
    }

    fun setUserScreenType(status: String) {
        editor.putString("user_screen_Type",status)
        editor.apply()
    }

    fun getUserScreenType() : String {
        return prefs.getString("user_screen_Type", "5-4-1").toString()
    }

    fun setCpuScreenType(status: String) {
        editor.putString("cpu_screen_Type",status)
        editor.apply()
    }

    fun getCpuScreenType() : String {
        return prefs.getString("cpu_screen_Type", "5-4-1").toString()
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true // Fast
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                when {
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                            networkCapabilities.linkDownstreamBandwidthKbps > 2000 -> true // Assume good speed
                    else -> false
                }
            }
            else -> false
        }
    }


    fun  alertError(msg:String){
        val dialog= Dialog(context, R.style.BottomSheetDialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alertbox_error)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams
        val tvTitle: TextView =dialog.findViewById(R.id.tv_title)
        val btnOk: LinearLayout =dialog.findViewById(R.id.btn_ok)
        tvTitle.text=msg
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun  alertErrorSession(msg:String){
        val dialog= Dialog(context, R.style.BottomSheetDialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alertbox_error)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams
        val tvTitle: TextView =dialog.findViewById(R.id.tv_title)
        val btnOk: LinearLayout =dialog.findViewById(R.id.btn_ok)
        tvTitle.text=msg
        btnOk.setOnClickListener {
            dialog.dismiss()
            deleteWholeData()
            setSoundEffect(1)
            val intent = Intent(context, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)


        }
        dialog.show()
    }


    private var toneGenerator: ToneGenerator? = null

    fun playClickSound() {
        try {
            if (toneGenerator == null) {
                toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME)
            }

            if (getSoundEffect() == 1){
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP)
            }
        }catch (e :Exception){
            Log.e("Tone Error",e.toString())
        }


    }


    fun saveListToPreferences( key: String, list: PenalitesUserCPUStore) {
        val gson = Gson()
        val json = gson.toJson(list) // Convert list to JSON
        editor.putString(key, json)
        editor.apply()
    }

    fun saveListToCount( key: String, value:Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun getListFromPreferences( key: String): PenalitesUserCPUStore? {
        val json = prefs.getString(key, null) ?: return null
        return Gson().fromJson(json, PenalitesUserCPUStore::class.java)
    }
    fun getSaveListToCount(key: String): Int{
        return prefs.getInt(key, 0)
    }


    fun showMe(contextData: Context?) {
        dialog?.dismiss()
        dialog = Dialog(context)
        dialog?.setContentView(R.layout.my_progess)
        dialog?.setCancelable(false)
        dialog?.window?.setDimAmount(0f)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.show()
    }

    fun dismissMe() {
        if (dialog != null) {
            dialog?.dismiss()
        }
    }


}