package com.cpoopc.smoothemojikeyboard.inputboard;/**
 * Created by Administrator on 2015-09-01.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpoopc.smoothemojikeyboard.R;
import com.cpoopc.smoothemojikeyboard.utils.DebugLog;
import com.cpoopc.smoothemojikeyboard.utils.InputMethodUtils;


/**
 * User: cpoopc
 * Date: 2015-09-01
 * Time: 00:18
 */
public class SoftInputLayout extends LinearLayout implements View.OnClickListener {

    private View rootView;
    private double mVisibleHeight;
    private boolean mIsKeyboardShow;
    private View container;
    private View btnKeyBoard;
    private View btnSmiley;
    public final static int SHOW_KEYBOARD = 0X1;
    public final static int SHOW_SMILE = 0X10;
    private int showWhat;
    private View smileyView;
    private int keyboardHeight = 400;
    private int rootViewHeight;

    public SoftInputLayout(Context context) {
        super(context);
        init();
    }

    public SoftInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        updateLog();
        rootView = this;
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.softinput_layout, this, true);
        btnKeyBoard = layout.findViewById(R.id.btnKeyBoard);
        btnSmiley = layout.findViewById(R.id.btnSmile);
        btnKeyBoard.setOnClickListener(this);
        btnSmiley.setOnClickListener(this);
        container = layout.findViewById(R.id.container);
        smileyView = layout.findViewById(R.id.smiley);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getKeyboardHeight();
                DebugLog.e("visiable height:" + mVisibleHeight + " mIsKeyboardShow:" + mIsKeyboardShow);
                updateLog();
                if (mIsKeyboardShow) {
                    showView(container);
                } else {
                    if (showWhat == 0) {
                        hideView(container);
                    } else if (showWhat == SHOW_KEYBOARD) {
                        showWhat = 0;
                    }
                }
            }
        });
    }

    private void getKeyboardHeight() {
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int visibleHeight = r.height();
        if (mVisibleHeight == 0) {
            mVisibleHeight = visibleHeight;
            return;
        }
        if (mVisibleHeight == visibleHeight) {
            return;
        }
        mVisibleHeight = visibleHeight;
        // Magic is here
        DebugLog.e("rootView.getHeight():" + rootView.getHeight() + "  rootViewHeight:"+rootViewHeight);
        if (mVisibleHeight < rootView.getHeight()) {
            int height = (int) (rootView.getHeight() - mVisibleHeight);
            if (keyboardHeight != height) {
                keyboardHeight = height;
                container.getLayoutParams().height = height;
                container.requestLayout();
            }
            mIsKeyboardShow = true;
            showWhat = SHOW_KEYBOARD;
        } else {
            mIsKeyboardShow = false;
            if (showWhat == SHOW_KEYBOARD) {
                showWhat = 0;
            }
        }
    }

    @Override
    public void onClick(View v) {
        updateLog();
        boolean isKeyBoardShow = mIsKeyboardShow;
        if (v == btnSmiley) {
            // 点击表情
            if (showWhat == SHOW_SMILE) {
                // 隐藏表情,隐藏layout
                hideView(smileyView);
                hideView(container);
            } else {
                showView(smileyView);
                showView(container);
            }
        } else if (v == btnKeyBoard) {
            // 点击键盘
            if (showWhat == SHOW_KEYBOARD) {
                hideView(container);
            } else if (showWhat == 0) {
                showView(container);
                showSoftInput();
            } else if (showWhat == SHOW_SMILE) {
                hideView(smileyView);
                showSoftInput();
            }
        }

        if (isKeyBoardShow) {
            hideSoftInput();
        }
        updateLog();
    }

    private void hideSoftInput() {
        DebugLog.e("隐藏软键盘");
        InputMethodUtils.hideSoftInput(getContext(), editText);
    }

    private void showSoftInput() {
        DebugLog.e("显示软键盘");
        showWhat = SHOW_KEYBOARD;
        InputMethodUtils.showSoftInputMethod(getContext(), editText);
    }

    private void hideView(View view) {
        view.setVisibility(GONE);
        if (view == container) {
            showWhat = 0;
        }
    }

    private void showView(View view) {
        view.setVisibility(VISIBLE);
        if (view == smileyView) {
            showWhat = SHOW_SMILE;
        }
    }

    private TextView tvLog;

    public void setLogText(TextView tvLog) {
        this.tvLog = tvLog;
    }

    public void updateLog() {
        StringBuilder sb = new StringBuilder();
        if (showWhat == SHOW_KEYBOARD) {
            sb.append("SHOW_KEYBOARD ,");
        } else if (showWhat == SHOW_SMILE){
            sb.append("SHOW_SMILE ,");
        } else if (showWhat == 0) {
            sb.append("SHOW_NOTHING ,");
        }
        sb.append(" 软键盘-");
        sb.append(mIsKeyboardShow ? " 显示" : " 隐藏");
        DebugLog.d(sb.toString());
        if (tvLog != null) {
            tvLog.setText(sb.toString());
        }
    }

    private EditText editText;

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

}
