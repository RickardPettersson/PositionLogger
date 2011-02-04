package com.hmc.positionlogger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class PositionService extends Service {
	private boolean isListening = false;
	public static final String listeningOnGPS = "false";
	public static final String listeningOnNetwork = "false";
	private LocationManager locationManager;

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
			// Create the LocationManager object
			locationManager = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);

			// Register the listener with the Location Manager to receive
			// location updates
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

			Log.w(getString(R.string.logTag), "Start listening");
			isListening = true;
		}
	}

	private void stop() {
		if (isListening) {
			Log.w(getString(R.string.logTag), "Stop listening");
			isListening = false;
		}
	}

	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			// Called when a new location is found by the network location
			// provider.
			
			Log.w(getString(R.string.logTag), "Recieved position - " + location.toString());
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}
	};

}