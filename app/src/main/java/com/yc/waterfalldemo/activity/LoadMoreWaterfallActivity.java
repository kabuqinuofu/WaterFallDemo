package com.yc.waterfalldemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunzn.http.client.library.OKClient;
import com.sunzn.http.client.library.handler.TextHandler;
import com.sunzn.utils.library.KeyBoardUtils;
import com.sunzn.utils.library.StringUtils;
import com.sunzn.utils.library.ViewUtils;
import com.yc.library.AgileRecyclerView;
import com.yc.library.listener.LoadMoreListener;
import com.yc.waterfalldemo.R;
import com.yc.waterfalldemo.adapter.LoadMoreWaterfallAdapter;
import com.yc.waterfalldemo.bean.WaterFallBean;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class LoadMoreWaterfallActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mClearView;
    TextView mCancleView;
    EditText mKeyWordView;
    AgileRecyclerView mRecycleView;
    LoadMoreWaterfallAdapter mAdapter;

    private int mPage;
    private String mKeyWord;
    private final int mRows = 30;

    private ArrayList<WaterFallBean> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);
        init();
    }

    public void init() {
        initView();
        initLise();
        initRecy();
    }

    private void initView() {
        mClearView = findViewById(R.id.search_clear);
        mKeyWordView = findViewById(R.id.search_keyword);
        mCancleView = findViewById(R.id.search_cancel);
        mRecycleView = findViewById(R.id.image_content);

        mClearView.setOnClickListener(this);
        mCancleView.setOnClickListener(this);
    }

    private void initLise() {
        KeyWordTextWatcher mKeyWordWatcher = new KeyWordTextWatcher();
        SearchActionListener mSearchListener = new SearchActionListener();
        mKeyWordView.setOnEditorActionListener(mSearchListener);
        mKeyWordView.addTextChangedListener(mKeyWordWatcher);
    }

    private void initRecy() {
        mAdapter = new LoadMoreWaterfallAdapter(this);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//避免item跳动
        mRecycleView.setLayoutManager(manager);

        mRecycleView.setOnLoadMoreListener(() -> {
            Log.e("as", "加载第" + mPage + "页");
            loadData(mPage, mKeyWord);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_cancel:
                this.finish();
                break;
            case R.id.search_clear:
                mKeyWordView.setText("");
                break;
            default:
                break;
        }
    }

    private void loadData(int page, String keyWord) {
        if (page == 1)
            mDatas.clear();
        String url = "http://image.so.com/j?pn=" + mRows + "&q=" + keyWord + "&sn=" + page;

        OKClient.get().url(url).build().execute(new TextHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, String response) {
                try {
                    Log.e("as", "请求成功" + response);
                    JSONObject jsonObject = JSON.parseObject(response);
                    List<WaterFallBean> list = JSON.parseArray(jsonObject.getJSONArray("list").toJSONString(), WaterFallBean.class);
                    bindView(list);
                    mPage++;
                } catch (Exception e) {
                    e.printStackTrace();
                    loadMoreError();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("as", "请求失败" + e.toString());
                loadMoreError();
            }
        });
    }

    private void loadMoreError() {
        if (mPage > 1) {
            mRecycleView.setLoadMoreCompleted();
            mRecycleView.setLoadMoreState(LoadMoreListener.STATE_ERROR);
        }
    }

    private void bindView(List<WaterFallBean> beans) {
        if (mPage == 1 && mRecycleView != null && beans != null && beans.size() > 0) {
            mDatas.addAll(beans);
            mAdapter.setData(mDatas);
            mRecycleView.setAdapter(mAdapter);
        } else if (mPage > 1) {
            mRecycleView.setLoadMoreCompleted();
            int dataSize = mAdapter.getData().size();
            mAdapter.getData().addAll(beans);
            mAdapter.notifyItemRangeInserted(dataSize, beans.size());
            mRecycleView.setLoadMoreState(LoadMoreListener.STATE_NORMAL);
        }
    }

    public void onSearchAction() {
        mPage = 1;
        KeyBoardUtils.hide(this);
        mKeyWord = mKeyWordView.getText().toString();
        loadData(mPage, mKeyWord);
    }

    private class SearchActionListener implements TextView.OnEditorActionListener {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (mKeyWordView.getText().toString().trim().length() > 0) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearchAction();
                    return true;
                }
            } else {
                return true;
            }
            return false;
        }
    }

    private class KeyWordTextWatcher implements TextWatcher {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (StringUtils.isEmpty(s.toString())) {
                ViewUtils.setVisibility(mClearView, View.GONE);
            } else {
                ViewUtils.setVisibility(mClearView, View.VISIBLE);
            }
        }
    }
}