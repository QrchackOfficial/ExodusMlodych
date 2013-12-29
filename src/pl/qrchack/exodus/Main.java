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
import android.app.*;
import java.net.*;

public class Main extends Activity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        webView = (WebView)findViewById(R.id.fullscreen_content);
        webView.setWebViewClient(new MyWebViewClient() {
			public void onPageFinished(WebView view, String url) {
				if(url.startsWith("tel:")) {
					// Open dialer intent
					// Toast.makeText(view.getContext(), "TODO: Implement dialer intent here", 1000).show();
					Intent dial = new Intent(Intent.ACTION_DIAL);
					dial.setData(Uri.parse(url));
					startActivity(dial);
					view.goBack();
				}
				if(url.startsWith("mailto:")) {
					Toast.makeText(view.getContext(), "TODO: Implement email intent here", 1000).show();
				}
			}
            public void onReceivedError(WebView view, int errorCode, String description, String url) {
				webView.loadUrl("file:///android_asset/www/error.html");
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
