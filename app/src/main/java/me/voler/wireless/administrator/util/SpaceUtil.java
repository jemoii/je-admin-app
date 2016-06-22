package me.voler.wireless.administrator.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpaceUtil {
    private static final String PROD_SERVER = "http://duapp.voler.me";
    private static final String LOCAL_SERVER = "http://172.16.163.1:8080";

    public static UserInfo loadUserInfo() {
        String username = LoginUtil.getLoginUsername();
        if (username == null) {
            return new UserInfo();
        }
        String jsonResponse = "{}";
        try {
            jsonResponse = Jsoup.connect(LOCAL_SERVER + "/jeadmin/app/space.json")
                    .data("username", username).ignoreContentType(true)
                    .method(Connection.Method.GET).timeout(15000).execute().body();
        } catch (IOException e) {
            return null;
        }

        JSONObject response = JSONObject.parseObject(jsonResponse);
        if (response.getBoolean("status")) {
            return response.getObject("obj", UserInfo.class);
        } else {
            return null;
        }
    }

    public static UserInfo editUserInfo(UserInfo info) {
        String jsonResponse = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(LOCAL_SERVER + "/jeadmin/space.json").openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(JSON.toJSONString(info));
            outputStream.flush();
            outputStream.close();

            if (conn.getResponseCode() == 200) {
                InputStreamReader inputStream = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(inputStream);
                String line = "";
                while ((line = reader.readLine()) != null) {
                    jsonResponse += line;
                }
                inputStream.close();
                conn.disconnect();
            }
        } catch (IOException e) {
            return new UserInfo();
        }

        JSONObject response = JSONObject.parseObject(jsonResponse);
        if (response.getBoolean("status")) {
            return response.getObject("obj", UserInfo.class);
        } else {
            return new UserInfo();
        }
    }

}
