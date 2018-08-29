package com.example.user.blogga.Models;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class EventsModel {
    private String event_name, event_time, event_location, u_id, event_image, user_image, user_name, post_time, post_key, event_desc, event_thumb;

    public EventsModel(String event_name, String event_time, String event_location, String u_id, String event_image, String user_image, String user_name, String post_time, String post_key, String event_desc, String event_thumb) {
        this.event_name = event_name;
        this.event_time = event_time;
        this.event_location = event_location;
        this.u_id = u_id;
        this.event_image = event_image;
        this.user_image = user_image;
        this.user_name = user_name;
        this.post_time = post_time;
        this.post_key = post_key;
        this.event_desc = event_desc;
        this.event_thumb = event_thumb;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getPost_key() {
        return post_key;
    }

    public void setPost_key(String post_key) {
        this.post_key = post_key;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }

    public String getEvent_thumb() {
        return event_thumb;
    }

    public void setEvent_thumb(String event_thumb) {
        this.event_thumb = event_thumb;
    }
}
