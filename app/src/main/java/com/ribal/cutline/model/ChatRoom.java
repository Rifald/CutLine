package com.ribal.cutline.model;

public class ChatRoom {
    private String id_usaha;
    private String id_user;
    private String id;

    public ChatRoom(String id, String id_usaha, String id_user) {
        this.id = id;
        this.id_usaha = id_usaha;
        this.id_user = id_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_usaha() {
        return id_usaha;
    }

    public void setId_usaha(String id_usaha) {
        this.id_usaha = id_usaha;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
