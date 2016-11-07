package com.waveloading.splashview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.waveloading.R;


/**
 * Created by lichengcai on 2016/11/7.
 */

public class SplashView extends View {
    private int mHeight;
    private int mWidth;
    private String mSplashText;
    private float mSplashSize;
    private Paint mPaintSplashText;
    private Paint mPaintCircle;
    private final int MAX_SIZE_SPLASH = dip2px(80);
    private final int MAX_SIZE_TEXT = sp2px(60);
    private Rect mBound = new Rect();

    public SplashView(Context context) {
        this(context,null);
    }

    public SplashView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public SplashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SplashView,defStyleAttr,0);
        int n = array.getIndexCount();
        for (int i=0; i<n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.SplashView_splash_text:
                    mSplashText = array.getString(attr);
                    break;
                case R.styleable.SplashView_splash_size:
                    mSplashSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        array.recycle();
        mPaintSplashText = new Paint();
        mPaintCircle = new Paint();
        mPaintCircle.setColor(getResources().getColor(R.color.black));
        mPaintSplashText.setColor(getResources().getColor(R.color.black));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaintSplashText.setTextSize(MAX_SIZE_TEXT);
        mPaintSplashText.getTextBounds(mSplashText, 0, mSplashText.length(), mBound);
        Log.d("onDraw"," length---" + mBound.width());

//        canvas.drawCircle(50,50,20,mPaintCircle);
        canvas.drawText(mSplashText, (getWidth() / 2 - mBound.width() / 2), (getHeight() / 2 + mBound.height() / 2), mPaintSplashText);
        Log.d("onDraw"," width--" + getWidth() + " mBound width--" + mBound.width()
                + " height--" + getHeight() + "  mBound height--" + mBound.height()
        );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }

        setMeasuredDimension(mWidth,mHeight);
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public int px2dip( float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    private int px2sp(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue/fontScale + 0.5f);
    }

}
