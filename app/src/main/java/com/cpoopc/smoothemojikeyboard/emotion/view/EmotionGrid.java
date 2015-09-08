package com.cpoopc.smoothemojikeyboard.emotion.view;/**
 * Created by cpoopc on 2015/9/1.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.cpoopc.smoothemojikeyboard.emotion.bean.EmotionEntity;
import com.cpoopc.smoothemojikeyboard.utils.DebugLog;

import java.util.List;

/**
 * 内含SmileyView,放入viewpager中的layout
 * User: cpoopc
 * Date: 2015-09-01
 * Time: 18:30
 * Ver.: 0.1
 */
public class EmotionGrid extends GridView{

    private SmileyAdapter<EmotionEntity> smileyAdapter;

    public EmotionGrid(Context context) {
        super(context);
        init();
    }

    public EmotionGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmotionGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmotionGrid(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setNumColumns(7);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = getMeasuredHeight();
        int height = MeasureSpec.getSize(heightMeasureSpec) * 4 / 5;
        if (height != 0 && height == measuredHeight) {
            return;
        }
//        DebugLog.e("height:" + height);
//        DebugLog.e("widthMeasureSpec:" + widthMeasureSpec + " " + MeasureSpec.getSize(widthMeasureSpec));
//        DebugLog.e("heightMeasureSpec:" + heightMeasureSpec + " " + MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(getMeasuredWidth(), height);
    }



    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        DebugLog.e(child+" parentHeightMeasureSpec:" + parentHeightMeasureSpec + " " + MeasureSpec.getSize(parentHeightMeasureSpec));
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    public void bindData(List<EmotionEntity> emotionEntities) {
        if (smileyAdapter == null) {
            smileyAdapter = new SmileyAdapter<EmotionEntity>(getContext(), null);
        }
        smileyAdapter.setItems(emotionEntities);
        setAdapter(smileyAdapter);
    }

    public static class SmileyAdapter<Model extends EmotionEntity> extends BaseAdapter {

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
            EmotionView smileyView = null;
            if (convertView != null) {
                smileyView = (EmotionView) convertView;
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
        protected EmotionView createView() {
            return new EmotionView(context);
        }
    }

}
