package com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.ROOM.model.Word
import com.applications.fishcardroomandmvvm.viewModels.EditFragmentViewModel
import com.applications.fishcardroomandmvvm.viewModels.FishCardListActivityViewModel

class EditFragmentViewModelFactory(
    val application: Application,
    val word: Word
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditFragmentViewModel(application, word) as T
    }

}