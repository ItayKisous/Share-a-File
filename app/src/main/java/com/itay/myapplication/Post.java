package com.itay.myapplication;

public class Post {
    private String PostText;
    private String PostID;
    private String postByUser;
    private String PdfName;

    public Post(String postText, String postID, String postByUser, String PdfName) {
        PostText = postText;
        PostID = postID;
        this.postByUser = postByUser;
        this.PdfName=PdfName;
    }

    public String getPdfName() {
        return PdfName;
    }

    public void setPdfName(String pdfName) {
        PdfName = pdfName;
    }

    public String getPostText() {
        return PostText;
    }

    public void setPostText(String postText) {
        PostText = postText;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getPostByUser() {
        return postByUser;
    }

    public void setPostByUser(String postByUser) {
        this.postByUser = postByUser;
    }

    public Post(){

    }
}
