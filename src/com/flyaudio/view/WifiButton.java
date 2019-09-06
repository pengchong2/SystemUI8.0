package com.flyaudio.view;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;


public class WifiButton extends PowerButton {

    private static final StateTracker sWifiState = new WifiStateTracker();

    /**
     * Subclass of StateTracker to get/set Wifi state.
     */
    private static final class WifiStateTracker extends StateTracker {
        @Override
        public int getActualState(Context context) {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                return wifiStateToFiveState(wifiManager.getWifiState());
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

            // Actually request the wifi change and persistent
            // settings write off the UI thread, as it can take a
            // user-noticeable amount of time, especially if there's
            // disk contention.
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... args) {
                    /*
                     * Disable tethering if enabling Wifi
                     */
                    int wifiApState = wifiManager.getWifiApState();
                    if (desiredState
                            && ((wifiApState == WifiManager.WIFI_AP_STATE_ENABLING) || (wifiApState == WifiManager.WIFI_AP_STATE_ENABLED))) {
                        wifiManager.setWifiApEnabled(null, false);
                    }

                    wifiManager.setWifiEnabled(desiredState);
                    return null;
                }
            }.execute();
        }

        @Override
        public void onActualStateChange(Context context, Intent intent) {
            if (!WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent
                    .getAction())) {
                return;
            }
            int wifiState = intent
                    .getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
            int widgetState = wifiStateToFiveState(wifiState);
            setCurrentState(context, widgetState);
        }

        /**
         * Converts WifiManager's state values into our Wifi/Bluetooth-common
         * state values.
         */
        private static int wifiStateToFiveState(int wifiState) {
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    return STATE_DISABLED;
                case WifiManager.WIFI_STATE_ENABLED:
                    return STATE_ENABLED;
                case WifiManager.WIFI_STATE_DISABLING:
                    return STATE_TURNING_OFF;
                case WifiManager.WIFI_STATE_ENABLING:
                    return STATE_TURNING_ON;
                default:
                    return STATE_UNKNOWN;
            }
        }
    }

    public WifiButton() {
        mType = BUTTON_WIFI;
    }

    @Override
    protected void updateState(Context context) {

        mState = sWifiState.getTriState(context);
        Log.d("wifi","updateState mState = "+mState);
        switch (mState) {
            case STATE_DISABLED:
              //  setUI("systemui_stat_wifi_off", "wifi_button", "power_button_text_color_d", false);
                setUI("flyaudio_systemui_wifi_u", "qs_wifi", "qs_holo_blue_white", false);
                break;
            case STATE_ENABLED:
              //  setUI("systemui_stat_wifi_on", "wifi_button", "power_button_text_color", true);
                setUI("flyaudio_systemui_wifi_d", "qs_wifi", "qs_button_text_color_pressed", false);
                break;
            case STATE_INTERMEDIATE:
                // In the transitional state, the bottom green bar
                // shows the tri-state (on, off, transitioning), but
                // the top dark-gray-or-bright-white logo shows the
                // user's intent. This is much easier to see in
                // sunlight.
                if (sWifiState.isTurningOn()) {
                 //   setUI("systemui_stat_wifi_on", "wifi_button", "power_button_text_color", true);
                    setUI("flyaudio_systemui_wifi_d", "qs_wifi", "qs_button_text_color_pressed", false);
                } else {
                 //   setUI("systemui_stat_wifi_off", "wifi_button", "power_button_text_color_d", false);
                    setUI("flyaudio_systemui_wifi_u", "qs_wifi", "qs_holo_blue_white", false);
                }
                break;
                default:
                    setUI("flyaudio_systemui_wifi_u", "qs_wifi", "qs_holo_blue_white", false);
                    break;
        }
    }

    @Override
    protected void toggleState(Context context) {
    sWifiState.toggleState(context);
    }

    @Override
    protected boolean handleLongClick(Context context) {
        context.sendBroadcast(new Intent().setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        Intent intent = new Intent("android.settings.WIFI_SETTINGS");

        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sWifiState.onActualStateChange(context, intent);
    }

    @Override
    protected IntentFilter getBroadcastIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        return filter;
    }
}
