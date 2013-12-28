package pl.qrchack.exodus;

import pl.qrchack.exodus.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
// import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import android.content.Context;

public class Main extends Activity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        webView = (WebView)findViewById(R.id.fullscreen_content);
        webView.setWebViewClient(new MyWebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webView.loadUrl("file:///android_asset/www/error.html");
            }
			public void onLoadResource(WebView view, String url) {
				if(url.startsWith("tel:")) {
					// Open dialer intent
					Toast.makeText(getApplicationContext(), "TODO: Implement dialer intent here", 1000).show();
					view.goBack();
				}
				if(url.startsWith("mailto:")) {
					Toast.makeText(getApplicationContext(), "TODO: Implement email intent here", 1000).show();
					view.goBack();
				}
			}
        });
        webView.loadUrl("file:///android_asset/www/index.html");
        //you can also link to a website. Example:
        //webView.loadUrl("www.google.com");
        //I have included web permissions in the AndroidManifest.xml
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        
    }
    
    @Override
    public void onBackPressed()
    {
        if(webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }
    

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    
}
