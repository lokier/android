package com.rdm.common.ui;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.rdm.base.app.BaseApp;

/**
 * Created by lokierao on 2015/1/23.
 */
public class ImageLoaderUtils {

    static{
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(BaseApp.get()));

    }

    private static DisplayImageOptions mDefaultOption;

    public static DisplayImageOptions getDisplayImageOptions(){

        if(mDefaultOption!= null){
            return mDefaultOption;
        }

        mDefaultOption = new DisplayImageOptions.Builder().showStubImage(R.drawable.default_image_load_empty)
                .showImageForEmptyUri(R.drawable.default_image_load_empty).showImageOnFail(R.drawable.default_image_load_fail).cacheInMemory()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        return mDefaultOption;
    }
}
