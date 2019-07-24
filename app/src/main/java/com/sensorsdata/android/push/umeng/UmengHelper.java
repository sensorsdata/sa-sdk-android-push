package com.sensorsdata.android.push.umeng;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.sensorsdata.analytics.android.sdk.SALog;
import com.sensorsdata.android.push.SFConstant;
import com.sensorsdata.android.push.SFLogger;
import com.sensorsdata.android.push.SFUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.Map;

public class UmengHelper {
    private static final String TAG = "UmengHelper";

    /**
     * 自定义通知栏打开动作,自定义行为的数据放在 UMessage.custom 字段。
     * 在【友盟+】后台或通过 API 发送消息时，在“后续动作”中的“自定义行为”中输入相应的值或代码即可实现。
     * 若开发者需要处理自定义行为，则可以重写方法 dealWithCustomAction()。其中自定义行为的内容，存放在 UMessage.custom 中。
     * 请在自定义 Application 类中添加以下代码.
     *
     * @param pushAgent PushAgent
     */
    public static void registerNotificationCallback(PushAgent pushAgent) {
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void launchApp(Context context, UMessage msg) {
                super.launchApp(context, msg);
                SFLogger.d(TAG, "launchApp");
                handleUmengMessage(context, msg);
            }
            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
                SFLogger.d(TAG, "openUrl");
                handleUmengMessage(context, msg);
            }
            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
                SFLogger.d(TAG, "openActivity");
                handleUmengMessage(context, msg);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                super.dealWithCustomAction(context, msg);
                SFLogger.d(TAG, "dealWithCustomAction");
                handleUmengMessage(context, msg);
            }
        };
        pushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    /**
     * 若开发者要使用自定义消息，则需重在自定义 Application 类的 onCreate() 中重写 dealWithCustomMessage() 方法，
     * 自定义消息的内容存放在 UMessage.custom 字段里。拦截自定义参数根据友盟官网介绍在 getNotification 中拦截。
     *
     * @param pushAgent PushAgent
     */
    public static void registerMessageCallback(PushAgent pushAgent) {
        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        SFLogger.d(TAG, "UMessage = " + msg.custom);
                        handleUmengMessage(context, msg);
                    }
                });
            }
        };

        pushAgent.setMessageHandler(messageHandler);
    }

    private static void handleUmengMessage(Context context, UMessage uMessage) {
        try {
            if (uMessage == null) return;
            if (!TextUtils.isEmpty(uMessage.custom)) {
                SFUtils.sendBroadcast(context,  SFConstant.PUSH_TYPE_UMENG,  "自定义参数---" + uMessage.custom);
            }
            if (uMessage.extra != null) {
                for (Map.Entry entry : uMessage.extra.entrySet()) {
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if (SFConstant.SF_DATA.equals(key)) {
                        SFUtils.sendBroadcast(context, SFConstant.PUSH_TYPE_UMENG, key + ":" + value);
                        SFUtils.handleSFConfig(context, value.toString());
                        SFUtils.trackAppOpenNotification(context, value.toString(), uMessage.msg_id, uMessage.title, uMessage.text);
                    }
                }
            }
        } catch (Exception ex) {
            SALog.printStackTrace(ex);
        }
    }
}
