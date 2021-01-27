package com.applications.fishcardroomandmvvm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applications.fishcardroomandmvvm.R
import kotlinx.android.synthetic.main.translation_item.view.*

class TranslationRecyclerViewAdapter(private val listener: TranslationListEvents) :
    RecyclerView.Adapter<TranslationRecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var translations = emptyList<String>()

    interface TranslationListEvents {
        fun onItemClick(text: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.translation_item, parent, false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.translation.text = translations[position]

        holder.itemView.setOnClickListener {
            listener.onItemClick(translations[position])
        }

    }

    override fun getItemCount(): Int = translations.size

    fun setData(list: List<String>) {
        this.translations = list
        notifyDataSetChanged()
    }


}