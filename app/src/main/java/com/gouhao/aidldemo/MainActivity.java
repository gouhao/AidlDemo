package com.gouhao.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Process;
import android.os.RemoteException;
import android.os.Trace;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.gouhao.aidldemo.service.AidlCreateService;
import com.gouhao.aidldemo.service.CustomCreateService;

/**
 * Created by GouHao on 2017/12/23.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isBindCustomService;
    private boolean isBindAutoService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: pid=" + Process.myPid());
        setContentView(R.layout.action_main);
        findViewById(R.id.btn_bind_custom_service).setOnClickListener(this);
        findViewById(R.id.btn_bind_auto_service).setOnClickListener(this);
    }

    private IDemoService demoService;
    IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.e(TAG, "binderDied");
        }
    };
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                service.linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            demoService = IDemoService.Stub.asInterface(service);
            Log.d(TAG, "service connected.");
            try {
                demoService.printMsg("This is MainActivity");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected");
        }
    };

//    private ICustomService customService;

    private ServiceConnection customServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            customService = (ICustomService) service;

            Log.d(TAG, "custom service connection");
//            customService.printMsg("Hello World");
            Parcel data = Parcel.obtain();
            data.writeString("Hello CustomCreateService");
            try {
                Log.d(TAG, "send pid: " + Process.myPid());
                service.transact(100, data, null, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind_custom_service:
                isBindCustomService = true;
                bindService(new Intent(this, CustomCreateService.class), customServiceConnection, BIND_AUTO_CREATE);
                break;
            case R.id.btn_bind_auto_service:
                isBindAutoService = true;
                bindService(new Intent(this, AidlCreateService.class), serviceConnection, BIND_AUTO_CREATE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(isBindCustomService) {
            unbindService(customServiceConnection);
//            customService = null;
            customServiceConnection = null;
        }
        if(isBindAutoService) {
            ((Binder)demoService).unlinkToDeath(deathRecipient, 0);
            unbindService(serviceConnection);
            serviceConnection = null;
            demoService = null;
        }
        super.onDestroy();
    }
}
