package com.naats.naatkidunya.adapter;

import android.Manifest;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.webkit.MimeTypeMap;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.naats.naatkidunya.R;
import com.naats.naatkidunya.SharedPref.AppPreferences;
import com.naats.naatkidunya.SharedPref.StorageUtil;
import com.naats.naatkidunya.activities.BestNaatActivity;
import com.naats.naatkidunya.activities.NaatkiDunyaMediaPlayer;
import com.naats.naatkidunya.model.NaatsModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class RingTonesAdapter extends RecyclerView.Adapter<RingTonesAdapter.ViewHolder> {

    public static final String ACTION_LIKE_IMAGE_CLICKED = "action_like_image_button";

    private List<NaatsModel> storyList;
    private Context context;

    AppPreferences appPreferences;
    List<String> songsName;
    String audioUrl;
    int changedItemPosition;
    private boolean isLiked;
    private AlertDialog.Builder builder;

    public RingTonesAdapter(Context context, List<NaatsModel> downModels, List<String> song_name, AppPreferences appPreferences) {
        this.context = context;
        this.storyList = downModels;
        this.songsName = song_name;
        this.appPreferences = appPreferences;



    }

    @NonNull
    @Override
    public RingTonesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RingTonesAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.ringtones_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RingTonesAdapter.ViewHolder holder, final int position) {

        holder.setViewData(storyList.get(position), holder.getAdapterPosition());

        holder.popupMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(context, v);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_Default:

//                                Uri m_path = Uri.parse(storyList.get(position).getUrl());
//                                try {
//                                    if (checkSystemWritePermission()) {
//                                        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, m_path);
//                                        Toast.makeText(context, "Set as ringtoon successfully ", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(context, "Allow modify system settings ==> ON ", Toast.LENGTH_LONG).show();
//                                    }
//                                } catch (Exception e) {
//                                    Log.i("ringtoon", e.toString());
//                                    Toast.makeText(context, "unable to set as Ringtoon ", Toast.LENGTH_SHORT).show();
//                                }
//                                setRingtone(position);
                                break;
                            case R.id.popup_Contact:

                                Uri m_Contact = Uri.parse(storyList.get(position).getUrl());
                                try {
                                    if (checkSystemWritePermission()) {
                                        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, m_Contact);
                                        Toast.makeText(context, "Set as ringtoon successfully ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Allow modify system settings ==> ON ", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    Log.i("ringtoon", e.toString());
                                    Toast.makeText(context, "unable to set as Ringtoon ", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.popup_Alaram:

                                Uri m_Alaram = Uri.parse(storyList.get(position).getUrl());
                                try {
                                    if (checkSystemWritePermission()) {
                                        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM, m_Alaram);
                                        Toast.makeText(context, "Set as ringtoon successfully ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Allow modify system settings ==> ON ", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    Log.i("ringtoon", e.toString());
                                    Toast.makeText(context, "unable to set as Ringtoon ", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.popup_Notification:

                                Uri m_Notification = Uri.parse(storyList.get(position).getUrl());
                                try {
                                    if (checkSystemWritePermission()) {
                                        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, m_Notification);
                                        Toast.makeText(context, "Set as ringtoon successfully ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Allow modify system settings ==> ON ", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    Log.i("ringtoon", e.toString());
                                    Toast.makeText(context, "unable to set as Ringtoon ", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.popup_Downlaod:

                                Dexter.withContext(context)
                                        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport)
                                    {
                                        if(multiplePermissionsReport.areAllPermissionsGranted())
                                        {
                                            Toast.makeText(context,"All Permissions Granted, Thanks",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(context,"Please Allow All Permissions",Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                                    }

                                }).check();

                                ProgressDialog progressDialog=new ProgressDialog(context);
                                progressDialog.setMessage("Downloading");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                File file=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                PRDownloader.download(storyList.get(position).getUrl(), file.getPath(), storyList.get(position).getName())
                                        .build()
                                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                            @Override
                                            public void onStartOrResume() {

                                            }
                                        })
                                        .setOnPauseListener(new OnPauseListener() {
                                            @Override
                                            public void onPause() {

                                            }
                                        })
                                        .setOnCancelListener(new OnCancelListener() {
                                            @Override
                                            public void onCancel() {

                                            }
                                        })
                                        .setOnProgressListener(new OnProgressListener() {

                                            @Override
                                            public void onProgress(Progress progress)
                                            {
                                                long down=progress.currentBytes * 100 / progress.totalBytes;
                                                progressDialog.setMessage("Downloading : "+down+" %");

                                            }
                                        })
                                        .start(new OnDownloadListener() {
                                            @Override
                                            public void onDownloadComplete()
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(context,"Downloading Completed",Toast.LENGTH_SHORT).show();
                                                setRingtone(position);
                                            }

                                            @Override
                                            public void onError(Error error)
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(context,"Errors",Toast.LENGTH_SHORT).show();

                                            }

                                        });

//
//
//                                int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                                int WRITE_EXTERNAL_STORAGE = 0;
//
//                                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
//                                } else {
////                                    Toast.makeText(context, "able to download ", Toast.LENGTH_SHORT).show();
//                                    String url = storyList.get(position).getUrl();
//                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                                    request.setDescription("Downloading . . .");
//                                    request.setTitle(storyList.get(position).getName());
//
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                                        request.allowScanningByMediaScanner();
//                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                                    }
//                                    String path = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";
//                                    ;
//                                    OutputStream output = null;
//                                    try {
//                                        output = new FileOutputStream(path);
//
//                                    } catch (FileNotFoundException e) {
//                                        e.printStackTrace();
//                                    }
//                                    // close streams
//                                    try {
//                                        output.flush();
//                                        output.close();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + File.separator + "NaatKiDunya", storyList.get(position).getName() + ".mp3");
//                                    DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//                                    manager.enqueue(request);
//                                }

                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                menu.inflate(R.menu.menu_popup);
                menu.show();
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playAudio(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return storyList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageButton popupMenuBtn;
        private CheckBox likeCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            popupMenuBtn = itemView.findViewById(R.id.popupMenuBtn);
            likeCheckBox = itemView.findViewById(R.id.like_button_cb);
        }

        public void setViewData(final NaatsModel story, final int adapterPosition) {
            title.setText(story.getName());
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
                            Toast.makeText(context, "Saved to Favourite", Toast.LENGTH_SHORT).show();
                        } else {

                            isLiked = false;
                            updateLikes();
//                            appPreferences.deleteCard(story.getId());
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

    private void playAudio(int audioIndex) {
        StorageUtil storage = new StorageUtil(context);
        storage.storeAudio(storyList);
        storage.storeAudioIndex(audioIndex);
        builder = new AlertDialog.Builder(context);

//        builder.setMessage("Do you want to Listen this Tone")
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
                        Toast.makeText(context, "Please Wait RingTone is Opening",
                                Toast.LENGTH_SHORT).show();

//                    }
//                });
//        //Creating dialog box
//        AlertDialog alert = builder.create();
//        //Setting the title manually
//        alert.setTitle("Listen");
//        alert.show();
    }

    public boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(context))
                return true;
            else
                openAndroidPermissionsMenu();
        }
        return false;
    }

    public void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
    }



//    // url = file path or suitable URL.
//    public static String getMIMEType(String url) {
//        String mType = null;
//        String mExtension = MimeTypeMap.getFileExtensionFromUrl(url);
//        if (mExtension != null) {
//            mType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mExtension);
//        }
//        return mType;
//    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setRingtone(int position)
    {
        File path =    Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);

        File k = new File(path, storyList.get(position).getName()); // path is a file to /sdcard/media/ringtone

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, storyList.get(position).getName());
        values.put(MediaStore.MediaColumns.SIZE, 21545454);
        String mType = null;
        String mExtension = MimeTypeMap.getFileExtensionFromUrl(k.getAbsolutePath());
        if (mExtension != null)
        {
            mType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mExtension);
        }
        values.put(MediaStore.MediaColumns.MIME_TYPE, mType);
        values.put(MediaStore.Audio.Media.ARTIST, storyList.get(position).getName());
        values.put(MediaStore.Audio.Media.DURATION, 230000);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(k.getPath());

        boolean settingsCanWrite=Settings.System.canWrite(context);
            if(!settingsCanWrite)
            {
                // If do not have write settings permission then open the Can modify system settings panel.
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                context.startActivity(intent);
            }

        Uri newUri = context.getContentResolver().insert(uri, values);
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
        Toast.makeText(context,"Ringtone Set",Toast.LENGTH_SHORT).show();

    }

}
