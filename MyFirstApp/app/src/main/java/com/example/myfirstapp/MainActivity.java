package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
//    An Activity represents a single screen in an app.
    Button mCaptureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    mCaptureBtn = findViewById(R.id.capture_image_btn);

    // Request for Camera Permission
    if (ContextCompat.checkSelfPermission(MainActivity.this,
            Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.CAMERA
                },
                100);
    }

    // button click
    mCaptureBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v){
//            The Intent describes the activity to start and carries any necessary data.
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent,100);
        }
    });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // called when image was captured from camera
        if (requestCode == 100) {
            //  Get Capture Image
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            captureImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Intent activity2 = new Intent(this,Activity2.class);
            activity2.putExtra("captured_image", byteArray);
            startActivity(activity2);
        }
        }
}