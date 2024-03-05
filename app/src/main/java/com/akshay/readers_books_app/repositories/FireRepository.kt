package com.akshay.readers_books_app.repositories

import com.akshay.readers_books_app.data.DataOrException
import com.akshay.readers_books_app.data.Resource
import com.akshay.readers_books_app.model.Item
import com.akshay.readers_books_app.model.MBook
import com.akshay.readers_books_app.network.BooksApi
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.Exception

class FireRepository @Inject constructor(private val queryBook: Query) {
    suspend fun getAllBooksFromDatabase(): DataOrException<List<MBook>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryBook.get()
                .await().documents.map { documentSnapshot -> documentSnapshot.toObject(MBook::class.java)!! }
            if(!dataOrException.data.isNullOrEmpty()) dataOrException.loading= false
        }
        catch (exception: FirebaseException) {
            dataOrException.e = exception

        }
        return dataOrException
    }
}