package com.example.user.blogga.Models;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class BlogInfo {
    public String display_name, user_image_url, blog_image_url, blog_text, user_uid, post_type, post_key;

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public BlogInfo(String display_name, String user_image_url, String blog_image_url, String blog_text, String user_uid, String post_type, String post_key) {
        this.display_name = display_name;
        this.user_image_url = user_image_url;
        this.blog_image_url = blog_image_url;
        this.blog_text = blog_text;
        this.user_uid = user_uid;
        this.post_type = post_type;
        this.post_key = post_key;
    }

    public String getPost_key() {
        return post_key;
    }

    public void setPost_key(String post_key) {
        this.post_key = post_key;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getUser_image_url() {
        return user_image_url;
    }

    public void setUser_image_url(String user_image_url) {
        this.user_image_url = user_image_url;
    }

    public String getBlog_image_url() {
        return blog_image_url;
    }

    public void setBlog_image_url(String blog_image_url) {
        this.blog_image_url = blog_image_url;
    }

    public String getBlog_text() {
        return blog_text;
    }

    public void setBlog_text(String blog_text) {
        this.blog_text = blog_text;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }
}
