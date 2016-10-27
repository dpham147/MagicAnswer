package edu.orangecoastcollege.cs273.magicanswer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by dpham147 on 10/27/2016.
 */

public class ShakeDetector implements SensorEventListener {

    // Constant to represent shake threshold
    private static final float SHAKE_THRESHOLD = 25f;
    // Constant to represent how long between shakes (in ms)
    private static final int SHAKE_TIMELAPSE = 2000;

    // What was the last time event occurred
    private long timeOfLastShake;
    // Define a listener to register onShake events
    private OnShakeListener shakeListener;

    // Constructor to create new ShakeDetector passing in an OnShakeListener as argument
    public ShakeDetector(OnShakeListener listener) {
        shakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Determine if the event is an accelerometer
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            // Get x, y, z values when event occurs:
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Compare all 3 values against gravity
            float gForceX = x - SensorManager.GRAVITY_EARTH;
            float gForceY = y - SensorManager.GRAVITY_EARTH;
            float gForceZ = z - SensorManager.GRAVITY_EARTH;

            // Compute sum of squares
            double vector = Math.pow(gForceX, 2.0) + Math.pow(gForceY, 2.0) + Math.pow(gForceZ, 2.0);

            // Compute gForce
            float gForce = (float) Math.sqrt(vector);

            // Compare gForce against threshold
            if (gForce > SHAKE_THRESHOLD)
            {
                // Get the current time
                long now = System.currentTimeMillis();
                // Compare to see if current time is at least 2000ms > timeOfLastShake
                if (now - timeOfLastShake > SHAKE_TIMELAPSE) {
                    timeOfLastShake = now;
                    // Register a shake event!!
                    shakeListener.onShake();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Define our own interface for other classes to implement called onShake()
    // Responsibility of MagicAnswerActivity.java to implement
    public interface OnShakeListener {
        void onShake();
    }
}
