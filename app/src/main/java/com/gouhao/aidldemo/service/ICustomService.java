package com.gouhao.aidldemo.service;

import android.os.IInterface;

/**
 * Created by GouHao on 2017/12/23.
 */

public interface ICustomService extends IInterface{
    void printMsg(String msg);
}
