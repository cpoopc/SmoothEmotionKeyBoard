package com.cpoopc.smoothemotionkeyboard.inputboard;/**
 * Created by Administrator on 2015-09-01.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpoopc.smoothemotionkeyboard.utils.DebugLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * User: cpoopc
 * Date: 2015-09-01
 * Time: 00:18
 */
public abstract class BaseSoftInputLayout extends LinearLayout implements View.OnClickListener {

    public final static int SHOW_KEYBOARD = 0X1;
    public final static int SHOW_EMOTION = 0X10;
    public final static int SHOW_OTHER = 0X11;

    private View rootView;
    private boolean mIsKeyboardShow;

    private View btnKeyBoard;

    // emotionView,otherView容器
    private View container;

    private int showWhat;

    private int keyboardHeight;
    private int minOtherBoardHeight = 300;
    private List<View> showViewList;
    private Map<View,ViewHolder> viewMapping;

    private View frame;
    private EditText editText;
    private int mNavigationBarHeight = -1;
    private int mHiddenHeight;
    private int mShownHeight;
    private int mLastCoverHeight;

    public static class ViewHolder{
        private int SHOW_TYPE;
        private View showView;

        public ViewHolder(int SHOW_TYPE, View showView) {
            this.SHOW_TYPE = SHOW_TYPE;
            this.showView = showView;
        }

        public int getSHOW_TYPE() {
            return SHOW_TYPE;
        }

        public View getShowView() {
            return showView;
        }
    }

    public BaseSoftInputLayout(Context context) {
        super(context);
        init();
    }

