package com.ui.adapter;

import android.app.Activity;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.BaseHelperAdapter;
import com.base.BaseViewHolder;
import com.crazysunj.domain.entity.base.MultiTypeIdEntity;
import com.crazysunj.domain.entity.zhihu.ZhihuNewsEntity;
import com.crazysunj.multitypeadapter.helper.AdapterHelper;
import com.massky.conditioningsystem.R;
import com.ui.adapter.helper.HomeAdapterHelper;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import static com.massky.conditioningsystem.Util.DisplayUtil.dip2px;

public class HomeAdapter  extends BaseHelperAdapter<MultiTypeIdEntity, BaseViewHolder, HomeAdapterHelper>{

//    @Named(EntityModule.NAME_ZHIHU)
//    @Inject
//    CommonHeaderEntity mZhihuHeaderEntity;

    @Inject
    public HomeAdapter(HomeAdapterHelper helper) {
        super(helper);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, @NonNull MultiTypeIdEntity item) {
        switch (item.getItemType()) {
            case ZhihuNewsEntity.StoriesEntity.TYPE_ZHIHU_NEWS:
                renderZhihuNews(holder, (ZhihuNewsEntity.StoriesEntity) item);
                break;
        }
    }

    private void renderZhihuNews(BaseViewHolder holder, ZhihuNewsEntity.StoriesEntity item) {
        // 根据列数计算项目宽度，以使总宽度尽量填充屏幕
        int itemWidth = ((mContext.getResources().getDisplayMetrics().widthPixels
        ) / 2 - dip2px(mContext, 6)) / 2;
        int itemHeight = itemWidth;
        AbsListView.LayoutParams params = (AbsListView.LayoutParams) holder.itemView.getLayoutParams();
        if (params == null) {
            params = new AbsListView.LayoutParams(
                    itemWidth,
                    itemHeight / 5 * 3 + itemHeight / 10 / 2);
            holder.itemView.setLayoutParams(params);
        } else {
            params.height = itemHeight;//
            params.width = itemWidth;
        }











//        final AppCompatImageView icon = holder.getView(R.id.item_zhihu_news_icon);
//        ImageLoader.load(mContext, getUrl(item.getImages()), icon);
//        holder.setText(R.id.item_zhihu_news_title, item.getTitle());
//        holder.itemView.setOnClickListener(v -> ZhihuNewsDetailActivity.start((Activity) v.getContext(), item.getId(), icon));
    }



    public void notifyZhihuNewsList(List<ZhihuNewsEntity.StoriesEntity> data) {
        final int level = HomeAdapterHelper.LEVEL_ZHIHU;
        final String title = String.format(Locale.getDefault(), "展开（剩余%d个）", data.size() - HomeAdapterHelper.MIN_ZHIHU);
//        mZhihuFooterEntity.initStatus(title);
        AdapterHelper.with(level)
                .data(data)
                .into(mHelper);

    }
}
