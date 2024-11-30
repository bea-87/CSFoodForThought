package com.example.csfoodforthought

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.example.csfoodforthought.LikedPage
import com.example.csfoodforthought.ListPage
import com.example.csfoodforthought.MainActivity

class SearchPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)
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
        val ListBtn = findViewById<Button>(R.id.ListPageButton)
        ListBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ListPage::class.java)
            startActivity(intent)
        })
    }
}