package com.applications.fishcardroomandmvvm.ROOM.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applications.fishcardroomandmvvm.ROOM.data.FishCardDao
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.ROOM.model.Word

class FishCardRepository(private val fishDao: FishCardDao) {

    val readAllFishLists: LiveData<List<FishCardList>> = fishDao.readAllFishLists()
    val readAllFistListsByFavorite: LiveData<List<FishCardList>> =
        fishDao.readAllFistListsByFavorite()

    suspend fun addFishCardList(fishCardList: FishCardList) = fishDao.addList(fishCardList)


    suspend fun addWord(word: Word) = fishDao.addWord(word)


    fun getWordsByListId(id: Int): LiveData<List<Word>> = fishDao.getWordsByListId(id)


    fun getFishCardListById(id: Int): LiveData<FishCardList> = fishDao.getFishCardListById(id)


    fun updateFishCardList(fishCardList: FishCardList) = fishDao.updateFishCardList(fishCardList)

    fun deleteFishCardList(fishCardList: FishCardList) = fishDao.deleteFishCardList(fishCardList)

    fun deleteWord(word: Word) = fishDao.deleteWord(word)

    fun getFishCardList(id: Int): FishCardList = fishDao.getFishCardList(id)

    fun getWordsByListIdAsList(listId: Int): List<Word> = fishDao.getWordsByListIdAsList(listId)

    fun updateWord(word: Word) = fishDao.updateWord(word)

}