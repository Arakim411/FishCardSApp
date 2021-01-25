package com.applications.fishcardroomandmvvm.viewModels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

     val menuBarVisible = MutableLiveData<Boolean>()


    init {
        menuBarVisible.value = true
    }


}