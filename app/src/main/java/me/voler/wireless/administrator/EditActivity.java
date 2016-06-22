package me.voler.wireless.administrator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.voler.wireless.administrator.util.SpaceUtil;
import me.voler.wireless.administrator.util.UserInfo;

public class EditActivity extends AppCompatActivity {

    private EditText nicknameEditText;
    private TextView usernameTextView;
    private TextView phoneTextView;
    private View progressView;
    private View editFormView;

    private EditInfoTask editTask;

    private UserInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        nicknameEditText = (EditText) findViewById(R.id.edit_nickname);
        usernameTextView = (TextView) findViewById(R.id.edit_username);
        phoneTextView = (TextView) findViewById(R.id.edit_telephone);
        Intent intent = getIntent();
        info = (UserInfo) intent.getSerializableExtra(SpaceActivity.USERINFO_EXTRA);
        nicknameEditText.setText(info.getNickname());
        usernameTextView.setText(info.getUsername());
        phoneTextView.setText(info.getTelephone());

        usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.tip_username_editable_false),
                        Toast.LENGTH_SHORT).show();
            }
        });
        phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.tip_phone_editable_false),
                        Toast.LENGTH_SHORT).show();
            }
        });

        editFormView = findViewById(R.id.edit_form);
        progressView = findViewById(R.id.edit_progress);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit_submit) {
            if (editTask != null) {
                return true;
            }
            nicknameEditText.setError(null);
            String nickname = nicknameEditText.getText().toString();
            if (TextUtils.isEmpty(nickname) || !isNicknameValid(nickname)) {
                nicknameEditText.setError(getString(R.string.error_invalid_nickname));
                nicknameEditText.requestFocus();
            } else {
                showProgress(true);
                editTask = new EditInfoTask(nickname);
                editTask.execute((Void) null);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNicknameValid(String nickname) {
        Matcher matcher = Pattern.compile("^[\\w]{5,}$").matcher(nickname);
        return matcher.matches();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            editFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            editFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    editFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            editFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class EditInfoTask extends AsyncTask<Void, Void, UserInfo> {

        private final String nickname;

        public EditInfoTask(String nickname) {
            this.nickname = nickname;
        }

        @Override
        protected UserInfo doInBackground(Void... params) {
            info.setNickname(nickname);
            return SpaceUtil.editUserInfo(info);
        }

        @Override
        protected void onPostExecute(UserInfo info) {
            editTask = null;
            showProgress(false);
            if (nickname.equals(info.getNickname())) {
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_edit_failed, Toast.LENGTH_LONG).show();
            }
        }

    }
}
