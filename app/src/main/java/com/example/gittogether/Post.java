package com.example.gittogether;

public class Post {

    public String userID, title, message;

    public Post() {
    }

    public Post(String userID, String title, String message){
        this.userID = userID;
        this.title = title;
        this.message = message;
    }
}
