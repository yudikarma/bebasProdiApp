package com.haekal.bebasprodiapp.ui.about


import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import com.haekal.bebasprodiapp.R
import androidx.appcompat.widget.AppCompatButton
import android.widget.ImageButton
import com.haekal.bebasprodiapp.utils.onClick
import kotlinx.android.synthetic.main.fragment_about.*


/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.haekal.bebasprodiapp.R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bt_getcode.onClick {
            showCustomDialog()
        }

    }


    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_dark)
        dialog.setCancelable(true)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.getWindow()?.getAttributes())
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        /*((TextView) dialog.findViewById(R.id.title)).setText(p.name);
        ((CircleImageView) dialog.findViewById(R.id.image)).setImageResource(p.image);*/

        (dialog.findViewById(R.id.bt_close) as ImageButton).onClick { dialog.dismiss() }

        (dialog.findViewById(R.id.bt_follow) as AppCompatButton).onClick {
            /* Toast.makeText(getApplicationContext(), "Follow Clicked", Toast.LENGTH_SHORT).show();*/
            dialog.dismiss()
        }

        dialog.show()
        dialog.getWindow()?.setAttributes(lp)
    }

}
