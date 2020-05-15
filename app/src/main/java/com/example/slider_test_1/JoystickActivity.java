package com.example.slider_test_1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class JoystickActivity extends AppCompatActivity implements JoystickView.JoystickListener{

    private JoystickView joystickView;
    RelativeLayout buttons_layout;
    FrameLayout frame;
    boolean mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frame= new FrameLayout(this);
        buttons_layout= new RelativeLayout(this);


        joystickView = new JoystickView(this);
        joystickView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        // So it shows only the JoystickView by default when the app starts
        mode=true;

        //====Switch=====
        Switch switch1= new Switch(this);
        final Button Button1 = new Button(this);
        Button1.setText("Accelerometer");
        //Button1.setId(1);
        //configure relative layout
        RelativeLayout.LayoutParams s1= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        //define with and height of layout (relative)
        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        //Add the parameters to the buttons layout
        buttons_layout.setLayoutParams(params);
        //Add the button
        buttons_layout.addView(Button1);
        //positioning
        s1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        s1.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        switch1.setLayoutParams(s1);
        //merge surfaceview and relativelayout together
        frame.addView(joystickView);
        frame.addView(buttons_layout);
        setContentView(frame);

        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToAcc();
            }
        });
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id){
        Log.d("Main method", "x%: " + xPercent + "y%: " + yPercent);
    }

    public void changeToAcc(){
        Intent intent = new Intent(this, AccelerometerActivity.class);
        startActivity(intent);
    }

}

