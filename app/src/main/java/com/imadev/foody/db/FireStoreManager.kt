package com.imadev.foody.db

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imadev.foody.model.Category
import com.imadev.foody.model.Client
import com.imadev.foody.model.Meal
import com.imadev.foody.utils.Constants
import com.imadev.foody.utils.Constants.Companion.CATEGORY_ID
import com.imadev.foody.utils.Resource
import com.imadev.foody.utils.safeFirebaseCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

private const val TAG = "FireStoreManager"
class FireStoreManager : FireStoreRepo {

    private val db = Firebase.firestore
    private val categoryCollection = db.collection(Constants.CATEGORY_COLLECTION)
    private val mealsCollection = db.collection(Constants.MEALS_COLLECTION)
    private val clientsCollection = db.collection(Constants.CLIENTS_COLLECTION)

    override suspend fun getMealsByCategory(categoryId: String) = safeFirebaseCall {

        mealsCollection.whereEqualTo(CATEGORY_ID, categoryId).get().await().documents.map {
           it.toObject(Meal::class.java)
        }
    }

    override suspend fun getCategories() = safeFirebaseCall {
        categoryCollection.get().await().documents.map {
            it.toObject(Category::class.java)
        }
    }

    override suspend fun getClient(uid: String): Flow<Client> = flow {


        clientsCollection.document(uid).get().await().toObject(Client::class.java)
    }


}