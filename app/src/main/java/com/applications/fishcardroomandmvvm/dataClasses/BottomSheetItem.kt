package com.applications.fishcardroomandmvvm.dataClasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BottomSheetItem(val id: Int, val text: String, val resourceDrawable: Int) : Parcelable
