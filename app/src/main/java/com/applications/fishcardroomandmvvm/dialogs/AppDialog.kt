package com.applications.fishcardroomandmvvm.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.applications.fishcardroomandmvvm.R
import java.lang.ClassCastException
import java.lang.IllegalArgumentException

const val DIALOG_ID_DELETE = 0

 const val DIALOG_ID = "DIALOG_ID"
 const val DIALOG_MESSAGE = "DIALOG_MESSAGE"
 const val DIALOG_POSITIVE = "positive"
 const val DIALOG_NEGATIVE = "negative"


class AppDialog : AppCompatDialogFragment() {

    private var dialogEvents: DialogEvents? = null

    internal interface DialogEvents {
        fun onPositiveDialogResult(dialogId: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(context)

        val arguments = arguments
        val dialogId: Int
        val messageString: String?
        var positiveStringId: Int
        var negativeStringId: Int

        if (arguments != null) {
            dialogId = arguments.getInt(DIALOG_ID)
            messageString = arguments.getString(DIALOG_MESSAGE)

            positiveStringId = arguments.getInt(DIALOG_POSITIVE,0)
            if(positiveStringId == 0){
                positiveStringId = R.string.ok
            }

            negativeStringId = arguments.getInt(DIALOG_NEGATIVE,0)
            if(negativeStringId == 0){
                negativeStringId = R.string.cancel
            }

        }else{
            throw IllegalArgumentException("arguments can't be null")
        }


        return builder.setMessage(messageString)
            .setPositiveButton(positiveStringId) {_, _ ->
                dialogEvents?.onPositiveDialogResult(dialogId)
            }
            .setNegativeButton(negativeStringId) {_ ,_ ->

            }.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        dialogEvents = try {
            context as DialogEvents
        } catch (e: TypeCastException) {

            try {

                parentFragment as DialogEvents
            } catch (e: ClassCastException) {

                throw  ClassCastException("Activity $context must implement DialogEvents")
            }
        }
    }

    override fun onDetach() {
        super.onDetach()

        dialogEvents = null
    }
}

