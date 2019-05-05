package com.massky.conditioningsystem.base;

import android.widget.Toast;

import com.crazysunj.data.util.NetworkUtils;
import com.crazysunj.domain.util.LoggerUtil;
import com.massky.conditioningsystem.Utils.App;

import io.reactivex.subscribers.DisposableSubscriber;

public abstract class BaseSubscriber<T> extends DisposableSubscriber<T> {

    @Override
    public void onError(Throwable e) {
        NetworkUtils.isNetworkAvailable(isAvailable -> Toast.makeText(App.getInstance(), isAvailable ? e.getMessage() : "请检测你的网络是否畅通", Toast.LENGTH_SHORT).show());
        LoggerUtil.e(LoggerUtil.MSG_HTTP, e);
    }

    @Override
    public void onComplete() {

    }
}
