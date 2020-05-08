package tech.jour.lib.updater

import okhttp3.*
import java.io.IOException


/**
 * Created by journey on 2020/5/7.
 */
object UpdaterRequest {
    fun get(url: String){
        val url = "http://wwww.baidu.com"
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()
        val call: Call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
            }
        })
    }
}