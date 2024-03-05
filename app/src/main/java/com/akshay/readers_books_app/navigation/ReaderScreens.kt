package com.akshay.readers_books_app.navigation

enum class ReaderScreens {
    SplashScreen,
    UpdateScreen,
    HomeScreen,
    ReaderDetailsScreen,
    LoginScreen,
    StatsScreen,
    AccountCreateScreen,
    SearchScreen;

    companion object{
        fun fromRoute(route: String?): ReaderScreens=
            when(route?.substringBefore("/")) {

                SplashScreen.name -> SplashScreen
                UpdateScreen.name -> UpdateScreen
                HomeScreen.name -> HomeScreen
                ReaderDetailsScreen.name -> ReaderDetailsScreen
                LoginScreen.name -> LoginScreen
                StatsScreen.name -> StatsScreen
                AccountCreateScreen.name -> AccountCreateScreen
                SearchScreen.name -> SearchScreen
                null -> HomeScreen
                else -> {
                    throw IllegalArgumentException("Route $route is not recognized")
                }
            }
    }

}