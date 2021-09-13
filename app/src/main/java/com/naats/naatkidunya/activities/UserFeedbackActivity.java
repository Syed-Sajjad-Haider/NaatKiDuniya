package com.naats.naatkidunya.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naats.naatkidunya.R;
import com.naats.naatkidunya.model.UserAppModel;

public class UserFeedbackActivity extends AppCompatActivity {

    private Button send_feedback;
    private AlertDialog.Builder builder;
    private Toolbar toolbar;
    private Typeface appNameTypeFace;
    private TextView app_name;
    private EditText name, phone_number, message;
    private AdView mAdView,mAdView2,mAdView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        initializeViews();
        bannersAds();
        firebaseDatabase();
    }

    public void firebaseDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Feedback");
        send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setMessage("Do you want to Send Feedback to Admin ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                table_user.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (validate()) {
                                            UserAppModel user = new UserAppModel(name.getText().toString(), phone_number.getText().toString(), message.getText().toString());
                                            table_user.child(phone_number.getText().toString()).setValue(user);
                                            Intent welcomeActivity = new Intent(UserFeedbackActivity.this, HomeScreenActivity.class);
                                            startActivity(welcomeActivity);
                                            finish();
                                            Toast.makeText(UserFeedbackActivity.this, "Thanks for you Feedback . . . !!!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(UserFeedbackActivity.this, "Required some information about feedback", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), "Your Feedback has been Cancel ...!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Your Feedback");
                alert.show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void initializeViews(){
        send_feedback = findViewById(R.id.send_feedback);

        builder = new AlertDialog.Builder(this);
        toolbar = findViewById(R.id.toolbar);
        appNameTypeFace = Typeface.createFromAsset(this.getAssets(), "fonts/Jameel Noori Nastaleeq Kasheeda.ttf");
        app_name = toolbar.findViewById(R.id.app_name);
        app_name.setTypeface(appNameTypeFace);

        name = findViewById(R.id.name);
        phone_number = findViewById(R.id.phone_number);
        message = findViewById(R.id.message);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    public void bannersAds(){
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView2 = findViewById(R.id.adView1);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

        mAdView3 = findViewById(R.id.adView2);
        AdRequest adRequest3 = new AdRequest.Builder().build();
        mAdView3.loadAd(adRequest3);
    }
    public boolean validate() {
        boolean valid = true;

        String user_Name = name.getText().toString();
        String user_MobileNumber = phone_number.getText().toString();
        String user_Message = message.getText().toString();

        if (user_Name.isEmpty()) {
            name.setError("please enter your name");
            valid = false;
        }
        if (user_MobileNumber.isEmpty() || user_MobileNumber.length() < 10) {
            phone_number.setError("please enter your mobile number");
            valid = false;
        }
        if (user_Message.isEmpty() ) {
            message.setError("please enter your message");
            valid = false;
        }
        return valid;
    }
}
