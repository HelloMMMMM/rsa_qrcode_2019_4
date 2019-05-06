package com.hellom.rsaandqrcode.encode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hellom.rsaandqrcode.R;
import com.hellom.rsaandqrcode.ToolBarActivity;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class EncodeInputActivity extends ToolBarActivity {
    private EditText encodeInput;

    @Override
    public void initView() {
        setTitle("编码");
        encodeInput = findViewById(R.id.et_content_input);
    }

    @Override
    public void initListener() {
        findViewById(R.id.btn_encode).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_encode_input;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_encode:
                String inputString = encodeInput.getText().toString();
                if (!TextUtils.isEmpty(inputString)) {
                    Intent intent = new Intent(this, EncodeResultActivity.class);
                    intent.putExtra("input", inputString);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
