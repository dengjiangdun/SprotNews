package com.johndon.cmcc.util.sportnews;

import android.content.Context;

import com.androidkun.adapter.BaseAdapter;
import com.androidkun.adapter.ViewHolder;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Created by root on 11/2/17.
 */

public class NewsAdapter extends BaseAdapter<News> {
    private Context mContext;
    private List<News> mListnews;

    public NewsAdapter(Context context, int layoutId, List<News> datas) {
        super(context, layoutId, datas);
        mContext = context;
        mListnews = datas;

    }


    @Override
    public void convert(ViewHolder holder, News news) {
        holder.setText(R.id.tv_title,news.getTitle());
        FinalBitmap.create(mContext).display(holder.getView(R.id.iv_pic),news.getPicUrl());
    }
}
