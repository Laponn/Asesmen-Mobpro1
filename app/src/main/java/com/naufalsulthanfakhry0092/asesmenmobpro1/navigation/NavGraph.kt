package com.naufalsulthanfakhry0092.asesmenmobpro1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen.CountScreen
import com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen.MainScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }

        composable(route = Screen.Count.route) {
            CountScreen(navController = navController)
        }
    }
}