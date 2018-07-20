package nana.android.locationserviceapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000 * 60 * 1;
    private static final float LOCATION_DISTANCE = 0.15f;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.i(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            Double latOnLockChange = mLastLocation.getLatitude();
            Double longOnLockChange = mLastLocation.getLongitude();

            String latOnlockChangeStr = String.valueOf(latOnLockChange);
            String longOnlockChangeStr = String.valueOf(longOnLockChange);

            Log.i(TAG, "Latitude On Change " + latOnlockChangeStr);
            Log.i(TAG, "Longitude On Change " + longOnlockChangeStr);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled: " + provider);

            Double latOnLockChange = mLastLocation.getLatitude();
            Double longOnLockChange = mLastLocation.getLongitude();

            String latOnlockChangeStr = String.valueOf(latOnLockChange);
            String longOnlockChangeStr = String.valueOf(longOnLockChange);

            Log.i(TAG, "Latitude onProviderDisabled " + latOnlockChangeStr);
            Log.i(TAG, "Longitude onProviderDisabled " + longOnlockChangeStr);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled: " + provider);

            Double latOnLockChange = mLastLocation.getLatitude();
            Double longOnLockChange = mLastLocation.getLongitude();

            String latOnlockChangeStr = String.valueOf(latOnLockChange);
            String longOnlockChangeStr = String.valueOf(longOnLockChange);

            Log.i(TAG, "Latitude On onProviderEnabled " + latOnlockChangeStr);
            Log.i(TAG, "Longitude On onProviderEnabled " + longOnlockChangeStr);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged: " + provider);

            Double latOnLockChange = mLastLocation.getLatitude();
            Double longOnLockChange = mLastLocation.getLongitude();

            String latOnlockChangeStr = String.valueOf(latOnLockChange);
            String longOnlockChangeStr = String.valueOf(longOnLockChange);

            Log.i(TAG, "Latitude onStatusChanged " + latOnlockChangeStr);
            Log.i(TAG, "Longitude onStatusChanged " + longOnlockChangeStr);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
            Location location = this.mLocationManager.getLastKnownLocation("gps");

            String latStr = String.valueOf(location.getLatitude());
            String longStr = String.valueOf(location.getLongitude());
            Log.i(TAG, " LATITUDE UP => " + latStr);
            Log.i(TAG, " LONGITUDE UP => " + longStr);

        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);

            Location location = this.mLocationManager.getLastKnownLocation("gps");

            String latStr = String.valueOf(location.getLatitude());
            String longStr = String.valueOf(location.getLongitude());
            Log.i(TAG, " LATITUDE DOWN => " + latStr);
            Log.i(TAG, " LONGITUDE DOWN => " + longStr);

        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
