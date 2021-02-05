package com.applications.fishcardroomandmvvm.fragments

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.applications.fishcardroomandmvvm.LanguageManager
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.ROOM.model.Word
import com.applications.fishcardroomandmvvm.databinding.FragmentEditBinding
import com.applications.fishcardroomandmvvm.hideKeyboard
import com.applications.fishcardroomandmvvm.viewModels.EditFragmentViewModel
import com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys.EditFragmentViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_word.view.*
import kotlinx.android.synthetic.main.translation_add_item.view.*

private const val WORD_ARG = "WORD_ARG"

private const val NATIVE_FLAG_ID_ARG = "NATIVE_FLAG"

private const val FOREIGN_FLAG_ID_ARG = "FOREIGN_FLAG"

private const val TAG = "editFragment"

private const val TAG_TRANSLATION = "TAG_TRANSLATION"


class EditFragment : Fragment() {

    lateinit var viewModel: EditFragmentViewModel
    lateinit var binding: FragmentEditBinding

    private var editFragmentEvents: EditFragmentEvents? = null

    private  var nativeFlag: Int = 0
    private  var foreignFlag: Int = 0

    interface EditFragmentEvents {
        fun onFragmentCloseByUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val word = requireArguments().getParcelable<Word>(WORD_ARG)!!
        val nativeFlagId = requireArguments().getString(NATIVE_FLAG_ID_ARG)
        val foreignFlagId = requireArguments().getString(FOREIGN_FLAG_ID_ARG)

        val languageManager = LanguageManager(requireContext())

        if(nativeFlagId!= null && foreignFlagId != null) {
            nativeFlag = languageManager.getLanguageDrawableResources(nativeFlagId)
            foreignFlag = languageManager.getLanguageDrawableResources(foreignFlagId)
        }else throw error("$nativeFlagId and $foreignFlagId can't be null")


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false)
        binding.lifecycleOwner = this

        val factory = EditFragmentViewModelFactory(context?.applicationContext as Application, word)
        viewModel = ViewModelProviders.of(this, factory).get(EditFragmentViewModel::class.java)

        viewModel.translationCount = 0

        binding.viewModel = viewModel
        binding.nativeFlag.setImageResource(nativeFlag)

        viewModel.translationList.observe(viewLifecycleOwner) { translations ->


            for (i in translations.iterator()) {
               addTranslationEditText(i)
            }
        }


        viewModel.addTranslation.observe(viewLifecycleOwner){ value ->

            if(value)
                addTranslationEditText()
        }

        viewModel.updatedWord.observe(viewLifecycleOwner){ word ->

            binding.word.editText?.setText(word)
        }

        viewModel.closeFragment.observe(viewLifecycleOwner){ value ->

            if(value) {
                editFragmentEvents?.onFragmentCloseByUser()
                fragmentManager?.beginTransaction()?.remove(this)?.commit()
            }

        }

        return binding.root
    }

    fun getUpdatedWord(): Word?{

        val updatedWord = viewModel.word
        updatedWord.word = binding.word.editText?.text.toString()

        val translations = getAllTranslationsFromUI()

        updatedWord.translation = translations

        if(translations.isEmpty())
            return  null

        if(updatedWord.word.isEmpty())
            return  null

            hideKeyboard(context as Activity)
            return  updatedWord
    }




    private fun addTranslationEditText(defaultText: String = "") {
        // add new editText that allows user add translation
        val view = layoutInflater.inflate(R.layout.translation_add_item, view?.add_root, false)

        viewModel.translationCount ++

        view.translation.hint =
            context?.getString(R.string.translation, viewModel.translationCount.toString())

        view.translation.editText?.setText(defaultText)

        view.foreign_flag.setImageResource(foreignFlag)

        view.tag = TAG_TRANSLATION

        binding.addRoot.addView(view, binding.addRoot.childCount - 1)
        
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


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel.onSaveInstance(getAllTranslationsFromUI())
        viewModel.setUpdatedWord(binding.word.editText?.text.toString())
    }

    companion object {

        @JvmStatic
        fun newInstance(word: Word,nativeFlagId: String, foreignFlagId: String) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(WORD_ARG, word)
                    putString(NATIVE_FLAG_ID_ARG,nativeFlagId)
                    putString(FOREIGN_FLAG_ID_ARG,foreignFlagId)
                }
            }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)


        when {
            context is EditFragment.EditFragmentEvents -> {
                editFragmentEvents = context
            }
            childFragmentManager is EditFragment.EditFragmentEvents -> {
                editFragmentEvents = childFragmentManager as EditFragment.EditFragmentEvents
            }
            parentFragment is EditFragment.EditFragmentEvents -> {
                editFragmentEvents = parentFragment as EditFragment.EditFragmentEvents
            }
            fragment is EditFragment.EditFragmentEvents -> {
                editFragmentEvents = fragment as EditFragment.EditFragmentEvents
            }
        }

    }


    override fun onDetach() {
        super.onDetach()
      editFragmentEvents = null
    }

    


}