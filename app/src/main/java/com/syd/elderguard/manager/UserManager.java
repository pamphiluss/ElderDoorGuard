package com.syd.elderguard.manager;

import com.syd.elderguard.model.User;

public class UserManager {

    /**
     * 是否是付费会员
     * @return
     */
    public static boolean isPaidMember() {
        //if (BuildConfig.DEBUG) return true;

        User user = User.getCurrentUser(User.class);
        if (User.isLogin()&& user.getType()!=null&&user.getType()>0) {
            return true;
        }

        return false;

    }

    public static boolean isLogin() {
        return User.isLogin();
    }

    public static User getUser() {
        return User.getCurrentUser(User.class);
    }
}

