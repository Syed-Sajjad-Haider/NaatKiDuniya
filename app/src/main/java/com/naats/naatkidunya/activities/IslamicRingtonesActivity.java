package com.naats.naatkidunya.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.naats.naatkidunya.R;
import com.naats.naatkidunya.SharedPref.AppPreferences;
import com.naats.naatkidunya.SharedPref.PreferenceManager;
import com.naats.naatkidunya.adapter.RingTonesAdapter;
import com.naats.naatkidunya.model.NaatsModel;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class IslamicRingtonesActivity extends AppCompatActivity {
    private RecyclerView naats_recyclerView;
    private List<NaatsModel> naatsModelsList;
    private MediaPlayer mediaPlayer;
    private RingTonesAdapter ringTonesAdapter;
    private android.app.AlertDialog alertDialog;
    private Toolbar toolbar;
    private Typeface appNameTypeFace;
    private TextView app_name;
    private AdView mAdView,mAdView2;

    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();

    private List<String> arraylistUrl=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_islamic_ringtones);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            jcPlayerView=(JcPlayerView)findViewById(R.id.jcplayer);
        }
        alertDialog = new SpotsDialog.Builder().setContext(IslamicRingtonesActivity.this).build();
        toolbar = findViewById(R.id.toolbar);
        appNameTypeFace = Typeface.createFromAsset(this.getAssets(), "fonts/Jameel Noori Nastaleeq Kasheeda.ttf");
        app_name = toolbar.findViewById(R.id.app_name);
        app_name.setTypeface(appNameTypeFace);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        naats_recyclerView = findViewById(R.id.ringtones_list);
        naatsModelsList = new ArrayList<>();
        naats_recyclerView.setHasFixedSize(true);
        naats_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        PreferenceManager preferenceManager = new PreferenceManager(sharedPreferences);
        AppPreferences appPreferences = new AppPreferences(preferenceManager);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

//        alertDialog.show();
        Toast.makeText(this, "Please Wait RingTones are Loading...",Toast.LENGTH_SHORT).show();

        db.collection("IslamicRingTones")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            naatsModelsList.addAll(queryDocumentSnapshots.toObjects(NaatsModel.class));

                            List<String> songsName = new ArrayList<>();
                            for (NaatsModel songs : naatsModelsList) {
                                songsName.add(songs.getName());
                                arraylistUrl.add(songs.getUrl());
                                jcAudios.add(JcAudio.createFromURL(songs.getName(),songs.getUrl()));
                            }
                            System.out.println("names jc "+jcAudios);

                            ringTonesAdapter = new RingTonesAdapter(IslamicRingtonesActivity.this, naatsModelsList, songsName,appPreferences);
                            jcPlayerView.initPlaylist(jcAudios,null);
                            System.out.println("names "+songsName);
                            naats_recyclerView.setAdapter(ringTonesAdapter);
                            ringTonesAdapter.notifyDataSetChanged();
//                            alertDialog.hide();

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
}
