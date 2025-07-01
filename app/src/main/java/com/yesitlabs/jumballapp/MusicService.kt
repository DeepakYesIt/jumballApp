package com.yesitlabs.jumballapp

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class MusicService : Service() {

    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private val musicResources = listOf(
        R.raw.brazilian_world_cup, // 0
        R.raw.bear_stock_music_file_a2_03, // 1
        R.raw.coin_flip_no_catch, // 2
        R.raw.football_theme, //3
        R.raw.heroes_and_champions, // 4
        R.raw.referee_whistle3, // 5
        R.raw.referee_whistle4, //6
        R.raw.referee_whistle_game_over, // 7
        R.raw.soccer_almost_goal_cheer, // 8
        R.raw.soccer_cheer_sound, // 9
        R.raw.soccer_crowd_background, // 10
        R.raw.soccer_crowd_booing, // 11
        R.raw.soccer_game_cheer_background, // 12
        R.raw.soccer_goal_celebration, // 13
        R.raw.soccer_goal_cheer_sound, // 14
        R.raw.soccer_kick_and_goal_celebration,  //15
        R.raw.soccer_kick_sound2, // 16
        R.raw.soccer_penalty_kick_and_goal_cheer,  // 17
        R.raw.whistle_game_over, /// 18
        R.raw.whistle_sound1, // 19
        R.raw.whistle_sound2, //20
        R.raw.soccer_crossbar_hit, //21
        R.raw.soccer_crowd_whistling, //22
        /* R.raw.sad_music_full_version,*/ //20
    )

    private var currentMusicIndex = 100


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val sessionManager = SessionManager(this)

        if (sessionManager.getMusic() == 1) {
            Log.e("Media Player",mediaPlayer.isPlaying.toString())
            if (!mediaPlayer.isPlaying){
                currentMusicIndex = intent?.getIntExtra("value", 0) ?: 0
                val repeat = intent?.getIntExtra("repeat", 0) ?: 0

                Log.e("Select Music", currentMusicIndex.toString())
                mediaPlayer.stop()
                mediaPlayer.release()
                mediaPlayer = try {
                    MediaPlayer.create(this, musicResources[currentMusicIndex])
                }catch (e :Exception){
                    Log.e("Background Sound Error",e.toString())
                    MediaPlayer.create(this, musicResources[0])
                }

                mediaPlayer.setOnCompletionListener {
                    if (repeat == 0){
                        mediaPlayer.release()
                        mediaPlayer = MediaPlayer.create(this, musicResources[0])
                        mediaPlayer.start()
                    }else{
                        mediaPlayer.isLooping = true
                    }
                }

                mediaPlayer.setVolume(1.0f, 1.0f)
                mediaPlayer.start()
            }else if (currentMusicIndex != intent?.getIntExtra("value", 0) ){

                currentMusicIndex = intent?.getIntExtra("value", 0) ?: 0
                val repeat = intent?.getIntExtra("repeat", 0) ?: 0

                Log.e("Select Music", currentMusicIndex.toString())
                mediaPlayer.stop()
                mediaPlayer.release()
                mediaPlayer = try {
                    MediaPlayer.create(this, musicResources[currentMusicIndex])
                }catch (e :Exception){
                    Log.e("Background Sound Error",e.toString())
                    MediaPlayer.create(this, musicResources[0])
                }

                mediaPlayer.setOnCompletionListener {
                    if (repeat == 0){
                        mediaPlayer.release()
                        mediaPlayer = MediaPlayer.create(this, musicResources[0])
                        mediaPlayer.start()
                    }else{
                        mediaPlayer.isLooping = true
                    }
                }

                mediaPlayer.setVolume(1.0f, 1.0f)
                mediaPlayer.start()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onDestroy()
    }


}
