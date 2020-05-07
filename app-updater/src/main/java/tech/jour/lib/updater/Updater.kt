package tech.jour.lib.updater

import androidx.core.app.ActivityCompat
import okhttp3.*
import java.io.IOException

/**
 * Created by journey on 2020/5/7.
 */
object Updater {
    fun check(url: String, callback: UpdaterCallback) {
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()
        val call: Call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e.message ?: "Unknown Exceptions")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.body != null) {
                    callback.onResponse(response.body!!.string())
                } else {
                    callback.onFailure("Null Response")
                }
            }
        })
    }
}

interface UpdaterCallback {
    fun onFailure(t: String)
    fun onResponse(response: String)
}