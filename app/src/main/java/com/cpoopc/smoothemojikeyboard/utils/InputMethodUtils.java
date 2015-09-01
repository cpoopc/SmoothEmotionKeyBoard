package com.cpoopc.smoothemojikeyboard.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 输入法相关类
 * <br>Created 2014-8-25 下午2:24:03
 * @version  1.0
 * @author   huangyx
 * @see
 *
 */
public class InputMethodUtils {

    private static final int DELAY_TIME = 100;

    /**
     * 隐藏输入法
     * <br>Created 2013-11-18上午11:07:08
     * @param context 上下文
     * @param view 控件
     * @author       huangszh
     */
    public static void hideSoftInput(Context context,View view){
        if(view == null){
            return;
        }
        InputMethodManager imm = (InputMethodManager)context
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     * <br>Created 2014-8-25 下午2:19:26
     * @param context 上下文
     * @param windowToken
     * @author       huangyx
     */
    public static void collapseSoftInputMethod(final Context context,
            final IBinder windowToken) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                ((InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(windowToken, 0);
            }
        }, DELAY_TIME);

    }

    /**
     * 弹出软键盘
     * <br>Created 2014-8-25 下午2:21:42
     * @param context 上下文
     * @param view 控件
     * @author       huangyx
     */
    public static void showSoftInputMethod(final Context context,
            final View view) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                ((InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE))
                        .showSoftInput(view, 0);
            }
        }, DELAY_TIME);
    }
}
