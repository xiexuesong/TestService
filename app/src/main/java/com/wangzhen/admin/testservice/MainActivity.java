package com.wangzhen.admin.testservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.wangzhen.admin.testservice.service.MyMessageService;
import com.wangzhen.admin.testservice.service.MyStartService;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startService;
    private Button bindService;
    private Button stopService;
    private MyStartService.LocalBinder localBinder;
    private Messenger messenger;
    private boolean isBind = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof MyStartService.LocalBinder) {
                localBinder = (MyStartService.LocalBinder) service;
                String serviceName = localBinder.getService().getServiceName();
                Log.i("MDL", "serviceName:" + serviceName);
            } else {
                messenger = new Messenger(service);
                Message message = Message.obtain();
                message.what = 1;
                message.obj = "hello";
                try {
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startService = findViewById(R.id.startService);
        bindService = findViewById(R.id.bindService);
        stopService = findViewById(R.id.stopService);

        startService.setOnClickListener(this);
        bindService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        findViewById(R.id.name).setOnClickListener(this);
        findViewById(R.id.unBind).setOnClickListener(this);
        findViewById(R.id.BIND_MESSENGER).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, MyStartService.class);

        switch (v.getId()) {
            case R.id.startService:
                intent.putExtra("id", new Random().nextInt());
                startService(intent);
                break;
            case R.id.bindService:
                //通过扩展Binder类来调用service的方法
                intent.putExtra("id", v.getId());
                isBind = bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                break;
            case R.id.stopService:
                stopService(intent);
                break;
            case R.id.name:
                if (localBinder != null) {
                    String name = localBinder.getService().getServiceName();
                    Log.i("MDL", "name:" + name);
                } else {
                    Log.i("MDL", "为空");
                }
                break;
            case R.id.unBind:
                if (isBind) {
                    unbindService(serviceConnection);
                    isBind = false;
                } else {
                    Log.i("MDL", "未注册");
                }
                break;
            case R.id.BIND_MESSENGER:
                intent.setClass(this, MyMessageService.class);
                bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        Log.i("MDL", "activityDestroy");
        //     finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //   finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
