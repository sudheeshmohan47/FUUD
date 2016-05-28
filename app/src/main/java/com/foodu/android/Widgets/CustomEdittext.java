package com.foodu.android.Widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ANDRO01 on 25-Jan-16.
 */
public class CustomEdittext extends EditText {
    public CustomEdittext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEdittext(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Freight Sans Light.otf");
        setTypeface(tf ,1);

    }

}
