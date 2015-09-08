package com.cpoopc.smoothemojikeyboard.emotion.view;/**
 * Created by cpoopc on 2015/9/8.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cpoopc.smoothemojikeyboard.emotion.bean.EmotionEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * User: cpoopc
 * Date: 2015-09-08
 * Time: 12:56
 * Ver.: 0.1
 */
public class EmotionPager extends ViewPager {

    private List<EmotionEntity> emotionList;

    public EmotionPager(Context context) {
        super(context);
    }

    public EmotionPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bindData(List<EmotionEntity> emotionList) {
        this.emotionList = emotionList;
        List<List<EmotionEntity>> lists = separateData(emotionList);
        EmotionPagerAdapter emotionPagerAdapter = new EmotionPagerAdapter(lists);
        setOffscreenPageLimit(lists.size());
        setAdapter(emotionPagerAdapter);
    }

    private List<List<EmotionEntity>> separateData(List<EmotionEntity> emotionList) {
        // 每行7个表情,每页显示3行
        int pageSize = 3 * 7 - 1;// 外加显示删除键
        int size = emotionList.size();
        int pageCount = size / pageSize + 1;
//        int lastPageSize = size % pageSize;
//        if (lastPageSize == 1 && pageCount > 0) {
//            //
//            pageCount -= 1;
//        }
        List<List<EmotionEntity>> emotionListList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            emotionListList.add(emotionList.subList(i * pageSize, Math.min(i * pageSize + pageSize, size)));
        }
        return emotionListList;
    }

    private static class EmotionPagerAdapter extends PagerAdapter {

        private List<List<EmotionEntity>> emotionListList;

        public EmotionPagerAdapter(List<List<EmotionEntity>> emotionListList) {
            this.emotionListList = emotionListList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            RelativeLayout relativeLayout = new RelativeLayout(container.getContext());
            relativeLayout.setGravity(Gravity.CENTER);
            EmotionGrid smileyGrid = new EmotionGrid(container.getContext());
            smileyGrid.bindData(emotionListList.get(position));
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            smileyGrid.setLayoutParams(layoutParams);
            relativeLayout.addView(smileyGrid);
            container.addView(relativeLayout);
            return relativeLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return emotionListList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
