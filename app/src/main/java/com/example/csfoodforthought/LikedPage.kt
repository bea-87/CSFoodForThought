package com.example.csfoodforthought

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout

class LikedPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_page)

        createPageButtons()
        createPageFragments()
    }

    fun createPageButtons() {
        val HomeBtn = findViewById<Button>(R.id.HomePageButton)
        HomeBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
        val LikedBtn = findViewById<ImageButton>(R.id.LikedPageButton)
        LikedBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, LikedPage::class.java)
            startActivity(intent)
        })
        val ListBtn = findViewById<ImageButton>(R.id.ListPageButton)
        ListBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ListPage::class.java)
            startActivity(intent)
        })
    }

    private fun createPageFragments() {
        val fragmentContainerOne = findViewById<LinearLayout>(R.id.fragmentContainerOne)
        val fragmentContainerTwo = findViewById<LinearLayout>(R.id.fragmentContainerTwo)
        val fragmentManager = supportFragmentManager

        // Clear existing fragments before adding new ones
        fragmentContainerOne.removeAllViews()
        fragmentContainerTwo.removeAllViews()

        val numberOfFragmentsToCreate = RecipePage.recipeNames.size
        var addToOne = true
        for (i in 0 until numberOfFragmentsToCreate) {
            if (RecipePage.recipeLiked[i]) {
                val fragment = RecipeDisplays()

                val args = Bundle()
                args.putString("recipeName", RecipePage.recipeNames[i])
                fragment.arguments = args

                val transaction = fragmentManager.beginTransaction()
                if (addToOne) {
                    transaction.add(fragmentContainerOne.id, fragment, "FragmentTag$i")
                } else{
                    transaction.add(fragmentContainerTwo.id, fragment, "FragmentTag$i")
                }
                addToOne = !addToOne
                transaction.commit()
            }
        }
        val recipePage = RecipePage()
        recipePage.saveData(this)
    }
}




