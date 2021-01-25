package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.applications.fishcardroomandmvvm.LanguageManager
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.dataClasses.LanguageSpinnerItem

private const val TAG = "ADD_VIEW_MODEL"

class FragmentAddViewModel(application: Application) : AndroidViewModel(application) {

    private var languageManager: LanguageManager = LanguageManager(application)
    private var fishCardVIewModel: FishCardViewModel = FishCardViewModel(application)
    private val resources: Resources = application.resources

    val listName = MutableLiveData<String>()

    //keeps string value of selected native Language on spinner
    val nativeLanguageName = MutableLiveData<String>()

    //keeps string value of selected foreign Language on spinner
    val foreignLanguageName = MutableLiveData<String>()


    //is set to true when all fields are right
    val correctData = MutableLiveData<Boolean>()

    //variable is true when user successfully add new list
    //when is true this fragment will be close and  user should be moved to homeFragment
    val listAdded = MutableLiveData<Boolean>()

    // app crash when phone rotate, because listeners were added 2 times
    // i know it's simple solution , but i don't see other way how to save app from crash by that way


    val displayToast = MutableLiveData<String>()


    init {
        listName.value = ""
        nativeLanguageName.value = ""
        foreignLanguageName.value = ""
        correctData.value = false
    }


    fun addList() {
        if (correctData.value == true) {
            val listName = listName.value!!
            val nativeLanguageId = languageManager.getLanguageId(nativeLanguageName.value!!)
            val foreignLanguageId = languageManager.getLanguageId(foreignLanguageName.value!!)

            val fishCardList = FishCardList(
                name = listName,
                foreignLanguage = foreignLanguageId,
                nativeLanguage = nativeLanguageId
            )
            fishCardVIewModel.addFishCardList(fishCardList)
            listAdded.value = true
            displayToast.value = resources.getString(R.string.list_added, listName)

            //navigate to home fragment
            Log.d(TAG, "new Fish List added: $fishCardList")
        } else {
            displayToast.value = resources.getString(R.string.not_correct)
        }

    }

    fun getSpinnerItems(): ArrayList<LanguageSpinnerItem> {

        val list = ArrayList<LanguageSpinnerItem>()

        val languagesNames = languageManager.languagesNames
        val languagesDrawable = LanguageManager.languagesDrawable

        for (i in languagesNames.indices) {
            list.add(LanguageSpinnerItem(languagesNames[i], languagesDrawable[i]))
        }


        return list
    }


    fun checkIfDataIsCorrect() {
        // checks if the user has correctly completed the data to create a new list


        correctData.value =
            !(listName.value.isNullOrEmpty() || nativeLanguageName.value.isNullOrEmpty() || foreignLanguageName.value.isNullOrEmpty()
                    || nativeLanguageName.value == foreignLanguageName.value)

        Log.d(TAG, "correctData: ${correctData.value}")
    }
}