package com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applications.fishcardroomandmvvm.viewModels.FragmentLearnViewModel
import com.applications.fishcardroomandmvvm.viewModels.FragmentSpellingViewModel

class FragmentSpellingViewModelFactory(val application: Application, val listId: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FragmentSpellingViewModel(application,listId) as T
    }
}
