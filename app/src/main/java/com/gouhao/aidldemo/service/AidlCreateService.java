package com.gouhao.aidldemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gouhao.aidldemo.IDemoService;

/**
 * Created by GouHao on 2017/12/23.
 */

public class AidlCreateService extends Service {
    private static String TAG = AidlCreateService.class.getSimpleName();
    private AutoServiceBinder binder;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: pid=" + Process.myPid());

        binder = new AutoServiceBinder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private class AutoServiceBinder extends IDemoService.Stub {

        @Override
        public void printMsg(String msg) throws RemoteException {
            Log.d(TAG, "printMsg: " + msg);
        }
    }
}
