package com.akshay.readers_books_app.screens.update

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.akshay.readers_books_app.components.InputSate
import com.akshay.readers_books_app.components.RatingBar
import com.akshay.readers_books_app.components.roundedButton
import com.akshay.readers_books_app.components.topBar
import com.akshay.readers_books_app.data.DataOrException
import com.akshay.readers_books_app.model.MBook
import com.akshay.readers_books_app.navigation.ReaderScreens
import com.akshay.readers_books_app.screens.home.HomeScreenViewModel
import com.akshay.readers_books_app.utlis.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.Exception

@ExperimentalComposeUiApi
@Composable
fun update(
    navController: NavHostController,
    bookItemId: String,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    Scaffold(topBar = {
        topBar(
            title = "Update Book",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ) {
            navController.popBackStack()
        }
    }) {

        val bookInfo = produceState<DataOrException<List<MBook>, Boolean, Exception>>(
            initialValue = DataOrException(
                data = emptyList(),
                true,
                Exception("")
            )
        ) {
            value = viewModel.data.value
        }.value

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
        ) {
            Column(
                modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Log.d("INFO", "BookUpdateScreen: ${viewModel.data.value.data.toString()}")
                if (bookInfo.loading == true) {
                    LinearProgressIndicator()
                    bookInfo.loading = false
                } else {
                    androidx.compose.material.Surface(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        shape = CircleShape,
                        elevation = 4.dp,
                    ) {
                        ShowBookUpdate(bookInfo = viewModel.data.value, bookItemId = bookItemId)
                    }
                    ShowSimpleForm(book = viewModel.data.value.data?.first { mBook -> mBook.googleBooksId == bookItemId }!!, navController)
                }
            }
        }

    }
}

@ExperimentalComposeUiApi
@Composable
fun ShowSimpleForm(book: MBook, navController: NavHostController) {

    val context= LocalContext.current
    val notesText= remember {
        mutableStateOf("")
    }

    val isStartedReading = remember {
        mutableStateOf(false)
    }
    val isFinishedReading = remember {
        mutableStateOf(false)
    }
    val ratingVal= remember{
        mutableStateOf(0)
    }
    SimpleForm(defaultValue = if (book.notes.toString().isNotEmpty()) book.notes.toString()
    else "No thoughts available. "){note ->
        notesText.value = note
    }
    
    Row(modifier = Modifier.padding(4.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = { isStartedReading.value = true }, enabled = book.startReading == null) {
          if(book.startReading == null){
              if(!isStartedReading.value) {
                  Text(text = "Start Reading")
              } else {
                  Text(text = "Started Reading", modifier = Modifier.alpha(0.6f), color = Color.Red.copy(alpha = 0.5f))
              }
          } else {
              Text(text = "started on: ${formatDate(book.startReading!!)}")
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        TextButton(onClick = { isFinishedReading.value = true }, enabled = book.finishReading == null) {
            if(book.finishReading == null) {
                if(!isFinishedReading.value) {
                    Text(text = "Mark as Read")
                } else {
                    Text(text = "Finished Reading")
                }
            }else {
                Text(text = "Finished on: ${formatDate(book.finishReading!!)}")
            }

        }
        
    }
    Text(text = "Rating", modifier = Modifier.padding(3.dp))
    book.rating?.toInt().let { RatingBar(rating = it!!){rating ->
        ratingVal.value= rating
    } }

    Spacer(modifier = Modifier.padding(bottom = 15.dp))

    Row() {
        val changedNotes= book.notes != notesText.value
        val changedRating = book.rating?.toInt() != ratingVal.value
        val isFinishedTimeStamp= if(isFinishedReading.value) Timestamp.now() else book.finishReading
        val isStartedTimeStamp= if(isStartedReading.value) Timestamp.now() else book.startReading

        val bookUpdate= changedNotes || changedRating || isStartedReading.value || isFinishedReading.value

        val bookToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimeStamp,
            "started_reading_at" to isStartedTimeStamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value
        ).toMap()

        roundedButton(label = "Update"){
            if(bookUpdate) {
                FirebaseFirestore.getInstance().collection("books").document(book.id!!).update(bookToUpdate).addOnCompleteListener{ task ->
                    Log.d(TAG,"showSimpleForm: ${task.result.toString()}")

                }.addOnFailureListener {
                    showToast(context, "Book Updated Successfully")
                }
            }
            navController.navigate(ReaderScreens.HomeScreen.name)
        }
        Spacer(modifier = Modifier.width(100.dp))

        val openDialog = remember{
            mutableStateOf(false)
        }
        if(openDialog.value) {
            ShowAlertDialog(message = "Are you Sure!" + "\n" + "This Action is not reversible", openDialog = openDialog){
                FirebaseFirestore.getInstance().collection("books").document(book.id!!).delete().addOnCompleteListener {
                    if(it.isSuccessful) {
                        openDialog.value = false
                    }
                }
                navController.navigate(ReaderScreens.HomeScreen.name)
            }
        }
        roundedButton(label = "Delete"){
               openDialog.value= true
        }
    }
    
}

@Composable
fun ShowAlertDialog(message: String, openDialog: MutableState<Boolean>, onYesPressed: () -> Unit) {
    if(openDialog.value) {
        AlertDialog(onDismissRequest = {openDialog.value = false}, title = { Text(text = "Delete Book")}, text = { Text(
            text = message
        )}, buttons = {
            Row(modifier = Modifier.padding(all = 8.dp), horizontalArrangement = Arrangement.Center) {
                TextButton(onClick = { onYesPressed.invoke()})  {
                       Text(text = "Yes")
                }
                TextButton(onClick = { openDialog.value = false}) {
                       Text(text = "No")
                }
                
            }
        })
    }
}


fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}

@ExperimentalComposeUiApi
@Composable
fun SimpleForm(
    modifier: Modifier= Modifier,
    loading: Boolean= false,
    defaultValue: String= "Great Book",
    onSearch: (String) -> Unit
) {
    Column() {
        val textFieldValue= rememberSaveable {
            mutableStateOf(defaultValue) }
        val keyboardController= LocalSoftwareKeyboardController.current
        val valid= remember(textFieldValue.value) {
            textFieldValue.value.trim().isNotEmpty()
        }

        InputSate(
            modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(3.dp)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            valueState = textFieldValue, labelId = "Enter Your Thoughts", enabled = true, onAction = KeyboardActions{
            if(!valid) return@KeyboardActions
            onSearch(textFieldValue.value.trim())
            keyboardController?.hide()
        })


    }

}

@Composable
fun ShowBookUpdate(bookInfo: DataOrException<List<MBook>, Boolean, Exception>, bookItemId: String) {

    Row() {
        Spacer(modifier = Modifier.width(43.dp))
        if (bookInfo.data != null) {
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {
                CardListItem(book = bookInfo.data!!.first { mBook ->
                    mBook.googleBooksId == bookItemId
                }, onPressDetails = {})

            }
        }
    }
}

@Composable
fun CardListItem(book: MBook, onPressDetails: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)
            .clip(
                RoundedCornerShape(20.dp)
            )
            .clickable { }, elevation = 8.dp
    ) {
        Row(horizontalArrangement = Arrangement.Start) {
            Image(
                painter = rememberImagePainter(data = book.photoUrl.toString()),
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
                    .padding(4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 120.dp,
                            topEnd = 20.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
            )

            Column {
                Text(
                    text = book.title.toString(),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = book.authors.toString(), style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 8.dp))
                Text(
                    text = book.publishedDate.toString(),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 8.dp)
                )
            }
        }
    }
}