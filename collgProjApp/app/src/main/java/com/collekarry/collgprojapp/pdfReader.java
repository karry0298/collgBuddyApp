package com.collekarry.collgprojapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class pdfReader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);

    //https://developer.android.com/guide/topics/providers/document-provider.html
    }
}

/*

private void CopyReadAssets()
        {
            AssetManager assetManager = getAssets();

            InputStream in = null;
            OutputStream out = null;
            File file = new File(getFilesDir(), "abc.pdf");
            try
            {
                in = assetManager.open("abc.pdf");
                out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e)
            {
                Log.e("tag", e.getMessage());
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    Uri.parse("file://" + getFilesDir() + "/abc.pdf"),
                    "application/pdf");

            startActivity(intent);
        }

        private void copyFile(InputStream in, OutputStream out) throws IOException
        {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, read);
            }
        }

    }

    XML => <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />


 */