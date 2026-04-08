package com.naufalsulthanfakhry0092.asesmenmobpro1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen.CountScreen
import com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen.MainScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    var billName by rememberSaveable { mutableStateOf("") }
    var amountText by rememberSaveable { mutableStateOf("") }
    var peopleText by rememberSaveable { mutableStateOf("") }
    var useTax by rememberSaveable { mutableStateOf(false) }
    var taxPercentText by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("") }
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }

        composable(route = Screen.Count.route) {
            CountScreen(
                navController = navController,
                billName = billName,
                onBillNameChange = { billName = it },
                amountText = amountText,
                onAmountTextChange = { amountText = it },
                peopleText = peopleText,
                onPeopleTextChange = { peopleText = it },
                useTax = useTax,
                onUseTaxChange = { useTax = it },
                taxPercentText = taxPercentText,
                onTaxPercentTextChange = { taxPercentText = it },
                result = result,
                onResultChange = { result = it }
            )
        }


    }
}