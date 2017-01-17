package com.hydraz.trungnam1992.realmdemo;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by trungnam1992 on 1/16/17.
 */

public class Dog extends RealmObject {

    @PrimaryKey
    public String id = UUID.randomUUID().toString();

    public String color;

    public String name;

}
