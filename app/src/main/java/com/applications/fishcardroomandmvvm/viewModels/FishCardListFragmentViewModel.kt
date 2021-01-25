package com.applications.fishcardroomandmvvm.viewModels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.SearchView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.applications.fishcardroomandmvvm.FISH_LIST_EXTRA
import com.applications.fishcardroomandmvvm.FishListActivity
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.listeners.FishCardRecyclerViewListener
import java.util.*
import kotlin.collections.ArrayList

class FishCardListFragmentViewModel(application: Application) : AndroidViewModel(application),
    FishCardRecyclerViewListener {

     val fishCardVIewModel: FishCardViewModel = FishCardViewModel(application)


    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String>
    get() = _searchText


    init {
        _searchText.value = ""
    }


    override fun onFavoriteChange(fishCardList: FishCardList) {
        val updatedFishCardList = FishCardList(
            id = fishCardList.id,
            name = fishCardList.name,
            nativeLanguage = fishCardList.nativeLanguage,
            foreignLanguage = fishCardList.foreignLanguage,
            favorite = !fishCardList.favorite
        )
        fishCardVIewModel.updateFishCardList(updatedFishCardList)
    }

    override fun onAddClick(navController: NavController) {
        navController.navigate(R.id.action_fishCardListFragment_to_addListFragment)
    }

    override fun onListClick(context: Context, fishCardList: FishCardList) {
            val intent = Intent(context,FishListActivity::class.java)
            intent.putExtra(FISH_LIST_EXTRA,fishCardList)
            context.startActivity(intent)
    }

    fun getSortedFishCardListByText(listName:String,fishCardLists: List<FishCardList>): List<FishCardList>{
        //function return list With fishCard which names contains @listName
        val sortedList = ArrayList<FishCardList>() // contains objects which names contains @listName

        for (i in fishCardLists.iterator()) {
            val name = i.name
            if (name.trim().toLowerCase(Locale.ROOT).contains(listName.trim().toLowerCase(Locale.ROOT)))
                sortedList.add(i)

        }

       return sortedList

    }

    fun getSearchListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                _searchText.value = text

                return false
            }

        }
    }

    fun getSearchCloseListener(): SearchView.OnCloseListener{

        return object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                _searchText.value = ""
                //when  _searchText.value is empty, we trigger listener to load list by favorite
                return  false
            }

        }
    }
    enum class EmptyType{
        NO_LISTS, // list is empty because user didn't add any fishList
        NO_SEARCH // list is empty because any list didn't match to what user was looking for

    }

}