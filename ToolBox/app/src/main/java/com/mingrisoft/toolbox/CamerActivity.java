package com.mingrisoft.toolbox;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

/**
 * 文件名:CamerActivity.java
 * 文件功能描述:放大镜功能页面
 * 开发时间:2016年8月4日
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class CamerActivity extends Activity implements SurfaceHolder.Callback {
	private SurfaceView mSurfaceView;// 用于显示内容
	private int value = 50;// 记录SeekBar值
	private Camera.Parameters parameters;// 相机服务
	private Camera mCamera;// 注册相机
	private SurfaceHolder holder;// 获取ui线程和 绘制线程
	private boolean mFlag = true;// 判断相机是否开启
	private int mCameraId = 0;// 注册的相机id
	private static final int BACK_CAMERA = 0;//用于判断相机ID
	private static final int ROTATION = 90;// 转动角度
	private static final int REVERT = 180;// 转动角度
	private static final int PREVIEW_WIDTH = 320;// 设置显示相机宽
	private static final int PREVIEW_HEIGHT = 240;// 设置相机高
	private int mZoomMax;// 相机放大倍数
	private SeekBar mSeekBar;// 用于调整放大镜放大缩小
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camer);// 加载布局
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 设置窗体始终点亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置窗体全屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置屏幕为竖屏
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);// 初始化surfaceView
		holder = mSurfaceView.getHolder();// 获得句柄
		holder.addCallback(this);// 添加回调
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// // 兼容旧的API
		mSeekBar = (SeekBar) findViewById(R.id.seekBar);// 初始话seekbar
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {// seekBar改变监听
			/**
			 * 停止拖动时触发操作
			 * @param seekBar
			 *            触发操作的SeekBar组件对象
			 */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				parameters.setZoom(value);// 设置放大倍数
				mCamera.setParameters(parameters);// 设置参数
				mCamera.startPreview();// 开启相机预览
			}
			/**
			 * 开始拖动时触发方法
			 *
			 * @param seekBar
			 *            触发操作的SeekBar组件对象
			 */
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			/**
			 * 改变中触发
			 *
			 * @param seekBar
			 *            触发操作的SeekBar组件对象
			 * @param progress
			 *            当前进入值
			 * @param fromUser
			 *            是否为用户自己触发
			 *
			 */
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				value = progress + 50;// 放大倍数
			}
		});
	}
	@Override
	protected void onPause() {// 应用程序启动其它Acitivity 时调用。
		// TODO Auto-generated method stub
		super.onPause();
		try {
			if (mCamera != null) {
				mCamera.stopPreview();// 停止预览
				mCamera.release();// 释放手机摄像头
				mCamera = null;// 致空摄像头
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 开启相机
	 */
	private void startCamera() {
		// TODO Auto-generated method stub
		if (mFlag) {
			if (mCamera != null)  {// 判断相机是否为空
				mCamera.stopPreview();// 停止相机服务
				mCamera.release();// 释放手机摄像头
				mCamera = null;//设置相机为空
			}
		}
		try {
			mCamera = Camera.open(mCameraId);// 开启id为mCameraId的相机
		} catch (RuntimeException e) {
			e.printStackTrace();
			mCamera = null;//设置相机为空
		}
		if (mCamera != null) {// 判断相机是否为空
			mCamera.setDisplayOrientation(mCameraId == BACK_CAMERA ? ROTATION
					: ROTATION);
			parameters = mCamera.getParameters();// 获取相机参数
			// 设置对焦模式为持续对焦，（最好先判断一下手机是否有这个对焦模式，有些手机没有会报错的）
			parameters
					.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			parameters.setPictureFormat(PixelFormat.JPEG);// 设置预览照片的大小
			parameters.set("orientation", "portrait");//解决自定义相机角度问题
			parameters.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);// 设置预览照片的大小
			parameters.setRotation(mCameraId == BACK_CAMERA ? ROTATION : REVERT
					+ ROTATION);// 设置预览旋转角度
			mZoomMax = value;// 获取最大像素
			parameters.setZoom(mZoomMax);// 设置放大倍数
			mCamera.setParameters(parameters);// 设置相机参数
			try {
				mCamera.setPreviewDisplay(holder);// 设置相机预览
				mCamera.startPreview();// 开始预览
				mFlag = true;// 记录相机开启
			} catch (Exception e) {
				mCamera.release();// 释放相机
			}
		}
	}
	/**
	 *  在surface的大小发生改变时激活
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		startCamera();// 当surfaceview创建时开启相机
	}
	/**
	 * 在创建时激发，一般在这里调用画图的线程
	 */
	public void surfaceCreated(SurfaceHolder holder) {
	}
	/**
	 * 销毁时激发，一般在这里将画图的线程停止、释放
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	/**
	 * 返回功能
	 */
	public void onBack(View v) {
		CamerActivity.this.finish();// 结束当前页面
	}
}