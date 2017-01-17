package com.saklapp.nico.saklapp;

/**
 * Created by Nico on 1/17/2017.
 */

public class Message {
    private String name;
    private String time;
    private String message;
    private String like;

    public Message() {
    }


    public Message(String name, String time, String message, String like) {
        this.name = name;
        this.time = time;
        this.message = message;
        this.like = like;
    }

    public String getLike() {
        return like;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }
}
