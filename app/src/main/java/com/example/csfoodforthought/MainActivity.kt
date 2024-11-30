package com.example.csfoodforthought

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.util.Log
import android.widget.LinearLayout
import android.widget.SearchView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recipePage = RecipePage()
        recipePage.loadData(this)

        createPageButtons()
        createSearch()
        createPageFragments(null)
    }

    fun createSearch() {
        val searchView = findViewById<SearchView>(R.id.SearchBar)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Gets called when user inputs something
                // 'query' holds the text entered
                createPageFragments(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Error if this doesn't get implemented as well?
                createPageFragments(newText)
                return true
            }
        })
    }

    private fun createPageFragments(searchString: String?) {
        val fragmentContainerOne = findViewById<LinearLayout>(R.id.fragmentContainerOne)
        val fragmentContainerTwo = findViewById<LinearLayout>(R.id.fragmentContainerTwo)
        val fragmentManager = supportFragmentManager

        // Clear existing fragments before adding new ones
        fragmentContainerOne.removeAllViews()
        fragmentContainerTwo.removeAllViews()

        val numberOfFragmentsToCreate = RecipePage.recipeNames.size
        var addToOne = true
        if (searchString.isNullOrEmpty()) {
            for (i in 0 until numberOfFragmentsToCreate) {
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
        } else {
            // Display recipes matching the search query
            for (i in 0 until numberOfFragmentsToCreate) {
                if (RecipePage.recipeNames[i].contains(searchString, ignoreCase = true)) {
                    val fragment = RecipeDisplays()

                    val args = Bundle()
                    args.putString("recipeName", RecipePage.recipeNames[i])
                    args.putInt("recipeTime", RecipePage.recipeTimes[i])
                    args.putInt("recipeImage", RecipePage.recipeImageList[i])
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
        }
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
        val AddRecipeBtn = findViewById<Button>(R.id.AddRecipePageButton)
        AddRecipeBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddRecipePage::class.java)
            startActivity(intent)
        })
    }
}