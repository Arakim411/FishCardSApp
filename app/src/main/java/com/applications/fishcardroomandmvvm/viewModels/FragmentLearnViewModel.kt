package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.applications.fishcardroomandmvvm.LanguageManager
import com.applications.fishcardroomandmvvm.Manager
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.ROOM.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "FragmentLearnModel"

class FragmentLearnViewModel(application: Application, val listId: Int) :
    AndroidViewModel(application) {

    private val fishCardViewModel = FishCardViewModel(application)
    private lateinit var words: List<Word>
    private lateinit var fishCardList: FishCardList
    private val manager = Manager.getLearnActivityManager(application)

    private var _listSize: Int = 0
    val listSize: Int
        get() = _listSize

    // MutableLiveData (sharedPreferences)

    private val _showWithTranslate = MutableLiveData<Boolean>()
    val showWithTranslate: LiveData<Boolean>
        get() = _showWithTranslate

    private val _saveData = MutableLiveData<Boolean>()
    val saveData: LiveData<Boolean>
        get() = _saveData

    private val _randomList = MutableLiveData<Boolean>()
    val randomList: LiveData<Boolean>
        get() = _randomList

    private val _showStatistics = MutableLiveData<Boolean>()
    val showStatistics: LiveData<Boolean>
        get() = _showStatistics


    // MutableLiveData

    private val _isDataLoaded = MutableLiveData<Boolean>()  // when we load full data from dateBase
    val isDataLoaded: LiveData<Boolean>
        get() = _isDataLoaded

    private val _toolBarTitle = MutableLiveData<String>()
    val toolBarTitle: LiveData<String>
        get() = _toolBarTitle

    private val _currentWord = MutableLiveData<Word>()
    val currentWord: LiveData<Word>
        get() = _currentWord

    private val _wordIndex = MutableLiveData<Int>()
    val wordIndex: LiveData<Int>
        get() = _wordIndex

    private val _listEmpty = MutableLiveData<Boolean>()
    val listEmpty: LiveData<Boolean>
        get() = _listEmpty

    private val _nativeFlagResource = MutableLiveData<Int>()
    val nativeFlagResource: LiveData<Int>
        get() = _nativeFlagResource

    private val _foreignFlagResource = MutableLiveData<Int>()
    val foreignFlagResource: LiveData<Int>
        get() = _foreignFlagResource

    private val _translationBntType = MutableLiveData<ButtonType>()
    val translationButtonType: LiveData<ButtonType>
    get() = _translationBntType


    private val _showTranslation = MutableLiveData<Boolean>()
    val showTranslation: LiveData<Boolean>
    get() = _showTranslation




    init {
        _isDataLoaded.value = false
        _wordIndex.value = 0
        initializeWords()

        _showWithTranslate.value = manager.getShowWithTranslate()
        _saveData.value = manager.getSaveData()
        _randomList.value = manager.getRandomList()
        _showStatistics.value = manager.getShowStatistics()

        _translationBntType.value = ButtonType.SHOW_TRANSLATION


        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sp, key ->

            Log.d(TAG,"sharedPreferences Listener with key: $key")
            when (key) {
                manager.showWithTranslate -> {
                    _showWithTranslate.value = sp.getBoolean(key, false)
                }

                manager.saveData -> {
                    _saveData.value = sp.getBoolean(key, false)
                }

                manager.randomList -> {
                    _randomList.value = sp.getBoolean(key, false)
                }

                manager.showStatistics -> {
                    _showStatistics.value = sp.getBoolean(key, false)
                }
            }
        }

        manager.setListener(listener)

    }

    private fun initializeWords() {

        viewModelScope.launch(Dispatchers.IO) {
            words = fishCardViewModel.getWordsByListIdAsList(listId)
            fishCardList = fishCardViewModel.getFishCardList(listId)
            val fishCardList = fishCardViewModel.getFishCardList(listId)

            withContext(Dispatchers.Main) {

                _isDataLoaded.value = true
                _toolBarTitle.value = fishCardList.name
                _listSize = words.size

                val languageManager = LanguageManager(getApplication())
                _nativeFlagResource.value =
                    languageManager.getLanguageDrawableResources(fishCardList.nativeLanguage)
                _foreignFlagResource.value =
                    languageManager.getLanguageDrawableResources(fishCardList.foreignLanguage)

                if (_listSize < 3) {
                    _listEmpty.value = true
                }
                nextWord()
                Log.i(TAG, "words and listName loaded")
            }
        }

    }

    fun nextWord() {
        if (_listSize > 0) {
            var index = _wordIndex.value
            _currentWord.value = words[index!!]

            _wordIndex.value = ++index
        }

        if(_showWithTranslate.value == false){
            hideTranslation()
        }
    }

    fun onTranslationBntClick(){
       when(_translationBntType.value) {

           ButtonType.SHOW_TRANSLATION -> showTranslation()

           ButtonType.HIDE_TRANSLATION -> hideTranslation()
       }
    }

      fun showTranslation(){
          _showTranslation.value = true
        _translationBntType.value = ButtonType.HIDE_TRANSLATION
    }

    fun hideTranslation(){
        _translationBntType.value = ButtonType.SHOW_TRANSLATION
        _showTranslation.value = false
    }

    enum class ButtonType{
        SHOW_TRANSLATION,
        HIDE_TRANSLATION
    }

}
