package com.android.ahivaran.blockkeyword.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ahivaran.blockkeyword.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.KeywordHolder> {

    private Context context;
    private ArrayList<String> keywordList;

    public KeywordAdapter(Context context) {
        this.context = context;
        this.keywordList = new ArrayList<>();
    }

    @Override
    public KeywordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KeywordHolder(LayoutInflater.from(context).inflate(R.layout.keyword_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(KeywordHolder holder, int position) {
        String keyword = keywordList.get(position);
        holder.keywordTv.setText(keyword);
    }

    @Override
    public int getItemCount() {
        return keywordList != null ? keywordList.size() : 0;
    }

    public void addData(String key) {
        keywordList.add(key);
        notifyDataSetChanged();

    }

    public ArrayList<String> getKeywordList() {
        return keywordList;
    }

    static class KeywordHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.keywordTv)
        TextView keywordTv;
        KeywordHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
