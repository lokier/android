package com.rdm.base.app.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.StatFs;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class AndroidNewApi {

    public static boolean isSDKLevelAbove(int nLevel) {
        return android.os.Build.VERSION.SDK_INT >= nLevel;
    }

    /**
     * 关闭View硬件加速
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void disableHardwareAcceleration(View view) {
        if (isSDKLevelAbove(Build.VERSION_CODES.HONEYCOMB)) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static long getBlockSizeLong(StatFs stat) {
        if (isSDKLevelAbove(18)) {
            return stat.getBlockSizeLong();
        } else {
            return stat.getBlockSize();
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static long getAvailableBlocks(StatFs stat) {
        if (isSDKLevelAbove(18)) {
            return stat.getAvailableBlocksLong();
        } else {
            return stat.getAvailableBlocks();
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void setBackground(View view, Drawable background) {
        if (isSDKLevelAbove(16)) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void removeOnGlobalLayoutListener(ViewTreeObserver observer, OnGlobalLayoutListener victim) {
        if (isSDKLevelAbove(Build.VERSION_CODES.JELLY_BEAN)) {
            observer.removeOnGlobalLayoutListener(victim);
        } else {
            observer.removeGlobalOnLayoutListener(victim);
        }
    }

    @SuppressLint("NewApi")
    public static Set<String> getQueryParameterNames(Uri uri) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            return uri.getQueryParameterNames();
        } else {
            return internalGetQueryParameterNames(uri);
        }
    }

    /**
     * Returns a set of the unique names of all query parameters. Iterating
     * over the set will return the names in order of their first occurrence.
     *
     * @return a set of decoded names
     * @throws UnsupportedOperationException if this isn't a hierarchical URI
     */
    public static Set<String> internalGetQueryParameterNames(Uri uri) {
        if (uri.isOpaque()) {
            throw new UnsupportedOperationException("This isn't a hierarchical URI.");
        }

        String query = uri.getEncodedQuery();
        if (query == null) {
            return Collections.emptySet();
        }

        Set<String> names = new LinkedHashSet<String>();
        int start = 0;
        do {
            int next = query.indexOf('&', start);
            int end = (next == -1) ? query.length() : next;

            int separator = query.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }

            String name = query.substring(start, separator);
            names.add(Uri.decode(name));

            // Move start to end of name.
            start = end + 1;
        } while (start < query.length());

        return Collections.unmodifiableSet(names);
    }

    public static void setViewAlpha(View view, float alpha) {
        if (isSDKLevelAbove(11)) {
            setAlphaAbove11(view, alpha);
        } else {
            AlphaAnimation a = new AlphaAnimation(alpha, alpha);
            a.setDuration(0); // Make animation instant
            a.setFillAfter(true); // Tell it to persist after the animation ends
            view.startAnimation(a);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void setAlphaAbove11(View view, float alpha) {
        view.setAlpha(alpha);
    }

}
