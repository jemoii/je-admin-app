package me.voler.wireless.administrator;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import me.voler.wireless.administrator.util.LoginUtil;
import me.voler.wireless.administrator.util.SpaceUtil;
import me.voler.wireless.administrator.util.UserInfo;

public class SpaceActivity extends AppCompatActivity {

    private SwipeRefreshLayout spaceLayout;
    private TextView nicknameTextView;
    private TextView usernameTextView;
    private TextView phoneTextView;
    private Button logoutButton;

    private UserInfo info;

    public static final String USERINFO_EXTRA = "me.me.voler.wireless.administrator.info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);

        nicknameTextView = (TextView) findViewById(R.id.info_nickname);
        usernameTextView = (TextView) findViewById(R.id.info_username);
        phoneTextView = (TextView) findViewById(R.id.info_telephone);

        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUtil.logout();
                finish();
            }
        });

        spaceLayout = (SwipeRefreshLayout) findViewById(R.id.space);
        spaceLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_blue_bright, android.R.color.holo_orange_light);
        spaceLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new LoadInfoTask().execute((Void) null);
                        spaceLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new LoadInfoTask().execute((Void) null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_space, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra(USERINFO_EXTRA, info);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public class LoadInfoTask extends AsyncTask<Void, Void, UserInfo> {

        @Override
        protected UserInfo doInBackground(Void... params) {
            return SpaceUtil.loadUserInfo();
        }

        @Override
        protected void onPostExecute(UserInfo info) {
            if (info != null) {
                SpaceActivity.this.info = info;
                nicknameTextView.setText(info.getNickname());
                usernameTextView.setText(info.getUsername());
                phoneTextView.setText(info.getTelephone());
            } else {
                Toast.makeText(getApplicationContext(), R.string.tip_request_info_error, Toast.LENGTH_LONG).show();
            }
        }
    }


}
