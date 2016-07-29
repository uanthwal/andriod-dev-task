package com.example.upendra.webyoguitest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.upendra.webyoguitest.Utils.HTTPUtils;
import com.example.upendra.webyoguitest.constants.URLConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private JSONArray jsonMailData = null;
    private JSONArray jsonIsRead = null;
    private JSONArray jsonIsStarred = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog = ProgressDialog.show(MainActivity.this, "", "Pulling latest mails....Please Wait.....");
        progressDialog.show();
        // call Async Task to perform network operation on separate thread
        new HttpAsyncTask().execute(URLConstants.BASE_URL);
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

        if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new InboxFragment();
                final Bundle inboxBundle = new Bundle();
                inboxBundle.putString("InboxMails", jsonMailData.toString());
                fragment.setArguments(inboxBundle);
                title = getString(R.string.title_inbox);
                break;
            case 1:
                fragment = new StarredFragment();
                final Bundle starredBundle = new Bundle();
                starredBundle.putString("StarredMails", jsonIsStarred.toString());
                fragment.setArguments(starredBundle);
                title = getString(R.string.title_starred);
                break;
            case 2:
                fragment = new UnreadFragment();
                final Bundle unreadBundle = new Bundle();
                unreadBundle.putString("UnreadMails", jsonIsRead.toString());
                fragment.setArguments(unreadBundle);
                title = getString(R.string.title_unread);

                break;
            case 3:
                fragment = new SpamAndTrashFragment();
                title = getString(R.string.title_spam);
                break;
            case 4:
                fragment = new SpamAndTrashFragment();
                title = getString(R.string.title_trash);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return HTTPUtils.GET(urls[0]);
        }

        @Override
        protected void onPreExecute() {

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            JSONArray response;
            try {
                response = new JSONArray(result);
                JSONObject jsonObject;
                boolean isRead, isStarred;
                int id;
                jsonMailData = new JSONArray();
                jsonIsStarred = new JSONArray();
                jsonIsRead = new JSONArray();
                for (int i = 0; i < response.length(); i++) {

                    jsonObject = (JSONObject) response.get(i);
                    jsonMailData.put(jsonObject);
                    isRead = jsonObject.getBoolean("isRead");
                    isStarred = jsonObject.getBoolean("isStarred");
                    id = jsonObject.getInt("id");

                    if (isStarred) {
                        jsonIsStarred.put(id, jsonObject);
                    }

                    if (!isRead) {
                        jsonIsRead.put(id, jsonObject);
                    }
                }
                progressDialog.dismiss();
                displayView(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}