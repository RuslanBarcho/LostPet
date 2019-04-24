package io.vinter.lostpet.ui.list.filter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import io.vinter.lostpet.R
import io.vinter.lostpet.network.form.FilterForm
import io.vinter.lostpet.ui.list.AllPetsFragment
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : DialogFragment() {

    val checked = booleanArrayOf(true, true, true, true, true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        check_cat.setOnCheckedChangeListener { _, b ->
            checked[0] = b
        }
        check_dog.setOnCheckedChangeListener { _, b ->
            checked[1] = b
        }
        check_lost.setOnCheckedChangeListener { _, b ->
            checked[2] = b
        }
        check_found.setOnCheckedChangeListener { _, b ->
            checked[3] = b
        }
        check_good_hands.setOnCheckedChangeListener { _, b ->
            checked[4] = b
        }
        filter_done.setOnClickListener {
            (targetFragment as AllPetsFragment).filterList(getFilter())
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(R.layout.fragment_filter)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.BOTTOM
        window.attributes = wlp
        window.attributes.windowAnimations = R.style.DialogAnimation
        return dialog
    }

    private fun getFilter(): FilterForm{
        val pets = ArrayList<String>()
        val adverts = ArrayList<String>()
        if (checked[0]) pets.add("cat")
        if (checked[1]) pets.add("dog")
        if (checked[2]) adverts.add("lost")
        if (checked[3]) adverts.add("found")
        if (checked[4]) adverts.add("good-hands")
        return FilterForm(pets, adverts)
    }

}
