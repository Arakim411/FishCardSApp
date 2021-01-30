@file:Suppress("DEPRECATION")

package com.applications.fishcardroomandmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.applications.fishcardroomandmvvm.databinding.ActivityLearnBinding
import com.applications.fishcardroomandmvvm.fragments.LearnWord.FragmentLearnContent
import com.applications.fishcardroomandmvvm.fragments.LearnWord.FragmentWordsOptions
import com.applications.fishcardroomandmvvm.viewModels.LearnViewModel
import com.applications.fishcardroomandmvvm.viewModels.NOT_START
import com.applications.fishcardroomandmvvm.viewModels.TYPE_LEARN_WORDS
import com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys.LearnViewModelFactory
import kotlinx.android.synthetic.main.activity_learn.*

private const val TAG = "LearnActivity"
private const val OPTIONS_FRAGMENT_TAG = "OPTIONS_FRAGMENT"
const val LIST_ERROR = -1

class LearnActivity : AppCompatActivity(), FragmentLearnContent.LearnFragmentEvents {

    private lateinit var binding: ActivityLearnBinding
    private lateinit var mViewModel: LearnViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)


        Log.d(TAG, "Learn Activity: onCreate")

        val type = intent.getIntExtra(LEARN_EXTRA_TYPE, LIST_ERROR)
        val listId = intent.getIntExtra(LEARN_EXTRA_LIST_ID, LIST_ERROR)

        if (type == NOT_START) throw error("BAD TYPE")
        if (listId == LIST_ERROR) throw error("NO LIST ID")



        binding = ActivityLearnBinding.bind(learn_root)
        setSupportActionBar(binding.toolbarLearn)
        supportActionBar?.title = ""

        val factory = LearnViewModelFactory(application, type)
        mViewModel = ViewModelProviders.of(this, factory).get(LearnViewModel::class.java)

        binding.viewModel = mViewModel

        //liveData Observers

        mViewModel.toolbarAction.observe(this) { action ->

            when (action) {
                LearnViewModel.ToolbarActions.ACTION_OPTIONS -> {
                    supportActionBar?.title = getString(R.string.options)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    binding.wordIndex.visibility = View.GONE
                    binding.toolbarLearn.setNavigationOnClickListener {
                        onBackPressed()
                    }
                }
                LearnViewModel.ToolbarActions.DEFAULT_TITLE -> {
                    supportActionBar?.title = mViewModel.toolBarTitle
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    binding.wordIndex.visibility = View.VISIBLE
                }

                LearnViewModel.ToolbarActions.NONE -> return@observe


            }
        }

        mViewModel.showOptions.observe(this) { value ->

            val fragment = supportFragmentManager.findFragmentByTag(OPTIONS_FRAGMENT_TAG)

            if (value) {

                if (fragment == null) {

                    Log.d(TAG, "show options fragment")
                    supportFragmentManager.beginTransaction().add(
                        R.id.fragment_learn_host, FragmentWordsOptions(),
                        OPTIONS_FRAGMENT_TAG
                    ).commit()

                } else Log.e(TAG, "fragment is already added, message should be visible only when device is rotating ")

            } else {

                if (fragment != null) {
                    supportFragmentManager.beginTransaction().remove(fragment).commit()
                }

            }
        }

        mViewModel.currentFragment.observe(this){ currentFragment ->

            when(currentFragment){

                LearnViewModel.CurrentFragment.FRAGMENT_LEARN_CONTENT -> {
                    supportFragmentManager.beginTransaction().replace(binding.fragmentLearnHost.id,FragmentLearnContent.getInstance(listId)).commit()
                }
                LearnViewModel.CurrentFragment.FRAGMENT_SUMMARY -> {}
                LearnViewModel.CurrentFragment.NONE ->  return@observe
            }
            mViewModel.currentFragmentReset()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.words_options -> {

                mViewModel.optionsIconClicked()
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        when (mViewModel.type) {
            TYPE_LEARN_WORDS -> menuInflater.inflate(R.menu.menu_learn_words, menu)
        }
        return true
    }


    override fun onBackPressed() {
        when(mViewModel.toolbarAction.value){
            LearnViewModel.ToolbarActions.ACTION_OPTIONS -> {mViewModel.eventHideOptions()}

            else ->  finish()
        }

    }

    override fun onNextWord(wordNumber: Int, wordCount: Int) {
        val a = wordNumber.toString() + " / " + (wordCount+1).toString()
        binding.wordIndex.text = a
    }

    override fun onListNameChanged(listName: String) {
        mViewModel.setToolBarTitle(listName)
    }

    override fun onListFinished() {
        finish()
    }

    override fun onListEmpty() {
        finish()
        Toast.makeText(applicationContext, getString(R.string.add_at_lest_3_words_to_your_list), Toast.LENGTH_SHORT).show()
    }

}

