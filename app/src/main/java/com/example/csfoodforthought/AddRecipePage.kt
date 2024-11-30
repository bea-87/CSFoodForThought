package com.example.csfoodforthought

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.google.android.material.textfield.TextInputEditText
import java.io.File

class AddRecipePage : AppCompatActivity() {
    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 1001
        private const val PREF_IMAGE_KEY = "selected_image_uri"
    }

    private var selectedImageUri: Uri? = null
    private lateinit var usersImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe_page)
        createPageButtons()

        usersImageView = findViewById(R.id.usersImageView)

        requestStoragePermissions()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val newSelectedImageUri: Uri? = data.data
            if (newSelectedImageUri != null) {
                selectedImageUri = newSelectedImageUri
                saveSelectedImage(selectedImageUri)
                setImageToView(selectedImageUri!!)
            }
        }
    }

    private fun setImageToView(imageUri: Uri) {
        usersImageView.setImageURI(imageUri)
    }

    private fun saveSelectedImage(imageUri: Uri?) {
        val sharedPreferences = getSharedPreferences("MyImages", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(PREF_IMAGE_KEY, imageUri?.toString())
        editor.apply()
    }


    private fun saveImageToInternalStorage(imageUri: Uri): String {
        val inputStream = contentResolver.openInputStream(imageUri)
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val outputStream = openFileOutput(fileName, Context.MODE_PRIVATE)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        val filePath = File(filesDir, fileName).absolutePath

        return filePath
    }

    private val PERMISSION_REQUEST_CODE = 100

    private fun requestStoragePermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with accessing photos
            } else {
                // Permission denied, handle accordingly (show a message or disable functionality)
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

        val addRecipeBtn = findViewById<Button>(R.id.uploadImageButton)
        addRecipeBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            saveRecipe()
        }
    }


    fun saveRecipe(){
        val nameInputText = findViewById<TextInputEditText>(R.id.nameInputText).text.toString()
        val timeInputText = findViewById<TextInputEditText>(R.id.timeInputText).text.toString()
        val portionInputText = findViewById<TextInputEditText>(R.id.portionInputText).text.toString()
        val ingredientsInputText = findViewById<TextInputEditText>(R.id.ingredientsInputText).text.toString()
        val methodInputText = findViewById<TextInputEditText>(R.id.methodInputText).text.toString()

        if (verifyRecipeParamaters(nameInputText, timeInputText, portionInputText, ingredientsInputText, methodInputText)) {
            addTextParamatersToRecipeLists(nameInputText, timeInputText, portionInputText, ingredientsInputText, methodInputText)
            selectedImageUri?.let { imageUri ->
                val imagePath = saveImageToInternalStorage(imageUri)
                RecipePage.recipeImageListCustom.add(imagePath)
            }
            showPopup(true)
            clearParamaters()
            val recipePage = RecipePage()
            recipePage.saveData(this)
        } else {
                showPopup(false)
            }
        }


    fun addTextParamatersToRecipeLists(name: String, time: String, portion: String, ingredients: String, method: String){
        // Add recipe name to the recipe names list
        RecipePage.recipeNames.add(name)

        // Add recipe time to the recipe times list
        RecipePage.recipeTimes.add(time.toInt())

        // Add recipe portions to the recipe portions list
        RecipePage.recipePortions.add(portion)

        // Process ingredients text and add them as a list to the recipe ingredients list
        val ingredientsList = mutableListOf<String>()
        val ingredientsLines = ingredients.split("\n")
        Log.d("ld", ingredientsLines.toString())
        for (line in ingredientsLines) {
            val indexOfFirstNonDigit = line.indexOfFirst { !it.isDigit() }
            val quantity = line.substring(0, indexOfFirstNonDigit).trim()
            val ingredient = line.substring(indexOfFirstNonDigit).trim()
            ingredientsList.add(quantity)
            ingredientsList.add(ingredient)
        }
        Log.d("recipeIngredients", ingredientsList.toString())
        RecipePage.recipeIngredients.add(ingredientsList)

        //Add recipe method to recipe's method list
        RecipePage.recipeMethod.add(method)

        //Make recipe unliked to start with
        RecipePage.recipeLiked.add(false)
    }


    fun verifyRecipeParamaters(nameInputText: String, timeInputText: String, portionInputText: String, ingredientsInputText: String, methodInputText: String): Boolean {
        if (nameInputText != null && timeInputText != null && portionInputText != null && ingredientsInputText != null && methodInputText != null && selectedImageUri != null) {
            if (timeInputText.toIntOrNull()!=null){
                val ingredientLines = ingredientsInputText.split("\n")
                for (i in ingredientLines){
                    if (i[0].digitToIntOrNull()==null){
                        return false
                    }
                }
                return true
            } else {
                return false
            }
        } else {
            return false
        }
    }

    private fun showPopup(sucess: Boolean) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val suceededPopupView = inflater.inflate(R.layout.popup_added_reipe, null)
        val failedPopupView = inflater.inflate(R.layout.popup_recipe_add_failed, null)
        val popupWidth = resources.getDimensionPixelSize(R.dimen.popup_width)


        if (sucess) {
            val popupWindow =
                PopupWindow(suceededPopupView, popupWidth, LinearLayout.LayoutParams.WRAP_CONTENT, true)
            popupWindow.showAtLocation(suceededPopupView, Gravity.CENTER, 0, 0)

            val okButton = suceededPopupView.findViewById<Button>(R.id.okButton)
            okButton.setOnClickListener {
                popupWindow.dismiss()
            }
        } else{
            val popupWindow =
                PopupWindow(failedPopupView, popupWidth, LinearLayout.LayoutParams.WRAP_CONTENT, true)
            popupWindow.showAtLocation(failedPopupView, Gravity.CENTER, 0, 0)

            val okButton = failedPopupView.findViewById<Button>(R.id.okButton)
            okButton.setOnClickListener {
                popupWindow.dismiss()
            }
        }
    }

    fun clearParamaters(){
        val nameInputText = findViewById<TextInputEditText>(R.id.nameInputText)
        val timeInputText = findViewById<TextInputEditText>(R.id.timeInputText)
        val portionInputText = findViewById<TextInputEditText>(R.id.portionInputText)
        val ingredientsInputText = findViewById<TextInputEditText>(R.id.ingredientsInputText)
        val methodInputText = findViewById<TextInputEditText>(R.id.methodInputText)
        nameInputText.setText("")
        timeInputText.setText("")
        portionInputText.setText("")
        ingredientsInputText.setText("")
        methodInputText.setText("")
        usersImageView.setImageURI(null)
        selectedImageUri = null
    }

}

