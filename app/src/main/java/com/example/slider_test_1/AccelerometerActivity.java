package com.example.slider_test_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class AccelerometerActivity extends AppCompatActivity  implements SensorEventListener {
    private SensorManager sensorManager;
    Sensor accelerometer;
    TextView datax;
    TextView datay;
    TextView dataz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_layout);

        datax = findViewById(R.id.textViewx);
        datay = findViewById(R.id.textViewy);
        dataz = findViewById(R.id.textViewz);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //mGeneralView.setSensorValues(sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]);
        CharSequence Roll  = String.valueOf(event.values[0]);
        CharSequence Pitch  = String.valueOf(event.values[1]);
        CharSequence Yaw  = String.valueOf(event.values[2]);
        datax.setText("Roll: "+Roll);
        datay.setText("Pitch: "+Pitch);
        dataz.setText("Yaw: "+Yaw);

        //Log.d(TAG, "onSensorChanged: X:"+ sensorEvent.values[0] + "Y:" + sensorEvent.values[1]+"Z:" + sensorEvent.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void changeToJoystick(View view){
        Intent intent = new Intent(this, JoystickActivity.class);
        startActivity(intent);
    }
}
