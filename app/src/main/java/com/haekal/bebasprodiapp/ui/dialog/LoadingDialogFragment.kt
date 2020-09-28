package com.haekal.bebasprodiapp.ui.dialog


import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.DialogFragment

import com.haekal.bebasprodiapp.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A simple [Fragment] subclass.
 */
class LoadingDialogFragment(var listener:LoadingDialogFragmentListener) : BottomSheetDialogFragment() {

    interface LoadingDialogFragmentListener {
        fun onDismis( dialog: BottomSheetDialogFragment)
    }

    override fun setupDialog(dialogParent: Dialog, style: Int) {
        super.setupDialog(dialogParent, style)
        val dialog : View = View.inflate(context,R.layout.fragment_loading_dialog, null)


        dialogParent.setContentView(dialog)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialog)

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(),theme)
        dialog.setCancelable(false)
        return dialog
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is LoadingDialogFragmentListener ){
            listener = parentFragment as LoadingDialogFragmentListener

        }
    }




}
