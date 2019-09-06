/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Message;
import android.support.annotation.VisibleForTesting;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.settingslib.Utils;
import com.android.systemui.BatteryMeterView;
import com.android.systemui.Dependency;
import com.android.systemui.R;
import com.android.systemui.R.id;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.qs.QSDetail.Callback;
import com.android.systemui.statusbar.SignalClusterView;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.DarkIconDispatcher.DarkReceiver;
import com.flyaudio.FlyaudioSystemUI;
import com.flyaudio.view.customerview.VerticalSeekBar;

import cn.flyaudio.sdk.FlySDKManager;
import cn.flyaudio.sdk.InitListener;
import cn.flyaudio.sdk.listener.SystemListener;
import cn.flyaudio.sdk.manager.FlySystemManager;


public class QuickStatusBarHeader extends RelativeLayout {

    private ActivityStarter mActivityStarter;

    private QSPanel mQsPanel;

    private boolean mExpanded;
    private boolean mListening;

    protected QuickQSPanel mHeaderQsPanel;
    protected QSTileHost mHost;
    private SeekBar seekBar_brightness;
    private Switch auto_brightness;
    private TextView volume_title;
    private SeekBar seekBar_volume;
    private int currVolumeStatus = 5,maxVolumeStatus = 30,currentBrightness = 80;

    private String TAG = "qspanel";
    private  boolean brightnessFromUser = false , VolumeFromUser = false;
    public MyHandler myHandler = new MyHandler();
    private TextView mHideNotification;
    public QuickStatusBarHeader(Context context, AttributeSet attrs) {
        super(context, attrs);

       Log.d(TAG,"QuickStatusBarHeader");


        FlySystemManager.getInstance().setSystemListener(new SystemListener() {
            @Override
            public void onVolumeStatus(int i, int i1, int i2) {
                Log.d(TAG,"onVolumeStatus i = "+i+"i1 = "+i1+" i2 = "+i2);
                currVolumeStatus = i2;
                maxVolumeStatus = i;
                if(seekBar_volume != null && !VolumeFromUser){
                    volume_title.setText(currVolumeStatus+"");
                    seekBar_volume.setProgress(currVolumeStatus);
                }
            }

            @Override
            public void onMediaVolumeStatus(int i) {
                Log.d(TAG,"onMediaVolumeStatus i = "+i);
            }

            @Override
            public void onPhoneVolumeStatus(int i) {
                Log.d(TAG,"onPhoneVolumeStatus i ="+i);
            }

            @Override
            public void onScreenLightLevelStatus(int i) {
                Log.d(TAG,"onScreenLightLevel Status");
            }

            @Override
            public void onScreenBrightness(int i) {
                Log.d(TAG,"onScreenBrightness i =  "+i);
                currentBrightness = i;
                if(seekBar_brightness != null && !brightnessFromUser){
                    seekBar_brightness.setProgress(currentBrightness);
                }

            }

            @Override
            public void onCarRadar(int i, int i1, int i2, int i3, int i4) {

            }

            @Override
            public void onDefaultNaviChanged() {

            }

            @Override
            public void onVolumeChannel(int i) {

            }

            @Override
            public void onDayNightMode(int i) {

            }

            @Override
            public void onCurrentPageChanged(int i) {
                Log.d(TAG,"onCurrentPageChanged  = "+i);


            }

            @Override
            public void onScreenBrightnessAutoStatus(int i) {


            }
        });
    }
 //   protected  final View mFlyaudioQsView;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Log.d("qspanel","onFinishInflate");



        mHeaderQsPanel = findViewById(R.id.quick_qs_panel);

        // RenderThread is doing more harm than good when touching the header (to expand quick
        // settings), so disable it for this view
            updateResources();

        // Set the light/dark theming on the header status UI to match the current theme.
        int colorForeground = Utils.getColorAttr(getContext(), android.R.attr.colorForeground);
        float intensity = colorForeground == Color.WHITE ? 0 : 1;
        Rect tintArea = new Rect(0, 0, 0, 0);

        applyDarkness(R.id.battery, tintArea, intensity, colorForeground);
        applyDarkness(R.id.clock, tintArea, intensity, colorForeground);

        BatteryMeterView battery = findViewById(R.id.battery);
        battery.setForceShowPercent(true);

        mActivityStarter = Dependency.get(ActivityStarter.class);


