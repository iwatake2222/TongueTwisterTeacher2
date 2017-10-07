package com.take_iwiw.tonguetwisterteacher

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


/**
 * Created by tak on 2017/10/05.
 */
class MySQLiteOpenHelper(c: Context) : SQLiteOpenHelper(c, DB, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Debug.logDebug("UpgradeDB")
//        Debug.showToast(mainContext!!, "Upgrade DB")
        try {
            db.execSQL(DROP_TABLE)
        } catch (e: Exception) {
            Debug.logError(e.toString())
//            Debug.showToastDetail(mainContext!!, e.toString())
        }
        onCreate(db)
    }

    companion object {
        val DB = "sqlite_tonguetwisterteacher.db"
        val TABLE_NAME = "sentences"
        val DB_VERSION = 3
        val CREATE_TABLE = "create table " + TABLE_NAME + " (" +
                "_id integer primary key autoincrement, " +
                "languageId integer, " +          // 0 = English, 1 = Japanese
                "sentenceMain text not null, " +
                "sentenceSub text, " +          // for Japanese (katakana, ro-ma ji)
                "pronunciation text, " +        // for Japanese (katakana comes here)
                "cntAll integer, " +
                "cntSuccess integer, " +
                "record real " +
                ")"
        val DROP_TABLE = "drop table " + TABLE_NAME + ";"
    }
}
