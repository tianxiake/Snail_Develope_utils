package com.snail.android;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * @author Barry 2017/10/25
 *         <p>
 *         1>android dp、sp、px相互转换
 *         2>获取手机分辨率
 *         </p>
 */

public class DensityUtils {

    private DensityUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * dp转px
     */

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }


    /**
     * sp转px
     */

    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }


    /**
     * px转dp
     */

    public static float px2dp(Context context, float pxVal) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }


    /**
     * px转sp
     */

    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取屏幕分辨率（单位是px,有虚拟按键时，获取的高度不包含虚拟按键的高度）
     *
     * @return 返回Point对象。屏幕宽度像素值{@link Point#x}，屏幕高度像素值{@link Point#y}
     */
    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    /**
     * 获取屏幕分辨率（单位是px，不受虚拟按键高度影响）
     *
     * @return 返回DisplayMetrics或者null。通过这个对象的{@link DisplayMetrics#heightPixels}
     * 或者{@link DisplayMetrics#widthPixels}来获取尺寸信息。
     */
    public static DisplayMetrics getScreenRealSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        try {
            DisplayMetrics dm = new DisplayMetrics();
            Class c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            return dm;
        } catch (Exception e) {
            return null;
        }
    }
}
