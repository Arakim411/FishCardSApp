package com.applications.fishcardroomandmvvm.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.applications.fishcardroomandmvvm.R

import com.applications.fishcardroomandmvvm.dataClasses.LanguageSpinnerItem
import kotlinx.android.synthetic.main.language_spinner_item.view.*


class LanguageSpinnerAdapter constructor(list: ArrayList<LanguageSpinnerItem>) : BaseAdapter() {

    private var list = emptyList<LanguageSpinnerItem>()

    init {
        this.list = list
    }


    override fun getCount(): Int = list.size + 1


    override fun getItem(p0: Int): Any = list[p0]


    override fun getItemId(p0: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        val context = p2?.context
        val view = LayoutInflater.from(context).inflate(R.layout.language_spinner_item, p2, false)

        if (p0 == 0) {
            view.spinner_item_text.text = context?.getString(R.string.select)
        } else {

            val currentItem: LanguageSpinnerItem = list[p0 - 1]

            view.spinner_item_text.text = currentItem.languageName
            view.spinner_item_image.setImageResource(currentItem.languageImage)

        }
        return view
    }


}