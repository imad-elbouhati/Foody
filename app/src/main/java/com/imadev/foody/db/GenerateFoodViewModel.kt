package com.imadev.foody.db

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.imadev.foody.R
import com.imadev.foody.model.Category
import com.imadev.foody.model.Meal
import com.imadev.foody.utils.Constants.Companion.CATEGORY_COLLECTION
import com.imadev.foody.utils.Constants.Companion.CATEGORY_DRINKS
import com.imadev.foody.utils.Constants.Companion.CATEGORY_FOODS
import com.imadev.foody.utils.Constants.Companion.CATEGORY_SNACKS
import com.imadev.foody.utils.Constants.Companion.MEALS_COLLECTION
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*


private const val TAG = "GenerateFoodViewModel"

class GenerateFoodViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()


    fun uploadCategories() = viewModelScope.launch {

        val categories = listOf(
            Category(name = CATEGORY_FOODS),
            Category(name = CATEGORY_DRINKS),
            Category(name = CATEGORY_SNACKS)
        )

        categories.map {
            async {
                db.collection(CATEGORY_COLLECTION).add(it)
            }
        }
    }


    val categoryFlow = MutableSharedFlow<String>()


    suspend fun getCategories() {
        val task = db.collection(CATEGORY_COLLECTION).get().await()
        task.documents.map {
            delay(1000)
            categoryFlow.emit(it.id)
        }
    }


    private fun insertMeal() {
        val img = "https://firebasestorage.googleapis.com/v0/b/foody-341522.appspot.com/o/images%2Fimages%2Fbrochettes%20de%20poulet.jpg?alt=media&token=3c615edb-1ef2-467e-8211-f9d9b7666091"
        val name = "Chicken brochettes"
        val ingredient = listOf(
            "1 cup plain nonfat yogurt",
            "5 tablespoons (packed) crumbled feta cheese",
            "2 teaspoons minced garlic",
            "1 teaspoon chopped fresh rosemary",
            "1/4 teaspoon pepper",
            "1 1/2 pounds skinless boneless chicken breast halves, cut into 1-inch\u00a0pieces",
            "2 large red bell peppers, cut into 1-inch pieces",
            "6 10- to 12-inch-long wooden skewers, soaked in water 30 minutes"
        )

        val catID = "rEFPNmJmVjmlIiNNjb0P"
        val price = 150.0

        val meal = Meal(
            name = name,
            price = price,
            ingredient = ingredient,
            image = img,
            categoryId = catID,
        )


        db.collection(MEALS_COLLECTION).add(meal).addOnSuccessListener {
            Log.d(TAG, "insertMeal: success")
        }.addOnFailureListener {
            Log.d(TAG, "insertMeal: ${it.message}")
        }
    }


}