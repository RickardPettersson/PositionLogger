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
	private final static String TAG = PositionLogger.class.getSimpleName();
	private boolean isListening = false;
	public static final String listeningOnGPS = "false";
	public static final String listeningOnNetwork = "false";
	private LocationManager locationManager;
	private Location lastGoodLocation = null;

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

			Log.i(TAG, "Start listening");
			isListening = true;
		}
	}

	private void stop() {
		if (isListening) {
			// Remove the listener you previously added
			locationManager.removeUpdates(locationListener);

			Log.i(TAG, "Stop listening");
			isListening = false;
		}
	}

	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			// Called when a new location is found by the network location
			// provider.

			boolean isBetter = false;

			// Check if we got a last good location
			if (lastGoodLocation == null) {
				isBetter = true;
			} else {
				// Check
				if (isBetterLocation(location, lastGoodLocation)) {
					isBetter = true;
				}
			}

			if (isBetter) {
				/*
				 * location.toString() = Location[ mProvider=network,
				 * mTime=1296831917582, mLatitude=59.22235755,
				 * mLongitude=17.955837000000002, mHasAltitude=false,
				 * mAltitude=0.0, mHasSpeed=false, mSpeed=0.0,
				 * mHasBearing=false, mBearing=0.0, mHasAccuracy=true,
				 * mAccuracy=55.0, mExtras=Bundle[mParcelledData.dataSize=148]]
				 */
				// The location is better
				Log.i(TAG, "Got a better location - " + location.toString());
			}
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.i(TAG, "Provider status changed - Provider: " + provider
					+ " - Status: " + status);
		}

		public void onProviderEnabled(String provider) {
			Log.i(TAG, provider + " Provider enabled");
		}

		public void onProviderDisabled(String provider) {
			Log.i(TAG, provider + " Provider enabled");
		}
	};

	/**
	 * 
	 * 
	 * Some copy paste code from
	 * http://developer.android.com/guide/topics/location
	 * /obtaining-user-location.html
	 * 
	 * 
	 */
	private static final int TWO_MINUTES = 1000 * 60 * 2;

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
}