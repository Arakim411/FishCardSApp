package com.applications.fishcardroomandmvvm.customViews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.dataClasses.ChoiceViewItem
import com.applications.fishcardroomandmvvm.toDp
import kotlinx.android.synthetic.main.choice_view_item.view.*
import java.lang.IllegalArgumentException

// i create custom constructor and don't pass AttributeSet, because i won't create this view in xml layout file , but always from code
// and this view is independent from parents param like constraint or gravity, this view is only displayed for a while in place where user long click on item in listView
@SuppressLint("ViewConstructor")
class ChoiceWindow @SuppressLint("ViewConstructor") constructor(
    context: Context,
    private val events: ChoiceWindowEvents,
    private val root: ViewGroup
) : LinearLayout(context, null) {

    private var items = ArrayList<ChoiceViewItem>()
    private var layout: LinearLayout =
        LinearLayout(context, null) // it is our main view in which we keep our items
    //view in constructor is only set listener on them and after when our window is open and we click anyWhere, we close choiceView

    interface ChoiceWindowEvents {
        fun onItemChoice(itemId: Int)
        fun onRemove()
    }

    init {

        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER_HORIZONTAL

        layout.setPadding(toDp(16f).toInt(), toDp(8f).toInt(), toDp(16f).toInt(), toDp(8f).toInt())

        layout.orientation = VERTICAL
        layout.setBackgroundColor(context.getColor(R.color.white))

        layout.layoutParams = params


    }

    fun setChoices(items: ArrayList<ChoiceViewItem>) {
        this.items = items
        rebuildView()
    }

    private fun rebuildView() {
        this.removeAllViews()

        var a = 0

        for (i in items.iterator()) {
            val name = i.name
            val resourceDrawable = i.drawableResources
            val itemId = i.id

            if (name.isEmpty()) {
                throw  IllegalArgumentException("name can't be empty")
            }

            val item = LayoutInflater.from(context).inflate(R.layout.choice_view_item, this, false)

            if (a != 0) {
                val params = item.layoutParams as LinearLayout.LayoutParams
                params.setMargins(0, toDp(16f).toInt(), 0, 0)
            }

            item.choice_name.text = name
            item.choice_image.setImageResource(resourceDrawable)


            item.setOnClickListener {
                events.onItemChoice(itemId)
                close()
            }

            layout.addView(item)

            a++
        }

        addView(layout)
        layout.invalidate()
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        close()
        return super.onTouchEvent(event)
    }


    private fun close() {
        events.onRemove()
        root.removeView(this)
    }

    fun setPosition(x: Float, y: Float) {
        layout.x = x
        layout.y = y
    }

    fun show() {
        root.addView(this)
    }


}