package com.hellom.rsaandqrcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public abstract class ToolBarActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_bar);

        title = findViewById(R.id.tv_title);
        findViewById(R.id.iv_back).setOnClickListener(this);

        LinearLayout container = findViewById(R.id.container);
        View layout = LayoutInflater.from(this).inflate(getLayoutId(), null, false);
        container.addView(layout);

        initView();
        initData();
        initListener();
    }

    public void setTitle(String titleString) {
        title.setText(titleString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    public abstract void initView();

    public abstract void initListener();

    public abstract void initData();

    public abstract int getLayoutId();
}
