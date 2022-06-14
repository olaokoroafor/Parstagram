package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String DESCRIPTION_KEY = "description";
    public static final String IMAGE_KEY = "image";
    public static final String USER_KEY = "user";

    public String getDescription(){
        return getString(DESCRIPTION_KEY);
    }

    public void setDescription(String description){
        put(DESCRIPTION_KEY, description);
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
}
