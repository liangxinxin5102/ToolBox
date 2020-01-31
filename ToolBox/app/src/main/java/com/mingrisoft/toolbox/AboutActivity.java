package com.mingrisoft.toolbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * 文件名:AboutActivity.java
 * 文件功能描述:关于页面
 * 开发时间:2016年8月4日
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);//加载关于布局

}
	/**
	 * 返回功能
	 */
	public void onBack(View v) {
		AboutActivity.this.finish();//关闭关于页面
	}
}
