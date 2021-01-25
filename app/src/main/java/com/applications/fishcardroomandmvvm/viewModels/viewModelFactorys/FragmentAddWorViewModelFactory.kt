package com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.viewModels.FishCardListActivityViewModel
import com.applications.fishcardroomandmvvm.viewModels.FragmentAddWordViewModel

class FragmentAddWorViewModelFactory(val application: Application, private val fishCardList: FishCardList): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FragmentAddWordViewModel(application,fishCardList) as T
        }
    }
