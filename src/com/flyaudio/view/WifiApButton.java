package com.flyaudio.view;

import com.android.systemui.R;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

public class WifiApButton extends PowerButton {

    public static final StateTracker sWifiApState = new WifiApStateTracker();

    /**
     * Subclass of StateTracker to get/set Wifi AP state.
     */
    private static final class WifiApStateTracker extends StateTracker {
        @Override
        public int getActualState(Context context) {
            WifiManager wifiManager = (WifiManager) context
            .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                return wifiApStateToFiveState(wifiManager.getWifiApState());
            }
            return STATE_UNKNOWN;
        }

        @Override
        protected void requestStateChange(Context context,
                final boolean desiredState) {

            final WifiManager wifiManager = (WifiManager) context
            .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) {
               
                return;
            }
           

            // Actually request the Wi-Fi AP change and persistent
            // settings write off the UI thread, as it can take a
            // user-noticeable amount of time, especially if there's
            // disk contention.
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... args) {
                    /*
                     * Disable Wif if enabling tethering
                     */
                    int wifiState = wifiManager.getWifiState();
                    if (desiredState
                            && ((wifiState == WifiManager.WIFI_STATE_ENABLING) || (wifiState == WifiManager.WIFI_STATE_ENABLED))) {
                        wifiManager.setWifiEnabled(false);
                    }

                    wifiManager.setWifiApEnabled(null, desiredState);
                    
                    return null;
                }
            }.execute();
        }

        @Override
        public void onActualStateChange(Context context, Intent intent) {

            if (!WifiManager.WIFI_AP_STATE_CHANGED_ACTION.equals(intent
                    .getAction())) {
                return;
            }
            int wifiState = intent
                .getIntExtra(WifiManager.EXTRA_WIFI_AP_STATE, -1);
            int widgetState=wifiApStateToFiveState(wifiState);
            setCurrentState(context, widgetState);
        }

        /**
         * Converts WifiManager's state values into our Wifi/WifiAP/Bluetooth-common
         * state values.
         */
        private static int wifiApStateToFiveState(int wifiState) {
            switch (wifiState) {
                case WifiManager.WIFI_AP_STATE_DISABLED:
                    return STATE_DISABLED;
                case WifiManager.WIFI_AP_STATE_ENABLED:
                    return STATE_ENABLED;
                case WifiManager.WIFI_AP_STATE_DISABLING:
                    return STATE_TURNING_OFF;
                case WifiManager.WIFI_AP_STATE_ENABLING:
                    return STATE_TURNING_ON;
                default:
                    return STATE_UNKNOWN;
            }
        }
    }

    public WifiApButton() { mType = BUTTON_WIFIAP; }

    @Override
    protected void updateState(Context context) {
        mState = sWifiApState.getTriState(context);

        switch (mState) {
            case STATE_DISABLED:
            	setUI("systemui_stat_wifi_ap_off","ap_button","power_button_text_color_d",false);
               
                break;
            case STATE_ENABLED:
            	setUI("systemui_stat_wifi_ap_on","ap_button","power_button_text_color",true);
                break;
            case STATE_INTERMEDIATE:
                // In the transitional state, the bottom green bar
                // shows the tri-state (on, off, transitioning), but
                // the top dark-gray-or-bright-white logo shows the
                // user's intent. This is much easier to see in
                // sunlight.
                if (sWifiApState.isTurningOn()) {
                	setUI("systemui_stat_wifi_ap_on","ap_button","power_button_text_color",true);
                } else {
                	setUI("systemui_stat_wifi_ap_off","ap_button","power_button_text_color_d",false);
                }
                break;
        }
    }

    @Override
    protected void toggleState(Context context) {
        sWifiApState.toggleState(context);
    }

    @Override
    protected boolean handleLongClick(Context context) {
        // it may be better to make an Intent action for the WifiAp settings
        // we may want to look at that option later
        context.sendBroadcast(new Intent().setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.android.settings", "com.android.settings.TetherSettings");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sWifiApState.onActualStateChange(context, intent);
    }

    @Override
    protected IntentFilter getBroadcastIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_AP_STATE_CHANGED_ACTION);
        return filter;
    }
}
