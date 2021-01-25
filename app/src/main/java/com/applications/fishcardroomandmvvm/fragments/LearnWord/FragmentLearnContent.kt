package com.applications.fishcardroomandmvvm.fragments.LearnWord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.applications.fishcardroomandmvvm.LIST_ERROR
import com.applications.fishcardroomandmvvm.R

// in this fragment user learn words

private  const val TAG = "fragmentLearnC"

class FragmentLearnContent : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val args by navArgs<FragmentLearnContentArgs>()
       val listId = args.listId

        if(listId == LIST_ERROR)  return inflater.inflate(R.layout.fragment_learn_content, container, false)



        return inflater.inflate(R.layout.fragment_learn_content, container, false)
    }


}