package com.cpoopc.smoothemojikeyboard.smiley.bean;/**
 * Created by cpoopc on 2015/9/1.
 */

import android.os.Parcel;

import com.cpoopc.smoothemojikeyboard.smiley.emoji.Emojicon;

/**
 * User: cpoopc
 * Date: 2015-09-01
 * Time: 18:32
 * Ver.: 0.1
 */
public class SmileyEntity extends Emojicon {
    public SmileyEntity(int icon, char value, String emoji) {
        super(icon, value, emoji);
    }

    public SmileyEntity(Parcel in) {
        super(in);
    }

    public SmileyEntity(String emoji) {
        super(emoji);
    }

//    private int icon;
//
//    private char value;
//
//    private String emoji;

}
