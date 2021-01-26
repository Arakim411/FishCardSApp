package com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applications.fishcardroomandmvvm.viewModels.FragmentLearnViewModel
import com.applications.fishcardroomandmvvm.viewModels.LearnViewModel

class FragmentLearnViewModelFactory(val application: Application, val listId: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FragmentLearnViewModel(application,listId) as T
    }
}
