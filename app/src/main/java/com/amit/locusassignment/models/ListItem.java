package com.amit.locusassignment.models;

import java.util.ArrayList;

public class ListItem {

    private String id;
    private String title;
    private String type;
    private DataMap dataMap;

    public ListItem(String id, String type, String title, DataMap dataMap) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.dataMap = dataMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataMap getDataMap() {
        return dataMap;
    }

    public void setDataMap(DataMap dataMap) {
        this.dataMap = dataMap;
    }

}
