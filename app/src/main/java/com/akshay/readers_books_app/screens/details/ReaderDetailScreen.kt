package com.akshay.readers_books_app.screens.details

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.akshay.readers_books_app.components.topBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.akshay.readers_books_app.components.roundedButton
import com.akshay.readers_books_app.data.Resource
import com.akshay.readers_books_app.model.Item
import com.akshay.readers_books_app.model.MBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun details(navController: NavController, bookId: String, viewModel: ReaderDetailsViewModel = hiltViewModel()) {

    Scaffold(topBar = {
        topBar(
            title = "Book Details",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        )
    }) {
        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bookInfo= produceState<Resource<Item>>(initialValue = Resource.Loading()){
                    value = viewModel.getBookInfo(bookId)
                }.value
                if(bookInfo.data == null) {
                    Row() {
                        LinearProgressIndicator()
                        Text(text = "Loading..")
                    }
                }else {
                    showBookDetailsInfo(navController,bookInfo)
                }
            }
        }
    }
}

@Composable
fun showBookDetailsInfo(navController: NavController, bookInfo: Resource<Item>) {
    val bookData= bookInfo.data?.volumeInfo
    val googleBookId= bookInfo.data?.id
    
    Card(modifier = Modifier.padding(34.dp), shape = CircleShape, elevation = 4.dp) {
        
        Image(painter = rememberImagePainter(data = bookData!!.imageLinks.thumbnail), contentDescription = "Book Image", modifier = Modifier
            .width(90.dp)
            .height(90.dp)
            .padding(1.dp))
        
    }
    
    Text(text = bookData?.title.toString(), style = MaterialTheme.typography.h6, overflow = TextOverflow.Ellipsis, maxLines = 19)
    Text(text = "Author: ${bookData?.authors.toString()}")
    Text(text = "Page Count: ${bookData?.pageCount.toString()}")
    Text(text = "Category: ${bookData?.categories.toString()}", style = MaterialTheme.typography.subtitle1, overflow = TextOverflow.Ellipsis, maxLines = 3, modifier = Modifier.padding(start = 2.dp, end = 2.dp))
    Text(text = "Published: ${bookData?.publishedDate.toString()}", style = MaterialTheme.typography.subtitle1)
    
    Spacer(modifier = Modifier.height(5.dp))

    val cleanDescription= HtmlCompat.fromHtml(bookData!!.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    val localDims= LocalContext.current.resources.displayMetrics

    Surface(modifier = Modifier
        .height(localDims.heightPixels.dp.times(0.09f))
        .padding(4.dp), shape = RectangleShape, border = BorderStroke(1.dp, Color.DarkGray)) {
        LazyColumn(modifier = Modifier.padding(3.dp)){ item{
            Text(text = cleanDescription)
        }
        }
    }

    Row(modifier = Modifier.padding(6.dp), horizontalArrangement = Arrangement.SpaceAround) {
        roundedButton(label = "Save"){
            val book= MBook(
                title = bookData.title,
                authors = bookData.authors.toString(),
                description = bookData.description,
                category = bookData.categories.toString(),
                notes = "",
                photoUrl = bookData.imageLinks.thumbnail,
                publishedDate = bookData.publishedDate,
                pageCount = bookData.pageCount.toString(),
                rating = 0.0,
                googleBooksId = googleBookId,
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

            )
            savetoFireBase(book, navController = navController)
        }

        Spacer(modifier = Modifier.width(25.dp))

        roundedButton(label = "Cancel"){
            navController.popBackStack()
        }
    }
}

fun savetoFireBase(book: MBook, navController: NavController) {

    val db= FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if(book.toString().isNotEmpty()){
        dbCollection.add(book).addOnSuccessListener { documentRef ->
            val docId= documentRef.id
            dbCollection.document(docId).update(hashMapOf("id" to docId) as Map<String, Any>)
                .addOnCompleteListener { task -> if(task.isSuccessful) {
                    navController.popBackStack()
                }
                }.addOnFailureListener {
                    Log.w("Error", "Error updating doc", it)
                }
        }
    }else {

    }


}
