package com.example.zisakuziten;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;



import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class GroupActivity extends Fragment {

    public Realm realm;
    public ListView listView;
    public CheckBox checkBox;
    private BottomNavigationView mBottomNav;
    public FloatingActionButton action_button;
    public int checkbox_status;
    public List<List> checked_list_data;
    public List<Ziten> checked_list;
    Group createGroup;

    //permittion
    String PERMISSION_WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";  //ストレージの読み書き権限
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;    //※番号は任意：onRequestPermissionsResult() で合わせる


//    @Override
//    protected void onCreateView(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_group);
        @Override
        public View onCreateView(final LayoutInflater inflater,ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.activity_group, container, false);

            //openRealm
            realm    = Realm.getDefaultInstance();
            listView = (ListView)view.findViewById(R.id.listView);
            checkBox = (CheckBox)view.findViewById(R.id.checkBox);
//            action_button = (FloatingActionButton)view.findViewById(R.id.action_button);
            //0 == GONE ,1 == VISIBLE
            checkbox_status = 0;
            checked_list = new ArrayList<>();
//            createGroup = new Group();

            //toooooolbarrrrr
            AppCompatActivity activity = (AppCompatActivity)getActivity();
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            setHasOptionsMenu(false);
            actionBar.setTitle("Group");


//            Toolbar toolbar = view.findViewById(R.id.toolbar);
//            activity.setSupportActionBar(toolbar);



            //clickでpiceに移動！
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Group group = (Group) parent.getItemAtPosition(position);
                        //!
//                        Intent intent = new Intent(getActivity().getApplicationContext(), GroupPiceActivity.class);
//                        intent.putExtra("updateTime", group.updateTime);
//                        startActivity(intent);
                        Bundle bundle = new Bundle();
                        Fragment fragment = new GroupPiceActivity();
                        bundle.putString("updateTime",group.updateTime);
                        fragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameLayout,fragment );
                        transaction.addToBackStack(null);
                        transaction.commit();
                }
            });
            //Group delete or hensyu
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Group group = (Group)parent.getItemAtPosition(position);
//                    ((Vibrator) getActivity(getContext().VIBRATOR_SERVICE)).vibrate(70);
                    final String[] items = {"名前を変える", "消去する","jsonに出力"};
                    new AlertDialog.Builder(view.getContext())
                            .setTitle(group.groupName + "をどうするんや？")
                            .setItems(items, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0){
                                        Log.d("AlertDialog","item1,hensyuu");
                                        Intent intent = new Intent(getContext(),GroupDetailActivity.class);
                                        intent.putExtra("updateTime",group.updateTime);
                                        startActivity(intent);

                                    }else if(which == 1) {
                                        Log.d("AlertDialog", "item2,syoukyo(delete");
                                        delete(group);
                                    }else if(which == 2){
                                        //権限確認。
                                        boolean isGranted = checkPermission(getContext(),PERMISSION_WRITE_EXTERNAL_STORAGE);  //現在のパーミッションをチェック
                                        Log.d("DebugTag", PERMISSION_WRITE_EXTERNAL_STORAGE + "\nisGranted = " + isGranted);
                                        createGroup = group;
                                        if (!isGranted) {
                                            //パーミッションが付与されてないとき、根拠の説明と要求をする（ダイアログを出す）
                                            Log.d("DebugTag", PERMISSION_WRITE_EXTERNAL_STORAGE + ", requestCode = " + REQUEST_WRITE_EXTERNAL_STORAGE);
                                            showPermissionRationaleAndRequest(PERMISSION_WRITE_EXTERNAL_STORAGE, REQUEST_WRITE_EXTERNAL_STORAGE); //API 23 (Android 6.0)
                                        }else {
                                            openFileAcitivity();
                                        }
                                        //onRequestPermissionsResultへ。

//                                        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//                                        intent.setType("*/*");
//                                        createGroup = group;
//                                        intent.setType("application/json");
//                                        intent.putExtra(Intent.EXTRA_TITLE, "Realm.json");
//
//                                        startActivityForResult(intent, 11);
                                    }else {
                                    }
                                }
                            }).show();
                    return true;
                }
            });

        // navigation selected, selected switch BUN is selectNavigation function
