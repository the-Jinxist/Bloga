package com.example.user.blogga.Models;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class CommentModel {
    private String user_id, comment, display_name, thumb_url;

    public CommentModel(String user_id, String comment, String display_name, String thumb_url) {
        this.user_id = user_id;
        this.comment = comment;
        this.display_name = display_name;
        this.thumb_url = thumb_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }
}
