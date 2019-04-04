package com.jin10.chatnow;

public class User {

    private String id;
    private String username;
    private String imageURL;

    public User(String _id, String _username, String _imageURL) {
        id = _id;
        username = _username;
        imageURL = _imageURL;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
