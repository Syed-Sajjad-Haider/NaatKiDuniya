package com.naats.naatkidunya.SharedPref;

import android.content.SharedPreferences;
import android.text.TextUtils;
public class PreferenceManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        editor = sharedPreferences.edit();
    }

    public void saveDataInMap(String mapKey, String dataKey, String dataValue) {
        JsonHashMap jsonHashMapData = getMapData(mapKey);
        if (jsonHashMapData == null)
            jsonHashMapData = new JsonHashMap();
        jsonHashMapData.put(dataKey, dataValue);
        save(mapKey, jsonHashMapData.toString());
    }

    public  void deleteDataInMap(String mapKey, String dataKey) {
        JsonHashMap jsonHashMapData = getMapData(mapKey);
        if (jsonHashMapData != null && jsonHashMapData.containsKey(dataKey)) {
            jsonHashMapData.remove(dataKey);
            save(mapKey, jsonHashMapData.toString());
        }
    }

    public  JsonHashMap getMapData(String mapKey) {
        String value = sharedPreferences.getString(mapKey, null);
        if (TextUtils.isEmpty(value))
            return null;
        return new JsonHashMap(value);
    }


    public void save(@PreferenceKey String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }


}

