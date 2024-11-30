package com.example.csfoodforthought

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupWindow
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


class RecipePage : AppCompatActivity() {
    companion object {
        var recipeImageList = mutableListOf<Int>(
            R.drawable.chicken_pasta_bake,
            R.drawable.tomatosoup,
            R.drawable.sausagelentilonepot,
            R.drawable.chicken_curry,
            R.drawable.pancakes
        )
        var recipeImageListCustom = mutableListOf<String>()

        var recipeTimes = mutableListOf<Int>(75, 25, 50, 30, 50)
        var recipeNames = mutableListOf<String>("Chicken pasta bake", "Tomato soup", "Sausage, roasted veg & puy lentil one-pot", "Chicken curry", "Pancakes")
        var recipePortions = mutableListOf<String>("6", "6-8", "4", "4", "12")
        var recipeLiked = mutableListOf<Boolean>(false, false, false, false, false)
        var recipeIngredients = mutableListOf(
            listOf("4", " tbsp olive oil",
                "1", " onion",
                "2", " garlic cloves",
                "1", " tsp chilli flakes",
                "2", " 400g cans chopped tomatoes",
                "1", " tsp caster sugar",
                "6", " tbsp mascarpone",
                "4", " skinless chicken breasts",
                "300", " g penne",
                "70", " g mature cheddar, grated",
                "50", " g grated mozzarella",
                "1", " small bunch of parsley"),
            listOf("1", "tbsp olive oil",
                "2", "garlic cloves",
                "5", "sundried tomatoes, roughly chopped",
                "3", "x 400g cans plum tomatoes",
                "500", "ml turkey or vegetable stock",
                "1", "tsp sugar, any type, or more to taste",
                "140", "ml soured cream",
                "1", "tbsp pesto",
                "1", "basil leaves, to serve"),
            listOf("8", " sausages", "800", "g of ready to roast vegetables", "3", " garlic cloves", "2", " tbsp of olive oil", "1", " tsp of smoked paprika", "500", " g of puy lentils", "1", " tbsp sherry or red wine vinegar", "1", " small pack of parsley"),
            listOf("2", " tbsp sunflower oil",
                "1", " onion",
                "2", " garlic cloves",
                "3", " thumb-sized piece of ginger",
                "6", " chicken thighs",
                "3", " tbsp medium spice paste",
                "400", " g can chopped tomatoes",
                "100", " g Greek yogurt",
                "1", " small bunch of coriander",
                "50", " g ground almonds"),
            listOf("200", "g self-raising flour", "1", "tsp of baking powder", "15", "g sugar", "2", "eggs", "300", "ml milk"));
        var recipeMethod = mutableListOf<String>("a", "a", "a", "a", "a")
        fun initialize(context: Context) {
            recipeMethod[0] = context.getString(R.string.chicken_pasta_bake_method)
            recipeMethod[1] = context.getString(R.string.tomato_soup_method)
            recipeMethod[2] = context.getString(R.string.sausage_roasted_veg_and_puy_lentils_one_pot_method)
            recipeMethod[3] = context.getString(R.string.curry_method)
            recipeMethod[4] = context.getString(R.string.pancake_method)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_page)

        initialize(this)

        displayRecipe()
        createPageButtons()
        createShoppingListButton()
        createLikeButton()
    }

    fun displayRecipe() {
        val recipeName = intent.getStringExtra("recipeName")
        val recipenameTextView = findViewById<TextView>(R.id.recipename)
        recipenameTextView.text = recipeName

        val position = recipeNames.indexOf(recipeName)


        val recipeImageView = findViewById<ImageView>(R.id.recipeimage)
        if (position < recipeImageList.size) {
            val recipeImage = recipeImageList[position]
            recipeImageView.setImageResource(recipeImage)
        } else{
            var imagePath = recipeImageListCustom[position - recipeImageList.size]
            // Load the image into the ImageView using Glide
            Glide.with(this)
                .load(File(imagePath))
                .into(recipeImageView)
        }


        var recipeIngredientsString = ""
        Log.d("ingredientCount", recipeIngredients[position].count().toString())
        for (i in 0 until recipeIngredients[position].count()) {
            recipeIngredientsString = recipeIngredientsString + recipeIngredients[position][i]
            if (i.rem(2) == 1) {
                recipeIngredientsString = recipeIngredientsString + "\n"
            }
        }

        val recipeIngredientsTextView = findViewById<TextView>(R.id.ingredients)
        recipeIngredientsTextView.text = recipeIngredientsString

        val recipeMethod = recipeMethod[position]
        val recipeMethodTextView = findViewById<TextView>(R.id.method)
        recipeMethodTextView.text = recipeMethod

        val recipeTime = Companion.recipeTimes[position]
        val recipeTimeTextView = findViewById<TextView>(R.id.timeForRecipe)
        recipeTimeTextView.text = recipeTime.toString() + " minutes"

        val recipePortions = Companion.recipePortions[position]
        val recipePortionsTextView = findViewById<TextView>(R.id.peopleToServe)
        recipePortionsTextView.text = recipePortions.toString() + " people"

        val likeButton = findViewById<ImageButton>(R.id.likeRecipeButton)
        if (recipeLiked[position]) {
            likeButton.setBackgroundResource(R.drawable.likedhearticon)
        } else {
            likeButton.setBackgroundResource(R.drawable.unlikedheart)
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
    }

    fun createShoppingListButton() {
        val ListBtn = findViewById<ImageButton>(R.id.addToListButton)
        ListBtn.setOnClickListener {
            val recipeName = intent.getStringExtra("recipeName")
            val position = recipeNames.indexOf(recipeName)

            for (ingredient in recipeIngredients[position].chunked(2)) {
                val ingredientText = "${ingredient[0]} ${ingredient[1]}"

                val foundIngredient = ListPage.shoppingList.find {
                    it.substringAfter(" ") == ingredient[1]
                }

                if (foundIngredient != null) {
                    val currentAmount = foundIngredient.substringBefore(" ").toIntOrNull() ?: 0
                    val additionalAmount = ingredient[0].toIntOrNull() ?: 0
                    val updatedAmount = currentAmount + additionalAmount

                    val pos = ListPage.shoppingList.indexOf(foundIngredient)
                    ListPage.shoppingList.removeAt(pos)
                    ListPage.checkedItems.removeAt(pos)
                    ListPage.shoppingList.add("$updatedAmount ${ingredient[1]}")
                    ListPage.checkedItems.add(false)
                } else {
                    ListPage.shoppingList.add(ingredientText)
                    ListPage.checkedItems.add(false)
                }
            }
            showPopup()
        }
    }


    fun createLikeButton() {
        val likeButton = findViewById<ImageButton>(R.id.likeRecipeButton)
        likeButton.setOnClickListener {
            val recipeName = intent.getStringExtra("recipeName")
            val position = recipeNames.indexOf(recipeName)
            recipeLiked[position] = !recipeLiked[position]
            if (recipeLiked[position]) {
                likeButton.setBackgroundResource(R.drawable.likedhearticon)
            } else {
                likeButton.setBackgroundResource(R.drawable.unlikedheart)
            }
            val recipePage = RecipePage()
            recipePage.saveData(this)        }
    }
    private fun showPopup() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_shopping_list, null)

