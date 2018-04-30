package othello.com.example.bhavneetsingh.Triads.WebBrowser;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import othello.com.example.bhavneetsingh.Triads.R;

public class WebViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String LINK = "web_view_link";

    private String current_link;
    private WebView webView;
    private LinearLayout customViewContainer;
    private OnFragmentInteractionListener mListener;

    public WebViewFragment() {
        // Required empty public constructor
    }

    public String getCurrent_link() {
        return current_link;
    }

    public void setCurrent_link(String current_link) {
        this.current_link = current_link;
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public static WebViewFragment newInstance(String link) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(LINK,link);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            current_link = getArguments().getString(LINK);
        }

    }
    public void onResume() {

        super.onResume();
        if(getArguments()!=null)
        current_link = getArguments().getString(LINK);
        Log.d("webfragment",current_link);
        if(webView!=null)
        {
            webView.loadUrl(current_link);
            webView.clearHistory();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customViewContainer=view.findViewById(R.id.web_layout);
        webView=view.findViewById(R.id.web_view);
        SmoothProgressBar progressBar=view.findViewById(R.id.web_progress_bar);
        webView.setWebViewClient(new BrowserClient(progressBar));
        WebSettings settings=webView.getSettings();
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setSupportMultipleWindows(false);
        webView.setWebChromeClient(new CrmClient());
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    class CrmClient extends WebChromeClient {
        View view;
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view,callback);
            webView.setVisibility(View.GONE);
            customViewContainer.setVisibility(View.VISIBLE);
            customViewContainer.addView(view);
            this.view=view;
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            webView.setVisibility(View.VISIBLE);
            customViewContainer.removeView(view);
        }}
}
