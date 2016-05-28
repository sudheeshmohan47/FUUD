/*
import com.squareup.picasso.Picasso;

/
package com.foodu.android.Utilities;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

*/
/**
 * Created by ANDRO01 on 04-Jan-16.
 *//*

public class PicassoCache {

    */
/**
     * Static Picasso Instance
     *//*

    private static Picasso picassoInstance = null;

    */
/**
     * PicassoCache Constructor
     *
     * @param context application Context
     *//*

    private PicassoCache(Context context) {
        OkHttpClient okHttpClient;
        // Cache cache = new LruCache( 1024*1024*1 );
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
        //okHttpClient.setCache(cache);
        //  Downloader downloader   = new OkHttpDownloader(context, Integer.MAX_VALUE);
        Downloader downloader = new OkHttpDownloader(okHttpClient);
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(downloader);

        picassoInstance = builder.build();
    }

    */
/**
     * Get Singleton Picasso Instance
     *
     * @param context application Context
     * @return Picasso instance
     *//*

    public static Picasso getPicassoInstance(Context context) {

        if (picassoInstance == null) {

            new PicassoCache(context);
            return picassoInstance;
        }

        return picassoInstance;
    }

}*/
