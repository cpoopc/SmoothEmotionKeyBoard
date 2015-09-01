package com.cpoopc.smoothemojikeyboard;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cpoopc.smoothemojikeyboard.inputboard.SoftInputLayout;

public class MainActivity extends FragmentActivity {

    private double mVisibleHeight;
    private boolean mIsKeyboardShow;
    private View btnKeyBoard;
    private View btnSmiley;
    private Button btnState;
    private TextView tvState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnState = (Button) findViewById(R.id.btnState);
        tvState = (TextView) findViewById(R.id.info);
        EditText editText = (EditText) findViewById(R.id.edittext);
        final SoftInputLayout softInputLayout = (SoftInputLayout) findViewById(R.id.softinputLayout);
        softInputLayout.setLogText(tvState);
        softInputLayout.setEditText(editText);
        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                softInputLayout.updateLog();
            }
        });

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
}
