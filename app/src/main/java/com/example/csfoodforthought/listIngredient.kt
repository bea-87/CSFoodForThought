package com.example.csfoodforthought

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.util.Log
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager

class listIngredient : Fragment(R.layout.fragment_list_ingredient) {
    private lateinit var ingredient: String
    private lateinit var checkBox: CheckBox
    private var listener: IngredientCheckedChangeListener? = null

    interface IngredientCheckedChangeListener {
        fun onIngredientChecked(ingredient: String, isChecked: Boolean)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = requireArguments()
        ingredient = args.getString("ingredient") ?: ""

        checkBox = view.findViewById(R.id.ingredientCheckBox)
        checkBox.text = ingredient

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            listener?.onIngredientChecked(ingredient, isChecked)
        }
    }

    fun setIngredientCheckedChangeListener(listener: IngredientCheckedChangeListener) {
        this.listener = listener
    }
}
