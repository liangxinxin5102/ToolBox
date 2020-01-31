package com.mingrisoft.toolbox;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mingrisoft.toolbox.util.ElectricityBR;
import com.mingrisoft.toolbox.util.PowerLED;
import com.mingrisoft.toolbox.view.CircleMenuLayout;
import com.mingrisoft.toolbox.view.CircleMenuLayout.OnMenuItemClickListener;

import org.w3c.dom.Text;

/**
 * 文件名:MainActivity.java
 * 文件功能描述:程序主入口
 * 开发时间:2016年8月4日
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class MainActivity extends Activity implements SensorEventListener {

    private CircleMenuLayout mCircleMenuLayout;//自定义圆盘菜单
    private String[] mItemTexts = new String[]{"放大镜 ", "尺子", "分贝测试仪", "手电筒",
            "计算器", "SOS"};//圆盘菜单显示文字
    private int[] mItemImgs = new int[]{R.drawable.home_mbank_1_normal,
            R.drawable.home_mbank_2_normal, R.drawable.home_mbank_3_normal,
            R.drawable.home_mbank_4_normal, R.drawable.home_mbank_5_normal,
            R.drawable.home_mbank_6_normal};//圆盘菜单显示图片
    // 定义显示指南针图片的组件
    private ImageView image_znz;
    // 记录指南针图片转过的角度
    private float angle = 0f;
    // 定义真机的Sensor管理器
    private SensorManager mSensorManager;

    //是否开启手电筒判断标示 默认关闭
    private boolean status = false;
    private PowerLED powerled;//闪光灯控制

    // sos闪光灯是否开启判断
    private boolean firsttime = false;
    // 线程执行闪光灯闪烁方法
    private Handler show_handler;
    private Runnable show_runnable;
    private int warmingcounter = -1;
    //sos 闪烁时间
    private int[] bgflashtime = new int[]{300, 300, 300, // ... S
            300, 300,
            //
            900,
            //
            900, 300, 900, // --- O
            300, 900,
            //
            900,
            //
            300, 300, 300, // ... S
            300, 300,
            //
            2100};
     // 剩余电量显示textview
    private TextView batterytv;
     // 不同电量显示图片数组
    private int[] batterystatusimgs = {R.drawable.battery1,
            R.drawable.battery2, R.drawable.battery3};
    //电量多少切换图片的标准数组
    private int[] batterystatuspercent = {75, 30, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//导入布局
        powerled=new PowerLED(this);
        batterytv=(TextView)findViewById(R.id.batterytv);
        image_znz = (ImageView) findViewById(R.id.iv_znz);//指南针图片
        // 获取真机的传感器管理服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
       // 为系统的方向传感器注册监听器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        batterytv = (TextView) findViewById(R.id.batterytv);			//电池信息显示
        // 电池电量监听显示注册广播接收器
        registerReceiver(mBatInfoReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));

        //初始化圆盘控件
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        //初始化圆盘控件菜单
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
        //自定义控件点击事件
        mCircleMenuLayout
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public void itemClick(View view, int pos) {
                        if (pos == 0) {//下标为0的按钮点击事件
                            /**
                             * 进入放大镜页面
                             */
                            Intent intent = new Intent(MainActivity.this,
                                    CamerActivity.class);
                            startActivity(intent);
                        }
                        if (pos == 1) {//下标为1的按钮点击事件
                            /**
                             * 进入工具尺子页面
                             */
                            Intent intent = new Intent(MainActivity.this,
                                    RuleActivity.class);
                            startActivity(intent);
                        }
                        if (pos == 2) {//下标为2的按钮点击事件
                            /**
                             * 进入分贝测试页面
                             */
                            Intent intent = new Intent(MainActivity.this,
                                    MicDemoActivity.class);
                            startActivity(intent);
                        }
                        if (pos == 3) {//下标为3的按钮点击事件
                             //开启关闭手电筒功能
                            onFlashlight();
                        }
                        if (pos == 4) {//下标为4的按钮点击事件
                             //进入计算器页面
                            Intent intent = new Intent(MainActivity.this,
                                    CalcActivity.class);
                            startActivity(intent);
                        }
                        if (pos == 5) {//下标为5的按钮点击事件
                             //sos闪光灯闪烁功能
                            onSOS();
                        }
                    }

                    @Override
                    //圆盘中间点击事件
                    public void itemCenterClick(View view) {
                         // 进入关于页面
                        Intent intent = new Intent(MainActivity.this,
                                AboutActivity.class);
                        startActivity(intent);
                    }
                });
        /**
         * sos 闪烁方法执行线程
         */
        show_handler = new Handler();
        show_runnable = new Runnable() {
            @Override
            public void run() {
                warmingcounter++;
                if (warmingcounter % 2 == 0) {
                    powerled.turnOn();// 开启闪光灯
                } else {
                    powerled.turnOff();// 关闭闪光灯
                }
                show_handler
                        .postDelayed(this, bgflashtime[warmingcounter % 18]);
            }
        };

    }

    /**
     * 生命周期页面开始时候运行
     */
    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        // 为系统的方向传感器注册监听器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        powerled = new PowerLED(this);//初始化闪光灯


    }

    /**
     * 生命周期应用暂停当前Activity的时候调用
     */
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // 取消注册
        mSensorManager.unregisterListener(this);
        powerled.Destroy();// 释放相机资源关闭闪光灯
    }

    /**
     * 传感器报告新的值（方向改变）
     */
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        // 如果真机上触发event的传感器类型为水平传感器类型
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            // 获取绕Z轴转过的角度
            float Zangle = event.values[0];
            // 创建旋转动画（反向转过Zangle度）
            RotateAnimation ra = new RotateAnimation(angle, -Zangle,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            // 设置动画的持续时间
            ra.setDuration(200);
            // 设置动画结束后的保留状态
            ra.setFillAfter(true);
            // 启动动画
            image_znz.startAnimation(ra);
            angle = -Zangle;
        }
    }

    /**
     * 传感器精度的改变
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }


    /**
     * SOS功能的开启关闭
     */
    public void onSOS() {
        powerled.turnOff();//关闭闪光灯
        status = false;//手电筒关闭
        if (!firsttime) {
            firsttime = true;//闪光灯开启
            show_handler.postDelayed(show_runnable, 50);//延时处理每五十毫秒执行闪光灯功能
        } else {
            firsttime = false;//闪光灯关闭
            powerled.turnOff();//关闭闪光灯
            show_handler.removeCallbacks(show_runnable);//删除线程
        }
    }

    /**
     * 电池信息
     */
    public void onDC(View v) {
        ElectricityBR receiver = new ElectricityBR();//接受电量信息
        IntentFilter filter = new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED);//声明电量信息接收者
        MainActivity.this.registerReceiver(receiver, filter);//注册广播
    }
    /**
     * 手电筒的开启关闭
     */
    public void onFlashlight() {
        powerled.turnOff();// 关闭闪光灯
        firsttime = false;
        show_handler.removeCallbacks(show_runnable);
        if (!status) {
            status = true;
            powerled.turnOn();// 开启闪光灯
        } else {
            status = false;
            powerled.turnOff();// 关闭闪光灯
        }
    }

    /**
     * 创建广播接收 电池电量信息
     */
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int intLevel = intent.getIntExtra("level", 0);//当前电量
                int intScale = intent.getIntExtra("scale", 100);//总电量
                onBatteryInfoReceiver(intLevel, intScale);
            }
        }
    };

    /**
     * 设置电量百分比，切换电池不同电量图片
     */
    public void onBatteryInfoReceiver(int intLevel, int intScale) {
        int bp = intLevel * 100 / intScale;
        batterytv.setText(bp + "%");
        if (bp >= batterystatuspercent[2]) {//电量大于0的时候设置电池背景
            batterytv.setBackgroundResource(batterystatusimgs[2]);//设置电池背景
        }
        if (bp >= batterystatuspercent[1]) {//电量大于30%的时候设置电池背景
            batterytv.setBackgroundResource(batterystatusimgs[1]);//设置电池背景
        }
        if (bp >= batterystatuspercent[0]) {//电量大于70%的时候设置电池背景
            batterytv.setBackgroundResource(batterystatusimgs[0]); //设置电池背景
        }
    }
}
