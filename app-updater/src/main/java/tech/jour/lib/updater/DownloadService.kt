package tech.jour.lib.updater

import android.app.DownloadManager
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * Created by journey on 2020/5/8.
 */
class DownloadService : IntentService("UpdaterDownloadService") {
    override fun onHandleIntent(intent: Intent?) {
        val url = intent!!.dataString
        val downloadManager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))

        //指定APK缓存路径和应用名称，可在SD卡/Android/data/包名/file/Download文件夹中查看
        request.setDestinationInExternalFilesDir(
            this,
            Environment.DIRECTORY_DOWNLOADS,
            "update.apk"
        )
        //设置网络下载环境为wifi
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
        //设置显示通知栏，下载完成后通知栏自动消失
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setTitle(Updater.downloadTitle)
        request.setDescription(Updater.downloadDesc)
        request.setAllowedOverRoaming(false)
        val requestId = downloadManager.enqueue(request)

        //查询下载信息
        val query = DownloadManager.Query()
        query.setFilterById(requestId)
        try {
            var isGoging = true
            while (isGoging) {
                val cursor = downloadManager.query(query)
                cursor.position
                if (cursor != null && cursor.moveToFirst()) {
                    val status =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                    //已经下载文件大小
                    val downloaded =
                        cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    //下载文件的总大小
                    val total =
                        cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    if (total > 0) {
                        val progress = downloaded / total.toFloat() * 100
                        val localIntent = Intent(Updater.UPDATER_DOWNLOAD_PROGRESS)
                        localIntent.putExtra("progress", progress)
                        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent)
                    }
                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            isGoging = false
                            val localIntent = Intent(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                            localIntent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, requestId)
                            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent)
                        }
                    }
                }
                cursor?.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

