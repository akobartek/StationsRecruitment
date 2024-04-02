package pl.sokolowskibartlomiej.stations.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import org.koin.compose.KoinContext
import pl.sokolowskibartlomiej.stations.presentation.ui.main.MainScreen
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
                // TODO()
                StationsRecruitmentTheme(
                    darkTheme = true
//                    dynamicColor =
                ) {
                    MainScreen()
                }
            }
        }
    }
}