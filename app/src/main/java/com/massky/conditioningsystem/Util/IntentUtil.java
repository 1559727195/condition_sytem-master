package com.massky.conditioningsystem.Util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by on 2016/12/28.
 */

public class IntentUtil {
    private static String BUNDLE_KEY = "bundle";

    public static void startActivity(Context context, Class cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public static void startActivityAndFinishFirst(Context context, Class cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public static void startActivity(Context context, Class cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(BUNDLE_KEY, bundle);
        context.startActivity(intent);
    }

    public static void startActivityAndFinish(Context context, Class cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(BUNDLE_KEY, bundle);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public static void startActivity(Context context, Class cls, String key, Object value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        Bundle bundle = new Bundle();
        if (value.getClass() == String.class) {
            bundle.putString(key, String.valueOf(value));
        } else if (value.getClass() == Integer.class) {
            bundle.putInt(key, (Integer) value);
        } else if (value.getClass() == Boolean.class) {
            bundle.putBoolean(key, (Boolean) value);
        } else {
            bundle.putSerializable(key, (Serializable) value);
        }
        intent.putExtra(BUNDLE_KEY, bundle);
        context.startActivity(intent);
    }

    public static Bundle getIntentBundle(AppCompatActivity activity) {
        return activity.getIntent().getBundleExtra(BUNDLE_KEY);
    }

    public static boolean getIntentBoolean(AppCompatActivity activity, String key) {
        if (activity.getIntent().getBundleExtra(BUNDLE_KEY) != null)
            return activity.getIntent().getBundleExtra(BUNDLE_KEY).getBoolean(key, false);
        return false;
    }

    public static Serializable getIntentSerializable(AppCompatActivity activity, String key) {
        if (activity.getIntent().getBundleExtra(BUNDLE_KEY) == null)
            return null;
        return activity.getIntent().getBundleExtra(BUNDLE_KEY).getSerializable(key);
    }

    public static String getIntentString(AppCompatActivity activity, String key) {
        if (activity.getIntent().getBundleExtra(BUNDLE_KEY) == null)
            return "";
        return activity.getIntent().getBundleExtra(BUNDLE_KEY).getString(key);
    }

    public static int getIntentInt(AppCompatActivity activity, String key) {
        return activity.getIntent().getBundleExtra(BUNDLE_KEY).getInt(key);
    }

    public static long getIntentLong(AppCompatActivity activity, String key) {
        return activity.getIntent().getBundleExtra(BUNDLE_KEY).getLong(key);
    }

    public static double getIntentDouble(AppCompatActivity activity, String key) {
        return activity.getIntent().getBundleExtra(BUNDLE_KEY).getDouble(key);
    }

    public static void startActivityAndFinishLine(Context context, Class cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startActivityAndFinishLine(Context context, Class cls, String key, String value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        intent.putExtra(BUNDLE_KEY, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }
}

