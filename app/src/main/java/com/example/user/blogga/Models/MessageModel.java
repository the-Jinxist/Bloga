package com.example.user.blogga.Models;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class MessageModel {
    public String message, u_id, time_stamp, image_u_id;

    public MessageModel(String message, String u_id, String time_stamp, String image_u_id) {
        this.message = message;
        this.u_id = u_id;
        this.time_stamp = time_stamp;
        this.image_u_id = image_u_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getImage_u_id() {
        return image_u_id;
    }

    public void setImage_u_id(String image_u_id) {
        this.image_u_id = image_u_id;
    }
}
