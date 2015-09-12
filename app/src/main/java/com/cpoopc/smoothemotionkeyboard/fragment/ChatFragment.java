package com.cpoopc.smoothemotionkeyboard.fragment;/**
 * Created by Administrator on 2015-09-12.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.cpoopc.smoothemotionkeyboard.R;
import com.cpoopc.smoothemotionkeyboard.inputboard.ChatSoftInputLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * User: cpoopc
 * Date: 2015-09-12
 * Time: 14:53
 */
public class ChatFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<CharSequence> listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_chat, container, false);
        final ChatSoftInputLayout softInputLayout = (ChatSoftInputLayout) layout.findViewById(R.id.softinputLayout);
        listView = (ListView) softInputLayout.findViewById(R.id.list_view);
        List<CharSequence> stringList = new ArrayList<>();
        listAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,stringList);
        listView.setAdapter(listAdapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏 软键盘|表情
                softInputLayout.hideKeyBoardView();
                return false;
            }
        });
        softInputLayout.setOnSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = softInputLayout.getEditText();
                if (!TextUtils.isEmpty(editText.getText())) {
                    listAdapter.add(editText.getText());
                    editText.setText("");
                }
            }
        });
        return layout;
    }
}
