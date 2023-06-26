package top.topsea.simpleglm.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.json.JSONArray
import top.topsea.simpleglm.R
import top.topsea.simpleglm.api.ApiExecutor
import top.topsea.simpleglm.api.ChatGLMQ
import top.topsea.simpleglm.chat.ConversationUiState
import top.topsea.simpleglm.chat.ChatMessage
import top.topsea.simpleglm.chat.ChatMessages
import top.topsea.simpleglm.chat.UserInput
import top.topsea.simpleglm.chat.exampleUiState
import top.topsea.simpleglm.settings.getCurrTime
import top.topsea.simpleglm.settings.me

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    uiState: ConversationUiState,
    navigateToProfile: (String) -> Unit,
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { MainTopBar(navController) }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
            ChatMessages(
                chatMessages = uiState.chatMessages,
                navigateToProfile = navigateToProfile,
                modifier = Modifier.weight(1f),
                scrollState = scrollState
            )
            UserInput(
                onMessageSent = { content ->
                    exampleUiState.addMessage(
                        ChatMessage(me, mutableStateOf(content), getCurrTime())
                    )
                    val glmq = ChatGLMQ(query = content, JSONArray())
                    Thread{
                        ApiExecutor.streamChat(glmq)
                    }.start()
                },
                resetScroll = {
                    scope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                // let this element handle the padding so that the elevation is shown behind the
                // navigation bar
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    }

}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun MainTopBar(
    navController: NavController
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(color = Color(dynamicLightColorScheme(context).primary.toArgb()))
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterStart),
            fontSize = TextUnit(value = 20F, type = TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 16.dp, top = 4.dp, bottom = 8.dp)
                .size(32.dp)
                .align(Alignment.CenterEnd)
                .clickable {
                    navController.navigate("settings")
                },
            tint = Color.White
        )
    }
}
