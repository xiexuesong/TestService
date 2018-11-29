package com.wangzhen.admin.testservice.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * onCreate()只会运行一次
 * startService生命周期 onCreate ---> onStartCommand --> onDestroy
 * bindService生命周期 onCreate ---> onBind -->unBind --> onDestroy
 * <p>
 * <p>
 * <p>
 * IntentService 和 Service
 * intentService 会在onStartCommand创建一个工作线程，按照队列的方式去一个一个处理进来的任务
 * service 处理请求时在主线程中操作的，所以处理耗时任务时，还是需要去创建线程，这也是两者最大的区别
 * Created by admin on 2018/11/29.
 */

public class MyStartService extends Service {

    /**
     * 在startService中会被调用
     * startService 通过StopService或者StopSelf来调用销毁
     * <p>
     * bindService不会调用该方法
     * bindService是与启动service的组件同生命周期，当启动的service的组件销毁是时即销毁，也可以通过unBindService来销毁服务
     * <p>
     * 服务正常销毁是都会运行ondDestroy()方法
     * <p>
     * service默认实在UI线程中进行的，不可以在服务中进行耗时操作，需要在服务中另起线程。另外也可以在AndroidManifest中通过process来指定服务运行的进程
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    private IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MyStartService getService() {
            return MyStartService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MDL", "id:" + intent.getIntExtra("id", 0));
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("MDL","onBind");
        return binder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Log.i("MDL","unBindService");
        super.unbindService(conn);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MDL","onCreate");
    }

    /**
     * 销毁服务
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MDL","onDestroy");
    }

    public String getServiceName(){
        return "com.wangzhen.admin.service.mystartservice";
    }
}
