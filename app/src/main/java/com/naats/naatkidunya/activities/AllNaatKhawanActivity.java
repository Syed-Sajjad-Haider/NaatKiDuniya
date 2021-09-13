package com.naats.naatkidunya.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.naats.naatkidunya.model.AllNaatkhawanModel;
import com.naats.naatkidunya.R;
import com.naats.naatkidunya.viewholder.AllNaatKhawanVH;

import java.io.ByteArrayOutputStream;

import dmax.dialog.SpotsDialog;

public class AllNaatKhawanActivity extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private Typeface appNameTypeFace;
    private FirebaseRecyclerAdapter<AllNaatkhawanModel, AllNaatKhawanVH> firebaseRecyclerAdapter;
    private android.app.AlertDialog alertDialog;
    private Toolbar toolbar;
    private TextView app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naatkhawan);

        appNameTypeFace = Typeface.createFromAsset(this.getAssets(), "fonts/Jameel Noori Nastaleeq Kasheeda.ttf");

        toolbar = findViewById(R.id.toolbar);
        app_name = toolbar.findViewById(R.id.app_name);
        app_name.setTypeface(appNameTypeFace);
        setSupportActionBar(toolbar);

//        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        alertDialog = new SpotsDialog.Builder().setContext(AllNaatKhawanActivity.this).build();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Data");
//        alertDialog.show();
        AllNaatKhawan();
    }

    public void AllNaatKhawan() {
        FirebaseRecyclerOptions<AllNaatkhawanModel> options =
                new FirebaseRecyclerOptions.Builder<AllNaatkhawanModel>()
                        .setQuery(mRef, AllNaatkhawanModel.class)
                        .build();
        alertDialog.hide();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AllNaatkhawanModel, AllNaatKhawanVH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllNaatKhawanVH allNaatKhawanVH, int i, @NonNull AllNaatkhawanModel allNaatkhawanModel) {
                allNaatKhawanVH.setDetails(AllNaatKhawanActivity.this, allNaatkhawanModel.getTitle(), allNaatkhawanModel.getImage());
                alertDialog.hide();

                allNaatKhawanVH.setOnClickListener(new AllNaatKhawanVH.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        TextView mTitleTv = view.findViewById(R.id.rTitleTv);
                        ImageView mImageView = view.findViewById(R.id.rImageView);
                        String mTitle = mTitleTv.getText().toString();
                        Drawable mDrawable = mImageView.getDrawable();
                        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
                        Intent intent = new Intent(view.getContext(), NaatKhawanNaatsActivity.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra("image", bytes); //put bitmap image as array of bytes
                        intent.putExtra("title", mTitle); // put title
                        intent.putExtra("key", allNaatkhawanModel.getKey()); // put title
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
            }

            @NonNull
            @Override
            public AllNaatKhawanVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.naatkhawan_cardview, parent, false);

                return new AllNaatKhawanVH(view);

            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
