package top.topsea.simpleglm

import android.app.Application
import android.util.Log
import com.tencent.mmkv.MMKV

const val TAG = "TopSea:::"
class SimpleApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        val rootDir = MMKV.initialize(this)
        Log.d(TAG, "onCreate: $rootDir")

    }
}