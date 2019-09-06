package com.flyaudio.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import com.flyaudio.tools.ActivityUtils;

/**
 * 
 * @author weifule 20140725
 *
 */
public class AirplaneModeButton extends PowerButton {

	private static String TAG = "AirplaneModeButton";
	private Context mContext = null;
	public AirplaneModeButton(){
		mType = BUTTON_AIRPLANE;
	}
	/**
	 * 改变布局
	 */
	@Override
	protected void updateState(Context context) {
	Log.d(TAG, "	updateState getDataState(context):"+getDataState(context));
	mContext = context;
	if (getDataState(context)) {
           // setUI("systemui_airplane_mode_on","airplane_mode_button","power_button_text_color",true);
		setUI("flyaudio_systemui_airplane_d","qs_airplane","qs_button_text_color_pressed",false);
            mState = STATE_ENABLED;         
        } else {
        	//setUI("systemui_airplane_mode_off","airplane_mode_button","power_button_text_color_d",false);
		setUI("flyaudio_systemui_airplane_u","qs_airplane","qs_holo_blue_white",false);
            mState = STATE_DISABLED;
        }
	}
	
	/**
	 * 切换状态
	 */
	@Override
	protected void toggleState(Context context) {

		handler.removeMessages(1);
		Message msg = Message.obtain();
		msg.what=1;
		msg.obj = context;
		handler.sendMessageDelayed(msg,800);

	}


	 Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:

					Context obj = (Context) msg.obj;
					boolean mAirplaneModeState = getDataState(obj);
					if (mAirplaneModeState) {
						setAirplaneModeState(false,obj);
					} else {
						setAirplaneModeState(true,obj);
					}
					break;

					default:break;

			}

		}
	};

	
	/**
	 * 长按
	 */
	@Override
	protected boolean handleLongClick(Context context) {

		context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
		ActivityUtils.startActivity(context,Settings.ACTION_AIRPLANE_MODE_SETTINGS);
		return false;
	}
	
	/**
	 * 监听飞行模式的变化
	 */
	@Override
    	protected IntentFilter getBroadcastIntentFilter() {

        	IntentFilter filter = new IntentFilter();
        	filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        	return filter;
   	 }



	/**
	 * 得到目前飞行模式的状态
	 */
	private boolean getDataState(Context context){
		Log.d(TAG,"airplane mode = "+Settings.Global.getInt(context.getContentResolver(),
				Settings.Global.AIRPLANE_MODE_ON, 0));
		return Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;

	}

	/**
	 * 把广播发出去，改变飞行模式的状态
	 */
	private void setAirplaneModeState(boolean enabling,Context context) {
	 // Change the system setting
        Settings.Global.putInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON,
                                enabling ? 1 : 0);
        // Post the intent
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", enabling);
		context.sendBroadcast(intent);
    }



}

