package pl.qrchack.exodus2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
	
	final Activity activity = this;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        WebView webView = (WebView)this.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                activity.setTitle("Loading...");
                activity.setProgress(progress * 100);
  
                if(progress == 100)
                    activity.setTitle(R.string.app_name);
            }
        });
        
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                // Handle the error
            }
  
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl("file:///android_asset/www/index.html");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }
    
}