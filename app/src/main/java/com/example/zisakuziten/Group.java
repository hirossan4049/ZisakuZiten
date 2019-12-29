package com.example.zisakuziten;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class Group extends RealmObject {
    @SerializedName("title")
    @Expose
    public String groupName;
    @SerializedName("updateTime")
    @Expose
    public String updateTime;
    @SerializedName("ziten_updT_List")
    @Expose
    public RealmList<Ziten> ziten_updT_List;

//    public String updateTime;
//    public String groupName;
//    public RealmList<Ziten> ziten_updT_List;
}
