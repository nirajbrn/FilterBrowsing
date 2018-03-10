package com.android.ahivaran.blockkeyword;

import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    public static final String TAG = SearchActivity.class.getSimpleName();

    private Queue<String> wordQueue;
    @BindView(R.id.webview)
    WebView browserWebView;
    @BindView(R.id.searchEt)
    EditText searchEt;
    @BindView(R.id.errorTv)
    TextView errorTv;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rootLayout)
    ConstraintLayout rootLayout;

    private ArrayList<String> keyWordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setupView();
        keyWordList = getIntent().getStringArrayListExtra("keywords");
        //Implement using queue
        wordQueue = new LinkedList<>();
        WebSettings webSettings = browserWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        browserWebView.setWebViewClient(new BrowserViewClient());
        searchEt.setOnEditorActionListener(new SearchEditorListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupView() {
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimaryDark,
                R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private class BrowserViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG, "onPageStarted():");
            refreshLayout.setRefreshing(true);
            browserWebView.setVisibility(View.INVISIBLE);
            errorTv.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished():");
            if (errorTv.getVisibility() == View.GONE) {
                refillQueue();
                findAndBlock();
            }
            refreshLayout.setRefreshing(false);
        }
    }

    private class SearchEditorListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!TextUtils.isEmpty(v.getText())) {
                    String url = getString(R.string.url, v.getText().toString());
                    browserWebView.loadUrl(url);
                } else {
                    Snackbar.make(rootLayout, "Enter Keyword to search", Snackbar.LENGTH_LONG).show();
                }
                return true;
            }
            return false;
        }
    }
    private void findAndBlock() {
        if (wordQueue.isEmpty()) {
            browserWebView.setVisibility(View.VISIBLE);
            browserWebView.setFindListener(null);
            return;
        }
        String word = wordQueue.remove();
        Log.d(TAG, "findAndBlock(): word: "+word);
        browserWebView.findAllAsync(word);
        browserWebView.setFindListener(new WebView.FindListener() {
            @Override
            public void onFindResultReceived(int activeMatchOrdinal, int numberOfMatches, boolean isDoneCounting) {
                Log.d(TAG, "findAndBlock(): numberOfMatches: "+numberOfMatches);
                if (numberOfMatches > 0) {
                    errorTv.setVisibility(View.VISIBLE);
                    browserWebView.setVisibility(View.INVISIBLE);
                    browserWebView.setFindListener(null);
                    Snackbar.make(rootLayout, R.string.redirect_message, Snackbar.LENGTH_LONG).show();

                } else {
                    findAndBlock();
                }
            }
        });
    }

    private void refillQueue() {
        wordQueue.clear();
        wordQueue.addAll(keyWordList);
    }

}
