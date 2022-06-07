package com.imadev.foody.repository

import com.imadev.foody.factory.FoodFactory
import com.imadev.foody.fcm.remote.PushNotification
import com.imadev.foody.model.*
import com.imadev.foody.utils.Resource
import com.imadev.foody.utils.safeFirebaseCall
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

class FoodyRepoImpTest : FoodyRepo {

    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        this.shouldReturnError = value
    }

    override suspend fun getMealsByCategory(categoryId: String): Flow<Resource<List<Meal?>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategories(): Flow<Resource<List<Category?>>> = safeFirebaseCall {
        when (shouldReturnError) {
            false -> {
                FoodFactory.categories()
            }
            true -> {
                throw Exception("Error occurs")
            }
        }
    }


    override suspend fun getClient(uid: String): Flow<Resource<Client>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateField(
        collectionName: String,
        uid: String,
        field: String,
        value: Any
    ): Flow<Resource<Void?>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendNotification(pushNotification: PushNotification): Response<ResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun getAvailableDeliveryUsers(): Flow<Resource<List<DeliveryUser?>>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendOrderToDeliveryUser(order: Order): Flow<Resource<Void?>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMeals(): Flow<Resource<List<Meal?>>> {
        TODO("Not yet implemented")
    }
}
