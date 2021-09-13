package com.naats.naatkidunya.model;

import androidx.constraintlayout.solver.state.helpers.BarrierReference;

import com.naats.naatkidunya.SharedPref.GsonUtils;

import java.util.ArrayList;

public class NaatsModel {

    private String name ;
    private String url ;
    private String id;
    private int isLiked;


    public NaatsModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }



    @Override
    public String toString() {
        return GsonUtils.convertToJSON(this);
    }
}
