package com.flyaudio.view;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.flyaudio.tools.SkinResource;

import java.util.ArrayList;
import java.util.List;

public abstract class PowerButton implements OnTouchListener {
    public static final String TAG = "SystemUI-PowerButton";

    public static final int STATE_ENABLED = 1;
    public static final int STATE_DISABLED = 2;
    public static final int STATE_TURNING_ON = 3;
    public static final int STATE_TURNING_OFF = 4;
    public static final int STATE_INTERMEDIATE = 5;
    public static final int STATE_UNKNOWN = 6;

    public static final String BUTTON_WIFI = "toggleWifi";
    public static final String BUTTON_GPS = "toggleGPS";
    public static final String BUTTON_BLUETOOTH = "toggleBluetooth";
    public static final String BUTTON_BRIGHTNESS = "toggleBrightness";
    public static final String BUTTON_SOUND = "toggleSound";
    public static final String BUTTON_SYNC = "toggleSync";
    public static final String BUTTON_WIFIAP = "toggleWifiAp";
    public static final String BUTTON_SCREENTIMEOUT = "toggleScreenTimeout";
    public static final String BUTTON_MOBILEDATA = "toggleMobileData";
    public static final String BUTTON_LOCKSCREEN = "toggleLockScreen";
    public static final String BUTTON_NETWORKMODE = "toggleNetworkMode";
    public static final String BUTTON_AUTOROTATE = "toggleAutoRotate";
    public static final String BUTTON_AIRPLANE = "toggleAirplane";
    public static final String BUTTON_FLASHLIGHT = "toggleFlashlight";
    public static final String BUTTON_SLEEP = "toggleSleepMode";
    public static final String BUTTON_MEDIA_PLAY_PAUSE = "toggleMediaPlayPause";
    public static final String BUTTON_MEDIA_PREVIOUS = "toggleMediaPrevious";
    public static final String BUTTON_MEDIA_NEXT = "toggleMediaNext";
    public static final String BUTTON_LTE = "toggleLte";
    public static final String BUTTON_WIMAX = "toggleWimax";
    public static final String BUTTON_UNKNOWN = "unknown";
    private static final String SEPARATOR = "OV=I=XseparatorX=I=VO";

    public static final String BUTTON_MEMORY = "toogme";
    public static final String BUTTON_OFFSCREEN = "toogOffscreen";
    public static final String BUTTON_RESET = "toogReset";
    public static final String BUTTON_SETTINGS = "toogSettings";
    private static final Mode MASK_MODE = Mode.SCREEN;
    public static final String BUTTON_LOCATION = "toggleLocation";
    public static final String BUTTON_CARSETTING = "togglecarsetting";

    // 20141022
    public static final String BUTTON_MOBILEDATA1 = "toggleMobileData1";
    public static final String BUTTON_AIRPLANE1 = "toggleAirplane1";
    public static final String BUTTON_OFFSCREEN1 = "toogOffscreen1";
    public static final String BUTTON_RESET1 = "toogReset1";

    public static final String BUTTON_WALLPICK = "togglewallpick";
    public static final String BUTTON_SYSTEM_UPDATE = "togglesystemupdate";

    public static final String BUTTON_SOUNDEFFECT = "toggleSoundeffect";
    public static final String BUTTON_MUTE = "toggleMuteeffect";


    protected static final int pg_color_on = 0xfffcf300;
    protected static final int pg_color_off = 0xffbababa;

    protected Drawable mIcon;
    protected int mTextColor;
    protected String mText;

    protected int mState;
    private boolean isChangeTextColor = false;
    protected View mView;
    protected String mType = BUTTON_UNKNOWN;

    /*private ImageView mIconView;*/
    private TextView mIconViewText;
    private LinearLayout mOutLayout;

    private View.OnClickListener mExternalClickListener;
    private View.OnLongClickListener mExternalLongClickListener;

    protected boolean mHapticFeedback;
    protected Vibrator mVibrator;
    private long[] mClickPattern;
    private long[] mLongClickPattern;

