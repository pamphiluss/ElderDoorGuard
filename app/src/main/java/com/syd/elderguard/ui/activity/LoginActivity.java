package com.syd.elderguard.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.syd.elderguard.R;
import com.syd.elderguard.model.User;
import com.syd.elderguard.ui.base.BaseActivity;
import com.syd.elderguard.utils.SwitchUtilsKt;
import com.syd.elderguard.utils.TimeCount;

import org.jetbrains.annotations.Nullable;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;


/**
 * 验证码登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView txvGetCode;
    private EditText edtPhone;
    private EditText edtSmscode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txvGetCode = findViewById(R.id.txvGetCode);
        edtPhone = findViewById(R.id.edtPhone);
        edtSmscode = findViewById(R.id.edtSmscode);

        findViewById(R.id.txvReadPrivacy).setOnClickListener(this);
        findViewById(R.id.txvReadAgreement).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.imvCloseLogin).setOnClickListener(this);
        txvGetCode.setOnClickListener(this);
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this)
                .navigationBarColor(android.R.color.white)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvGetCode:
                sendSmscode();
                break;
            case R.id.imvCloseLogin:
                finish();
                break;
            case R.id.btnLogin:
                requestLogin();
                break;
            case R.id.txvReadAgreement:
                SwitchUtilsKt.toAgreement(this);
                break;
            case R.id.txvReadPrivacy:
                SwitchUtilsKt.toPrivacy(this);
                break;
        }

    }



    private void sendSmscode() {
        String phone = edtPhone.getText().toString();
        if(TextUtils.isEmpty(phone)) {
            showLongToast("请输入手机号");
            return;
        }

        if(!phone.startsWith("1") || phone.length()<11) {
            showLongToast("请正确手机号");
            return;
        }

        BmobSMS.requestSMSCode(phone, "", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    new TimeCount(txvGetCode, 60000, 1000).start();
                    showLongToast("发送验证码成功");
                } else {
                    showLongToast("发送验证码失败");
                }
            }
        });

    }

    private void requestLogin() {
        String phone = edtPhone.getText().toString();
        String code = edtSmscode.getText().toString();
        if(TextUtils.isEmpty(phone)) {
            showLongToast("请输入手机号");
            return;
        }

        if(!phone.startsWith("1") || phone.length()<11) {
            showLongToast("请正确手机号");
            return;
        }

        if(TextUtils.isEmpty(code)) {
            showLongToast("请输入手机号");
            return;
        }

        if(code.length()<4) {
            showLongToast("请正确验证码");
            return;
        }

        User.signOrLoginByMobilePhone(phone, code, new LogInListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                if (e == null) {
                    finish();
                } else {
                    showLongToast("短信注册或登录失败");
                }
            }
        });

    }
}
