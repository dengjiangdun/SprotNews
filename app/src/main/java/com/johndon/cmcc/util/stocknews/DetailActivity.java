package com.johndon.cmcc.util.stocknews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import net.tsz.afinal.FinalBitmap;

/**
 * Created by root on 11/6/17.
 */

public class DetailActivity extends AppCompatActivity {
    private WebView mWv;
    private Toolbar mTb;
    private CollapsingToolbarLayout toolbarLayout;
    private ImageView mIvPic;
    private TextView mTvSrc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTb);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(getIntent().getStringExtra("title"));
        mIvPic = (ImageView) findViewById(R.id.iv_detail);
        mWv = (WebView) findViewById(R.id.wv_details);
        mTvSrc = (TextView) findViewById(R.id.tv_src);
        mWv.loadData(getIntent().getStringExtra("content"),"text/html; charset=UTF-8", null);
        FinalBitmap finalBitmap = FinalBitmap.create(this);
        finalBitmap.display(mIvPic,getIntent().getStringExtra("picture"));
        mTvSrc.setText("新闻来源:"+getIntent().getStringExtra("src"));
        FloatingActionButton buttonShare = (FloatingActionButton)  findViewById(R.id.fab_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("weburl"));
                intent.setType("text/plain");
                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent,"分享到"));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWv.removeAllViews();
        mWv.destroy();
    }
}
