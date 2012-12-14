package com.EzTexting;

import java.util.Hashtable;
import java.util.List;

public abstract class BaseObject {

    public String id;

    String getUpdateUrl() {
        return getBaseUrl() + "/"+id;
    }

    abstract String getBaseUrl();

    abstract Hashtable<String,Object> getParams();

    protected void putList(Hashtable<String, Object> res, String key, List<String> vals) {
        if (vals != null) {
            for (int i = 0; i < vals.size(); i++) {
                String val = vals.get(i);
                res.put(key+"["+i+"]", val);
            }
        }
    }

    static void putNotNull(Hashtable<String, Object> res, String key, String val) {
        if (val != null) {
            res.put(key, val);
        }
    }

    protected boolean hasResponseAfterUpdate() {
        return true;
    }
}
