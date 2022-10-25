package com.imadev.foody.fcm.remote

import android.util.Config
import com.imadev.foody.BuildConfig
import com.imadev.foody.model.Order
import com.imadev.foody.utils.Constants.CONTENT_TYPE
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationService {

    @Headers("Authorization: key=${BuildConfig.SERVER_KEY}", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification,

    ): Response<ResponseBody>


}