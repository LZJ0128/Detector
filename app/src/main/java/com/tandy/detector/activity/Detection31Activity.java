package com.tandy.detector.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tandy.detector.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 3/1 0001.
 * 液晶判断
 */
public class Detection31Activity extends Activity {

    private View mViewColor;
    /**
     * 通过颜色（红、绿、蓝、黑、白）来判断液晶是否有坏点或者变色
     */
    int[] mColors = {Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, Color.WHITE};
    int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detection31);
        mViewColor = (View)this.findViewById(R.id.view_color);
        timer.schedule(task, 0, 3000);
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if (msg.what == 1){
                if (num <= 4) {
                    mViewColor.setBackgroundColor(mColors[num++]);
                }else {
                    setResult(31);
                    finish();
                }
            }
        }
    };

    private Timer timer = new Timer();

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    };

}
