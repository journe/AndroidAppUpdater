package tech.jour.lib.updater

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class DownloadApkReceiver(private val progressListener: OnProgressListener) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {
                val downloadId =
                    intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                installApk(context, downloadId)
            }
            DownloadManager.ACTION_NOTIFICATION_CLICKED -> {
                val viewDownloadIntent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
                viewDownloadIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(viewDownloadIntent)
            }
            else -> {
                val p = intent.getFloatExtra("progress", 0f)
                progressListener.onProgress(p)
            }
        }
    }


    private fun installApk(
        context: Context,
        downloadId: Long
    ) {
        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadFileUri = downloadManager.getUriForDownloadedFile(downloadId)
        if (downloadFileUri != null) {
            context.startActivity(
                Intent(Intent.ACTION_VIEW)
                    .apply {
                        setDataAndType(downloadFileUri, "application/vnd.android.package-archive")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    })
        } else {
        }
    }
}

interface OnProgressListener {
    fun onProgress(fraction: Float)
}