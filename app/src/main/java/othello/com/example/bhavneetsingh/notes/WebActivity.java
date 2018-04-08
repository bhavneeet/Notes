package othello.com.example.bhavneetsingh.notes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends ToolbarActivity {

    private String url;
    WebView webView;
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.activity_web);
        Intent intent=getIntent();
        if(intent!=null&&intent.hasExtra(News.URL))
        {
            url=intent.getStringExtra(News.URL);
        }
        webView=findViewById(R.id.web_browser);
        webView.setWebViewClient(new BrowserClient());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
    }
}
