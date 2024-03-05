package com.akshay.readers_books_app.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshay.readers_books_app.data.DataOrException
import com.akshay.readers_books_app.model.MBook
import com.akshay.readers_books_app.repositories.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository): ViewModel() {
    val data: MutableState<DataOrException<List<MBook>,Boolean,Exception>> = mutableStateOf(
        DataOrException(listOf(),true,Exception(""))
    )

    init {
        getAllBooksFromDataBase()
    }

    private fun getAllBooksFromDataBase(){
        viewModelScope.launch {
            data.value.loading = true
            data.value= repository.getAllBooksFromDatabase()

            if(!data.value.data.isNullOrEmpty()) data.value.loading= false
        }
    }
}