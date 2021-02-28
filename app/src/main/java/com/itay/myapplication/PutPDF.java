package com.itay.myapplication;

public class PutPDF {

    public String fileName;
    public String postUrl;

    public PutPDF(){

    }

    public PutPDF(String fileName, String postUrl){
        this.fileName = fileName;
        this.postUrl = postUrl;
    }

    public String getName() {
        return fileName;
    }

    public void setName(String name) {
        fileName = name;
    }

    public String getUrl() {
        return postUrl;
    }

    public void setUrl(String url) {
        postUrl = url;
    }
}
