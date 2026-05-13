package com.naufalsulthanfakhry0092.asesmenmobpro1.navigation

const val KEY_ID_TAGIHAN = "idTagihan"

sealed class Screen(val route: String) {
    data object Home : Screen("mainScreen")

    data object FormBaru : Screen("countScreen")
    data object RecycleBin : Screen("recycleBinScreen")

    data object FormUbah : Screen("countScreen/{$KEY_ID_TAGIHAN}") {
        fun withId(id: Long) = "countScreen/$id"
    }
}