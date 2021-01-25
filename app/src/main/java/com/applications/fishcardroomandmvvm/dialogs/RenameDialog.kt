package com.applications.fishcardroomandmvvm.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.applications.fishcardroomandmvvm.R
import com.applications.fishcardroomandmvvm.ROOM.model.FishCardList
import kotlinx.android.synthetic.main.change_name.view.*

private const val ARG_FISH_CARD_LIST = "FISH_CARD_LIST"
private  const val ARG_TEXT = "ARG_TEXT"

class RenameDialog : AppCompatDialogFragment() {
    //this class is used only for change name of FishCardList


    private var dialogEvents: UpdateListEvent? = null
    private var fishCardList: FishCardList? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fishCardList = arguments?.getParcelable(ARG_FISH_CARD_LIST)
    }

    interface UpdateListEvent {
        fun updateList(fishCardList: FishCardList)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.change_name, container, false)


        view.confirm.setOnClickListener {

            val newName = view.rename?.editText?.text.toString()
            if (newName.isNotEmpty()) {
                val updatedFishCard = FishCardList(
                    id = fishCardList?.id!!,
                    name = newName,
                    nativeLanguage = fishCardList?.nativeLanguage!!,
                    foreignLanguage = fishCardList?.foreignLanguage!!,
                    favorite = fishCardList?.favorite!!
                )
                dialogEvents?.updateList(updatedFishCard)
                Toast.makeText(context, "List updated", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(context, "name can't be empty", Toast.LENGTH_SHORT).show()
            }
        }

        view.cancel.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            dialogEvents = context as UpdateListEvent
               
        }catch (e: TypeCastException){
            throw TypeCastException("$context must implement UpdateListEvent")
        }
    }


    companion object {
        fun newInstance(fishCardList: FishCardList) =
            RenameDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FISH_CARD_LIST, fishCardList)
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARG_TEXT,view?.rename?.editText?.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
      view?.rename?.editText?.setText(savedInstanceState?.getString(ARG_TEXT) ?: "")
        view?.rename?.requestFocus()
    }


}