package com.tandy.detector.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tandy.detector.adapter.DetectInfoAdapter;
import com.tandy.detector.entity.BaseEntity;
import com.tandy.detector.entity.DetectInfoEntity;
import com.tandy.detector.helper.DialogHelper;
import com.tandy.detector.helper.PhoneInfoHelper;
import com.tandy.detector.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{

    private TextView mTxvStartDetect;
    private ListView mLsvDetectInfo;
    List<DetectInfoEntity> mEntityList;
    DetectInfoAdapter mAdapter;
    List<BaseEntity> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("flag", 0);
        if (!sp.getBoolean("tip", false)){
            showTip();
        }

        initUI();
    }

    /**
     * 初始化UI
     */
    public void initUI(){
        mTxvStartDetect = (TextView)findViewById(R.id.txv_start_detect);
        mTxvStartDetect.setOnClickListener(this);
        mLsvDetectInfo = (ListView)findViewById(R.id.lsv_detect_info);
        mEntityList = getDetectInfo();
        mAdapter = new DetectInfoAdapter(this, mEntityList);
        mLsvDetectInfo.setEnabled(false);
        mLsvDetectInfo.setAdapter(mAdapter);
        mLsvDetectInfo.setOnItemClickListener(mOnItemClick);
    }

    /**
     * ListView item点击事件
     */
    AdapterView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 5){
                DialogHelper.showInfoDialog(MainActivity.this, PhoneInfoHelper.getSensorList(getBaseContext()));
            }
            if (position == 6){
                DialogHelper.showInfoDialog(MainActivity.this, PhoneInfoHelper.getScreenInfo(MainActivity.this));
            }
            if (position == 7){
                DialogHelper.showInfoDialog(MainActivity.this, mList);
            }
//            if (position == 8){
//                DialogHelper.showInfoDialog(MainActivity.this, PhoneInfoHelper.getMoreInfo(getBaseContext()));
//            }
        }
    };

    /**
     * 获取检测信息列表，手动添加
     * @return
     */
    public List<DetectInfoEntity> getDetectInfo(){
        List<DetectInfoEntity> list = new ArrayList<>();

        list.add(setDetectInfo("型号", Build.BRAND + Build.MODEL));
        list.add(setDetectInfo("版本", Build.VERSION.RELEASE));
        list.add(setDetectInfo("内存", PhoneInfoHelper.getAvailMemory(this) + "/" + PhoneInfoHelper.getTotalMemory(this)));
        list.add(setDetectInfo("WIFI", enabled(PhoneInfoHelper.isWifiConnected(getBaseContext()))));
        list.add(setDetectInfo("蓝牙", enabled(BluetoothAdapter.getDefaultAdapter().isEnabled())));
        list.add(setDetectInfo("传感器", PhoneInfoHelper.getSensorList(this).size() + "个>>"));
        list.add(setDetectInfo("屏幕", ">>"));
        list.add(setDetectInfo("电池", ">>"));

        return list;
    }

    /**
     * 广播，用来获取电池信息
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            mList = new ArrayList<>();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)){
                int status = intent.getIntExtra("status", 0);
                int health = intent.getIntExtra("health", 0);
                int plugged = intent.getIntExtra("plugged", 0);

                String statusString = "";

                switch (status) {
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        statusString = "未知";
                        break;
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        statusString = "正在充电";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        statusString = "未充电";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        statusString = "未充电";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        statusString = "已充满";
                        break;
                }

                String healthString = "";

                switch (health) {
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        healthString = "未知";
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        healthString = "正常";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        healthString = "过热";
                        break;
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        healthString = "坏死";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        healthString = "电压";
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                        healthString = "未指定的错误";
                        break;
                }

                String acString;

                switch (plugged) {
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        acString = "电流连接";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        acString = "usb连接";
                        break;
                    default:
                        acString = "无";
                        break;
                }
                mList.add(PhoneInfoHelper.getListItem("电量\n" + intent.getIntExtra("level", 100) + "%"));
                mList.add(PhoneInfoHelper.getListItem("状态\n" + statusString));
                mList.add(PhoneInfoHelper.getListItem("运行状况\n" + healthString));
                mList.add(PhoneInfoHelper.getListItem("连接\n" + acString));
                mList.add(PhoneInfoHelper.getListItem("温度\n" + intent.getIntExtra("temperature", 0) * 0.1 + "℃"));
                mList.add(PhoneInfoHelper.getListItem("电压\n" + intent.getIntExtra("voltage", 0) + "mV"));
                mList.add(PhoneInfoHelper.getListItem("电池类型\n" + intent.getStringExtra("technology")));

            }
        }
    };

    public void onClick(View view){
        switch (view.getId()){
            case R.id.txv_start_detect:
                if (mTxvStartDetect.getText().equals("开始检测")){
                    SharedPreferences.Editor editor = getSharedPreferences("flag", 0).edit();
                    editor.putBoolean("flag", true);
                    editor.commit();
                    SharedPreferences.Editor editor1 = getSharedPreferences("tip", 0).edit();
                    editor1.putBoolean("tip", true);
                    editor1.commit();
                    mLsvDetectInfo.setEnabled(true);
                    mAdapter.notifyDataSetChanged();
                    mTxvStartDetect.setText("点击继续");
                }else if (mTxvStartDetect.getText().equals("点击继续")){
                    Intent intent = new Intent(MainActivity.this, ArtificialDetectionActivity.class);
                    startActivity(intent);
                    mTxvStartDetect.setText("开始检测");
                }
                break;
            default:
                break;
        }
    }

    public String enabled(boolean b){
        if (b){
            return "可用";
        }else {
            return "不可用";
        }
    }

    /**
     * 设置检测信息
     * @param name 检测信息名
     * @param value 检测信息值
     * @return
     */
    public DetectInfoEntity setDetectInfo(String name, String value){
        DetectInfoEntity entity = new DetectInfoEntity();
        entity.setName(name);
        entity.setValue(value);
        if (value.equals("不可用")) {
            entity.setEnabled("不可");
        }else {
            entity.setEnabled("可用");
        }
        return entity;
    }

    @Override
    protected void onResume(){
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }


    public void showTip(){
        new AlertDialog.Builder(this)
                .setTitle("提示！")
                .setMessage("请确保蓝牙、WiFi为开启状态！")
                .show();
    }

}
