package com.applications.fishcardroomandmvvm.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.applications.fishcardroomandmvvm.LanguageManager
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.listeners.FishCardRecyclerViewListener
import com.applications.fishcardroomandmvvm.viewModels.FishCardListFragmentViewModel
import kotlinx.android.synthetic.main.fish_card_list_item.view.*
import kotlinx.android.synthetic.main.fish_list_empty.view.*


private const val VIEW_TYPE_FISH_ITEM = 0
private const val VIEW_TYPE_EMPTY_NO_LISTS = 1 // user didn't add any list
private const val VIEW_TYPE_EMPTY_SEARCH = 2 // user search but can't see result


class FishCardRecycleViewAdapter(private val listener: FishCardRecyclerViewListener) :
    RecyclerView.Adapter<FishCardRecycleViewAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    private var fishLists = emptyList<FishCardList>()
    private var emptyType =
        FishCardListFragmentViewModel.EmptyType.NO_LISTS // what should display list when there is no items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return when (viewType) {
            VIEW_TYPE_FISH_ITEM -> MyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.fish_card_list_item, parent, false)
            )
            VIEW_TYPE_EMPTY_NO_LISTS -> MyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.fish_list_empty, parent, false)
            )
            VIEW_TYPE_EMPTY_SEARCH ->  MyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.fish_search_empty, parent, false)
            )
            else -> throw  Error("Unknown viewType")
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // when position is 0 we don't have to set  nothing, we just display info view

        val drawableFav = holder.itemView.context.getDrawable(R.drawable.ic_baseline_favorite_24)
        val drawableFavBorder =
            holder.itemView.context.getDrawable(R.drawable.ic_baseline_favorite_border_24)

        when (holder.itemViewType) {
            VIEW_TYPE_FISH_ITEM -> {
                val fishList = fishLists[position]
                val languageManager = LanguageManager(holder.itemView.context)

                //setting name and flags
                holder.itemView.list_name.text = fishList.name
                holder.itemView.native_flag.setImageResource(
                    languageManager.getLanguageDrawableResources(
                        fishList.nativeLanguage
                    )
                )
                holder.itemView.foreign_flag.setImageResource(
                    languageManager.getLanguageDrawableResources(
                        fishList.foreignLanguage
                    )
                )
                //setting position
                holder.itemView.count_text.text = (position + 1).toString()

                //setting favorite
                if (fishList.favorite) {
                    holder.itemView.isFavorite.setImageDrawable(drawableFav)
                } else {
                    holder.itemView.isFavorite.setImageDrawable(drawableFavBorder)
                }

                holder.itemView.isFavorite.setOnClickListener {

                    listener.onFavoriteChange(fishList)
                }

                //setting listClick
                holder.itemView.onClickView.setOnClickListener {
                    listener.onListClick(holder.itemView.context,fishList)
                }

            }
            VIEW_TYPE_EMPTY_NO_LISTS -> {
                holder.itemView.bnt_add.setOnClickListener {
                    listener.onAddClick(holder.itemView.findNavController())
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return   if(fishLists.isEmpty()) 1 else fishLists.size
    }

    override fun getItemViewType(position: Int): Int {

        return if (fishLists.isEmpty()) {
            if (emptyType == FishCardListFragmentViewModel.EmptyType.NO_LISTS) {
                VIEW_TYPE_EMPTY_NO_LISTS
            } else {
                VIEW_TYPE_EMPTY_SEARCH
            }

        } else {
            VIEW_TYPE_FISH_ITEM
        }
    }

    fun setData(list: List<FishCardList>) {
        this.fishLists = list
        notifyDataSetChanged()

    }

    fun setEmptyType (type: FishCardListFragmentViewModel.EmptyType){
        emptyType = type
    }

}





