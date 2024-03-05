package com.akshay.readers_books_app.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshay.readers_books_app.data.Resource
import com.akshay.readers_books_app.model.Item
import com.akshay.readers_books_app.repositories.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class BooksSearchViewModel @Inject constructor(private val repository: BookRepository): ViewModel(){

    var list: List<Item> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)
    init {
        loadBooks()
    }

    private fun loadBooks(){
        SearchBooks("flutter")
    }

    fun SearchBooks(Query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if(Query.isEmpty()){
                return@launch
            }
            try{
                when(val response= repository.getBooks(Query)){
                    is Resource.Success -> {
                        list = response.data!!
                        if(list.isNotEmpty()) isLoading = false
                    }
                    is Resource.Error -> {
                        isLoading = false
                        Log.e("Network", "search Books: Failed getting books")
                    }
                    else -> {isLoading = false}
                }

            }catch (exception: Exception) {
                isLoading = false
                Log.d("Network","SearchBooks: ${exception.message.toString()}")
            }
        }
    }
}