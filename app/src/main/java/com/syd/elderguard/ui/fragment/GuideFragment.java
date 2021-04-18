package com.syd.elderguard.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.syd.elderguard.ui.base.BaseFragment;


/**
 * 悬浮窗适配
 */
public class GuideFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private static final String ARG_LAYOUT_INDEX = "index";
    private int layoutResId;
    private int index;

    private Button btnOpenOverlays;
    private Button btnUsageStats;
    private LottieAnimationView lottieLock;


    public static GuideFragment newInstance(int layoutResId, int index) {
        GuideFragment sampleSlide = new GuideFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        args.putInt(ARG_LAYOUT_INDEX, index);
        sampleSlide.setArguments(args);

        return sampleSlide;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
            index = getArguments().getInt(ARG_LAYOUT_INDEX);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutResId, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {


        }
    }




}
