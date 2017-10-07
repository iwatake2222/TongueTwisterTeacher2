package com.take_iwiw.tonguetwisterteacher

import android.widget.Toast
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log


/**
 * Created by tak on 2017/10/05.
 */
class Debug {
    companion object {
        private val BR = System.getProperty("line.separator")
        private val TAG = "@] TTT: "
        private val IS_DEBUG = true
        fun logDebug(msg: String) {
            if (IS_DEBUG) {
                val callStack = Thread.currentThread().stackTrace[3]
                Log.d(TAG + callStack.fileName + "#" + callStack.methodName + ":" + callStack.lineNumber, msg)
            } else {
                Log.d(TAG, msg)
            }
        }

        fun logError(msg: String) {
            val callStack = Thread.currentThread().stackTrace[3]
            Log.e(TAG + callStack.fileName + "#" + callStack.methodName + ":" + callStack.lineNumber, msg)
        }

        fun showToast(context: Context, msg: String) {
            if (IS_DEBUG) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            }
        }

        fun showToastDetail(context: Context, msg: String) {
            if (IS_DEBUG) {
                val callStack = Thread.currentThread().stackTrace[3]
                Toast.makeText(context, callStack.fileName + "#" + callStack.methodName + ":" + callStack.lineNumber
                        + BR + msg, Toast.LENGTH_LONG).show()
            }
        }
    }
}