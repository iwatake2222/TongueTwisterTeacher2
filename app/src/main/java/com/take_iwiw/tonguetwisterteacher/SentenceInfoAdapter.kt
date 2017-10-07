package com.take_iwiw.tonguetwisterteacher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_sentence.view.*

/**
 * Created by tak on 2017/10/05.
 */
class SentenceInfoAdapter : ArrayAdapter<SentenceInfo> {
    private var layoutInflater: LayoutInflater? = null

    constructor(context: Context?, resource: Int, objects: ArrayList<SentenceInfo>) : super(context, resource, objects) {
        this.layoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = layoutInflater?.inflate(R.layout.item_sentence, parent, false)
        if(v is View) {
            v.textView_item_sentence.text = getItem(position).sentenceMain
            v.textView_item_recordCnt.text = getItem(position).getRecordNumString()
            v.textView_item_recordTime.text = getItem(position).getRecordTimeString()
            return v
        }

        return v!!
    }

}