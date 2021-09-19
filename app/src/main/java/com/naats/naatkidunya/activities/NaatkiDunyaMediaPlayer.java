package com.naats.naatkidunya.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.ads.AdView;

import com.naats.naatkidunya.CreateNotification;
import com.naats.naatkidunya.Playable;
import com.naats.naatkidunya.R;
import com.naats.naatkidunya.Services.onClearFromRecentServices;
import com.naats.naatkidunya.SharedPref.StorageUtil;
import com.naats.naatkidunya.SharedPref.Utilities;
import com.naats.naatkidunya.Trace;
import com.naats.naatkidunya.model.NaatsModel;

import dmax.dialog.SpotsDialog;

public class NaatkiDunyaMediaPlayer extends Activity
        implements OnCompletionListener, SeekBar.OnSeekBarChangeListener, Playable {
    List<String> myList=new ArrayList<>();

    private ImageButton btnPlay, btnForward, btnBackward, btnNext, btnPrevious, btnRepeat, btnShuffle;
    private SeekBar naatProgressBar;
    private TextView naatTitleLabel, naatCurrentDurationLabel, naatTotalDurationLabel;
    private MediaPlayer mp;
    private Handler mHandler = new Handler();
    private Utilities utils;
    private int seekForwardTime = 5000;
    private int seekBackwardTime = 5000;
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private ArrayList<NaatsModel> audioList;
    private int audioIndex = -1;
    private int position= 0;
    private AdView mAdView, mAdView2;


    private ImageView closeplayer;

    NotificationManager notificationManager;
    List<Trace> traces;
    Boolean isPlaying = false;
    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player_example);
        initializeViews();
        mediaPlayerInitialize();

//        populateTrack();
//        populateTrack();

//        btnPlayNaat();
//        btnForwardNaat();
//        btnBackwardNaat();
//        setBtnNextNaat();
//        btnPreviousNaats();
//        btnRepeatNaats();
//        btnShuffleNaats();
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            currentSongIndex = data.getExtras().getInt("songIndex");
            playSong(currentSongIndex);
        }

    }

    public void initializeViews() {
        btnPlay = findViewById(R.id.btnPlay);
        btnForward = findViewById(R.id.btnForward);
        btnBackward = findViewById(R.id.btnBackward);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        naatProgressBar = findViewById(R.id.seekbar);
        naatTitleLabel = findViewById(R.id.songTitle);
        naatTitleLabel.startAnimation(AnimationUtils.loadAnimation(NaatkiDunyaMediaPlayer.this, R.anim.translate));
        naatCurrentDurationLabel = findViewById(R.id.songCurrentDurationLabel);
        naatTotalDurationLabel = findViewById(R.id.songTotalDurationLabel);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnShuffle = findViewById(R.id.btnShuffle);

        jcPlayerView = findViewById(R.id.jcplayer);


        closeplayer = findViewById(R.id.closemediaplayer);
//        jcPlayerView.pause();
//        jcPlayerView.isPaused();



    }


    public void mediaPlayerInitialize() {
        mp = new MediaPlayer();
        utils = new Utilities();
        StorageUtil storage = new StorageUtil(getApplicationContext());
        audioList = storage.loadAudio();
        audioIndex = storage.loadAudioIndex();
        naatProgressBar.setOnSeekBarChangeListener(this); // Important
        mp.setOnCompletionListener(this); // Important
        playSong(0);
    }

    private void populateTrack(int audioIndex) {
//        NaatsModel obj = new NaatsModel();
        System.out.println("Natts Name"+audioList.get(audioIndex).getName());
        traces = new ArrayList<>();
//        for (int i=1; i<audioList.size(); i++){
//            traces.addAll(audioList.add());
            traces.add(new Trace(audioList.get(audioIndex).getName(), audioList.get(audioIndex).getName(), R.mipmap.applogo));
            System.out.println("Natts Name "+audioList);
//        }
        System.out.println("Natts Name "+audioList);

//        traces.add(new Trace(audioList.get(audioIndex).getName(), audioList.get(audioIndex).getUrl(), R.drawable.profile_img));
//        traces.add(new Trace("Natt3","Awais3",R.drawable.main_splash));
//        traces.add(new Trace("Natt4","Awais4",R.drawable.loading_icon));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CreateNotification.CHANNEL_ID_1,
                    "Naat Ki Duniya", NotificationManager.IMPORTANCE_LOW);
            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }

            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), onClearFromRecentServices.class));

