package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Post")
public class Post extends ParseObject {
    String DESCRIPTION_KEY = "description";
    String IMAGE_KEY = "image";
    String USER_KEY = "user";

}
