package com.naats.naatkidunya.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.naats.naatkidunya.R;
import com.naats.naatkidunya.SharedPref.AppPreferences;
import com.naats.naatkidunya.SharedPref.PreferenceManager;
import com.naats.naatkidunya.adapter.AllNaatKhawanAdapter;
import com.naats.naatkidunya.adapter.FavouriteAdapter;
import com.naats.naatkidunya.model.NaatsModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements AllNaatKhawanAdapter.DownloadInterface {

    private RecyclerView favRecyclerView;
    private TextView title_fav;
    private AppPreferences appPreferences;
    private List<NaatsModel> listNaats;
    private FavouriteAdapter favouriteAdapter;
    private Toolbar toolbar;
    private Typeface appNameTypeFace;
    private TextView app_name;
    private AdView mAdView, mAdView2;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        favRecyclerView = findViewById(R.id.song_list);
        title_fav = findViewById(R.id.title_fav);

        toolbar = findViewById(R.id.toolbar);
        appNameTypeFace = Typeface.createFromAsset(this.getAssets(), "fonts/Jameel Noori Nastaleeq Kasheeda.ttf");
        app_name = toolbar.findViewById(R.id.app_name);
        app_name.setTypeface(appNameTypeFace);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        builder = new AlertDialog.Builder(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView2 = findViewById(R.id.adView1);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);


        AdRequest adRequest1 = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        PreferenceManager preferenceManager = new PreferenceManager(sharedPreferences);
        appPreferences = new AppPreferences(preferenceManager);
        fetchData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void fetchData() {
        List<NaatsModel> storyList = appPreferences.getFavouriteCardList();
        if (storyList != null && storyList.size() > 0) {
            showNoFavtText(false);
            favRecyclerView.setHasFixedSize(true);
            favRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            listNaats = new ArrayList<>();

            List<String> naatName = new ArrayList<>();
            for (NaatsModel naats : listNaats) {
                naatName.add(naats.getName());
            }

            favouriteAdapter = new FavouriteAdapter(FavoriteActivity.this, storyList, naatName, appPreferences, this::onHandleDownload);
            favRecyclerView.setAdapter(favouriteAdapter);
        } else {
            showNoFavtText(true);
        }
    }

    private void showNoFavtText(boolean show) {
        title_fav.setVisibility(show ? View.VISIBLE : View.GONE);
        favRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onHandleDownload(int position) {
        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to Download this Naat in your Mobile?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int permissionCheck = ContextCompat.checkSelfPermission(FavoriteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        int WRITE_EXTERNAL_STORAGE = 0;
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) FavoriteActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                        } else {
                            String url = listNaats.get(position).getUrl();
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setDescription("Downloading . . .");
                            request.setTitle(listNaats.get(position).getName());
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
                            // close streams
                            try {
                                output.flush();
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + File.separator + "NaatKiDunya", listNaats.get(position).getName() + ".mp3");
                            DownloadManager manager = (DownloadManager) FavoriteActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Toast.makeText(FavoriteActivity.this, "Download Cancel ...!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Download");
        alert.show();
    }
}