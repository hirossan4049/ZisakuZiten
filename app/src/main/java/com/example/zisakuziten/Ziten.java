package com.example.zisakuziten;

import java.io.Serializable;

import io.realm.RealmObject;

public class Ziten extends RealmObject implements Serializable {

    public String title;

    public String content;

    public String updateTime;

//    public Ziten(String title, String content, String updateTime) {
//        this.title = title;
//        this.content = content;
//        this.updateTime = updateTime;
//    }
}

