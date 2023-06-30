package top.topsea.simpleglm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private var instance: ChatDatabase? = null

@Database(entities = [ChatMessage::class], version = 1)
@TypeConverters(ContentConverter::class, DateConverter::class)
abstract class ChatDatabase: RoomDatabase() {

    companion object {
        fun getInstance(context: Context): ChatDatabase? {
            if (instance == null) {
                synchronized(ChatDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            ChatDatabase::class.java, "ChatDatabase"
                        ).allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return instance
        }
    }

    abstract fun chatMessageDao(): ChatMessageDao
}