package com.waveloading.splashview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.waveloading.R;
import com.waveloading.particleview.LineEvaluator;
import com.waveloading.particleview.Particle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


/**
 * Created by lichengcai on 2016/11/7.
 */

public class SplashView extends View {
    /**
     * 粒子覆盖文字内容
     */
    private String mSplashText;
    /**
     * 粒子覆盖文字最终大小
     */
    private int mSplashSizeEnd;
    /**
     * 默认文字初始大小值
     */
    private int mSplashSizeStart = sp2px(60);
    private int mSplashSizeDef = dip2px(180);
    private float mSplashSizeX;
    private float mSplashSizeY;
    final Particle particle = new Particle(50,50,20);
    /**
     * 粒子半径
     */
    private int mRadius = dip2px(5);
    /**
     * 粒子覆盖文字画笔
     */
    private Paint mPaintSplashText;
    /**
     * 粒子画笔
     */
    private Paint mPaintCircle;
    private Rect mBound = new Rect();
    private Rect mBoundEnd = new Rect();
    private HashMap<Integer, ArrayList<Circle>> mCircleMapStart = new HashMap<>();
    private HashMap<Integer, ArrayList<Circle>> mCircleMapEnd = new HashMap<>();

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
                    mSplashSizeEnd = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        array.recycle();

    }

    public void initPaint() {
        mPaintSplashText = new Paint();
        mPaintSplashText.setColor(getResources().getColor(R.color.gray));
        mPaintSplashText.setTextSize(mSplashSizeEnd);
        mPaintSplashText.getTextBounds(mSplashText,0,mSplashText.length(),mBoundEnd);

        mPaintSplashText.setTextSize(mSplashSizeStart);
        mPaintSplashText.getTextBounds(mSplashText, 0, mSplashText.length(), mBound);
        Log.d("init"," mBound--" + mBound.width()+ "---" + mBound.height()
         + "  mBoundEnd--" + mBoundEnd.width() + "--" + mBoundEnd.height());

        int widthSize = mBoundEnd.width() + 40;
        int heightSize = mBoundEnd.height() + 20;
        int r = widthSize/38;
        int b = heightSize/9;
        mSplashSizeX = (getWidth()/2-mBoundEnd.width()/2) - 10 + r;
        mSplashSizeY = (getHeight()/2-mBoundEnd.height()/2) - 12 + r;
        for (int i=0; i<10; i++) {
            Circle circle = new Circle(mSplashSizeX+(4*r)*i,mSplashSizeY,r);
            ArrayList<Circle> arrayList = new ArrayList<>();
            for (int j=0; j<10; j++) {
                Circle circle2 = new Circle(circle.x,circle.y+b*j,r);
                Log.d("circle2","circle2===" + circle2.toString());
                arrayList.add(circle2);
            }
            mCircleMapEnd.put(i,arrayList);
        }


        mPaintCircle = new Paint();
        mPaintCircle.setColor(getResources().getColor(R.color.gray_light));

        mCircleMapStart = new HashMap<>();
        int x = (getWidth()/2-mSplashSizeDef/2);
        int y = getHeight()/2-mSplashSizeDef/2;
        for (int i=0; i<10; i++) {
            Circle circle = new Circle(x + dip2px(20)*i,y,mRadius);
            ArrayList<Circle> arrayList = new ArrayList<>();
            for (int j=0; j<10; j++) {
                Circle circle2 = new Circle(circle.x,circle.y+dip2px(20)*j,mRadius);
                arrayList.add(circle2);
            }
            mCircleMapStart.put(i,arrayList);
        }
    }

    public void circleTest() {

        final Particle particle1 = new Particle(100,100,10);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new LineEvaluator(),particle,particle1);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Particle particle2 = (Particle) animation.getAnimatedValue();
                particle.x = particle2.x;
                particle.y = particle2.y;
                particle.radius = particle2.radius;
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    public void start() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mSplashSizeStart,mSplashSizeEnd);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int textSize = (int) animation.getAnimatedValue();
                mPaintSplashText.setTextSize(textSize);
                mPaintSplashText.getTextBounds(mSplashText, 0, mSplashText.length(), mBound);
                int boundWidth = mBound.width();
                int boundHeight = mBound.height();
                Log.d("onMeasure"," boundWidth--" + boundWidth + "  boundHeight--" + boundHeight);
                postInvalidate();
            }
        });
        valueAnimator.start();

        Collection<Animator> animList = new ArrayList<>();
        AnimatorSet set = new AnimatorSet();
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                Log.d("animList"," mCircleMapStart--" + mCircleMapStart.get(i).get(j).toString()
                 + " mCircleMapEnd---" + mCircleMapEnd.get(i).get(j).toString());
                ValueAnimator objectAnimator =  ObjectAnimator.ofObject(new CircleEvaluator(),mCircleMapStart.get(i).get(j),mCircleMapEnd.get(i).get(j));
                objectAnimator.setDuration(1000 + 50 * i + 30 * j);
                final int finalI = i;
                final int finalJ = j;
                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Circle circle = (Circle) animation.getAnimatedValue();
                        Log.d("onAnimation"," circle--" + circle.toString());
                        mCircleMapStart.get(finalI).get(finalJ).setX(circle.x);
                        mCircleMapStart.get(finalI).get(finalJ).setY(circle.y);
                        mCircleMapStart.get(finalI).get(finalJ).setR(circle.r);
                        postInvalidate();
                    }
                });
                animList.add(objectAnimator);
            }
        }


//        set.setDuration(3000);
        set.playTogether(animList);
        set.start();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPaint();

        Log.d("onSizeChanged"," w--" + w + "  h--" + h + "oldW--" + oldw + "  oldH--" + oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (ArrayList<Circle> array : mCircleMapStart.values()) {
            for (int i=0; i<array.size(); i++) {
                Circle circle = array.get(i);
                canvas.drawCircle(circle.getX(),circle.getY(),circle.getR(),mPaintCircle);
            }
        }
        canvas.drawCircle(particle.x,particle.y,particle.radius,mPaintCircle);
        canvas.drawText(mSplashText, (getWidth() / 2 - mBound.width() / 2), (getHeight() / 2 + mBound.height() / 2), mPaintSplashText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    private class CircleEvaluator implements TypeEvaluator<Circle> {

        @Override
        public Circle evaluate(float fraction, Circle startValue, Circle endValue) {
            Circle particle = new Circle();
            particle.x = startValue.x + (endValue.x - startValue.x) * fraction;
            particle.y = startValue.y + (endValue.y - startValue.y) * fraction;
            particle.r = startValue.r + (endValue.r - startValue.r) * fraction;
            return particle;
        }
    }
    private class Circle {
        private float x;
        private float y;
        private float r;

        public Circle() {

        }
        @Override
        public String toString() {
            return "Circle{" +
                    "x=" + x +
                    ", y=" + y +
                    ", r=" + r +
                    '}';
        }

        public Circle(float x, float y, float r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getR() {
            return r;
        }

        public void setR(float r) {
            this.r = r;
        }
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
