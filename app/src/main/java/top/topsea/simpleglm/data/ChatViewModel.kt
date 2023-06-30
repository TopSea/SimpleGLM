package top.topsea.simpleglm.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(context: Context): ViewModel() {
    val database = ChatDatabase.getInstance(context = context)!!

    val chatMessageDao by lazy {
        database.chatMessageDao()
    }

    fun insertChatMessage(chatMessage: ChatMessage) = chatMessageDao.insert(chatMessage)

    fun getAllMessage() : Flow<List<ChatMessage>> = chatMessageDao.getAll()

    fun getLatest10() : List<ChatMessage> = chatMessageDao.getLatest10()

    fun get10History(before: Int) : List<ChatMessage> = chatMessageDao.get10History(before)



    //使用MutableStateFlow创建展示的状态值，布尔类型，
    private var _refresh=MutableStateFlow(false)
    //view层不能对它直接操作我们要通过另一个变量暴露出去让view层只能读取
    val refresh =_refresh
    //通过调用startRefresh()来进行刷新的状态值的改变
    fun startRefresh(){
        //刷新是异步操作所以应该在协程中
        //使用 viewModelScope.launch开启协程
        viewModelScope.launch {
            _refresh.value=true
            //两秒后把状态改为false关闭刷新
            delay(1000)
            _refresh.value=false
        }
    }
}