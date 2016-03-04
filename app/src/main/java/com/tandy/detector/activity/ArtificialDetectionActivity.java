package com.tandy.detector.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tandy.detector.R;
import com.tandy.detector.helper.DialogHelper;

/**
 * Created by Administrator on 2/29 0029.
 * 人工检测主页
 */
public class ArtificialDetectionActivity extends Activity implements View.OnClickListener{

    private TextView mTxv11, mTxv12, mTxv21, mTxv22, mTxv31, mTxv32;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artificial_detection);
        initUI();
    }

    public void initUI(){
        this.findViewById(R.id.rel_11).setOnClickListener(this);
        this.findViewById(R.id.rel_12).setOnClickListener(this);
        this.findViewById(R.id.rel_21).setOnClickListener(this);
        this.findViewById(R.id.rel_22).setOnClickListener(this);
        this.findViewById(R.id.rel_31).setOnClickListener(this);
        this.findViewById(R.id.rel_32).setOnClickListener(this);
        mTxv11 = (TextView)findViewById(R.id.txv_11);
        mTxv12 = (TextView)findViewById(R.id.txv_12);
        mTxv21 = (TextView)findViewById(R.id.txv_21);
        mTxv22 = (TextView)findViewById(R.id.txv_22);
        mTxv31 = (TextView)findViewById(R.id.txv_31);
        mTxv32 = (TextView)findViewById(R.id.txv_32);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rel_11:
                showDialog11();
                break;
            case R.id.rel_12:
                DialogHelper.showDialog(this, R.string.detection_12_title, R.string.detection_12_btn1, R.string.detection_12_btn2, mTxv12);
                break;
            case R.id.rel_21:
                DialogHelper.showDialog(this, R.string.detection_21_title, R.string.detection_21_btn1, R.string.detection_21_btn2, mTxv21);
                break;
            case R.id.rel_22:
                Intent intent22 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:112"));
                startActivityForResult(intent22, 22);
                break;
            case R.id.rel_31:
                showDialog31();
                break;
            case R.id.rel_32:
                DialogHelper.showDialog(this, R.string.detection_32_title, R.string.detection_32_btn1, R.string.detection_32_btn2, mTxv32);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 11:
                DialogHelper.showDialog(this, R.string.detection_11_title, R.string.detection_11_btn1, R.string.detection_11_btn2, mTxv11);
                break;
            case 22:
                DialogHelper.showDialog(this, R.string.detection_22_title, R.string.detection_22_btn1, R.string.detection_22_btn2, mTxv22);
                break;
            case 31:
                DialogHelper.showDialog(this, R.string.detection_31_title, R.string.detection_31_btn1, R.string.detection_31_btn2, mTxv31);
                break;
            default:
                break;
        }
    }

    public void showDialog31(){
        new AlertDialog.Builder(this)
                .setTitle("液晶判断")
                .setMessage(R.string.detection_31_continue)
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(ArtificialDetectionActivity.this, Detection31Activity.class);
                        startActivityForResult(i, 31);
                    }
                }).show();
    }

    public void showDialog11(){
        new AlertDialog.Builder(this)
                .setTitle("触屏检测")
                .setMessage(R.string.detection_11_continue)
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(ArtificialDetectionActivity.this, Detection11Activity.class);
                        startActivityForResult(i, 11);
                    }
                }).show();
    }
}
