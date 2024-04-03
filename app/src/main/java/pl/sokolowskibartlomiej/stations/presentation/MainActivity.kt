package pl.sokolowskibartlomiej.stations.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext
import pl.sokolowskibartlomiej.stations.domain.repository.UserPreferences
import pl.sokolowskibartlomiej.stations.presentation.ui.main.MainScreen
import pl.sokolowskibartlomiej.stations.presentation.ui.settings.SettingsScreen
import pl.sokolowskibartlomiej.stations.presentation.ui.settings.SettingsViewModel
import pl.sokolowskibartlomiej.stations.presentation.ui.theme.StationsRecruitmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )
        setContent {
            KoinContext {
                val navController = rememberNavController()
                val actions = remember(navController) { Actions(navController) }
                val settingsViewModel = koinViewModel<SettingsViewModel>()
                val preferences by
                settingsViewModel.preferencesFlow.collectAsStateWithLifecycle(initialValue = UserPreferences())
                StationsRecruitmentTheme(
                    colorTheme = preferences.colorTheme,
                    dynamicColor = preferences.dynamicColors
                ) {
                    NavHost(navController = navController, startDestination = Destinations.MAIN) {
                        composable(Destinations.MAIN) {
                            MainScreen(
                                openSettings = actions.openSettings,
                                finish = { this@MainActivity.finish() }
                            )
                        }
                        composable(Destinations.SETTINGS) {
                            SettingsScreen(
                                viewModel = settingsViewModel,
                                navigateBack = actions.navigateBack
                            )
                        }
                    }
                }
            }
        }
    }
}