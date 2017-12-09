package com.snail.android;


import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by Jack on 7/27/16.
 */

public class RefUtil {
    private static final String LYJ_TAG = "LYJ_RefUtil";
    private static final boolean DEBUG = false;

    /**
     * 反射构建对象
     *
     * @param target       目标Class对象
     * @param paramClasses 目标Class构造器的参数列表
     * @param paramValues  Class创建对象时对象构造器参数
     * @return 返回构造的对象，失败返回null
     */
    public static Object newConstructor(Class target, Class[] paramClasses, Object[] paramValues) {
        if (DEBUG) {
            Log.d(LYJ_TAG, target.getName());
        }
        try {
            return target.getDeclaredConstructor(paramClasses).newInstance(paramValues);
        } catch (Exception e) {
            Log.e(LYJ_TAG, e + "");
        }
        return null;
    }

    /**
     * 反射构建对象
     *
     * @param target       class文件完整的路径名 比如： com.example.CustomClass
     * @param paramClasses 目标Class构造器的参数列表
     * @param paramValues  Class创建对象时对象构造器参数
     * @return 返回构造的对象，失败返回null
     */
    public static Object newConstructor(String target, Class[] paramClasses, Object[] paramValues) {
        if (DEBUG) {
            Log.d(LYJ_TAG, target);
        }
        try {
            Class<?> targetClass = Class.forName(target);
            if (targetClass != null) {
                Log.d(LYJ_TAG, "targetClass is available");
                return targetClass.getDeclaredConstructor(paramClasses).newInstance(paramValues);
            }
        } catch (Exception e) {
            Log.e(LYJ_TAG, e + "");
        }
        return null;
    }

    /**
     * 反射调用某个对象的方法
     *
     * @param target       目标对象
     * @param clazz        目标对象的Class对象
     * @param methodName   要调用的方法名
     * @param paramClasses 要调用的方法中参数对应的Class
     * @param paramValues  要调用的方法的参数
     * @return 返回方法的返回值或者是null
     */
    public static Object invokeMethod(Object target, Class clazz,
                                      String methodName, Class[] paramClasses, Object[] paramValues) {
        if (DEBUG) {
            Log.d(LYJ_TAG, methodName);
        }
        Method method;
        try {
            method = clazz.getDeclaredMethod(methodName, paramClasses);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
        } catch (NoSuchMethodException e) {
            method = getAccessibleMethodFromInterfaceNest(clazz, methodName, paramClasses);
            if (method == null) {
                method = getAccessibleMethodFromSuperclass(clazz, methodName, paramClasses);
            }
        }

        if (method != null) {
            try {
                if (DEBUG) {
                    Log.d(LYJ_TAG, methodName);
                }
                return method.invoke(target, paramValues);
            } catch (Exception e) {
                Log.e(LYJ_TAG, e + "");
            }
        }
        return null;
    }

