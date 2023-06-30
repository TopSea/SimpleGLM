package top.topsea.simpleglm.chat

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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

    //使用MutableStateFlow创建展示的状态值，布尔类型，
    private var _refresh= MutableStateFlow(false)
    //view层不能对它直接操作我们要通过另一个变量暴露出去让view层只能读取
    val refresh =_refresh

    fun addMessage(msg: ChatMessage) {
        Log.d(TAG, "addMessage: ${msg}")
        _Chat_messages.add(0, msg) // 因为是倒序，所以往前加
        viewModel.insertChatMessage(msg)    // 我的消息直接入库
    }

    fun addMessageGLM(msg: ChatMessage, finished: Boolean) {
        Log.d(TAG, "addMessage: ${msg}")
        // ChatGLM-6B 回复之后，要加两条信息：一条没完成的用来刷新界面，一条完成了的用来存到数据库
        if (finished) viewModel.insertChatMessage(msg) else _Chat_messages.add(0, msg) // 因为是倒序，所以往前加
    }

    fun refreshHistory() {
        _refresh.value=true
        val finalIndex = _Chat_messages[_Chat_messages.lastIndex].index!!
        val historyMessages = viewModel.get10History(finalIndex)
        _Chat_messages.addAll(historyMessages)
        _refresh.value=false
    }
}

//@Immutable
//data class ChatMessage(
//    val author: String,
//    val content: MutableState<String>,
//    val timestamp: String,
//    val authorImage: Int = if (author == me) R.drawable.baseline_perm else R.drawable.baseline_android
//)
