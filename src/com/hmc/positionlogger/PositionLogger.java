package com.hmc.positionlogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PositionLogger extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    public void startListening(View v) {
        Intent i=new Intent(this, PositionService.class);
        
        i.putExtra(PositionService.listeningOnGPS, "true");
        i.putExtra(PositionService.listeningOnNetwork, "true");
        
        startService(i);
      }
      
      public void stopListening(View v) {
        stopService(new Intent(this, PositionService.class));
      }
}