        seekBar_brightness = (SeekBar)findViewById(R.id.id_seekbar_brightness);
        Log.d(TAG,"maxbrightness = "+FlySystemManager.getInstance().getMaxScreenBrightness());
        Log.d(TAG,"minbrightness = "+FlySystemManager.getInstance().getMinScreenBrightness());
        seekBar_brightness.setMax(100);
        seekBar_brightness.setProgress(currentBrightness);

        auto_brightness = (Switch) findViewById(R.id.qs_auto_brightness);
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
                            auto_brightness.setChecked(false);
                            brightnessFromUser = true;
                            Log.d(TAG,"brightnessseekbar onProgressChanged progress = "+progress);
                            if (progress == 0) {
                                currentBrightness = 1;
                                FlySystemManager.getInstance().setScreenBrightness(1);
                            } else {
                                currentBrightness = progress;
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
                        Log.d(TAG,"onStopTrackingTouch");
                        myHandler.removeMessages(6);
                        myHandler.sendEmptyMessageDelayed(6, 500);
                    }
                });


        volume_title = (TextView)findViewById(R.id.volume_value_tv);
        seekBar_volume = (SeekBar)findViewById(R.id.id_seekbar_volume);
        Log.d(TAG,"ccurrVolumeStatusu = "+currVolumeStatus+" maxVolumeStatus = "+maxVolumeStatus);
        volume_title.setText(currVolumeStatus+"");
        seekBar_volume.setMax(maxVolumeStatus);
        seekBar_volume.setProgress(currVolumeStatus);

        seekBar_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG,"onStartTracking");
            }


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG,"onProgressChanged fromUser = "+fromUser);
                if (fromUser) {

                    VolumeFromUser = true;
                    currVolumeStatus = progress;
                    FlySystemManager.getInstance().setVolume(currVolumeStatus);
                    volume_title.setText(currVolumeStatus+"");
                }
            }

            // 结束拖动时触发
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG,"onStopTrackingTouch");
               // FlySystemManager.getInstance().setVolume(currVolumeStatus);
                myHandler.removeMessages(7);
                myHandler.sendEmptyMessageDelayed(7, 15000);
            }
        });

        mHideNotification = findViewById(R.id.hidenotification);
        mHideNotification.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"statusbar = "+StatusBar.instance);
                if(StatusBar.instance!=null){
                    StatusBar.instance.clearNotification();
                }

            }
        });
    }

    public class MyHandler extends android.os.Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 6:
                    brightnessFromUser = false;
                    break;
                case 7:
                    VolumeFromUser = false;
                    break;
                default:
                    break;
            }
        }
    }

    private void applyDarkness(int id, Rect tintArea, float intensity, int color) {
        View v = findViewById(id);
        if (v instanceof DarkReceiver) {
            ((DarkReceiver) v).onDarkChanged(tintArea, intensity, color);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateResources();
    }

    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        updateResources();
    }

    private void updateResources() {
    }

    public int getCollapsedHeight() {
        Log.d(TAG,"getCollapsedHeight getHeight ="+getHeight());
        return getHeight();
    }

    public int getExpandedHeight() {
        Log.d(TAG,"getExpandedHeight = "+getHeight());
        return getHeight();
    }

    public void setExpanded(boolean expanded) {
       // if (mExpanded == expanded) return;
        mExpanded = expanded;
        mHeaderQsPanel.setExpanded(expanded);
        updateEverything();
    }

    public void setExpansion(float headerExpansionFraction) {
    }

    @Override
    @VisibleForTesting
    public void onDetachedFromWindow() {
        setListening(false);
        super.onDetachedFromWindow();
    }

    public void setListening(boolean listening) {
        if (listening == mListening) {
            return;
        }
        mHeaderQsPanel.setListening(listening);
        mListening = listening;
    }

    public void updateEverything() {
        Log.d("qspanel","updateeEvery");
        post(()->setEnabled(true));
        post(() -> setClickable(true));
    }

    public void setQSPanel(final QSPanel qsPanel) {
        mQsPanel = qsPanel;
        setupHost(qsPanel.getHost());
    }

    public void setupHost(final QSTileHost host) {
        mHost = host;
        //host.setHeaderView(mExpandIndicator);
        mHeaderQsPanel.setQSPanelAndHeader(mQsPanel, this);
        mHeaderQsPanel.setHost(host, null /* No customization in header */);
    }

    public void setCallback(Callback qsPanelCallback) {
        mHeaderQsPanel.setCallback(qsPanelCallback);
    }
}
