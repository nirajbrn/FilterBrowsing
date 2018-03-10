package com.android.ahivaran.blockkeyword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.android.ahivaran.blockkeyword.adapter.KeywordAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LauncherActivity extends AppCompatActivity {

    @BindView(R.id.addBtn)
    Button addBtn;
    @BindView(R.id.launchBtn)
    Button launchBtn;
    @BindView(R.id.filterEt)
    EditText filterEt;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private KeywordAdapter keywordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);
        keywordAdapter = new KeywordAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(keywordAdapter);

    }

    @OnClick(R.id.addBtn)
    void addBtn() {
        if (!TextUtils.isEmpty(filterEt.getText()) && !keywordAdapter.getKeywordList().contains(filterEt.getText().toString())) {
            keywordAdapter.addData(filterEt.getText().toString());
            filterEt.setText("");
        }
    }

    @OnClick(R.id.launchBtn)
    void launchBtn() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("keywords", keywordAdapter.getKeywordList());
        startActivity(intent);
    }
}
