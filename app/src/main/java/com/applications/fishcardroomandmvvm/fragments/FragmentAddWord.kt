@file:Suppress("DEPRECATION")

package com.applications.fishcardroomandmvvm.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.ROOM.model.Word
import com.applications.fishcardroomandmvvm.databinding.FragmentAddWordBinding
import com.applications.fishcardroomandmvvm.hideKeyboard
import com.applications.fishcardroomandmvvm.viewModels.FragmentAddWordViewModel
import com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys.FragmentAddWorViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_word.view.*
import kotlinx.android.synthetic.main.translation_add_item.view.*


private const val ARG_FISH_CARD_LIST = "ARG_FISH_CARD_LIST"
private const val TAG_TRANSLATION = "TAG_TRANSLATION"

private const val TAG = "AddWord"

class FragmentAddWord : Fragment() {


    private lateinit var mViewModel: FragmentAddWordViewModel
    private lateinit var binding: FragmentAddWordBinding

    private var addFragmentEvents: AddFragmentEvents? = null

    interface AddFragmentEvents {
        fun fragmentRemovedByUser(fragment: Fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //binding
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_add_word,container,false)
        binding.lifecycleOwner = this

        //viewModel
        val factory = FragmentAddWorViewModelFactory(activity?.application!!,requireArguments().getParcelable(ARG_FISH_CARD_LIST)!!)
        mViewModel = ViewModelProviders.of(this,factory).get(FragmentAddWordViewModel::class.java)

        binding.viewModel = mViewModel

        //observers
        mViewModel.addTranslations.observe(viewLifecycleOwner){ value ->
            when (value) {
                is Int -> {
                    for (i in 0 until value) {
                        mViewModel.translationCount++
                        addTranslationEditText()
                    }
                }
                is String -> {
                    mViewModel.translationCount++
                    addTranslationEditText(value)
                }
                is ArrayList<*> -> {

                    for(i in value.iterator()){
                        if(i !is String)  throw  error("$value is not string array")
                        mViewModel.translationCount ++
                        addTranslationEditText(i)
                    }
                }
            }

        }

        // settings view
        binding.word.requestFocus()
        binding.nativeFlag.setImageResource(mViewModel.nativeFlag)


        binding.word.editText?.doOnTextChanged { text, _, _, _ ->
            mViewModel.word = text.toString()
        }


        binding.dismiss.setOnClickListener {
                addFragmentEvents?.fragmentRemovedByUser(this)
           }

        return binding.root
    }



    private fun addTranslationEditText(defaultText: String = "") {
        // add new editText that allows user add translation
        val view = layoutInflater.inflate(R.layout.translation_add_item, view?.add_root, false)

        view.translation.hint =
            context?.getString(R.string.translation, mViewModel.translationCount.toString())

        view.translation.editText?.setText(defaultText)

        view.foreign_flag.setImageResource(mViewModel.foreignFlag)

        view.tag = TAG_TRANSLATION

        binding.addRoot.addView(view, binding.addRoot.childCount - 1)


    }

    fun getWordToAdd(): Word? {
        //function return word which will be later added in FragmentWordsViewModel
        // it could be add in this fragment but i wan't manage database only from  FragmentWordsViewModel
        val translation = getAllTranslationsFromUI()
        val nativeWord = view?.word?.editText?.text.toString()




        if (translation.isNotEmpty() && nativeWord.isNotEmpty()) {
            
            hideKeyboard(context as Activity)

            return Word(
                id = 0,
                fishId = this.mViewModel.fishCardList.id,
                word = nativeWord,
                translation = translation,
                0,
                0
            )
        }

        return null
    }


    private fun getAllTranslationsFromUI(): ArrayList<String>{
        //returns list of translation in current editTexts
        val translation = ArrayList<String>()

        for(i in binding.addRoot.children){
            if( i.tag == TAG_TRANSLATION){
                // i is view with translation edit text
                val text = i.translation.editText?.text.toString()
                if(text.isNotEmpty()){
                    translation.add(text)
                }
            }
        }
        return  translation
    }



    companion object {

        fun newInstance(fishCardList: FishCardList) = FragmentAddWord().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_FISH_CARD_LIST, fishCardList)
            }
        }

    }

    override fun onDestroy() {
        addFragmentEvents = null
        super.onDestroy()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        when {
            context is AddFragmentEvents -> {
                addFragmentEvents = context
            }
            childFragmentManager is AddFragmentEvents -> {
                addFragmentEvents = childFragmentManager as AddFragmentEvents
            }
            parentFragment is AddFragmentEvents -> {
                addFragmentEvents = parentFragment as AddFragmentEvents
            }
            fragment is AddFragmentEvents -> {
                addFragmentEvents = fragment as AddFragmentEvents
            }
        }

    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(TAG,"view was restored")
        if(savedInstanceState != null) {
            binding.word.editText?.setText(mViewModel.word ?: "")
            if(mViewModel.translation.isNotEmpty())
            mViewModel.addTranslation(mViewModel.translation)
            else mViewModel.addTranslation(2)
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mViewModel.translation = getAllTranslationsFromUI()
        mViewModel.addTranslation(null) //  we don't want additional translations field when view is restored, observer will trigger but won't do nothing
        mViewModel.translationCount = 0
        super.onSaveInstanceState(outState)
    }


    override fun onDetach() {
        addFragmentEvents = null
        super.onDetach()
    }

}