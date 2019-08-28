package com.instrumentation.valueobjects;

import com.instrumentation.cache.CacheType;

import java.sql.Timestamp;
import java.util.Objects;


public class LogData {
    private String id;
    private CacheType state;
    private String  type;
    private String host;
    private Timestamp timestamp;

    public String getId() {
        return id;
    }

    public CacheType getState() {
        return state;
    }

    public String getHost() {
        return (host == null) ? "NA" : host;
    }

    public String getType() {
        return (type == null) ? "NA" : type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = CacheType.valueOf(state);
    }

    public void  setHost() {
        this.host = host;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
