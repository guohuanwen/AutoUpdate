package com.bcgtgjyb.updatelib;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.bcgtgjyb.updatelib.dialog.DialogActivity;
import com.bcgtgjyb.updatelib.dialog.DialogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by bigwen on 2016/6/26.
 */
public class UpdateUtil {

    private String TAG = UpdateUtil.class.getSimpleName();
    private static UpdateUtil updateUtil;
    private UpdateInfo updateInfo;
    private NetWorkBroadcast netWorkBroadcast;
    private String APPNAME = "update.apk";
    private String localPath = "";
    private int lastNetWork = -1;
    private long downloadId = -2;
    private DownLoadCompleteReceiver downLoadReceiver;

    public static UpdateUtil getInstance(Context context) {
        if (updateUtil == null) {
            updateUtil = new UpdateUtil(context);
        }
        return updateUtil;
    }

    private UpdateUtil(Context context) {
        netWorkBroadcast = new NetWorkBroadcast();
        downLoadReceiver = new DownLoadCompleteReceiver();
        initAppPath(context);
    }

    public void checkUpdate(final Context context) {
        Log.i(TAG, "checkUpdate: ");
        if (checkNetWork(context) == 0) {
            return;
        }
        requestUpdate(context, new UpdateNet.Callback() {
            @Override
            public void onSuccess(UpdateInfo updateInfo) {
                update(context, updateInfo);
            }

            @Override
            public void onFail(String msg) {

            }
        });

        //test
        update(context, test());
    }

    public void registerReceiver(Context context) {
        context.registerReceiver(downLoadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        lastNetWork = checkNetWork(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(netWorkBroadcast, filter);
    }

    public void unRegisterReceiver(Context context) {
        context.unregisterReceiver(downLoadReceiver);

        context.unregisterReceiver(netWorkBroadcast);
    }

    private void update(Context context, UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
        Log.i(TAG, "update: ");
        if (!updateInfo.need_update) {
            return;
        }

        if (checkLocalApk(updateInfo.md5)) {
            //show install dialog
            showInstanllDialog(context, updateInfo.is_force);
            return;
        }
        //show download dialog
        showDownloadDialog(context, updateInfo.is_force);
    }

    private void showDownloadDialog(final Context context, boolean isForce) {
        Log.i(TAG, "showDownloadDialog: ");
        DialogUtil.showUpdateDialog(
                context, "下载更新", "hello world", "取消", "确定",
                isForce, new DialogActivity.Callback() {
                    @Override
                    public void onSure() {
                        download(updateInfo.apkUrl,localPath,context);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    private void showInstanllDialog(final Context context, boolean isForce) {
        Log.i(TAG, "showInstanllDialog: ");
        DialogUtil.showUpdateDialog(
                context, "安装更新", "hello world", "取消", "确定",
                isForce, new DialogActivity.Callback() {
                    @Override
                    public void onSure() {
                        installApk(context,localPath);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    private void requestUpdate(Context context, final UpdateNet.Callback callback) {
        Log.i(TAG, "requestUpdate: ");
        UpdateNet.requestUpdateInfo(context, new UpdateNet.Callback() {
            @Override
            public void onSuccess(UpdateInfo updateInfo) {
                callback.onSuccess(updateInfo);
            }

            @Override
            public void onFail(String msg) {
                callback.onFail(msg);
            }
        });
    }

    private UpdateInfo test() {
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.apkUrl = "http://ddmyapp.kw.tc.qq.com/16891/9DF04CE7F7706D080E05E321A80234BB.apk?mkey=576d005f82ff575e&f=ae10&c=0&fsname=com.devuni.flashlight_10.0.6_20160624.apk&p=.apk";
        updateInfo.is_force = true;
        updateInfo.md5 = "9df04ce7f7706d080e05e321a80234bb";
        updateInfo.need_update = true;
        return updateInfo;
    }

    private boolean checkLocalApk(String md5) {
        String localMd5 = getFileMD5(new File(localPath));
        if (localMd5 == null) {
            return false;
        }
        if (md5.equals(localMd5)) {
            return true;
        }
        return false;
    }

    private void initAppPath(Context context) {
        try {
            localPath = context.getExternalFilesDir(null).getAbsolutePath() + "/" + APPNAME;
        } catch (Exception e) {
            e.printStackTrace();
            localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath() + "/" + APPNAME;
        }
    }

    /**
     * @param context
     * @return 1 wifi , 2 moble ，0 net unable
     */
    private int checkNetWork(Context context) {
        ConnectivityManager mConnectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null){
            return 0;
        }
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && !mNetworkInfo.isAvailable()) {
            //network is not avalilable
            return 0;
        }
        if (mNetworkInfo != null && mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return 1;
        }
        if (mNetworkInfo != null && mNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return 2;
        }
        return 0;
    }

    private String getFileMD5(File file) {
        if (!file.isFile()) {
            Log.d(TAG, "getFileMD5: 文件不存在");
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            Log.d(TAG, "getFileMD5: " + e.toString());
            e.printStackTrace();
            return null;
        }
        BigInteger bigint = new BigInteger(1, digest.digest());
        return bigint.toString(16);
    }

    private boolean installApk(Context context, String apkPath) {
        if (apkPath == null) {
            return false;
        }
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    /**
     * @param fileUrl
     * @param path
     * @param context
     * @return downloadId ->only one,
     */
    private long download(String fileUrl, String path, Context context) {
        Log.i(TAG, "download: ");
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationUri(Uri.fromFile(new File(path)));
        downloadId = downloadManager.enqueue(request);
        return downloadId;
    }

    public class DownLoadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId == reference) {
                //apk download finish,you can do something in this
                showDownloadDialog(context, updateInfo.is_force);
            }
        }
    }

    class NetWorkBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int nowNetWork = checkNetWork(context);
            //first register,the onReceiver() will be invoked,so avoid it
            if (nowNetWork == lastNetWork) {
                return;
            }
            if (nowNetWork != 0) {
                checkUpdate(context);
            }
        }
    }
}
