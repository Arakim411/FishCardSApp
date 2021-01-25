@file:Suppress("DEPRECATION")

package com.applications.fishcardroomandmvvm



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.applications.fishcardroomandmvvm.databinding.ActivityMainBinding
import com.applications.fishcardroomandmvvm.viewModels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var mViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init
        binding = ActivityMainBinding.bind(main_root)
        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        //Observe
        mViewModel.menuBarVisible.observe(
            this,
            { menuBarVisible ->
                if (!menuBarVisible) {
                    binding.toolbar.menu.setGroupVisible(R.id.app_bar_main_items_to_hide, false)
                } else {
                    binding.toolbar.menu.setGroupVisible(R.id.app_bar_main_items_to_hide, true)
                }
            })


        //toolbar
        setSupportActionBar(binding.toolbar)


        //navigation
        navController = findNavController(R.id.fragment)
        setupActionBarWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.fishCardListFragment -> mViewModel.menuBarVisible.value = true
            }
        }


        //others

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        //it triggers observer to set bar on View.Gone (if value is false)
        //if user rotate phone it reset toolbar and makes icons visible
        mViewModel.menuBarVisible.value = mViewModel.menuBarVisible.value
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.app_bar_add -> {
                navController.navigate(R.id.action_fishCardListFragment_to_addListFragment)
                mViewModel.menuBarVisible.value = false
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}

