package com.naats.naatkidunya.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.naats.naatkidunya.R;

public class AdminContactActivity extends AppCompatActivity {

    private Button phone_number, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_admin_contact);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        phone_number = findViewById(R.id.phone_number);
        button3 = findViewById(R.id.button3);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opneFacebookProfile(AdminContactActivity.this);
            }
        });

        phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: +923094172404"));
                startActivity(intent);
            }
        });

    }

    public static void opneFacebookProfile(Activity activity) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = "https://www.facebook.com/tehreekelabbik.lahore.9";
        facebookIntent.setData(Uri.parse(facebookUrl));
        activity.startActivity(facebookIntent);
    }
}
