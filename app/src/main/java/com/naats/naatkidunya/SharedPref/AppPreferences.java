package com.naats.naatkidunya.SharedPref;

import com.naats.naatkidunya.model.NaatsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppPreferences {

    private com.naats.naatkidunya.SharedPref.PreferenceManager preferenceManager;

    public AppPreferences(com.naats.naatkidunya.SharedPref.PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public void saveFavouriteCard(NaatsModel story) {
          preferenceManager.saveDataInMap(PreferenceKey.FAVOURITE_CONTENT_LIST, story.getId(), story.toString());

    }


    public List<NaatsModel> getFavouriteCardList() {
        JsonHashMap jsonHashMap = preferenceManager.getMapData(PreferenceKey.FAVOURITE_CONTENT_LIST);
        if (jsonHashMap == null || jsonHashMap.isEmpty())
            return null;
        List<NaatsModel> favouriteCardList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : jsonHashMap.entrySet()) {
            NaatsModel favouriteCard = null;
            try {
                favouriteCard = GsonUtils.convertToObject(entry.getValue().toString(), NaatsModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            favouriteCardList.add(favouriteCard);
        }
        return favouriteCardList;
    }

    public void deleteCard(String contentID) {
        if (contentID != null) {
            preferenceManager.deleteDataInMap(PreferenceKey.FAVOURITE_CONTENT_LIST, contentID);
        }
    }
}

