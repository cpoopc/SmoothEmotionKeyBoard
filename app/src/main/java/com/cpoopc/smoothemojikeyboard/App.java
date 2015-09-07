package com.cpoopc.smoothemojikeyboard;/**
 * Created by Administrator on 2015-09-07.
 */

import android.app.Application;

/**
 * User: cpoopc
 * Date: 2015-09-07
 * Time: 22:02
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHelper.instance.setAppContext(this);
    }
}
