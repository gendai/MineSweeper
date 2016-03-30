package com.example.english.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    Button reset;
    Button mode;
    TextView marked;
    TextView left;
    CustomView cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reset = (Button)findViewById(R.id.Resest);
        mode = (Button)findViewById(R.id.Mode);
        marked = (TextView)findViewById(R.id.MinesMarked);
        left = (TextView)findViewById(R.id.MinesLeft);
        cv = (CustomView)findViewById(R.id.CustomView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
