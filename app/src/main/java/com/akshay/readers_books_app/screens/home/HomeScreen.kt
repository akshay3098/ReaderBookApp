package com.akshay.readers_books_app.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.akshay.readers_books_app.components.floatButton
import com.akshay.readers_books_app.components.listCard
import com.akshay.readers_books_app.components.titleSection
import com.akshay.readers_books_app.components.topBar
import com.akshay.readers_books_app.model.Item
import com.akshay.readers_books_app.model.MBook
import com.akshay.readers_books_app.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navController: NavController, viewModel: HomeScreenViewModel= hiltViewModel()) {

    Scaffold(topBar = {
        topBar("Reader", navController = navController)
    },
        floatingActionButton = {
            floatButton {
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            homeContent(navController, viewModel = viewModel )
        }

    }

}


@Composable
fun homeContent(navController: NavController, viewModel: HomeScreenViewModel) {

    var listOfBooks= emptyList<MBook>()
    val currentUser= FirebaseAuth.getInstance().currentUser

    if(!viewModel.data.value.data.isNullOrEmpty()) {
        listOfBooks= viewModel.data.value.data!!.toList().filter { mBook ->
            mBook.userId == currentUser?.uid.toString()
        }

    }

    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty()) {
        email.split("@")[0]
    } else {
        "N/A"
    }

    Column(Modifier.padding(2.dp), verticalArrangement = Arrangement.SpaceEvenly) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            titleSection(label = "Your Reading \n " + " activity right now")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
            Column {
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable { navController.navigate(ReaderScreens.StatsScreen.name) }
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant)
                Text(
                    text = currentUserName!!,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Divider()
            }
        }
        ReadingRightNowArea(books = listOfBooks, navController = navController)
        titleSection(label = "Reading List")
        bookListArea(listOfBooks= listOfBooks, navController = navController)
    }
}

@Composable
fun bookListArea(listOfBooks: List<MBook>, navController: NavController) {

    val addBooks= listOfBooks.filter { mBook -> mBook.startReading == null && mBook.finishReading == null }
    horizontalScrollableComponent(addBooks){
        navController.navigate(ReaderScreens.UpdateScreen.name +"/$it")
    }
}

@Composable
fun horizontalScrollableComponent(
    listOfBooks: List<MBook>,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    onCardPressed: (String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .horizontalScroll(scrollState)
    ) {

        if (homeScreenViewModel.data.value.loading == true) {
            LinearProgressIndicator()
        } else {
            if (listOfBooks.isNullOrEmpty()) {
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(
                        text = "No Books Found. Add a Book",
                        style = TextStyle(
                            color = Color.Red.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )

                }
            } else {
                for (book in listOfBooks) {
                    listCard(book) {
                        onCardPressed(book.googleBooksId.toString())
                    }
                }
            }
        }


    }

}


@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {

    val readingNowList= books.filter { mBook -> mBook.startReading != null && mBook.finishReading == null }
    horizontalScrollableComponent(readingNowList){
        navController.navigate(ReaderScreens.UpdateScreen.name + "/$it")
    }

}



