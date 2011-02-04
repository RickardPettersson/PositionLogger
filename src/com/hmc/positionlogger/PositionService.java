package com.hmc.positionlogger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class PositionService extends Service {
	private boolean isListening = false;
	public static final String listeningOnGPS = "false";
	public static final String listeningOnNetwork = "false";
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		start();
		
		// START_NOT_STICKY = The service being closed if Android has to kill
		// off this service (e.g., low memory), it should not restart it once
		// conditions improve.
		return (START_NOT_STICKY);
	}

	@Override
	public void onDestroy() {
		stop();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return (null);
	}

	private void start() {
		if (!isListening) {
			Log.w(getClass().getName(), "Start listening");
			isListening = true;
		}
	}

	private void stop() {
		if (isListening) {
			Log.w(getClass().getName(), "Stop listening");
			isListening = false;
		}
	}
}