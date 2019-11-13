package com.example.zisakuziten;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class Group extends RealmObject {
    public String updateTime;
    public String groupName;
    public RealmList<Ziten> ziten_updT_List;
}
