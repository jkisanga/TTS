package tzchoice.kisanga.joshua.tts;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import tzchoice.kisanga.joshua.tts.Fragments.HomeFragment;
import tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler;
import tzchoice.kisanga.joshua.tts.Helper.SessionManager;
import tzchoice.kisanga.joshua.tts.Pojo.RefAction;
import tzchoice.kisanga.joshua.tts.Pojo.Tp;

import static tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler.KEY_CHECKPOINT;
import static tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler.KEY_CHECKPOINT_ID;

public class MainActivity extends AppCompatActivity  implements HomeFragment.OnDataPass {
    private static final String TAG = "MainActivity";
    public static final String EXPIRE_COUNTER = "expireCounter";
    public static final String WAITING_COUNTER = "waitingCounter";
    private Toolbar toolbar;
    private PagerSlidingTabStrip tabLayout;
    private ViewPager viewPager;
    private SQLiteHandler db;
    private SessionManager session;
    private String fileName;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    TextView txtChangeLogContent;
    FragmentPagerAdapter adapter;

    private ProgressDialog pDialog;
    private  ArrayList<ViewPagerTab> tabsList;
    SharedPreferences.Editor editor, expire;
    @Override
    public void onDataPass(String data) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("loading data ...");
        showDialog();

         editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        expire = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        ListData();
        countExpire();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        int cuntWait = prefs.getInt(WAITING_COUNTER, 0); //0 is the default value.
        int expireCounter = prefs.getInt(EXPIRE_COUNTER, 0); //0 is the default value.


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String TTSTitle = "TTS - " + db.getUserDetails().get(KEY_CHECKPOINT);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        txtChangeLogContent = (TextView) findViewById(R.id.txt_change_log_content);

        getSupportActionBar().setTitle(TTSTitle);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (PagerSlidingTabStrip) findViewById(R.id.tabs);


         tabsList = new ArrayList<>();

        tabsList.add(new ViewPagerTab("Waiting", expireCounter));
        tabsList.add(new ViewPagerTab("Expired",  cuntWait));
        //tabsList.add(new ViewPagerTab("Licenses", 1));

        adapter = new MainAdapter(getSupportFragmentManager(), tabsList);
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);

        feedAction();
        feedIrregularities();
        feedProducts();
        feedUnits();

    }

    private void countExpire() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
        Call<List<Tp>> call = service.getTPExpired(Integer.parseInt(db.getUserDetails().get(KEY_CHECKPOINT_ID)));
        call.enqueue(new Callback<List<Tp>>() {
            @Override
            public void onResponse(Response<List<Tp>> response, Retrofit retrofit) {
                try {

                    List<Tp> tps = response.body();

                    expire.putInt(EXPIRE_COUNTER, tps.size());
                    expire.commit();


                }catch (Exception e){
                    e.printStackTrace();
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void ListData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
        Call<List<Tp>> call = service.getTPasses(Integer.parseInt(db.getUserDetails().get(KEY_CHECKPOINT_ID)));
        call.enqueue(new Callback<List<Tp>>() {
            @Override
            public void onResponse(Response<List<Tp>> response, Retrofit retrofit) {
                hideDialog();
                try {

                    List<Tp> tps = response.body();

                    editor.putInt(WAITING_COUNTER, tps.size());
                    editor.commit();
                    hideDialog();
                }catch (Exception e){
                    e.printStackTrace();
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                hideDialog();
            }
        });
    }



    private void feedAction() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        try {

            Call<List<RefAction>> call = service.getActions();
            call.enqueue(new Callback<List<RefAction>>() {
                @Override
                public void onResponse(Response<List<RefAction>> response, Retrofit retrofit) {


                    if(response.isSuccess()) {

                      List<RefAction>   refAction = response.body();
                        for(int i =0; i < refAction.size(); i++){
                            db.addAction(refAction.get(i).getId(), refAction.get(i).getName());
                        }


                    }else{

                    }

                }

                @Override
                public void onFailure(Throwable t) {


                }
            });
        }catch (Exception e){
            Log.d("testRes", "onResponse: "+ e.toString());
        }

    }
   private void feedUnits() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        try {

            Call<List<RefAction>> call = service.getUnits();
            call.enqueue(new Callback<List<RefAction>>() {
                @Override
                public void onResponse(Response<List<RefAction>> response, Retrofit retrofit) {


                    if(response.isSuccess()) {

                      List<RefAction>   refAction = response.body();
                        for(int i =0; i < refAction.size(); i++){
                            db.addUnits(refAction.get(i).getId(), refAction.get(i).getName());
                        }


                    }else{

                    }

                }

                @Override
                public void onFailure(Throwable t) {


                }
            });
        }catch (Exception e){
            Log.d("testRes", "onResponse: "+ e.toString());
        }

    }

    private void feedIrregularities() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        try {

            Call<List<RefAction>> call = service.getIrregularities();
            call.enqueue(new Callback<List<RefAction>>() {
                @Override
                public void onResponse(Response<List<RefAction>> response, Retrofit retrofit) {


                    if(response.isSuccess()) {

                      List<RefAction>   refAction = response.body();
                        for(int i =0; i < refAction.size(); i++){

                            db.addIrregularities(refAction.get(i).getId(),refAction.get(i).getName());
                        }


                    }else{

                    }

                }

                @Override
                public void onFailure(Throwable t) {

                    Log.d("testFa", "onFailure: " + t.getStackTrace());

                }
            });
        }catch (Exception e){
            Log.d("testRes", "onResponse: "+ e.toString());
        }

    }

    private void feedProducts() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        try {

            Call<List<RefAction>> call = service.getProducts();
            call.enqueue(new Callback<List<RefAction>>() {
                @Override
                public void onResponse(Response<List<RefAction>> response, Retrofit retrofit) {


                    if(response.isSuccess()) {

                      List<RefAction>   refAction = response.body();
                        for(int i =0; i < refAction.size(); i++){

                            db.addProducts(refAction.get(i).getId(),refAction.get(i).getName());
                        }


                    }else{

                    }

                }

                @Override
                public void onFailure(Throwable t) {



                }
            });
        }catch (Exception e){

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        switch (id){

            case R.id.action_settings:
                logoutUser();
                return true;
            case R.id.action_download:
                //        Here you can get download url link
                if (Build.VERSION.SDK_INT >= 23)
                {
                    if (checkPermission())
                    {
                        downloadInstall();
                    } else {
                        requestPermission(); // Code for permission
                    }
                }
                else
                {
                    downloadInstall();
                    // Code for Below 23 API Oriented Device
                    // Do next code
                }

                return true;
            case R.id.action_install:
                install();
                return true;
            case R.id.action_about_us:
                AssetManager assetManager = getAssets();
                // To load text file
                InputStream input;
                try {
                    input = assetManager.open("change_log.txt");

                    int size = input.available();
                    byte[] buffer = new byte[size];
                    input.read(buffer);
                    input.close();

                    // byte buffer into a string
                    String text = new String(buffer);

                    txtChangeLogContent.setText(text);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void downloadInstall(){

        String url = "http://tts.tfs.go.tz/download_apk";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("tts");
        request.setTitle("title");
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }



        Random rand = new Random();

        int  n = rand.nextInt(100000) + 1;
        fileName = n + "tts.apk";

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);


    }

    private void install() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + fileName)),
                "application/vnd.android.package-archive");
        startActivity(intent);
        finish();

    }


    private void logoutUser() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to sign off?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        //Starting login activity
                        session.setLogin(false);

                        db.deleteUsers();

                        // Launching the login activity
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



}
