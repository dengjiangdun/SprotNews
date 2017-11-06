package com.johndon.cmcc.util.stocknews;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

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
    private NewsItemClickListener mNewsItemClickListener;

    public NewsItemClickListener getNewsItemClickListener() {
        return mNewsItemClickListener;
    }

    public void setNewsItemClickListener(NewsItemClickListener mNewsItemClickListener) {
        this.mNewsItemClickListener = mNewsItemClickListener;
    }

    public NewsAdapter(Context context, int layoutId, List<News> datas) {
        super(context, layoutId, datas);
        mContext = context;
        mListnews = datas;

    }


    @Override
    public void convert(ViewHolder holder, final News news) {
        holder.setText(R.id.tv_title,news.getTitle());
        String url = news.getPic();
        if ( !TextUtils.isEmpty(url)) {
            FinalBitmap.create(mContext).display(holder.getView(R.id.iv_pic),news.getPic());
        } else {
            holder.setImageResource(R.id.iv_pic,R.drawable.no_picture_icon);
        }

        holder.setOnclickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNewsItemClickListener != null) {
                    mNewsItemClickListener.getNews(news);
                }
            }
        });
    }

    public interface NewsItemClickListener{
        public void getNews(News news);
    }

}
