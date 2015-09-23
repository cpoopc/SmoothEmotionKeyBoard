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

    private int keyboardHeight = 400;
    private List<View> showViewList;
    private Map<View,ViewHolder> viewMapping;

    private View frame;
    // 高度计算
    private boolean initFrameHeight = false;

    private EditText editText;
    private int mNavigationBarHeight;
    private int mHiddenHeight;
    private int mShownHeight;
    private int mLastCoverHeight;
    private int mRootViewHeight;

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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        updateLog();
        final Context context = getContext();
        if (context instanceof Activity) {
            rootView = ((Activity) context).getWindow().getDecorView();
        } else {
            rootView = this;
        }
        // ....
        btnKeyBoard = getBtnKeyBoard();
        editText = getEditText();
        container = getContainer();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                detectKeyBoardState();
                detectRootViewHeightChange();
                DebugLog.i("mShownHeight:" + mShownHeight + " mHiddenHeight: " + mHiddenHeight + " mLastCoverHeight:" + mLastCoverHeight + " mIsKeyboardShow:" + mIsKeyboardShow);
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
     * 检测键盘弹出状态
     */
    private void detectKeyBoardState() {
        Rect visibleRect = new Rect();
        rootView.getWindowVisibleDisplayFrame(visibleRect);
        Rect hitRect = new Rect();
        rootView.getHitRect(hitRect);
        int coverHeight = hitRect.bottom - visibleRect.bottom;
//        DebugLog.i("visibleRect:" + visibleRect + " hitRect:" + hitRect + " Height:"+rootView.getHeight());
//        DebugLog.i("container.height"+container.getLayoutParams().height);
        if (mLastCoverHeight == coverHeight) {
            return;
        }
        mLastCoverHeight = coverHeight;
        if (coverHeight > mNavigationBarHeight) {
            mShownHeight = coverHeight - mHiddenHeight;
            int height = mShownHeight;
            if (keyboardHeight != height) {
                keyboardHeight = height;
                container.getLayoutParams().height = height;
                container.requestLayout();
            }
            mIsKeyboardShow = true;
            showWhat = SHOW_KEYBOARD;
        } else {
            mHiddenHeight = coverHeight;
            mIsKeyboardShow = false;
            if (showWhat == SHOW_KEYBOARD) {
                showWhat = 0;
            }
        }
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
//                showView(container);// 可以先显示,后隐藏
            } else {
//                hideView(container);// 不能马上隐藏
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
        if (!initFrameHeight) {
            mRootViewHeight = rootView.getHeight();
            // 设置frame高度
            frame = getFrame();
            frame.getLayoutParams().height = getMeasuredHeight();
            mNavigationBarHeight = getNavigationBarHeight(getContext());
            DebugLog.e("NavigationBar h : " + mNavigationBarHeight);
            initFrameHeight = true;
        }
    }

    private void detectRootViewHeightChange() {
        if (mRootViewHeight != rootView.getHeight()) {
            int deltaH = rootView.getHeight() - mRootViewHeight;
            frame.getLayoutParams().height += deltaH;
            mRootViewHeight = rootView.getHeight();
            DebugLog.e("frame.height:" + frame.getLayoutParams().height + " deltaH : " + deltaH);
            frame.requestLayout();
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
        sb.append(mIsKeyboardShow ? " 显示" : " 隐藏");
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
