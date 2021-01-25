package com.applications.fishcardroomandmvvm.customViews

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.dataClasses.BottomSheetItem
import com.applications.fishcardroomandmvvm.toDp
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_item.view.*
import java.lang.IllegalArgumentException

private const val TAG = "BottomSheet"

const val BOTTOM_SHEET_ITEMS_ID = "BOTTOM_SHEET_ITEMS_ID"


class BottomSheet : BottomSheetDialogFragment() {

    var events: BottomSheetEvents? = null

    lateinit var rootLayout: LinearLayout  //in this fragment will be fill with child LinearLayout Fragments where one fragment represents one BottomSheetItem

    interface BottomSheetEvents {
        fun onBottomItemClick(id: Int)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootLayout = LinearLayout(context, null)
        rootLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        rootLayout.orientation = LinearLayout.VERTICAL

        rootLayout.setPadding(
            context?.toDp(8f)?.toInt()!!,
            (context?.toDp(8f)?.toInt()!!),
            (context?.toDp(8f)?.toInt()!!),
            (context?.toDp(8f)?.toInt()!!)
        )

        val items = arguments?.getParcelableArrayList<BottomSheetItem>(BOTTOM_SHEET_ITEMS_ID)

        Log.d(TAG, items.toString())

        if (items.isNullOrEmpty() || items.size < 2) throw error("arguments must contains list with at least 2 BottomSheetItems")


        for (i in items.indices) {
            val item: BottomSheetItem = items[i] as BottomSheetItem

            val itemLayout = layoutInflater.inflate(R.layout.bottom_sheet_item, rootLayout, false)

            itemLayout.setOnClickListener {
                events?.onBottomItemClick(item.id)!!
                dismiss()
            }

            itemLayout.bottomSheetItem_image.setImageResource(item.resourceDrawable)
            itemLayout.bottomSheetItem_text.text = item.text

            rootLayout.addView(itemLayout)
        }

        return rootLayout
    }

    fun setBackgroundColor(color: Int) = rootLayout.setBackgroundColor(color)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        events =
            when {
                context is BottomSheetEvents -> context
                parentFragment is BottomSheetEvents -> parentFragment as BottomSheetEvents
                else -> throw IllegalArgumentException("context or parentFragment must implement BottomSheetEvents interface")
            }
    }

    override fun onDetach() {
        events = null
        super.onDetach()
    }

}