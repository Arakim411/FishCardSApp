package com.applications.fishcardroomandmvvm.listeners

import android.graphics.Point
import com.applications.fishcardroomandmvvm.ROOM.model.Word

interface WordRecyclerViewListener {
    fun onLongClick(word:Word, x:Float, y:Float)
}