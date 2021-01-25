package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.applications.fishcardroomandmvvm.Manager

private const val TAG = "optionsFragment"

class FragmentOptionsViewModel(application: Application) : AndroidViewModel(application) {

    private val manager = Manager.getLearnActivityManager(application)

    val showWithTranslate = manager.getShowWithTranslate()
    val saveData = manager.getSaveData()
    val randomList = manager.getRandomList()
    val animationsOnOFF = manager.getAnimationsOnOff()
    val showStatic = manager.getShowStatistics()


    fun setShowWithTranslate(value: Boolean) {
        Log.i(TAG, "ShowWithTranslate changed: $value")
        manager.setShowWithTranslation(value)
    }

    fun setSaveData(value: Boolean) {
        Log.i(TAG, "saveData changed: $value")
        manager.setSaveData(value)
    }

    fun setRandomList(value: Boolean) {
        Log.i(TAG, "randomList changed: $value")
        manager.setRandomList(value)
    }

    fun setAnimationsOnOFF(value: Boolean) {
        Log.i(TAG, "animationsOnOFF changed: $value")
        manager.setAnimationsOnOff(value)
    }

    fun setShowStatistics(value: Boolean) {
        Log.i(TAG, "showStatistics changed: $value")
        manager.setShowStatistics(value)
    }


}