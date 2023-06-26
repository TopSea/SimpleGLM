package top.topsea.simpleglm.api

import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Streaming

interface ChatdroidApi {
    @Streaming
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/stream")
    fun streamChat(@Body json: RequestBody): Call<ChatGLMA>

    @Streaming
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/chat")
    fun normalChat(result: RequestBody): Call<Any>

}

data class ChatGLMQ(
    var query: String,
    var history: JSONArray?
) {
    override fun toString(): String {
        return "{ \"query\": \"$query\", " +
                "\"history\": $history }"
    }
}

data class ChatGLMA(
    var query: String?,
    var delta: String?,
    var response: String?,
    var history: JSONArray?,
    var finished: Boolean
)

data class XmlResponse(
    var event: String,
    var data: JSONObject
)
