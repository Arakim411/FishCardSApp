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

    private val _showWordOptions = MutableLiveData<Boolean>()
    val showWordOptions: LiveData<Boolean>
        get() = _showWordOptions

    private val _showSpellingOptions = MutableLiveData<Boolean>()
    val showSpellingOptions: LiveData<Boolean>
        get() = _showSpellingOptions

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

        managerWords = if (type == TYPE_LEARN_WORDS) Manager.getLearnManager(application.baseContext) else null


        _showWordOptions.value = false
        _showSpellingOptions.value = false

        _toolBarAction.value = ToolbarActions.DEFAULT_TITLE

        if(type == TYPE_LEARN_WORDS)
        _currentFragment.value = CurrentFragment.FRAGMENT_LEARN_CONTENT

        if(type == TYPE_LEARN_SPELLING)
            _currentFragment.value = CurrentFragment.FRAGMENT_SPELLING

    }

    //options events
    fun optionsWordIconClicked() {
        if (_showWordOptions.value == true) {
            eventHideOptions()
        } else {
            //_showOptions.value = false
            eventShowOptions()
        }
    }

    fun optionsSpellingIconClicked() {
        if (_showSpellingOptions.value == true) {
            eventHideOptions()
        } else {
            eventShowOptions()
        }
    }



    fun currentFragmentReset(){
        _currentFragment.value = CurrentFragment.NONE
    }

    private fun eventShowOptions() {
        Log.d(TAG,"event showOptions")

        when(type){
            TYPE_LEARN_WORDS ->  _showWordOptions.value = true

            TYPE_LEARN_SPELLING -> _showSpellingOptions.value = true
        }

        _toolBarAction.value = ToolbarActions.ACTION_OPTIONS
    }

     fun eventHideOptions() {
        Log.d(TAG,"eventHideOptions")

         when(type){
             TYPE_LEARN_WORDS ->  _showWordOptions.value = false

             TYPE_LEARN_SPELLING -> _showSpellingOptions.value = false
         }

         _toolBarAction.value = ToolbarActions.DEFAULT_TITLE
    }

    fun setToolBarTitle(title: String){
        _toolBarTitle = title
        _toolBarAction.value = _toolBarAction.value
    }

    enum class CurrentFragment{
        FRAGMENT_LEARN_CONTENT,
        FRAGMENT_SUMMARY,
        FRAGMENT_SPELLING,
        NONE
    }

    enum class ToolbarActions{
        ACTION_OPTIONS,
        DEFAULT_TITLE,
        NONE
    }




}