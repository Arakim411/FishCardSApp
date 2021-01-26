package com.applications.fishcardroomandmvvm.ROOM.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.ROOM.model.Word

@Dao
interface FishCardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend  fun addList(fishCardList: FishCardList)

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWord(word: Word)

    @Query("SELECT * FROM list_table")
    fun readAllFishLists(): LiveData<List<FishCardList>>

    @Query("SELECT * FROM list_table ORDER BY favorite DESC")
    fun readAllFistListsByFavorite(): LiveData<List<FishCardList>>

    @Query("SELECT * FROM words_table WHERE FishCardListId = :listId ORDER BY word DESC")
    fun getWordsByListId(listId: Int): LiveData<List<Word>>

    @Query("SELECT * FROM list_table WHERE id =:id")
    fun getFishCardListById(id: Int): LiveData<FishCardList>

    @Query("SELECT * FROM list_table WHERE id =:id")
    fun getFishCardList(id: Int): FishCardList

    @Query("SELECT * FROM words_table WHERE FishCardListId = :listId ORDER BY word DESC")
    fun getWordsByListIdAsList(listId: Int): List<Word>


    @Update
    fun updateFishCardList(fishCardList: FishCardList)

    @Delete
    fun deleteFishCardList(fishCardList: FishCardList)

    @Delete
    fun deleteWord(word: Word)



}