package com.example.csfoodforthought

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import java.io.File


class RecipeDisplays : Fragment(R.layout.fragment_recipe_displays) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createLikeButton(view)

        val likeButton = view.findViewById<ImageButton>(R.id.likeRecipeButton)
        val recipeName = requireArguments().getString("recipeName")

        val position = RecipePage.recipeNames.indexOf(recipeName)

        if (RecipePage.recipeLiked[position]) {
            likeButton.setBackgroundResource(R.drawable.likedhearticon)
        } else {
            likeButton.setBackgroundResource(R.drawable.unlikedheart)
        }

        val rootLayout = view.findViewById<RelativeLayout>(R.id.rootLayout)

        val recipenameTextView = view.findViewById<TextView>(R.id.recipename)
        recipenameTextView.text = recipeName


        val recipeTime = RecipePage.recipeTimes[position]
        val recipetimeView = view.findViewById<TextView>(R.id.RecipeTime)
        recipetimeView.text = "$recipeTime minutes"

        val recipeImageView = view.findViewById<ImageView>(R.id.recipeImageView)
        if (position < RecipePage.recipeImageList.size) {
            val recipeImage = RecipePage.recipeImageList[position]
            recipeImageView.setImageResource(recipeImage)
        } else{
            Log.d("position", position.toString())
            Log.d("recipeImageSize", RecipePage.recipeImageList.size.toString())
            Log.d("recipeCustomImageSize", RecipePage.recipeImageListCustom.size.toString())
            Log.d("index", (position - RecipePage.recipeImageList.size +1).toString())
            var imagePath = RecipePage.recipeImageListCustom[position - RecipePage.recipeImageList.size]
            // Load the image into the ImageView using Glide
            Glide.with(this)
                .load(File(imagePath))
                .into(recipeImageView)
        }


        rootLayout.setOnClickListener {
            val intent = Intent(requireContext(), RecipePage::class.java)
            intent.putExtra("recipeName", recipeName)
            startActivity(intent)
        }
    }

    fun createLikeButton(view: View) {
        val likeButton = view.findViewById<ImageButton>(R.id.likeRecipeButton)
        likeButton.setOnClickListener {
            val recipeName = requireArguments().getString("recipeName")
            val position = RecipePage.recipeNames.indexOf(recipeName)
            RecipePage.recipeLiked[position] = !RecipePage.recipeLiked[position]
            if (RecipePage.recipeLiked[position]) {
                likeButton.setBackgroundResource(R.drawable.likedhearticon)
            } else {
                likeButton.setBackgroundResource(R.drawable.unlikedheart)
            }
        }
    }
}



