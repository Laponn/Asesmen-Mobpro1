package com.naufalsulthanfakhry0092.asesmenmobpro1.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object Count: Screen("countScreen")
}