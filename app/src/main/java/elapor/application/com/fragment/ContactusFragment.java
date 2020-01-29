package elapor.application.com.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import elapor.application.com.elapor.MainActivity;
import elapor.application.com.elapor.R;
import elapor.application.com.libs.CommonUtilities;

public class ContactusFragment extends Fragment {

	public static ImageView back;
	public static WebView webView;
	public static LinearLayout retry;
	public static Button btnReload;
	public static ProgressBar loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_contactus, container, false);

		back          = rootView.findViewById(R.id.back);
		webView = (WebView) rootView.findViewById(R.id.webview);
		loading = (ProgressBar) rootView.findViewById(R.id.pgbarLoading);
		retry = (LinearLayout) rootView.findViewById(R.id.loadMask);
		btnReload = (Button) rootView.findViewById(R.id.btnReload);
		btnReload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loading.setVisibility(View.VISIBLE);
				retry.setVisibility(View.GONE);
				webView.loadUrl("https://karangtarunaku.blogspot.com");
			}
		});

		webView.setVerticalScrollBarEnabled(false);
		webView.setWebChromeClient(new MyWebViewClient());

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).displayView(0);
			}
		});

		return rootView;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loading.setVisibility(View.VISIBLE);
        retry.setVisibility(View.GONE);
        webView.loadUrl(CommonUtilities.SERVER_URL+"/adminweb/contactus/detail.php");
    }

	private class MyWebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if(newProgress==100) {
				loading.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}
	}

}
