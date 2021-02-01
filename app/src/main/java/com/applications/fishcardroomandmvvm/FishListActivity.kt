@file:Suppress("DEPRECATION")

package com.applications.fishcardroomandmvvm

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import com.applications.fishcardroomandmvvm.customViews.BOTTOM_SHEET_ITEMS_ID
import com.applications.fishcardroomandmvvm.customViews.BottomSheet
import com.applications.fishcardroomandmvvm.databinding.ActivityFishListBinding
import com.applications.fishcardroomandmvvm.dialogs.*
import com.applications.fishcardroomandmvvm.fragments.FragmentWordsList
import com.applications.fishcardroomandmvvm.listeners.FishListActivityListener
import com.applications.fishcardroomandmvvm.viewModels.FishCardListActivityViewModel
import com.applications.fishcardroomandmvvm.viewModels.NOT_START
import com.applications.fishcardroomandmvvm.viewModels.TYPE_LEARN_SPELLING
import com.applications.fishcardroomandmvvm.viewModels.viewModelFactorys.FishCardListActivityViewModelFactory
import kotlinx.android.synthetic.main.activity_fish_list.*

private const val TAG = "FishActivity"




@Suppress("DEPRECATION")
class FishListActivity : AppCompatActivity(), RenameDialog.UpdateListEvent, AppDialog.DialogEvents,
    BottomSheet.BottomSheetEvents, FishListActivityListener {

    private lateinit var binding: ActivityFishListBinding
    private lateinit var mViewModel: FishCardListActivityViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fish_list)

        binding = ActivityFishListBinding.bind(fish_root)

        val viewModelFactory = FishCardListActivityViewModelFactory(
            application,
            intent.getParcelableExtra(FISH_LIST_EXTRA)!!
        )

        mViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FishCardListActivityViewModel::class.java)
        setSupportActionBar(binding.toolbar)
        binding.fishCardListActivityViewModel = mViewModel


        mViewModel.listName.observe(this) { name ->
            supportActionBar?.title = name
        }

        mViewModel.showDialogDelete.observe(this) { value ->
            if (value) {
                val dialog = AppDialog()
                val args = Bundle().apply {
                    putInt(DIALOG_ID, DIALOG_ID_DELETE)
                    putString(
                        DIALOG_MESSAGE,
                        "Are you sure you want delete ${mViewModel.fishCardList.name} ?"
                    )
                }

                dialog.arguments = args
                dialog.show(supportFragmentManager, null)

                mViewModel.resetShowDialogDelete()
            }

        }

        mViewModel.loadFragment.observe(this) { fragmentToLoad ->

            when (fragmentToLoad) {
                FishCardListActivityViewModel.AvailableFragments.FragmentWordsList -> {
                    mViewModel.initializeFragmentWordList()
                    replaceFragment(
                        mViewModel.fragmentWordList!!,
                        binding.fragmentContainer.id
                    )
                }

                FishCardListActivityViewModel.AvailableFragments.NONE -> {
                    Log.d(TAG, "change instate state")
                    return@observe
                }

                else -> throw  error("no Such fragment: $fragmentToLoad")
            }

            mViewModel.setLoadAsNone()
        }

        mViewModel.startLearnActivity.observe(this){ id ->
            if(id != NOT_START) {
                if(id == TYPE_LEARN_SPELLING){
                    Toast.makeText(this,"not implemented yet",Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, LearnActivity::class.java)
                intent.putExtra(LEARN_EXTRA_TYPE, id)
                intent.putExtra(LEARN_EXTRA_LIST_ID,mViewModel.fishCardList.id)
                startActivity(intent)
                mViewModel.resetActivityStarting()
            }

        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fish_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fish_list_start_learn -> {

                val bottomSheet = BottomSheet()
                bottomSheet.arguments = Bundle().apply {
                    putParcelableArrayList(BOTTOM_SHEET_ITEMS_ID, mViewModel.getBottomSheetItems())
                }

                bottomSheet.show(supportFragmentManager, null)
            }
            R.id.app_bar_fish_list_home -> {
                finish()
            }
            R.id.app_bar_fish_list_statistics -> {
            }
            R.id.app_bar_fish_list_rename -> {

                RenameDialog.newInstance(mViewModel.fishCardList).show(supportFragmentManager, null)
            }
            R.id.app_bar_fish_list_delete -> {

                mViewModel.showDeleteDialog()
            }

        }
        return super.onOptionsItemSelected(item)
    }


    override fun updateList(fishCardList: FishCardList) {
        mViewModel.updateListDatabase(fishCardList)
    }

    override fun onPositiveDialogResult(dialogId: Int) {
        when (dialogId) {
            DIALOG_ID_DELETE -> {
                mViewModel.deleteCurrentList()
                finish()
            }
        }

    }

    override fun onBottomItemClick(id: Int) {
            mViewModel.onBottomItemClick(id)
    }

    override fun setFacIcon(drawable: Drawable) {
        binding.floating.setImageDrawable(drawable)
    }


}