//            for (int i = 0; audioList.size() < 0; i++) {
//            audioList.add(new NaatsModel());
            CreateNotification.createNotification(NaatkiDunyaMediaPlayer.this, traces.get(audioIndex)
                    , R.drawable.ic_baseline_pause_24, 1, audioList.size()-1);
            }
//        }
        }



    public void playSong(int songIndex) {
        try {
//            myList = (List<String>) getIntent().getSerializableExtra("list");

            System.out.println("Natts are "+ myList);
//            mp.reset();
//            mp.setDataSource(audioList.get(audioIndex).getUrl());
//            mp.prepare();
//            mp.start();
//            String songTitle = audioList.get(audioIndex).getName();
//
//            naatTitleLabel.setText(songTitle);

            int i;
//                jcAudios.add(JcAudio.createFromURL(audioList.get(songIndex).getName(),audioList.get(audioIndex).getUrl()));
                for( i =0; i<audioList.size(); i++){
                    jcAudios.add(JcAudio.createFromURL(audioList.get(i).getName(),audioList.get(i).getUrl()));
                }

            System.out.println("Natts are "+ jcAudios);
            jcPlayerView.initPlaylist(jcAudios,null);
            jcPlayerView.playAudio(jcAudios.get(audioIndex));
            jcPlayerView.setVisibility(View.VISIBLE);
            jcPlayerView.createNotification(R.mipmap.applogo);
            closeplayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
//                    jcPlayerView.pause();
//                    jcPlayerView.isPaused();
                    onBackPressed();
                    Toast.makeText(v.getContext(), "Media Player Close", Toast.LENGTH_SHORT).show();

                }
            });
//          jcAudios.add(JcAudio.createFromURL(audioList.get(audioIndex).getName(),audioList.get(audioIndex).getUrl()));
//            jcPlayerView.initPlaylist(jcAudios,null);
//            jcPlayerView.playAudio(jcAudios.get(audioIndex));
//            naatTitleLabel.setText(jcAudios.get(audioIndex).getTitle());

//            jcPlayerView.setVisibility(View.VISIBLE);
//            jcPlayerView.createNotification();
//            jcAudios.add(JcAudio.createFromURL(audioList.get(audioIndex).getName(),
//                    audioList.get(songIndex).getUrl()));
//            jcPlayerView.initPlaylist(jcAudios,null);
//
//            jcPlayerView.playAudio(jcAudios.get(audioIndex));
//            jcPlayerView.setVisibility(View.VISIBLE);
//            jcPlayerView.createNotification();

//            btnPlay.setImageResource(R.drawable.btn_pause);
//            naatProgressBar.setProgress(0);
//            naatProgressBar.setMax(100);
//
//
//            jcPlayerView.playAudio(jcAudios.get(songIndex));
//            jcPlayerView.setVisibility(View.VISIBLE);
//            jcPlayerView.createNotification();

