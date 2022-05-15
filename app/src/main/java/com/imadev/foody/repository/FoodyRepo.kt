package com.imadev.foody.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.auth.User
import com.imadev.foody.model.Category
import com.imadev.foody.model.Client
import com.imadev.foody.model.Meal
import com.imadev.foody.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.util.ArrayList

interface FoodyRepo {

    suspend fun getMealsByCategory(categoryId: String):Flow<Resource<List<Meal?>>>

    suspend fun getCategories(): Flow<Resource<List<Category?>>>

    suspend fun getClient(uid:String):Flow<Resource<Client?>>

}