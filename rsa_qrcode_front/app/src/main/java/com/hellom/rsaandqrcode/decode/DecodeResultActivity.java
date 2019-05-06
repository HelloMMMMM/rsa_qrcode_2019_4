package com.hellom.rsaandqrcode.decode;

import android.widget.TextView;

import com.hellom.rsaandqrcode.LoadingDialogFragment;
import com.hellom.rsaandqrcode.R;
import com.hellom.rsaandqrcode.ToolBarActivity;
import com.hellom.rsaandqrcode.util.KeyUtil;
import com.hellom.rsaandqrcode.util.RSAUtil;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class DecodeResultActivity extends ToolBarActivity {
    private TextView privateInput;
    private TextView privateKey;
    private TextView input;
    private LoadingDialogFragment loadingDialogFragment;

    private String privateInputString;

    @Override
    public void initView() {
        setTitle("解码");
        privateInput = findViewById(R.id.tv_private_input);
        privateKey = findViewById(R.id.tv_private_key);
        input = findViewById(R.id.tv_input);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        privateInputString = getIntent().getStringExtra("privateInput");

        showLoading();

        String privateKeyString = RSAUtil.getPrivateKey(KeyUtil.keys);
        String inputString = null;
        try {
            inputString = RSAUtil.decryptPrivateKey(privateInputString, privateKeyString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        privateInput.setText(privateInputString);
        privateKey.setText(privateKeyString);
        input.setText(inputString);

        hideLoading();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_decode_result;
    }

    private void showLoading() {
        if (loadingDialogFragment == null) {
            loadingDialogFragment = new LoadingDialogFragment();
        }
        loadingDialogFragment.show(getSupportFragmentManager(), "loading");
    }

    private void hideLoading() {
        if (loadingDialogFragment != null) {
            loadingDialogFragment.dismiss();
        }
    }
}
