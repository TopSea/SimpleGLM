package top.topsea.simpleglm.chat

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import top.topsea.simpleglm.R
import top.topsea.simpleglm.TAG
import top.topsea.simpleglm.data.ChatMessage
import top.topsea.simpleglm.data.ChatViewModel
import top.topsea.simpleglm.settings.me

class ConversationUiState(
    initialChatMessages: List<ChatMessage>,
    context: Context
) {
    private val _Chat_messages: MutableList<ChatMessage> = initialChatMessages.toMutableStateList()
    val chatMessages: List<ChatMessage> = _Chat_messages
    val viewModel = ChatViewModel(context)

    fun addMessage(msg: ChatMessage) {
        Log.d(TAG, "addMessage: ${msg}")
        _Chat_messages.add(0, msg) // 因为是倒序，所以往前加
    }

    fun addMessageGLM(msg: ChatMessage, finished: Boolean) {
        Log.d(TAG, "addMessage: ${msg}")
        // ChatGLM-6B 回复之后，要加两条信息：一条没完成的用来刷新界面，一条完成了的用来存到数据库
        if (finished) viewModel.insertChatMessage(msg) else _Chat_messages.add(0, msg) // 因为是倒序，所以往前加
    }

}

//@Immutable
//data class ChatMessage(
//    val author: String,
//    val content: MutableState<String>,
//    val timestamp: String,
//    val authorImage: Int = if (author == me) R.drawable.baseline_perm else R.drawable.baseline_android
//)
