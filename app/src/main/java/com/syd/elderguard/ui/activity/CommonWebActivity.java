package com.syd.elderguard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.syd.elderguard.R;
import com.syd.elderguard.common.FragmentKeyDown;
import com.syd.elderguard.ui.fragment.AgentWebFragment;


public class CommonWebActivity extends AppCompatActivity {


    private FrameLayout mFrameLayout;
    public static final String TYPE_URL = "url";
    public static final String TYPE_TITLE = "title";
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_web);

        mFrameLayout = this.findViewById(R.id.container_framelayout);
        String url = getIntent().getStringExtra(TYPE_URL);
        String title = getIntent().getStringExtra(TYPE_TITLE);
        mFragmentManager = this.getSupportFragmentManager();
        openFragment(url, title);
    }


    private AgentWebFragment mAgentWebFragment;

    private void openFragment(String url, String title) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;


        ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
        mBundle.putString(AgentWebFragment.URL_KEY, url);
        mBundle.putString(AgentWebFragment.TITLE_KEY, title);
        ft.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        AgentWebFragment mAgentWebFragment = this.mAgentWebFragment;
        if (mAgentWebFragment != null) {
            if (((FragmentKeyDown) mAgentWebFragment).onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }

        return super.onKeyDown(keyCode, event);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
