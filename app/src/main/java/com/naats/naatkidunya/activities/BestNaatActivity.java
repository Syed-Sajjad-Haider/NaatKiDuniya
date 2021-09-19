package com.naats.naatkidunya.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.naats.naatkidunya.R;
import com.naats.naatkidunya.SharedPref.AppPreferences;
import com.naats.naatkidunya.SharedPref.PreferenceManager;
import com.naats.naatkidunya.adapter.BestNaatsAdapter;
import com.naats.naatkidunya.model.NaatsModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

import dmax.dialog.SpotsDialog;

public class BestNaatActivity extends AppCompatActivity implements BestNaatsAdapter.DownloadInterface {

    private RecyclerView naatslist;
    private List<NaatsModel> songs;
    private MediaPlayer mediaPlayer;
    private BestNaatsAdapter adapter;
    private Toolbar toolbar;
    private Typeface appNameTypeFace;
    private TextView app_name;
    private android.app.AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private AdView mAdView,mAdView2;
    private FirebaseFirestore db;
    AdRequest adRequest;


    ProgressDialog progress;

    private List<String> arraylistUrl=new ArrayList<>();

    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_naat);


        progress = new ProgressDialog(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        PRDownloader.initialize(getApplicationContext());

        toolbar = findViewById(R.id.toolbar);
        appNameTypeFace = Typeface.createFromAsset(this.getAssets(), "fonts/Jameel Noori Nastaleeq Kasheeda.ttf");
        app_name = toolbar.findViewById(R.id.app_name);
        app_name.setTypeface(appNameTypeFace);

        jcPlayerView=(JcPlayerView)findViewById(R.id.jcplayer);

        builder = new AlertDialog.Builder(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        alertDialog = new SpotsDialog.Builder().setContext(BestNaatActivity.this).build();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });



//        jcPlayerView=(JcPlayerView)findViewById(R.id.jcplayer);



        naatslist = findViewById(R.id.song_list);
        songs = new ArrayList<>();
        naatslist.setHasFixedSize(true);
        naatslist.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        PreferenceManager preferenceManager = new PreferenceManager(sharedPreferences);
        AppPreferences appPreferences = new AppPreferences(preferenceManager);

//        alertDialog.show();

        progress.setTitle("Best Naats");
        progress.setMessage("Please Wait, Naats Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        progress.setCancelable(false);


        Toast.makeText(this, "Please Wait Naats are Loading...",Toast.LENGTH_SHORT).show();
        db = FirebaseFirestore.getInstance();
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        db.collection("BestNaats")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        alertDialog.hide();
                        progress.dismiss();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            songs.addAll(queryDocumentSnapshots.toObjects(NaatsModel.class));

                            List<String> songsName = new ArrayList<>();
                            for (NaatsModel songs : songs)
                            {
                                songsName.add(songs.getName());
                                arraylistUrl.add(songs.getUrl());
                                jcAudios.add(JcAudio.createFromURL(songs.getName(),songs.getUrl()));
                            }
                            adapter = new BestNaatsAdapter(BestNaatActivity.this, songs, songsName,appPreferences, BestNaatActivity.this::onHandleDownload);
                            jcPlayerView.initPlaylist(jcAudios,null);
                            System.out.println("names "+songs);
//                            Intent intent=new Intent(getApplicationContext(),NaatkiDunyaMediaPlayer.class);
//                            intent.putExtra("list", String.valueOf(songsName));
//                            startActivity(intent);
                            naatslist.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }
                    }
                });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onHandleDownload(int position) {
        builder.setMessage("Do you want to Download this Naat in your Mobile?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkpermissions();
                        downloadnaat(position);


//                        downloadfile(getApplicationContext(),songs.get(position).getName(), ".mp3",DIRECTORY_DOWNLOADS,songs.get(id).getUrl());


//                        int permissionCheck = ContextCompat.checkSelfPermission(BestNaatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                        int WRITE_EXTERNAL_STORAGE = 0;
//                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions((Activity) BestNaatActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
//                        } else {
//                            String url = songs.get(position).getUrl();
//                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                            request.setDescription("Downloading . . .");
//                            request.setTitle(songs.get(position).getName());
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                                request.allowScanningByMediaScanner();
//                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                            }
//                            String path = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";
//                            ;
//                            OutputStream output = null;
//                            try {
//                                output = new FileOutputStream(path);
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            }
//                            try {
//                                output.flush();
//                                output.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + File.separator + "NaatKiDunya", songs.get(position).getName() + ".mp3");
//                            DownloadManager manager = (DownloadManager) BestNaatActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
//
//                            manager.enqueue(request);


//                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(BestNaatActivity.this, "Download Cancel ...!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Download");
        alert.show();
    }

    private void checkpermissions()
    {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport)
            {
                if(multiplePermissionsReport.areAllPermissionsGranted())
                {
                    Toast.makeText(BestNaatActivity.this,"All Permissions Granted, Thanks",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(BestNaatActivity.this,"Please Allow All Permissions",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }

       }).check();
    }

    private void downloadnaat(int position)
    {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Downloading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        File file=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        PRDownloader.download(songs.get(position).getUrl(), file.getPath(), songs.get(position).getName())
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {

                    @Override
                    public void onProgress(Progress progress)
                    {
                        long down=progress.currentBytes * 100 / progress.totalBytes;
                        progressDialog.setMessage("Downloading : "+down+" %");

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete()
                    {
                        progressDialog.dismiss();
                        Toast.makeText(BestNaatActivity.this,"Downloading Completed",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Error error)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(BestNaatActivity.this,"Errors",Toast.LENGTH_SHORT).show();

                    }

                });

    }

    public void downloadfile(Context context, String filename, String fileextension, String destination, String url)
    {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request= new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destination,filename+fileextension);
        downloadManager.enqueue(request);
    }

}
