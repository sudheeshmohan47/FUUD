package com.foodu.android.Widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by ANDRO01 on 03-Feb-16.
 */
public class CustomSurfaceView extends SurfaceView {

    public CustomSurfaceView(Context context) {
        super(context);
    }

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    // The aspect ratio to be respected by the measurer
    private static final double VIEW_ASPECT_RATIO = 2.5;

    private ViewAspectRatioMeasurer varm = new ViewAspectRatioMeasurer( VIEW_ASPECT_RATIO );

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       // varm.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(1280, heightMeasureSpec );

    }

}
