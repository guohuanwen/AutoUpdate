package com.bcgtgjyb.updatelib.dialog;

import android.content.Context;

/**
 * Created by bigwen on 2016/6/26.
 */
public class DialogUtil {

    public static void showUpdateDialog(
            Context context,String title,String content,
            String cancel,String sure,boolean isForce ,
            DialogActivity.Callback callback){
        DialogActivity.goActivity(context,title,content,cancel,sure,isForce,callback);
    }
}
