package com.jive.twitter.client;

import org.json.JSONObject;

import android.webkit.WebView;

import com.jive.twitter.oauth.DummyOAuth;
import com.jive.twitter.oauth.TwitterOAuth;
import com.jive.twitter.oauth.engines.MyOAuthEngine.OAuthClient;
import com.jive.twitter.oauth.engines.OAuthProvider;
import com.jive.twitter.utils.Constants;

public class TwitterClient implements OAuthClient {
    TwitterOAuth m_twitterAuth;

    public interface TwitterClientLifeCycle {
        void startComplete(boolean successful);

        void shutdownComplete();
    }

    public interface TwitterAuthListener {
        void onAuthenticated(boolean successful);
    }

    TwitterClientLifeCycle m_listener;

    public void start(TwitterClientLifeCycle listener) {

        OAuthProvider twitterOAuth;
        if (Constants.debugSignatureGen) {
            twitterOAuth = new DummyOAuth(this);
        } else {
            twitterOAuth = new TwitterOAuth(this);

        }
        twitterOAuth.start(null);
    }

    public void signIn(WebView webView, TwitterAuthListener listener) {

    }

    public interface TwitterResponseListener {
        public void onResponse(boolean successful, JSONObject response);
    }

    public void getTrends(TwitterResponseListener listener) {
        // async http to send the trends request
        // m_tokensecret = m_twitterAuth
    }

    @Override
    public void onReady() {
        // I have to decide what to do here

    }

    @Override
    public void onAuthenticated(boolean successful) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLoggedOut() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError() {
        // TODO Auto-generated method stub

    }
}
