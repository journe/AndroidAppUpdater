package tech.jour.appupdater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import kotlinx.android.synthetic.main.activity_main.*
import tech.jour.lib.updater.Updater
import tech.jour.lib.updater.UpdaterCallback

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logger.addLogAdapter(AndroidLogAdapter())
        check_btn.setOnClickListener {
            Updater.check("",
                object : UpdaterCallback {
                    override fun onFailure(t: String) {

                    }

                    override fun onResponse(response: String) {
                        Logger.d(response)
                        val updaterBean = Gson().fromJson(response, UpdaterBean::class.java)
                    }
                })
        }
    }
}
