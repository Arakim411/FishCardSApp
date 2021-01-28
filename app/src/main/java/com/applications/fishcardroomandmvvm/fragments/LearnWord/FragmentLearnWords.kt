@file:Suppress("DEPRECATION")

package com.applications.fishcardroomandmvvm.fragments.LearnWord


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.adapters.TranslationRecyclerViewAdapter
import com.applications.fishcardroomandmvvm.databinding.FragmentLearnContentBinding
import com.applications.fishcardroomandmvvm.viewModels.FragmentLearnViewModel
import com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys.FragmentLearnViewModelFactory


// in this fragment user learn words

private const val TAG = "fragmentLearnC"

private const val ARG_LIST_ID = "ARG_LIST_ID"

private const val animationDuration = 500L

private const val SIS_NEXT_WORD = "SIS_NEXT_WORD"
private const val SIS_SHOW_WITH_TRANSLATION = "SIS_SHOW_WITH_TRANSLATION"

@Suppress("DEPRECATION")
class FragmentLearnContent : Fragment(), TranslationRecyclerViewAdapter.TranslationListEvents {

    private var events: LearnFragmentEvents? = null

    private lateinit var binding: FragmentLearnContentBinding
    private lateinit var mViewModel: FragmentLearnViewModel


    private val updateListener = ValueAnimator.AnimatorUpdateListener { animator ->

        val animatedValue = animator.animatedValue as Float

        binding.wordNative.alpha = animatedValue

        if (mViewModel.showStatistics.value == true) {
            binding.badAnswers.alpha = animatedValue
            binding.goodAnswers.alpha = animatedValue
        }

        if (mViewModel.showTranslation.value == true) {
            binding.translationList.alpha = animatedValue
        }
    }

    private val disappearUI = ObjectAnimator.ofFloat(0f, 1f).apply {
        duration = animationDuration
        addUpdateListener(updateListener)
    }

    private val appearUI = ObjectAnimator.ofFloat(1f, 0f).apply {
        duration = animationDuration
        addUpdateListener(updateListener)
    }

    private val bouncer = AnimatorSet().apply {
        play(disappearUI).after(appearUI)
    }

    interface LearnFragmentEvents {
        fun onNextWord(wordNumber: Int, wordCount: Int)
        fun onListNameChanged(listName: String)
        fun onListFinished()
        fun onListEmpty()
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

        mViewModel.isNextWordByOrientationChange =
            savedInstanceState?.getBoolean(SIS_NEXT_WORD) ?: false

        mViewModel.isShowTranslationByOrientationChange = savedInstanceState?.getBoolean(
            SIS_SHOW_WITH_TRANSLATION) ?: false

        // RecyclerView
        val adapter = TranslationRecyclerViewAdapter(this)
        binding.translationList.layoutManager = LinearLayoutManager(requireContext())
        binding.translationList.adapter = adapter

        //Observers

        mViewModel.isDataLoaded.observe(viewLifecycleOwner) { value ->
            binding.waitingLayout.visibility = if (value) View.GONE else View.VISIBLE
        }

        mViewModel.toolBarTitle.observe(viewLifecycleOwner) { title ->
            events?.onListNameChanged(title)
        }



        mViewModel.currentWord.observe(viewLifecycleOwner) { word ->
            val wordInNative = word.word
            val translations = word.translation
            val goodAnswers = word.goodAnswers
            val badAnswers = word.badAnswers



            bouncer.childAnimations[1].doOnEnd {
                binding.wordNative.text = wordInNative
                binding.goodAnswers.text = goodAnswers.toString()
                binding.badAnswers.text = badAnswers.toString()
                adapter.setData(translations)
            }

            if (mViewModel.wordIndex.value != 0 && !mViewModel.isNextWordByOrientationChange)
                bouncer.start()
            else {
                binding.wordNative.text = wordInNative
                binding.goodAnswers.text = goodAnswers.toString()
                binding.badAnswers.text = badAnswers.toString()
                adapter.setData(translations)
            }

            mViewModel.isNextWordByOrientationChange = false

        }

        mViewModel.showTranslation.observe(viewLifecycleOwner) { value ->
            if (value) {
                Log.i(TAG, "Translation Visible")
                binding.translationList.visibility = View.VISIBLE
            } else {
                Log.i(TAG, "Translation Invisible")
                binding.translationList.visibility = View.GONE
            }
        }

        mViewModel.translationButtonType.observe(viewLifecycleOwner) { type ->
            when (type) {
                FragmentLearnViewModel.ButtonType.SHOW_TRANSLATION -> {
                    binding.bntShowTranslation.text = getString(R.string.show_translation)
                }

                FragmentLearnViewModel.ButtonType.HIDE_TRANSLATION -> {
                    binding.bntShowTranslation.text = getString(R.string.hide)
                }

                else -> {
                }
            }

        }

        mViewModel.wordIndex.observe(viewLifecycleOwner) { index ->
            // current word index
            events?.onNextWord(index, mViewModel.listSize)
        }

        mViewModel.listEmpty.observe(viewLifecycleOwner) { value -> if (value) events?.onListEmpty() }


        //observers (sharedPreferences)

        mViewModel.showWithTranslate.observe(viewLifecycleOwner) { value ->

                if (value) {
                    Log.d(TAG, "show  translation")
                    binding.bntShowTranslation.visibility = View.INVISIBLE
                    mViewModel.showTranslation()

                    val params =
                        binding.translationList.layoutParams as ConstraintLayout.LayoutParams
                    params.topToBottom = binding.wordNative.id
                    binding.translationList.layoutParams = params

                } else if(!mViewModel.isShowTranslationByOrientationChange) {
                    Log.d(TAG, "hide translation")
                    binding.bntShowTranslation.visibility = View.VISIBLE
                    mViewModel.hideTranslation()

                    val params =
                        binding.translationList.layoutParams as ConstraintLayout.LayoutParams
                    params.topToBottom = binding.bntShowTranslation.id
                    binding.translationList.layoutParams = params
                }

            mViewModel.isShowTranslationByOrientationChange = false

        }


        mViewModel.saveData.observe(viewLifecycleOwner) { value ->
            // allows user to decide if he know or don't know word and save it to database
            if (value) {
                binding.dontKnowAnswer.visibility = View.VISIBLE
                binding.know.visibility = View.VISIBLE
                binding.nextWord.visibility = View.INVISIBLE
            } else {
                binding.dontKnowAnswer.visibility = View.INVISIBLE
                binding.know.visibility = View.GONE
                binding.nextWord.visibility = View.VISIBLE
            }
        }


        mViewModel.showStatistics.observe(viewLifecycleOwner) { value ->

            if (value) {
                binding.goodAnswers.visibility = View.VISIBLE
                binding.badAnswers.visibility = View.VISIBLE

            } else {
                binding.goodAnswers.visibility = View.INVISIBLE
                binding.badAnswers.visibility = View.INVISIBLE
            }
        }


        mViewModel.nativeFlagResource.observe(viewLifecycleOwner) { resourcesId ->
            binding.nativeLanguageFlag.setImageResource(resourcesId)
        }

        mViewModel.foreignFlagResource.observe(viewLifecycleOwner) { resourceId ->
            binding.foreignFlag.setImageResource(resourceId)

        }


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

    override fun onItemClick(text: String) {
        Log.d(TAG, "Translation: $text clicked")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SIS_NEXT_WORD, true)
        outState.putBoolean(SIS_SHOW_WITH_TRANSLATION,true)
    }


}