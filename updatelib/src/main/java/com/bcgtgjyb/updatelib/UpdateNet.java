package com.bcgtgjyb.updatelib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bigwen on 2016/6/26.
 */
public class UpdateNet {
    public static final String UPDATE_URL = "";
    private static final String TAG = UpdateUtil.class.getSimpleName();

    public static void requestUpdateInfo(Context context,final UpdateNet.Callback callback){
        Log.i(TAG, "requestUpdateInfo: ");
        final HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("version_code",getVersionCode(context)+"");
        hashMap.put("channel","official");
        new Thread(new Runnable() {
            @Override
            public void run() {
                post(UPDATE_URL,hashMap,callback);
            }
        }).start();
    }

    private static void post(String url, HashMap<String,String> hashMap,final UpdateNet.Callback callback){
        InputStream inputStream = null;
        InputStreamReader isr = null;
        OutputStream outputStream = null;
        Handler handler = new Handler(Looper.getMainLooper());

        try {
            URL uurl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) uurl.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(1000*10);
            connection.setReadTimeout(1000*10);
            connection.setDoOutput(true);

            outputStream = connection.getOutputStream();
            String content = makeParam(hashMap);
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();

            inputStream = connection.getInputStream();
            isr = new InputStreamReader(inputStream);
            char [] buffer = new char[1024];
            StringBuffer stringBuffer = new StringBuffer();
            int len = 0;
            while ((len =isr.read(buffer,0,buffer.length)) > 0){
                stringBuffer.append(buffer,0,len);
            }
            String result = stringBuffer.toString();
            Log.i(TAG, "post: "+result+"  param = "+content);
            JSONObject jsonObject = new JSONObject(result);
            final UpdateInfo updateInfo = new UpdateInfo();
            updateInfo.need_update = jsonObject.getBoolean("need_update");
            updateInfo.md5 = jsonObject.getString("md5");
            updateInfo.apkUrl = jsonObject.getString("url");
            updateInfo.is_force = jsonObject.getBoolean("is_force");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(updateInfo);
                }
            });
        }catch (final Exception e){
            Log.i(TAG, "post: "+e.toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onFail(e.toString());
                }
            });
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null){
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String makeParam(HashMap<String,String> hashMap){
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            stringBuilder.append(entry.getKey()+"="+entry.getValue()+"&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private static int getVersionCode(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public interface Callback{
        void onSuccess(UpdateInfo updateInfo);
        void onFail(String msg);
    }
}
