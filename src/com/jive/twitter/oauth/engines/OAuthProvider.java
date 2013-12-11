package com.jive.twitter.oauth.engines;

import org.apache.http.Header;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Pair;

import com.google.gson.GsonBuilder;
import com.jive.twitter.oauth.engines.MyOAuthEngine.OAuthClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

interface OAuthConstants {
    public static final String OAUTH_NONCE = "oauth_nonce";
    public static final String OAUTH_CALLBACK = "oauth_callback";
    public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    public static final String OAUTH_SIGNATURE_METHOD_ALG = "HMAC-SHA1";
    public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    public static final String OAUTH_SIGNATURE = "oauth_signature";
    public static final String OAUTH_VERSION = "oauth_version";
    public static final String OAUTH_TOKEN = "oauth_token";

    public static final String OAUTH_VERSION_VALUE = "1.0";
    public static final String OAUTH_HEADER_PREFIX = "OAuth ";
}

interface HttpConstants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String REALM = "realm";

}

public abstract class OAuthProvider implements AuthProvider, OAuthConstants, HttpConstants {

    private OAuthClient m_listener;
    private OAuthToken m_oauthRequestToken;

    public OAuthProvider(OAuthClient authListener) {
        m_listener = authListener;
    }

    public void start(Context context) {

        AsyncHttpClient client = new AsyncHttpClient();

        String authorizationHeaderValue = getAuthorizationHeader(getRequestTokenUrl());
        client.addHeader(AUTHORIZATION_HEADER, authorizationHeaderValue);

        client.post(getRequestTokenUrl(), new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] responseHeaders, byte[] content) {
                if (content != null && content.length > 0) {
                    String responseJson = new String(content);

                    // extract the oauthToekn
                    m_listener.onReady();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] responseHeaders, byte[] content, Throwable exception) {
                m_listener.onError();

            }

        });

    }

    /*
     * public void login(Context context) {
     * 
     * IntentFilter filter = new IntentFilter(
     * SignInActivityCompletedReceiver.ACTION_TWITTER_SIGNIN_COMPLETE);
     * context.registerReceiver(m_receiver, filter);
     * 
     * Intent intent = new Intent(context, SignInActivity.class);
     * 
     * String url = getAuthenticateUrl() + "?oauth_token=\"" +
     * percentEncode(m_oauthToken) + "\"";
     * 
     * intent.putExtra(SignInActivity.EXTRA_AUTH_URL, url);
     * intent.putExtra(SignInActivity.EXTRA_CALLBACK_URL, getCallbackUrl());
     * intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
     * context.startActivity(intent);
     * 
     * }
     */

    public void shutdown() {

    }

    OAuthToken getOauthRequestToken() {
        return m_oauthRequestToken;
    }

    public abstract String getRequestTokenUrl();

    public abstract String getAuthenticateUrl();

    public abstract String getLogoutUrl();

    // /sign-in-with-twitter/?

    public abstract String getCallbackUrl();

    public abstract Pair<String, String> getConsumerCredentials();
    
    class SignInActivityCompletedReceiver extends BroadcastReceiver {
        public static final String ACTION_TWITTER_SIGNIN_COMPLETE = "com.jive.twitter.signincomplete";
        public static final String EXTRA_SUCCESSFUL = "successful";
        public static final String EXTRA_TOKEN_SECRET = "token_secret";

        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(this);
        }

    }

    public interface OAuthClient {
        public void onReady();

        public void onAuthenticated(boolean successful);

        public void onLoggedOut();

        public void onError();
    }
}
