package me.voler.wireless.administrator.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class LoginUtil {

    private static final String PROD_SERVER = "http://duapp.voler.me";
    private static final String LOCAL_SERVER = "http://172.16.163.1:8080";

    private static final String PREFERENCE_LOGIN = "me.voler.wireless.administrator.preference.login";
    private static final String KEY_LOGIN_SUCCESS = "me.voler.wireless.administrator.key.login";

    public static int login(String username, String password) {
        String jsonResponse = "{}";
        try {
            jsonResponse = Jsoup.connect(LOCAL_SERVER + "/jeadmin/login.json")
                    .data("username", username).data("password", password)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST).timeout(15000).execute().body();
        } catch (IOException e) {
            return LoginError.SYSTEM_ERROR.getErrCode();
        }

        JSONObject response = JSONObject.parseObject(jsonResponse);
        if (response.getBoolean("status")) {
            saveLogin(username);
            return LoginError.NONE_ERROR.getErrCode();

        } else if (response.getString("obj").equals("SYSTEM_ERROR")) {
            return LoginError.SYSTEM_ERROR.getErrCode();
        } else if (response.getString("obj").equals("NOT_EQUAL_ERROR")) {
            return LoginError.NOT_EQUAL_ERROR.getErrCode();
        } else if (response.getString("obj").equals("EMAIL_ERROR")) {
            return LoginError.EMAIL_ERROR.getErrCode();
        } else {
            return LoginError.SYSTEM_ERROR.getErrCode();
        }
    }

    public static void logout() {
        SharedPreferences sharedPref = CustomApplication.getContext().getSharedPreferences(PREFERENCE_LOGIN, Context.MODE_PRIVATE);
        if (sharedPref.getString(KEY_LOGIN_SUCCESS, null) != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(KEY_LOGIN_SUCCESS);
            editor.commit();
        }
    }

    /**
     * 保存用户成功登录信息
     *
     * @param username 成功登录的用户名
     */
    private static void saveLogin(String username) {
        SharedPreferences sharedPref = CustomApplication.getContext().getSharedPreferences(PREFERENCE_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_LOGIN_SUCCESS, username);
        editor.commit();
    }

    /**
     * @return 用户当前是否成功登录
     */
    public static boolean checkLogin() {
        return getLoginUsername() != null;
    }

    /**
     * 用户未成功登录时返回{@code null}
     *
     * @return 用户成功登录后保存的用户名
     */
    public static String getLoginUsername() {
        SharedPreferences sharedPref = CustomApplication.getContext().getSharedPreferences(PREFERENCE_LOGIN, Context.MODE_PRIVATE);
        return sharedPref.getString(KEY_LOGIN_SUCCESS, null);
    }

}
