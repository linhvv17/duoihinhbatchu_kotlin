package com.example.duoihinhbatchu

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import java.util.*

class PlayActivity : AppCompatActivity() {

    private var soundPool: SoundPool? = null
    private var soundClick = 0
    private var soundTrue = 0
    private var soundFalse = 0
    private var mMediaPlayer: MediaPlayer? = null
    private var isVolume: Boolean = true

    private var heart = 0
    private var point = 0
    private var handler: Handler? = null
    private var btntiep: Button? = null
    private var txtHeart: TextView? = null
    private var txtPoint: TextView? = null
    private var imgPicture: ImageView? = null
    private var lnAnwser1: LinearLayout? = null
    private var lnAnwser2: LinearLayout? = null
    private var lnCh1: LinearLayout? = null
    private var lnCh2: LinearLayout? = null
    private var listQuestions: MutableList<Question?>? = null
    private var random: Random? = null
    private var i = 0
    private var pst = 0
    private var dapan: String? = null
    private var listChar: MutableList<IDButton?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        initComponets()
        makeQuestion()

        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build()
            SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build()
        } else SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        soundClick = soundPool!!.load(this, R.raw.click_button, 1)
        soundTrue = soundPool!!.load(this, R.raw.answer_true, 1)
        soundFalse = soundPool!!.load(this, R.raw.answer_false, 1)

        mMediaPlayer = MediaPlayer()
        val mediaPath = Uri.parse("android.resource://" + packageName.toString() + "/" + R.raw.background_sound_2)
        try {
            mMediaPlayer!!.setDataSource(applicationContext, mediaPath)
            mMediaPlayer!!.prepare()
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    CHECK_ANWSER -> if (checkAnwser()) {
                        soundPool!!.play(soundTrue, 1f, 1f, 0, 0, 1f)
                        Toast.makeText(this@PlayActivity, "Thiên tài !", Toast.LENGTH_SHORT).show()

                        point += 100
                        txtPoint!!.text = point.toString() + ""
                        run {
                            var i = 16
                            while (i < dapan!!.length + 16) {
                                (findViewById<View>(i) as Button).setBackgroundResource(R.drawable.ic_tile_true)
                                (findViewById<View>(i) as Button).isClickable = false
                                i++
                            }
                        }
                        btntiep!!.visibility = View.VISIBLE
                    } else {
                        heart--
                        txtHeart!!.text = heart.toString() + ""
                        if (heart <= 0) {
                            handler!!.sendEmptyMessage(GAME_OVER)
                            return
                        }
                        soundPool!!.play(soundFalse, 1f, 1f, 0, 0, 1f)
                        Toast.makeText(this@PlayActivity, "Đáp án sai !", Toast.LENGTH_SHORT).show()
                        run {
                            var i = 16
                            while (i < dapan!!.length + 16) {
                                (findViewById<View>(i) as Button).setBackgroundResource(R.drawable.ic_tile_false)
                                i++
                            }
                        }
                        if (heart <= 0) {
                            handler!!.sendEmptyMessage(GAME_OVER)
                        }
                    }
                    GAME_OVER -> {
                        Toast.makeText(this@PlayActivity, "GAME OVER", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else -> {
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        mMediaPlayer!!.isPlaying
        mMediaPlayer!!.start()
    }

    override fun onPause() {
        super.onPause()
        mMediaPlayer!!.pause()
    }

    private fun makeQuestion() {
        val qs = listQuestions!![i]
        dapan = qs!!.context
        val inflater = LayoutInflater.from(this)
        if (dapan!!.length > 8) {
            for (i in 0..7) {
                val view = inflater.inflate(R.layout.item_btn_anwser, lnAnwser1, false) as Button
                view.id = 16 + i
                lnAnwser1!!.addView(view)
                view.setOnClickListener { view ->
                    soundPool!!.play(soundClick, 1f, 1f, 0, 0, 1f)
                    if ((view as Button).text !== "") {
                        (view as Button).text = ""
                        for (i in listChar!!.indices) {
                            if (listChar!![i]!!.idAnwser == view.getId()) {
                                (findViewById<View>(listChar!![i]!!.idPick) as Button).visibility = View.VISIBLE
                                listChar!!.removeAt(i)
                                break
                            }
                        }
                        pst--
                        for (i in 16 until dapan!!.length + 16) {
                            (findViewById<View>(i) as Button).setBackgroundResource(R.drawable.ic_anwser)
                        }
                    }
                }
            }
            for (i in 8 until dapan!!.length) {
                val view = inflater.inflate(R.layout.item_btn_anwser, lnAnwser2, false) as Button
                view.id = 16 + i
                lnAnwser2!!.addView(view)
                view.setOnClickListener { view ->
                    soundPool!!.play(soundClick, 1f, 1f, 0, 0, 1f)
                    if ((view as Button).text !== "") {
                        (view as Button).text = ""
                        for (i in listChar!!.indices) {
                            if (listChar!![i]!!.idAnwser == view.getId()) {
                                (findViewById<View>(listChar!![i]!!.idPick) as Button).visibility = View.VISIBLE
                                listChar!!.removeAt(i)
                                break
                            }
                        }
                        pst--
                        for (i in 16 until dapan!!.length + 16) {
                            (findViewById<View>(i) as Button).setBackgroundResource(R.drawable.ic_anwser)
                        }
                    }
                }
            }
        } else {
            for (i in 0 until dapan!!.length) {
                val view = inflater.inflate(R.layout.item_btn_anwser, lnAnwser1, false) as Button
                view.id = 16 + i
                lnAnwser1!!.addView(view)
                view.setOnClickListener { view ->
                    soundPool!!.play(soundClick, 1f, 1f, 0, 0, 1f)
                    if ((view as Button).text !== "") {
                        (view as Button).text = ""
                        for (i in listChar!!.indices) {
                            if (listChar!![i]!!.idAnwser == view.getId()) {
                                (findViewById<View>(listChar!![i]!!.idPick) as Button).visibility = View.VISIBLE
                                listChar!!.removeAt(i)
                                break
                            }
                        }
                        pst--
                        for (i in 16 until dapan!!.length + 16) {
                            (findViewById<View>(i) as Button).setBackgroundResource(R.drawable.ic_anwser)
                        }
                    }
                }
            }
        }
        imgPicture!!.setImageResource(qs.id)
        val kt = arrayOf("a", "b", "c", "d", "e", "g", "h", "i", "k", "l", "m", "n", "o", "u", "q", "p", "r", "s", "t", "y", "v", "x")
        val tl = ArrayList<String?>()
        for (i in dapan!!.indices) {
            tl.add(dapan!![i].toString() + "")
        }
        for (i in 0 until 16 - dapan!!.length) {
            tl.add(kt[random!!.nextInt(kt.size)])
        }
        tl.shuffle()
        for (i in 0..7) {
            val view = inflater.inflate(R.layout.item_btn, lnCh1, false) as Button
            view.id = i
            view.text = tl[i]
            lnCh1!!.addView(view)
            view.setOnClickListener { view ->
                soundPool!!.play(soundClick, 1f, 1f, 0, 0, 1f)
                if (pst < dapan!!.length) {
                    val btn = view as Button
                    addChar(btn.id, btn.text.toString())
                    btn.visibility = View.INVISIBLE
                }
            }
        }
        for (i in 8..15) {
            val view = inflater.inflate(R.layout.item_btn, lnCh2, false) as Button
            view.id = i
            view.text = tl[i]
            lnCh2!!.addView(view)
            view.setOnClickListener { view ->
                soundPool!!.play(soundClick, 1f, 1f, 0, 0, 1f)
                if (pst < dapan!!.length) {
                    val btn = view as Button
                    addChar(btn.id, btn.text.toString())
                    btn.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun initComponets() {
        listChar = ArrayList<IDButton?>()
        heart = 5
        point = 0
        random = Random()
        btntiep = findViewById<View>(R.id.btn_tiep) as Button
        btntiep!!.setOnClickListener { newQuestion() }
        imgPicture = findViewById<View>(R.id.img_picture) as ImageView
        lnAnwser1 = findViewById<View>(R.id.anwser1) as LinearLayout
        lnAnwser2 = findViewById<View>(R.id.anwser2) as LinearLayout
        lnCh1 = findViewById<View>(R.id.ln_3) as LinearLayout
        lnCh2 = findViewById<View>(R.id.ln_4) as LinearLayout
        txtHeart = findViewById<View>(R.id.txt_heart) as TextView
        txtPoint = findViewById<View>(R.id.txt_point) as TextView
        txtHeart!!.text = heart.toString() + ""
        txtPoint!!.text = point.toString() + ""
        listQuestions = ArrayList()
        listQuestions!!.add(Question(R.drawable.aomua, "aomua"))
        listQuestions!!.add(Question(R.drawable.baocao, "baocao"))
        listQuestions!!.add(Question(R.drawable.canthiep, "canthiep"))
        listQuestions!!.add(Question(R.drawable.cattuong, "cattuong"))
        listQuestions!!.add(Question(R.drawable.chieutre, "chieutre"))
        listQuestions!!.add(Question(R.drawable.danong, "danong"))
        listQuestions!!.add(Question(R.drawable.danhlua, "danhlua"))
        listQuestions!!.add(Question(R.drawable.giandiep, "giandiep"))
        listQuestions!!.add(Question(R.drawable.giangmai, "giangmai"))
        listQuestions!!.add(Question(R.drawable.kiemchuyen, "kiemchuyen"))
        listQuestions!!.add(Question(R.drawable.nambancau, "nambancau"))
        listQuestions!!.add(Question(R.drawable.masat, "masat"))
        listQuestions!!.add(Question(R.drawable.lancan, "lancan"))
        listQuestions!!.add(Question(R.drawable.quyhang, "quyhang"))
        listQuestions!!.add(Question(R.drawable.xedapdien, "xedapdien"))
        listQuestions!!.add(Question(R.drawable.xakep, "xakep"))
        listQuestions!!.add(Question(R.drawable.xaphong, "xaphong"))
        listQuestions!!.add(Question(R.drawable.vuonbachthu, "vuonbachthu"))
        listQuestions!!.add(Question(R.drawable.vuaphaluoi, "vuaphaluoi"))
        listQuestions!!.add(Question(R.drawable.tranhthu, "tranhthu"))
        listQuestions!!.add(Question(R.drawable.totien, "totien"))
        listQuestions!!.add(Question(R.drawable.tichphan, "tichphan"))
        listQuestions!!.add(Question(R.drawable.thattinh, "thattinh"))
        listQuestions!!.add(Question(R.drawable.thothe, "thothe"))
        listQuestions!!.shuffle()
    }

    private fun newQuestion() {
        if (i < listQuestions!!.size - 1) {
            listChar!!.clear()
            btntiep!!.visibility = View.INVISIBLE
            pst = 0
            i++
            lnAnwser1!!.removeAllViews()
            lnAnwser2!!.removeAllViews()
            lnCh1!!.removeAllViews()
            lnCh2!!.removeAllViews()
            makeQuestion()
        } else {
            Toast.makeText(this, "Bạn đã trả lời hết câu hỏi !", Toast.LENGTH_LONG).show()
        }
    }

    fun addChar(id: Int, s: String?) {
        for (i in 16 until dapan!!.length + 16) {
            if ((findViewById<View>(i) as Button).text === "") {
                (findViewById<View>(i) as Button).text = s
                listChar!!.add(IDButton(id, i))
                pst++
                if (pst == dapan!!.length) {
                    handler!!.sendEmptyMessage(CHECK_ANWSER)
                }
                return
            }
        }
    }

    fun checkAnwser(): Boolean {
        var da = ""
        for (i in 16 until dapan!!.length + 16) {
            da += (findViewById<View>(i) as Button).text
        }
        return da == dapan
    }

    companion object {
        private const val CHECK_ANWSER = 0
        private const val GAME_OVER = 1
    }

    fun clickRefresh(view: View) {
        heart -= 1
        txtHeart!!.text = heart.toString() + ""
        lnCh1!!.removeAllViews()
        lnCh2!!.removeAllViews()
        lnAnwser1!!.removeAllViews()
        lnAnwser2!!.removeAllViews()
        newQuestion()

    }

    fun clickCup(view: View) {

    }

    fun clickMute(view: View) {
        if (isVolume){
            isVolume = false
            mMediaPlayer!!.pause()
            view.findViewById<ImageView>(R.id.img_mute).setImageResource(R.drawable.mute)
        } else{
            isVolume = true
            mMediaPlayer!!.start()
            view.findViewById<ImageView>(R.id.img_mute).setImageResource(R.drawable.volume)
        }

//        view.findViewById<ImageView>(R.id.img_volume).visibility = View.VISIBLE
    }

//    fun clickVolume(view: View) {
//        mMediaPlayer!!.start()
//        view.findViewById<ImageView>(R.id.img_volume).visibility = View.GONE
//        view.findViewById<ImageView>(R.id.img_mute).visibility = View.VISIBLE
//    }
}