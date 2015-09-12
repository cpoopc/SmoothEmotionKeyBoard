package com.cpoopc.smoothemotionkeyboard;/**
 * Created by Administrator on 2015-09-07.
 */

import android.content.Context;

/**
 * User: cpoopc
 * Date: 2015-09-07
 * Time: 22:00
 */
public enum  ContextHelper {
    instance;

    private Context appContext;

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }
}
