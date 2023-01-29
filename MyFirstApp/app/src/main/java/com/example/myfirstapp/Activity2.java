package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.os.Bundle;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

//public class Activity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
public class Activity2 extends AppCompatActivity{
    File f;
    Button uploadBtn;
    String image_file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("captured_image");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = findViewById(R.id.image_view);

        image.setImageBitmap(bmp);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        image_file_name = "temporary_" +sdf.format(new Date())+".png";

        //create a file to write bitmap data
        f = new File(getCacheDir(), image_file_name);

        try {
            boolean x = f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100 , bos);

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        uploadBtn = findViewById(R.id.upload_btn);

        // button click
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String baseURL = "http://192.168.0.241:5000/upload";
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                File fls1 = new File(getCacheDir(), image_file_name);
                RequestParams params = new RequestParams();
                try {
                    params.put("file", fls1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                asyncHttpClient.post(Activity2.this, baseURL, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(Activity2.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(Activity2.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}