package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applications.fishcardroomandmvvm.Manager

private const val TAG = "learnViewModel"

class LearnViewModel(application: Application, val type: Int) : AndroidViewModel(application) {

    //managers for every type
    private  var managerWords: Manager.WordsManager? = null
    //private var spellingManager to do

    //MutableLiveData

    private val _showOptions = MutableLiveData<Boolean>()
    val showOptions: LiveData<Boolean>
        get() = _showOptions

    private val _toolBarAction = MutableLiveData<ToolbarActions>()
    val toolbarAction: LiveData<ToolbarActions>
    get() = _toolBarAction

    private val _currentFragment = MutableLiveData<CurrentFragment>()
    val currentFragment: LiveData<CurrentFragment>
    get() = _currentFragment

    private var _toolBarTitle = ""
    val toolBarTitle: String
    get() = _toolBarTitle



    init {

        managerWords =
            if (type == TYPE_LEARN_WORDS) Manager.getLearnActivityManager(application.baseContext)
            else null
        //TODO throw error when we don't know type

        _showOptions.value = false
        _toolBarAction.value = ToolbarActions.DEFAULT_TITLE
        _currentFragment.value = CurrentFragment.FRAGMENT_LEARN_CONTENT
    }

    //options events
    fun optionsIconClicked() {
        if (_showOptions.value == true) {
            eventHideOptions()
        } else {
            //_showOptions.value = false
            eventShowOptions()
        }
    }

    fun currentFragmentReset(){
        _currentFragment.value = CurrentFragment.NONE
    }

    private fun eventShowOptions() {
        Log.d(TAG,"event showOptions")
        _showOptions.value = true
        _toolBarAction.value = ToolbarActions.ACTION_OPTIONS
    }

     fun eventHideOptions() {
        Log.d(TAG,"eventHideOptions")
        _showOptions.value = false
         _toolBarAction.value = ToolbarActions.DEFAULT_TITLE
    }

    fun setToolBarTitle(title: String){
        _toolBarTitle = title
        _toolBarAction.value = _toolBarAction.value
    }

    enum class CurrentFragment{
        // main fragments in this activity
        FRAGMENT_LEARN_CONTENT,
        FRAGMENT_SUMMARY,
        NONE
    }

    enum class ToolbarActions{
        ACTION_OPTIONS,
        DEFAULT_TITLE,
        NONE
    }




}