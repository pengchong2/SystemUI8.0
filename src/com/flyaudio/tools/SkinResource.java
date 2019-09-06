package com.flyaudio.tools;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.android.systemui.SystemUIApplication;

import java.io.IOException;
import java.io.InputStream;

/**
 * 用于加载包括其他apk中的资源（raw，layout，drawable，values/strings,colors）
 * <p>
 * 加载其他APK的资源，如果找不到就加载本地资源
 */
public class SkinResource {

	public static final String TAG = "systemui-flyaudio";
	private static Context mSkinContext = null;
	private static Context mLocalContext = null;

	public static Context getSkinContext() {
		return mSkinContext;
	}

	public static void initSkinResource(Context context, String pkgName) {
		try {
			mLocalContext = context;
			mSkinContext = context.createPackageContext(pkgName, Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			Log.d(TAG,"systemui no skin");
			mSkinContext = context;
		}
		Log.d(TAG,"systmeui init mSkinContext:"+mSkinContext);
	}

	private static int getIdentifier(String name, String type, Context context) {
		try {
			return context.getResources().getIdentifier(name, type, context.getPackageName());

		} catch (Exception e) {
		Log.d(TAG,"systemui 8  no skin");
			return 0;
		}
	}

	/**
	 * 皮肤包找不到，找本地
	 * @param name
	 * @param type
	 * @return
	 */
	private static int getIdentifier(String name,String type) {
		int id = getIdentifier(name, type, mSkinContext);
		if (id == 0 && mSkinContext != mLocalContext) {
			id = getIdentifier(name, type, mLocalContext);
		}
		return id;
	}

	public static int getSkinDrawableIdByName(String name) {
		return getIdentifier(name, "drawable");
	}

	public static int getSkinLayoutIdByName(String name) {
		return getIdentifier(name, "layout");
	}

	public static int getSkinResourceId(String name, String type) {
		return getIdentifier(name, type);
	}

	public static InputStream getSkinRawInputStream(String name) {
		Context context = mSkinContext;
		int rawId = getIdentifier(name, "raw", context);
		if (rawId == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			rawId = getIdentifier(name, "raw", context);
		}
		if (rawId == 0)
			return null;
		return context.getResources().openRawResource(rawId);
	}

	public static View getSkinLayoutViewByName(String name) {
		Context context = mSkinContext;
		int id = getIdentifier(name, "layout", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(name, "layout", context);
		}
		if(id == 0)
			return null;
		LayoutInflater mInflater = LayoutInflater.from(context);
		return mInflater.inflate(id, null);
	}

	public static Integer getIntegerByName(String name){
		Context context = mSkinContext;
		int id = getIdentifier(name, "integer", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(name, "integer", context);
		}
		if (id == 0) {
			return null;
		}
		return context.getResources().getInteger(id);
	}

	/**
	 * 获取整型数据，获取不到产生异常
	 * @param name
	 * @return
	 */
	public static Integer getSkinIntegerByName(String name) {
		Context context = mSkinContext;
		int id = getIdentifier(name, "integer", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(name, "integer", context);
		}
		return context.getResources().getInteger(id);
	}

	/**
	 * 获取整型数据，获取不到则返回默认值
	 * @param name
	 * @param def
	 * @return
	 */
	public static Integer getSkinIntegerByName(String name, int def) {
		Context context = mSkinContext;
		int id = getIdentifier(name, "integer", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(name, "integer", context);
		}
		if (id == 0) {
			return def;
		}
		return context.getResources().getInteger(id);
	}

	public static String getSkinStringByName(String name) {
		Context context = mSkinContext;
		int id = getIdentifier(name, "string", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(name, "string", context);
		}
		if (id == 0) {
			return null;
		}

		return context.getResources().getString(id);
	}

	public static int getSkinColorByName(String name) {
		Context context = mSkinContext;
		int id = getIdentifier(name, "color", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(name, "color", context);
		}
		if (id == 0) {
			return -1;
		}
		return context.getResources().getColor(id);
	}

	public static int getSkinColorByName(String name, int def) {
		Context context = mSkinContext;
		int id = getIdentifier(name, "color", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(name, "color", context);
		}
		if (id == 0) {
			return def;
		}
		return context.getResources().getColor(id);
	}

	/**
	 * 取得皮肤包中String,获取不到指定则返回默认值
	 * @param resourceName
	 * @return
	 */
	public static float getSkinDimenFromSkin(String resourceName,float def)
	{
		Context context = mSkinContext;
		int id = getIdentifier(resourceName, "dimen", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(resourceName, "dimen", context);
		}
		if (id == 0) {
			return def;
		}
		return context.getResources().getDimension(id);
	}

	/**
	 * 取得皮肤包中String，获取不到指定则会产生Exception
	 * @param resourceName
	 * @return
	 */
	public static float getSkinDimenFromSkin(String resourceName)
	{
		Context context = mSkinContext;
		int id = getIdentifier(resourceName, "dimen", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(resourceName, "dimen", context);
		}
		return context.getResources().getDimension(id);
	}

	public static boolean getSkinBooleanFromSkin(String resourceName) {
		Context context = mSkinContext;
		int id = getIdentifier(resourceName, "bool", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(resourceName, "bool", context);
		}
		return context.getResources().getBoolean(id);
	}

	/**
	 * 获取boolean资源
	 * @param resourceName
	 * @param def
	 * @return
	 */
	public static boolean getSkinBooleanFromSkin(String resourceName, boolean def) {
		Context context = mSkinContext;
		int id = getIdentifier(resourceName, "bool", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(resourceName, "bool", context);
		}
		if (id == 0) {
			return def;
		}
		return context.getResources().getBoolean(id);
	}


	public static int getSkinColorByName2(String name) {
		Context context = mLocalContext;
		int id = getIdentifier(name, "color", context);

		if (id == 0) {
			return -1;
		}
		Log.d(TAG,"getSkinColorByName2 name = "+name+" color = "+context.getResources().getColor(id));
		return context.getResources().getColor(id);
	}

	public static View getSkinLayoutViewByName2(String name) {


		int id = getIdentifier(name, "layout", mLocalContext);

		LayoutInflater mInflater = LayoutInflater.from(mLocalContext);
		return mInflater.inflate(id, null);
	}

	private static int getIdentifier2(String name,String type) {

		int id = getIdentifier(name, type, mLocalContext);

		return id;
	}


	public static int getSkinResourceId2(String name, String type) {
		return getIdentifier2(name, type);
	}

	public static Drawable getSkinDrawableByName(String name) {
		Context context = mSkinContext;
		int id = getIdentifier(name, "drawable", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(name, "drawable", context);
		}
		if (id == 0) {
			return null;
		}
		return context.getResources().getDrawable(id);

	}

	public static ColorStateList getSkinColorStateList(String name) {
		Context context = mSkinContext;
		int id = getIdentifier(name, "color", context);
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(name, "color", context);
		}
		if (id == 0) {
			return null;
		}
		return context.getResources().getColorStateList(id);
	}

	/**
	 * 注意顺序问题
	 * @param names
	 * @return
	 */
	public static int[] getSkinStyleableIdByName(String[] names) {
		int[] result = new int[names.length];
		for (int i = 0; i < names.length; i++) {
			result[i] = getIdentifier(names[i], "attr");
		}
		return result;
	}

	private static class Result{
		private final int id;
		private final Context context;
		public Result(int id,Context context){
			this.id = id;
			this.context = context;
		}
		public int getId() {
			return id;
		}
		public Context getContext() {
			return context;
		}
	}

	private static Result getIdentifierAndContext(String name,String type) {
		Context context = mSkinContext;
		int id = getIdentifier(name, type, context);
		//皮肤包没找到，找本地
		if (id == 0 && mSkinContext != mLocalContext) {
			context = mLocalContext;
			id = getIdentifier(name, type, context);
		}
		//返回结果中包含id和context
		return new Result(id,context);
	}

	public static InputStream getSkinAssetsInputStream(String name){
		Context context = mSkinContext;
		try {
			return context.getAssets().open(name);
		} catch (IOException e) {
		}
		context = mLocalContext;
		try {
			return context.getAssets().open(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}