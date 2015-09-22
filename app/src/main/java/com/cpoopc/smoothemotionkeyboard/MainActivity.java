package com.cpoopc.smoothemotionkeyboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.cpoopc.smoothemotionkeyboard.fragment.ChatFragment;
import com.cpoopc.smoothemotionkeyboard.fragment.EditFragment;
import com.cpoopc.smoothemotionkeyboard.utils.DebugLog;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Button btnEditFragment;
    private Button btnChatFragment;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        btnEditFragment = (Button) findViewById(R.id.btnEditFragment);
        btnChatFragment = (Button) findViewById(R.id.btnChatFragment);
        btnEditFragment.setOnClickListener(this);
        btnChatFragment.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btnEditFragment) {
            showFragment(EditFragment.class);
        }else if (v == btnChatFragment) {
            showFragment(ChatFragment.class);
        }
    }

    @Override
    public void onBackPressed() {
        if (!removeFragment()) {
            super.onBackPressed();
        } else {
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    public boolean removeFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            return true;
        } else {
            return false;
        }
    }

    public <T extends Fragment> void showFragment(Class<T> clzz) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        DebugLog.e("currentFragment : " + fragment);
        try {
            if (fragment == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, clzz.newInstance()).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, clzz.newInstance()).commit();
            }
            scrollView.setVisibility(View.GONE);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
