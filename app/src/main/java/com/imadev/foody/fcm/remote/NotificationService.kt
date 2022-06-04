package com.imadev.foody.fcm.remote

import com.imadev.foody.model.Order
import com.imadev.foody.utils.Constants.CONTENT_TYPE
import com.imadev.foody.utils.Constants.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationService {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification,

    ): Response<ResponseBody>


}