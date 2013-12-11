package com.jive.twitter;

import com.jive.twitter.client.TwitterClient;

import android.app.Application;

public class TwitterApp extends Application {
	
	TwitterClient m_twitterClient;
	
	public void onCreate() {
		super.onCreate();
		m_twitterClient = new TwitterClient();
		
	}
	
	public TwitterClient getTwitterClient(){
		return m_twitterClient;
	}
}
