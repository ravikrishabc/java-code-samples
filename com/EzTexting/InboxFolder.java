package com.EzTexting;

import java.util.Hashtable;

public class InboxFolder extends BaseObject {

    private static final String URL = "/messages-folders";

    public String name;

    public InboxFolder(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public InboxFolder(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", phoneNumber='" + name + '\'' +
                '}';
    }

    @Override
    protected boolean hasResponseAfterUpdate() {
        return false;
    }

    @Override
    protected String getBaseUrl() {
        return URL;
    }

    @Override
    Hashtable<String,Object> getParams() {
        Hashtable<String,Object> res = new Hashtable<String,Object>();
        putNotNull(res, "Name", name);
        return res;
    }
}