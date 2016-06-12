package me.voler.wireless.administrator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import me.voler.wireless.administrator.util.LoginUtil;

public class MainActivity extends AppCompatActivity {

    private static final int ACTION_REGISTER = 1;
    private static final int ACTION_LOGIN = 2;

    private static final int ACTION_SPACE = 3;
    private static final int ACTION_QRCODE = 4;
    private static final int ACTION_COPYRIGHT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.clear();
        if (LoginUtil.checkLogin()) {
            menu.add(Menu.NONE, ACTION_SPACE, Menu.NONE, getString(R.string.action_space));
        } else {
            menu.add(Menu.NONE, ACTION_LOGIN, Menu.NONE, getString(R.string.action_login));
        }
        menu.add(Menu.NONE, ACTION_QRCODE, Menu.NONE, getString(R.string.action_qrcode));
        menu.add(Menu.NONE, ACTION_COPYRIGHT, Menu.NONE, getString(R.string.action_copyright));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == ACTION_LOGIN) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == ACTION_SPACE) {
            Intent intent = new Intent(this, SpaceActivity.class);
            startActivity(intent);
            return true;
        } else if (id == ACTION_QRCODE) {
            Intent intent = new Intent(this, QrcodeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == ACTION_COPYRIGHT) {
            Intent intent = new Intent(this, CopyrightActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
