package com.crazysunj.domain.entity.common;

import com.crazysunj.domain.entity.base.MultiTypeIdEntity;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;

public class CommonHeaderEntity extends MultiTypeIdEntity {
    private String id;
    private int type;
    private String title;
    private String options;

    public CommonHeaderEntity(String id, int level, String title, String options) {
        this.id = id;
        this.type = level - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
        this.title = title;
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public String getOptions() {
        return options;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    @Override
    public String getStringId() {
        return id;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
