package com.sr7d.myposts;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by ShubhamRaja on 05-Nov-17.
 */

public class PicassoClient {

    public static void downloadImage(Context c, String url, ImageView img)
    {
        if (url != null && url.length() > 0) {
            Picasso.with(c).load(url).placeholder(R.drawable.ic_dashboard_black_24dp).into(img);
        } else {
            Picasso.with(c).load(R.drawable.blur1).into(img);
        }
    }
}
