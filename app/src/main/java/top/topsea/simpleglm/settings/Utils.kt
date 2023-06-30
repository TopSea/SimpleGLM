package top.topsea.simpleglm.settings

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit


const val time_format = "yyyy-MM-dd HH:mm:ss"
const val today_format = "HH:mm"
const val before_format = "yyyy-MM-dd"

const val me = "me"
const val droid = "ChatGLM-6B"

@SuppressLint("SimpleDateFormat")
fun getCurrTime(): String {
    val time = System.currentTimeMillis()
    val format = SimpleDateFormat(time_format)
    val d1 = Date(time)
    return format.format(d1)
}

@SuppressLint("SimpleDateFormat")
fun getFormatByDate(date: java.sql.Date): String {
    val todayFormat = SimpleDateFormat(today_format)
    val beforeFormat = SimpleDateFormat(before_format)

    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.get(Calendar.YEAR)


    val curr = System.currentTimeMillis()
    val dNow = Date(curr)
    val calendarNow = Calendar.getInstance()
    calendarNow.time = dNow

    return if (calendarNow.get(Calendar.YEAR) < calendar.get(Calendar.YEAR)) {              // 间隔大于一年直接用日期
        beforeFormat.format(date)
    } else {              // 间隔小于一年大于一天直接用日期
        if (calendarNow.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)) {
            beforeFormat.format(date)
        } else {
            // 还要把下午的小时数减掉 12，嫌麻烦先将就着吧
//            if (calendarNow.get(Calendar.AM_PM) == Calendar.AM) {             // 间隔小于一天的情况
//                todayFormat.format(date) + " AM"
//            } else {
//                todayFormat.format(date) + " PM"
//            }
            todayFormat.format(date)
        }
    }

}


/**
 * okhttp 拦截器
 */
class HttpLogInterceptor() : Interceptor {
    private val UTF8 = Charset.forName("UTF-8")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBody = request.body
        var body: String? = null
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset: Charset? = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            body = buffer.readString(charset!!)
        }
        Log.d(
            TAG,
            "发送请求: method：" + request.method
                    + "\nurl：" + request.url
                    + "\n请求头：" + request.headers
                    + "\n请求参数: " + body
        )
        val startNs = System.nanoTime()
        val response: Response = chain.proceed(request)
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body
        val rBody: String
        val source = responseBody!!.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer()
        var charset: Charset? = UTF8
        val contentType = responseBody.contentType()
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8)
            } catch (e: UnsupportedCharsetException) {
                e.printStackTrace()
            }
        }
        rBody = buffer.clone().readString((charset)!!)
        Log.d(
            TAG,
            ("收到响应: code:" + response.code
                    + "\n请求url：" + response.request.url
                    + "\n请求body：" + body
                    + "\nResponse: " + rBody)
        )
        return response
    }

    companion object {
        private val TAG = HttpLogInterceptor::class.java.simpleName
    }
}
