package com.imadev.foody.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.imadev.foody.R
import com.imadev.foody.utils.Constants.CLIENTS_COLLECTION
import com.imadev.foody.utils.Constants.FCM_TOKEN
import com.imadev.foody.utils.Constants.FCM_TOKEN_PREF
import com.imadev.foody.utils.Constants.NOTIFICATION_ID
import com.imadev.foody.utils.Constants.ORDER_CHANNEL_ID
import com.imadev.foody.utils.Constants.TOKEN_FIELD


private const val TAG = "FirebaseMessagingServic"
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

        getSharedPreferences(FCM_TOKEN_PREF, MODE_PRIVATE).edit().putString(FCM_TOKEN, newToken).apply()

        updateTokenForUser(newToken)

    }

    private fun updateTokenForUser(newToken: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        Log.d(TAG, "updateTokenForUser: $newToken")
        Firebase.firestore.collection(CLIENTS_COLLECTION).document(uid).update(TOKEN_FIELD,newToken).addOnFailureListener {
            Toast.makeText(this, "Failed updating token ", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        createNotification(message)
    }


    private fun createNotification(remoteMessage: RemoteMessage) {
        val notification = remoteMessage.notification
        
        createNotificationChannel()

        val builder = NotificationCompat.Builder(this, ORDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notification?.title)
            .setContentText(notification?.body)
            .setPriority(NotificationCompat.DEFAULT_SOUND)

        Log.d(TAG, "createNotification: ${remoteMessage.data?.toString()}")

        Log.d(TAG, "createNotification: ${notification?.title}")


        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_ID, builder.build())
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(ORDER_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        fun getToken(context: Context): String? =  context.getSharedPreferences(FCM_TOKEN_PREF, MODE_PRIVATE).getString(FCM_TOKEN, null)
    }
}