package com.example.slider_test_1;

import android.Manifest;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private float centerX;
    private float centerY;
    private float baseRadius; // "base" of the joystick
    private float hatRadius;  //top of the joystick
    private JoystickListener joystickCallback;  // so we can call the onJoystickMoved method

    private void setupDimensions(){
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 3;
        hatRadius = Math.min(getWidth(), getHeight()) / 5;
    }

    private void drawJoystick(float newX, float newY){
        if(getHolder().getSurface().isValid()) {
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // clear the canvas
            // draw the "base" of joystick
            colors.setARGB(255, 50, 50, 50); // Alpha determines transparency, with 255 being solid and 0 being invisible; then red, green and blue
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors);
            // draw the hat of joystick
            colors.setARGB(255, 50, 100, 255); // Alpha determines transparency, with 255 being solid and 0 being invisible; then red, green and blue
            myCanvas.drawCircle(newX, newY, hatRadius, colors);
            // make the joystick visible
            getHolder().unlockCanvasAndPost(myCanvas);
        }
    }

    public  JoystickView(Context context){
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener){
            joystickCallback = (JoystickListener) context;
        }
    }

    public JoystickView(Context context, AttributeSet attributes){
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener){
            joystickCallback = (JoystickListener) context;
        }
    }

    public JoystickView(Context context, AttributeSet attributes, int style){
        super(context, attributes, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener){
            joystickCallback = (JoystickListener) context;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.equals(this)){ // So the touch listener accepts touches coming only from this SurfaceView
            if (event.getAction() != event.ACTION_UP){ // If the action is NOT untouching the screen
                float displacement = (float) Math.sqrt(Math.pow(event.getX() - centerX, 2) + Math.pow(event.getY() - centerY, 2));
                if(displacement < baseRadius) {
                    drawJoystick(event.getX(), event.getY());
                    joystickCallback.onJoystickMoved( (event.getX() - centerX)/baseRadius, (event.getY() - centerY)/baseRadius, getId());
                }
                else{    // calculate by parallel triangles identity
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (event.getX() - centerX) * ratio;
                    float constrainedY = centerY + (event.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    joystickCallback.onJoystickMoved( (constrainedX - centerX)/baseRadius, (constrainedY - centerY)/baseRadius, getId());
                }
            }else{
                drawJoystick(centerX, centerY);
                joystickCallback.onJoystickMoved( 0, 0, getId());
            }
        }
        return true;
    }

    public interface JoystickListener{
        void onJoystickMoved(float xPercent, float yPercent, int source);
    }
}
