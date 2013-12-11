package com.jive.twitter.oauth.engines;

import android.net.Uri;

public class OAuthToken {
    String oauth_token;
    String oauth_token_secret;
    boolean oauth_callback_confirmed;
    
    public void parse(String formsParam){
        Uri uri=Uri.parse(formsParam);
        oauth_token = uri.getQueryParameter("oauth_token");
        oauth_token_secret = uri.getQueryParameter("oauth_token_secret");
        oauth_callback_confirmed = Boolean.valueOf(uri.getQueryParameter("oauth_callback_confirmed"));
    }

}
