package com.snail.java;

import java.lang.reflect.Method;

/**
 * 通过反射的方式设置/取得系统属性
 */
public class SystemPropertiesUtils {


    public static boolean getBoolean(String key, boolean def) {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getDeclaredMethod("getBoolean", String.class, boolean.class);
            method.setAccessible(true);
            Boolean result = (Boolean) method.invoke(null, key, def);
            return result;
        } catch (Exception e) {
            return def;
        }
    }

    public static String get(String key, String def){
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getDeclaredMethod("get", String.class, String.class);
            method.setAccessible(true);
            String result = (String) method.invoke(null, key, def);
            return result;
        } catch (Exception e) {
            return def;
        }
    }
}
