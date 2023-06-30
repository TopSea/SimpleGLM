package top.topsea.simpleglm

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.forEach
import top.topsea.simpleglm.chat.ConversationUiState
import top.topsea.simpleglm.data.ChatMessage
import top.topsea.simpleglm.data.ChatViewModel
import top.topsea.simpleglm.settings.getFormatByDate
import top.topsea.simpleglm.ui.screen.MainScreen
import top.topsea.simpleglm.ui.screen.SettingScreen
import top.topsea.simpleglm.ui.theme.SimpleGLMTheme
import java.sql.Date

var messageState: ConversationUiState? = null
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            SimpleGLMTheme {
                // A surface container using the 'background' color from the theme

                val context = LocalContext.current
                val viewModel = ChatViewModel(context)

                val latestMessages = viewModel.getLatest10()
                messageState = ConversationUiState(
                    initialChatMessages = latestMessages,
                    context = context
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "main_screen") {
                        composable("main_screen") {
                            MainScreen(
                                uiState = messageState!!,
                                navigateToProfile = { },
                                navController = navController,
                            )
                        }
                        composable("settings") { SettingScreen(navController = navController) }
                    }
//                    DatabaseText()
                }
            }
        }
    }
}

@Composable
fun DatabaseText() {
    val context = LocalContext.current

    val viewmodel = ChatViewModel(context)

    val content = remember { mutableStateOf("${System.currentTimeMillis()}") }

    val message = ChatMessage(null, content, Date(System.currentTimeMillis()), "")

    viewmodel.insertChatMessage(message)

    val messages = viewmodel.getAllMessage().collectAsState(initial = listOf())

    Button(onClick = {
        viewmodel.insertChatMessage(message)
        val messageZero = messages.value[0]
        Log.d(TAG, "DatabaseText: ${messages.value[0]}")
        Log.d(TAG, "DatabaseText: ${getFormatByDate(messageZero.date)}")
    }) {
        Text(text = "123")
    }
}
