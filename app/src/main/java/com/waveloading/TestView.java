package com.waveloading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by lichengcai on 2016/11/9.
 */

public class TestView extends View {

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final String s = "Hello. I'm some text!";

        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(60);

        p.getTextBounds(s, 0, s.length(), bounds);
        float mt = p.measureText(s);
        int bw = bounds.width();

        Log.i("LCG", String.format(
                "measureText %f, getTextBounds %d (%s)",
                mt,
                bw, bounds.toShortString())
        );
        Log.d("LCG",String.format("getTextBounds %d, bound.left %d",bounds.width(),
                bounds.left));
        bounds.offset(0, -bounds.top);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawColor(0xff000080);
        p.setColor(0xffff0000);
        canvas.drawRect(bounds, p);
        p.setColor(0xff00ff00);
        canvas.drawText(s, 0, bounds.bottom, p);
    }
}
