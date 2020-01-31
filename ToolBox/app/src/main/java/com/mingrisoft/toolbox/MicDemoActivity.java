package com.mingrisoft.toolbox;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 文件名:MicDemoActivity.java 
 * 文件功能描述:分贝测试页面 
 * 开发时间:2016年8月4日 
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class MicDemoActivity extends Activity {
	private MediaRecorder mARecorder;// 麦克风控制
	private File mAudiofile, mSampleDir;// 录音文件保存
	private boolean istrue = true;//用于run()中判断
	private ImageView iv_record_wave_left, iv_record_wave_right;//声音动画图片
	private AnimationDrawable ad_left, ad_right;//动画效果
	private TextView tView1;// 用于显示分贝值
	private MHandler mHandler = new MHandler();// 接收消息修改分贝值用
	class MHandler extends Handler {// 接收消息修改布局
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:// 返回消息1
				tView1.setText(msg.obj.toString());// 修改分贝值
				break;
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕不变黑
		setContentView(R.layout.activity_mic);
		init();//初始化控件
	}
	//初始化控件
	private void init() {
		tView1 = (TextView) findViewById(R.id.textView1);//初始化显示分贝控件
		//初始化左侧动态动画控件
		iv_record_wave_left = (ImageView) findViewById(R.id.iv_record_wave_left);
		//初始化右侧动态动画控件
		iv_record_wave_right = (ImageView) findViewById(R.id.iv_record_wave_right);
		ad_left = (AnimationDrawable) iv_record_wave_left.getBackground();//动画效果
		ad_right = (AnimationDrawable) iv_record_wave_right.getBackground();//动画效果
		ad_left.start();//开启左侧动画
		ad_right.start();//开启右侧动画
	}
	@Override
	protected void onStart() {
		// 录音获取麦克风声音
		super.onStart();
		mARecorder = new MediaRecorder();//声音录制
		mARecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//录制的音源为麦克风
		mARecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);//设置音频文件的编码
		mARecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置audio的格式
		try {
			mSampleDir = Environment.getExternalStorageDirectory();//获取手机内存路径
					//用IM+系统当前时间为文件名建立.amr的文件,文件路径为mSampleDir。
			mAudiofile = File.createTempFile("IM" + System.currentTimeMillis(),
					".amr", mSampleDir);
		} catch (IOException e) {
			Log.e("IMMESSAGE", "sdcard access error");
		}
		mARecorder.setOutputFile(mAudiofile.getAbsolutePath());//设置声音输出文件的路径
		try {
			mARecorder.prepare();//准备录制
		} catch (Exception e) {
			e.printStackTrace();
		}
		mARecorder.start();//开启麦克风声音录制
		new MicrophoneThread().start();//开启线程声音转换成分辨率
	}
	class MicrophoneThread extends Thread {//测量当前分贝值通知ＵＩ修改
		final float minAngle = (float) Math.PI * 4 / 11;
		float angle;//分贝值
		@Override
		public void run() {
			while (istrue) {
				angle = 100 * minAngle * mARecorder.getMaxAmplitude() / 32768;
				if (angle > 100) {
					angle = 100;
				}
				// 构造方法的字符格式这里如果小数不足2位,会以0补足.
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				String p = decimalFormat.format(angle);// format 返回的是字符串 分贝值
				mHandler.sendMessage(mHandler.obtainMessage(1, p));//发送当前分贝值
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void onBack(View v) {
		MicDemoActivity.this.finish();//关闭当前页面
	}



	@Override
	protected void onPause() {
		mARecorder.stop();
		super.onPause();
	}
}
