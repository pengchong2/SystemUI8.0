
package com.flyaudio.view;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.flyaudio.entities.FlyNavigator;

import cn.flyaudio.sdk.manager.FlyBluetoothManager;
import cn.flyaudio.sdk.manager.FlySystemManager;

public class BluetoothButton extends PowerButton {

    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private String FLY_BLUESATUS_ACTION = "com.android.action.bluestate";// 蓝牙和配对状态
    
    private String FLY_BLUETOOTH_SWITCH = "com.android.action.btSwitch"; //蓝牙开关状态
   
    private FlyNavigator mFlyNavigator;
    private boolean isBTOpen = false;
    public BluetoothButton() {
        mType = BUTTON_BLUETOOTH;
    }

    @Override
    protected void updateState(Context context) {

        if (isBTOpen) {
       //     setUI("systemui_bluetooth_on", "bluetooth_button", "power_button_text_color",true);
            setUI("flyaudio_systemui_bluetooth_d", "qs_bluetooth", "qs_button_text_color_pressed",false);
            mState = STATE_ENABLED;
        } else {
          //  setUI("systemui_bluetooth_off", "bluetooth_button",
          //         "power_button_text_color_d", false);
            setUI("flyaudio_systemui_bluetooth_u", "qs_bluetooth", "qs_holo_blue_white",false);
            mState = STATE_DISABLED;
        }
    }

    @Override
    protected void toggleState(Context context) {
        mFlyNavigator = FlyNavigator.getInstance(context);
        int commd = -1;
        if (getBluetoothDataState(context)) {
            Log.d("BluetoothButton", "FlyConstant.OFF---");
            // 蓝牙目前处于打开状态，关闭
            /*PhoneStatusBar.mFlyNavigator.new AdapterCenter().sendControlVolum(30);
            PhoneStatusBar.mFlyNavigator.new AdapterCenter().sendControlBT(0x00);*/
            //mFlyNavigator.new AdapterCenter().sendControlVolum(30);
           // mFlyNavigator.new AdapterCenter().sendControlBT(0x00);
            FlyBluetoothManager.getInstance().enableBTPower(false);
            isBTOpen = false;
        } else {
            Log.d("BluetoothButton", "FlyConstant.ON");
            // 蓝牙目前处于关闭状态，打开
           // mFlyNavigator.new AdapterCenter().sendControlBT(0x01);
            FlyBluetoothManager.getInstance().enableBTPower(true);
            isBTOpen = true;
        }
    }

    @Override
    protected void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (FLY_BLUETOOTH_SWITCH.equals(action)) {
            //Log.d("BluetoothButton", "接收到飞歌蓝牙广播");
            switch (intent.getIntExtra("btSwitch", 0)) {
                // 蓝牙关闭
                case 0:
                   // setUI("systemui_bluetooth_off", "bluetooth_button",
                   //         "power_button_text_color_d", false);
                    setUI("flyaudio_systemui_bluetooth_u", "qs_bluetooth", "qs_holo_blue_white",false);
                    mState = STATE_ENABLED;
                    isBTOpen = false;
                    break;
                // 蓝牙打开
                case 1:
                  //  setUI("systemui_bluetooth_on", "bluetooth_button", "power_button_text_color",
                  //          true);
                    setUI("flyaudio_systemui_bluetooth_d", "qs_bluetooth", "qs_button_text_color_pressed",true);
                    mState = STATE_ENABLED;
                    isBTOpen = true;
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    protected IntentFilter getBroadcastIntentFilter() {
        // 这个是系统的蓝牙
        // filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        // 飞歌蓝牙
        IntentFilter filter = new IntentFilter();
        filter.addAction(FLY_BLUETOOTH_SWITCH);
        return filter;
    }

    @Override
    protected boolean handleLongClick(Context context) {
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        FlySystemManager.getInstance().gotoPage(FlySystemManager.BT_PAGE);
        return true;
    }

    private boolean getBluetoothDataState(Context context) {
        // 初始化的时候蓝牙是不打开的
        return isBTOpen;
        // return adapter.isEnabled();
    }
}

