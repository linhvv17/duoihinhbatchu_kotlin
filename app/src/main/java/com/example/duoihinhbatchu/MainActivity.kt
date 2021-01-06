package com.example.duoihinhbatchu

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var soundPool: SoundPool? = null
    private var soundClick = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btn_play).setOnClickListener(this)

        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build()
            SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build()
        } else SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        soundClick = soundPool!!.load(this, R.raw.click, 1)

    }

    override fun onClick(view: View) {
        soundPool!!.play(soundClick, 1f, 1f, 0, 0, 1f)
        startActivity(Intent(this, PlayActivity::class.java))
    }
}