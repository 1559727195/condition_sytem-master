package com.crazysunj.domain.bus;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

public class RxBus {
    private static volatile RxBus instance;
    private final Map<String, FlowableProcessor<Object>> processorMap;

    private RxBus() {
        processorMap = new HashMap<>();
    }

    public static RxBus getDefault() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    public void post(String tag, Object event) {
        FlowableProcessor<Object> processor = processorMap.get(tag);
        if (processor == null) {
            processor = PublishProcessor.create().toSerialized();
            processorMap.put(tag, processor);
        }
        processor.onNext(event);
    }

    public <T> Flowable<T> toFlowable(String tag, Class<T> eventType) {
        FlowableProcessor<Object> processor = processorMap.get(tag);
        if (processor == null) {
            processor = PublishProcessor.create().toSerialized();
            processorMap.put(tag, processor);
        }
        return processor.ofType(eventType);
    }

    public boolean hasSubscribers(String tag) {
        FlowableProcessor<Object> processor = processorMap.get(tag);
        if (processor == null) {
            processor = PublishProcessor.create().toSerialized();
            processorMap.put(tag, processor);
        }
        return processor.hasSubscribers();
    }
}
