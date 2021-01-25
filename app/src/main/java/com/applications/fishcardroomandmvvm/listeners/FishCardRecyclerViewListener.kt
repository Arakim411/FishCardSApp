package com.applications.fishcardroomandmvvm.listeners

import android.content.Context
import androidx.navigation.NavController
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList

interface FishCardRecyclerViewListener {

    fun onFavoriteChange(fishCardList: FishCardList)
    fun onAddClick(navController: NavController)
    fun onListClick(context: Context, fishCardList: FishCardList)
}