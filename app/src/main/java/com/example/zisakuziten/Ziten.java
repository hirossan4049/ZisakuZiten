package com.example.zisakuziten;

import io.realm.RealmObject;

public class Ziten extends RealmObject {

    public String title;

    public String content;

//    public Integer probability;
//
//    public Integer all_ans;
//
//    public Integer correct_ans;

    public Integer groupId;
    public String groupName;

    public String updateTime;

}
