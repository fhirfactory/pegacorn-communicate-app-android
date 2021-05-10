package im.vector.health.role_selection

import android.app.Activity
import android.content.DialogInterface
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.view.KeyEvent
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import butterknife.BindView
import im.vector.R
import im.vector.activity.VectorAppCompatActivity

class RoleSelectionActivity : VectorAppCompatActivity() {

    @BindView(R.id.role_selector_webview)
    lateinit var mWebView: WebView
    override fun getLayoutRes(): Int = R.layout.activity_role_selector
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initUiAndData() {
        super.initUiAndData()
        //configureToolbar()
        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.domStorageEnabled = true
        mWebView.settings.databaseEnabled = true
        mWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        mWebView.loadUrl(getString(R.string.role_selector_url))
        //mWebView.loadUrl("http://www.google.com")
        mWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler,
                                            error: SslError) {
                handler.proceed()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                request?.url?.let { url ->
                    if (url.host == "lingo-server.site-a") {
                        //TODO: Remove this stuff when the role selector's oauth doesn't redirect weirdly
                        url.host?.let { original ->
                            Uri.parse(getString(R.string.role_selector_url)).host?.let {toReplaceWith ->
                                val str = url.toString().replace(original,toReplaceWith)
                                mWebView.loadUrl(str)
                            }
                        }
                        return true
                    } else if (url.toString() == getString(R.string.role_selector_redirect_link)) {
                        setResult(Activity.RESULT_OK)
                        finish()
                        return true;
                    }
                }
                return false
            }
        }
    }

}