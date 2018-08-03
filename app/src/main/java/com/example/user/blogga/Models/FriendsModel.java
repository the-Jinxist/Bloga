package com.example.user.blogga.Models;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class FriendsModel {
    public String friend_name, friend_status, friend_image, friend_date, u_id;

    public FriendsModel(String friend_name, String friend_status, String friend_image, String friend_date, String u_id) {
        this.friend_name = friend_name;
        this.friend_status = friend_status;
        this.friend_image = friend_image;
        this.friend_date = friend_date;
        this.u_id = u_id;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getFriend_status() {
        return friend_status;
    }

    public void setFriend_status(String friend_status) {
        this.friend_status = friend_status;
    }

    public String getFriend_image() {
        return friend_image;
    }

    public void setFriend_image(String friend_image) {
        this.friend_image = friend_image;
    }

    public String getFriend_date() {
        return friend_date;
    }

    public void setFriend_date(String friend_date) {
        this.friend_date = friend_date;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }
}
