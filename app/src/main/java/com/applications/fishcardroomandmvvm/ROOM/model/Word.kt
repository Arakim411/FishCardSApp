package com.applications.fishcardroomandmvvm.ROOM.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "words_table")
data class Word(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "word_id") val id: Int = 0,
    @ColumnInfo(name = "FishCardListId") val fishId: Int,
    var word: String,
    var translation: ArrayList<String>,
    var goodAnswers: Int,
    var badAnswers: Int
) : Parcelable


class TranslationTypeConverter {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String> {

        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: ArrayList<String?>): String {
        return Gson().toJson(list)
    }
}

