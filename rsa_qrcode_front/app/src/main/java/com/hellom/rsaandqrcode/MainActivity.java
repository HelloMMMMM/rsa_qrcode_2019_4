package com.hellom.rsaandqrcode;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.hellom.rsaandqrcode.decode.DecodeResultActivity;
import com.hellom.rsaandqrcode.encode.EncodeInputActivity;
import com.hellom.rsaandqrcode.util.KeyUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String RSA_PUBLIC_KEY = "RSAPublicKey";
    private static final String RSA_PRIVATE_KEY = "RSAPrivateKey";

    //更换服务器ip地址
    private final String ROOT_URL = "http://47.102.196.236/api/";

    private final String GET_KEYS = "getKeys";

    private Handler handler;
    private LoadingDialogFragment loadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        findViewById(R.id.btn_encode).setOnClickListener(this);
        findViewById(R.id.btn_decode).setOnClickListener(this);

        checkPermission();
        //注释掉就是用本地生成的密钥，打开请求服务器统一密钥
        requestKeys();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_encode:
                intent = new Intent(this, EncodeInputActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_decode:
                intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Intent intent = new Intent(MainActivity.this, DecodeResultActivity.class);
                    intent.putExtra("privateInput", result);
                    startActivity(intent);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtils.showShort("解析二维码失败");
                }
            }
        }
    }

    private void checkPermission() {
        showLoading();
        if (!PermissionUtils.isGranted(PermissionConstants.CAMERA)) {
            PermissionUtils.permission(PermissionConstants.CAMERA).callback(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    hideLoading();
                }

                @Override
                public void onDenied() {
                    hideLoading();
                    ToastUtils.showShort("请授予必需的权限");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.exit(0);
                        }
                    }, 1000);
                }
            }).request();
        } else {
            hideLoading();
        }
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

    private void requestKeys() {
        OkGo.<KeyBean>post(ROOT_URL + GET_KEYS).execute(new AbsCallback<KeyBean>() {
            @Override
            public void onStart(Request<KeyBean, ? extends Request> request) {
                showLoading();
            }

            @Override
            public void onSuccess(Response<KeyBean> response) {
                hideLoading();
                KeyBean keyBean = response.body();
                Map<String, String> keyMap = new HashMap<>();
                keyMap.put(RSA_PUBLIC_KEY, keyBean.getPublicKey());
                keyMap.put(RSA_PRIVATE_KEY, keyBean.getPrivateKey());
                KeyUtil.keys = keyMap;
            }

            @Override
            public KeyBean convertResponse(okhttp3.Response response) throws Throwable {
                Gson gson = new Gson();
                return gson.fromJson(response.body().string(), KeyBean.class);
            }

            @Override
            public void onError(Response<KeyBean> response) {
                super.onError(response);
                hideLoading();
                ToastUtils.showShort("获取密钥失败:" + response.message());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                }, 1000);
            }
        });
    }
}
