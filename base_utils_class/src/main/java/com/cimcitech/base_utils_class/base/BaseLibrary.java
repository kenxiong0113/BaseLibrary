package com.cimcitech.base_utils_class.base;

import android.content.Context;

public class BaseLibrary {
    public static Context mContext;
    public static int mIcon;

    public static void initBaseLibrary(Context context) {
        mContext = context;
    }


    /**
     * 设置应用图标
     * 通知栏用到
     */
    public static void setAppIcon(int icon) {
        mIcon = icon;
    }

}
