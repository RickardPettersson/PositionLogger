package com.hmc.positionlogger;

import com.google.android.maps.MapActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MapFrontendActivity extends MapActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mapfrontend);
	}
	
    public static Intent createIntent(Context context) {
    	Intent intent = new Intent(context, MapFrontendActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	return intent;
    }
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
