package com.applications.fishcardroomandmvvm.viewModels



import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.ROOM.model.Word
import com.applications.fishcardroomandmvvm.customViews.ChoiceWindow
import com.applications.fishcardroomandmvvm.fragments.EditFragment
import com.applications.fishcardroomandmvvm.fragments.FragmentAddWord
import com.applications.fishcardroomandmvvm.fragments.TAG_ADD_FRAGMENT
import com.applications.fishcardroomandmvvm.fragments.TAG_EDIT_FRAGMENT
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FragmentWordsViewModel(application: Application) : AndroidViewModel(application) {


    //fab
    private val fabConfirmIcon = application.getDrawable(R.drawable.ic_baseline_check_24)
    private val fabOutPutIcon = application.getDrawable(R.drawable.ic_add)


    lateinit var childFragmentManager: FragmentManager

    private val fishCardViewModel: FishCardViewModel = FishCardViewModel(application)

    //variables
    private var fabNextAction = FabAction.SHOW_WINDOW_ADD_WORD
    lateinit var fishCardList: FishCardList


    //MutableLiveData
    private val _performAction = MutableLiveData<PerformAction>()
    val performAction: LiveData<PerformAction>
        get() = _performAction

    private val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String>
        get() = _showToast

     val choiceWindow = MutableLiveData<ChoiceWindow?>()

    private val _showEditFragment = MutableLiveData<Word?>()
    val showEditFragment: LiveData<Word?>
        get() = _showEditFragment

    private val _fabIcon = MutableLiveData<Drawable>()
    val fabIcon: LiveData<Drawable>
    get() = _fabIcon

    init {
        _performAction.value = PerformAction.PERFORM_NONE
        choiceWindow.value = null
    }


    fun onFabClicked(fab: FloatingActionButton) {

        when (fabNextAction) {

            FabAction.SHOW_WINDOW_ADD_WORD -> {

                _performAction.value = PerformAction.PERFORM_SHOW_WINDOW_ADD_WORD
                fabNextAction = FabAction.ADD_WORD
                _fabIcon.value = fabConfirmIcon
            }

            FabAction.ADD_WORD -> {
                // i hope it is good approach to do it here
                val addWordFragment =
                    (childFragmentManager.findFragmentByTag(TAG_ADD_FRAGMENT) as FragmentAddWord)

                val word = addWordFragment.getWordToAdd()

                if (word != null) {
                    fishCardViewModel.addWord(word)
                    _showToast.value = "word added ${word.word}"
                    childFragmentManager.beginTransaction().remove(addWordFragment).commit()

                    resetFab()
                } else {
                    _showToast.value = "field are not correct"
                }
            }

            FabAction.SAVE_EDIT -> {


                val editWordFragment = childFragmentManager.findFragmentByTag(TAG_EDIT_FRAGMENT) as EditFragment

                    val updatedWord = editWordFragment.getUpdatedWord()

                if(updatedWord == null){
                    _showToast.value = "Make correct changes"
                }else{
                    fishCardViewModel.updateWord(updatedWord)
                    childFragmentManager.beginTransaction().remove(editWordFragment).commit()
                    _showToast.value = "Word Updated"
                    resetFab()
                }
            }

            else -> {}
        }

    }


    fun getWords(): LiveData<List<Word>> = fishCardViewModel.getWordsByListId(fishCardList.id)

     fun resetFab() {
        fabNextAction = FabAction.SHOW_WINDOW_ADD_WORD
       _fabIcon.value = fabOutPutIcon

    }


    private enum class FabAction {
        //variables describes which actions will do fab on next click
        SHOW_WINDOW_ADD_WORD,
        ADD_WORD,
        SAVE_EDIT,
        NONE
    }

    enum class PerformAction {
        PERFORM_SHOW_WINDOW_ADD_WORD,
        PERFORM_NONE
    }

    fun deleteWord(word: Word){
        fishCardViewModel.deleteWord(word)
    }

    fun editWord(word: Word){
    _showEditFragment.value = word
        fabNextAction = FabAction.SAVE_EDIT
     _fabIcon.value = fabConfirmIcon
    }


    fun onAddFragmentRemovedByUser() {
        resetFab()
    }

    override fun onCleared() {
        resetFab()
        super.onCleared()
    }

    fun setPerformAsNone() {
        _performAction.value = PerformAction.PERFORM_NONE
    }

    fun getInstanceFragmentAddWord(): FragmentAddWord = FragmentAddWord.newInstance(fishCardList)

    fun resetToastMessage() {
        _showToast.value = ""
    }



}