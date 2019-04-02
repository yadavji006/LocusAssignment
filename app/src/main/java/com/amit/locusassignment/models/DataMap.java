package com.amit.locusassignment.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class DataMap {

    private Bitmap bitmap;
    private ArrayList<String> options;
    private int checkedPstn;
    private boolean isComment;
    private String comment;

    public DataMap(Bitmap bitmap, ArrayList<String> options, int checkedPstn, boolean isComment, String comment) {
        this.bitmap = bitmap;
        this.options = options;
        this.checkedPstn = checkedPstn;
        this.isComment = isComment;
        this.comment = comment;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public int getCheckedPstn() {
        return checkedPstn;
    }

    public void setCheckedPstn(int checkedPstn) {
        this.checkedPstn = checkedPstn;
    }

    public boolean isComment() {
        return isComment;
    }

    public void setIsComment(boolean comment) {
        isComment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
