package com.cpoopc.smoothemojikeyboard.smiley.view;/**
 * Created by cpoopc on 2015/9/1.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.cpoopc.smoothemojikeyboard.smiley.bean.SmileyEntity;
import com.cpoopc.smoothemojikeyboard.smiley.emoji.Emojicon;
import com.cpoopc.smoothemojikeyboard.smiley.emoji.People;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        init();
    }

    public SmileyGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmileyGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SmileyGrid(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setNumColumns(6);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        SmileyAdapter<Emojicon> smileyAdapter = new SmileyAdapter<Emojicon>(getContext(), Arrays.asList(People.DATA));
        setAdapter(smileyAdapter);
    }

    public static class SmileyAdapter<Model extends Emojicon> extends BaseAdapter {

        private Context context;
        protected List<Model> itemList;

        public SmileyAdapter(Context context, List<Model> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        public List<Model> getItems() {
            return itemList;
        }

        public void setItems(List<Model> contacts) {
            this.itemList = contacts;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (itemList == null) {
                return 0;
            }
            return itemList.size();
        }

        @Override
        public Model getItem(int position) {
            if (itemList == null || position > itemList.size() - 1) {
                return null;
            }
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SmileyView smileyView = null;
            if (convertView != null) {
                smileyView = (SmileyView) convertView;
            } else {
                smileyView = createView();
            }
            Model item = getItem(position);
            smileyView.bindData(item);
            return smileyView;
        }

        /**
         * 子类需要实现创建View的方法
         * @return
         */
        protected SmileyView createView() {
            return new SmileyView(context);
        }
    }

}
