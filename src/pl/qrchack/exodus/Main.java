package pl.qrchack.exodus;

// Imports
import java.io.IOException;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Main extends Activity {
    WebView webView;
    private MediaPlayer mp;
    AssetFileDescriptor afd;
    public static String prevUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mp = new MediaPlayer();

        webView = (WebView)findViewById(R.id.fullscreen_content);
        webView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {	

				if (url.startsWith("tel:")) {
					Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
					startActivity(dial);
					return true;
				}
				if (url.startsWith("mailto:")) {
					Intent intent = new Intent(Intent.ACTION_SENDTO);
					intent.setType("text/plain");
					/*
					intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
					intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
					*/
					intent.setData(Uri.parse(url));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);

					return true;
				}
				if (url.startsWith("fb:")) {
					try {
						getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
						Intent fb = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+url.substring(3)));
						startActivity(fb);
					}
					catch (PackageManager.NameNotFoundException e) {
						Intent fb = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id="+url.substring(3)));
						startActivity(fb);
					}

					return true;
				}
				if (url.startsWith("yt:")) {
					Intent intent = null;
					try {
        				intent = new Intent(Intent.ACTION_VIEW);
        				intent.setPackage("com.google.android.youtube");
     					intent.setData(Uri.parse(url.substring(3)));
        				startActivity(intent);
    				}
    				catch (ActivityNotFoundException e) {
        				intent = new Intent(Intent.ACTION_VIEW);
       					intent.setData(Uri.parse(url.substring(3)));
        				startActivity(intent);
    				}

					return true;
				}
		        if (url.endsWith(".mp3") || url.endsWith(".ogg")) {
		        	try {
			        	url = url.replace("file:///android_asset/", "");
						afd = getAssets().openFd(url);
						
						//Toast.makeText(getApplicationContext(), "choose: "+choose + " " + prevUrl + " " + url, Toast.LENGTH_LONG).show();
						
						if (url.equals(Main.prevUrl)) {
							// Toast.makeText(getApplicationContext(), "te same", Toast.LENGTH_LONG).show();
							mp.stop();
							Main.prevUrl="";
						}
						else {
							// Toast.makeText(getApplicationContext(), "inne", Toast.LENGTH_LONG).show();
							if (mp.isPlaying()) mp.stop();
	
				        	Main.prevUrl=url;
							mp.reset();
		        			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			        		mp.prepare();
			        		mp.start();
			        	}
					}
					catch (IOException e) {
						Toast.makeText(getApplicationContext(), "IOException: "+e.getMessage(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
					
					}
					catch (Exception e) {
						Toast.makeText(getApplicationContext(), "Exception: "+e.getMessage(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}

		            return true;
		        }
				else {
					view.loadUrl(url);
					return false;
				}
			}
            public void onReceivedError(WebView view, int errorCode, String description, String url) {
				webView.loadUrl("file:///android_asset/www/error.html");
            }
        });
        webView.loadUrl("file:///android_asset/www/index.html");
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= 16) {
            webSettings.setAllowUniversalAccessFromFileURLs(true); // from API JELLY_BEAN (16)
        }
        
    }
    
    @Override
    public void onBackPressed()
    {
        if(webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}