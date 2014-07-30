package pl.qrchack.exodus;

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
// import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Main extends Activity {
    WebView webView;
    private MediaPlayer mp;
    AssetFileDescriptor afd;
    private String prevUrl;
    int choose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mp = new MediaPlayer();
        
        prevUrl = "";
        
        setContentView(R.layout.main);
        webView = (WebView)findViewById(R.id.fullscreen_content);
        webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				if(url.startsWith("tel:")) {
					Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
					startActivity(dial);
					return true;
				}
				if(url.startsWith("mailto:")) {
					Intent intent = new Intent(Intent.ACTION_SENDTO);
					intent.setType("text/plain");
					/*
					intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
					intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
					*/
					intent.setData(Uri.parse(url));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
					startActivity(intent);
					return true;
				}
				if(url.startsWith("fb:")) {
					try
					{
						getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
						Intent fb = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+url.substring(3)));
						startActivity(fb);
					}
					catch (PackageManager.NameNotFoundException e)
					{
						Intent fb = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id="+url.substring(3)));
						startActivity(fb);
					}
					return true;
				}
				if(url.startsWith("yt:")) {
					Intent intent = null;
					try {
        				intent = new Intent(Intent.ACTION_VIEW);
        				intent.setPackage("com.google.android.youtube");
     					intent.setData(Uri.parse(url.substring(3)));
        				startActivity(intent);
    				} catch (ActivityNotFoundException e) {
        				intent = new Intent(Intent.ACTION_VIEW);
       					intent.setData(Uri.parse(url.substring(3)));
        				startActivity(intent);
    				}
					return true;
				}
		        if (url.endsWith(".mp3") || url.endsWith(".ogg")) {

		        	try {
			        	boolean IsPlaying = mp.isPlaying();
			        	boolean IsDifferent = false;
			        	
			        	choose = 0;
			        	url = url.replace("file:///android_asset/", ""); /// :D xD thumb up ;P
						afd = getAssets().openFd(url);
			        	
						if(IsPlaying)
							choose +=1;
						
						if(prevUrl != url)
							IsDifferent = true;
						
						
						if(IsDifferent)
							choose +=2;
						
		        		
						switch(choose)
						{
							case 1: // IsPlaying
								mp.stop();
								break;
							case 3: // IsPlaying & IsDifferent
								mp.stop();
								
								prevUrl = url;
			        			
			        			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				        		mp.prepare();
				        		mp.start();
				        		break;
							case 2: // IsDifferent ...
								prevUrl = url;
							case 0: // ... or the same BUT NOT PLAYING
								prevUrl = url;
			        			
			        			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				        		mp.prepare();
				        		mp.start();
								break;
								default: throw new Exception();
							}	
					
	        		
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					} catch (Exception e)
					{
						Toast.makeText(getApplicationContext(), "Exception - STH STUPID!!!" + choose, Toast.LENGTH_LONG).show();
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
        //you can also link to a website. Example:
        //webView.loadUrl("www.google.com");
        //I have included web permissions in the AndroidManifest.xml
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
