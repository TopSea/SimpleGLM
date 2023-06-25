package top.topsea.simpleglm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import top.topsea.simpleglm.chat.exampleUiState
import top.topsea.simpleglm.ui.screen.MainScreen
import top.topsea.simpleglm.ui.screen.SettingScreen
import top.topsea.simpleglm.ui.theme.SimpleGLMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            SimpleGLMTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "main_screen") {
                        composable("main_screen") {
                            MainScreen(
                                uiState = exampleUiState,
                                navigateToProfile = { },
                                navController = navController,
                            )
                        }
                        composable("settings") { SettingScreen(navController = navController) }
                    }

                }
            }
        }
    }
}
