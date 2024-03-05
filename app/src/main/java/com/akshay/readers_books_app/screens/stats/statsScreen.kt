package com.akshay.readers_books_app.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.akshay.readers_books_app.components.topBar
import com.akshay.readers_books_app.model.MBook
import com.akshay.readers_books_app.screens.home.HomeScreenViewModel
import com.akshay.readers_books_app.utlis.formatDate
import com.google.firebase.auth.FirebaseAuth
import java.util.*

@Composable
fun stats(navController: NavController, viewModel: HomeScreenViewModel) {

    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser


    Scaffold(topBar = {
        topBar(
            title = "Books Stats",
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false,
        ) {
            navController.popBackStack()
        }
    }) {
        androidx.compose.material.Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 3.dp, start = 3.dp, end = 3.dp)
        ) {
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook -> mBook.userId == currentUser?.uid }
            } else {
                emptyList()
            }

            Column {
                Row() {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .padding(2.dp)
                    ) {
                        Icon(imageVector = Icons.Sharp.Person, contentDescription = "Icon")

                    }

                    Text(
                        text = "Hi, ${
                            currentUser?.email.toString()
                                .split("@")[0].uppercase(Locale.getDefault())
                        }"
                    )
                }
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(), shape = CircleShape, elevation = 5.dp
                ) {
                    val readBooksList: List<MBook> =
                        if (!viewModel.data.value.data.isNullOrEmpty()) {
                            books.filter { mBook ->
                                (mBook.userId == currentUser?.uid) && (mBook.finishReading != null)
                            }
                        } else {
                            emptyList()
                        }

                    val readingBooks: List<MBook> =
                        books.filter { mBook -> (mBook.startReading != null) && (mBook.finishReading == null) }
                    Column(
                        modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                        horizontalAlignment = Alignment.Start
                    ) {

                        Text(text = "Your Stats", style = MaterialTheme.typography.h5)
                        Divider()
                        Text(text = "You're reading: ${readingBooks.size} books")
                        Text(text = "You've read: ${readBooksList.size} books")

                    }
                }

                if (viewModel.data.value.loading == true) {
                    LinearProgressIndicator()
                } else {
                    Divider()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(), contentPadding = PaddingValues(16.dp)
                    ) {
                        val readBooks: List<MBook> =
                            if (!viewModel.data.value.data.isNullOrEmpty()) {
                                viewModel.data.value.data!!.filter { mBook -> (mBook.userId == currentUser?.uid) && (mBook.finishReading != null) }
                            } else {
                                emptyList()
                            }

                        items(items = readBooks) { book ->
                            BookRowStats(book = book)

                        }

                    }
                }
            }

        }

    }

}

@Composable
fun BookRowStats(book: MBook) {

    Card(
        modifier = Modifier
            .clickable { }
            .fillMaxSize()
            .height(100.dp)
            .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp
    ) {
        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {
            val imgUrl =
                if (book.photoUrl.toString()
                        .isEmpty()
                ) "https://images.unsplash.com/photo-1592496431122-2349e0fbc666?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Ym9vayUyMGNvdmVyfGVufDB8fDB8fHww"
                else {
                    book.photoUrl.toString()
                }
            Image(
                painter = rememberImagePainter(data = imgUrl),
                contentDescription = "book image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )

            Column() {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
                    if(book.rating!! >= 4) {
                        Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                        Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "Thumbs Up", tint = Color.Green.copy(alpha = 0.5f))
                    } else {
                        Box() {
                            
                        }
                    }

                }
                Text(
                    text = "Author: ${book.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )


                Text(
                    text = "Started: ${formatDate(book.startReading!!)}",
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )


                Text(
                    text = "Finished ${formatDate(book.finishReading!!)}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

