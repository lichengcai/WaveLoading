package com.waveloading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by wing on 16/1/14.
 */
public class WaveLoadingView extends View {
    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private int y;
    private int x;

    private PorterDuffXfermode mMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private Bitmap mBgBitmap;
    private Path mPath;
    private boolean mIsturn;
    private int mWidth;
    private int mHeight;
    private double mPercent;
    private double mSpeed;
    private int mPaintColor;
    private FullListener mFullListener;



    public WaveLoadingView(Context context) {
        this(context, null);
    }

    public WaveLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStrokeWidth(10);


        mPath = new Path();
        mPaint.setAntiAlias(true);
        mPaintColor = getResources().getColor(R.color.colorDefault);

        mSpeed = 0.3;//默认速度


        mBitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,R.styleable.WaveLoadingView,defStyleAttr,0);
        int n = array.getIndexCount();
        for (int i=0; i<n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.WaveLoadingView_paintColor:
                    mPaintColor = array.getColor(attr,getResources().getColor(R.color.colorDefault));
                    break;
                case R.styleable.WaveLoadingView_speed:
                    mSpeed = array.getFloat(attr, (float) 0.1);
                    break;
                case R.styleable.WaveLoadingView_bgBitmap:
                    int r = array.getResourceId(attr,R.drawable.koolearn);
                    mBgBitmap = BitmapFactory.decodeResource(getResources(),r);
                    break;
            }
        }
        array.recycle();

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }else {
            mWidth = mBgBitmap.getWidth();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }else {
            mHeight = mBgBitmap.getHeight();
        }

        y = mHeight;
        setMeasuredDimension(mWidth, mHeight);
        Log.d("onMeasure","onMeasure mWidth:--" + mWidth + "  mHeight:--" + mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (x > 100) {
            mIsturn = true;
        } else if (x < 0) {
            mIsturn = false;
        }

        if (mIsturn) {
            x = x - 3;
        } else {
            x = x + 3;
        }
        mPath.reset();
        y = (int) ((1-mPercent /100f) *mHeight);
        if (y < 0) {
            mFullListener.full();
            mPercent = 0;
        }
        Log.d("tag", "onDraw y---" + y + " mHeight--" + mHeight);
        mPaint.setColor(mPaintColor);//默认颜色
        mPath.moveTo(0, y);
        mPath.cubicTo(100 + x * 2, 40 + y, 100 + x * 2, y - 40, mWidth, y);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();


        //清除掉图像 不然path会重叠
        mBitmap.eraseColor(Color.parseColor("#00000000"));
        mCanvas.drawBitmap(mBgBitmap, 0, 0, null);
        mPaint.setXfermode(mMode);
        mCanvas.drawPath(mPath, mPaint);
        mPaint.setXfermode(null);


        canvas.drawBitmap(mBitmap, 0, 0, null);

        postInvalidate();
        mPercent = mPercent + mSpeed;
    }


    public void setPercent(int percent){
        mPercent = percent;
    }

    public void setSpeed(double speed) {
        mSpeed = speed;
    }

    /**
     * 设置加载完成监听
     * @param fullListener
     */
    public void setFullListener(FullListener fullListener) {
        mFullListener = fullListener;
    }
    /**
     * 设置颜色
     * @param color
     */
    public void setPaintColor(int color) {
        mPaintColor = color;
    }

    /**
     * 设置背景
     * @param bitmap
     */
    public void setBgBitmap(int bitmap) {
        mBgBitmap = BitmapFactory.decodeResource(getResources(), bitmap);
    }

    interface FullListener {
        void full();
    }
}