//        mBottomNav = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
//        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                selectNavigation(item);
//                return true;
//            }
//        });
            return view;
    }






    @Override
    public void onStart(){
        super.onStart();

        FloatingActionButton floatingActionButton = (FloatingActionButton)getActivity().findViewById(R.id.action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("actionbutton","onclick");
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.group_create_dialog);
                dialog.setTitle("hellllllo");
                Button createBtn = dialog.findViewById(R.id.create);
                createBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        final EditText editTitle = dialog.findViewById(R.id.editTitle);
                        Log.d("Dialog", String.valueOf(editTitle.getText()));
                        //Date
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
                        final String updateDate = sdf.format(date);
                        //realm save
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Group group = realm.createObject(Group.class);
                                group.groupName = editTitle.getText().toString();
                                group.updateTime = updateDate;
                                group.ziten_updT_List = new RealmList<Ziten>();
                            }
                        });
                        setGroupList();
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
    }



    public void setGroupList(){
        //Read Realm
        RealmResults<Group> results = realm.where(Group.class).findAll();
        List<Group> items = realm.copyFromRealm(results);

//        if (checkbox_status == 0){
//            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_edit, null);
//            action_button.setImageDrawable(drawable);
//        }
//        else if (checkbox_status == 1){
//            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_delete, null);
//            action_button.setImageDrawable(drawable);
//        }

//        ZitenAdapter adapter = new ZitenAdapter(containerp.getContext(), R.layout.home_item, items,checkbox_status);
        ZitenGroupAdapter adapter = new ZitenGroupAdapter(getActivity(), R.layout.group_item, items,checkbox_status);
        listView.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        checkbox_status = 0;
        setGroupList();
    }

    @Override
    //protected
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    public void create(View view){
        Log.d("GroupActivity","groupActivity -> groupcreateActivity");
//        Intent intent = new Intent(containerp.getContext(), GroupCreateActivity.class);
//        startActivity(intent);
//        FragmentManager fragmentManager;
//        Fragment fragment = new GroupCreateActivity();
    }


    public void delete(final Group delete_item){
//        setGroupList();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for(int i = 0; i < delete_item.ziten_updT_List.size(); ++i) {
                     Ziten checked_pice = delete_item.ziten_updT_List.get(i);
                    Ziten realmZiten = realm.where(Ziten.class).equalTo("updateTime", checked_pice.updateTime).findFirst();
                    realmZiten.deleteFromRealm();
                }
                Group delete_group = realm.where(Group.class).equalTo("updateTime",delete_item.updateTime).findFirst();
                delete_group.deleteFromRealm();
            }
        });
        setGroupList();
    }


    public void all(View v){
        Log.d("cliked!","all!");
//        Intent intent = new Intent(getApplicationContext(), GroupPiceActivity.class);
//        //booleannnnnn
//        intent.putExtra("all_boolean",true);
//        startActivity(intent);
    }


    public void onClickBtn(View v){
            Log.d("GroupActivity","onClickBtn");
    }


    //URI get -> URI > PATH return
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("===onActivityResult===","==========onActivityResult=====");
        Log.d("data",data.getDataString());
        Log.d("requestsCode", String.valueOf(requestCode));
        try {
            //====file get contents =====
            if (requestCode == 10 && resultCode == getActivity().RESULT_OK) {
                String filePath = data.getDataString().replace("content://", "");
                String decodedfilePath = URLDecoder.decode(data.getDataString(), "utf-8");

                Uri uri = Uri.parse(decodedfilePath);
                Log.d("PATH1", uri.toString()+"");
//                Log.d("pathdaaaaaa",getPathFromUri(getContext(),uri));
                gsonRealmOpen(getPathFromUri(getContext(),uri));

                //===file create ====
            }else if(requestCode == 11 && resultCode == getActivity().RESULT_OK){
                Uri fileuri = Uri.parse(data.getDataString());
                String filepath = getPathFromUri(getContext(),fileuri);
                gsonRealmSave(filepath);
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
                        Toast.makeText(getContext(),"ファイルが正常に読み込めました！",Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (NullPointerException e){
                    Toast.makeText(getContext(),"ファイルが読めません。",Toast.LENGTH_SHORT).show();
                }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"ファイルがありません。",Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void gsonRealmSave(String filepath){
        try (JsonWriter writer = new JsonWriter(new FileWriter(filepath))) {
            writer.setIndent(" ");
            Gson gson = new Gson();
            gson.toJson(createGroup, Group.class, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //==========Permission=============

//https://developer.android.com/training/permissions/requesting#perm-check
    private boolean checkPermission(Context context, String permission) {
        final PackageManager pm = getContext().getPackageManager();
        return pm.checkPermission(permission, getContext().getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }

    //パーミッションの根拠と要求（API 23 [Android 6.0]）
//https://developer.android.com/training/permissions/requesting#make-the-request
    private void showPermissionRationaleAndRequest(final String permission, final int requestCode) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {  //Android 6.0

            //パーミッション要求の根拠の説明を必要とするか？「今後表示しない」をチェックすると常に false になる。
            boolean isShouldRationale = shouldShowRequestPermissionRationale(permission); //API 23 (Android 6.0)
            if (isShouldRationale) {
                //根拠の説明ダイアログを必要とするとき
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());  //this は Context //※この実装では画面回転で消えるので注意
                builder.setTitle("以下の理由でストレージの権限が必要です。")
                        .setMessage("・jsonファイルで出力\n・jsonファイルをインポート")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission(permission, requestCode);
                            }
                        })
                        .show();
            } else {
                //根拠の説明ダイアログ不要のとき
                requestPermission(permission, requestCode);
            }
        }
    }

    //パーミッションを要求する（API 23 [Android 6.0]）
    private void requestPermission(String permission, int requestCode) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {  //Android 6.0
            requestPermissions(new String[]{ permission }, requestCode);
        }
    }

    //パーミッション要求の結果コールバックハンドラ（API 23 [Android 6.0]）
//https://developer.android.com/training/permissions/requesting#handle-response
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFileAcitivity();
                    Toast.makeText(getContext(), "ユーザーによってストレージ読み書き権限が許可されました。", Toast.LENGTH_SHORT).show(); //this は Context
                } else {
                    Toast.makeText(getContext(), "ユーザーによってストレージ読み書き権限が拒否されました。", Toast.LENGTH_SHORT).show(); //this は Context
                }
                return;
            }
        }
    }

    public void openFileAcitivity(){
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("*/*");
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, createGroup.groupName+".json");
        startActivityForResult(intent, 11);
    }






}
