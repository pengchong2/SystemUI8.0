package com.flyaudio.tools;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import java.io.IOException;

/**
 * @author 彭冲
 * @des 启动第三方app的工具类
 */
public class ActivityUtils {


    /**
     * 根据action启动页面
     * @param context
     * @param action   启动页面的action
     */
    public static void startActivity(Context context, String action){
        if(context == null || action == null){
            return;
        }
        Intent intent = new Intent(action);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try{
            context.startActivity(intent);
        }catch (Exception e){

        }

    }


    /**
     * 根据包名和目标activity启动页面
     * @param context
     * @param packagename  应用的包名
     * @param classname  启动的页面名
     */
    public static void startActivity(Context context ,String packagename,String classname){
        if(context == null || packagename == null || classname == null){
            return;
        }
        Intent intent = new Intent();
        ComponentName comp = new ComponentName(packagename,classname);
        intent.setComponent(comp);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try{
            context.startActivity(intent);
        }catch (Exception e){

        }
    }




}
