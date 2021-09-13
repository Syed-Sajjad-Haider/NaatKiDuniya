package com.naats.naatkidunya.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.naats.naatkidunya.BuildConfig;
import com.naats.naatkidunya.Flags;
import com.naats.naatkidunya.R;
import com.naats.naatkidunya.SharedPref.ExitDialogClass;
import com.naats.naatkidunya.adapter.NavigationAdapter;
import com.naats.naatkidunya.model.NaatKhwanModel;

import io.paperdb.Paper;

public class HomeScreenActivity extends AppCompatActivity {
    private LinearLayout allNaatKhawan, bestNaats, favNaats, naat_ring_tones, share_of_App, myAccount;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Toolbar toolbar;
    private Typeface appNameTypeFace;
    private TextView app_name;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private AdView mAdView, mAdView2;
    private static final String TAG = HomeScreenActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_navigation_drawer);
        Paper.init(this);
//        Paper.book().write(Flags.flag,"0");

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

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        setupToolbar();

        NaatKhwanModel[] drawerItem = new NaatKhwanModel[8];

        drawerItem[0] = new NaatKhwanModel(R.mipmap.home, "Home");
        drawerItem[1] = new NaatKhwanModel(R.mipmap.ring, "Islamic Ringtones");
        drawerItem[2] = new NaatKhwanModel(R.mipmap.feedback, "User Feedback");
        drawerItem[3] = new NaatKhwanModel(R.mipmap.share, "Share App");
        drawerItem[4] = new NaatKhwanModel(R.mipmap.rate, "Rate Our App");
        drawerItem[5] = new NaatKhwanModel(R.mipmap.moreapp, "More App");
        drawerItem[6] = new NaatKhwanModel(R.mipmap.admincontact, "Contact to Admin");
        drawerItem[7] = new NaatKhwanModel(R.mipmap.exit, "Exit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationAdapter adapter = new NavigationAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
        intializLayouts();
        openActivities();

        if (isNetworkConnectionAvailable()) {
        } else {
            checkNetworkConnection();
        }

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        switch (position) {
            case 0:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(HomeScreenActivity.this, HomeScreenActivity.class);
                        startActivity(intent);
                    }
                }, 50);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 1:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(HomeScreenActivity.this, IslamicRingtonesActivity.class);
                        startActivity(intent);
                    }
                }, 50);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 2:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(HomeScreenActivity.this, UserFeedbackActivity.class);
                        startActivity(intent);
                    }
                }, 50);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;

            case 3:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shareApp();
                    }
                }, 50);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 4:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rateOfApp();
                    }
                }, 50);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 5:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openPlaystoreAccount();
                    }
                }, 50);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 6:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(HomeScreenActivity.this, AdminContactActivity.class);
                        startActivity(intent);
                    }
                }, 50);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 7:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ExitDialogClass cdd=new ExitDialogClass(HomeScreenActivity.this);
                        cdd.show();
                    }
                }, 50);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        appNameTypeFace = Typeface.createFromAsset(this.getAssets(), "fonts/Jameel Noori Nastaleeq Kasheeda.ttf");
        app_name = toolbar.findViewById(R.id.app_name);
        app_name.setTypeface(appNameTypeFace);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
    }

    public void intializLayouts() {
        allNaatKhawan = findViewById(R.id.allNaatKhawan);
        bestNaats = findViewById(R.id.bestNaats);
        favNaats = findViewById(R.id.favNaats);
        naat_ring_tones = findViewById(R.id.naat_ring_tones);
        share_of_App = findViewById(R.id.share_of_App);
        myAccount = findViewById(R.id.myAccount);

    }

    public void openActivities() {


        allNaatKhawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, AllNaatKhawanActivity.class));
                ;
            }
        });

        bestNaats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, BestNaatActivity.class));
            }
        });

        favNaats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, FavoriteActivity.class));
            }
        });

        naat_ring_tones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, IslamicRingtonesActivity.class));
            }
        });

        share_of_App.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
            }
        });

        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlaystoreAccount();
            }
        });
    }

    public void checkNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HomeScreenActivity.this.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            checkNetworkConnection();
            Log.d("Network", "Not Connected");
            return false;
        }
    }

    public void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Naat ki Dunya");
            String shareMessage = "\n Naat ki Dunya \n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
        }
    }

    public void rateOfApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    public void openPlaystoreAccount() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/account?utm_source=google&utm_medium=account&utm_campaign=my-account")));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}