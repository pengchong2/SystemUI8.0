package com.flyaudio;

import android.app.Dialog;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.systemui.recents.RecentsActivity;
import com.flyaudio.tools.FlyNavigator;
import com.android.systemui.R;
import com.flyaudio.view.customerview.VerticalSeekBar;

import java.io.IOException;
import cn.flyaudio.sdk.FlySDKManager;
import cn.flyaudio.sdk.InitListener;
import cn.flyaudio.sdk.listener.SystemListener;
import cn.flyaudio.sdk.manager.FlySystemManager;

public class FlyaudioSystemUI extends FlyaudioSystemUITools implements View.OnClickListener,View.OnLongClickListener
{

    private final static String TAG = "FlyaudioSystemUI";
    private static final boolean DEBUG = true;
    private static FlyaudioSystemUI instance = new FlyaudioSystemUI();
    private MyBroadcast myBroadcast;
    private String FLY_BLUETOOTH_SWITCH = "com.android.action.btSwitch";
    private Context mContext;
    private String activity_name = null;
    private String className = null;
    private Boolean isFlySDKManagerInit = false;
    private ImageButton mHomeImgBtn,mBackImgBtn,mNavigationImgBtn,mAppImgBtn,mVolumeImgBtn;
    private View mView;
    private ImageButton mleftseatImgBtn,mfrontacImgBtn,mrightseatImgBtn,mbackacImgBtn;
    private WindowManager mWindowManager = null;
    private   View view = null;
    public static synchronized FlyaudioSystemUI getInstance() {
        return instance;
    }
    private TextView volume_title;
    private VerticalSeekBar seekBar_volume;
    private static boolean VolumeFromUser = false;
    private static int currprogress = 0;
    private TextView mLeftTextView,mRightTextView;

    private FlyaudioSystemUI() {



    }
    /**
     * 开机起来每隔1秒注册flysdk，直到注册成功
     */
   Runnable FlySDKIniRunnable = new Runnable() {
       @Override
       public void run() {
           while(!isFlySDKManagerInit){
               FlySDKManager.getInstance().initialize(mContext, new InitListener() {
                   @Override
                   public void onError() {
                       Log.d(TAG," flysdkmanager onError");
                   }
                   @Override
                   public void onSucceed() {
                       Log.d(TAG," flysdkmanager onSucceed");
                       FlySystemManager.getInstance().registerCallBackListener();
                       //new systemui
                       startActivity("cn.flyaudio.launcher.carinfor","cn.flyaudio.launcher.carinfor.CarInforActivity");
                       isFlySDKManagerInit = true;
                   }
               });

               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       }
   };



    @Override
    public void setContext(Context context) {
        super.setContext(context);
        mContext = context;

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        new Thread(FlySDKIniRunnable).start();



        myBroadcast = new MyBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_LOCKED_BOOT_COMPLETED);
        intentFilter.addAction("cn.flyaudio.screen.navigationbar");
        intentFilter.addAction("cn.flyaudio.launcher.senddata");
        intentFilter.addAction("flyaudio.systemui.navigationbar.menu.checked");
        intentFilter.addAction(FLY_BLUETOOTH_SWITCH);
        intentFilter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        intentFilter.addAction("com.android.action.bluestate");
        intentFilter.addAction("com.flyaudio.power");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("com.flyaudio.show.LockScreen");
        intentFilter.addAction("cn.flyaudio.screen.navigationbar_show");
        intentFilter.addAction("com.android.systemui.screenoff");
        intentFilter.addAction("cn.flyaudio.mobiledata.open");
        context.registerReceiver(myBroadcast, intentFilter);
    }


    public void startFlyaudioServices() {

    }

    @Override
    void onScreenBrightnessAutoStatus(int i) {
        super.onScreenBrightnessAutoStatus(i);
    }

    @Override
    void onScreenBrightness(int i) {
        super.onScreenBrightness(i);
    }

