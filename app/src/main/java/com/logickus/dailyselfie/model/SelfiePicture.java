package com.logickus.dailyselfie.model;

/**
 * Created by User on 3/23/2015.
 */
public class SelfiePicture {
    String title;
    String date;
    String pictureFilePath;

    public SelfiePicture(String title, String date, String pictureFilePath) {
        this.title = title;
        this.date = date;
        this.pictureFilePath = pictureFilePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPictureFilePath() {
        return pictureFilePath;
    }

    public void setPictureFilePath(String pictureFilePath) {
        this.pictureFilePath = pictureFilePath;
    }
}
