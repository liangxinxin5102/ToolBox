package com.mingrisoft.toolbox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * 文件名:RulerView.java
 * 文件功能描述:自定义尺子view
 * 开发时间:2016年8月4日
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class RulerView extends View {
    private float xcm;// 厘米
    private float xmm;// 毫米
    private float ruler_length;// 尺子宽度
    private float ruler_width;// 尺子高度
    private PointF mid_point = new PointF(0, 0);// 尺子中点坐标
    private float angle_rotate = 90;// 旋转角度
    private PointF touchPoint = new PointF(0, 0);//手指在屏幕坐标
    private float wdth;//屏幕宽
    public RulerView(Context context, DisplayMetrics dm) {
        super(context);
        find_pixal(dm);// 设置xcm大小
        ruler_length = 50 * xcm; // 设置一开始为6厘米的尺子
        ruler_width = 2 * xcm;// 尺子宽度
        wdth = dm.widthPixels;
        mid_point.set(dm.widthPixels, (float) (ruler_length * 0.5));// 尺子中点坐标点
    }
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();// 画笔
        paint.setColor(Color.WHITE);// 画笔颜色
        paint.setStyle(Style.STROKE);// 画笔样式
        paint.setStrokeWidth(2);// 画笔宽度
        paint.setTextSize(30);// 画笔文字大小
        canvas.save();// 保存信息
        canvas.rotate(angle_rotate, mid_point.x, mid_point.y);// 确定尺子中心
        // 绘制矩形
        canvas.drawRect(mid_point.x - ruler_length / 2, mid_point.y,
                mid_point.x + ruler_length / 2, mid_point.y + ruler_width,
                paint);
        // 画线以及文字 绘制厘米，毫米 刻度
        for (int i = 0; i < ruler_length / xmm; i++) {
            float Left = mid_point.x - ruler_length / 2;
            if (i % 10 == 0 && i != 0) {
                canvas.drawLine(Left + i * xmm, mid_point.y, Left + i * xmm,
                        mid_point.y + 50, paint);//绘制尺子长线
                canvas.drawText(Integer.toString(i / 10), Left + i * xmm,
                        mid_point.y + 55, paint);//绘制尺子可读值
            } else if (i == 0) {
                canvas.drawLine(Left + i * xmm, mid_point.y, Left + i * xmm,
                        mid_point.y + 50, paint);//绘制尺子0位置的线
                canvas.drawText(Integer.toString(i / 5) + "cm", Left + i * xmm,
                        mid_point.y + 55, paint);//绘制尺子0刻度的数字
            } else {
                canvas.drawLine(Left + i * xmm, mid_point.y, Left + i * xmm,
                        mid_point.y + 30, paint);//绘制尺子短线
            }
        }
        canvas.restore();// 可以取出画笔状态改变再次绘制的时候不影响
        drawVerticalLine(canvas);//绘制辅助红线

    }
    /**
     * 获取xcm，以及xmm在屏幕上的大小
     */
    protected void find_pixal(DisplayMetrics dm) {
        xcm = (float) (dm.xdpi / 2.54); // 单位都是pixal
        xmm = xcm / 10;
    }
    protected void drawVerticalLine(Canvas canvas) {//绘制辅助红线
        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);//红色画笔
        mPaint.setStyle(Style.STROKE);// 画笔样式
        mPaint.setStrokeWidth(2);// 画笔宽度
        canvas.drawLine(0, touchPoint.y, wdth, touchPoint.y,
                mPaint);//绘制辅助线
    }
    public boolean onTouchEvent(MotionEvent event) {//触摸控制改变辅助线位置
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 手指按下
            case MotionEvent.ACTION_DOWN:
                touchPoint.set(event.getX(), event.getY());//设置手指按下坐标
                invalidate();//重新绘制
                break;
            // 手指移动
            case MotionEvent.ACTION_MOVE:
                touchPoint.set(event.getX(), event.getY());//设置手指移动坐标
                invalidate();//重新绘制
                break;
           // 手指抬起
            case MotionEvent.ACTION_UP:
                touchPoint.set(event.getX(), event.getY());//设置手指抬起坐标
                invalidate();//重新绘制
                break;
        }
        return true;
    }
}
