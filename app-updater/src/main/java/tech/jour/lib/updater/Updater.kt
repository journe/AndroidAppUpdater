package tech.jour.lib.updater

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import okhttp3.*
import java.io.IOException
import kotlin.system.exitProcess

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

    fun showDialog(
        context: Context,
        forceUpdate: Boolean,
        updateUrl: String
    ) {
        AlertDialog.Builder(context)
            .setTitle("应用更新")
            .setMessage("您的应用版本过低，请升级应用。")
            .setPositiveButton(
                "升级"
            ) { dialog, which -> }
            .setNegativeButton("取消") { dialog, which ->
                if (forceUpdate) {
                    exitProcess(0)
                }
                dialog.dismiss()
            }
            .setCancelable(!forceUpdate)
            .create()
            .show()
    }
}

interface UpdaterCallback {
    fun onFailure(t: String)
    fun onResponse(response: String)
}