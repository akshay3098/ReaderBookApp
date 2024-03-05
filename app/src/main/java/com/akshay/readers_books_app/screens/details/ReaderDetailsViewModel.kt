package com.akshay.readers_books_app.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshay.readers_books_app.data.Resource
import com.akshay.readers_books_app.model.Item
import com.akshay.readers_books_app.repositories.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderDetailsViewModel @Inject constructor(private val repository: BookRepository) : ViewModel(){

    suspend fun getBookInfo(bookId : String): Resource<Item>{

        return repository.getBookInfo(bookId)
    }
}