package com.akshay.readers_books_app.components

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.akshay.readers_books_app.R
import com.akshay.readers_books_app.model.MBook
import com.akshay.readers_books_app.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReaderLogo() {
    Text(text = "Reader App", modifier = Modifier.padding(bottom= 16.dp), fontSize = 40.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun emailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    onAction: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Next
) {
    InputSate(modifier= modifier, valueState = emailState, labelId = labelId, enabled = enabled, keyboardType = KeyboardType.Email, imeAction = imeAction, onAction = onAction)

}

@Composable
fun InputSate(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(value = valueState.value,
        onValueChange = { valueState.value = it },
        label= { Text(text = labelId)},
        singleLine = isSingleLine,
        enabled = enabled,
        textStyle = TextStyle(fontSize = 18.sp,color= MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction
    )

}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTranformation =
        if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(value = passwordState.value, onValueChange = { passwordState.value = it },
        label = { Text(text = labelId) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTranformation,
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility) },
        keyboardActions = onAction
    )

}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icons.Default.Close

    }

}


@Composable
fun titleSection(modifier: Modifier = Modifier, label: String){
    Surface(modifier = Modifier.padding(start = 5.dp, top = 1.dp))
    {
        Column() {
            Text(text = label, fontSize = 19.sp, fontStyle = FontStyle.Normal, textAlign = TextAlign.Left)
        }
    }
}


@Composable
fun topBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    onBackPressed: () -> Unit = {}
) {

    TopAppBar(title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showProfile) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Icon",
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .scale(0.6f)
                )
            }
            if(icon!=null) {
                Icon(imageVector = icon, contentDescription = "arrow back", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.clickable { onBackPressed.invoke() })
            }
            Text(text = title, color = Color.Blue, modifier = Modifier.padding(start = 20.dp))
            Spacer(modifier = Modifier.width(120.dp))

        }

    }, actions = {
        IconButton(onClick = {
            FirebaseAuth.getInstance().signOut().run {
                navController.navigate(
                    ReaderScreens.LoginScreen.name
                )
            }
        }) {
            if(showProfile){
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Logout",
                    tint = Color.Green.copy(alpha = 0.4f)
                )
            }else {
                Box(){}
            }

        }
    }, backgroundColor = Color.Transparent, elevation = 0.dp)
}


@Composable
fun floatButton(myFloat: () -> Unit) {

    FloatingActionButton(
        onClick = { myFloat() },
        shape = RoundedCornerShape(50.dp),
        backgroundColor = Color(0xFF92CBDF)
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add a Book", tint = Color.White)

    }

}

@Composable
fun BooksRating(score: Double = 4.5) {
    Surface(
        modifier = Modifier
            .height(70.dp)
            .padding(4.dp), shape = RoundedCornerShape(56.dp), elevation = 6.dp, color = Color.White
    ) {

        Column(modifier = Modifier.padding(4.dp)) {
            Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = "Start",
                modifier = Modifier.padding(3.dp)
            )
            Text(text = score.toString(), style = MaterialTheme.typography.subtitle1)

        }

    }

}


@ExperimentalComposeUiApi
@Composable
fun RatingBar(modifier: Modifier= Modifier, rating: Int, onPresses: (Int) -> Unit){

  var selectSate by remember {
      mutableStateOf(false)
  }
    var ratingState by remember {
        mutableStateOf(rating)
    }

    val size by animateDpAsState(
        targetValue = if (selectSate) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )
    
    Row(modifier= Modifier
        .width(200.dp)
        .padding(2.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        for(i in 1..5){
            Icon(painter = painterResource(id = R.drawable.ic_baseline_star_24), contentDescription = "Rating", modifier= Modifier
                .width(size)
                .height(size)
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            selectSate = true
                            onPresses(i)
                            ratingState = i
                        }
                        MotionEvent.ACTION_UP -> {
                            selectSate = false
                        }

                    }
                    true

                }, tint = if(i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1))

        }

    }

}

@Composable
fun listCard(
    book: MBook,
    onPressDetails: (String) -> Unit = {}
) {

    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics

    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp

    Card(
        shape = RoundedCornerShape(29.dp),
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(242.dp)
            .clickable { onPressDetails.invoke(book.title.toString()) }) {

        Column(
            modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start
        ) {
            Row(horizontalArrangement = Arrangement.Center) {

                Image(
                    painter = rememberImagePainter(data = book.photoUrl.toString()),
                    contentDescription = "Book Image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.width(50.dp))

                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Fav Icon",
                        modifier = Modifier.padding(bottom = 1.dp)
                    )

                    BooksRating(score = book.rating!!)

                }
            }

            Text(
                text = book.title.toString(),
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = book.authors.toString(),
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.caption
            )

            val isStartedReading = remember {
                mutableStateOf(false)
            }

            Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {
                isStartedReading.value = book.startReading != null

                roundedButton(label = if(isStartedReading.value) "Reading" else "Not Yet", radius = 70)
            }
        }
    }
}

@Composable
fun roundedButton(
    label: String,
    radius: Int= 29,
    onPress: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.clip(
            RoundedCornerShape(
                bottomEndPercent = radius,
                topStartPercent = radius

            )
        ), color = Color(0xFF92CBDF)
    ) {

        Column(modifier = Modifier
            .width(90.dp)
            .height(40.dp)
            .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = TextStyle(color = Color.White, fontSize = 15.sp))
        }

    }

}


