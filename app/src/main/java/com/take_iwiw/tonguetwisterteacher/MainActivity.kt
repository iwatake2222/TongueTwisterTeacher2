package com.take_iwiw.tonguetwisterteacher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import kotlinx.android.synthetic.main.content_main.*
import android.content.Intent
import android.preference.PreferenceManager
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.content.DialogInterface
import android.support.v7.app.AlertDialog

var mainContext: Context? = null   // todo: should be better way to get context

class MainActivity : AppCompatActivity() {
    companion object {
        /*** const  ***/
        val INTENT_REQUEST_CODE = 1000    /* MainActivity <- SpeakActivity */
        val INTENT_OBJ_SELECTED_SENTENCE = "selected_sentence"   /* MainActivity -> SpeakActivity */
        val INTENT_INT_LANGUAGE = "language_id"         /* MainActivity -> SpeakActivity */
        val INTENT_INT_LEVEL = "level_id"            /* MainActivity -> SpeakActivity */
        val PREFERENCE_LANGUAGE_ID = "LANGUAGE_ID"
        val PREFERENCE_LEVEL = "LEVEL"
    }

    /*** settings ***/
    var languageId = 0
    var level      = 0

    /*** objects ***/
    private var sentenceInfoAdapter: SentenceInfoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        Debug.logDebug("Hello");
//        Debug.showToast(this, "Hello");
        mainContext = this
        initSentenceListView()
    }

    override fun onStart() {
        super.onStart()
        loadPreference()

        /* Get sentences from DB, and show them on ListView */
        getSentencesFromDB()

        /* Reset DB for the first use */
        if(sentenceInfoAdapter?.count == 0) {
            resetDB()
            selectLanguage()
        }
    }

    override fun onStop() {
        super.onStop()
        savePreference()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == INTENT_REQUEST_CODE && data != null) {
            val sentenceInfo = data.getSerializableExtra(SpeakActivity.INTENT_OBJ_UPDATED_SENTENCE) as SentenceInfo
            updateSentenceData(sentenceInfo)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.menu_about -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(getString(R.string.text_info_detail)).setPositiveButton("OK") { dialog, id -> }
                builder.show()
            }
            R.id.menu_reset -> {
                AlertDialog.Builder(this)
                    .setTitle(resources.getString(R.string.text_menu_reset))
                    .setMessage("Are you sure?")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        resetDB()
                        getSentencesFromDB()
                    })
                    .setNegativeButton("CANCEL", null)
                    .show()
            }
            R.id.menu_language -> {
                selectLanguage()
            }
            R.id.menu_level -> {
                var level_temp = level
                val strArrayLevel = resources.getStringArray(R.array.strList_level)
                AlertDialog.Builder(this)
                    .setTitle(resources.getString(R.string.text_menu_level))
                    .setSingleChoiceItems(strArrayLevel, level) { dialog, which -> level_temp = which }
                    .setPositiveButton("OK") { dialog, which -> level = level_temp }
                    .setNegativeButton("CANCEL", null)
                    .show()
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    private fun initSentenceListView() {
        var list: ArrayList<SentenceInfo> = ArrayList<SentenceInfo>()
        if (sentenceInfoAdapter == null) {
            sentenceInfoAdapter = SentenceInfoAdapter(this, R.layout.item_sentence, list)
        }
        listView_main_sentenceList.setAdapter(sentenceInfoAdapter)

        /* when a sentence is tapped, call ActivitySpeak */
        listView_main_sentenceList.setOnItemClickListener( { parent, view, position, id ->
            if (parent is ListView) {
                val selectedSentenceInfo = parent.getItemAtPosition(position)
                if(selectedSentenceInfo is SentenceInfo) {
                    Debug.logDebug(selectedSentenceInfo.sentenceMain)
                    val intent = Intent()
                    intent.setClassName("com.take_iwiw.tonguetwisterteacher", "com.take_iwiw.tonguetwisterteacher.SpeakActivity")
                    intent.putExtra(INTENT_OBJ_SELECTED_SENTENCE, selectedSentenceInfo)
                    intent.putExtra(INTENT_INT_LANGUAGE, languageId)
                    intent.putExtra(INTENT_INT_LEVEL, level)
                    startActivityForResult(intent, INTENT_REQUEST_CODE)
                } else {
                    Debug.logError(position.toString())
//                    Debug.showToastDetail(this, position.toString())
                }
            } else {
                Debug.logError(position.toString())
//                Debug.showToastDetail(this, position.toString())
            }
        })
    }

    private fun loadPreference() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        languageId = sharedPreferences.getInt(PREFERENCE_LANGUAGE_ID, 0)
        level = sharedPreferences.getInt(PREFERENCE_LEVEL, 1)
    }

    private fun savePreference() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.putInt(PREFERENCE_LANGUAGE_ID, languageId)
        editor.putInt(PREFERENCE_LEVEL, level)
        editor.commit()
    }

    private fun updateSentenceData(sentence: SentenceInfo) {
        val mydb: SQLiteDatabase
        val hlpr = MySQLiteOpenHelper(applicationContext)
        mydb = hlpr.writableDatabase
        val values = ContentValues()
        values.put("cntAll", sentence.cntAll)
        values.put("cntSuccess", sentence.cntSuccess)
        values.put("record", sentence.timeRecord)
        mydb.update(MySQLiteOpenHelper.TABLE_NAME, values, "_id = " + sentence.dbId, null)
        mydb.close()

        getSentencesFromDB()
    }

    private fun resetDB() {
        val mydb: SQLiteDatabase
        val hlpr = MySQLiteOpenHelper(applicationContext)
        mydb = hlpr.writableDatabase

        mydb.delete(MySQLiteOpenHelper.TABLE_NAME, null, null)

        for (sentence in SentenceInfo.populateDefaultSentences()) {
            val values = ContentValues()
            values.put("languageId", sentence.languageId)
            values.put("sentenceMain", sentence.sentenceMain)
            values.put("sentenceSub", sentence.sentenceSub)
            values.put("pronunciation", sentence.pronunciation)
            values.put("cntAll", sentence.cntAll)
            values.put("cntSuccess", sentence.cntSuccess)
            values.put("record", sentence.timeRecord)
            mydb.insert(MySQLiteOpenHelper.TABLE_NAME, null, values)
        }

        mydb.close()
    }

    private fun getSentencesFromDB() {
        val mydb: SQLiteDatabase
        val hlpr = MySQLiteOpenHelper(applicationContext)
        mydb = hlpr.writableDatabase

        val cursor = mydb.query(MySQLiteOpenHelper.TABLE_NAME, arrayOf("_id", "languageId", "sentenceMain", "sentenceSub", "pronunciation", "cntAll", "cntSuccess", "record"), "languageId = " + languageId.toString(), null, null, null, "_id ASC")

        val indexId = cursor.getColumnIndex("_id")
        val indexLanguageId = cursor.getColumnIndex("languageId")
        val indexSentenceMain = cursor.getColumnIndex("sentenceMain")
        val indexSentenceSub = cursor.getColumnIndex("sentenceSub")
        val indexPronunciation = cursor.getColumnIndex("pronunciation")
        val indexCntAll = cursor.getColumnIndex("cntAll")
        val indexCntSuccess = cursor.getColumnIndex("cntSuccess")
        val indexRecord = cursor.getColumnIndex("record")

        sentenceInfoAdapter?.clear()
        while (cursor.moveToNext()) {
            sentenceInfoAdapter?.add(SentenceInfo(
                cursor.getInt(indexId),
                cursor.getInt(indexLanguageId),
                cursor.getString(indexSentenceMain),
                cursor.getString(indexSentenceSub),
                cursor.getString(indexPronunciation),
                cursor.getInt(indexCntAll),
                cursor.getInt(indexCntSuccess),
                cursor.getDouble(indexRecord))
            )
        }

        cursor.close()
        mydb.close()
    }

    private fun selectLanguage() {
        var languageIdTemp = languageId
        val strArrayLanguage = resources.getStringArray(R.array.strList_language)
        AlertDialog.Builder(this)
            .setTitle("Language to Practice")
            .setSingleChoiceItems(strArrayLanguage, languageId) { dialog, which -> languageIdTemp = which }
            .setPositiveButton("OK") { dialog, which ->
                languageId = languageIdTemp
                getSentencesFromDB()
            }
            .setNegativeButton("CANCEL", null)
            .show()
    }
}
