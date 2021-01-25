package com.applications.fishcardroomandmvvm

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


fun hideKeyboard(activity: Activity) {
    val imm: InputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view: View? = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.toDp(value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,resources.displayMetrics)
fun Context.toDp(value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,resources.displayMetrics)




fun FragmentManager.removeFragmentWithTag(tag: String){
    val fragment = findFragmentByTag(tag)
    if(fragment != null){
        beginTransaction().remove(fragment).commit()
    }
}


// functions from course https://www.udemy.com/course/android-oreo-kotlin-app-masterclass/

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun FragmentActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment)}
}

fun FragmentActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction{ replace(frameId, fragment)}
}

fun FragmentActivity.removeFragment(fragment: Fragment) {
    supportFragmentManager.inTransaction { remove(fragment)}
}


