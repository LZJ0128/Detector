package com.tandy.detector.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tandy.detector.R;
import com.tandy.detector.adapter.SensorAdapter;
import com.tandy.detector.entity.BaseEntity;

import java.util.List;

/**
 * Created by Administrator on 3/1 0001.
 * 对话框帮助类
 */
public class DialogHelper {

    public static void showDialog(final Activity activity, int title, final int btn1, int btn2, final TextView txv){
        final Dialog dialog = new Dialog(activity, R.style.myDialogTheme);
        dialog.setContentView(R.layout.dialog_detection);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager manager = activity.getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER_VERTICAL;
        params.width = display.getWidth();
        window.setAttributes(params);

        Button btnFirst = (Button)dialog.findViewById(R.id.btn_screen_good);
        Button btnSecond = (Button)dialog.findViewById(R.id.btn_screen_bad);
        TextView txvTitle = (TextView)dialog.findViewById(R.id.txv_screen);
        btnFirst.setText(btn1);
        btnSecond.setText(btn2);
        txvTitle.setText(title);
        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txv.setTextColor(Color.BLUE);
                    }
                });
                dialog.dismiss();
            }
        });
        btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txv.setTextColor(Color.RED);
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showInfoDialog(final Activity activity, List<BaseEntity> baseList){
        final Dialog dialog = new Dialog(activity, R.style.myDialogTheme);
        dialog.setContentView(R.layout.dialog_sensor);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        WindowManager manager = activity.getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER_VERTICAL;
        window.setAttributes(params);

        ListView listView = (ListView)dialog.findViewById(R.id.lsv_item);
        listView.setAdapter(new SensorAdapter(activity, baseList));
        dialog.show();
    }

}
