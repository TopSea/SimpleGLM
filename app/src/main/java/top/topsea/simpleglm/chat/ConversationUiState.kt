package top.topsea.simpleglm.chat

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.toMutableStateList
import top.topsea.simpleglm.R
import top.topsea.simpleglm.settings.me

class ConversationUiState(
    initialChatMessages: List<ChatMessage>
) {
    private val _Chat_messages: MutableList<ChatMessage> = initialChatMessages.toMutableStateList()
    val chatMessages: List<ChatMessage> = _Chat_messages

    fun addMessage(msg: ChatMessage) {
        _Chat_messages.add(0, msg) // Add to the beginning of the list
    }
}

@Immutable
data class ChatMessage(
    val author: String,
    val content: MutableState<String>,
    val timestamp: String,
    val authorImage: Int = if (author == me) R.drawable.baseline_perm else R.drawable.baseline_android
)
