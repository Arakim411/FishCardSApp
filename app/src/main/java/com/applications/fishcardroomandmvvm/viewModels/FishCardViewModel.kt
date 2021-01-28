package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.applications.fishcardroomandmvvm.ROOM.data.FishCardDatabase
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.ROOM.model.Word
import com.applications.fishcardroomandmvvm.ROOM.repository.FishCardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "FISH_DATABASE"

class FishCardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FishCardRepository
    val readAllFishListsByFavorite: LiveData<List<FishCardList>>


    init {
        val wordListDao = FishCardDatabase.getDatabase(application).wordListDao()
        repository = FishCardRepository(wordListDao)
        readAllFishListsByFavorite = repository.readAllFistListsByFavorite


        Log.d(TAG, "FishViewModel initialized ")
    }

    fun addFishCardList(list: FishCardList) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFishCardList(list)
            Log.d(TAG, "FishCardList added: $list")
        }

    }

    fun addWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWord(word)
            Log.d(TAG, "Word added: $word")
        }
    }

    fun getWordsByListId(id: Int): LiveData<List<Word>> = repository.getWordsByListId(id)

    fun getFishCardListById(id: Int): LiveData<FishCardList> = repository.getFishCardListById(id)

    fun updateFishCardList(fishCardList: FishCardList) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFishCardList(fishCardList)
        }
    }

    fun deleteFishCardList(fishCardList: FishCardList) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFishCardList(fishCardList)
        }
    }

    fun deleteWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWord(word)
        }
    }

    fun getFishCardList(id: Int): FishCardList = repository.getFishCardList(id)

    fun getWordsByListIdAsList(listId: Int): List<Word> = repository.getWordsByListIdAsList(listId)

    fun updateWord(word: Word)  = viewModelScope.launch(Dispatchers.IO) {  repository.updateWord(word)}  }





