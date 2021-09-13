package com.naats.naatkidunya.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naats.naatkidunya.R;
import com.naats.naatkidunya.SharedPref.PrefManager;
import com.naats.naatkidunya.model.UserAppModel;

public class UserInfoActivity extends AppCompatActivity {

    private EditText userName, userMobileNumber, userCityName, userCountryName;
    private Button btnSignUp;
    private PrefManager prefManager;
    private AdView mAdView,mAdView2,mAdView3;
    private FirebaseDatabase database;
    private DatabaseReference apps_user;
    private String token;
    private String user_Name, user_MobileNumber,user_CityName,user_CountryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        initializView();
        bannersAds();
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

         database = FirebaseDatabase.getInstance();
         apps_user = database.getReference("User");

        btnSignUpFun();
    }

    public void initializView(){
        userName = findViewById(R.id.user_name);
        userMobileNumber = findViewById(R.id.mobile_number);
        userCityName = findViewById(R.id.city_name);
        userCountryName = findViewById(R.id.country_name);
        btnSignUp = findViewById(R.id.home);
    }

    public void bannersAds(){
        mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView2 = findViewById(R.id.adView2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

        mAdView3 = findViewById(R.id.adView3);
        AdRequest adRequest3 = new AdRequest.Builder().build();
        mAdView3.loadAd(adRequest3);
    }

    public void btnSignUpFun(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(UserInfoActivity.this);
                mDialog.setMessage("Please Wating...");
                mDialog.show();

                apps_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if user not exist in database
                        if (dataSnapshot.child(userMobileNumber.getText().toString()).exists()) {
                            //get user information
                            mDialog.dismiss();
                            UserAppModel user = dataSnapshot.child(userMobileNumber.getText().toString()).getValue(UserAppModel.class);
                            Toast.makeText(UserInfoActivity.this, "Phone number already registered !", Toast.LENGTH_SHORT).show();

                        } else {
                            mDialog.dismiss();
                            if (validate()) {
                                UserAppModel user = new UserAppModel(userName.getText().toString(), userMobileNumber.getText().toString(), userCityName.getText().toString(), userCountryName.getText().toString(), token);
                                apps_user.child(userMobileNumber.getText().toString()).setValue(user);
                                Intent welcomeActivity = new Intent(UserInfoActivity.this, WelcomeActivity.class);
                                startActivity(welcomeActivity);
                                finish();

                            } else {
                                Toast.makeText(UserInfoActivity.this, "Required some information about user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(UserInfoActivity.this, HomeScreenActivity.class));
        finish();
    }

    public boolean validate() {
        boolean valid = true;
        user_Name = userName.getText().toString();
        user_MobileNumber = userMobileNumber.getText().toString();
        user_CityName = userCityName.getText().toString();
        user_CountryName = userCountryName.getText().toString();

        if (user_Name.isEmpty()) {
            userName.setError("please enter your name");
            valid = false;
        }
        if (user_MobileNumber.isEmpty() || user_MobileNumber.length() < 10) {
            userMobileNumber.setError("please enter your mobile number");
            valid = false;
        }
        if (user_CityName.isEmpty()) {
            userCityName.setError("please enter your city name");
            valid = false;
        }
        if (user_CountryName.isEmpty()) {
            userCountryName.setError("please enter your country name");
            valid = false;
        }
        return valid;
    }
}
