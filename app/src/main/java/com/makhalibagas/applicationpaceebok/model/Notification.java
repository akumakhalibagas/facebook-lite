package com.makhalibagas.applicationpaceebok.model;

import com.google.firebase.database.ServerValue;

/**
 * Created by Bagas Makhali on 7/26/2020.
 */
public class Notification {

    private String  publisherId, notificationDesc, key, notificationKet;
    private Object time;

    public Notification() {
    }

    public Notification(String publisherId, String notificationDesc, String key, String notificationKet) {
        this.publisherId = publisherId;
        this.notificationDesc = notificationDesc;
        this.key = key;
        this.notificationKet = notificationKet;
        this.time = ServerValue.TIMESTAMP;
    }


    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getNotificationDesc() {
        return notificationDesc;
    }

    public void setNotificationDesc(String notificationDesc) {
        this.notificationDesc = notificationDesc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNotificationKet() {
        return notificationKet;
    }

    public void setNotificationKet(String notificationKet) {
        this.notificationKet = notificationKet;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }
}
