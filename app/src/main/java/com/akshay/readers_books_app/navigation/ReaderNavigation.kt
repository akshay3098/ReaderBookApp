package com.akshay.readers_books_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.akshay.readers_books_app.screens.details.details
import com.akshay.readers_books_app.screens.home.Home
import com.akshay.readers_books_app.screens.home.HomeScreenViewModel
import com.akshay.readers_books_app.screens.login.loginScreen
import com.akshay.readers_books_app.screens.search.BooksSearchViewModel
import com.akshay.readers_books_app.screens.search.search
import com.akshay.readers_books_app.screens.splashScreen
import com.akshay.readers_books_app.screens.stats.stats
import com.akshay.readers_books_app.screens.update.update


@ExperimentalComposeUiApi
@Composable
fun ReaderNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {
        composable(ReaderScreens.SplashScreen.name) {
            splashScreen(navController = navController)
        }
        composable(ReaderScreens.SearchScreen.name) {
            val viewModel = hiltViewModel<BooksSearchViewModel>()
            search(navController = navController, viewModel)
        }
        composable(ReaderScreens.LoginScreen.name) {
            loginScreen(navController = navController)
        }

        composable(ReaderScreens.StatsScreen.name){
            val viewModel= hiltViewModel<HomeScreenViewModel>()
            stats(navController = navController, viewModel= viewModel)
        }


        composable(ReaderScreens.HomeScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            Home(navController = navController, viewModel = homeViewModel)
        }
        val detailsName = ReaderScreens.ReaderDetailsScreen.name
        composable("$detailsName/{bookId}", arguments = listOf(navArgument("bookId") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                details(navController = navController, bookId = it.toString())
            }
        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}", arguments = listOf(navArgument("bookItemId") {
            type = NavType.StringType
        })) { navBackStack ->
            navBackStack.arguments?.getString("bookItemId").let {
                update(navController = navController, bookItemId = it.toString())
            }
        }
    }


}