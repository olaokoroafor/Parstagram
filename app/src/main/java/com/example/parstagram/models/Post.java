package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String DESCRIPTION_KEY = "description";
    public static final String IMAGE_KEY = "image";
    public static final String USER_KEY = "user";
    public static final String LIKES_KEY = "likes";
    public static final String COMMENTS_KEY = "comments";
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public String getDescription(){
        return getString(DESCRIPTION_KEY);
    }

    public void setDescription(String description){
        put(DESCRIPTION_KEY, description);
    }

    public List<String> getLikes(){
        return getList(LIKES_KEY);
    }

    public void setLikes(ParseUser user, boolean like){
        List<String> likesArray = this.getLikes();
        if (like){
            likesArray.add(user.getObjectId());
        }
        else{
            likesArray.remove(user.getObjectId());
        }
        put(LIKES_KEY, likesArray);
    }

    public void setLikes(){
        JSONArray array = new JSONArray();
        put(LIKES_KEY, array);
    }

    public JSONArray getComments(){
        return getJSONArray(COMMENTS_KEY);
    }

    public void setComment(Comment comment){
        JSONArray array = getComments();
        array.put(comment);
        put(COMMENTS_KEY, comment);
    }

    public void setComment(){
        JSONArray array = new JSONArray();
        put(COMMENTS_KEY, array);
    }
    public ParseFile getImage(){
        return getParseFile(IMAGE_KEY);
    }

    public void setImage(ParseFile image){
        put(IMAGE_KEY, image);
    }

    public ParseUser getUser(){
        return getParseUser(USER_KEY);
    }

    public void setUser(ParseUser user){
        put(USER_KEY, user);
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = 0;
            time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + "m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + "h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + "d";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
