package com.imadev.foody.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.imadev.foody.fcm.remote.NotificationService
import com.imadev.foody.fcm.remote.PushNotification
import com.imadev.foody.model.Category
import com.imadev.foody.model.Client
import com.imadev.foody.model.Meal
import com.imadev.foody.utils.Constants.CATEGORY_COLLECTION
import com.imadev.foody.utils.Constants.CATEGORY_ID
import com.imadev.foody.utils.Constants.CLIENTS_COLLECTION
import com.imadev.foody.utils.Constants.MEALS_COLLECTION
import com.imadev.foody.utils.safeFirebaseCall
import kotlinx.coroutines.tasks.await
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "FireStoreManager"

class FoodyRepoImp @Inject constructor(
    private val db: FirebaseFirestore,
    private val api: NotificationService
) : FoodyRepo {

    private val categoryCollection = db.collection(CATEGORY_COLLECTION)
    private val mealsCollection = db.collection(MEALS_COLLECTION)
    private val clientsCollection = db.collection(CLIENTS_COLLECTION)


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

    override suspend fun getClient(uid: String) = safeFirebaseCall {

        clientsCollection.document(uid).get().await().toObject(Client::class.java)
    }

    override suspend fun updateField(
        collectionName: String,
        uid: String,
        field: String,
        value: Any
    ) = safeFirebaseCall {
        Log.d(TAG, "updateField: ^^^^")
        getCollection(collectionName)?.document(uid)?.update(field, value)?.await()
    }


    private fun getCollection(collectionName: String): CollectionReference? {

        return when (collectionName) {
            CLIENTS_COLLECTION -> {
                db.collection(collectionName)
            }
            else -> {
                null
            }
        }

    }


    override suspend fun sendNotification(pushNotification: PushNotification): Response<ResponseBody> {
        pushNotification.apply {
            return api.postNotification(
                pushNotification
            )
        }
    }
}