package com.crazysunj.domain.bus;

public class DownloadEvent {
    public int taskId;
    public long total;
    public long loaded;

    public DownloadEvent(int taskId, long total, long loaded) {
        this.taskId = taskId;
        this.total = total;
        this.loaded = loaded;
    }
}
