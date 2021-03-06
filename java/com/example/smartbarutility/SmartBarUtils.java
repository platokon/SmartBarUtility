
package com.example.smartbarutility;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressLint("NewApi")
public class SmartBarUtils {

    public static final int SMART_BAR_HEIGHT_PIXEL = 96;

    /**
     * uc等在使用的方法(新旧版flyme均有效)，
     * 此方法需要配合requestWindowFeature(Window.FEATURE_NO_TITLE)使用,缺点是程序无法使用系统actionbar
     * 
     * @param decorView window.getDecorView
     */
    public static void hide(View decorView) {
        if (!isMX2())
            return;

        try {
            Class[] arrayOfClass = new Class[1];
            arrayOfClass[0] = Integer.TYPE;
            Method localMethod = View.class.getMethod("setSystemUiVisibility", arrayOfClass);
            Field localField = View.class.getField("SYSTEM_UI_FLAG_HIDE_NAVIGATION");
            Object[] arrayOfObject = new Object[1];
            try {
                arrayOfObject[0] = localField.get(null);
            } catch (Exception e) {

            }
            localMethod.invoke(decorView, arrayOfObject);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final boolean isMX2() {
        if (Build.DEVICE.equalsIgnoreCase("mx2")) {
            return true;
        }
        return false;
    }

    /**
     * 新发现的方法(新旧版flyme均有效)
     * 此方法需要配合requestWindowFeature(Window.FEATURE_NO_TITLE)使用，缺点是程序无法使用系统actionbar
     * 
     * @param context
     * @param window
     */
    public static void hide(Context context, Window window) {
        hide(context, window, 0);
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 新发现的方法(新旧版flyme均有效)
     * 需要使用顶部actionbar的应用请使用此方法
     *
     * @param context
     * @param window
     * @param smartBarHeight set SmartBarUtils.SMART_BAR_HEIGHT_PIXEL
     */
    public static void hide(Context context, Window window, int smartBarHeight) {
        if (!isMX2()) {
            return;
        }
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return;
        }

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        int statusBarHeight = getStatusBarHeight(context);

        window.getDecorView().setPadding(0, statusBarHeight, 0, -smartBarHeight);
    }

    /**
     * 新版flyme下已失效
     * 
     * @param activity
     */
    @Deprecated
    public static final void hide(Activity activity) {
        if (!isMX2())
            return;

        ActionBar actionBar = activity.getActionBar();
        if (actionBar == null) {
            return;
        }
        Class<? extends ActionBar> ActionBarClass = actionBar.getClass();
        Method setTabsShowAtBottom;
        try {
            setTabsShowAtBottom = ActionBarClass.getMethod("setTabsShowAtBottom", Boolean.TYPE);
            setTabsShowAtBottom.invoke(activity.getActionBar(), true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
