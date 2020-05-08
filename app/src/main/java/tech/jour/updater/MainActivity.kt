package tech.jour.updater

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import tech.jour.lib.updater.Updater
import tech.jour.lib.updater.UpdaterCallback

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logger.addLogAdapter(AndroidLogAdapter())
        Updater.register(this).apply {
            dialogTitle = "应用更新"
            dialogMessage = "您的应用版本过低，请升级应用。"
            negativeButton = "取消"
            positiveButton = "升级"
            downloadDesc = "AppUpdater应用升级下载"
            downloadTitle = "AppUpdater升级"
            forceUpdate = true
        }
        check_btn.setOnClickListener {
            Updater.get("https://raw.githubusercontent.com/journe/AndroidAppUpdater/master/samples/android-version.json",
                object : UpdaterCallback {
                    override fun onFailure(t: String) {
                        runOnUiThread {
                            toast(t)
                        }
                    }

                    override fun onResponse(response: String) {
                        Logger.d(response)
                        val updaterBean = Gson().fromJson(response, UpdaterBean::class.java)
                        val versionCode = packageManager.getPackageInfo(packageName, 0).versionCode
                        if (versionCode < updaterBean.minimumVersionCode) {
                            runOnUiThread {
                                Updater.showDialog(
                                    context = this@MainActivity,
                                    updateUrl = updaterBean.androidUpdateUrl!!
                                )
                            }
                        }

                    }
                })
        }
    }
}
