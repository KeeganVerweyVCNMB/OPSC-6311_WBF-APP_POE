package com.example.opsctask2app;

public class UploadModel {
    //Setting Variables
    private String ImgURL;

    public UploadModel() {
        //Do Nothing
    }
    //Parsing URI ImgURL into Upload Model and setting return type
    public UploadModel(String ImgURL) {
        this.ImgURL = ImgURL;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String ImgURL) {
        this.ImgURL = ImgURL;
    }
}
