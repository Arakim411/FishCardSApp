package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import android.app.WallpaperColors
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applications.fishcardroomandmvvm.ROOM.model.Word

class EditFragmentViewModel(application: Application, val word: Word) :
    AndroidViewModel(application) {

    private val translations = word.translation
    var translationCount = 0

    private val _updatedWord = MutableLiveData<String>()
    val updatedWord: LiveData<String>
        get() = _updatedWord

    private val _translationList = MutableLiveData<List<String>>()
    val translationList: LiveData<List<String>>
        get() = _translationList

    private val _addTranslation = MutableLiveData<Boolean>()
    val addTranslation: LiveData<Boolean>
        get() = _addTranslation

    private val _closeFragment = MutableLiveData<Boolean>()
    val closeFragment: LiveData<Boolean>
        get() = _closeFragment


    fun setUpdatedWord(text: String?){
        if(text != null) {
            _updatedWord.value = text
        }
    }

    init {
        _translationList.value = translations
        _updatedWord.value = word.word
        _addTranslation.value = true
        _closeFragment.value = false
    }

    fun addTranslationBntClick() {
        _addTranslation.value = true
    }


     fun onSaveInstance(translations: List<String>) {
       _translationList.value = translations
   }

    fun closeFragment() {
        _closeFragment.value = true
    }


}