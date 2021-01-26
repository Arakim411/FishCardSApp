package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.applications.fishcardroomandmvvm.Manager

private const val TAG = "optionsFragment"

class FragmentOptionsViewModel(application: Application) : AndroidViewModel(application) {

    private val manager = Manager.getLearnActivityManager(application)

    var showWithTranslate = manager.getShowWithTranslate()
    var saveData = manager.getSaveData()
    var randomList = manager.getRandomList()
    var showStatic = manager.getShowStatistics()


    fun showWithTranslate(value: Boolean) {
        Log.i(TAG, "ShowWithTranslate changed: $value")
        manager.setShowWithTranslation(value)
        showWithTranslate = value
    }

    fun saveData(value: Boolean) {
        Log.i(TAG, "saveData changed: $value")
        manager.setSaveData(value)
        saveData = value
    }

    fun randomList(value: Boolean) {
        Log.i(TAG, "randomList changed: $value")
        manager.setRandomList(value)
        randomList = value
    }


    fun showStatistics(value: Boolean) {
        Log.i(TAG, "showStatistics changed: $value")
        manager.setShowStatistics(value)
        showStatic = value
    }


}