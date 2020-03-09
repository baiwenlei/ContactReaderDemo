package com.sogou.sgmar.usepermissiondemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public final static int REQUEST_CODE_PHONE_CALL = 0;
    public final static int REQUEST_CODE_READ_CONTACT = 1;
    public final static int REQUEST_CODE_VIEW_EXTERNAL_STORAGE = 2;

    public final static int ACTIVITY_REQUEST_CODE_SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button callButton = (Button)findViewById(R.id.call_button);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    int ret = checkSelfPermission(Manifest.permission.CALL_PHONE);
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MainActivity.this, "call permission denied", Toast.LENGTH_SHORT).show();
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_PHONE_CALL);
                        return;
                    }
                }
                makeCallTo10086();
            }
        });

        Button viewContactButton = (Button)findViewById(R.id.view_contact_button);
        viewContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    int ret = checkSelfPermission(Manifest.permission.READ_CONTACTS);
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MainActivity.this, "READ CONTACTS permission denied", Toast.LENGTH_SHORT).show();
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACT);
                        return;
                    }
                }
                viewContact();
            }
        });

        Button viewExternalButton = (Button)findViewById(R.id.view_storage_button);
        viewExternalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    int ret = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MainActivity.this, "WRITE EXTERNAL permission denied", Toast.LENGTH_SHORT).show();
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_VIEW_EXTERNAL_STORAGE);
                        return;
                    }
                }
                viewExternalStorage();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(MainActivity.this, "onRequestPermissionsResult get requestCode " + requestCode, Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case REQUEST_CODE_PHONE_CALL: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCallTo10086();
                } else {
                    Toast.makeText(MainActivity.this, "You have just denied permission request of make phone call", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case REQUEST_CODE_READ_CONTACT: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewContact();
                } else {
                    Toast.makeText(MainActivity.this, "You have just denied permission request read contact", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case REQUEST_CODE_VIEW_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewExternalStorage();
                } else {
                    Toast.makeText(MainActivity.this, "You have just denied permission request of view external storage", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void makeCallTo10086() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:10086"));
        startActivity(intent);
    }

    private void viewContact() {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    private void viewExternalStorage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");      //all files
//        intent.setType("text/xml");   //XML file only
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), ACTIVITY_REQUEST_CODE_SELECT_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Toast.makeText(this, "file path: "+data.getData().toString(), Toast.LENGTH_SHORT).show();
            Log.e("baiwenlei", "file path: "+data.getData().toString());
        }
    }
}
