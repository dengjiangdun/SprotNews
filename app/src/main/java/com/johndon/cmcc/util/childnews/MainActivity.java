package com.johndon.cmcc.util.childnews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.List;

import util.cmcc.johndon.com.show.CMCCProgressDialog;

public class MainActivity extends AppCompatActivity implements NewsAdapter.NewsItemClickListener{
    private PullToRefreshRecyclerView mRvNews;
    private List<News> mListNews;
    private int page = 0;
    private NewsAdapter mNewsAdater;
    private boolean isRefresh = true;
    private CMCCProgressDialog cmccDialog;
    private static final String URL_GET_NEWS = "https://way.jd.com/jisuapi/get?channel=育儿&num=10&start=%d&appkey=efe1e02d6bd91de63c8a07d1666910d3";
   //数据简介：头条、新闻、财经、体育、娱乐、军事、教育、科技、NBA、股票、星座、女性、健康、育儿等频道热门新闻

    //体育 sprotnews sport_icon
    //股票 stocknews stock_icon
    //财经 financenews finance_icon
    //NBA  nbanews nba_icon
    //女性 womennews women_icon
    //育儿 childnews child_icon
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRvNews = (PullToRefreshRecyclerView) findViewById(R.id.rv_news);
        mRvNews.setHasFixedSize(true);
        cmccDialog = new CMCCProgressDialog(this);
        initRecyclerView();
        loadNews();
    }

    private void initRecyclerView() {
        mListNews = new ArrayList<>();
        mNewsAdater = new NewsAdapter(MainActivity.this, R.layout.item_news, mListNews);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvNews.setLayoutManager(linearLayoutManager);
        mRvNews.setAdapter(mNewsAdater);

        mRvNews.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                isRefresh = true;
                loadNews();
            }

            @Override
            public void onLoadMore() {
                page++;
                isRefresh = false;
                loadNews();
            }
        });

        mNewsAdater.setNewsItemClickListener(this);
    }

    private void loadNews() {
        cmccDialog.show();
        FinalHttp finalHttp = new FinalHttp();
        finalHttp.get(String.format(URL_GET_NEWS,page), new AjaxCallBack<String>() {
            @Override
            public void onSuccess(final String s) {
                super.onSuccess(s);
                Context context = getBaseContext();
                if (context != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cmccDialog.dismiss();
                            dealNeews(s);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t, final int errorNo, final String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Context context = getBaseContext();
                if (context != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cmccDialog.dismiss();
                            showShortToast(strMsg+"error_code"+errorNo);
                        }
                    });
                }
            }
        });
    }

    private void dealNeews(String str) {

        if (isRefresh) {
            mRvNews.setRefreshComplete();
            mListNews.clear();
        } else {
            mRvNews.setLoadMoreComplete();
        }

        JSONObject jsonObject = JSON.parseObject(str);
        int code = jsonObject.getInteger("code");
        String msg = jsonObject.getString("msg");
        if (code == 10000) {
            String  result = jsonObject.getString("result");
            JSONObject resultJS = JSON.parseObject(result);
            JSONObject resultSeond = JSON.parseObject(resultJS.getString("result"));
            String list = resultSeond.getString("list");
            mListNews.addAll(JSON.parseArray(list,News.class));
            mNewsAdater.notifyDataSetChanged();

            int num = resultSeond.getIntValue("num");
            if (num < 10) {
                mRvNews.setLoadingMoreEnabled(false);
                showShortToast("没有更多可以加载 !");
            }

        } else {
            showShortToast(msg);
        }
    }


    @Override
    public void getNews(News news) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("content",news.getContent());
        intent.putExtra("title",news.getTitle());
        intent.putExtra("picture",news.getPic());
        intent.putExtra("src",news.getSrc());
        intent.putExtra("weburl",news.getWeburl());
        startActivity(intent);
    }

    private void showShortToast(String mgs) {
        if ( !TextUtils.isEmpty(mgs)) {
            Toast.makeText(this,mgs,Toast.LENGTH_SHORT).show();
        }
    }

}
