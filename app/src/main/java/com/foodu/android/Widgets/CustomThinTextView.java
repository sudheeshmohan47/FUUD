package com.foodu.android.Widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by QA-2 on 04-Sep-15.
 */

public class CustomThinTextView extends TextView {

    public CustomThinTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomThinTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomThinTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_thin.ttf");
        setTypeface(tf ,Typeface.NORMAL);

    }

}