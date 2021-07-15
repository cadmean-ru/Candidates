package ru.cadmean.candidates.models;

import com.google.firebase.Timestamp;

import java.util.Map;

public class Post {

    private String id;
    private String title;
    private String description;
    private String picture;
    private String userId;
    private Timestamp timeStamp ;


    public Post(String title, String description, String picture, String userId) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.timeStamp = Timestamp.now();
    }

    private static final String JSON_TITLE = "title";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_PICTURE = "picture";
    private static final String JSON_USER_ID = "userId";
    private static final String JSON_TIMESTAMP = "timeStamp";


    public Post(String id, Map<String, Object> data) {
        this.id = id;
        title = (String) data.get(JSON_TITLE);
        description = (String) data.get(JSON_DESCRIPTION);
        picture = (String) data.get(JSON_PICTURE);
        userId = (String) data.get(JSON_USER_ID);
        timeStamp = (Timestamp) data.get(JSON_TIMESTAMP);
    }

    public Post() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
