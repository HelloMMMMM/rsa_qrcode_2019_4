package com.hellom.rsaandqrcode.encode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hellom.rsaandqrcode.LoadingDialogFragment;
import com.hellom.rsaandqrcode.R;
import com.hellom.rsaandqrcode.ToolBarActivity;
import com.hellom.rsaandqrcode.decode.DecodeResultActivity;
import com.hellom.rsaandqrcode.util.CodeUtil;
import com.hellom.rsaandqrcode.util.KeyUtil;
import com.hellom.rsaandqrcode.util.RSAUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class EncodeResultActivity extends ToolBarActivity {
    private TextView input;
    private TextView publicKey;
    private TextView privateInput;
    private ImageView qrCode;
    private LoadingDialogFragment loadingDialogFragment;

    private String inputString;

    @Override
    public void initView() {
        setTitle("编码");
        input = findViewById(R.id.tv_input);
        publicKey = findViewById(R.id.tv_public_key);
        privateInput = findViewById(R.id.tv_private_input);
        qrCode = findViewById(R.id.iv_qr_code);
    }

    @Override
    public void initListener() {
        qrCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                parseQrCode();
                return false;
            }
        });
    }

    @Override
    public void initData() {
        inputString = getIntent().getStringExtra("input");

        showLoading();

        String publicKeyString = RSAUtil.getPublicKey(KeyUtil.keys);
        String privateInputString = null;
        try {
            privateInputString = RSAUtil.encryptPublicKey(inputString, publicKeyString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        input.setText(inputString);
        publicKey.setText(publicKeyString);
        privateInput.setText(privateInputString);
        createQrCode(privateInputString);

        hideLoading();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_encode_result;
    }

    private void parseQrCode() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) qrCode.getDrawable();
        Bitmap mBitmap = bitmapDrawable.getBitmap();
        CodeUtil.analyzeBitmap(mBitmap, new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                Log.e("mx", "result:" + result);
                Toast.makeText(EncodeResultActivity.this, "解析成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EncodeResultActivity.this, DecodeResultActivity.class);
                intent.putExtra("privateInput", result);
                startActivity(intent);
            }

            @Override
            public void onAnalyzeFailed() {
                Log.e("mx", "failed");
            }
        });
    }

    private void createQrCode(String textContent) {
        float density = getResources().getDisplayMetrics().density;
        Bitmap mBitmap = CodeUtils.createImage(textContent, (int) (200 * density), (int) (200 * density), null);
        qrCode.setImageBitmap(mBitmap);
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
