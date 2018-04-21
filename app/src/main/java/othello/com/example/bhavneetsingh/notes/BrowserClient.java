package othello.com.example.bhavneetsingh.notes;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class BrowserClient extends WebViewClient {

    public SmoothProgressBar progressBar;

    public BrowserClient(SmoothProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(url.contains("https://deloton.com/afu.php")||url.contains("https://1movies.se/user/buyregistration"))
            return false;
        view.loadUrl(url);
        if (progressBar.getVisibility()== View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }

        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        System.out.println("on finish");
        if (progressBar.getVisibility()== View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }

    }
}