    @Override
    public void onVolumeStatus(int i, int i2) {
        Log.d(TAG, "onVolumeStatus: i  = " + i + " i2 = " + i2+" seekBar_volume = "+seekBar_volume);
//        if (seekBar_volume == null) {
//            return;
//        }
        if (maxVolumeStatus != i) {
            maxVolumeStatus = i;
            if(seekBar_volume != null){
                seekBar_volume.setMax(maxVolumeStatus);
                seekBar_volume.setProgress(i2);
            }

            if(volume_title != null){
                volume_title.setText(i2 + "");
            }


            if (!VolumeFromUser) {
                Log.d(TAG, "setprogress onVolumeStatus i2 = " + i2);
                if(seekBar_volume != null){
                    seekBar_volume.setProgress(i2);
                }

                if(volume_title !=null ){
                    volume_title.setText(i2+"");
                }

            }




        }
        currVolumeStatus = i2;
      //  super.onVolumeStatus(i, i2);
    }


    private  void gotoAir(){
        Log.d(TAG,"gotoAir");
        mContext.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        FlySystemManager.getInstance().gotoPage(FlySystemManager.AIR_PAGE);
    }


    public void setNewNavigationBarViewRoot(FrameLayout nav,Context context) {

        View navbarLayout = 	LayoutInflater.from(context).inflate(R.layout.newnavigationbar,null);
        mHomeImgBtn =(ImageButton)navbarLayout.findViewById(R.id.home);
        mBackImgBtn =(ImageButton)navbarLayout.findViewById(R.id.back);
        mAppImgBtn = (ImageButton)navbarLayout.findViewById(R.id.app);
        mLeftTextView = (TextView)navbarLayout.findViewById(R.id.lefttv) ;
        mRightTextView = (TextView)navbarLayout.findViewById(R.id.righttv) ;
        mHomeImgBtn.setOnClickListener(this);
        mHomeImgBtn.setOnLongClickListener(this);
        mBackImgBtn.setOnClickListener(this);
        mBackImgBtn.setOnLongClickListener(this);
        mAppImgBtn.setOnClickListener(this);

        mLeftTextView.setOnClickListener(this);
        mRightTextView.setOnClickListener(this);



        /*
        mNavigationImgBtn = (ImageButton)navbarLayout.findViewById(R.id.navigation);
        mVolumeImgBtn = (ImageButton)navbarLayout.findViewById(R.id.new_volume);
        mView = navbarLayout.findViewById(R.id.voice);
        mleftseatImgBtn = (ImageButton)navbarLayout.findViewById(R.id.leftseat);
        mrightseatImgBtn = (ImageButton)navbarLayout.findViewById(R.id.rightseat);
        mfrontacImgBtn = (ImageButton)navbarLayout.findViewById(R.id.frontac);
        mbackacImgBtn = (ImageButton)navbarLayout.findViewById(R.id.backac);

        mNavigationImgBtn.setOnClickListener(this);
        mVolumeImgBtn.setOnClickListener(this);
        mView.setOnClickListener(this);
        mleftseatImgBtn.setOnClickListener(this);
        mrightseatImgBtn.setOnClickListener(this);
        mfrontacImgBtn.setOnClickListener(this);
        mbackacImgBtn.setOnClickListener(this);*/

        //添加底部导航栏
        nav.addView(navbarLayout);

        //初始化下拉菜单
       // setFlyaudioQsContainer();
    }



    public void sendKeyCode(final int keyCode){
        new Thread () {
            @Override public void run() {
                try
                {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    Log.e("Exception when sendPointerSync", e.toString());
                }
            }
        }.start();
    }

    private void sendVirtualKey(int keycode){

        try
        {
            String keyCommand = "input keyevent " + keycode;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(keyCommand);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            Log.d(TAG,"exception e "+e.getMessage());
            e.printStackTrace();
        }
    }




    public void sendFlyAppManger(int i) {

    }


    private void setButtonText() {

    }


