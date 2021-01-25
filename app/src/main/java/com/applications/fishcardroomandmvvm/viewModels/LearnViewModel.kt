package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applications.fishcardroomandmvvm.Manager

class LearnViewModel(application: Application, val type: Int) : AndroidViewModel(application) {

    //managers for every type
     var managerWords: Manager.WordsManager? = null
    //private var spellingManager to do

    //MutableLiveData

    private val _showOptions = MutableLiveData<Boolean>()
    val showOptions: LiveData<Boolean>
    get() = _showOptions


    init {

        managerWords =
            if (type == TYPE_LEARN_WORDS) Manager.getLearnActivityManager(application.baseContext)
            else null
        //TODO throw error when we don't know type

        _showOptions.value = false
    }

        //options events
        fun eventShowOptions() { _showOptions.value = true }
        fun eventHideOptions() {_showOptions.value = false}






}