package top.topsea.simpleglm.api

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import top.topsea.simpleglm.TAG
import top.topsea.simpleglm.chat.ChatMessage
import top.topsea.simpleglm.chat.exampleUiState
import top.topsea.simpleglm.settings.droid
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type


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

    private fun stream2String(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuffer = StringBuffer()
//        val glma = ChatGLMA(null, null, null, null, false)
        val chatA = ChatMessage(droid, mutableStateOf(""), "")
        exampleUiState.addMessage(chatA)
        try {
            reader.forEachLine { line ->
                Log.d(TAG, "line: $line")
                if (line.contains("data: {")){
                    val data = line.removePrefix("data: ")
                    val jsonData = JSONObject(data)
//                    glma.query = jsonData.getString("query")
//                    glma.delta = jsonData.getString("delta")
//                    glma.response = jsonData.getString("response")
//                    glma.finished = jsonData.getBoolean("finished")
                    if (!jsonData.getBoolean("finished")) {
                        chatA.content.value = chatA.content.value + jsonData.getString("delta")
                    }
                    Log.d(TAG, "stream2String: ${jsonData.toString()}")
                }
                stringBuffer.append(line)
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
        return stringBuffer.toString()
    }
}