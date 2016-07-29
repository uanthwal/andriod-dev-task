package com.example.upendra.webyoguitest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.upendra.webyoguitest.MainActivity;
import com.example.upendra.webyoguitest.R;

public class ComposeMailActivity extends AppCompatActivity {
    private static String TAG = ComposeMailActivity.class.getSimpleName();

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_mail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Compose Mail");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose_mail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.attachments) {
            Toast.makeText(getApplicationContext(), "Attachments option is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.sendMail) {
            Toast.makeText(getApplicationContext(), "Send Mail option is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
