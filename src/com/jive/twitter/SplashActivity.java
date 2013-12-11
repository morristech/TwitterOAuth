package com.jive.twitter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.jive.twitter.client.TwitterClient.TwitterClientLifeCycle;
import com.jive.twitter.utils.Constants;

public class SplashActivity extends Activity implements TwitterClientLifeCycle{
	//on create -- start twitter client and show a spinner
	//when you 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.splash_screen);
        
        
		
        /* New Handler to start the NearByActivity        
         * and close this Splash-Screen after Defined seconds.*/

        new Handler().postDelayed(new Runnable()
        {          
                public void run() 
                {

                	((TwitterApp)(getApplication())).getTwitterClient().start(SplashActivity.this);

                }

        }, Constants.MSG_SPLASH_DISPLAY_LENGTH);
      
    }

	

	@Override
	public void startComplete(boolean successful) {
		Log.d("Splash", "Start Complete");
		
	}

	@Override
	public void shutdownComplete() {
		// TODO Auto-generated method stub
		
	}
}
