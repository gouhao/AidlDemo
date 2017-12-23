package com.gouhao.aidldemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gouhao.aidldemo.MainActivity;

/**
 * Created by GouHao on 2017/12/23.
 */

public class MessengerService extends Service {
    private static String TAG = MessengerService.class.getSimpleName();

    private Messenger messenger;

    public final static int WHAT_PRINT_MSG = 1;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: pid=" + Process.myPid());

        messenger = new Messenger(new InnerHandler());
    }

    private class InnerHandler extends Handler{
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case WHAT_PRINT_MSG:
                    Log.d(TAG, "receive msg: " + msg.getData().getString("data") + ", pid=" + Process.myPid());
                    Messenger reply = msg.replyTo;
                    Message replyMsg = Message.obtain();
                    replyMsg.what = MainActivity.WHAT_REPLY_MESSAGE;
                    Bundle replyData = new Bundle();
                    replyData.putString("reply", "this is server reply");
                    replyMsg.setData(replyData);
                    try {
                        reply.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            super.dispatchMessage(msg);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onDestroy() {
        messenger = null;
        super.onDestroy();
    }
}
