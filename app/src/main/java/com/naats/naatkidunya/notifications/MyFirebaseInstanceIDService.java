//package com.naats.naatkidunya.notifications;
//
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.messaging.FirebaseMessagingService;
//
//
//public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
//    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
//
//    public void onNewToken(String s) {
//        super.onNewToken(s);
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "New Token: " + refreshedToken);
//        storeRegIdInPref(refreshedToken);
//    }
//
//    private void storeRegIdInPref(String token) {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("regId", token);
//        editor.commit();
//    }
//}
