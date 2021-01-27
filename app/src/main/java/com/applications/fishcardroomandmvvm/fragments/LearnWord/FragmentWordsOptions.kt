package com.applications.fishcardroomandmvvm.fragments.LearnWord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.databinding.FragmentWordsOptionsBinding
import com.applications.fishcardroomandmvvm.viewModels.FragmentOptionsViewModel

private const val TAG ="fragmentOption"

class FragmentWordsOptions : Fragment() {

    private lateinit var bidning: FragmentWordsOptionsBinding
    private lateinit var mViewModel: FragmentOptionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        bidning = DataBindingUtil.inflate(inflater,R.layout.fragment_words_options,container,false)
        bidning.lifecycleOwner = this


        mViewModel = ViewModelProviders.of(this).get(FragmentOptionsViewModel::class.java)
        bidning.viewModel = mViewModel

         



      return  bidning.root
    }


}