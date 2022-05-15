package com.imadev.foody.repository

import com.imadev.foody.factory.FoodFactory
import com.imadev.foody.model.Category
import com.imadev.foody.model.Client
import com.imadev.foody.model.Meal
import com.imadev.foody.utils.Resource
import com.imadev.foody.utils.safeFirebaseCall
import kotlinx.coroutines.flow.Flow

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
}
