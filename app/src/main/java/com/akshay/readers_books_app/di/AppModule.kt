package com.akshay.readers_books_app.di

import com.akshay.readers_books_app.network.BooksApi
import com.akshay.readers_books_app.repositories.FireRepository

import com.akshay.readers_books_app.utlis.Constants.BASE_URL
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFireBookRepository() = FireRepository(queryBook = FirebaseFirestore.getInstance().collection("books"))

//    @Singleton
//    @Provides
//    fun provideBookRepository(api: BooksApi)= BookRepository(api)


    @Singleton
    @Provides
    fun provideBookApi() : BooksApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)

    }
}