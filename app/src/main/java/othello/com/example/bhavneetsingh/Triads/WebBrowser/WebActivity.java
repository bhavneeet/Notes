package othello.com.example.bhavneetsingh.Triads.WebBrowser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import othello.com.example.bhavneetsingh.Triads.News.News;
import othello.com.example.bhavneetsingh.Triads.R;
import othello.com.example.bhavneetsingh.Triads.News.*;

public class WebActivity extends ToolbarActivity {

    private String url;
    WebView webView;
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_web);
        Intent intent=getIntent();
        if(intent!=null&&intent.hasExtra(News.URL))
        {
            url=intent.getStringExtra(News.URL);
        }
        webView=findViewById(R.id.web_view);
        SmoothProgressBar progressBar=findViewById(R.id.web_progress_bar);
        webView.setWebViewClient(new BrowserClient(progressBar));
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
    }
}