    public BaseSoftInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BaseSoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseSoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    protected void init() {
        viewMapping = new HashMap<>();
        showViewList = new ArrayList<>();
        inflateView();
        updateLog();
        final Context context = getContext();
        if (context instanceof Activity) {
            rootView = ((Activity) context).getWindow().getDecorView();
        } else {
            rootView = this;
        }
        btnKeyBoard = getBtnKeyBoard();
        editText = getEditText();
        container = getContainer();
        frame = getFrame();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                detectKeyBoardState();
//                DebugLog.i("mShownHeight:" + mShownHeight + " mHiddenHeight: " + mHiddenHeight + " mLastCoverHeight:" + mLastCoverHeight + " mIsKeyboardShow:" + mIsKeyboardShow);
                updateLog();
                if (mIsKeyboardShow) {
                    if (showWhat == SHOW_KEYBOARD) {
                        hideAllViewExceptKeyBoard();
                    }
                    showView(container);
                } else {
                    if (showWhat == 0) {
                        hideView(container);
                    } else {
                        showView(container);
                    }
                }
            }
        });
    }

    /**
     * 渲染自定义布局
     */
    protected abstract void inflateView();

    protected abstract View getContainer();

    protected abstract View getFrame();

    public abstract EditText getEditText();

    /**
     * @return 返回键盘按键,若无则返回null
     */
    protected abstract View getBtnKeyBoard();

    private int mLastHitBottom;

    /**
     * 检测键盘弹出状态
     */
    private void detectKeyBoardState() {
        Rect visibleRect = new Rect();
        rootView.getWindowVisibleDisplayFrame(visibleRect);
        Rect hitRect = new Rect();
        rootView.getHitRect(hitRect);
        DebugLog.i("hit.bottom:" + hitRect.bottom + " visible.bottom:" + visibleRect.bottom);
        int coverHeight = hitRect.bottom - visibleRect.bottom;
        if (mLastCoverHeight == coverHeight) {
            return;
        }
        mLastHitBottom = hitRect.bottom;
        int deltaCoverHeight = coverHeight - mLastCoverHeight;
        mLastCoverHeight = coverHeight;
        if (deltaCoverHeight == mNavigationBarHeight) {
            // 显示navigationBar

        } else if (deltaCoverHeight == -mNavigationBarHeight) {
            // 隐藏navigationBar

        }
        if (coverHeight > mNavigationBarHeight) {
            mShownHeight = coverHeight - mHiddenHeight;
            int height = mShownHeight;
            int overMinHeight = 0;
            if (height < minOtherBoardHeight) {
                overMinHeight = minOtherBoardHeight - height;
                height = minOtherBoardHeight;
            }
            if (keyboardHeight != height) {
                keyboardHeight = height;
                container.getLayoutParams().height = height;
                container.requestLayout();
            }
            mIsKeyboardShow = true;
            showWhat = SHOW_KEYBOARD;
            refreshFrame(visibleRect.bottom + mShownHeight + overMinHeight);
//            refreshFrame(visibleRect.bottom + mShownHeight - overMinHeight);
        } else {
            if (coverHeight != mHiddenHeight) {
                mHiddenHeight = coverHeight;
            }
            refreshFrame(visibleRect.bottom);
            mIsKeyboardShow = false;
            if (showWhat == SHOW_KEYBOARD) {
                showWhat = 0;
            }
        }
    }

    public void refreshFrame(int bottom) {
        Rect rect = new Rect();
        frame.getHitRect(rect);
        int[] location = new int[2];
        frame.getLocationOnScreen(location);
        DebugLog.e("bottom:" + bottom + " location onscreen " + location[0] + "," + location[1]);
        frame.getLocationInWindow(location);
        DebugLog.e("bottom:" + bottom + " location InWindow " + location[0] + "," + location[1]);
        int height = bottom - rect.top - location[1];
        if (height != frame.getLayoutParams().height) {
            frame.getLayoutParams().height = height;
            frame.requestLayout();
        }
        updateLog();
    }

    protected void add2MappingMap(View view, int SHOW_TYPE, View showView) {
        viewMapping.put(view, new ViewHolder(SHOW_TYPE, showView));
    }

    protected void add2ShowViewList(View view) {
        showViewList.add(view);
    }

    @Override
    public void onClick(View v) {
        updateLog();
        if (v == btnKeyBoard) {
            // 点击键盘
            if (showWhat == SHOW_KEYBOARD) {
                showWhat = 0;
                hideSoftInput();
            } else if (showWhat == 0) {
                showWhat = SHOW_KEYBOARD;
                showSoftInput();
                showView(container);
            } else {
                showWhat = SHOW_KEYBOARD;
                hideAllViewExceptKeyBoard();
                showSoftInput();
            }
        }else {
            ViewHolder viewHolder = viewMapping.get(v);
            if (viewHolder != null) {
                int show_type = viewHolder.getSHOW_TYPE();
                View showView = viewHolder.getShowView();
                DebugLog.e("showViewshowView:" + showView + "　showWhat：" + showWhat);
                // 点击表情
                if (showWhat == show_type) {
                    // 隐藏表情,隐藏layout
                    showWhat = 0;
                    hideView(showView);
                    hideView(container);
                } else if (showWhat == SHOW_KEYBOARD) {
                    showWhat = show_type;
                    hideSoftInput();
                    showView(showView);
                    showView(container);
                } else {
                    showWhat = show_type;
                    hideAllViewExceptKeyBoard();
                    showView(showView);
                    showView(container);
                }
            }
        }
        updateLog();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mNavigationBarHeight == -1) {
            frame.getLayoutParams().height = getMeasuredHeight();
            mNavigationBarHeight = getNavigationBarHeight(getContext());
            DebugLog.e("NavigationBar h : " + mNavigationBarHeight);
        }
    }

    private void hideSoftInput() {
        DebugLog.e("隐藏软键盘");
        if(editText == null){
            DebugLog.e("editText "+editText);
            return;
        }
        InputMethodManager imm = (InputMethodManager)getContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showSoftInput() {
        DebugLog.e("显示软键盘");
        if(editText == null){
            DebugLog.e("editText "+editText);
            return;
        }
        editText.requestFocus();
        ((InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(editText, 0);
    }

    private void hideView(View view) {
        view.setVisibility(GONE);
    }

    /**
     * 隐藏除了键盘外的view
     */
    private void hideAllViewExceptKeyBoard() {
        for (int i = 0; i < showViewList.size(); i++) {
            hideView(showViewList.get(i));
        }
    }

    public void hideKeyBoardView() {
        showWhat = 0;
        hideSoftInput();
        hideView(container);
    }

    private void showView(View view) {
        view.setVisibility(VISIBLE);
    }

    private static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        try {
            Resources rs = context.getResources();
            int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
            if (id > 0 ) {
                navigationBarHeight = rs.getDimensionPixelSize(id);
            }
        } catch (Exception e) {
            // default 0
        }
        return navigationBarHeight;
    }

    /**
     * 设置最小高度(除了键盘外的最小高度)
     *
     * @param minOtherBoardHeight
     */
    public void setMinOtherBoardHeight(int minOtherBoardHeight) {
        this.minOtherBoardHeight = minOtherBoardHeight;
    }

    /*********************************** 调试LOG ****************************************/
    private TextView tvLog;

    private String mLogText;

    /**
     * 设置显示log的textview
     * @param tvLog
     */
    public void setLogText(final TextView tvLog) {
        this.tvLog = tvLog;
        tvLog.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateLog();
                tvLog.postDelayed(this, 1000);
            }
        }, 1000);
    }

    /**
     * 打印log
     */
    public void updateLog() {
        StringBuilder sb = new StringBuilder();
        if (showWhat == SHOW_KEYBOARD) {
            sb.append("SHOW_KEYBOARD ,");
        } else if (showWhat == SHOW_EMOTION){
            sb.append("SHOW_EMOTION ,");
        } else if (showWhat == SHOW_OTHER){
            sb.append("SHOW_OTHER ,");
        } else if (showWhat == 0) {
            sb.append("SHOW_NOTHING ,");
        }
        sb.append(" 软键盘-");
        sb.append(mIsKeyboardShow ? " 显示 " : " 隐藏 ");
        sb.append(" mShownHeight: " + mShownHeight);
        sb.append(" mHiddenHeight: " + mHiddenHeight);

        try {
            sb.append(" frame height:" + (frame == null ? " null " : frame.getHeight()));
        } catch (Exception e) {

        }
        logView(rootView, sb);
        String logText = sb.toString();
        if (TextUtils.equals(mLogText, logText)) {
            return;
        }
        mLogText = logText;
        DebugLog.d(logText);
        if (tvLog != null) {
            tvLog.setText(logText);
        }
    }

    private void logView(View rootView, StringBuilder sb) {
        if (rootView != null) {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int[] location = new int[2];
            rootView.getLocationOnScreen(location);
            sb.append("\n");
            sb.append(rootView + " rect " + r + " rootView location " + location[0] + "," + location[1] + " scrollY : " + rootView.getScrollY());
        }
    }

}
