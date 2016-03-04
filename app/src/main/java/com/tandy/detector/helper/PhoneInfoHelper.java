package com.tandy.detector.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

import com.tandy.detector.entity.BaseEntity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2/29 0029.
 * 手机信息帮助类
 */
public class PhoneInfoHelper {
    /**
     * 获取android当前可用内存大小
     */
    public static String getAvailMemory(Activity activity) {

        ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        String mem = Formatter.formatFileSize(activity.getBaseContext(), mi.availMem);
        mem = mem.substring(0, mem.length()-1);
        return mem;// 将获取的内存大小规格化
    }

    /**
     * 获得系统总内存
     */
    public static String getTotalMemory(Activity activity) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String mem = Formatter.formatFileSize(activity.getBaseContext(), initial_memory);
        mem = mem.substring(0, mem.length()-1);
        return mem;// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 获取传感器信息列表
     * @param context
     * @return
     */
    public static List<BaseEntity> getSensorList(Context context){
        List<BaseEntity> list = new ArrayList<>();
        SensorManager sm = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);
        for (Sensor s : sensors){
            String tempString = "\n" + "  设备名称：" + s.getName() + "\n" + "  设备版本：" + s.getVersion() + "\n" + "  供应商："
                    + s.getVendor() + "\n";
            BaseEntity entity = new BaseEntity();
            switch (s.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    entity.setContent(s.getType() + " 加速传感器accelerometer" + tempString);
                    list.add(entity);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    entity.setContent(s.getType() + " 陀螺仪传感器gyroscope" + tempString);
                    list.add(entity);
                    break;
                case Sensor.TYPE_LIGHT:
                    entity.setContent(s.getType() + " 环境光线传感器light" + tempString);
                    list.add(entity);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    entity.setContent(s.getType() + " 电磁场传感器magnetic field" + tempString);
                    list.add(entity);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    entity.setContent(s.getType() + " 方向传感器orientation" + tempString);
                    list.add(entity);
                    break;
                case Sensor.TYPE_PRESSURE:
                    entity.setContent(s.getType() + " 压力传感器pressure" + tempString);
                    list.add(entity);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    entity.setContent(s.getType() + " 距离传感器proximity" + tempString);
                    list.add(entity);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    entity.setContent(s.getType() + " 温度传感器temperature" + tempString);
                    list.add(entity);
                    break;
                default:
                    break;
            }
        }
        return list;
    }

    /**
     * 获取屏幕信息
     */
    public static List<BaseEntity> getScreenInfo(Activity activity){
        List<BaseEntity> list = new ArrayList<>();
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        list.add(getListItem("屏幕分辨率\n" + width + "×" + height));
        list.add(getListItem("屏幕密度\n" + metrics.density));
        return list;
    }

    /**
     * 获取手机更多信息（系统、基带、IMEI、序列号）
     */
    public static List<BaseEntity> getMoreInfo(Context context){
        List<BaseEntity> list = new ArrayList<>();
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        list.add(getListItem("IMEI\n" + mTm.getDeviceId()));
        list.add(getListItem("序列号\n" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)));
        return list;
    }

    /**
     * 列表添加item简化方法
     * @param str 添加的值
     * @return
     */
    public static BaseEntity getListItem(String str){
        BaseEntity entity = new BaseEntity();
        entity.setContent(str);
        return entity;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
