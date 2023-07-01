package top.topsea.simpleglm.api

import android.util.Log
import com.tencent.mmkv.MMKV
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import top.topsea.simpleglm.TAG

var history = JSONArray()
object ApiExecutor {
    private val kv = MMKV.defaultMMKV()
    private val serverIP = kv.decodeString("Server_IP", "http:192.168.0.107:8888")!!

    @JvmStatic
    fun streamChat(glmq: ChatGLMQ) {
        val retrofit = Retrofit.Builder() //设置网络请求BaseUrl地址
            .baseUrl(serverIP) //设置数据解析器
            .addConverterFactory(object : StreamTxtConverterFactory<ChatGLMA>() {
                override fun responseConverter(): StreamTxtResponseConverter<ChatGLMA> {
                    return StreamResponseConverter()
                }
            })
            .build()

        if (kv.decodeBool("Chat_History", true)) {
            glmq.history = history
        }

        val request: ChatdroidApi = retrofit.create(ChatdroidApi::class.java)

        val json = "application/json; charset=utf-8".toMediaType()
        val requestBody = glmq.toString().toRequestBody(json)

        val call: Call<ChatGLMA> = request.streamChat(requestBody)

        call.enqueue(object : Callback<ChatGLMA> {
            override fun onResponse(call: Call<ChatGLMA>, response: Response<ChatGLMA>) {

//                val buffer = Buffer()
//                call.request().body!!.writeTo(buffer)
//                Log.d(TAG, "request body: ${buffer.readUtf8()}")

//                exampleUiState.addMessage(
//                    Message(droid, txt, getCurrTime())
//                )
            }

            override fun onFailure(call: Call<ChatGLMA>, t: Throwable) {
                Log.d(TAG, "onFailure: $call, $t")
            }
        })
    }
}


