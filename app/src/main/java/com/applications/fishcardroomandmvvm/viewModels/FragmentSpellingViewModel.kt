package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.applications.fishcardroomandmvvm.LanguageManager
import com.applications.fishcardroomandmvvm.Manager
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.ROOM.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private const val TAG = "SPELLING_VB"

open class FragmentSpellingViewModel(application: Application, val listId: Int) :
    AndroidViewModel(application) {

     data  class Two(val first: Int, val second: Int)

    private val fishCardViewModel = FishCardViewModel(application)
    private lateinit var words: List<Word>
    private var translations: List<String> = emptyList()
    private lateinit var fishCardList: FishCardList
    private val manager = Manager.getSpellingManager(application)

    lateinit  var foreignLanguage: String

    private val green = application.resources.getColor(R.color.green)
    private val red = application.resources.getColor(R.color.red)


    private var searchedTranslation: String = "" //translation  user is likely looking for

    private val finishedText = application.getString(R.string.home)


    private val _infoTextSpannable = MutableLiveData<SpannableString?>()
    val infoTextSpannable: LiveData<SpannableString?>
    get() = _infoTextSpannable

    private val _infoProgressText = MutableLiveData<Two>()
    val infoProgressText: LiveData<Two>
    get() = _infoProgressText

    private val _toolBarTitle = MutableLiveData<String>()
    val toolBarTitle: LiveData<String>
        get() = _toolBarTitle

    private var _listSize: Int = 0
    val listSize: Int
        get() = _listSize

    private val _isGoodTranslation = MutableLiveData<Boolean>()
    val isGoodTranslation: LiveData<Boolean>
        get() = _isGoodTranslation

    private val _nativeFlagResource = MutableLiveData<Int>()
    val nativeFlagResource: LiveData<Int>
        get() = _nativeFlagResource

    private val _foreignFlagResource = MutableLiveData<Int>()
    val foreignFlagResource: LiveData<Int>
        get() = _foreignFlagResource

    private val _listEmpty = MutableLiveData<Boolean>()
    val listEmpty: LiveData<Boolean>
        get() = _listEmpty

    private val _showHint = MutableLiveData<Boolean>()
    val showHint: LiveData<Boolean>
        get() = _showHint

    private val _listFinished = MutableLiveData<Boolean>()
    val listFinished: LiveData<Boolean>
        get() = _listFinished

    private val _nextWordBntText = MutableLiveData<String>()
    val nextWordBntText: LiveData<String>
        get() = _nextWordBntText

    private val _wordIndex = MutableLiveData<Int>()
    val wordIndex: LiveData<Int>
        get() = _wordIndex

    private val _showTranslation = MutableLiveData<Boolean>()
    val showTranslation: LiveData<Boolean>
        get() = _showTranslation

    private val _currentWord = MutableLiveData<Word>()
    val currentWord: LiveData<Word>
        get() = _currentWord

    private val _closeKeyboard = MutableLiveData<Boolean>()
    val closeKeyboard: LiveData<Boolean>
        get() = _closeKeyboard

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { sp, key ->

        when (key) {
            manager.showHint -> {
                _showHint.value = manager.getShowHint()
            }
        }
    }

    init {
        manager.setListener(listener)
        _wordIndex.value = 0
        initializeWords()

        _isGoodTranslation.value = false
        _showTranslation.value = false

        _showHint.value = manager.getShowHint()
    }

    fun onTranslationBntClick(){
        if(_showTranslation.value == false)
            showTranslation()
        else
            hideTranslation()
    }

    private fun initializeWords() {

        viewModelScope.launch(Dispatchers.IO) {
            words = fishCardViewModel.getWordsByListIdAsList(listId)
            fishCardList = fishCardViewModel.getFishCardList(listId)
            val fishCardList = fishCardViewModel.getFishCardList(listId)

            withContext(Dispatchers.Main) {

                if (manager.getRandomList())
                    words = words.shuffled()

                _toolBarTitle.value = fishCardList.name
                _listSize = words.size - 1

                foreignLanguage = fishCardList.foreignLanguage
                val languageManager = LanguageManager(getApplication())
                _nativeFlagResource.value =
                    languageManager.getLanguageDrawableResources(fishCardList.nativeLanguage)
                _foreignFlagResource.value =
                    languageManager.getLanguageDrawableResources(fishCardList.foreignLanguage)



                if (_listSize + 1 < 3) {
                    _listEmpty.value = true
                }
                nextWord()
                Log.i(TAG, "words and listName loaded")
            }
        }

    }

    fun nextWord() {

        if (wordIndex.value == listSize + 1) {
            _listFinished.value = true
            return
        } else if (wordIndex.value == listSize) {
            _nextWordBntText.value = finishedText
        }

            hideTranslation()


        if (_listSize > 0) {
            var index = _wordIndex.value
            _currentWord.value = words[index!!]
            translations = words[index].translation
            _isGoodTranslation.value = false

            searchedTranslation = ""
            Log.i(TAG, "next word ${_currentWord.value}")
            _wordIndex.value = ++index
        }

    }

    private fun hideTranslation() {
        _showTranslation.value = false
    }

    private fun showTranslation(){
        _showTranslation.value = true
    }

    fun onEditTextChanged(text: String) {

        val mText = text.trim().toLowerCase(Locale.ROOT)

        if (mText.isEmpty()) {
            _infoTextSpannable.value = null
            _infoProgressText.value = Two(0,0)
            return
        }

        if (isTranslation(mText)) onGoodTranslationTyped() else onBaTranslationTyped()

        val mostSimilar = getMostSimilarTranslation(mText).trim().toLowerCase(Locale.ROOT)

        _infoTextSpannable.value = getSpannable(mText,mostSimilar)

        _infoProgressText.value = Two(mText.length,mostSimilar.length)

    }

    private fun getMostSimilarTranslation(text: String): String {

        var similarityCount = 0
        var mostSimilar = ""

        for (i in translations.iterator()) {

            var currentSimilarity = 0
            var z = 0

            i.forEach {

                if (z >= text.length)
                    return@forEach

                if (it == text[z]) {
                    currentSimilarity++
                    z++
                }
            }

            if (currentSimilarity > similarityCount) {
                mostSimilar = i
                similarityCount = currentSimilarity
            }

        }

        return mostSimilar
    }

    private fun onGoodTranslationTyped() {
        _isGoodTranslation.value = true
        _closeKeyboard.value = true
    }

    private fun onBaTranslationTyped() {
        _isGoodTranslation.value = false
    }

    private fun getSpannable(userWord: String, targetTranslation: String): SpannableString{

        val coloredText = SpannableString(userWord)

        for(i in userWord.indices){

            if(i <targetTranslation.length &&userWord[i] == targetTranslation[i]){
                coloredText.setSpan(ForegroundColorSpan(green),i,i+1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }else{
                coloredText.setSpan(ForegroundColorSpan(red),i,i+1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        return  coloredText
    }

    private fun isTranslation(text: String): Boolean {

        translations.forEach {
            if (it.toLowerCase(Locale.ROOT)
                    .contentEquals(text.toLowerCase(Locale.ROOT))
            ) return true
        }

        return false

    }



}