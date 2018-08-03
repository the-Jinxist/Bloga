package com.example.user.blogga.Models;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class HomeModel {
    public String chat_name, chat_image_u_id, chat_other_user_uid, chat_status;

    public HomeModel(String chat_name, String chat_image_u_id, String chat_other_user_uid, String chat_status) {
        this.chat_name = chat_name;
        this.chat_image_u_id = chat_image_u_id;
        this.chat_other_user_uid = chat_other_user_uid;
        this.chat_status = chat_status;
    }

    public String getChat_name() {
        return chat_name;
    }

    public void setChat_name(String chat_name) {
        this.chat_name = chat_name;
    }

    public String getChat_image_u_id() {
        return chat_image_u_id;
    }

    public void setChat_image_u_id(String chat_image_u_id) {
        this.chat_image_u_id = chat_image_u_id;
    }

    public String getChat_other_user_uid() {
        return chat_other_user_uid;
    }

    public void setChat_other_user_uid(String chat_other_user_uid) {
        this.chat_other_user_uid = chat_other_user_uid;
    }

    public String getChat_status() {
        return chat_status;
    }

    public void setChat_status(String chat_status) {
        this.chat_status = chat_status;
    }
}
