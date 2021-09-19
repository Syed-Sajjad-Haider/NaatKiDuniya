package com.naats.naatkidunya.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.naats.naatkidunya.activities.NaatkiDunyaMediaPlayer;
import com.naats.naatkidunya.R;
import com.naats.naatkidunya.SharedPref.AppPreferences;
import com.naats.naatkidunya.SharedPref.StorageUtil;
import com.naats.naatkidunya.model.NaatsModel;

import java.util.ArrayList;
import java.util.List;

public class BestNaatsAdapter extends RecyclerView.Adapter<BestNaatsAdapter.ViewHolder> {

    public static final String ACTION_LIKE_IMAGE_CLICKED = "action_like_image_button";

    private List<NaatsModel> storyList;
    private Context context;

    private int changedItemPosition;
    private boolean isLiked;
    private AppPreferences appPreferences;
    List<String> songsName;
    String audioUrl;
    private DownloadInterface downloadInterfaces;
    private AlertDialog.Builder builder;

    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();

    public BestNaatsAdapter(Context context, List<NaatsModel> downModels, List<String> song_name, AppPreferences appPreferences, DownloadInterface downloadInterface) {
        this.context = context;
        this.storyList = downModels;
        this.songsName = song_name;
        this.appPreferences = appPreferences;
        downloadInterfaces = downloadInterface;
        builder = new AlertDialog.Builder(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.bestnaats_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.setViewData(storyList.get(position), holder.getAdapterPosition());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                System.out.println("Lists Are"+storyList);
//                for (int i =0;i<storyList.size();i++) {
//                    jcAudios.add(JcAudio.createFromURL(storyList.get(i).getName(), storyList.get(i).getUrl()));
//                }
//                jcPlayerView.initPlaylist(jcAudios,null);
//                jcPlayerView.playAudio(jcAudios.get(position));
//                jcPlayerView.setVisibility(View.VISIBLE);
//                jcPlayerView.createNotification();

                playAudio(position);
            }
        });
        holder.downloadNaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadInterfaces != null) {
                    downloadInterfaces.onHandleDownload(position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private CheckBox likeCheckBox;
        private ImageButton downloadNaat;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
            likeCheckBox = itemView.findViewById(R.id.like_button_cb);
            downloadNaat = itemView.findViewById(R.id.download);
        }

        public void setViewData(final NaatsModel story, final int adapterPosition) {
            textView.setText(story.getName());

            audioUrl = story.getUrl();

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
                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                        } else {

                            isLiked = false;
                            updateLikes();
                            appPreferences.deleteCard(story.getId());
                            Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private void playAudio(int audioIndex) {
        StorageUtil storage = new StorageUtil(context);
        storage.storeAudio(storyList);
        storage.storeAudioIndex(audioIndex);
        System.out.println("Lists Are"+storyList);
//        for (int i =0;i<storyList.size();i++) {
//            jcAudios.add(JcAudio.createFromURL(storyList.get(i).getName(), storyList.get(i).getUrl()));
//        }
//        jcPlayerView.initPlaylist(jcAudios,null);
//        jcPlayerView.playAudio(jcAudios.get(audioIndex));
//        jcPlayerView.setVisibility(View.VISIBLE);
//        jcPlayerView.createNotification();

//        for (int i=0;i<storyList.size();i++)
//        {
//            jcAudios.add(JcAudio.createFromURL(storyList.get(i).getName(),storyList.get(i).getUrl()));
//        }
//        jcPlayerView.initPlaylist(jcAudios,null);
//        jcPlayerView.playAudio(jcAudios.get(audioIndex));
//        jcPlayerView.setVisibility(View.VISIBLE);
//        jcPlayerView.createNotification();

//        for (int i=0;i<storyList.size();i++)
//        {
//            jcAudios.add(JcAudio.createFromURL(storyList.get(a),storyList.getUrl()));
//        }
//        jcPlayerView.initPlaylist(jcAudios,null);
//
//        jcPlayerView.playAudio(jcAudios.get(audioIndex));
//        jcPlayerView.setVisibility(View.VISIBLE);
//        jcPlayerView.createNotification();
//        builder.setMessage("Do you want to Listen this Naat")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
                        Intent playerIntent = new Intent(context, NaatkiDunyaMediaPlayer.class);
                        context.startActivity(playerIntent);

//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //  Action for 'NO' Button
//                        dialog.cancel();
                        Toast.makeText(context, "Please Wait Naat is Opening",
                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.setTitle("Listen");
//        alert.show();

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

    public interface DownloadInterface {
        void onHandleDownload(int position);
    }


}
