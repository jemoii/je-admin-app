package me.voler.wireless.administrator.util;

import android.app.Application;
import android.content.Context;

public class CustomApplication extends Application {

    /**
     * 全局上下文
     */
    private static Context GlobalContext;

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalContext = getApplicationContext();
    }

    /**
     * @return 全局上下文
     */
    public static Context getContext() {
        return GlobalContext;
    }

}
