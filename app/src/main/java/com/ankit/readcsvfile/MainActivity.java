package com.ankit.readcsvfile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnOpenCSV, btnDownloadSampleCSV, btnGenerateSampleCSV;
    String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    String fileName = "snv_insurance_sample.csv";
    String filePath = baseDir + File.separator + fileName;
    File f = new File(filePath);
    CSVWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("filePath", filePath);

        btnOpenCSV = findViewById(R.id.btnOpenCSV);
        btnDownloadSampleCSV = findViewById(R.id.btnDownloadSampleCSV);
        btnGenerateSampleCSV = findViewById(R.id.btnGenerateSampleCSV);

        btnDownloadSampleCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    File outDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
                    Utility.downloadSampleCSV(MainActivity.this, "sample", outDir.toString());
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }
            }
        });

        btnGenerateSampleCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (f.exists() && !f.isDirectory()) {
                    try {
                        writer = new CSVWriter(new FileWriter(filePath, true));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        writer = new CSVWriter(new FileWriter(filePath));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                List<String[]> data = new ArrayList<>();

                data.add(new String[]{"Ship Name", "Scientist Name", ""});
                data.add(new String[]{"Ship Name 1", "Scientist Name 1", ""});
                data.add(new String[]{"Ship Name 2", "Scientist Name 2", ""});

                data.add(new String[]{"", "", ""});
                data.add(new String[]{"", "", ""});

                data.add(new String[]{"name", "age", "gender"});
                data.add(new String[]{"A", "12", "male"});
                data.add(new String[]{"B", "14", "female"});

                writer.writeAll(data);

                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnOpenCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openCSV();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }
            }
        });
    }

    public void openCSV() {
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