package pl.sokolowskibartlomiej.stations.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import org.koin.compose.KoinContext
import pl.sokolowskibartlomiej.stations.presentation.ui.theme.StationsRecruitmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinContext {
                StationsRecruitmentTheme(
//                    darkTheme =
//                    dynamicColor =
                ) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        Text(text = "STATIONS")
                    }
                }
            }
        }
    }
}