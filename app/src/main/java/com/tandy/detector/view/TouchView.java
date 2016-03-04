package com.tandy.detector.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.tandy.detector.R;
import com.tandy.detector.activity.Detection31Activity;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 3/1 0001.
 * 绘制全屏
 */
public class TouchView extends View {

    private Paint mOutterPaint;
    private Path mPath;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private int mLastX;
    private int mLastY;
    private Bitmap mOutterBitmap;
    private Paint mOutBmpPaint;
    //当前绘制的百分比
    private double mCurrentPercent = 0.0;
    //上次绘制的百分比
    private double mLastPercent = -1.0;
    //绘制的宽度
    public final int SWIPE_PAINT_WIDTH = 150;

    private OnCompleteListener mOnCompleteListener;

    public void setOnCompleteListener(OnCompleteListener onCompleteListener){
        this.mOnCompleteListener = onCompleteListener;
    }

    private volatile boolean mCompleted = false;

    /**
     * 画完之后的回调接口
     */
    public interface OnCompleteListener{
        void complete();
    }

    public TouchView(Context context){
        this(context, null);
    }

    public TouchView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public TouchView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 进行初始化操作
     */
    private void initView(){
        mOutterPaint = new Paint();
        mOutBmpPaint = new Paint();
        mPath = new Path();
        mOutterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //初始化bitmap
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //初始化canvas
        mCanvas = new Canvas(mBitmap);
        //设置画笔的一些属性
        setOutterPaint();
        //绘制一层
        mCanvas.drawRoundRect(new RectF(0, 0, width, height), 30, 30, mOutBmpPaint);
        mCanvas.drawBitmap(mOutterBitmap, null, new RectF(0, 0, width, height), null);
    }

    /**
     * 设置画笔的属性
     */
    private void setOutterPaint(){
        mOutterPaint.setColor(Color.BLUE);
        mOutterPaint.setAntiAlias(true);
        mOutterPaint.setDither(true);
        mOutterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutterPaint.setStyle(Paint.Style.STROKE);
        mOutterPaint.setStrokeWidth(SWIPE_PAINT_WIDTH);
    }

    /**
     * 设置Xfermode模式为DST_OUT，并绘制扫的路径
     */
    private void drawPath(){
        mOutterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath, mOutterPaint);
    }

    @Override
    protected void onDraw(Canvas canvas){
        //设置画笔的颜色
        canvas.drawColor(Color.BLUE);
        drawPath();
        canvas.drawBitmap(mBitmap, 0, 0, null);
        //刮扫完成回调
        if(mCompleted){
            if(null != mOnCompleteListener){
                mOnCompleteListener.complete();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);
                if (dx > 3 || dy > 3){
                    mPath.lineTo(x, y);
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                countArea();
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 计算已经扫的面积及占总区域的比例
     * 根据百分比由用户自己来判断是否完成
     */
    public void countArea(){
        int w = getWidth();
        int h = getHeight();

        float wipeArea = 0;
        float totalArea = w * h ;

        Bitmap bitmap = mBitmap;

        int[] mPixels = new int[w * h];
        //获取bitmap的所有像素信息
        bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);
        for(int i= 0; i< w;i++)
            for(int j= 0; j< h;j++){
                int index = i + j * w;
                if(mPixels[index] == 0){
                    wipeArea ++;
                }
            }
        //计算已扫区域所占的比例
        if(wipeArea >0 && totalArea > 0){
            mLastPercent = mCurrentPercent;
            mCurrentPercent = (double) (wipeArea * 100.0 / totalArea);
            //保留一位小数
            mCurrentPercent = Double.parseDouble(new DecimalFormat("0.0").format(mCurrentPercent));
        }
        if (mCurrentPercent == mLastPercent) {
            showDialog11(mCurrentPercent);
        }
    }

    /**
     * 显示一个dialog，由用户根据百分比判断是否完成绘制
     * @param percent
     */
    public void showDialog11(double percent){
        new AlertDialog.Builder(getContext())
                .setTitle("当前完成度" + percent + "%")
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("我已完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //绘制完成，返回
                        mCompleted = true;
                        postInvalidate();
                    }
                }).show();
    }
}
