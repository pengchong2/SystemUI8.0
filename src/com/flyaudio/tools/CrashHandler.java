package com.flyaudio.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.systemui.SystemUIApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 *  需要在Application中注册，为了要在程序启动器就监控整个程序。
 *  需要在清单文件中声明Application以及添加文件读写权限
 *
 *  查看输出结果请到：Environment.getExternalStorageDirectory().getAbsolutePath() + “/” + "crash_log/"
 *
 * Created by yao on 16-6-29.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{
    //查看日志
    public static final String TAG = "systemui-flyaudio";
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler instance;
    //程序的Context对象
    private Context mcontext;
    //用来存储设备信息和异常信息
    private Map<String, String> infors = new HashMap<String, String>();
    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("MM-dd-HH-mm-ss");
    /** 保证只有一个CrashHandler实例 */
    private CrashHandler(){}
    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance(){
        if (instance==null) {
            instance = new CrashHandler();
        }
        return instance;
    }
    /**
     * 初始化
     */
    public void init(Context context) {
        mcontext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex==null) {
            return false;
        }
        //收集设备参数信息
        collectDeviceInfo(mcontext);

        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                //Toast.makeText(mcontext, "SystemUI err...", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        //保存日志文件
        saveCathInfo2File(ex);

        return true;
    }
    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return  返回文件名称,便于将文件传送到服务器
     */
    private String saveCathInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infors.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "SysUIErr-"+"?"+".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //保存路径如下
                String path= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SysUIErr"+ File.separator;
                File dir = new File(path);
                if (dir.exists()) {
                    long size=getFileSize(dir)/1024/1024;
                    if (size>5) {
                        deleteFiles(dir);
                    }
                 }
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(path + fileName,true);
                byte[]b=sb.toString().getBytes();
                for(int i=0;i<b.length;i++){
                    b[i]=(byte)(b[i]^'S');
                }
                fos.write(b);
                //发送给开发人员
                //sendCrashLog2PM(path+fileName);
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.d(TAG, "捕捉系统异常时写入文件时候出现错误...：", e);
        }
        return null;
    }

    private long fileSize;
    public long getFileSize(File dir) {
        if (!dir.isDirectory()){
            fileSize+=dir.length();
            Log.d(TAG,"-size-"+fileSize);
        }else{
            for (File file:dir.listFiles()){
                if (!file.isDirectory()){
                    fileSize+=file.length();
                }else {
                    getFileSize(file);
                }
            }
        }
        return fileSize;
    }

    private boolean deleteFiles(File dir) {
        if (dir==null&&!dir.exists()){
            return false;
        }
        if (dir.isFile()|| dir.listFiles()==null){
            dir.delete();
            return true;
        }else{
            for (File file:dir.listFiles()){
                if (file.isFile()){
                    file.delete();
                }else if (file.isDirectory()){
                    deleteFiles(file);
                }
            }
            dir.delete();
        }
        return true;
    }

    public static void jiema() throws IOException {
        boolean exists = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SysUIErr" + File.separator + "SysUIErr-" + "?" + ".log").exists();
        if (!exists)return;

        FileInputStream  in=new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SysUIErr"+ File.separator+"SysUIErr-"+"?"+".log");

        FileOutputStream  out= new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SysUIErr"+ File.separator+"SysUIErr-"+"show"+".log");

        byte ch[]=new byte[1024];

        int n=0;

        String context=null;

        while((n=in.read(ch,0,ch.length))!=-1){

            for(int i=0;i<ch.length;i++){

                ch[i]=(byte)(ch[i]^'S');

            }

            context=new String(ch,0,n);

            out.write(context.getBytes());

        }

        //System.out.println(context);

        //关闭输入流

        out.close();

    }

    /**
     * 收集设备参数信息
     *
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi!=null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infors.put("versionName", versionName);
                infors.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "捕捉系统异常时收集设备参数(package)时候发生: ",e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infors.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
               Log.d(TAG, "捕捉系统异常时收集设备参数(crash)时候发生:", e);
            }
        }

    }
    /**
     * 将捕获的导致崩溃的错误信息发送给开发人员
     *
     * 目前只将log日志保存在sdcard 和输出到LogCat中，并未发送给后台。
     */
    private void sendCrashLog2PM(String fileName){
        if(!new File(fileName).exists()){
            Toast.makeText(mcontext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
            return;
        }
        FileInputStream fis = null;
        BufferedReader reader = null;
        String s = null;
        try {
            fis = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(fis, "GBK"));
            while(true){
                s = reader.readLine();
                if(s == null) {
                    break;
                }
                //由于目前尚未确定以何种方式发送
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{   // 关闭流
            try {
                reader.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(throwable) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, throwable);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
               Log.d(TAG, "捕捉系统异常时error : ", e);
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
}
/*第二种上传到服务器
*
*
*     /**
     * 自定义的 异常处理类 , 实现了 UncaughtExceptionHandler接口
     * @author Administrator
     *
     */
/*public class MyCrashHandler implements UncaughtExceptionHandler {
    // 需求是 整个应用程序 只有一个 MyCrash-Handler
    private static MyCrashHandler myCrashHandler ;
    private Context context;
    private DoubanService service;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    //1.私有化构造方法
    private MyCrashHandler(){

    }

    public static synchronized MyCrashHandler getInstance(){
        if(myCrashHandler!=null){
            return myCrashHandler;
        }else {
            myCrashHandler  = new MyCrashHandler();
            return myCrashHandler;
        }
    }
    public void init(Context context,DoubanService service){
        this.context = context;
        this.service = service;
    }


    public void uncaughtException(Thread arg0, Throwable arg1) {
        System.out.println("程序挂掉了 ");
        // 1.获取当前程序的版本号. 版本的id
        String versioninfo = getVersionInfo();

        // 2.获取手机的硬件信息.
        String mobileInfo  = getMobileInfo();

        // 3.把错误的堆栈信息 获取出来
        String errorinfo = getErrorInfo(arg1);

        // 4.把所有的信息 还有信息对应的时间 提交到服务器
        try {
            service.createNote(new PlainTextConstruct(dataFormat.format(new Date())),
                    new PlainTextConstruct(versioninfo+mobileInfo+errorinfo), "public", "yes");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //干掉当前的程序
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    *//**
     * 获取错误的信息
     * @param arg1
     * @return
     *//*
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error= writer.toString();
        return error;
    }

    *//**
     * 获取手机的硬件信息
     * @return
     *//*
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        //通过反射获取系统的硬件信息
        try {

            Field[] fields = Build.class.getDeclaredFields();
            for(Field field: fields){
                //暴力反射 ,获取私有的信息
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name+"="+value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    *//**
     * 获取手机的版本信息
     * @return
     *//*
    private String getVersionInfo(){
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info =pm.getPackageInfo(context.getPackageName(), 0);
            return  info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }
}



创建一个Application实例将MyCrashHandler注册到整个应用程序上，创建出服务并进行传递：
        Java代码  收藏代码

*//**
 * 整个(app)程序初始化之前被调用
 * @author Administrator
 *
 *//*
public class DoubanApplication extends Application {
    public NoteEntry entry;
    @Override
    public void onCreate() {
        super.onCreate();
        String apiKey = "0fab7f9aa21f39cd2f027ecfe65dad67";
        String secret = "87fc1c5e99bfa5b3";
        // 获取到service
        DoubanService myService = new DoubanService("我的小豆豆", apiKey,
                secret);
        myService.setAccessToken("1fa4e5be0f808a0b5eeeb13a2e819e21", "56a622c1138dbfce");
        MyCrashHandler handler = MyCrashHandler.getInstance();
        handler.init(getApplicationContext(),myService);
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }
}
* */
