package tech.jour.lib.updater

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.download_dialog.*

/** Created by journey on 2020/5/9.  */
class DownloadDialog(private val mContext: Context) :
    AlertDialog(mContext) {
    private var mTextView: TextView? = null
    private var mProgressBar: ProgressBar? = null
    private var mView: View? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        // 设置对话框样式
        setStyle()
        // 初始化控件
        initView()
    }

    private fun initView() {
        mView = View.inflate(mContext, R.layout.download_dialog, null)
//        mTextView = mView.findViewById<View>(R.id.mTextView) as TextView
//        mProgressBar = mView.findViewById<View>(R.id.mProgressBar) as ProgressBar
//        setContentView(mView)
    }

    fun setMessage(msg: String) {
        mMessageText.text = msg
    }

    private fun setStyle() {
        // 设置对话框不可取消
        setCancelable(false)
        // 设置触摸对话框外面不可取消
        setCanceledOnTouchOutside(false)
        val displaymetrics = DisplayMetrics()
        window!!.windowManager.defaultDisplay.getMetrics(displaymetrics)
        // 获得应用窗口大小
        val layoutParams = this.window!!.attributes
        // 设置对话框居中显示
        layoutParams.gravity = Gravity.CENTER
        // 设置对话框宽度为屏幕的3/5
        layoutParams.width = displaymetrics.widthPixels / 5 * 3
    }

    // 设置进度条
    fun setProgress(progress: Int) {
        mTextView!!.text = "$progress%"
        mProgressBar!!.progress = progress
    }

}