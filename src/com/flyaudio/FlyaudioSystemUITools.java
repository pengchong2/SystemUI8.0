package com.flyaudio;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.os.SystemProperties;
import android.widget.SeekBar;
import android.widget.Switch;
import com.android.systemui.R;
import com.flyaudio.entities.FlyNavigator;

import java.util.List;
import cn.flyaudio.sdk.manager.FlySystemManager;

class FlyaudioSystemUITools {

    private final static String TAG = "FlyaudioSystemUI";
    protected static Context mContext;
    public MyHandler myHandler;
    public static final int MG_SEEKBAR_VOLUME=5;
    public static final int MG_SEEKBAR_BRIGHTNESS=6;
    public static boolean PROVISIONED = true;
    private SeekBar seekBar_brightness;
    private static boolean VolumeFromUser;
    private static int currprogress = 0;
    public int currVolumeStatus = 5;
    public int maxVolumeStatus = 30;
    private Switch auto_brightness;
    private static int currScreenBrightness = 0;
    private static boolean brightnessFromUser;
    private static int maxbrightness;


    public FlyaudioSystemUITools() {

    }

    public void setContext(Context context) {
        mContext = context;
        myHandler = new MyHandler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"FlyNavigator.getInstance");
                FlyNavigator.getInstance(mContext.getApplicationContext());
            }
        },10000);
        /*下拉显示隐藏状态*/
        SystemProperties.set("fly.persist.statusbar.show", "0");
        /*false 处于一键安装*/
        PROVISIONED=true;
        /*是否显示同意页*/
        if (PROVISIONED) {
            String processName = getProcessName(mContext);
            Log.d(TAG,"processName = "+processName);
            if (processName != null) {
                if ("0".equals(SystemProperties.get("agreepage.state.displaying", "-1"))) {

                }else{
                    if (processName.equals("com.android.systemui")) {
                        Log.d(TAG,"start agreepage");
						Intent intent3 = new Intent();
                        intent3.setComponent(new ComponentName("cn.flyaudio.agreepage.android","cn.flyaudio.agreepage.android.BasService"));
                        mContext.startService(intent3);
                    }
                }
            }
        }
    }

    protected void startVoice() {

        if (mContext == null){
            return;
        }
        Intent DuerOSIntent = new Intent("flyaudio.intent.action.CONTROL_VOICE");
        DuerOSIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DuerOSIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        DuerOSIntent.putExtra("ENABLE_VOICE", "open_voice");
        mContext.sendBroadcast(DuerOSIntent);
    }


    protected void setFlyaudioQsContainer() {

        Log.d(TAG,"setFlyaudioQsContainer");


        View mQslayout = LayoutInflater.from(mContext).inflate(R.layout.qs_panel,null);
        /*初始化音量进度条*/


        seekBar_brightness = (SeekBar)mQslayout.findViewById(R.id.id_seekBar_brightness);
        auto_brightness = (Switch) mQslayout.findViewById(R.id.qs_auto_brightness);

        auto_brightness.setOnLongClickListener(null);

        auto_brightness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG,"autobrightness oncheckedChanged isChecked = "+isChecked);
                if (isChecked) {
                    FlySystemManager.getInstance().setScreenBrightnessAutoEnabled(FlySystemManager.SCREEN_BRIGHTNESS_AUTO_ON);

                } else {
                    FlySystemManager.getInstance().setScreenBrightnessAutoEnabled(FlySystemManager.SCREEN_BRIGHTNESS_AUTO_OFF);

                }
            }
        });




        seekBar_brightness
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        Log.d(TAG,"onProgressChanged");
                        if (fromUser) {
                            brightnessFromUser = true;
                            Log.d(TAG,"brightnessseekbar onProgressChanged progress = "+progress);
                            if (progress == 0) {
                               FlySystemManager.getInstance().setScreenBrightness(1);
                            } else {
                               FlySystemManager.getInstance().setScreenBrightness(progress);
                            }
                        } else {
                            brightnessFromUser = false;
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                            Log.d(TAG,"onStartTrackingTouch");
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        myHandler.removeMessages(MG_SEEKBAR_BRIGHTNESS);
                        myHandler.sendEmptyMessageDelayed(MG_SEEKBAR_BRIGHTNESS, 500);
                    }
                });


    }


    public void setQsText(){

    }


    private void setSeekBarClickable(SeekBar mSeekBar, int i) {
        Log.d(TAG,"setSeekBarClickable i = "+i);
        if (seekBar_brightness == null) return;
        if (i == 1) {
            mSeekBar.setClickable(true);
            mSeekBar.setEnabled(true);
            mSeekBar.setSelected(true);
            mSeekBar.setFocusable(true);


        } else {
          //禁用状态
            mSeekBar.setClickable(false);
            mSeekBar.setEnabled(false);
            mSeekBar.setSelected(false);
            mSeekBar.setFocusable(false);


        }
    }

    public void destroy() {

    }

    /*更新快捷开关状态*/
    public static class MyHandler extends android.os.Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MG_SEEKBAR_VOLUME:
                    VolumeFromUser = false;
                    break;
                case MG_SEEKBAR_BRIGHTNESS:
                    brightnessFromUser = false;
                    break;

                default:
                    break;
            }
        }
    }




    /*获取当前进程名称*/
    String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    public void onVolumeStatus(int i, int i2) {

    }


    void onMediaVolumeStatus(int i) {

    }

    void onPhoneVolumeStatus(int i) {

    }

    void onScreenLightLevelStatus(int i) {

    }

    void onScreenBrightness(int i) {
        Log.d(TAG,"onScreenBrightness i = "+i+" seekBar_brightness = "+seekBar_brightness);
        if (seekBar_brightness == null){
            return;
        }
        if (maxbrightness != FlySystemManager.getInstance().getMaxScreenBrightness()) {
            maxbrightness = FlySystemManager.getInstance().getMaxScreenBrightness();
            seekBar_brightness.setMax(FlySystemManager.getInstance().getMaxScreenBrightness() - FlySystemManager.getInstance().getMinScreenBrightness());
            seekBar_brightness.setProgress(i);
        }

        if (currScreenBrightness != i) {
            if (!brightnessFromUser) {
                seekBar_brightness.setProgress(i);
            }
        }
        currScreenBrightness = i;
    }

    void onCarRadar(int i, int i1, int i2, int i3, int i4) {

    }

    void onDefaultNaviChanged() {

    }

    void onVolumeChannel(int i) {

    }

    void onDayNightMode(int i) {

    }

    void onScreenBrightnessAutoStatus(int i) {
        if (seekBar_brightness == null) return;
        if (auto_brightness != null) {
            switch (i) {
                case FlySystemManager.RADAR_INIT:
                    break;
                case FlySystemManager.SCREEN_BRIGHTNESS_AUTO_ON:
                    setSeekBarClickable(seekBar_brightness, 0);
                    //brightness_switch.setChecked(true);

                    break;
                case FlySystemManager.SCREEN_BRIGHTNESS_AUTO_OFF:
                    setSeekBarClickable(seekBar_brightness, 1);
                    //brightness_switch.setChecked(false);

                    break;
            }
        }
    }
}
