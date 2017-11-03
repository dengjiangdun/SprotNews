package com.johndon.cmcc.util.sportnews;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PullToRefreshRecyclerView mRvNews;
    private List<News> mListNews;
    private int page = 1;
    private NewsAdapter mNewsAdater;
    private static final String URL_GET_NEWS = "http://api.avatardata.cn/SportsNews/Query?key=bb62b11905e44bf9aee2ef6fed2e041a&page=%d&rows=10";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRvNews = (PullToRefreshRecyclerView) findViewById(R.id.rv_news);
        mRvNews.setHasFixedSize(true);
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

            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    private void loadNews() {
        FinalHttp finalHttp = new FinalHttp();
        finalHttp.get(String.format(URL_GET_NEWS, page), new AjaxCallBack<String>() {
            @Override
            public void onSuccess(final String s) {
                super.onSuccess(s);
                Context context = getBaseContext();
                if (context != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dealNeews(s);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void dealNeews(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int error_code = jsonObject.getInteger("error_code");
        String reason = jsonObject.getString("reason");
        if (error_code == 0) {
            /*String  result = jsonObject.getString("result");
            JSONObject jsonObjectdata = JSON.parseObject(result);
            mListNews.addAll(JSON.parseArray(jsonObjectdata.getString("data"),News.class));*/

        }
        Log.d("TAG", "dealNeews: "+str);
    }



}
