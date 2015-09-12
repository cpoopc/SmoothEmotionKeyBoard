package com.cpoopc.smoothemojikeyboard.fragment;/**
 * Created by Administrator on 2015-09-12.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpoopc.smoothemojikeyboard.R;

/**
 * User: cpoopc
 * Date: 2015-09-12
 * Time: 14:53
 */
public class EditFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_edit, container, false);
        return layout;
    }
}
