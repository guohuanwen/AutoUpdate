package com.bcgtgjyb.updatelib.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.bcgtgjyb.updatelib.R;

/**
 * Created by bigwen on 2016/6/26.
 */
public class DialogActivity extends Activity implements View.OnClickListener {

    private TextView title;
    private TextView content;
    private TextView cancel;
    private TextView sure;
    private static Callback callback;

    private String titleText;
    private String contextText;
    private String cancelText;
    private String sureText;
    private boolean isForce = false;

    private static final String TITLET = "title";
    private static final String CONTENTT = "cantent";
    private static final String CANCEL = "cancel";
    private static final String SURE = "sure";
    private static final String ISFORCE = "force";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        setContentView(R.layout.activity_dialog);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        cancel = (TextView) findViewById(R.id.cancel);
        sure = (TextView) findViewById(R.id.sure);

        title.setText(titleText);
        content.setText(contextText);
        cancel.setText(cancelText);
        sure.setText(sureText);

        cancel.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    private void initIntent() {
        titleText = getIntent().getStringExtra(TITLET);
        contextText = getIntent().getStringExtra(CONTENTT);
        cancelText = getIntent().getStringExtra(CANCEL);
        sureText = getIntent().getStringExtra(SURE);
        isForce = getIntent().getBooleanExtra(ISFORCE, false);
    }

    public static void goActivity(
            Context context, String titleText, String contextText,
            String cancelText, String sureText, boolean isForce,Callback c) {
        Intent intent = new Intent(context, DialogActivity.class);
        intent.putExtra(TITLET, titleText);
        intent.putExtra(CONTENTT, contextText);
        intent.putExtra(CANCEL, cancelText);
        intent.putExtra(SURE, sureText);
        intent.putExtra(ISFORCE, isForce);
        context.startActivity(intent);
        callback = c;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == cancel.getId()) {
            if (!isForce) {
                finish();
            }else {
                //finish all activity then exit
            }
            return;
        }
        if (v.getId() == sure.getId()) {
            if (callback != null) {
                callback.onSure();
            }
            return;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isForce) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public interface Callback {
        void onSure();
        void onCancel();
    }
}
