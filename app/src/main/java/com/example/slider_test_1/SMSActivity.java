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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
    private TextView selectedNumber;
    private Spinner messageSpinner;
    private Spinner numberSpinner;
    private String phoneNumber;
    private Button send, save, erase;

    // keep the data after closing the app
    private List<String> list;  // list to add new numbers
    ArrayAdapter<String> dataAdapter;
    private SharedPreferences savedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_layout);

        // config elements of layout
        number = findViewById(R.id.inputNumber);
        send = findViewById(R.id.buttonSend);
        save = findViewById(R.id.saveButton);
        erase = findViewById(R.id.buttonErase);
        messageSpinner = findViewById(R.id.message_spinner);
        numberSpinner = findViewById(R.id.number_spinner);
        selectedNumber = findViewById(R.id.numberSelected);

        // check if we have the permission to send sms, if not, ask for the permission
        if(checkPermission(Manifest.permission.SEND_SMS)){
            Toast.makeText(this, "Application can send texts", Toast.LENGTH_SHORT).show();
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        // config buttons
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend(v);
            }
        });
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

        // Initialize elements to keep data after closing app
        list = new ArrayList<String>();
        // To RECOVER what's been saved in the sharedpreferences file
        savedData = getSharedPreferences("data", Context.MODE_PRIVATE);
        if(savedData.contains("DATA_LIST")){
            // Get data from sharedpreferences and put it in the dropdown list
            retrieveSharedValue();
        }else{
            // save list in sharedPreferences
            packageSharedPreferences();
            // Get data from sharedpreferences and put it in the dropdown list
            retrieveSharedValue();
        }

        // config spinner for numbers
        addItemsOnNumberSpinner();
        numberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedString;
                // from adapter
                selectedString = dataAdapter.getItem(position).toString();
                selectedNumber.setText(selectedString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onEraseButtonClick(View view){
        savedData.edit().remove("DATA_LIST").apply();
        list.clear();
        // save list the now empty in sharedPreferences
        packageSharedPreferences();
    }

    public void onSaveButtonClick(View view){
        String phoneNumber = number.getText().toString();
        list.add(phoneNumber);
        selectedNumber.setText(phoneNumber);
        Log.d("list", "" + list);

        // save list in sharedPreferences
        packageSharedPreferences();
    }

    // add items into spinner
    public void addItemsOnNumberSpinner(){
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberSpinner.setAdapter(dataAdapter);
    }

    // Put an arraylist in sharedpreferences
    private void packageSharedPreferences() {
        SharedPreferences.Editor editor = savedData.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(list);
        editor.putStringSet("DATA_LIST", set);
        editor.apply();
        Log.d("storeSharedPreferences",""+set);
    }

    // Recover the data from the sharedpreferences
    private void retrieveSharedValue() {
        // add the shared preferences to the dropdown list
        addItemsOnNumberSpinner();
        Set<String> set = savedData.getStringSet("DATA_LIST", null);
        list.clear();       // clear the list
        list.addAll(set);   // get the new values in the list
        Log.d("retrieveSharedPreferenc","" + set);
    }

    // method for the button
    public void onSend(View v){
        phoneNumber = selectedNumber.getText().toString();
        String smsMessage = messageSpinner.getSelectedItem().toString();

        // are fields empty?
        if(phoneNumber.length() == 0 || smsMessage.length() == 0){
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
