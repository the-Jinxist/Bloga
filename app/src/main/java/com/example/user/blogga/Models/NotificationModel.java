package com.example.user.blogga.Models;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class NotificationModel {
    private String notification_title, notification_desc;

    public NotificationModel(String notification_title, String notification_desc) {
        this.notification_title = notification_title;
        this.notification_desc = notification_desc;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getNotification_desc() {
        return notification_desc;
    }

    public void setNotification_desc(String notification_desc) {
        this.notification_desc = notification_desc;
    }
}