    @Override
    public void onClick(View v) {
      Log.d(TAG,"onclick");
        mContext.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        switch (v.getId()){
            case R.id.home:
                removeVolumeWindow();
               // sendVirtualKey(KeyEvent.KEYCODE_HOME);
                sendKeyCode(KeyEvent.KEYCODE_HOME);
                break;
            case R.id.back:
                removeVolumeWindow();
               // sendVirtualKey(KeyEvent.KEYCODE_BACK);
                sendKeyCode(KeyEvent.KEYCODE_BACK);
                break;
            case R.id.navigation:
                startActivity("com.autonavi.amapauto","com.autonavi.auto.MainMapActivity");
                break;
            case R.id.app:
                startActivity("cn.flyaudio.moreapplication","cn.flyaudio.moreapplication.ui.MainActivity");
                break;
            case R.id.new_volume:
                addVolumeWindow();
                //addVolumeDialog();
                break;
            case R.id.voice:
                Log.d(TAG,"startvoice");
                startVoice();
                break;
            case R.id.lefttv:
                gotoAir();
                break;
            case R.id.righttv:
                gotoAir();
            case R.id.leftseat:
                break;
            case R.id.rightseat:
                break;
            case R.id.frontac:
                break;
            case R.id.backac:
                break;
                default:
                    break;
        }

    }




    /**
     * 添加音量设置的弹窗
     */
   private void addVolumeWindow(){

       view = LayoutInflater.from(mContext).inflate(R.layout.new_volume_llayout,null);
       volume_title = (TextView) view.findViewById(R.id.volume_txv);
       seekBar_volume = (VerticalSeekBar) view.findViewById(R.id.volume_seekbar);
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
               //if (fromUser) {
                   VolumeFromUser = true;
                   currprogress = progress;
                   Log.d(TAG,"volumeseekbar onProgressChanged progress = "+progress);
                   FlySystemManager.getInstance().setVolume(currprogress);
                   volume_title.setText(currprogress+"");
               //}
           }

           // 结束拖动时触发
           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
               Log.d(TAG,"onStopTrackingTouch");
               myHandler.removeMessages(MG_SEEKBAR_VOLUME);
               myHandler.sendEmptyMessageDelayed(MG_SEEKBAR_VOLUME, 500);
           }
       });


       mWindowManager.addView(view,getLayoutParams());

   }


   private void removeVolumeWindow(){
       if(mWindowManager!=null && view != null){
           mWindowManager.removeViewImmediate(view);
           view = null;
       }
   }


    private WindowManager.LayoutParams getLayoutParams() {

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                150, 921,
                2016,WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);



        lp.gravity = Gravity.START|Gravity.TOP;
        lp.windowAnimations = 0;
        lp.x = 930;
        lp.y = 863;

        return lp;
    }

    private void addVolumeDialog(){

        Dialog dialog = new Dialog(mContext.getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_volume_llayout);
        Window dialogWindow = dialog.getWindow();
       // dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 150;
        lp.height = 921;
        lp.x = 930;
        lp.y = 863;
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


    }




    private void startActivity(String packagename,String classname){
        Intent intent = new Intent();
        intent.setClassName(packagename, classname);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d(TAG, "startActivity: packagename = "+packagename+" classname = " + classname);
        try {
            mContext.startActivity(intent);
        } catch (Exception e) {
            Log.d(TAG,"startactivity exception = "+e.getMessage());

        }
    }

    @Override
    public boolean onLongClick(View v) {
       Log.d(TAG,"onLongClick");
       switch (v.getId()){
            case R.id.home:
                startVoice();
                break;
           case R.id.back:
               startRecentActivity();
               break;
               default:break;
       }
        return true;
    }


    /**
     * 启动最近运行应用的列表
     */
    public void startRecentActivity(){
        Log.d(TAG,"start recent");
        Intent intent = new Intent("com.android.systemui.recents.TOGGLE_RECENTS");
        intent.setClassName("com.android.systemui",
                "com.android.systemui.recents.RecentsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        mContext.startActivity(intent);

    }

    class MyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "onReceive: action:" + action);
            if (action == null){
                return;
            } else if (action.equals("cn.flyaudio.launcher.senddata")) {
                if (mContext != null) {

                }
            } else if (action.equals(Intent.ACTION_CONFIGURATION_CHANGED)) {
                //中英文切换
                setButtonText();
                setQsText();
            } else if ("com.android.systemui.screenoff".equals(intent.getAction())) {
                String screenOff = intent.getStringExtra("KEY_SCREEN_OFF");
                if (!TextUtils.isEmpty(screenOff) && screenOff.endsWith("VALUE_HIDE")) {
                    mContext.sendBroadcast(new Intent().setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
                }
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();

    }

}
