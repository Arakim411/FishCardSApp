@file:Suppress("DEPRECATION")

package com.applications.fishcardroomandmvvm.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.applications.fishcardroomandmvvm.FishListActivity
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.ROOM.model.Word
import com.applications.fishcardroomandmvvm.adapters.WordRecyclerViewAdapter
import com.applications.fishcardroomandmvvm.customViews.ChoiceWindow
import com.applications.fishcardroomandmvvm.dataClasses.ChoiceViewItem
import com.applications.fishcardroomandmvvm.databinding.FragmentWordsListBinding
import com.applications.fishcardroomandmvvm.listeners.FabListener
import com.applications.fishcardroomandmvvm.listeners.FishListActivityListener
import com.applications.fishcardroomandmvvm.listeners.WordRecyclerViewListener
import com.applications.fishcardroomandmvvm.viewModels.FragmentWordsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val ARG_FISH_LIST = "ARG_FISH_LIST"
const val TAG_ADD_FRAGMENT = "TAG_ADD_FRAGMENT"
const val TAG_EDIT_FRAGMENT = "TAG_EDIT_FRAGMENT"

private const val ITEM_EDIT_ID = 0
private const val ITEM_REMOVE_ID = 1


class FragmentWordsList : Fragment(), FabListener, FragmentAddWord.AddFragmentEvents,
    WordRecyclerViewListener, EditFragment.EditFragmentEvents {

    private lateinit var mViewModel: FragmentWordsViewModel
    private lateinit var binding: FragmentWordsListBinding

    //Choice window items
    private lateinit var itemEdit: ChoiceViewItem
    private lateinit var itemRemove: ChoiceViewItem

    private val listChoiceItems = ArrayList<ChoiceViewItem>()

    private var fishListActivityListener: FishListActivityListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        itemEdit = ChoiceViewItem(
            ITEM_EDIT_ID,
            requireContext().getString(R.string.edit),
            R.drawable.ic_baseline_settings_24
        )

        itemRemove = ChoiceViewItem(
            ITEM_REMOVE_ID,
            requireContext().getString(R.string.delete_word),
            R.drawable.ic_baseline_delete_forever_24
        )

        listChoiceItems.add(itemEdit)
        listChoiceItems.add(itemRemove)



        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_words_list, container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner


        mViewModel = ViewModelProviders.of(this).get(FragmentWordsViewModel::class.java)
        mViewModel.fishCardList = requireArguments().getParcelable(ARG_FISH_LIST)!!

        mViewModel.childFragmentManager = childFragmentManager


        val adapter = WordRecyclerViewAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val wordsObserver = Observer<List<Word>> { wordsList ->

            adapter.setData(wordsList)
            if (wordsList.isEmpty()) binding.info.visibility =
                View.INVISIBLE else binding.info.visibility = View.VISIBLE
        }

        mViewModel.getWords().observe(viewLifecycleOwner, wordsObserver)

        mViewModel.showEditFragment.observe(viewLifecycleOwner) { word ->

            val editFragment = childFragmentManager.findFragmentByTag(TAG_EDIT_FRAGMENT)

            if (word != null && editFragment == null) {
                childFragmentManager.beginTransaction().replace(
                    binding.container.id,
                    EditFragment.newInstance(
                        word,
                        mViewModel.fishCardList.nativeLanguage,
                        mViewModel.fishCardList.foreignLanguage
                    ),
                    TAG_EDIT_FRAGMENT
                ).commit()

            }
        }

        mViewModel.showToast.observe(viewLifecycleOwner) { toastMessage ->
            if (toastMessage.isNotEmpty()) {
                Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                mViewModel.resetToastMessage()
            }
        }

        mViewModel.fabIcon.observe(viewLifecycleOwner) { drawable ->
            fishListActivityListener?.setFacIcon(drawable)
        }

        mViewModel.performAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                FragmentWordsViewModel.PerformAction.PERFORM_SHOW_WINDOW_ADD_WORD -> {
                    //show window that allow user add new word
                    // in this state mViewModel.fragmentAddWord.value can't be null
                    childFragmentManager.beginTransaction()
                        .replace(
                            binding.container.id, mViewModel.getInstanceFragmentAddWord(),
                            TAG_ADD_FRAGMENT
                        ).commit()
                }


                FragmentWordsViewModel.PerformAction.PERFORM_NONE -> return@observe

                null -> {
                }
            }

            mViewModel.setPerformAsNone()
        }

        mViewModel.choiceWindow.observe(viewLifecycleOwner) { window ->
            if (window != null) {
                window.show()
            } else {
                adapter.restoreCheckedView()
            }

        }


        return binding.root
    }

    companion object {
        fun newInstance(fishCardList: FishCardList) =
            FragmentWordsList().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FISH_LIST, fishCardList)
                }
            }
    }

    override fun onFabClicked(fab: FloatingActionButton) {
        mViewModel.onFabClicked(fab)
    }

    //FRAGMENT ADD WORD
    override fun fragmentRemovedByUser(fragment: Fragment) {
        childFragmentManager.beginTransaction().remove(fragment).commit()
        mViewModel.onAddFragmentRemovedByUser()
    }

    override fun onLongClick(word: Word, x: Float, y: Float) {

        val events = object : ChoiceWindow.ChoiceWindowEvents {
            override fun onItemChoice(itemId: Int) {

                when (itemId) {
                    ITEM_EDIT_ID -> mViewModel.editWord(word)

                    ITEM_REMOVE_ID -> mViewModel.deleteWord(word)
                }
            }

            override fun onRemove() {
                mViewModel.choiceWindow.value = null
            }
        }

        val choiceWindow = ChoiceWindow(requireContext(), events, binding.root as ViewGroup)
        choiceWindow.setChoices(listChoiceItems)
        choiceWindow.setPosition(x, y)

        mViewModel.choiceWindow.value = choiceWindow

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FishListActivityListener) {
            fishListActivityListener = context
        } else throw error("$context must implement FishListActivityListener")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        mViewModel.choiceWindow.value = null
        super.onSaveInstanceState(outState)
    }

    //EDIT FRAGMENT
    override fun onFragmentCloseByUser() {
        mViewModel.resetFab()
    }

}