//            ArrayList<JcAudio> jcAudios = new ArrayList<>();
////            jcAudios.add();
//            for (int i = 0; i < jcAudios.size(); i++) {
//
//                jcAudios.add(JcAudio.createFromURL(audioList.get(audioIndex).getName(),
//                        audioList.get(audioIndex).getUrl()));
//                jcPlayerView.createNotification();
//            }
//            updateProgressBar();


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    public void btnPlayNaat() {
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (mp.isPlaying()) {
                    if (mp != null) {
                        mp.pause();
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                } else {

                    if (mp != null) {
                        mp.start();
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                }

            }
        });
    }

    public void btnForwardNaat() {
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int currentPosition = mp.getCurrentPosition();
                if (currentPosition + seekForwardTime <= mp.getDuration()) {
                    mp.seekTo(currentPosition + seekForwardTime);
                } else {
                    mp.seekTo(mp.getDuration());
                }
            }
        });

    }

    public void btnBackwardNaat() {
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int currentPosition = mp.getCurrentPosition();
                if (currentPosition - seekBackwardTime >= 0) {
                    mp.seekTo(currentPosition - seekBackwardTime);
                } else {
                    mp.seekTo(0);
                }

            }
        });
    }

    public void setBtnNextNaat() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp.reset();
                    mp.setDataSource(audioList.get(audioIndex++).getUrl());
                    mp.prepareAsync();
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
//                            populateTrack(audioIndex);

                        }
                    });

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        });
    }

    public void btnPreviousNaats() {
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    mp.reset();
                    mp.setDataSource(audioList.get(audioIndex--).getUrl());
                    mp.prepareAsync();
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    mp.stop();
                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        });

    }

    public void btnRepeatNaats() {
        btnRepeat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if (isRepeat) {
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                } else {
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }
            }
        });
    }

    public void btnShuffleNaats() {
        btnShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isShuffle) {
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                } else {
                    // make repeat to true
                    isShuffle = true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }
            }
        });
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();
            naatTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
            naatCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            naatProgressBar.setProgress(progress);
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
        mp.seekTo(currentPosition);
        updateProgressBar();
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        if (isRepeat) {
            playSong(currentSongIndex);
        } else if (isShuffle) {
            Random rand = new Random();
            currentSongIndex = rand.nextInt((audioList.size() - 1) - 0 + 1) + 0;
            playSong(currentSongIndex);
        } else {
            if (currentSongIndex < (audioList.size() - 1)) {
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            } else {
                playSong(0);
                currentSongIndex = 0;
            }
        }
    }


    // edit by me
    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationManager.cancelAll();
//        }
//        unregisterReceiver(broadcastReceiver);
        mp.stop();
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");
            switch (action) {
                case CreateNotification.ACTION_PREVIOUS:
//                    audioIndex--;
//                    btnPreviousNaats();
                    onTrackPrevious();
                    break;

                case CreateNotification.ACTION_PLAY:
                    if (isPlaying) {
//                        onPause();
                        onTrackPause();
                    } else {

                        onTrackPlay();
                    }
                    break;
                case CreateNotification.ACTION_NEXT:
//                    setBtnNextNaat();
//                    audioIndex++;
//                    populateTrack(audioIndex);
                    onTrackNext();
                    break;

            }
        }
    };

    @Override
    public void onTrackPrevious() {
//        for (int i = 0; traces.size() > 0; i++) {
        audioIndex--;
            CreateNotification.createNotification(NaatkiDunyaMediaPlayer.this, traces.get(audioIndex),
                    R.drawable.btn_pause, audioIndex, traces.size() - 1);
//            naatTitleLabel.setText(traces.get(i).getTitle());
//        }

    }

    @Override
    public void onTrackPlay() {
//        for (int i = 0; traces.size() > 0; i++) {
            CreateNotification.createNotification(NaatkiDunyaMediaPlayer.this, traces.get(audioIndex),
                    R.drawable.btn_pause, audioIndex, traces.size() - 1);
//            btnPlay.setImageResource(R.drawable.ic_baseline_pause_24);

//           naatTitleLabel.setText(traces.get(i).getTitle());

            isPlaying = true;
//        }
    }

    @Override
    public void onTrackPause() {

//        for (int i = 0; traces.size() > 0; i++) {
            CreateNotification.createNotification(NaatkiDunyaMediaPlayer.this, traces.get(audioIndex),
                    R.drawable.btn_pause, audioIndex, traces.size() - 1);

            btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);

//           naatTitleLabel.setText(traces.get(i).getTitle());
            isPlaying = false;

//        }
    }

    @Override
    public void onTrackNext() {
//        for (int i = 0; audioList.size() > 0; i++) {
            audioIndex++;
            CreateNotification.createNotification(NaatkiDunyaMediaPlayer.this, traces.get(audioIndex),
                    R.drawable.btn_pause, audioIndex, traces.size() - 1);
//            setBtnNextNaat();
//           naatTitleLabel.setText(traces.get(i).getTitle());
//        }

    }
}