package com.collekarry.collgdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import static com.collekarry.collgdemo.MainActivity.common;
import static com.collekarry.collgdemo.MainActivity.rollNo;
import static com.collekarry.collgdemo.uploadFolderSelection.childfoldName;
import static java.security.AccessController.getContext;

public class naviGation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static String folderName,uploadName;
    ListView commonList;
    private static final int PICK_PDF_CODE = 2487 ;
   // private static final int CAMERA = 232 ;
    int flag ;
    public Uri filePath;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    DatabaseReference mDatabaseReference;
    DatabaseReference commonReference;
    List<Upload> uploadList;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1: if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                            Intent intent = new Intent();
                            intent.setType("*/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select your content"), PICK_PDF_CODE);

                        }
                    }
                    else
                        Toast.makeText(this, " -_- GET LOST -_-", Toast.LENGTH_LONG).show();
                    break;
            case 2: if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

                            startActivity(new Intent(this , captureImage.class));
                        }
                    }
                    else
                        Toast.makeText(this, " -_- GET LOST -_-", Toast.LENGTH_LONG).show();
                    break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_gation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      //  TextView textView = (TextView) findViewById(R.id.myNameTextView);
//        textView.setText(rollNo+"Jumped in!!!!!!");

        flag=0;
        uploadList = new ArrayList<>();
        commonList = (ListView) findViewById(R.id.CommonlistView);


        commonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                Upload upload = uploadList.get(i);

                //Opening the upload file in browser using the upload url
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(upload.getUrl()));
                startActivity(intent);
            }
        });

        commonDisplay();

        folderName = "";
        String basename = rollNo;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(basename);
        Log.i("name",basename+" "+rollNo);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), webView.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void commonDisplay()
    {
       // uploadList.clear();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(common);
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayAdapter<String> adapter;
                uploadList.clear();
//                commonList.getAdapter().notify();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadList.add(upload);
                }

                String[] uploads = new String[uploadList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadList.get(i).getName();
                }

                //displaying it to list
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                commonList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navi_gation, menu);

        return true;
    }


    public void getPDF() {

        //taking user permission
        //ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                    Uri.parse("package:" + "com.collekarry.collgdemo"));
//            startActivity(intent);
            ActivityCompat.requestPermissions(this,new  String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else {

            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select your content"), PICK_PDF_CODE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            if (data.getData() != null) {

                uploadFile(data.getData());

            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFile(Uri data) {

        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storageReference.child(rollNo+"/"+ System.currentTimeMillis() + ".*");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();

                            //editTextFilename.getText()
                            Upload upload = new Upload(uploadName, taskSnapshot.getDownloadUrl().toString());
                            folderDelection folderDelection = new folderDelection(folderName);

                            if(flag == 0)
                            {
                                if(folderName.equals(""))
                                    folderName = "BIN";

                                mDatabaseReference = mDatabaseReference.child(folderName);
                                mDatabaseReference.child(uploadName).setValue(upload);
                                mDatabaseReference = mDatabaseReference.getParent();
                            }
                            else {

                                mDatabaseReference = FirebaseDatabase.getInstance().getReference(common);
                                mDatabaseReference.child(uploadName).setValue(upload);
                            }
                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_folder)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter the name to the file:");

            // Set up the input
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    folderName = "";
                    folderName = input.getText().toString();
                    mDatabaseReference.child(folderName).setValue("");
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

        else if (id == R.id.action_upload)
        {
            flag=0;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter the Folder name and your name to the file:");

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText titleBox = new EditText(this);
            titleBox.setHint("Foldername");
            layout.addView(titleBox);

            final EditText descriptionBox = new EditText(this);
            descriptionBox.setHint("Title Of The File to Be Uploaded");
            layout.addView(descriptionBox);

            builder.setView(layout);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface d, int id) {
                    folderName = titleBox.getText().toString();
                    uploadName = descriptionBox.getText().toString();
                        getPDF();
                        d.dismiss();
                    }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();

            return true;
        }
        else if(id == R.id.action_note)
        {
            startActivity(new Intent(this , pdfListMaker.class));
            return  true;
        }
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new  String[]{Manifest.permission.CAMERA},2);
            }
            else {
                startActivity(new Intent(this,captureImage.class));
            }
        }else if (id == R.id.nav_capture) {

            startActivity(new Intent(this , photoViewer.class));

        } else if (id == R.id.nav_upload) {

            startActivity(new Intent(this, uploadFolderSelection.class));
            //startActivity(new Intent(this, viewUploads.class));
        } else if (id == R.id.nav_manage) {


        } else if (id == R.id.nav_share) {

            flag=1;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter the name to the file:");

            final EditText input = new EditText(this);
            input.setHint("Enter the name to the file");
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    uploadName = input.getText().toString();
                    uploadName = uploadName+"    By:"+rollNo;
                    getPDF();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            //getPDF();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
