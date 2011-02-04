package com.hmc.positionlogger;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class PositionLogger extends Activity {
	
	private final static String TAG = PositionLogger.class.getSimpleName();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setHomeAction(new IntentAction(this, PositionLogger.createIntent(this), R.drawable.home));
        actionBar.addAction(new IntentAction(this, PositionLogger.createIntent(this), R.drawable.layers));
    }

	public void startListening(View v) {
		Log.i(TAG, "Start listening click");
		
		Intent i = new Intent(this, PositionService.class);

		i.putExtra(PositionService.listeningOnGPS, "true");
		i.putExtra(PositionService.listeningOnNetwork, "true");
		
		startService(i);
	}

	public void stopListening(View v) {
		Log.i(TAG, "Stop listening click");

		stopService(new Intent(this, PositionService.class));
	}
      
    public static Intent createIntent(Context context) {
    	Intent intent = new Intent(context, PositionLogger.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	return intent;
    }
}
