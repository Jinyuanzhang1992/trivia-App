package social.bondoo.triviaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import social.bondoo.triviaapp.ui.theme.TriviaAppTheme

import social.bondoo.triviaapp.screens.TriviaHomeScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp {
                TriviaHomeScreen()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit = { }) {
    TriviaAppTheme {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApp {
        TriviaHomeScreen()
    }
}