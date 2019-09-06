package com.flyaudio.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.flyaudio.entities.FlyNavigator;


public class SoundEffectButton extends PowerButton {

    private FlyNavigator mFlyNavigator = null;
    public SoundEffectButton() { mType = BUTTON_SOUNDEFFECT;

    }

    @Override
    protected void updateState(Context context) {
    	//setUI("systemui_settings_off","settings_button","power_button_text_color_d",false);
        Log.d("sound","updateState");
        setUI("flyaudio_systemui_audio_u","qs_audio","qs_holo_blue_white",false);
        mState = STATE_DISABLED;
    }  
    @Override
    protected void setupButton(View view) {
        // TODO Auto-generated method stub
        view.setId(22);
        super.setupButton(view);
    }
    
    
    @Override
    protected void toggleState(Context context) {

        context.sendBroadcast(new Intent().setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        int pageID = 257;
        byte[] seekbar_increase = { 0x10, (byte) 0x82,(byte) ((pageID & 0xff00) >> 8),(byte) (pageID & 0xff)};
        int id = 0xFC0001;
        Log.d("sound","togglestate");
        FlyNavigator.getInstance(context).setZControl(id, (byte) 0xFE,seekbar_increase);



    }

    @Override
    protected boolean handleLongClick(Context context) {
       // context.sendBroadcast(new Intent().setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
      //  context.startActivity(new Intent(Settings.ACTION_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        return true;
    }
}
