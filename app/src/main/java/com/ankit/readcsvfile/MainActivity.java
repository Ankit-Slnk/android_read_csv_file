package com.ankit.readcsvfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {

    Button btnOpenExcel, btnDownloadSampleExcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpenExcel = findViewById(R.id.btnOpenExcel);
        btnDownloadSampleExcel = findViewById(R.id.btnDownloadSampleExcel);

        btnDownloadSampleExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    File outDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
                    Utility.downloadSampleExcel(MainActivity.this, "sample", outDir.toString());
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }
            }
        });

        btnOpenExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openExcel();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }
            }
        });
    }

    public void openExcel() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/comma-separated-values");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    String path = uri.getPath().replaceAll("document/raw:/", "");
                    Log.e("path", path);
                    File file = new File(path);
                    Log.e("exists", String.valueOf(file.exists()));

                    try {
                        CSVReader reader = new CSVReader(new FileReader(path));
//                        reader.skip(1);
                        String[] nextLine;
                        while ((nextLine = reader.readNext()) != null) {
                            Log.e("nextLine[0]", nextLine[0]);
                            Log.e("nextLine[1]", nextLine[1]);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
    }
}