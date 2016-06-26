package com.bcgtgjyb.updatelib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.HashMap;

/**
 * Created by bigwen on 2016/6/26.
 */
public class UpdateNet {
    public static final String UPDATE_URL = "";

    public static void requestUpdateInfo(Context context, UpdateNet.Callback callback){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("version_code",getVersionCode(context)+"");
        post(UPDATE_URL,hashMap,callback);
    }

    private static void post(String url, HashMap<String,String> hashMap, UpdateNet.Callback callback){

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
