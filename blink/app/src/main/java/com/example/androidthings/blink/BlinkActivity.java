package com.example.androidthings.blink;
import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;

import android.util.Log;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager; //changed class name peripheralmanagerservices to peripheralmanager added the dependency in the gradle

public class BlinkActivity extends Activity {
    private static final String TAG = BlinkActivity.class.getSimpleName();

    private Gpio mLedGpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting ButtonActivity");

        PeripheralManager manager = PeripheralManager.getInstance(); //changed from this : PeripheralManagerService service = new PeripheralManagerService();

        try {
            Log.i(TAG, "Configuring GPIO pins");
            mLedGpio = manager.openGpio(BoardDefaults.getGPIOForLED()); //getting the pin number from the board ddefault.java file
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

        } catch (IOException e) {
            Log.e(TAG, "Error configuring GPIO pins", e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Runnable ledBlinker = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    // Turn on the LED
                    setLedValue(true);
                    sleep(3000);

                    // Turn off the LED
                    setLedValue(false);
                    sleep(3000);
                }
            }
        };
        new Thread(ledBlinker).start();
    }

    private void sleep(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the value of the LED output.
     */
    private void setLedValue(boolean value) {
        try {
            mLedGpio.setValue(value);
        } catch (IOException e) {
            Log.e(TAG, "Error updating GPIO value", e);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        if (mLedGpio != null) {
            try {
                mLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing LED GPIO", e);
            } finally{
                mLedGpio = null;
            }
            mLedGpio = null;
        }
    }
}