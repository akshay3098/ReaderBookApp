package com.akshay.readers_books_app.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshay.readers_books_app.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginScreenViewModel: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _loading= MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ){
        viewModelScope.launch {
            try{
                if(_loading.value == false) {
                    _loading.value= true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                val displayName= task.result?.user?.email.toString().split("@")[0]
                                createUser(displayName)
                                home()
                            } else {
                                Log.d("FB" ,"creasteUserWithEmailAndPassword : ${task.result.toString()}")
                            }
                        }
                    _loading.value= false
                }
            } catch (ex: Exception) {
                Log.d("FB", "signInWithEmailAndPassword: ${ex.message}")
            }

        }

    }

    private fun createUser(displayName: String?) {

        val userId= auth.currentUser?.uid
        val users=  MUser(
            userId = userId.toString(), display_name = displayName.toString(), quote = "Life is Great", profession = "Android Developer", id = null
        ).toMap()


        FirebaseFirestore.getInstance().collection("users")
            .add(users)

    }

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit ) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task -> if(task.isSuccessful) {
                        Log.d("FB", "signInWithEmailAndPassword: Yayayay! ${task.result.toString()}")
                        home()
                    }else {
                        Log.d("FB" ,"creasteUserWithEmailAndPassword : ${task.result.toString()}")
                    }
                    }
            } catch (ex: Exception) {
                Log.d("FB", "signInWithEmailAndPassword: ${ex.message}")
            }
        }
}