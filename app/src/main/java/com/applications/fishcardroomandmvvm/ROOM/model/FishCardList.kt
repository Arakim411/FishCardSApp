package com.applications.fishcardroomandmvvm.ROOM.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull



@Parcelize
@Entity(tableName = "list_table")
 data class FishCardList (
    @PrimaryKey(autoGenerate = true) val id :Int = 0,
    @NotNull val name: String,
    @NotNull @ColumnInfo(name = "native_language") val nativeLanguage: String,
    @NotNull val foreignLanguage: String,
    val favorite: Boolean = false
        ):Parcelable













