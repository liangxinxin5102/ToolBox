package com.mingrisoft.toolbox.util;

import android.app.AlertDialog;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
/**
 * 文件名:ElectricityBR.java
 * 文件功能描述:注册广播接收者,接收电池电量信息。
 * 开发时间:2016年8月4日
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class ElectricityBR extends BroadcastReceiver {
	Dialog dialog=null;
	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
			int level = intent.getIntExtra("level", 0);//当前电量
			int scale = intent.getIntExtra("scale", 0);//总电量
			int voltage = intent.getIntExtra("voltage", 0);//电压
			int temperature = intent.getIntExtra("temperature", 0);//当前电池温度
			String technology = intent.getStringExtra("technology");//电池类型
			if(dialog==null){
				dialog = new AlertDialog.Builder(context)
				.setTitle("电池电量")
				.setMessage(
						"电池电量为：" + String.valueOf(level * 100 / scale)
						+ "%\n" + "电池电压为："
						+ String.valueOf((float)voltage / 1000) + "v"
						+ "\n电池类型为：" + technology + "\n" + "电池温度为："
						+ String.valueOf((float)temperature / 10) + "°C")
						.setNegativeButton("关闭",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0,
									int arg1) {
							}
						}).create();
				dialog.show();
			}
		}
	}
}
