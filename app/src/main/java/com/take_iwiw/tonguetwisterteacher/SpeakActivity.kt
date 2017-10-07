package com.take_iwiw.tonguetwisterteacher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.method.ScrollingMovementMethod
import android.view.KeyEvent
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import kotlinx.android.synthetic.main.content_speak.*
import java.util.*

var speakContext: Context? = null   // todo: should be better way to get context

class SpeakActivity : AppCompatActivity(), RecognitionListener {
    companion object {
        /*** const  */
        val COLOR_UNFOCUS = -0x55555556
        val TEXT_SIZE_MAX = 42.0f
        val PREF_PROGRESS_TEXT_SIZE = "PROGRESS_TEXT_SIZE"
        val INTENT_OBJ_UPDATED_SENTENCE = "updated_sentence"    /* MainActivity <- SpeakActivity */
    }

    /*** objects ***/
    var speechRecognizer: SpeechRecognizer? = null
    var sentence: SentenceInfo? = null

    /*** for Timer  */
    var timeStart: Long = 0
    var measuredTime: Double = 0.0
    var timer: Timer? = null
    private var timerTask: TimerTaskCount? = null
    var handlerUI = Handler()    // to attach UI from timer handler

    /*** settings ***/
    var languageId = 0
    var level      = 0
    var progressTextSize: Int = 0
    var isSpeaking: Boolean = false   /* true :from tapping START button to finishing judgement */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speak)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        Debug.logDebug("onCreate")

        speakContext = this
        initView()
        getIntentExtra()
        setViewListener()
    }

    override fun onStart() {
        super.onStart()
        Debug.logDebug("onStart")
        loadPreference()

        /* Init SpeechRecognizer */
        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizer?.setRecognitionListener(this)
        }

        /* Mute ON system sounds to disable "pi" sound at the beginning of speech recognition */
        val amanager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
            amanager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
            amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            amanager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
            amanager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
        } else {
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true)
            amanager.setStreamMute(AudioManager.STREAM_ALARM, true)
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true)
            amanager.setStreamMute(AudioManager.STREAM_RING, true)
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true)
        }

        initStatus()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        drawGauge(0)
    }

    override fun onStop() {
        super.onStop()
        Debug.logDebug("onStop")

        /* finish SpeechRecognizer */
        speechRecognizer?.destroy()
        speechRecognizer = null

        /* Mute OFF system sounds */
        val amanager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
            amanager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, 0);
            amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
            amanager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
            amanager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
        } else {
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false)
            amanager.setStreamMute(AudioManager.STREAM_ALARM, false)
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, false)
            amanager.setStreamMute(AudioManager.STREAM_RING, false)
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false)
        }

        savePreference()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode === KeyEvent.KEYCODE_BACK) {
            val intent = Intent()
            intent.putExtra(INTENT_OBJ_UPDATED_SENTENCE, sentence)
            setResult(Activity.RESULT_OK, intent)
            finish()
            return super.onKeyDown(keyCode, event)
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }

    private fun initView() {
        textView_speak_recognizedSentence.text = getString(R.string.text_speaking_memo)
        textView_speak_sentence.movementMethod = ScrollingMovementMethod.getInstance()
        textView_speak_recognizedSentence.movementMethod = ScrollingMovementMethod.getInstance()
    }

    private fun getIntentExtra() {
        languageId = intent.getIntExtra(MainActivity.INTENT_INT_LANGUAGE, 0)
        level = intent.getIntExtra(MainActivity.INTENT_INT_LEVEL, 1)
        if (level > 2) level = 2
        if (level < 0) level = 0
        sentence = intent.getSerializableExtra(MainActivity.INTENT_OBJ_SELECTED_SENTENCE) as SentenceInfo
        if (sentence is SentenceInfo) {
            textView_speak_sentence.text = sentence?.getSentenceAll()
            textView_speak_record.text = sentence?.getRecordNumString() + ", " + sentence?.getRecordTimeString()
        } else {
            textView_speak_sentence.text = "SYSTEM ERROR"
            Debug.logError("")
            Debug.showToastDetail(this, "System Error")
        }
    }

    private fun setViewListener() {
        seekBar_speak_textSize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textView_speak_sentence.textSize = TEXT_SIZE_MAX * (progress + 10) / 100.0f
                progressTextSize = progress
            }
        })

        button_speak_start.setOnClickListener {
            /* Get Language to recognize */
            val ta = resources.obtainTypedArray(R.array.strList_language)
            val selectedLanguage = ta.getString(languageId)
            var locale = ""
            if (selectedLanguage == resources.getString(R.string.text_menu_language_EN)) {
                locale = Locale.ENGLISH.toString()
            } else if (selectedLanguage == resources.getString(R.string.text_menu_language_JA)) {
                locale = Locale.JAPANESE.toString()
            } else {
                locale = Locale.ENGLISH.toString()
            }

            /* Start Speech recognition */
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale)
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
//            intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
            speechRecognizer?.startListening(intent)

            /* Change views and status */
            isSpeaking = true
            textView_speak_timer.setTextColor(COLOR_UNFOCUS)
            textView_speak_timer.text = "00:00.00"
            button_speak_start.setEnabled(false)
            button_speak_start.text = getString(R.string.text_speaking_button_WAIT)
            button_speak_start.setTextColor(Color.RED)
        }

    }

    private fun loadPreference() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        progressTextSize = sharedPreferences.getInt(PREF_PROGRESS_TEXT_SIZE, 50)
        seekBar_speak_textSize.progress = progressTextSize
        textView_speak_sentence.textSize = TEXT_SIZE_MAX * (progressTextSize + 10) / 100.0f
    }

    private fun savePreference() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.putInt(PREF_PROGRESS_TEXT_SIZE, progressTextSize)
        editor.commit()
    }


    /**
     * Functions for Others
     */
    private fun drawGauge(level: Int) {
        var width = imageView_speak_gauge.getWidth()
        var height = imageView_speak_gauge.getHeight()

        var bitmap = Bitmap.createBitmap(width,  height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        canvas.drawColor(COLOR_UNFOCUS)

        var paint = Paint()
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);

        // Margine = 5
        canvas.drawRect(0 + 5.0f, 0 + 5.0f, ((width - 10) * level/100.0f + 5), height - 5.0f, paint);
        imageView_speak_gauge.setImageBitmap(bitmap);
    }

    private fun updateSentenceInfo(score: Double) {
        sentence?.cntAll = sentence?.cntAll!! + 1
        if (score >= SentenceChecker.JUDGE_SIMILAR_THRESHOLD[level]) {
            sentence?.cntSuccess = sentence?.cntSuccess!! + 1
            if (measuredTime < sentence?.timeRecord!!) {
                sentence?.timeRecord = measuredTime
                textView_speak_timer.setTextColor(Color.RED);
                textView_speak_timer.text = "New Record!!  " + textView_speak_timer.text
            }
        }

        textView_speak_record.text = sentence?.getRecordNumString() + ", " + sentence?.getRecordTimeString()
    }

    private fun initStatus() {
        isSpeaking = false
        button_speak_start.setEnabled(true)
        button_speak_start.setText(getString(R.string.text_speaking_button_START))
        button_speak_start.setTextColor(Color.BLACK)
    }

    /**
     * Functions for Timer
     */
    private fun stopTimer() {
        if(timer == null) return;
        timer?.cancel();
        timer = null;
    }

    private fun startTimer() {
        if (timer != null) return
        timer = Timer()
        timerTask = TimerTaskCount()
        timeStart = System.currentTimeMillis()
        timer?.schedule(timerTask, 0, 10)
    }

    private inner class TimerTaskCount : TimerTask() {
        override fun run() {
            handlerUI.post(Runnable {
                val timeNow = System.currentTimeMillis()
                val timeMil = timeNow - timeStart
                measuredTime = (timeMil!! / 1000.0f).toDouble()
                textView_speak_timer.text = Utility.convertTimeFormat(measuredTime)
            })
        }
    }

    /**
     * Functions for SpeechRecognizer
     */
    override fun onReadyForSpeech(params: Bundle?) {
        Debug.logDebug("onReadyForSpeech")
//        Debug.showToast(speakContext!!, "onReadyForSpeech")
        if (isSpeaking == true) {       // isSpeaking is false when speech recognizer starts for workaround purpose
            button_speak_start.text = getString(R.string.text_speaking_button_SPEAK)
            button_speak_start.setTextColor(Color.GREEN)
        }
    }

    override fun onBeginningOfSpeech() {
        Debug.logDebug("onBeginningOfSpeech")
//        Debug.showToast(speakContext!!, "onBeginningOfSpeech")
        if(isSpeaking) {
            button_speak_start.text = getString(R.string.text_speaking_button_SPEAKING)
            startTimer()
        }
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Debug.logDebug("onBufferReceived")
//        Debug.showToast(speakContext!!, "onBufferReceived")
    }

    override fun onRmsChanged(rmsdB: Float) {
//        Debug.logDebug("onRmsChanged: " + rmsdB + "[dB]")
//        Debug.showToast(speakContext!!, "onRmsChanged")
        /* level = 0 - 100 */
        var level = (10 * Math.pow(10.0, rmsdB / 10.0)).toInt()
        if (level > 100) level = 100
        if (level < 0) level = 0
        drawGauge(level)
    }

    override fun onPartialResults(partialResults: Bundle?) {
        Debug.logDebug("onPartialResults")
//        Debug.showToast(speakContext!!, "onPartialResults")
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Debug.logDebug("onEvent")
//        Debug.showToast(speakContext!!, "onEvent")
    }

    override fun onEndOfSpeech() {
        Debug.logDebug("onEndOfSpeech")
//        Debug.showToast(speakContext!!, "onEndOfSpeech")
        if(isSpeaking) {
            stopTimer();
            button_speak_start.text = getString(R.string.text_speaking_button_JUDGING)
            button_speak_start.setTextColor(Color.GRAY)
        }
    }

    override fun onError(error: Int) {
        Debug.logDebug("onError" + error.toString())
        Debug.showToast(speakContext!!, "onError" + error.toString())
        when (error) {
            SpeechRecognizer.ERROR_AUDIO , SpeechRecognizer.ERROR_CLIENT , SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                textView_speak_recognizedSentence.text = "ERROR:" + Utility.BR +
                        "Please confirm app permission"
            }

            SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT, SpeechRecognizer.ERROR_SERVER, SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> {
                textView_speak_recognizedSentence.text = "Network ERROR:" + Utility.BR +
                        "Please check network, or speak faster." +
                        "If needed, choose the same language as you installed in your device."
            }
            SpeechRecognizer.ERROR_NO_MATCH -> {
                textView_speak_recognizedSentence.text = "Recognition ERROR:" + Utility.BR + "Please speak clearly."
            }
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                textView_speak_recognizedSentence.text = "No Input:" + Utility.BR + "Please speak clearly."
            }
            else -> {
                textView_speak_recognizedSentence.text = "ERROR:"
            }

        }
        stopTimer();
        initStatus();
    }

    override fun onResults(results: Bundle?) {
        Debug.logDebug("onResults")
//        Debug.showToast(speakContext!!, "onResults")

        if (results is Bundle) {
            var recData: ArrayList<String> = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            var scoreMax: Double = 0.0
            var strResult = ""
            for (recognizedSentence in recData) {
                Debug.logDebug(recognizedSentence)

                /* Examine each sentence. Use the highest score */
                var score = 0.0
                val ta = resources.obtainTypedArray(R.array.strList_language)
                val selectedLanguage = ta.getString(languageId)
                if (selectedLanguage == resources.getString(R.string.text_menu_language_EN)) {
                    score = SentenceChecker.checkSimilar(sentence!!.sentenceMain, recognizedSentence)
                } else if (selectedLanguage == resources.getString(R.string.text_menu_language_JA)) {
                    score = SentenceChecker.checkSimilarJa(sentence!!.sentenceMain, sentence!!.pronunciation, recognizedSentence)
                } else {
                    score = SentenceChecker.checkSimilar(sentence!!.sentenceMain, recognizedSentence)
                }

                /* Show result */
                strResult += " - " + recognizedSentence + " (score = " + String.format("%.4f", score) + ")" + Utility.BR
                if (score > scoreMax) {
                    scoreMax = score
                }
            }

            when {
                scoreMax == 1.0  -> {strResult = "PERFECT : score = " + String.format("%.4f", scoreMax) + Utility.BR + strResult}
                scoreMax == 0.95 -> {strResult = "EXCELLENT : score = " + String.format("%.4f", scoreMax) + Utility.BR + strResult}
                scoreMax == 0.90 -> {strResult = "GREAT : score = " + String.format("%.4f", scoreMax) + Utility.BR + strResult}
                scoreMax >= SentenceChecker.JUDGE_SIMILAR_THRESHOLD[level] -> {strResult = "GOOD : score = " + String.format("%.4f", scoreMax) + Utility.BR + strResult}
                else -> {strResult = "BAD : score = " + String.format("%.4f", scoreMax) + Utility.BR + strResult}
            }
            textView_speak_recognizedSentence.text = strResult
            updateSentenceInfo(scoreMax)
        }

        initStatus()

        // workaround to get onRmsChanged
        // refer: https://stackoverflow.com/questions/42321965/onrmschanged-not-called-after-first-time-in-recognitionlistener-android
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toString())
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        speechRecognizer?.startListening(intent)
        speechRecognizer?.cancel()

    }
}
