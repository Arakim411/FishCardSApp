package com.applications.fishcardroomandmvvm.fragments.SpellingWord

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.SpeechManager
import com.applications.fishcardroomandmvvm.adapters.TranslationRecyclerViewAdapter
import com.applications.fishcardroomandmvvm.databinding.FragmentSpellingBinding
import com.applications.fishcardroomandmvvm.fragments.LearnWord.FragmentLearnContent
import com.applications.fishcardroomandmvvm.hideKeyboard
import com.applications.fishcardroomandmvvm.viewModels.FragmentSpellingViewModel
import com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys.FragmentSpellingViewModelFactory


private const val LIST_ID_ARG = "LIST_ID_ARG"

class FragmentSpelling : Fragment(),TranslationRecyclerViewAdapter.TranslationListEvents {

    private lateinit var binding: FragmentSpellingBinding
    private lateinit var mViewModel: FragmentSpellingViewModel

    private lateinit var speechManager: SpeechManager


    private var events: FragmentLearnContent.LearnFragmentEvents? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_spelling, container, false)
        binding.lifecycleOwner = this


        val listId = requireArguments().getInt(LIST_ID_ARG)

        speechManager = SpeechManager(requireContext())

        val factory = FragmentSpellingViewModelFactory(
            context?.applicationContext as Application,
            listId
        )
        mViewModel = ViewModelProviders.of(this, factory).get(FragmentSpellingViewModel::class.java)

        binding.viewModel = mViewModel

        //Observers

        mViewModel.infoProgressText.observe(viewLifecycleOwner) { two ->

            val first = two.first
            val second = two.second

            val text = "$first\\$second"

            if (first == 0 || second == 0) binding.infoProgress.visibility = View.INVISIBLE
            else binding.infoProgress.visibility = View.VISIBLE

            binding.infoProgress.text = text

        }

        mViewModel.nativeFlagResource.observe(viewLifecycleOwner) { resources ->
            binding.nativeLanguageFlag.setImageResource(resources)
        }

        mViewModel.foreignFlagResource.observe(viewLifecycleOwner) { resource ->

            binding.foreignFlag.setImageResource(resource)
        }

        mViewModel.listEmpty.observe(viewLifecycleOwner) { value ->
            if (value)
                events?.onListEmpty()
        }

        mViewModel.wordIndex.observe(viewLifecycleOwner) { index ->
            events?.onNextWord(index, mViewModel.listSize)
        }

        mViewModel.toolBarTitle.observe(viewLifecycleOwner) { title ->
            events?.onListNameChanged(title)
        }

        mViewModel.showHint.observe(viewLifecycleOwner) { showHint ->
            if (showHint)
                binding.hintLayout.visibility = View.VISIBLE
            else
                binding.hintLayout.visibility = View.INVISIBLE
        }

        mViewModel.listFinished.observe(viewLifecycleOwner) { listFinished -> if (listFinished) events?.onListFinished() }

        // translations

        val adapter = TranslationRecyclerViewAdapter(this)
        binding.translations.layoutManager = LinearLayoutManager(requireContext())
        binding.translations.adapter = adapter

        mViewModel.currentWord.observe(viewLifecycleOwner) { word ->

            binding.wordNative.text = word.word
            binding.textInputEditText.text?.clear()
            adapter.setData(word.translation)

        }

        mViewModel.infoTextSpannable.observe(viewLifecycleOwner) { spannableString ->

            if (spannableString == null)
                binding.infoText.text = ""
            else
                binding.infoText.setText(spannableString, TextView.BufferType.SPANNABLE)
        }

        mViewModel.closeKeyboard.observe(viewLifecycleOwner) { value ->
            if (value) hideKeyboard(
                requireActivity() as Activity
            )
            binding.textInputEditText.clearFocus()
        }

        mViewModel.isGoodTranslation.observe(viewLifecycleOwner) { isGoodTranslation ->
            if (isGoodTranslation) {
                binding.infoProgress.setTextColor(resources.getColor(R.color.green))
                binding.compatibilityIcon.setImageResource(R.drawable.check_green_40)

            } else {
                binding.infoProgress.setTextColor(resources.getColor(R.color.red))
                binding.compatibilityIcon.setImageResource(R.drawable.close_red_40)

            }
        }

        mViewModel.showTranslation.observe(viewLifecycleOwner){ value ->
            if(value) binding.translations.visibility = View.VISIBLE
            else binding.translations.visibility = View.INVISIBLE
        }

        binding.textInputEditText.doOnTextChanged { text, start, before, count ->
            mViewModel.onEditTextChanged(
                text.toString()
            )
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FragmentLearnContent.LearnFragmentEvents)
            events = context

        if (events == null) throw error("$context mustImplement LearnFragmentEvents")
    }

    companion object {
        @JvmStatic
        fun newInstance(listId: Int) =
            FragmentSpelling().apply {
                arguments = Bundle().apply {
                    putInt(LIST_ID_ARG, listId)
                }
            }
    }

    override fun onItemClick(text: String) {
        speechManager.setLanguage(mViewModel.foreignLanguage)
        speechManager.sayText(text)
    }


}