package com.gouhao.aidldemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by GouHao on 2017/12/23.
 */

public class CustomCreateService extends Service {
    private String TAG = CustomCreateService.class.getSimpleName();
    private CustomServiceBinder customServiceBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: pid=" + Process.myPid());
        customServiceBinder = new CustomServiceBinder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return customServiceBinder;
    }

    private class CustomServiceBinder extends Binder implements ICustomService{
        private String descriptor = "CustomServiceBinder";

        public CustomServiceBinder() {
            attachInterface(this, descriptor);
        }

        @Override
        public void printMsg(String msg) {
            Log.d(TAG, "printMsg: " + msg + ", pid=" + Process.myPid());
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Log.d(TAG, "onTransact: pid=" + Process.myPid());
            switch (code) {
                case 100:
                    printMsg(data.readString());
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
