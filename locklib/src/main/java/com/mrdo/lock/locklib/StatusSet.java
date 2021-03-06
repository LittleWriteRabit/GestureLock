package com.mrdo.lock.locklib;

/**
 * 设置手势密码用
 */
public enum StatusSet {
	//默认的状态，刚开始的时候（初始化状态）
	DEFAULT(R.string.create_gesture_default, R.color.grey_a5a5a5),
	//第一次记录成功
	CORRECT(R.string.create_gesture_correct, R.color.grey_a5a5a5),
	//连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
	LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
	//二次确认错误
	CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.red_f4333c),
	//二次确认正确
	CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.grey_a5a5a5);

	private StatusSet(int strId, int colorId) {
		this.strId = strId;
		this.colorId = colorId;
	}
	public int strId;
	public int colorId;
}