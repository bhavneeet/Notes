package othello.com.example.bhavneetsingh.notes;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserClient extends WebViewClient {
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
