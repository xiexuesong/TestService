package com.wangzhen.admin.testservice.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by admin on 2018/11/29.
 */

public class MyMessageService extends Service {

    public class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String content = (String) msg.obj;
                    Toast.makeText(MyMessageService.this,content,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    private Messenger messenger = new Messenger(new MyHandler());

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String getServiceName() {
        return "MyMessageService";
    }
}
