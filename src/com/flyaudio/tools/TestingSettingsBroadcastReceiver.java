package com.flyaudio.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.telephony.SubscriptionManager;

import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.PhoneConstants;

/**
 *
 * Created by yuqin on 2018/3/29.
 */

public class TestingSettingsBroadcastReceiver extends BroadcastReceiver {
    private final static String ACTION_SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED";
    private final static int SIM_VALID = 0;
    private final static int SIM_INVALID = 1;
    private int simState = SIM_INVALID;
    private static String SimInfo = "";
    private static int SIM_STATE = -1;
    private static boolean isRemove = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SECRET_CODE")) {
            SystemProperties.set("fly.systemui.debug", "1");
        }else if (intent.getAction().equals(ACTION_SIM_STATE_CHANGED)) {
           setSimInfoByChange(context,intent);
        }
    }

    public int getSimState() {
        return simState;
    }

    private void setSimInfoByChange(Context context,Intent intent){
        //subid 卡ID
        int subId = intent.getIntExtra(PhoneConstants.SUBSCRIPTION_KEY, SubscriptionManager.INVALID_SUBSCRIPTION_ID);
        //soltId 卡槽ID 0：卡槽一  1：卡槽二
        int soltId = intent.getIntExtra("slot",-1);

        Bundle mBundle = intent.getExtras();//从中可以看到intent的发送过来的数据

        String stateExtra = intent.getStringExtra(IccCardConstants.INTENT_KEY_ICC_STATE);
        if (stateExtra!=null) {
            if (stateExtra.equals("ABSENT")) { //卡拔出状态
                SIM_STATE = 1;
                SimInfo = "No SIM Cards";
                if (soltId == 0) {
                    isRemove = true;
                }
            } else if (stateExtra.equals("READY") ||  //卡正常状态  即可以读出卡信息
                    stateExtra.equals("IMSI") ||
                    stateExtra.equals("LOADED") ){
                SIM_STATE = 0;
                SimInfo = "ok";
                if (soltId == 0) {
                    if (isRemove) {
                        context.sendBroadcast(new Intent("cn.flyaudio.mobiledata.open"));
                    }
                    isRemove = false;
                }
            }else if(stateExtra.equals("LOCKED") || //卡被锁状态
                    stateExtra.equals("NOT_READY") ||
                    stateExtra.equals("PIN")||
                    stateExtra.equals("PUK")){
                SIM_STATE = 2;
                SimInfo = "No Services";
            }
        }
    }

}
