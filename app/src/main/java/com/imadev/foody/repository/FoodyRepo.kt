package com.imadev.foody.repository

import com.google.firebase.firestore.CollectionReference
import com.imadev.foody.fcm.remote.PushNotification
import com.imadev.foody.model.Category
import com.imadev.foody.model.Client
import com.imadev.foody.model.Meal
import com.imadev.foody.utils.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface FoodyRepo {

    suspend fun getMealsByCategory(categoryId: String): Flow<Resource<List<Meal?>>>

    suspend fun getCategories(): Flow<Resource<List<Category?>>>

    suspend fun getClient(uid: String): Flow<Resource<Client?>>

    suspend fun updateField(
        collectionName: String,
        uid: String,
        field: String,
        value: Any
    ): Flow<Resource<Void?>>


    suspend fun sendNotification(pushNotification: PushNotification): Response<ResponseBody>

}