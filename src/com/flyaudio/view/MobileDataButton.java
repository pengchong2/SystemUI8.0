
package com.flyaudio.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MobileDataButton extends PowerButton {

	public static final String EXTRA_NETWORK_MODE = "networkMode";
	private final String MOBILEACTION = "com.android.flyaudio.powerwidget.mobiledatabutton";
	private boolean mMobileDataEnabled = false;
	private boolean isAirPlane = false;
	private boolean isFirst = true;
	private Context mContext;
	private final int MSG_MOBILE = 1;
	private ConnectivityManager cm;
    private final TelephonyManager mTelephonyManager;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_MOBILE:
				boolean isMobileOpen = (Boolean) msg.obj;
				Log.d("MobileContentObserver", " mHandler isMobileOpen : "
						+ isMobileOpen + " mHandler mMobileDataEnabled : "
						+ mMobileDataEnabled);
				mContext.sendBroadcast(new Intent(MOBILEACTION));
				break;

			default:
				break;
			}
		};
	};
	private boolean isEnabelFormEcar=true;
	private Timer timer = new Timer();
	public MobileDataButton() {
		mType = BUTTON_MOBILEDATA;
		mContext = PowerWidget.staticContext;
		cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
                mTelephonyManager = TelephonyManager.from(mContext);

		mMobileDataEnabled = getDataState(mContext);
		Log.d("MobileContentObserver",
				" MobileDataButton() mMobileDataEnabled : "
						+ mMobileDataEnabled);
		// 开启线程读取状态
		if (isFirst) {
			Log.d("MobileContentObserver", "thread is isFirst start");
			Thread thread = new Thread(new MobileStateRunnable());
			thread.start();
			isFirst =false;
		}
	}

	@Override
	protected IntentFilter getBroadcastIntentFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(MOBILEACTION);
                //filter.addAction("android.intent.action.ANY_DATA_STATE"); //lyl+
                filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                filter.addAction("com.android.flyaudio.enableMobileData");
		        filter.addAction("com.android.flyaudio.openMobileData");
		filter.addAction("cn.flyaudio.mobiledata.open");
		return filter;
	}

	@Override
	protected void onReceive(final Context context, Intent intent) {
		super.onReceive(context, intent);

		if (intent.getAction().equals("com.android.flyaudio.enableMobileData")) {
			Log.d("flyecar", "systemui onReceive: enabelMobileData?");
			String enableMobileData = intent.getStringExtra("enableMobileData");
			if (enableMobileData.equals("disenable")){
				isEnabelFormEcar = false;
			}else {
				isEnabelFormEcar=true;
			}
		}

		if (intent.getAction().equals("com.android.flyaudio.openMobileData")) {
			if (!getDataState(context)) {
				toggleState(context);
			}

		}

		if (intent.getAction().equals("cn.flyaudio.mobiledata.open")){
			final int type = intent.getIntExtra("type", 1);
			Settings.Global.putInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON,
					0);

			if (timer==null){
				timer = new Timer();
			}
			timer.schedule(new TimerTask() {
				@Override
				public void run() {

					if (mTelephonyManager != null) {

						final WifiManager wifiManager = (WifiManager) context
								.getSystemService(Context.WIFI_SERVICE);

						try {
							if (type == 1) {
								mTelephonyManager.setDataEnabled(true);
							} else if (type == 0) {
								mTelephonyManager.setDataEnabled(false);
							} else if (type == 2) {
//								boolean turningOn = WifiApButton.sWifiApState.isTurningOn();
//								if (turningOn) {
//									return;
//								}
								if (wifiManager == null) {

									return;
								}
								new AsyncTask<Void, Void, Void>() {
									@Override
									protected Void doInBackground(Void... args) {
										/*
										 * Disable Wif if enabling tethering
										 */
										int wifiState = wifiManager.getWifiState();
										if (true
												&& ((wifiState == WifiManager.WIFI_STATE_ENABLING) || (wifiState == WifiManager.WIFI_STATE_ENABLED))) {
											wifiManager.setWifiEnabled(false);
										}

										return null;
									}
								}.execute();
								if (timer==null){
									timer = new Timer();
								}
								timer.schedule(new TimerTask() {
									@Override
									public void run() {
										wifiManager.setWifiApEnabled(null, true);
									}
								},3000);


							} else if (type == 3) {

								new AsyncTask<Void, Void, Void>() {
									@Override
									protected Void doInBackground(Void... args) {

										wifiManager.setWifiApEnabled(null, false);

										return null;
									}
								}.execute();


							}
						} catch (Exception e) {
						}
					}
				}
			},5000);

		}

	}

	@Override
	protected void updateState(Context context) {
	if (!isAirPlaneOn(context)) {
		Log.d("MobileContentObserver", "updateUI systemui_stat_data_off");
       // setUI("systemui_stat_data_off", "mobiledata_button", "power_button_text_color_d",
       //         false);
		setUI("flyaudio_systemui_database_u","qs_database","qs_holo_blue_white",false);
        mState = STATE_DISABLED;
        return;
	}
	if (mMobileDataEnabled) {
            Log.d("MobileContentObserver", "updateUI systemui_stat_data_on");
          //  setUI("systemui_stat_data_on", "mobiledata_button", "power_button_text_color",
           //         true);
		setUI("flyaudio_systemui_database_d","qs_database","qs_button_text_color_pressed",false);
            mState = STATE_ENABLED;
        } else {
            Log.d("MobileContentObserver", "updateUI systemui_stat_data_off");
          //  setUI("systemui_stat_data_off", "mobiledata_button", "power_button_text_color_d",
         //           false);
		setUI("flyaudio_systemui_database_u","qs_database","qs_holo_blue_white",false);
            mState = STATE_DISABLED;
        }
	}

	@Override
	protected void toggleState(Context context) {
		isAirPlane = Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) == 0;
		if (!isAirPlane) {
			return;
		}
		if (!isEnabelFormEcar){
			return;
		}
		boolean enabled = getDataState(context);
		
		if (enabled) {
                        mTelephonyManager.setDataEnabled(false);
			//cm.setMobileDataEnabled(false);
		} else {
                         mTelephonyManager.setDataEnabled(true);
			//cm.setMobileDataEnabled(true);
		}
	}
	
	private boolean isAirPlaneOn(Context context){
		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) == 0;
	}

	@Override
	protected boolean handleLongClick(Context context) {
		context.sendBroadcast(new Intent()
				.setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
		Intent intent = new Intent();
		intent.setClassName(
				"com.android.phone",
				"com.android.phone.MobileNetworkSettings");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		return true;
	}

	private boolean getDataState(Context context) {
        boolean state=false;
        try {
		     state=cm.getMobileDataEnabled();
		} catch (Exception e) {
                     e.printStackTrace(); 
		}
		return state;
	}

	private class MobileStateRunnable implements Runnable {

		boolean mobileState;

		MobileStateRunnable(){
			Log.d("MobileContentObserver", "MobileStateRunnable() 多次创建！！！");
		}

		@Override
		public void run() {
			Log.d("MobileContentObserver", "MobileStateRunnable running");
			while (true) {
				try {
					Thread.sleep(1000);
					mobileState = cm.getMobileDataEnabled();
					if (mobileState != mMobileDataEnabled) {
						Log.d("MobileContentObserver", "mobileState != mMobileDataEnabled mobileState : " + mobileState);
						mMobileDataEnabled = mobileState;
						mHandler.obtainMessage(MSG_MOBILE, mMobileDataEnabled)
								.sendToTarget();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					Log.d("MobileContentObserver", "InterruptedException : " + e.getMessage());
				}
			}
		}
	}

}
