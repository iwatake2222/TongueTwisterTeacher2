package com.take_iwiw.tonguetwisterteacher

/**
 * Created by tak on 2017/10/06.
 */
class Utility {
    companion object {
        val BR = System.getProperty("line.separator")
        
        /* Convert time[sec] to "00:00.00" */
        fun convertTimeFormat(time: Double): String {
            val timeInt = time.toInt()
            return String.format("%02d:%02d.%02d", timeInt / 60 % 60, timeInt % 60, ((time - timeInt) * 100).toInt())
        }
    }

}