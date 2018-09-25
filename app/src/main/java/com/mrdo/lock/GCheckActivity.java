package com.mrdo.lock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.mrdo.lock.locklib.ACache;
import com.mrdo.lock.locklib.LockPatternUtil;
import com.mrdo.lock.locklib.LockPatternView;
import com.mrdo.lock.locklib.StatusCheck;

import java.util.List;

/**
 * Created by dulijie on 2018/9/25.
 */
public class GCheckActivity extends AppCompatActivity implements LockPatternView.OnPatternListener {

    ACache aCache;
    private byte[] gesturePassword;
    private static final long DELAYTIME = 600L;

    TextView tvCheckTips;
    LockPatternView lockPatternView;

    private int type = -1;

    private int count = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_check_gesture);
        tvCheckTips=  findViewById(R.id.tv_check_tips);
        lockPatternView= findViewById(R.id.lockPatternView);

        aCache = ACache.get(GCheckActivity.this);
        //得到当前用户的手势密码
        gesturePassword = aCache.getAsBinary(Constants.GESTURE_PASSWORD);
//        gesturePassword=SPUtils.getInstance().getString(Constants.GESTURE_PASSWORD).getBytes();
        lockPatternView.setOnPatternListener(this);
        updateStatus(StatusCheck.DEFAULT);
    }

    /**
     * 更新状态
     *
     * @param status
     */
    private void updateStatus(StatusCheck status) {
        tvCheckTips.setText(status.strId);
        tvCheckTips.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                count++;
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                if (count >= 5) {
                    //最多錯誤五次
                    Toast.makeText(this,R.string.check_error_over_time,Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
        Toast.makeText(this,R.string.gesture_login_success,Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPatternStart() {
        lockPatternView.removePostClearPatternRunnable();
    }

    @Override
    public void onPatternComplete(List<LockPatternView.Cell> pattern) {
        if (pattern != null) {
            if (LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                updateStatus(StatusCheck.CORRECT);
            } else {
                updateStatus(StatusCheck.ERROR);
            }
        }
    }
}
