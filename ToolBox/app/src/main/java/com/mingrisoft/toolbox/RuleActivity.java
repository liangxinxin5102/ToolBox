package com.mingrisoft.toolbox;


import com.mingrisoft.toolbox.view.RulerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.RelativeLayout;
/**
 * 文件名:RuleActivity.java
 * 文件功能描述:工具尺子页面
 * 开发时间:2016年8月4日
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class RuleActivity extends Activity {
	private DisplayMetrics dm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕不变黑  
		setContentView(R.layout.activity_rule);
		//获取屏幕信息
        dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		//初始化尺子
		RulerView rulerView1 = new RulerView(this, dm);
		//获取布局
		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.main_layout);
		//相对布局中添加尺子布局
		relativeLayout.addView(rulerView1);	
	}
}
