package com.naats.naatkidunya;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.naats.naatkidunya.Services.NotificationActionServices;
import com.naats.naatkidunya.model.AllNaatkhawanModel;
import com.naats.naatkidunya.model.NaatsModel;

public class CreateNotification {
    public static final String CHANNEL_ID_1 = "channel1";
    public static final String ACTION_PREVIOUS = "actionprevious";
    public static final String ACTION_NEXT = "actionnext";
    public static final String ACTION_PLAY = "actionplay";

    public static Notification notification;

    public static void createNotification(Context context, Trace allNaatkhawanMode, int playbutton,
                                          int position, int size) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context,"tag");

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.applogo);
            PendingIntent pendingIntent;
            int drw_previous;
            if (position==0)
            {
                pendingIntent = null;
                drw_previous=0;
            }
            else
            {
                Intent intentprevious = new Intent(context, NotificationActionServices.class).setAction(ACTION_PREVIOUS);
                pendingIntent = PendingIntent.getBroadcast(context, 0,
                        intentprevious, PendingIntent.FLAG_UPDATE_CURRENT);
                drw_previous= R.drawable.ic_baseline_skip_previous_24;
            }
            Intent intentplay = new Intent(context, NotificationActionServices.class).setAction(ACTION_PLAY);
            PendingIntent pendingIntentplay = PendingIntent.getBroadcast(context, 0,
                    intentplay, PendingIntent.FLAG_UPDATE_CURRENT);
            drw_previous= R.drawable.ic_baseline_skip_previous_24;


            PendingIntent pendingIntentnext;
            int drw_next;
            if (position==size)
            {
                pendingIntentnext = null;
                drw_next=0;
            }
            else
            {
                Intent intentnext = new Intent(context, NotificationActionServices.class).setAction(ACTION_NEXT);
                pendingIntentnext = PendingIntent.getBroadcast(context, 0,
                        intentnext, PendingIntent.FLAG_UPDATE_CURRENT);
                drw_next= R.drawable.ic_baseline_skip_next_24;
            }


            notification = new NotificationCompat.Builder(context,CHANNEL_ID_1).
                    setSmallIcon(R.drawable.ic_baseline_music_note_24)
                    .setContentTitle(allNaatkhawanMode.getTitle())
                    .setContentText(allNaatkhawanMode.getArtist())
                    .setLargeIcon(bitmap)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .addAction(drw_previous,"Previous", pendingIntent)
                    .addAction(playbutton,"Play", pendingIntentplay)
                    .addAction(drw_next,"Next", pendingIntentnext)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_LOW).build();

            notificationManagerCompat.notify(1,notification);

        }

        }
}
