package com.applications.fishcardroomandmvvm.fragments.SpellingWord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.databinding.FragmentSpellingOptionsBinding
import com.applications.fishcardroomandmvvm.viewModels.FragmentSpellingOptionsViewModel


class FragmentSpellingOptions : Fragment() {

    private lateinit var binding: FragmentSpellingOptionsBinding
    private lateinit var mViewModel: FragmentSpellingOptionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_spelling_options, container, false)
        binding.lifecycleOwner = this

        mViewModel = ViewModelProviders.of(this).get(FragmentSpellingOptionsViewModel::class.java)

        binding.viewModel = mViewModel

        mViewModel.randomListChanged.observe(viewLifecycleOwner) { value ->
            if (value)
                Toast.makeText(context, getString(R.string.list_will_be_random), Toast.LENGTH_SHORT)
                    .show()
            else
                Toast.makeText(context, getString(R.string.list_normal_order), Toast.LENGTH_SHORT)
                    .show()
        }

        return binding.root
    }

}