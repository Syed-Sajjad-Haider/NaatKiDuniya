package com.naats.naatkidunya.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.naats.naatkidunya.R;
import com.naats.naatkidunya.SharedPref.AppPreferences;
import com.naats.naatkidunya.SharedPref.PreferenceManager;
import com.naats.naatkidunya.SharedPref.StorageUtil;
import com.naats.naatkidunya.Trace;
import com.naats.naatkidunya.adapter.AllNaatKhawanAdapter;
import com.naats.naatkidunya.model.NaatsModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class NaatKhawanNaatsActivity extends AppCompatActivity implements AllNaatKhawanAdapter.CallbackInterface, AllNaatKhawanAdapter.DownloadInterface {

    private RecyclerView songslist;
    private List<NaatsModel> songs;
    private List<String> arraylistUrl=new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private AllNaatKhawanAdapter adapter;
    private String key = "Music";
    private String key1 = "Music";
    private String title_name;
    private android.app.AlertDialog alertDialog;

    private Toolbar toolbar;
    private Typeface appNameTypeFace;
    private TextView app_name;
    private static final int MY_REQUEST = 1001;
    private AlertDialog.Builder builder;
    private AdView mAdView, mAdView2;
    FirebaseFirestore db;
    AppPreferences appPreferences;
//    String flag;

    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (getIntent().getExtras() != null)
            key = getIntent().getExtras().getString("key");
//            key1 = getIntent().getExtras().getString("title");

        toolbar = findViewById(R.id.toolbar);
        appNameTypeFace = Typeface.createFromAsset(this.getAssets(), "fonts/Jameel Noori Nastaleeq Kasheeda.ttf");
        app_name = toolbar.findViewById(R.id.app_name);
        app_name.setTypeface(appNameTypeFace);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView2 = findViewById(R.id.adView1);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

        title_name = getIntent().getExtras().getString("title");

        app_name.setText(title_name);
        alertDialog = new SpotsDialog.Builder().setContext(NaatKhawanNaatsActivity.this).build();

        songslist = findViewById(R.id.song_list);
        songs = new ArrayList<>();
        songslist.setHasFixedSize(true);
        songslist.setLayoutManager(new LinearLayoutManager(this));

        jcPlayerView=(JcPlayerView)findViewById(R.id.jcplayer);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        PreferenceManager preferenceManager = new PreferenceManager(sharedPreferences);
        appPreferences = new AppPreferences(preferenceManager);

        db = FirebaseFirestore.getInstance();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        naatsOfNaatkhawan();
        alertDialog = new SpotsDialog.Builder().setContext(NaatKhawanNaatsActivity.this).build();
//        alertDialog.show();

        Toast.makeText(this, "Please Wait Naats are Loading...",Toast.LENGTH_SHORT).show();
//        songslist.addOnItemTouchListener(Context );

    }

    public void naatsOfNaatkhawan() {

        db.collection(key)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        alertDialog.hide();
//                        Trace obj = new Trace();

                        if (!queryDocumentSnapshots.isEmpty()) {

                            songs.addAll(queryDocumentSnapshots.toObjects(NaatsModel.class));

                            List<String> songsName = new ArrayList<>();
//                            List<String> songsUrl = new ArrayList<>();
                            for (NaatsModel songs : songs)
                            {
                                songsName.add(songs.getName());
                                arraylistUrl.add(songs.getUrl());
                                jcAudios.add(JcAudio.createFromURL(songs.getName(),songs.getUrl()));
                            }
                            adapter = new AllNaatKhawanAdapter(NaatKhawanNaatsActivity.this, songs, songsName, appPreferences, NaatKhawanNaatsActivity.this::onHandleDownload);
                            jcPlayerView.initPlaylist(jcAudios,null);
                            songslist.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        Intent intent = new Intent(NaatKhawanNaatsActivity.this, AllNaatKhawanActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        NaatKhawanNaatsActivity.this.finish();
//        return true;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        alertDialog.hide();
        switch (resultCode) {

            case RESULT_OK:
                if (requestCode == MY_REQUEST) {
                    // .. lets toast again
                    int position = -1;
                    if (data != null) {
                        position = data.getIntExtra("Position", 0);
                    }

                    if (position != -1) {
                        Toast.makeText(this, "Handled the result successfully at position " + position, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to get data from intent", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case RESULT_CANCELED:
                break;
        }
    }

    @Override
    public void onHandleSelection(int positiont) {
        StorageUtil storage = new StorageUtil(this);
        storage.storeAudio(songs);
        storage.storeAudioIndex(positiont);
        jcPlayerView.playAudio(jcAudios.get(positiont));
        jcPlayerView.setVisibility(View.VISIBLE);
        jcPlayerView.createNotification();
//        Intent playerIntent = new Intent(NaatKhawanNaatsActivity.this, NaatkiDunyaMediaPlayer.class);
//        startActivityForResult(playerIntent, MY_REQUEST);
        Toast.makeText(NaatKhawanNaatsActivity.this, "Please Wait Naat is Opening",Toast.LENGTH_LONG).show();


//        builder.setMessage("Do you want to Listen this Naat")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent playerIntent = new Intent(NaatKhawanNaatsActivity.this, NaatkiDunyaMediaPlayer.class);
//                        startActivityForResult(playerIntent, MY_REQUEST);
//
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //  Action for 'NO' Button
//                        dialog.cancel();
//                        Toast.makeText(NaatKhawanNaatsActivity.this, "Cancel ...!",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//        //Creating dialog box
//        AlertDialog alert = builder.create();
//        //Setting the title manually
//        alert.setTitle("Listen");
//        alert.show();

    }
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Paper.book().write(flag,"1");
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(NaatKhawanNaatsActivity.this, AllNaatKhawanActivity.class);
            finish();
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onHandleDownload(int position) {

        builder.setMessage("Do you want to Download this Naat in your Mobile?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int permissionCheck = ContextCompat.checkSelfPermission(NaatKhawanNaatsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        int WRITE_EXTERNAL_STORAGE = 0;
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) NaatKhawanNaatsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                        } else {
                            String url = songs.get(position).getUrl();
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setDescription("Downloading . . .");
                            request.setTitle(songs.get(position).getName());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            }
                            String path = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";
                            ;
                            OutputStream output = null;
                            try {
                                output = new FileOutputStream(path);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            try {
                                output.flush();
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + File.separator + "NaatKiDunya", songs.get(position).getName() + ".mp3");
                            DownloadManager manager = (DownloadManager) NaatKhawanNaatsActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(NaatKhawanNaatsActivity.this, "Download Cancel ...!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Download");
        alert.show();
    }
}