        val okButton = popupView.findViewById<Button>(R.id.okButton)

        val popupWidth = resources.getDimensionPixelSize(R.dimen.popup_width)
        val popupWindow = PopupWindow(popupView, popupWidth, LinearLayout.LayoutParams.WRAP_CONTENT, true)

        okButton.setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }


    public fun saveData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("RecipeData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()

        // Convert lists to JSON strings
        val imageListJson = gson.toJson(recipeImageList)
        val timesJson = gson.toJson(recipeTimes)
        val namesJson = gson.toJson(recipeNames)
        val portionsJson = gson.toJson(recipePortions)
        val likedJson = gson.toJson(recipeLiked)
        val ingredientsJson = gson.toJson(recipeIngredients)
        val methodsJson = gson.toJson(recipeMethod)
        val recipeImageListCustomJson = gson.toJson(recipeImageListCustom)



        if (!recipeImageListCustom.isNullOrEmpty()) {
            editor.putString("recipe_image_list", Gson().toJson(recipeImageListCustom))
        }
        // Save JSON strings to SharedPreferences
        editor.putString("recipeImageList", imageListJson)
        editor.putString("recipeTimes", timesJson)
        editor.putString("recipeNames", namesJson)
        editor.putString("recipePortions", portionsJson)
        editor.putString("recipeLiked", likedJson)
        editor.putString("recipeIngredients", ingredientsJson)
        editor.putString("recipeMethod", methodsJson)
        editor.putString("recipeImageListCustom", recipeImageListCustomJson)


        editor.apply()

        Log.d("SaveData", "Saved imageListJson: $imageListJson")
        Log.d("SaveData", "Saved timesJson: $timesJson")
    }

    public fun loadData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("RecipeData", Context.MODE_PRIVATE)
        val gson = Gson()

        if (sharedPreferences.contains("recipe_image_list")) {

            // Retrieve JSON strings from SharedPreferences
            val imageListJson = sharedPreferences.getString("recipeImageList", null)
            val timesJson = sharedPreferences.getString("recipeTimes", null)
            val namesJson = sharedPreferences.getString("recipeNames", null)
            val portionsJson = sharedPreferences.getString("recipePortions", null)
            val likedJson = sharedPreferences.getString("recipeLiked", null)
            val ingredientsJson = sharedPreferences.getString("recipeIngredients", null)
            val methodsJson = sharedPreferences.getString("recipeMethod", null)
            val recipeImageListCustomJson = sharedPreferences.getString("recipeImageListCustom", null)

            // Convert JSON strings back to lists
            val typeInt = object : TypeToken<List<Int>>() {}.type
            val typeString = object : TypeToken<List<String>>() {}.type
            val typeBoolean = object : TypeToken<List<Boolean>>() {}.type
            val typeListOfStringLists = object : TypeToken<List<List<String>>>() {}.type

            recipeImageList = gson.fromJson(imageListJson, typeInt) ?: mutableListOf()
            recipeTimes = gson.fromJson(timesJson, typeInt) ?: mutableListOf()
            recipeNames = gson.fromJson(namesJson, typeString) ?: mutableListOf()
            recipePortions = gson.fromJson(portionsJson, typeString) ?: mutableListOf()
            recipeLiked = gson.fromJson(likedJson, typeBoolean) ?: mutableListOf()
            recipeIngredients = gson.fromJson(ingredientsJson, typeListOfStringLists) ?: mutableListOf()
            recipeMethod = gson.fromJson(methodsJson, typeString) ?: mutableListOf()
            recipeImageListCustom = gson.fromJson(recipeImageListCustomJson, typeString) ?: mutableListOf()
            Log.d("LoadData", "Retrieved imageListJson: $imageListJson")
            Log.d("LoadData", "Retrieved timesJson: $timesJson")
        }
    }


}

