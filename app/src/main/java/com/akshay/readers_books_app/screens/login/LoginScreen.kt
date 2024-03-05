package com.akshay.readers_books_app.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.akshay.readers_books_app.R
import com.akshay.readers_books_app.components.PasswordInput
import com.akshay.readers_books_app.components.ReaderLogo
import com.akshay.readers_books_app.components.emailInput
import com.akshay.readers_books_app.navigation.ReaderScreens

@ExperimentalComposeUiApi
@Composable
fun loginScreen(navController: NavController, viewModel: LoginScreenViewModel= androidx.lifecycle.viewmodel.compose.viewModel()) {
    val showLoginForm= rememberSaveable {
        mutableStateOf(true)
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ReaderLogo()
            if(showLoginForm.value) userForm(loading = false, isCreateAccount = false){email, password ->
                    viewModel.signInWithEmailAndPassword(email = email, password = password){
                          navController.navigate(ReaderScreens.HomeScreen.name)
                    }
            }
                else {
                    userForm(loading = false, isCreateAccount = true){email, password ->
                        viewModel.createUserWithEmailAndPassword(email= email, password= password) {
                            navController.navigate(ReaderScreens.LoginScreen.name)
                        }
                    }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row(modifier = Modifier.padding(top= 15.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                val text= if(showLoginForm.value) "Sign up" else "Login"
                Text(text= "New User?")
                Text(text, modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable {
                        showLoginForm.value = !showLoginForm.value
                    }, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview
@ExperimentalComposeUiApi
@Composable
fun userForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, pwd -> }
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(
            rememberScrollState()
        )

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if(isCreateAccount) Text(text = stringResource(id = R.string.Create_Act)) else Text("")
        
        emailInput(
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions { passwordFocusRequest.requestFocus() })
        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
            }

        )

        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            loading= loading,
            validInput= valid,
        ){onDone(email.value.trim(), password.value.trim())
        keyboardController?.hide()
        }

    }

}

@Composable
fun SubmitButton(textId: String, loading: Boolean, validInput: Boolean, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp), enabled = !loading && validInput, shape = CircleShape) {
        if(loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))
                            
    }

}







