package com.applications.fishcardroomandmvvm.adapters


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.ROOM.model.Word
import com.applications.fishcardroomandmvvm.listeners.WordRecyclerViewListener
import kotlinx.android.synthetic.main.word_item.view.*

private const val VIEW_TYPE_WORD = 0
private const val VIEW_TYPE_EMPTY = 1

class WordRecyclerViewAdapter(private val listener: WordRecyclerViewListener) :
    RecyclerView.Adapter<WordRecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var wordList = emptyList<Word>()

    private  var viewToRestore: View? = null
    private  var valueToRestore: Drawable? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return when (viewType) {
            VIEW_TYPE_WORD -> MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
            )
            VIEW_TYPE_EMPTY -> {
                MyViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.word_empty, parent, false)
                )
            }
            else -> throw  error("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        when (holder.itemViewType) {
            VIEW_TYPE_WORD -> {
                val word = wordList[position]
                holder.itemView.wordNumber.text = (position + 1).toString()
                holder.itemView.word.text = word.word
                holder.itemView.translations.text = word.translation.joinToString(", ")
                holder.itemView.goodAnswers.text = word.goodAnswers.toString()
                holder.itemView.badAnswers.text = word.badAnswers.toString()

                holder.itemView.setOnLongClickListener { view ->
                    valueToRestore  = holder.itemView.background
                    holder.itemView.setBackgroundColor(holder.itemView.context.getColor(R.color.light_yellow))
                    listener.onLongClick(word, view?.x!!, view.y)
                        viewToRestore = holder.itemView
                    true
                }


            }

            VIEW_TYPE_EMPTY -> {
            }
        }

    }

    override fun getItemCount(): Int {
        return if (wordList.isEmpty()) 1 else wordList.size
    }

    override fun getItemViewType(position: Int): Int =
        if (wordList.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_WORD

    fun setData(newData: List<Word>) {
        this.wordList = newData
        notifyDataSetChanged()
    }

    fun  restoreCheckedView() {
        if(viewToRestore != null){
            viewToRestore?.background = valueToRestore
            valueToRestore = null
            valueToRestore = null
        }
    }



}


