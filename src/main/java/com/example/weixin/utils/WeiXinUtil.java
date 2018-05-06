package com.example.weixin.utils;

import com.example.weixin.model.AccessToken;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class WeiXinUtil {

    private static final String APPID = "wxe29e03bbb3e0a2a0";
    private static final String APPSECRET = "b450e68acffe1e8befedff04448d53f8";

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?" +
            "grant_type=client_credential&appid=" + APPID + "&secret=" + APPSECRET;


    public static JSONObject doGet(String url) {

        HttpClientBuilder builder = HttpClientBuilder.create();

        HttpGet get = new HttpGet(url);

        JSONObject jsonObject = null;

        HttpResponse response = null;
        try {
            response = builder.build().execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                jsonObject = JSONObject.fromObject(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static JSONObject doPost(String url, String param) {

        HttpClientBuilder builder = HttpClientBuilder.create();

        HttpPost post = new HttpPost(url);

        JSONObject jsonObject = null;

        post.setEntity(new StringEntity(param, "UTF-8"));

        HttpResponse response = null;

        try {
            response = builder.build().execute(post);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");

            jsonObject = JSONObject.fromObject(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static AccessToken getAccessToken1() {
        AccessToken accessToken = new AccessToken();
        JSONObject jsonObject = doGet(ACCESS_TOKEN_URL);
        if (jsonObject != null) {
            accessToken.setExpiresIn(jsonObject.getString("expires_in"));
            accessToken.setToken(jsonObject.getString("access_token"));
        }
        return accessToken;

    }

    public synchronized static String getAccessToken() {
        //保存access_token文件名字
        String FileName = "AccessToken.properties";
        try {
            // 从文件中获取token值及时间
            Properties prop = new Properties();// 属性集合对象
            //获取文件流
            InputStream fis = WeiXinUtil.class.getClassLoader().getResourceAsStream(FileName);
            prop.load(fis);// 将属性文件流装载到Properties对象中
            fis.close();// 关闭流

            //获取Appid，APPsecret
            String APPID = prop.getProperty("APPID");
            String APPSECRET = prop.getProperty("APPSECRET");
            // 获取accesstoken，初始值为空，第一次调用之后会把值写入文件
            String access_token = prop.getProperty("access_token");
            String expires_in = prop.getProperty("expires_in");
            String last_time = prop.getProperty("last_time");


            int int_expires_in = 0;
            long long_last_time = 0;


            try {
                int_expires_in = Integer.parseInt(expires_in);
                long_last_time = Long.parseLong(last_time);

            } catch (Exception e) {

            }
             //得到当前时间
            long current_time = System.currentTimeMillis();

            // 每次调用，判断expires_in是否过期，如果token时间超时，重新获取，expires_in有效期为7200
            if ((current_time - long_last_time) / 1000 >= int_expires_in) {
                JSONObject jsonObject = doGet(ACCESS_TOKEN_URL);
                if (jsonObject != null) {
                    prop.setProperty("expires_in",jsonObject.getString("expires_in"));
                    prop.setProperty("access_token",jsonObject.getString("access_token"));
                    prop.setProperty("last_time", System.currentTimeMillis() + "");

                    URL url_ = WeiXinUtil.class.getClassLoader().getResource(FileName);
                    FileOutputStream fos = new FileOutputStream(new File(url_.toURI()));
                    prop.store(fos, null);
                    fos.close();// 关闭流
                }
                //如果已经过期返回获取到的access_token
                return jsonObject.getString("access_token");
            }
            else {
                //如果没有过期，返回从文件中读取的access_token
                return access_token;
            }
        }catch (Exception e) {
            return null;
        }
    }
}