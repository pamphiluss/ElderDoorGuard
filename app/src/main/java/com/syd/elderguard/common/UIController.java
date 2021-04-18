package com.syd.elderguard.common;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import com.just.agentweb.AgentWebUIControllerImplBase;

public class UIController extends AgentWebUIControllerImplBase {

    public UIController(Activity activity){
    }

    @Override
    public void onShowMessage(String message, String from) {
        super.onShowMessage(message,from);
        Log.i(TAG,"message:"+message);
    }

    @Override
    public void onSelectItemsPrompt(WebView view, String url, String[] items, Handler.Callback callback) {
       super.onSelectItemsPrompt(view,url,items,callback); // 使用默认的UI
    }


}
