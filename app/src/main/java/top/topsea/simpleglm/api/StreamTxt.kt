package top.topsea.simpleglm.api

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.tencent.mmkv.MMKV
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import top.topsea.simpleglm.data.ChatMessage
import top.topsea.simpleglm.messageState
import top.topsea.simpleglm.settings.droid
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.sql.Date


abstract class StreamTxtResponseConverter<T> : Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T {
//        Log.d(TAG, "convert: ${value.string()}")
        return parseStreamTxt(value.byteStream())
    }

    abstract fun parseStreamTxt(inputStream: InputStream): T
}

abstract class StreamTxtConverterFactory<T> : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return responseConverter()
    }

    abstract fun responseConverter(): StreamTxtResponseConverter<T>
}

class StreamResponseConverter : StreamTxtResponseConverter<ChatGLMA>() {
    override fun parseStreamTxt(inputStream: InputStream): ChatGLMA {
        var glma = ChatGLMA(null, null, null, null, false)
        stream2String(inputStream)
        return glma
    }

    private fun stream2String(inputStream: InputStream) {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val enabledHistory = MMKV.defaultMMKV().decodeBool("Chat_History", true)
//        val glma = ChatGLMA(null, null, null, null, false)
        val chatA = ChatMessage(null, mutableStateOf(""), Date(System.currentTimeMillis()), droid)
        messageState!!.addMessageGLM(chatA, false)
        try {
            reader.forEachLine { line ->
                if (line.contains("data: {")){
                    val data = line.removePrefix("data: ")

                    val jsonData = JSONObject(data)
//                    glma.query = jsonData.getString("query")
//                    glma.delta = jsonData.getString("delta")
//                    glma.response = jsonData.getString("response")
//                    glma.finished = jsonData.getBoolean("finished")
                    if (!jsonData.getBoolean("finished")) {
                        if (jsonData.getString("delta").isEmpty()) {
                            // 防止 Emoji 变成乱码
                            chatA.content.value = jsonData.getString("response")
                        } else {
                            chatA.content.value = chatA.content.value + jsonData.getString("delta")
                        }
                    } else {
                        if (enabledHistory) {
                            history = jsonData.getJSONArray("history")
                        }
                        messageState!!.addMessageGLM(chatA, true)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}