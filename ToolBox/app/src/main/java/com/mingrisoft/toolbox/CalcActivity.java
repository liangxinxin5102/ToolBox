package com.mingrisoft.toolbox;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 文件名:CalcActivity.java
 * 文件功能描述:计算器功能
 * 开发时间:2016年8月4日
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class CalcActivity extends Activity implements OnClickListener {
	private Button btn_0;// 数字0-9
	private Button btn_1;
	private Button btn_2;
	private Button btn_3;
	private Button btn_4;
	private Button btn_5;
	private Button btn_6;
	private Button btn_7;
	private Button btn_8;
	private Button btn_9;
	private Button btn_point;// 小数点
	private Button btn_plus;// 运算符
	private Button btn_minus;
	private Button btn_mul;
	private Button btn_div;
	private Button btn_equ;
	private Button btn_root;// 平方根(暂未用到)
	private Button btn_delete;
	private Button btn_clear;
	private EditText et_input;
	private boolean clear_flag = false; // 清空标识
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calc);
		// 实例化按钮
		btn_0 = (Button) findViewById(R.id.btn_0);
		btn_1 = (Button) findViewById(R.id.btn_1);
		btn_2 = (Button) findViewById(R.id.btn_2);
		btn_3 = (Button) findViewById(R.id.btn_3);
		btn_4 = (Button) findViewById(R.id.btn_4);
		btn_5 = (Button) findViewById(R.id.btn_5);
		btn_6 = (Button) findViewById(R.id.btn_6);
		btn_7 = (Button) findViewById(R.id.btn_7);
		btn_8 = (Button) findViewById(R.id.btn_8);
		btn_9 = (Button) findViewById(R.id.btn_9);
		btn_plus = (Button) findViewById(R.id.btn_plus);
		btn_minus = (Button) findViewById(R.id.btn_minus);
		btn_mul = (Button) findViewById(R.id.btn_multiply);
		btn_div = (Button) findViewById(R.id.btn_divide);
		btn_equ = (Button) findViewById(R.id.btn_equal);
		btn_delete = (Button) findViewById(R.id.btn_backspace);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_point = (Button) findViewById(R.id.btn_point);
		btn_root = (Button) findViewById(R.id.btn_root);
		et_input = (EditText) findViewById(R.id.et1);
		// 给按钮添加监听
		btn_0.setOnClickListener(this);
		btn_1.setOnClickListener(this);
		btn_2.setOnClickListener(this);
		btn_3.setOnClickListener(this);
		btn_4.setOnClickListener(this);
		btn_5.setOnClickListener(this);
		btn_6.setOnClickListener(this);
		btn_7.setOnClickListener(this);
		btn_8.setOnClickListener(this);
		btn_9.setOnClickListener(this);
		btn_plus.setOnClickListener(this);
		btn_minus.setOnClickListener(this);
		btn_mul.setOnClickListener(this);
		btn_div.setOnClickListener(this);
		btn_equ.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
		btn_point.setOnClickListener(this);
		btn_root.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		String str = et_input.getText().toString();// 获取输入框内容
		switch (v.getId()) {
		case R.id.btn_0:// 若是数字和小数点，则显示出来
		case R.id.btn_1:
		case R.id.btn_2:
		case R.id.btn_3:
		case R.id.btn_4:
		case R.id.btn_5:
		case R.id.btn_6:
		case R.id.btn_7:
		case R.id.btn_8:
		case R.id.btn_9:
			if (clear_flag) { // 若需要清空，设置为空
				clear_flag = false;
				str = "";
				et_input.setText("");
			}
			et_input.setText(str + ((Button) v).getText()); // 把数字添加到输入框
			break;
		case R.id.btn_point:
			if (clear_flag) {
				clear_flag = false;
				str = "";
				et_input.setText("");
			}
			if (str.contains(".")) {// 若已有小数点，无操作
				return;
			}
			et_input.setText(str + ((Button) v).getText()); // 把小数点添加到输入框
			break;
		case R.id.btn_plus:// 把加号添加到输入框
			if (clear_flag) {
				clear_flag = false;
				str = "";
				et_input.setText("");
			}
			if (str.contains(" ")) {// 若已含运算符，无操作（防止出现连续运算符）
				return;
			}
			et_input.setText(str + " " + ((Button) v).getText() + " ");// 运算符前后加空格，添加到输入框
			break;
		case R.id.btn_minus:
			if (clear_flag) {
				clear_flag = false;
				str = "";
				et_input.setText("");
			}
			if (str.contains(" ")) {
				return;
			}
			et_input.setText(str + " " + ((Button) v).getText() + " ");
			break;
		case R.id.btn_multiply:
			if (clear_flag) {
				clear_flag = false;
				str = "";
				et_input.setText("");
			}
			if (str.contains(" ")) {
				return;
			}
			et_input.setText(str + " " + ((Button) v).getText() + " ");
			break;
		case R.id.btn_divide:
			if (clear_flag) {
				clear_flag = false;
				str = "";
				et_input.setText("");
			}
			if (str.contains(" ")) {
				return;
			}
			et_input.setText(str + " " + ((Button) v).getText() + " ");
			break;
		case R.id.btn_root:
			if (clear_flag) {
				clear_flag = false;
				str = "";
				et_input.setText("");
			}
			if (str.contains(" ")) {
				return;
			}
			if (!(str == null || str.isEmpty())) {// 若不为空，无操作（保证根号在最前面）
				return;
			}
			et_input.setText(str + " " + ((Button) v).getText() + " ");
			break;
		case R.id.btn_backspace:// 退格键删除最后一位
			if (clear_flag) {
				clear_flag = false;
				str = "";
				et_input.setText("");
			}
			if (str != null && !str.equals("")) { // 不为null或""，删除最后一位
				et_input.setText(str.substring(0, str.length() - 1));
			}
			break;
		case R.id.btn_clear: // 清空键，清屏
			et_input.setText("");
			break;
		case R.id.btn_equal:// 若为等号，获取运算结果
			getResult();
			clear_flag = true;// 设置清屏标志位
			break;
		}
	}

	/**
	 * 获取运算结果
	 */
	private void getResult() {
		String exp = et_input.getText().toString();// 获取输入框的内容
		if (exp == null || exp.equals("")) { // 为空则不作处理
			return;
		}
		if (!exp.contains(" ")) { // 不包含运算符，不作处理
			return;
		}
		double result = 0;// 初始化运算结果
		String s1 = exp.substring(0, exp.indexOf(" ")); // 截取运算符前面的字符串
		String op = exp.substring(exp.indexOf(" ") + 1, exp.indexOf(" ") + 2); // 截取运算符
		String s2 = exp.substring(exp.indexOf(" ") + 3); // 截取运算符后面的字符串
		if (!s1.equals("") && !s2.equals("")) {// 当 s1 和 s2 均不为空
			double d1 = Double.parseDouble(s1); // 强转为double类型
			double d2 = Double.parseDouble(s2);
			if (op.equals("+")) {
				result = d1 + d2;
			} else if (op.equals("-")) {
				result = d1 - d2;
			} else if (op.equals("×")) {
				result = d1 * d2;
			} else if (op.equals("÷")) {
				if (d2 == 0) { // 除数为0，结果设为0
					result = 0;
				} else {
					result = d1 / d2;
				}
			}
			if (!s1.contains(".") && !s2.contains(".") && !op.equals("÷")) { // 若s1和s2都是不包含小数点（即都是整数），且不是除法，输出为整数
				int r = (int) result;
				et_input.setText(r + "");
			} else {
				et_input.setText(result + "");
			}
		} else if (!s1.equals("") && s2.equals("")) { // s1不为空，s2为空，不作处理
			return;
		} else if ((s1.equals("") || s1 == null) && !s2.equals("")) { // s1为空，s2不空，把s1当0处理
			double d2 = Double.parseDouble(s2);
			if (op.equals("+")) {
				result = 0 + d2;
			} else if (op.equals("-")) {
				result = 0 - d2;
			} else if (op.equals("×")) {
				result = 0 * d2;
			} else if (op.equals("÷")) {
				result = 0;
			} 
		} else if (s1.equals("") && s2.equals("")) { // s1、s2都为空
			et_input.setText("");
		}
	}
}