    public static Object invokeMethodUnsafe(Object target, Class clazz,
                                            String methodName,
                                            Class[] paramClasses, Object[] paramValues)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (DEBUG) {
            Log.d(LYJ_TAG, methodName);
        }
        Method method = clazz.getDeclaredMethod(methodName, paramClasses);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method.invoke(target, paramValues);
    }

    public static Object invokeMethod(final Object target, final String className,
                                      final String methodName,
                                      final Class[] paramClasses, final Object[] paramValues) {
        try {
            Class clazz = Class.forName(className);
            return invokeMethod(target, clazz, methodName, paramClasses, paramValues);
        } catch (ClassNotFoundException e) {
            Log.e(LYJ_TAG, e + "");
        }
        return null;
    }

    public static Object invokeMethod(final Object target, final String className,
                                      final String methodName, final ClassLoader classLoader, final Class[] paramClasses, final Object[] paramValues) {
        try {
            Class clazz = Class.forName(className, false, classLoader);
            return invokeMethod(target, clazz, methodName, paramClasses, paramValues);
        } catch (ClassNotFoundException e) {
            Log.e(LYJ_TAG, e + "");
        }
        return null;
    }

    public static Object invokeMethodUnsafe(final Object target, final String className,
                                            final String methodName, final ClassLoader classLoader, final Class[] paramClasses,
                                            final Object[] paramValues)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Class clazz = Class.forName(className, false, classLoader);
        return invokeMethodUnsafe(target, clazz, methodName, paramClasses, paramValues);
    }

    public static Object invokeStaticMethod(final String className, final String methodName,
                                            final Class[] paramClasses, final Object[] paramValues) {
        return invokeMethod(null, className, methodName, paramClasses, paramValues);
    }

    public static Object invokeStaticMethod(final ClassLoader classLoader, final String className,
                                            final String methodName, final Class[] paramClasses, final Object[] paramValues) {
        return invokeMethod(null, className, methodName, classLoader, paramClasses, paramValues);
    }

    public static Object invokeStaticMethod(final Class clazz, final String methodName,
                                            final Class[] paramClasses, final Object[] paramValues) {

        return invokeMethod(null, clazz, methodName, paramClasses, paramValues);
    }

    public static Object invokeStaticMethod(final Class clazz, final String methodName) {
        return invokeStaticMethod(clazz, methodName, null, null);
    }

    public static Object invokeStaticMethodUnsafe(final ClassLoader classLoader,
                                                  final String className, final String methodName, final Class[] paramClasses,
                                                  final Object[] paramValues)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        return invokeMethodUnsafe(null, className, methodName, classLoader, paramClasses,
                paramValues);
    }

    public static Object getObject(final Object target, final Class clazz, final String fieldName) {
        if (DEBUG) {
            Log.d(LYJ_TAG, fieldName);
        }

        // check up the superclass hierarchy
        for (Class<?> clazzSuperclass = clazz; clazzSuperclass != null; clazzSuperclass = clazzSuperclass.getSuperclass()) {
            try {
                final Field field = clazzSuperclass.getDeclaredField(fieldName);
                // getDeclaredField checks for non-public scopes as well
                // and it returns accurate results
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field.get(target);
            } catch (final NoSuchFieldException e) {
            } catch (IllegalAccessException e) {
                Log.e(LYJ_TAG, e + "");
            }
        }
        return null;
    }

    public static Object getObject(final Object target, final String className,
                                   final String fieldName) {
        try {
            Class clazz = Class.forName(className);
            return getObject(target, clazz, fieldName);
        } catch (ClassNotFoundException e) {
            Log.e(LYJ_TAG, e + "");
        }
        return null;
    }

    public static Object getObject(final Object target, final String className,
                                   final String fieldName, final ClassLoader classLoader) {
        try {
            Class clazz = Class.forName(className, true, classLoader);
            return getObject(target, clazz, fieldName);
        } catch (ClassNotFoundException e) {
            Log.e(LYJ_TAG, e + "");
        }
        return null;
    }

    public static Object getStaticObject(final String className, final String fieldName,
                                         final ClassLoader classLoader) {
        return getObject(null, className, fieldName, classLoader);
    }

    public static Object getStaticObject(final String className, final String fieldName) {
        return getObject(null, className, fieldName);
    }

    public static void setObject(final Object target, final Class clazz,
                                 final String fieldName, final Object fieldObject) {
        if (DEBUG) {
            Log.d(LYJ_TAG, fieldName);
        }
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(target, fieldObject);
        } catch (NoSuchFieldException e) {
            Log.e(LYJ_TAG, e + "");
        } catch (IllegalAccessException e) {
            Log.e(LYJ_TAG, e + "");
        }
    }

    public static void setObject(final Object target, final String className,
                                 final String fieldName, final Object fieldObject) {
        try {
            Class clazz = Class.forName(className);
            setObject(target, clazz, fieldName, fieldObject);
        } catch (ClassNotFoundException e) {
            Log.e(LYJ_TAG, e + "");
        }
    }

    public static void setStaticObject(final String className, final String fieldName,
                                       final Object fieldObject) {
        setObject(null, className, fieldName, fieldObject);
    }

    private static Method getAccessibleMethodFromSuperclass(final Class<?> cls,
                                                            final String methodName, final Class<?>... parameterTypes) {

        Class<?> parentClass = cls.getSuperclass();
        while (parentClass != null) {
            if (Modifier.isPublic(parentClass.getModifiers())) {
                try {
                    return parentClass.getMethod(methodName, parameterTypes);
                } catch (final NoSuchMethodException e) {
                    return null;
                }
            }
            parentClass = parentClass.getSuperclass();
        }
        return null;
    }

    private static Method getAccessibleMethodFromInterfaceNest(Class<?> cls, final String methodName, final Class<?>... parameterTypes) {

        for (; cls != null; cls = cls.getSuperclass()) {

            final Class<?>[] interfaces = cls.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (!Modifier.isPublic(interfaces[i].getModifiers())) {
                    continue;
                }
                try {
                    return interfaces[i].getDeclaredMethod(methodName, parameterTypes);
                } catch (final NoSuchMethodException e) {
                }
                // Recursively check interfaces
                Method method =
                        getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
                if (method != null) {
                    return method;
                }
            }
        }
        return null;
    }
}
