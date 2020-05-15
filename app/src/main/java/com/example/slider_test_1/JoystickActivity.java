package com.example.slider_test_1;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

public class JoystickActivity extends AppCompatActivity implements JoystickView.JoystickListener{

    private JoystickView joystickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        joystickView = new JoystickView(this);
        // So it shows only the JoystickView by default when the app starts
        setContentView(joystickView);
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id){
        Log.d("Main method", "x%: " + xPercent + "y%: " + yPercent);
    }
}
