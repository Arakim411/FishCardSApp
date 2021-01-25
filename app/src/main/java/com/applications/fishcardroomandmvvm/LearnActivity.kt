@file:Suppress("DEPRECATION")

package com.applications.fishcardroomandmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.applications.fishcardroomandmvvm.databinding.ActivityLearnBinding
import com.applications.fishcardroomandmvvm.fragments.LearnWord.FragmentLearnContentDirections
import com.applications.fishcardroomandmvvm.fragments.LearnWord.FragmentWordsOptions
import com.applications.fishcardroomandmvvm.viewModels.LearnViewModel
import com.applications.fishcardroomandmvvm.viewModels.NOT_START
import com.applications.fishcardroomandmvvm.viewModels.TYPE_LEARN_WORDS
import com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys.LearnViewModelFactory
import kotlinx.android.synthetic.main.activity_learn.*

private const val TAG = "LearnActivity"
private const val OPTIONS_FRAGMENT_TAG = "OPTIONS_FRAGMENT"
const val LIST_ERROR  = -1

class LearnActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLearnBinding
    private lateinit var mViewModel: LearnViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        Log.d(TAG,"Learn Activity: onCreate")

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

        mViewModel.showOptions.observe(this) { value ->
            if (value) {
                // we can easily recreate this fragment in the same state because it takes data from sharedPreferences
                // so user won't lose current state of fragment.
                val fragment = supportFragmentManager.findFragmentByTag(OPTIONS_FRAGMENT_TAG)

                if (fragment == null) {
                    supportFragmentManager.beginTransaction().add(
                        R.id.fragment_learn_host, FragmentWordsOptions(),
                        OPTIONS_FRAGMENT_TAG
                    ).commit()
                } else {
                    supportFragmentManager.removeFragmentWithTag(OPTIONS_FRAGMENT_TAG)
                }
            } else {
                supportFragmentManager.removeFragmentWithTag(OPTIONS_FRAGMENT_TAG)
            }

        }


        //it not put it in viewModel because it trigger once, only when user launch activity
        // and there is no sense to create variable for this in viewModel
        if (savedInstanceState != null) {
            val nav = findNavController(R.id.fragment_learn_host)
            val action = FragmentLearnContentDirections.actionFragmentLearnContentSelf(listId)
            nav.navigate(action)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.words_options -> {

                mViewModel.eventShowOptions()
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
        super.onBackPressed()
        finish()
    }

}

