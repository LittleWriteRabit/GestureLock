package com.mrdo.lock.locklib;

/**
 * Created by dulijie on 2018/8/7.
 * 验证手势密码用
 */
public enum StatusCheck {
    //默认的状态
    DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
    //密码输入错误
    ERROR(R.string.gesture_error, R.color.red_f4333c),
    //密码输入正确
    CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);

    private StatusCheck(int strId, int colorId) {
        this.strId = strId;
        this.colorId = colorId;
    }

    public int strId;
    public int colorId;
}
