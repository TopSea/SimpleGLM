package top.topsea.simpleglm.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.TypeConverter
import java.sql.Date


class DateConverter {
    @TypeConverter
    fun convertDate(value: Date): Long {
        return value.time
    }

    @TypeConverter
    fun revertDate(value: Long): Date {
        return Date(value)
    }
}

class ContentConverter {
    @TypeConverter
    fun convertContent(content: MutableState<String>): String {
        return content.value
    }

    @TypeConverter
    fun revertDate(value: String): MutableState<String> {
        return mutableStateOf(value)
    }
}