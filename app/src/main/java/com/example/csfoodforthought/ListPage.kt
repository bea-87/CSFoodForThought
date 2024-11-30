package com.example.csfoodforthought

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class ListPage : AppCompatActivity() {
    companion object {
        var shoppingList = mutableListOf<String>()
        var checkedItems = mutableListOf<Boolean>() // Keep track of checked/unchecked items with checked items being true and uncheed being false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_page)

        createPageButtons()
        createOrUpdateViews()
        createClearBtn()
    }

    private fun createOrUpdateViews() {
        val uncheckedItemsLayout = findViewById<LinearLayout>(R.id.uncheckedItemsLayout)
        val checkedItemsLayout = findViewById<LinearLayout>(R.id.checkedItemsLayout)

        //remove all items from the views so you can readd them without duplicates and stuff going wrong
        uncheckedItemsLayout.removeAllViews()
        checkedItemsLayout.removeAllViews()

        for ((index, item) in shoppingList.withIndex()) {
            //goes through shoppingList with two variables one for the item and one for the index of that item
            val checkBox = CheckBox(this)
            checkBox.text = item
            checkBox.isChecked = checkedItems.getOrNull(index) == true // Set checked status ==true needs to be there to avoid error

            //sets an event for when an item gets checked/unchecked
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                checkedItems[index] = isChecked
                createOrUpdateViews()
            }

            if (checkedItems.getOrNull(index) == true) {
                checkedItemsLayout.addView(checkBox)
            } else {
                uncheckedItemsLayout.addView(checkBox)
            }
        }

        val recipePage = RecipePage()
        recipePage.saveData(this)
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

    private fun createClearBtn(){
        val clearBtn = findViewById<Button>(R.id.clearCheckedItemsBtn)
        clearBtn.setOnClickListener(View.OnClickListener {
            val range = 0..(shoppingList.size-1)
            for (i in range.reversed()){
                if (checkedItems[i]){
                    shoppingList.removeAt(i)
                    checkedItems.removeAt(i)
                }
            }
            createOrUpdateViews()
        })
    }
}
