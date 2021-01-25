@file:Suppress("DEPRECATION")

package com.applications.fishcardroomandmvvm.fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.adapters.FishCardRecycleViewAdapter
import com.applications.fishcardroomandmvvm.databinding.FragmentFishCardListsBinding
import com.applications.fishcardroomandmvvm.viewModels.FishCardListFragmentViewModel


class FishCardListsFragment : Fragment() {

    private lateinit var binding: FragmentFishCardListsBinding
    private lateinit var mViewModel: FishCardListFragmentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_fish_card_lists,
            container,
            false
        )

        setHasOptionsMenu(true)

        binding.lifecycleOwner = viewLifecycleOwner

        mViewModel = ViewModelProviders.of(this).get(FishCardListFragmentViewModel::class.java)

        val adapter = FishCardRecycleViewAdapter(mViewModel)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        val observerForFavorite = Observer<List<FishCardList>> { fishLists ->
            if (mViewModel.searchText.value.isNullOrEmpty()) {
                adapter.setEmptyType(FishCardListFragmentViewModel.EmptyType.NO_LISTS)
                adapter.setData(fishLists)
                if (fishLists.isEmpty()) {
                    binding.infoLayout.visibility = View.GONE
                } else {
                    binding.infoLayout.visibility = View.VISIBLE
                }
            } else {
                adapter.setEmptyType(FishCardListFragmentViewModel.EmptyType.NO_SEARCH)
                adapter.setData(
                    mViewModel.getSortedFishCardListByText(
                        mViewModel.searchText.value ?: "", fishLists
                    )
                )
            }


        }

        mViewModel.fishCardVIewModel.readAllFishListsByFavorite.observe(
            viewLifecycleOwner,
            observerForFavorite
        )

        mViewModel.searchText.observe(viewLifecycleOwner){
            mViewModel.fishCardVIewModel.readAllFishListsByFavorite.removeObserver(
                observerForFavorite
            )
            mViewModel.fishCardVIewModel.readAllFishListsByFavorite.observe(
                viewLifecycleOwner,
                observerForFavorite
            ) // we want to reload data in recycleView
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (menu.findItem(R.id.app_bar_search)?.actionView as SearchView).apply {
            setOnQueryTextListener(mViewModel.getSearchListener())
            setOnCloseListener(mViewModel.getSearchCloseListener())

                setQuery(mViewModel.searchText.value, false) // when user rotate screen we want we save  text in searchView


        }

        super.onCreateOptionsMenu(menu, inflater)
    }


}