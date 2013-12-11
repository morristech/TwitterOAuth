package com.jive.twitter.oauth;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jive.twitter.R;

public class SignInActivity extends Activity {

	public static final String EXTRA_AUTH_URL = "ExtraAuthUrl";
	public static final String EXTRA_CALLBACK_URL = "ExtraCallbackUrl";

	public String m_callbackURL;
	public String m_authURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in_layout);

		m_authURL = getIntent().getStringExtra(EXTRA_AUTH_URL);
		m_callbackURL = getIntent().getStringExtra(EXTRA_CALLBACK_URL);

		WebView wv = (WebView) findViewById(R.id.webview);
		wv.setWebViewClient(oauthWebViewClient);
		wv.loadUrl(m_authURL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_oauth, menu);
		return true;
	}

	/* WebViewClient must be set BEFORE calling loadUrl! */
	WebViewClient oauthWebViewClient = new WebViewClient() {

		@Override
		public void onPageFinished(WebView view, String url) {

			if (url.startsWith(m_callbackURL)) {
				try {

					if (url.indexOf("oauth=") != -1) {
						// sign-in-with-twitter/?oauth_token=NPcudxy0yU5T3tBzho7iCotZ3cnetKwcTIRlX0iwRl0&oauth_verifier=uw7NjWHT6OJ1MpJOXsHfNxoAhPKpgI8BlYDhxEjIBY
						// extract oauth_token
						// store it in m_tokenSecret
						// m_listener.onAuthenticated(true);
					}
				} catch (Exception e) {

				}
				System.out.println("onPageFinished : " + url);

			}
		}
	};
}