    private Context context;
    // we use this to ensure we update our views on the UI thread
    private Handler mViewUpdateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mOutLayout != null) {
                Log.d(TAG,"mOutLayout.setbackground");
                mOutLayout.setBackgroundDrawable(mIcon);
            }

            if (mText != null) {
                try {

                    Log.d(TAG,"mIconViewText.setText() mText = "+mText);
                  //  mIconViewText.setText(mText);
                  //  mIconViewText.setTextColor(mTextColor);
                } catch (Exception e) {

                }
            }
        }
    };


    protected abstract void updateState(Context context);

    protected abstract void toggleState(Context context);

    protected abstract boolean handleLongClick(Context context);

    protected void update(Context context) {
        this.context = context;
        updateState(context);
        updateView();
    }

    public String[] parseStoredValue(CharSequence val) {
        if (TextUtils.isEmpty(val)) {
            return null;
        } else {
            return val.toString().split(SEPARATOR);
        }
    }

    protected void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("action.flyaudio.colortheme")) {

            int colortheme = intent.getIntExtra("rgb", 0);
            if (colortheme == 0) {
                String colorFromPro = SystemProperties.get(
                        "persist.fly.colortheme", "-65536");
                if (!colorFromPro.equals("-65536")) {
                  //  mIconViewText.setTextColor(Integer
                  //          .parseInt(colorFromPro));
                } else {
                  //  mIconViewText.setTextColor(Color.RED);
                }
            } else {
                if (isChangeTextColor) {
                  //  mIconViewText.setTextColor(colortheme);

                } else {
                   // mIconViewText.setTextColor(mTextColor);
                }
            }

        }
    }

    protected void onChangeUri(ContentResolver resolver, Uri uri) {
        // do nothing as a standard, override this if the button needs to
        // respond
        // to a changed setting
    }

    /* package */void setHapticFeedback(boolean enabled, long[] clickPattern,
                                        long[] longClickPattern) {
        mHapticFeedback = enabled;
        mClickPattern = clickPattern;
        mLongClickPattern = longClickPattern;
    }

    protected IntentFilter getBroadcastIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("action.flyaudio.colortheme");
        return filter;
        // return new IntentFilter();
    }

    protected List<Uri> getObservedUris() {
        return new ArrayList<Uri>();
    }

    protected void setupButton(View view) {
        mView = view;
        if (mView != null) {
            mView.setTag(mType);
            mView.setOnClickListener(mClickListener);
            mView.setOnLongClickListener(mLongClickListener);

            mOutLayout = (LinearLayout) mView.findViewById(
                    SkinResource.getSkinResourceId2("power_widget_button_out", "id"));

           // mIconViewText = (TextView) mView.findViewById(
           //         SkinResource.getSkinResourceId2("power_widget_button_text", "id"));


            mVibrator = (Vibrator) mView.getContext().getSystemService(
                    Context.VIBRATOR_SERVICE);
            if (mView.getId() == 5
                    || mView.getId() == 6
                    || mView.getId() == 7
                    || mView.getId() == 11
                    || mView.getId() == 20
                    || mView.getId() == 21
                    || mView.getId() == 22)
                mView.setOnTouchListener(this);
        } /*else {
            mIconView = null;
        }*/
    }

    protected void updateView() {
        Log.d(TAG,"updateView");
        mViewUpdateHandler.sendEmptyMessage(0);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mHapticFeedback && mClickPattern != null) {
                if (mClickPattern.length == 1) {
                    // One-shot vibration
                    mVibrator.vibrate(mClickPattern[0]);
                } else {
                    // Pattern vibration
                    mVibrator.vibrate(mClickPattern, -1);
                }
            }
            toggleState(v.getContext());
            update(v.getContext());
            if (mExternalClickListener != null) {
                mExternalClickListener.onClick(v);
            }
        }
    };

    private View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {
            boolean result = handleLongClick(v.getContext());

            if (result && mHapticFeedback && mLongClickPattern != null) {
                mVibrator.vibrate(mLongClickPattern, -1);
            }

            if (result && mExternalLongClickListener != null) {
                mExternalLongClickListener.onLongClick(v);
            }
            return result;
        }
    };

    void setExternalClickListener(View.OnClickListener listener) {
        mExternalClickListener = listener;
    }

    void setExternalLongClickListener(View.OnLongClickListener listener) {
        mExternalLongClickListener = listener;
    }

    protected SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("PowerButton-" + mType,
                Context.MODE_PRIVATE);
    }

    protected void setUI(String icon, String text,
                         String colors, boolean textColor) {
        try {
            Log.d(TAG,"setUI text = "+text);
            isChangeTextColor = textColor;
            if (icon != null)
                mIcon = SkinResource.getSkinDrawableByName(icon);
        /*if (icontag != null)
            mIconTag = SkinResource.getSkinDrawableByName(icontag);*/
//            if (text != null)
//                mText = SkinResource.getSkinStringByName(text);
//            if (colors != null)
//                mTextColor = SkinResource.getSkinColorByName2(colors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onTouch(View view, MotionEvent event) {


        LinearLayout linearLayout = (LinearLayout) mView
                .findViewById(
                        SkinResource.getSkinResourceId2("power_widget_button_out", "id"));

      //  TextView text = (TextView) view
       //         .findViewById(
        //                SkinResource.getSkinResourceId2("power_widget_button_text", "id"));

        if (view.getId() == 5) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
               // text.setTextColor(
                //        SkinResource.getSkinColorByName("power_button_text_color"));
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    ||event.getAction() == MotionEvent.ACTION_CANCEL) {
              //  text.setTextColor(
               //         SkinResource.getSkinColorByName("power_button_text_color_d"));
            }
        } else if (view.getId() == 6) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                linearLayout.setBackgroundDrawable(
                            SkinResource.getSkinDrawableByName("systemui_reset_on"));
               // text.setTextColor(
                //        SkinResource.getSkinColorByName("power_button_text_color"));
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    ||event.getAction() == MotionEvent.ACTION_CANCEL) {
                linearLayout.setBackgroundDrawable(
                            SkinResource.getSkinDrawableByName("systemui_reset_off"));
              //  text.setTextColor(
               //         SkinResource.getSkinColorByName("power_button_text_color_d"));
            }

        } else if (view.getId() == 7) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
               linearLayout.setBackgroundDrawable(
                            SkinResource.getSkinDrawableByName("systemui_settings_on"));

                 //   text.setTextColor(SkinResource.getSkinColorByName(
                  //          "power_button_text_color"));

            } else if (event.getAction() == MotionEvent.ACTION_UP
                    ||event.getAction() == MotionEvent.ACTION_CANCEL) {
                linearLayout.setBackgroundDrawable(
                            SkinResource.getSkinDrawableByName("systemui_settings_off"));
                //text.setTextColor(SkinResource.getSkinColorByName(
                 //       "power_button_text_color_d"));
            }
        } else if (view.getId() == 10) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                linearLayout.setBackgroundDrawable(
                            SkinResource.getSkinDrawableByName("systemui_quick_on"));
               // text.setTextColor(
               //         SkinResource.getSkinColorByName("power_button_text_color"));

            } else if (event.getAction() == MotionEvent.ACTION_UP
                    ||event.getAction() == MotionEvent.ACTION_CANCEL) {
                linearLayout.setBackgroundDrawable(
                            SkinResource.getSkinDrawableByName("systemui_quick_off"));
               // text.setTextColor(
                 //       SkinResource.getSkinColorByName("power_button_text_color_d"));
            }
        } else if (view.getId() == 11) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    linearLayout.setBackgroundDrawable(
                            SkinResource.getSkinDrawableByName("systemui_carsetting_on"));

                 //   text.setTextColor(
                   //         SkinResource.getSkinColorByName("power_button_text_color"));

            } else if (event.getAction() == MotionEvent.ACTION_UP
                    ||event.getAction() == MotionEvent.ACTION_CANCEL) {

               linearLayout.setBackgroundDrawable(
                            SkinResource.getSkinDrawableByName("systemui_carsetting_off"));

              //  text.setTextColor(
               //         SkinResource.getSkinColorByName("power_button_text_color_d"));
            }
        } else if (view.getId() == 20) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                linearLayout
                        .setBackgroundDrawable(
                                SkinResource.getSkinDrawableByName("systemui_wallpick_on"));
              //  text.setTextColor(SkinResource.getSkinColorByName("power_button_text_color"));
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    ||event.getAction() == MotionEvent.ACTION_CANCEL) {
                linearLayout
                        .setBackgroundDrawable(SkinResource.getSkinDrawableByName("systemui_wallpick_off"));
               // text.setTextColor(SkinResource.getSkinColorByName("power_button_text_color_d"));
            }
        } else if (view.getId() == 21) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                linearLayout
                        .setBackgroundDrawable(SkinResource.getSkinDrawableByName("systemui_system_update_on"));
               // text.setTextColor(SkinResource.getSkinColorByName("power_button_text_color"));
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    ||event.getAction() == MotionEvent.ACTION_CANCEL) {
                linearLayout
                        .setBackgroundDrawable(SkinResource.getSkinDrawableByName("systemui_system_update_off"));
                //text.setTextColor(SkinResource.getSkinColorByName("power_button_text_color_d"));
            }
        }else if(view.getId() == 22){
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                linearLayout
                        .setBackgroundDrawable(SkinResource.getSkinDrawableByName("flyaudio_systemui_audio_d"));
                //text.setTextColor(SkinResource.getSkinColorByName("qs_button_text_color_pressed"));
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    ||event.getAction() == MotionEvent.ACTION_CANCEL) {
                linearLayout
                        .setBackgroundDrawable(SkinResource.getSkinDrawableByName("flyaudio_systemui_audio_u"));
                //text.setTextColor(SkinResource.getSkinColorByName("qs_holo_blue_white"));
            }
        }

        return false;
    }
}

