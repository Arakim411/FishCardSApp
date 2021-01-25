package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applications.fishcardroomandmvvm.LanguageManager
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList


class FragmentAddWordViewModel(application: Application, val fishCardList: FishCardList): AndroidViewModel(application) {

    var word: String? = null
    var translation = ArrayList<String>()

    private var languageManager = LanguageManager(application)

    //resources
    val nativeFlag = languageManager.getLanguageDrawableResources(fishCardList.nativeLanguage)
    val foreignFlag = languageManager.getLanguageDrawableResources(fishCardList.foreignLanguage)

    //variables
    var translationCount = 0

    private val _addTranslations = MutableLiveData<Any>() // how much is translation fields in view
    val addTranslations: LiveData<Any>
    get() = _addTranslations

    init {
        _addTranslations.value = 2
    }


    fun addTranslation(translation: Any?){
        _addTranslations.value = translation
    }

    fun addTranslationBntClick(){
        _addTranslations.value = 1
    }



}