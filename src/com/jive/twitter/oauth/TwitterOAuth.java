package com.jive.twitter.oauth;

import android.util.Pair;

import com.jive.twitter.oauth.engines.MyOAuthEngine;

interface TwitterConstants {
	static String CONSUMER_KEY = "BpPlSzQQCGJWd17CBTisrA";
	static String CONSUMER_SECRET = "mwbc50ovgKBB3JndbfwpRLUjHVio5GXXt2up4Zsm8";

	static final String TEMPORARY_TOKEN_REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	static final String AUTHORIZATION_VERIFIER_SERVER_URL = "https://api.twitter.com/oauth/authorize";
	static final String TOKEN_SERVER_URL = "https://api.twitter.com/oauth/access_token";
	static final String CALLBACK_URL =  null;//"https://localhost/twtCallback";
	static final String BASE_URL = "http://api.twitter.com/";
}

public class TwitterOAuth extends MyOAuthEngine implements
		TwitterConstants {

	public TwitterOAuth(OAuthClient listener) {
		super(listener);
	}

	@Override
	public String getRequestTokenUrl() {
		return TEMPORARY_TOKEN_REQUEST_URL;
	}

	@Override
	public String getAuthenticateUrl() {
		return AUTHORIZATION_VERIFIER_SERVER_URL;
	}

	@Override
	public String getLogoutUrl() {
		return null;
	}

	@Override
	public String getCallbackUrl() {
		// return null;
		return CALLBACK_URL;
	}

	@Override
	public Pair<String, String> getConsumerCredentials() {
		Pair<String, String> consumerCredentials = new Pair<String, String>(
				CONSUMER_KEY, CONSUMER_SECRET);
		return consumerCredentials;
	}

}
