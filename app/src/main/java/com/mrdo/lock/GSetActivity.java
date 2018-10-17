package com.mrdo.lock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.mrdo.lock.locklib.LockPatternUtil;
import com.mrdo.lock.locklib.LockPatternView;
import com.mrdo.lock.locklib.PreferencesUtility;
import com.mrdo.lock.locklib.StatusSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dulijie on 2018/9/25.
 */
public class GSetActivity extends AppCompatActivity implements LockPatternView.OnPatternListener {
    TextView tvSetTitle;
    TextView tvSetTip;
    LockPatternView lockPatternView;
    private List<LockPatternView.Cell> mChosenPattern = null;
    private static final long DELAYTIME = 600L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_set_gesture);
        tvSetTitle = findViewById(R.id.tv_set_title);
        tvSetTip = findViewById(R.id.tv_set_tip);
        lockPatternView = findViewById(R.id.lockPatternView);

//        aCache = ACache.get(GSetActivity.this);
        lockPatternView.setOnPatternListener(this);
    }

    @Override
    public void onPatternStart() {
        lockPatternView.removePostClearPatternRunnable();
        //updateStatus(Status.DEFAULT, null);
        lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
    }

    @Override
    public void onPatternComplete(List<LockPatternView.Cell> pattern) {
        //Log.e(TAG, "--onPatternDetected--");
        if (mChosenPattern == null && pattern.size() >= 4) {
            mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
            updateStatus(StatusSet.CORRECT, pattern);
        } else if (mChosenPattern == null && pattern.size() < 4) {
            updateStatus(StatusSet.LESSERROR, pattern);
        } else if (mChosenPattern != null) {
            if (mChosenPattern.equals(pattern)) {
                updateStatus(StatusSet.CONFIRMCORRECT, pattern);
            } else {
                updateStatus(StatusSet.CONFIRMERROR, pattern);
            }
        }
    }

    /**
     * 更新状态
     *
     * @param status
     * @param pattern
     */
    private void updateStatus(StatusSet status, List<LockPatternView.Cell> pattern) {
        tvSetTitle.setTextColor(getResources().getColor(status.colorId));
        tvSetTitle.setText(status.strId);
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
//                updateLockPatternIndicator();
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case LESSERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CONFIRMERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                setLockPatternSuccess();
                break;
        }
    }

    /**
     * 保存手势密码
     */
    private void saveChosenPattern(List<LockPatternView.Cell> cells) {
        String pwd = LockPatternUtil.paramLockPwd(cells);
        PreferencesUtility.getInstance(this).put(Constants.GESTURE_PASSWORD, pwd);
    }

    /**
     * 成功设置了手势密码(跳到首页)
     */
    private void setLockPatternSuccess() {
        Toast.makeText(this, R.string.gesture_set_success, Toast.LENGTH_SHORT).show();
        //保存手势状态及设置手势的用户
        finish();
    }
}
