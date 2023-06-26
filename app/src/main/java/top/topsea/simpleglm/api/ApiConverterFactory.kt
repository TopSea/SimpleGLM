package top.topsea.simpleglm.api

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


abstract class TxtResponseConverter<T> : Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T {
//        Log.d(TAG, "convert: ${value.string()}")
        return parseTxt(value.string())
    }

    abstract fun parseTxt(txt: String): T
}

abstract class TxtConverterFactory<T> : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return responseConverter()
    }

    abstract fun responseConverter(): TxtResponseConverter<T>
}

class ApiResponseConverter : TxtResponseConverter<ChatGLMA>() {
    override fun parseTxt(txt: String): ChatGLMA {
        var glma = ChatGLMA(null, null, null, null, false)
        return glma
    }
}