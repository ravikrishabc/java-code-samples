package com.EzTexting;

import java.util.Hashtable;

public class Group extends BaseObject {

    private static final String URL = "/groups";

    public String name;
    public String note;
    public int contactCount;

    public Group(String id, String name, String note, int contactCount) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.contactCount = contactCount;
    }

    public Group(String name, String note) {
        this.name = name;
        this.note = note;
    }

    public Group(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", note='" + note + '\'' +
                ", contactCount=" + contactCount +
                '}';
    }

    @Override
    protected String getBaseUrl() {
        return URL;
    }

    @Override
    Hashtable<String,Object> getParams() {
        Hashtable<String,Object> res = new Hashtable<String,Object>();
        putNotNull(res, "Name", name);
        putNotNull(res, "Note", note);
        return res;
    }
}