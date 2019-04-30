package com.crazysunj.data.util;

import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.operators.flowable.FlowableTakeUntil;
import io.reactivex.schedulers.Schedulers;

public class RxTransformerUtil {
    public static<T> FlowableTransformer<T,T>  normalTransformer() {
        return observable-> observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
