package com.applications.fishcardroomandmvvm.fragments.LearnWord

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.applications.fishcardroomandmvvm.LIST_ERROR
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.databinding.FragmentLearnContentBinding
import com.applications.fishcardroomandmvvm.viewModels.FishCardViewModel
import com.applications.fishcardroomandmvvm.viewModels.FragmentLearnViewModel
import com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys.FragmentLearnViewModelFactory
import kotlinx.android.synthetic.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// in this fragment user learn words

private const val TAG = "fragmentLearnC"

private const val ARG_LIST_ID = "ARG_LIST_ID"

class FragmentLearnContent : Fragment() {

    private var events: LearnFragmentEvents? = null

    private lateinit var binding: FragmentLearnContentBinding
    private lateinit var mViewModel: FragmentLearnViewModel

    interface LearnFragmentEvents {
        fun onNextWord(wordNumber: Int, wordCount: Int)
        fun onListNameChanged(listName: String)
        fun onListFinished()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val listId = requireArguments().getInt(ARG_LIST_ID)


        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_learn_content,
            container,
            false
        )
        binding.lifecycleOwner = this


        val viewModelFactory =
            FragmentLearnViewModelFactory(context?.applicationContext as Application, listId)
        mViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FragmentLearnViewModel::class.java)

        binding.viewModel = mViewModel

        //Observers

        mViewModel.isDataLoaded.observe(viewLifecycleOwner) { value ->
            binding.waitingLayout.visibility = if (value) View.GONE else View.VISIBLE
        }

        mViewModel.toolBarTitle.observe(viewLifecycleOwner){ title ->
            events?.onListNameChanged(title)
        }

        mViewModel.currentWord.observe(viewLifecycleOwner){ word ->

        }

        mViewModel.wordIndex.observe(viewLifecycleOwner) { index ->
            // current word index
            events?.onNextWord(index, mViewModel.listSize)
        }


        //observers (sharedPreferences)


        return binding.root
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is LearnFragmentEvents)
            events = context

        if (events == null) throw error("$context mustImplement LearnFragmentEvents")


    }

    companion object {
        fun getInstance(listId: Int) = FragmentLearnContent().apply {
            arguments = Bundle().apply {
                putInt(ARG_LIST_ID, listId)
            }
        }

    }

    override fun onDetach() {
        events = null
        super.onDetach()
    }


}