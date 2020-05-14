package tech.jour.lib.updater

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import okhttp3.*
import java.io.IOException
import kotlin.system.exitProcess


/**
 * Created by journey on 2020/5/7.
 */
object Updater : OnProgressListener {
    const val UPDATER_DOWNLOAD_PROGRESS = "UPDATER_DOWNLOAD_PROGRESS"
    var dialogTitle: String = "应用更新"
    var downloadTitle: String = "下载"
    var downloadDesc: String = "应用正在下载"
    var dialogMessage: String = "您的应用版本过低，请升级应用。"
    var negativeButton: String = "取消"
    var positiveButton: String = "升级"
    var forceUpdate: Boolean = false

    private lateinit var mView: View

    fun get(url: String, callback: UpdaterCallback) {
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

    fun post(url: String, body: RequestBody, callback: UpdaterCallback) {
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
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
        updateUrl: String
    ) {
        mView = View.inflate(context, R.layout.download_dialog, null)
        mView.findViewById<TextView>(R.id.mMessageText).text = dialogMessage
        val dialog = AlertDialog.Builder(context)
            .setTitle(dialogTitle)
            .setView(mView)
            .setPositiveButton(
                positiveButton, null
            )
            .setNegativeButton(negativeButton) { dialog, which ->
                if (forceUpdate) {
                    exitProcess(0)
                }
            }
            .setCancelable(!forceUpdate)
            .create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val serviceIntent = Intent(context, DownloadService::class.java)
            serviceIntent.data = Uri.parse(updateUrl)
            context.startService(serviceIntent)
            if (!forceUpdate) {
                dialog.dismiss();
            }
        }

    }

    fun register(context: Context): Updater {
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        intentFilter.addAction(UPDATER_DOWNLOAD_PROGRESS)
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(DownloadApkReceiver(this), intentFilter)
        return this
    }

    override fun onProgress(fraction: Float) {
        val progressBar = mView.findViewById<ProgressBar>(R.id.mProgressBar)
        val textView = mView.findViewById<TextView>(R.id.mTextView)
        progressBar.progress = fraction.toInt()
        textView.text = String.format("%.2f", fraction) + "%"
//        fraction.toString()
    }

}

interface UpdaterCallback {
    fun onFailure(t: String)
    fun onResponse(response: String)
}