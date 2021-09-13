package com.naats.naatkidunya.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.naats.naatkidunya.R;
import com.naats.naatkidunya.SharedPref.AppPreferences;
import com.naats.naatkidunya.model.NaatsModel;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class AllNaatKhawanAdapter extends RecyclerView.Adapter<AllNaatKhawanAdapter.AllNaatKhawanVH> {

    public static final String ACTION_LIKE_IMAGE_CLICKED = "action_like_image_button";
    private CallbackInterface mCallback;
    private DownloadInterface downloadInterface;
    private List<NaatsModel> storyList;
    private Context context;
    private int changedItemPosition;
    private boolean isLiked;
    private AppPreferences appPreferences;
    List<String> songsName;

    android.app.AlertDialog alertDialog;

    public AllNaatKhawanAdapter(Context context, List<NaatsModel> downModels, List<String> song_name, AppPreferences appPreferences, DownloadInterface downloadInterface) {
        this.context = context;
        this.storyList = downModels;
        this.songsName = song_name;
        this.appPreferences = appPreferences;
        this.downloadInterface = downloadInterface;


        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e("MyAdapter", "Must implement the CallbackInterface in the Activity", ex);
        }
        alertDialog = new SpotsDialog.Builder().setContext(context).build();



    }

    @NonNull
    @Override
    public AllNaatKhawanVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllNaatKhawanVH(LayoutInflater.from(context).inflate(R.layout.favoritenaats_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllNaatKhawanVH holder, final int position) {
        holder.setViewData(storyList.get(position), holder.getAdapterPosition());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onHandleSelection(position);
                }
            }
        });
        holder.downloadNaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (downloadInterface != null) {
                    downloadInterface.onHandleDownload(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public class AllNaatKhawanVH extends RecyclerView.ViewHolder {

        private TextView textView;
        private CheckBox likeCheckBox;
        private ImageButton downloadNaat;

        public AllNaatKhawanVH(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
            likeCheckBox = itemView.findViewById(R.id.like_button_cb);
            downloadNaat = itemView.findViewById(R.id.download);
        }

        public void setViewData(final NaatsModel story, final int adapterPosition) {
            textView.setText(story.getName());
            if (story.getIsLiked() == 1)
                likeCheckBox.setChecked(true);
            else
                likeCheckBox.setChecked(false);


            likeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    changedItemPosition = adapterPosition;
                    if (buttonView.isPressed()) {
                        if (isChecked) {
                            isLiked = true;
                            updateLikes();
                            appPreferences.saveFavouriteCard(story);
                            Toast.makeText(context, "Saved to Favourite", Toast.LENGTH_SHORT).show();
                        } else {

                            isLiked = false;
                            updateLikes();
                            appPreferences.deleteCard(story.getId());
                            Toast.makeText(context, "Removed to Favourite", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    public void updateLikes() {
        if (isLiked && storyList.get(changedItemPosition).getIsLiked() == 0) {
            storyList.get(changedItemPosition).setIsLiked(1);
            notifyItemChanged(changedItemPosition, ACTION_LIKE_IMAGE_CLICKED);
        } else if (!isLiked && storyList.get(changedItemPosition).getIsLiked() == 1) {
            storyList.get(changedItemPosition).setIsLiked(0);
            storyList.remove(changedItemPosition);
            notifyItemRemoved(changedItemPosition);
            notifyItemChanged(changedItemPosition, ACTION_LIKE_IMAGE_CLICKED);
        }
    }

    public interface CallbackInterface {
        void onHandleSelection(int position);
    }

    public interface DownloadInterface {
        void onHandleDownload(int position);
    }
}