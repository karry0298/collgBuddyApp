package com.collekarry.collgprojapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class noteReader extends AppCompatActivity {

    int noteNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_reader);

        EditText editText = (EditText)findViewById(R.id.editText);

        Intent intent = getIntent();
        noteNo = intent.getIntExtra("noteNo" , -1 );

        if( noteNo != -1){
            editText.setText(MainActivity.info.get(noteNo));
        }
        else{
            MainActivity.info.add("");
            noteNo = MainActivity.info.size() -1;

        }


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                MainActivity.info.set(noteNo , String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

}
