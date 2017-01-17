package com.hydraz.trungnam1992.realmdemo;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by trungnam1992 on 1/17/17.
 */

public class RealmApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }


}
