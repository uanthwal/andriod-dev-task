package com.example.upendra.webyoguitest.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.upendra.webyoguitest.MainActivity;
import com.example.upendra.webyoguitest.R;
import com.example.upendra.webyoguitest.Utils.HTTPUtils;
import com.example.upendra.webyoguitest.constants.URLConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailedMailActivity extends AppCompatActivity {
    private static String TAG = DetailedMailActivity.class.getSimpleName();

    private TextView mailTitleDM;
    private TextView mailSubjectDM;
    private TextView senderNameTV;
    private TextView getSenderEmailTV;
    private TextView mailBody;
    private ImageView mailStar;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_mail_activity);
        Intent intent = getIntent();
        String mailID = intent.getStringExtra("mailID");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        mailStar = (ImageView) findViewById(R.id.senderLetter);
        new HttpAsyncTask().execute(URLConstants.BASE_URL + "/" + mailID);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return HTTPUtils.GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            initializeUIComponents();
            Toast.makeText(getBaseContext(), "Received Mail data from Server!", Toast.LENGTH_LONG).show();
            JSONObject response;
            JSONArray participantsInfo;
            try {
                response = new JSONObject(result);
                boolean isStarred = response.getBoolean("isStarred");
                mailTitleDM.setText(response.getString("subject"));
                mailSubjectDM.setText(response.getString("preview"));
                if (isStarred) {
                    mailStar.setImageResource(R.drawable.starred);
                } else {
                    mailStar.setImageResource(R.drawable.unstarred);
                }
                participantsInfo = response.getJSONArray("participants");
                String senderName = participantsInfo.getJSONObject(0).getString("name");
                String senderEmail = participantsInfo.getJSONObject(0).getString("email");

                TextDrawable drawable = TextDrawable.builder().buildRound(senderName.substring(0, 1), ColorGenerator.MATERIAL.getRandomColor());
                ImageView image = (ImageView) findViewById(R.id.senderLetter);
                image.setImageDrawable(drawable);
                senderNameTV.setText(senderName);
                getSenderEmailTV.setText(senderEmail);
                mailBody.setText(response.getString("body"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeUIComponents() {
        senderNameTV = (TextView) findViewById(R.id.senderName);
        getSenderEmailTV = (TextView) findViewById(R.id.senderEmail);
        mailTitleDM = (TextView) findViewById(R.id.mailTitleDM);
        mailSubjectDM = (TextView) findViewById(R.id.mailSubjectDM);
        mailBody = (TextView) findViewById(R.id.mailBody);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detailed_mail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.MenuItem1) {
            Toast.makeText(getApplicationContext(), "Delete mail", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.MenuItem2) {
            Toast.makeText(getApplicationContext(), "Mark mail as Unread", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.MenuItem3) {
            Toast.makeText(getApplicationContext(), "Archive mail", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.MenuItem4) {
            Toast.makeText(getApplicationContext(), "Archive mail", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.MenuItem5) {
            Toast.makeText(getApplicationContext(), "Mark as Spam", Toast.LENGTH_SHORT).show();
            return true;
        }
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}