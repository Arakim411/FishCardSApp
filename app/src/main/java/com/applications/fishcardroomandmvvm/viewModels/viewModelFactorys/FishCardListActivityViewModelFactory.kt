package com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.viewModels.FishCardListActivityViewModel

class FishCardListActivityViewModelFactory(
    val application: Application,
    private val fishCardList: FishCardList
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FishCardListActivityViewModel(application, fishCardList) as T
    }

}