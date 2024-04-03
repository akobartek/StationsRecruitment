package pl.sokolowskibartlomiej.stations.presentation

import androidx.navigation.NavHostController

object Destinations {
    const val MAIN = "main"
    const val SETTINGS = "settings"
}

class Actions(navController: NavHostController) {
    val openSettings: () -> Unit = {
        navController.navigate(Destinations.SETTINGS)
    }
    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }
}