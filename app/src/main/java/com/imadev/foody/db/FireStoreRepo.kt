package com.imadev.foody.db

import com.google.firebase.firestore.DocumentSnapshot
import com.imadev.foody.model.Category
import com.imadev.foody.model.Meal
import com.imadev.foody.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.util.ArrayList

interface FireStoreRepo {

    suspend fun getMealsByCategory(categoryId: String):Flow<Resource<List<Meal?>>>

    suspend fun getCategories(): Flow<Resource<List<Category?>>>

}