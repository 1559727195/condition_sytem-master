package com.ui.adapter.helper;

import com.crazysunj.domain.entity.base.MultiTypeIdEntity;
import com.crazysunj.domain.entity.zhihu.ZhihuNewsEntity;
import com.crazysunj.multitypeadapter.helper.AsynAdapterHelper;
import com.massky.conditioningsystem.R;
import javax.inject.Inject;



public class HomeAdapterHelper extends AsynAdapterHelper<MultiTypeIdEntity> {
    public static final int LEVEL_ZHIHU = 0;
    public static final int MIN_ZHIHU = 2;
    @Inject
    public HomeAdapterHelper() {
        super(null);
    }

    @Override
    protected void registerModule() {
        registerModule(LEVEL_ZHIHU)
                .type(ZhihuNewsEntity.StoriesEntity.TYPE_ZHIHU_NEWS)
                .layoutResId(R.layout.detail_home_device)

                .minSize(MIN_ZHIHU)
                .isFolded(true)
                .register();
    }
}
