package com.tandy.detector.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tandy.detector.R;
import com.tandy.detector.view.TouchView;

public class Detection11Activity extends Activity {

    private TouchView mTouchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detection11);
        mTouchView = (TouchView)findViewById(R.id.view_touch);
        mTouchView.setOnCompleteListener(new TouchView.OnCompleteListener() {
            @Override
            public void complete() {
                setResult(11);
                finish();
            }
        });
    }
}
