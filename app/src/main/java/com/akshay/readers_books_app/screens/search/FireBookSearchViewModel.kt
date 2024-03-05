package com.akshay.readers_books_app.screens.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshay.readers_books_app.data.DataOrException
import com.akshay.readers_books_app.model.Item
import com.akshay.readers_books_app.model.MBook
import com.akshay.readers_books_app.repositories.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FireBookSearchViewModel @Inject constructor(private val repository: FireRepository): ViewModel() {

     val listOfBooks: MutableState<DataOrException<List<MBook>, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(null, true, Exception(""))
        )

    init {
        searchBooks("android")
    }

     fun searchBooks(query: String) {
        viewModelScope.launch() {
            if(query.isEmpty()){
                return@launch
            }
            listOfBooks.value.loading= true
            listOfBooks.value= repository.getAllBooksFromDatabase()

            if(listOfBooks.value.data.toString().isNotEmpty()) listOfBooks.value.loading= false
        }
    }
}