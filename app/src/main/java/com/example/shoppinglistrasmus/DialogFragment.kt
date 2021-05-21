package org.pondar.shoppinglistrasmus

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.shoppinglistrasmus.R

open class DialogFragment (var posClick: () -> Unit, var negClick: () -> Unit= {}) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Dialogbuilder gets created
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(R.string.confirmation)
        alert.setMessage(R.string.areYouSure)
        alert.setPositiveButton(R.string.yes, pListener)
        alert.setNegativeButton(R.string.no, nListener)

        return alert.create()
    }

    // The positive listener for when user presses the YES button
    private var pListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener{_, _ ->
        posClick()
    }

    // The negative listener for when user presses the NO button
    private var nListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener{ _, _ ->
        negClick()
    }
}