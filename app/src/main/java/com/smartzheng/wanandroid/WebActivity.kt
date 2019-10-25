package com.smartzheng.wanandroid

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.activity_main.*


class WebActivity : AppCompatActivity() {
    companion object {
        const val URL = "http://172.16.1.210:3000/"
        const val EXTRA_URL = "EXTRA_URL"
    }

    private lateinit var mAgentWeb: AgentWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAgentWeb()
    }

    private fun initAgentWeb() {
        val url = intent.getStringExtra(EXTRA_URL) ?: URL
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(wb_container, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(getColor(R.color.colorPrimary))
                .createAgentWeb()
                .ready()
                .go(url)
        mAgentWeb.jsInterfaceHolder?.addJavaObject("jsi", JSI())
    }

    inner class JSI {
        @JavascriptInterface
        fun toWeb(url: String) {
            val intent = Intent(this@WebActivity, WebActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            startActivity(intent)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle?.onDestroy()
        super.onDestroy()
    }
}
