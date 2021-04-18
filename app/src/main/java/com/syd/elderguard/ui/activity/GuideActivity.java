package com.syd.elderguard.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.syd.elderguard.Constant;
import com.syd.elderguard.R;
import com.syd.elderguard.ui.fragment.GuideFragment;
import com.syd.elderguard.utils.SwitchUtilsKt;
import com.tencent.mmkv.MMKV;


public class GuideActivity extends AppIntro {

    public boolean hasOpenOverlays = false;
    public boolean hasOpenUsageStats = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        super.onCreate(savedInstanceState);

        addSlide(GuideFragment.newInstance(R.layout.fragment_guide_1, 1));
        addSlide(GuideFragment.newInstance(R.layout.fragment_guide_2, 2));
        addSlide(GuideFragment.newInstance(R.layout.fragment_guide_3, 3));
        addSlide(GuideFragment.newInstance(R.layout.fragment_guide_4, 4));

        //setSeparatorColor(getResources().getColor(R.color.lightgray));
        showSkipButton(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomUIMenu();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            hideBottomUIMenu();
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        MMKV.defaultMMKV().encode(Constant.SP_IS_FIRST, false);
        SwitchUtilsKt.toMain(this);
        finish();
    }

    private void goToGetPermission() {
        if (!hasOpenOverlays) {
            getPager().setCurrentItem(1);
            return;
        }

        if (!hasOpenUsageStats) {
            getPager().setCurrentItem(2);
            return;
        }
    }

    private void goToMainActivity() {
      //  MMKV.defaultMMKV().encode(ProjectConstant.SP_IS_FIRST, false);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateOverlaysPermission(boolean hasPermission) {
        this.hasOpenOverlays = hasPermission;
    }

    public void updateUsageStats(boolean hasPermission) {
        this.hasOpenUsageStats = hasPermission;
    }

    /**
     * 隐藏底部导航栏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY ;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }

}

