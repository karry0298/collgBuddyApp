package com.collekarry.collgdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static String finalPathName,pdfFolderPath;
    EditText roll,pass;
    Button log;
    static String rollNo;
    static final String common = "COMMON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roll = (EditText) findViewById(R.id.rollEditText);
        pass = (EditText) findViewById(R.id.passEditText);
        log = (Button) findViewById(R.id.logButton);

        pdfFolderPath = getCacheDir().toString()+"/myPdf/";
        log.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        rollNo = roll.getText().toString();
        String password = pass.getText().toString();

        finalPathName = getCacheDir().toString()+"/"+roll+"/";
        if(!rollNo.equals("") && !password.equals(""))
        {
            Log.i("name",rollNo);
            Intent intent = new Intent(getApplicationContext(),naviGation.class);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "Enter the containts...", Toast.LENGTH_SHORT).show();
    }
}
