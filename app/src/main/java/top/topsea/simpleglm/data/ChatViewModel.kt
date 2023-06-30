package top.topsea.simpleglm.data

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ChatViewModel(context: Context): ViewModel() {
    val database = ChatDatabase.getInstance(context = context)!!

    val chatMessageDao by lazy {
        database.chatMessageDao()
    }

    fun insertChatMessage(chatMessage: ChatMessage) = chatMessageDao.insert(chatMessage)

    fun getAllMessage() : Flow<List<ChatMessage>> = chatMessageDao.getAll()

    fun getLatest10() : List<ChatMessage> = chatMessageDao.getLatest10()
}