package com.example.gittogether;

public class Post {

    private String userID, title, message;

    public Post() {
    }

    public Post(String userID, String title, String message){
        this.userID = userID;
        this.title = title;
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
