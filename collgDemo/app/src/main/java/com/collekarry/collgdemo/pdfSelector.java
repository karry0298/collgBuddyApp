package com.collekarry.collgdemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import static com.collekarry.collgdemo.MainActivity.pdfFolderPath;

public class pdfSelector extends AppCompatActivity {

    private static final int SELECT_PDF = 23224;
    public static String listOfPdfsInFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list_maker);

        listOfPdfsInFile = pdfFolderPath;
        getPDF();
    }

    public void getPDF() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a PDF "), SELECT_PDF);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PDF) {

                Uri selectedUri_PDF = data.getData();
                listOfPdfsInFile = listOfPdfsInFile+selectedUri_PDF.toString();
                Toast.makeText(this, "Added into the folder", Toast.LENGTH_LONG).show();
                Log.i("abc",selectedUri_PDF.toString());
            }
        }
    }
}