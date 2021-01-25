package com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.viewModels.FragmentAddWordViewModel
import com.applications.fishcardroomandmvvm.viewModels.LearnViewModel

class LearnViewModelFactory(val application: Application,val type: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LearnViewModel(application,type) as T
    }
}
