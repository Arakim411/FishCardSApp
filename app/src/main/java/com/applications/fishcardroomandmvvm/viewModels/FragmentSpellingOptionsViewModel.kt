package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.applications.fishcardroomandmvvm.Manager

class FragmentSpellingOptionsViewModel(application: Application) : AndroidViewModel(application) {

    val manager = Manager.getSpellingManager(application)

    var showHint = manager.getShowHint()
    var randomList = manager.getRandomList()

    private val _randomListChanged = MutableLiveData<Boolean>()
    val randomListChanged: LiveData<Boolean>
        get() = _randomListChanged

    fun showHint(boolean: Boolean){
        manager.setShowHint(boolean)
        showHint = boolean
    }

    fun randomList(boolean: Boolean){
        manager.setRandomList(boolean)
        randomList = boolean
        _randomListChanged.value = boolean
    }


}