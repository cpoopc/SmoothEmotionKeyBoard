package com.cpoopc.smoothemojikeyboard.smiley.view;/**
 * Created by cpoopc on 2015/9/1.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 内含SmileyView,放入viewpager中的layout
 * User: cpoopc
 * Date: 2015-09-01
 * Time: 18:30
 * Ver.: 0.1
 */
public class SmileyGrid extends GridView{
    public SmileyGrid(Context context) {
        super(context);
    }

    public SmileyGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmileyGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SmileyGrid(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
