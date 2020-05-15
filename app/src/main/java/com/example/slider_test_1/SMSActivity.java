package com.example.slider_test_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SMSActivity extends AppCompatActivity{

    // constant for a request code
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    // variables for UI elements
    private EditText  number;
    private Spinner messageSpinner;
    private Spinner numberSpinner;
    private Button send, save, erase;

    // keep the data after closing the app
    private List<String> list;  // list to add new numbers
    private SharedPreferences savedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_layout);

        number = findViewById(R.id.inputNumber);
        send = findViewById(R.id.buttonSend);
        save = findViewById(R.id.saveButton);
        erase = findViewById(R.id.buttonErase);

        // Initialize elements to keep data after closing app
        list = new ArrayList<String>();
        // To RECOVER what's been saved in the sharedpreferences file
        savedData = getSharedPreferences("data", Context.MODE_PRIVATE);



        // send the message
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend(v);
            }
        });

        // check if we have the permission to send sms, if not, ask for the permission
        send.setEnabled(false);
        if(checkPermission(Manifest.permission.SEND_SMS)){
            send.setEnabled(true);
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick(v);
            }
        });

        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEraseButtonClick(v);
            }
        });



        // save list in sharedPreferences
        packageSharedPreferences();
        // Get data from sharedpreferences and put it in the dropdown list
        retrieveSharedValue();

    }

    public void onEraseButtonClick(View view){
        savedData.edit().remove("DATE_LIST").apply();
        list.clear();
        // save list the now empty in sharedPreferences
        packageSharedPreferences();
    }

    public void onSaveButtonClick(View view){
        String phoneNumber = number.getText().toString();
        list.add(phoneNumber);
        Log.d("list", "" + list);

        // save list in sharedPreferences
        packageSharedPreferences();
    }

    // add items into spinner
    public void addItemsOnNumberSpinner(){
        numberSpinner = (Spinner) findViewById(R.id.number_spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberSpinner.setAdapter(dataAdapter);
    }

    // Put an arraylist in sharedpreferences
    private void packageSharedPreferences() {
        SharedPreferences.Editor editor = savedData.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(list);
        editor.putStringSet("DATE_LIST", set);
        editor.apply();
        Log.d("storeSharedPreferences",""+set);
    }

    // Recover the data from the sharedpreferences
    private void retrieveSharedValue() {
        // add the shared preferences to the dropdown list
        addItemsOnNumberSpinner();
        Set<String> set = savedData.getStringSet("DATE_LIST", null);
        list.clear();       // clear the list
        list.addAll(set);   // get the new values in the list
        Log.d("retrieveSharedPreferenc","" + set);
    }

    // method for the button
    public void onSend(View v){
        String phoneNumber = number.getText().toString();
        //String smsMessage = message.getText().toString();
        messageSpinner = findViewById(R.id.message_spinner);
        String smsMessage = messageSpinner.getSelectedItem().toString();

        //Toast.makeText(this,"OnClickListener : " + "\nSpinner 1 : "+ smsMessage2 , Toast.LENGTH_SHORT).show();

        // are fields empty?
        if(phoneNumber == null || phoneNumber.length() == 0 ||
                smsMessage == null || smsMessage.length() == 0){
            Toast.makeText(this, "Empty fields!", Toast.LENGTH_SHORT).show();
        }

        // check for permission again
        if(checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
            Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    // check if we have the permission to send SMS
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
