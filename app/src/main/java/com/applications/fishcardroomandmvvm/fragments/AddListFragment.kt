@file:Suppress("DEPRECATION")

package com.applications.fishcardroomandmvvm.fragments

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.adapters.LanguageSpinnerAdapter
import com.applications.fishcardroomandmvvm.databinding.FragmentAddListBinding
import com.applications.fishcardroomandmvvm.hideKeyboard
import com.applications.fishcardroomandmvvm.viewModels.FragmentAddViewModel




private const val TAG = "ADD_LIST_FRAGMENT"

class AddListFragment : Fragment() {

    private lateinit var binding: FragmentAddListBinding
    private lateinit var mViewModel: FragmentAddViewModel

    private var previousCorrectData = true


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_list,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        mViewModel = ViewModelProviders.of(this).get(FragmentAddViewModel::class.java)

        binding.fragmentAddViewModel = mViewModel



        //Observers
        val observer = Observer<String> {
            mViewModel.checkIfDataIsCorrect()
        }

        mViewModel.foreignLanguageName.observe(viewLifecycleOwner, observer)
        mViewModel.nativeLanguageName.observe(viewLifecycleOwner, observer)
        mViewModel.listName.observe(viewLifecycleOwner, observer)

        mViewModel.displayToast.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        mViewModel.listAdded.observe(viewLifecycleOwner) { value ->
            if (value) {
                hideKeyboard(requireActivity())
                findNavController().navigate(R.id.action_addListFragment_to_fishCardListFragment)
            }

        }

        //is not the best way , but i can't see other choice
        //trying bind drawableIconTint value in viewModel doesn't work
        mViewModel.correctData.observe(viewLifecycleOwner) { correctData ->
            val icon: Drawable
            if (correctData != previousCorrectData) {
                previousCorrectData = correctData
                icon = if (correctData) {
                    resources.getDrawable(R.drawable.ic_check_green)
                } else {
                    resources.getDrawable(R.drawable.ic_check_gray)
                }
                binding.addBnt.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    icon,
                    null
                )
            }

        }


        //spinners data
        val adapter = LanguageSpinnerAdapter(mViewModel.getSpinnerItems())
        binding.addSpinnerLanguageNative.adapter = adapter
        binding.addSpinnerLanguageToLearn.adapter = adapter

        //spinners listener

        //EditText saving value of list name
        binding.addName.editText?.doOnTextChanged { text, _, _, _ ->
            mViewModel.listName.value = text.toString()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinnersListeners()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.addName.editText?.setText(mViewModel.listName.value.toString())
    }

    private fun setSpinnersListeners() {

        binding.addSpinnerLanguageNative.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0 && selectedItemView != null) {
                    val textView: TextView = selectedItemView.findViewById(R.id.spinner_item_text)
                    mViewModel.nativeLanguageName.value = textView.text.toString()
                } else {
                    mViewModel.nativeLanguageName.value = ""
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }


        binding.addSpinnerLanguageToLearn.onItemSelectedListener = object :
            OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0  && selectedItemView != null) {
                    val textView: TextView = selectedItemView.findViewById(R.id.spinner_item_text)
                    mViewModel.foreignLanguageName.value = textView.text.toString()
                } else {
                    mViewModel.foreignLanguageName.value = ""
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }

        Log.i(TAG, "spinners listeners loaded")
    }






}