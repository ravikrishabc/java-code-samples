package com.EzTexting;

import java.util.Hashtable;
import java.util.List;

abstract class BaseObject {

    public String id;

    String getUpdateUrl() {
        return getBaseUrl() + "/"+id;
    }

    abstract String getBaseUrl();

    abstract Hashtable<String,String> getParams();

    protected void putList(Hashtable<String, String> res, String key, List<String> vals) {
        if (vals != null) {
            for (int i = 0; i < vals.size(); i++) {
                String val = vals.get(i);
                res.put(key+"["+i+"]", val);
            }
        }
    }

    static void putNotNull(Hashtable<String, String> res, String key, String val) {
        if (val != null) {
            res.put(key, val);
        }
    }
}
