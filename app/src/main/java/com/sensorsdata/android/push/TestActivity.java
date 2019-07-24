package com.sensorsdata.android.push;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.umeng.message.PushAgent;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        PushAgent.getInstance(this).onAppStart();
    }
}
