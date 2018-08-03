package com.example.user.blogga.Models;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class RequestModel {
    public String requester_name, requester_thumb_nail, requester_uid, request_type;

    public RequestModel(String requester_name, String requester_thumb_nail, String requester_uid, String request_type) {
        this.requester_name = requester_name;
        this.requester_thumb_nail = requester_thumb_nail;
        this.requester_uid = requester_uid;
        this.request_type = request_type;
    }

    public String getRequester_name() {
        return requester_name;
    }

    public void setRequester_name(String requester_name) {
        this.requester_name = requester_name;
    }

    public String getRequester_thumb_nail() {
        return requester_thumb_nail;
    }

    public void setRequester_thumb_nail(String requester_thumb_nail) {
        this.requester_thumb_nail = requester_thumb_nail;
    }

    public String getRequester_uid() {
        return requester_uid;
    }

    public void setRequester_uid(String requester_uid) {
        this.requester_uid = requester_uid;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
}
