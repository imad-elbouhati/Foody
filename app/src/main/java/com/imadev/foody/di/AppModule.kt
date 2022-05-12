package com.imadev.foody.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imadev.foody.repository.FoodyRepo
import com.imadev.foody.repository.FoodyRepoImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }


    @Provides
    @Singleton
    fun provideFoodyRepoImp(
        db: FirebaseFirestore
    ): FoodyRepo = FoodyRepoImp(db)

}