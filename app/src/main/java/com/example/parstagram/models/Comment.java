package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String POST_KEY = "post";
    public static final String BODY_KEY = "body";
    public static final String USER_KEY = "user";
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public Post getPost(){
        return (Post) getParseObject(this.POST_KEY);
    }

    public void setPost(Post post){
        put(POST_KEY, post);
    }

    public String getBody(){
        return getString(BODY_KEY);
    }

    public void setBody(String body){
        put(BODY_KEY, body);
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
