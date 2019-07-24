package com.sensorsdata.android.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.umeng.message.PushAgent;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushAgent.getInstance(this).onAppStart();
        textView = findViewById(R.id.tv_test);
        /*
         * 注册广播，接收推送展示的数据
         */
        IntentFilter intentFilter = new IntentFilter(getPackageName());
        registerReceiver(sfResultReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sfResultReceiver);
    }

    BroadcastReceiver sfResultReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String text = textView.getText().toString();
            String type = intent.getStringExtra("type");
            String message = intent.getStringExtra("message");
            textView.setText(text + "\ntype = " + type + "\n\t\tmessage = " + message + "\n");
        }
    };
}
