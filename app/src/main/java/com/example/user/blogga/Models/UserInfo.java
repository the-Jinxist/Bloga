package com.example.user.blogga.Models;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class UserInfo {
    private String username, status, thumb_nails, u_id;

    public UserInfo(String username, String status, String thumb_nails, String u_id) {
        this.username = username;
        this.status = status;
        this.thumb_nails = thumb_nails;
        this.u_id = u_id;
    }

    public String getThumb_nails() {
        return thumb_nails;
    }

    public void setThumb_nails(String thumb_nails) {
        this.thumb_nails = thumb_nails;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }
}
