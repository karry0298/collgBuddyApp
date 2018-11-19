package com.collekarry.collgdemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.collekarry.collgdemo.MainActivity.rollNo;

public class uploadFolderSelection extends AppCompatActivity {

    ListView listView;
    static String childfoldName;
    DatabaseReference mDatabaseReference;
    List<folderDelection> uploadList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_folder_selection);

        listView = (ListView) findViewById(R.id.listViewChoice);
        uploadList = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                folderDelection upload = uploadList.get(i);

                childfoldName = upload.getName();
                Log.i("Upload folder",childfoldName);

                Intent intent = new Intent(getApplicationContext() , viewUploads.class);
                startActivity(intent);
            }
        });

        //getting the database reference
        String basename = rollNo;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(basename);

        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String keyName = postSnapshot.getKey();
                    folderDelection upload = new folderDelection(keyName);
                    uploadList.add(upload);
                }

                if(uploadList != null){

                    String[] uploads = new String[uploadList.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = uploadList.get(i).getName();
                    }

                    //displaying it to list
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                    listView.setAdapter(adapter);
                }
                else{
                    String[] up =new String[1] ;
                    up[0] = "no folder created ";
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, up);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
