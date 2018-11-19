package com.collekarry.collgdemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import static com.collekarry.collgdemo.MainActivity.finalPathName;
import static com.collekarry.collgdemo.MainActivity.rollNo;
import static java.security.AccessController.getContext;

public class captureImage extends AppCompatActivity implements View.OnClickListener {


    private static final int CAMERA_REQUEST = 123;
    ImageView imageView;
    Intent cameraIntent;
    Button addButton,reCapture;
    File images;
    Uri uriSavedImage;
    Bitmap photo;
    String name;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            photo = rotateImage(photo,90);

            imageView.setImageBitmap(photo);

        }
    }


    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

       images = new File(finalPathName);
        Log.i("info123",finalPathName);

        if (!images.exists()) {

            File wallpaperDirectory = new File(finalPathName);
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File(finalPathName), fileName);

        if (file.exists()) {

            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);

            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Log.i("info1",file.getAbsolutePath().toString());
            out.flush();
            Log.i("info2",file.getAbsolutePath().toString());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void selectImage()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the name to the file:");

        final EditText input = new EditText(this);
        input.setHint("Enter the name to the file");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                name = input.getText().toString();
                cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                images = new File(getCacheDir(), "asdaf");
//                images.mkdir();
//                Log.i("info",images.getAbsolutePath().toString());
//
//                uriSavedImage= Uri.fromFile(images);
//
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
//
               startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);

        imageView = (ImageView) findViewById(R.id.captureImageView);
        addButton = (Button) findViewById(R.id.addButton);
        reCapture = (Button) findViewById(R.id.captureAgain);

        addButton.setOnClickListener(this);
        reCapture.setOnClickListener(this);

        selectImage();
    }

    @Override
    public void onClick(View view) {

        if(view == addButton){
          createDirectoryAndSaveFile(photo,name);
            Toast.makeText(this, "Image Stored in file...", Toast.LENGTH_SHORT).show();
       }
        else{
            selectImage();
        }

    }
}
