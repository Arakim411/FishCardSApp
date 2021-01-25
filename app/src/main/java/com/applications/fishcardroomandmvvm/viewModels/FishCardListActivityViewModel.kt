package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.dataClasses.BottomSheetItem
import com.applications.fishcardroomandmvvm.fragments.FragmentWordsList
import com.applications.fishcardroomandmvvm.listeners.FabListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val TYPE_LEARN_WORDS = 0  // app shows word and user must tick if he know or don't know word translation
const val TYPE_LEARN_SPELLING = 1 // app shows word and user must write this word in other language

const val NOT_START = -1


class FishCardListActivityViewModel(application: Application, fishCardList: FishCardList) :
    AndroidViewModel(application) {

    private var resources = application.resources

    private val fishCardViewModel = FishCardViewModel(application)

    var fragmentWordList: FragmentWordsList? = null
    lateinit var fragmentWordsListener: FabListener

    private var currentActiveFragment: AvailableFragments

    private val _loadFragment = MutableLiveData<AvailableFragments>()
    val loadFragment: LiveData<AvailableFragments>
        get() = _loadFragment

    private val _listName = MutableLiveData<String>()
    val listName: LiveData<String>
        get() = _listName

    private val _showDialogDelete = MutableLiveData<Boolean>()
    val showDialogDelete: LiveData<Boolean>
        get() = _showDialogDelete

    private val _startLearnActivity = MutableLiveData<Int>() //start activity with value in intent extra, but don't start when value is
    val startLearnActivity: LiveData<Int>
        get() = _startLearnActivity


    private var _fishCardList: FishCardList = fishCardList
    val fishCardList: FishCardList
        get() = _fishCardList

    init {
        _loadFragment.value = AvailableFragments.FragmentWordsList
        currentActiveFragment = AvailableFragments.FragmentWordsList

        _listName.value = _fishCardList.name

        _showDialogDelete.value = false

    }


    fun onFabClicked(view: View) {
        when (currentActiveFragment) {

            AvailableFragments.FragmentWordsList -> {
                fragmentWordsListener.onFabClicked(view as FloatingActionButton)
            }
            else -> {}
        }
    }

    fun getBottomSheetItems(): ArrayList<BottomSheetItem> = arrayListOf(
        BottomSheetItem(
            TYPE_LEARN_WORDS,
            resources.getString(R.string.bottom_id_words_title),
            R.drawable.ic_baseline_arrow_forward_24
        ),
        BottomSheetItem(
            TYPE_LEARN_SPELLING,
            resources.getString(R.string.bottom_id_spelling_title),
            R.drawable.ic_baseline_spellcheck_24
        )
    )


    enum class AvailableFragments {
        FragmentWordsList,
        NONE,
    }

    fun showDeleteDialog() {
        _showDialogDelete.value = true
    }

    fun resetShowDialogDelete() {
        _showDialogDelete.value = false
    }


    fun deleteCurrentList() {
        fishCardViewModel.deleteFishCardList(_fishCardList)

    }

    fun updateListDatabase(fishCardList: FishCardList) {
        fishCardViewModel.updateFishCardList(fishCardList)
        _listName.value = fishCardList.name
        this._fishCardList = fishCardList
    }

    fun initializeFragmentWordList() {
        fragmentWordList = FragmentWordsList.newInstance(_fishCardList)
        fragmentWordsListener = fragmentWordList!!
    }

    fun setLoadAsNone() {
        //that function prevents fragment from reset when we change instate state(fragment won't be loaded again and user won't lose his state)
        currentActiveFragment = _loadFragment.value!!
        _loadFragment.value = AvailableFragments.NONE
    }

    fun onBottomItemClick(id: Int) {
        _startLearnActivity.value = id
    }

    fun resetActivityStarting(){
        _startLearnActivity.value = NOT_START
    }


}