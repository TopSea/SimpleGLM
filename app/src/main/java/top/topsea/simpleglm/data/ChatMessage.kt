package top.topsea.simpleglm.data

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Immutable
@TypeConverters(ContentConverter::class, DateConverter::class)
@Entity
data class ChatMessage (
    @PrimaryKey(autoGenerate = true)
    val index: Int?,
    @ColumnInfo(name = "content")
    val content: MutableState<String>,
    @ColumnInfo(name = "date")
    val date: Date,
    @ColumnInfo(name = "author")
    val author: String
)

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM ChatMessage")
    fun getAll(): Flow<List<ChatMessage>>

    // 查询最近十条信息
    @Query("SELECT * FROM ChatMessage order by `index` desc limit 10")
    fun getLatest10(): List<ChatMessage>

    @Query("SELECT * FROM ChatMessage WHERE `index` < (:before)  order by `index` desc limit 10")
    fun get10History(before: Int): List<ChatMessage>

    @Insert
    fun insertAll(vararg chatMessages: ChatMessage)
    @Insert
    fun insert(chatMessage: ChatMessage)
    @Delete
    fun delete(chatMessages: ChatMessage)

}