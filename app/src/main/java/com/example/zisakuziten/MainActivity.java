package com.example.zisakuziten;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private BottomNavigationView mBottomNav;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        Log.d("MAINACTIVITY","oncreate");

        realm = Realm.getDefaultInstance();

        Fragment fragment = new GroupActivity();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout,fragment );
        transaction.addToBackStack(null);
        transaction.commit();

        // navigation selected, selected switch BUN is selectNavigation function
        mBottomNav = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.groupactivity_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.import_group:
//                Toast.makeText(this,"IMPORT GROUP!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,10);
                return true;
            case R.id.setting:
                new AlertDialog.Builder(this)
                        .setTitle("設定")
                        .setMessage("設定はまだ作ってません；；\nいつかのアップデートに期待しましょう！！")
                        .setPositiveButton("OK", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // navigation view selected
    private void selectNavigation(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Log.d("Navigation","home");
                Fragment fragment = new GroupActivity();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout,fragment );
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.quiz:
                Log.d("Navigation","quiz");
                fragment = new PlayChoiceActivity();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout,fragment );
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.store:
                Log.d("Navigation","play");
                fragment = new StoreActivity();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("===onActivityResult===", "==========onActivityResult=====");
        Log.d("data", data.getDataString());
        Log.d("requestsCode", String.valueOf(requestCode));
        try {
            //====file get contents =====
            if (requestCode == 10 && resultCode == this.RESULT_OK) {
                String filePath = data.getDataString().replace("content://", "");
                String decodedfilePath = URLDecoder.decode(data.getDataString(), "utf-8");

                Uri uri = Uri.parse(decodedfilePath);
                Log.d("PATH1", uri.toString() + "");
//                Log.d("pathdaaaaaa",getPathFromUri(this,uri));
                gsonRealmOpen(getPathFromUri(this, uri));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPathFromUri(final Context context, final Uri uri) {
        boolean isAfterKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isAfterKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(
                    uri.getAuthority())) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }else {
                    return "/stroage/" + type +  "/" + split[1];
                }
            }else if ("com.android.providers.downloads.documents".equals(
                    uri.getAuthority())) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }else if ("com.android.providers.media.documents".equals(
                    uri.getAuthority())) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                contentUri = MediaStore.Files.getContentUri("external");
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())) {//MediaStore
            return getDataColumn(context, uri, null, null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String[] projection = {
                MediaStore.Files.FileColumns.DATA
        };
        try {
            cursor = context.getContentResolver().query(
                    uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int cindex = cursor.getColumnIndexOrThrow(projection[0]);
                return cursor.getString(cindex);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public void gsonRealmOpen(String path){
        Gson gson = new Gson();
        Group object = null;
        try {
            object = gson.fromJson(new FileReader(path), Group.class);
            final Group finalObject = object;
            try {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Group group = realm.createObject(Group.class);
                        group.groupName = finalObject.groupName;
                        group.updateTime = finalObject.updateTime;
                        group.ziten_updT_List.addAll(finalObject.ziten_updT_List);
                        Toast.makeText(getApplicationContext(),"ファイルが正常に読み込めました！",Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (NullPointerException e){
                Toast.makeText(this,"ファイルが読めません。",Toast.LENGTH_SHORT).show();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"ファイルがありません。",Toast.LENGTH_SHORT).show();
        }
    }







}




