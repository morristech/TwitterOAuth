package com.jive.twitter.oauth.engines;

import android.content.Context;

public interface AuthProvider {
	public void start(Context context);
	public void shutdown();
	public String getAuthorizationHeader(String requestUrl);
}
