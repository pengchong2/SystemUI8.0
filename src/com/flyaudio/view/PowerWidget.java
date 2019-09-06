/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flyaudio.view;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flyaudio.entities.FlyNavigator;
import com.flyaudio.tools.SkinResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PowerWidget extends FrameLayout {
	private static final String TAG = "PowerWidget";
	// 20141021
	private ViewPager viewPager;
	private List<View> vlist;
	private FrameLayout mFrameLayout;
	private ImageView[] mTips;
	public static final String BUTTON_DELIMITER = "|";

	private static final String BUTTONS_DEFAULT =
			 PowerButton.BUTTON_WIFI + BUTTON_DELIMITER
			+ PowerButton.BUTTON_MOBILEDATA + BUTTON_DELIMITER
			+ PowerButton.BUTTON_BLUETOOTH + BUTTON_DELIMITER
			+ PowerButton.BUTTON_AIRPLANE + BUTTON_DELIMITER
			+ PowerButton.BUTTON_WIFIAP + BUTTON_DELIMITER
			+ PowerButton.BUTTON_SOUNDEFFECT;

	private static final LayoutParams WIDGET_LAYOUT_PARAMS = new LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, // width = match_parent
			ViewGroup.LayoutParams.WRAP_CONTENT // height = wrap_content
	);

	private static final LinearLayout.LayoutParams BUTTON_LAYOUT_PARAMS = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT, // width = wrap_content
			ViewGroup.LayoutParams.MATCH_PARENT // height = match_parent
	);

	private static final int LAYOUT_SCROLL_BUTTON_THRESHOLD = 6;

	// this is a list of all possible buttons and their corresponding classes
	private static final HashMap<String, Class<? extends PowerButton>> sPossibleButtons = new HashMap<String, Class<? extends PowerButton>>();

	static {

		sPossibleButtons.put(PowerButton.BUTTON_WIFI,
				WifiButton.class);
		sPossibleButtons.put(PowerButton.BUTTON_MOBILEDATA,
				MobileDataButton.class);
		sPossibleButtons.put(PowerButton.BUTTON_AIRPLANE,
				AirplaneModeButton.class);
		sPossibleButtons.put(PowerButton.BUTTON_BLUETOOTH,
				BluetoothButton.class);
		sPossibleButtons.put(PowerButton.BUTTON_MOBILEDATA1,
				MobileDataButton.class);
		sPossibleButtons.put(PowerButton.BUTTON_AIRPLANE1,
				AirplaneModeButton.class);
		sPossibleButtons.put(PowerButton.BUTTON_SOUNDEFFECT,
				SoundEffectButton.class);
		sPossibleButtons.put(PowerButton.BUTTON_WIFIAP,WifiApButton.class);

	}

	// this is a list of our currently loaded buttons
	private final HashMap<String, PowerButton> mButtons = new HashMap<String, PowerButton>();
	private final ArrayList<String> mButtonNames = new ArrayList<String>();

	private OnClickListener mAllButtonClickListener;
	private OnLongClickListener mAllButtonLongClickListener;

	private Context mContext;
	private Handler mHandler;
	private LayoutInflater mInflater;
	private WidgetBroadcastReceiver mBroadcastReceiver = null;
	private WidgetSettingsObserver mObserver = null;

	private long[] mShortPressVibePattern;
	private long[] mLongPressVibePattern;

	private LinearLayout mButtonLayout;
	private LinearLayout mSecondButtonLayout;
	private LinearLayout mTotalButtonLayout;

	public static Context staticContext;

	private SnappingScrollView mScrollView;

	public PowerWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d("FlyaudioSystemUI","PowerWidget struct ");
		mContext = context;

		staticContext = context;
		mHandler = new Handler();
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mShortPressVibePattern = getLongIntArray(mContext.getResources(),
				com.android.internal.R.array.config_virtualKeyVibePattern);
		mLongPressVibePattern = getLongIntArray(mContext.getResources(),
				com.android.internal.R.array.config_longPressVibePattern);
		// get an initial width
		updateButtonLayoutWidth();
		setupWidget();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		// updateVisibility();
	}

	static long[] getLongIntArray(Resources r, int resid) {
		int[] ar = r.getIntArray(resid);
		if (ar == null) {
			return null;
		}
		long[] out = new long[ar.length];
		for (int i = 0; i < ar.length; i++) {
			out[i] = ar[i];
		}
		return out;
	}

	public void destroyWidget() {

		// remove all views from the layout
		removeAllViews();

		// unregister our content receiver
		if (mBroadcastReceiver != null) {
			mContext.unregisterReceiver(mBroadcastReceiver);
		}
		// unobserve our content
		if (mObserver != null) {
			mObserver.unobserve();
		}

		// clear the button instances
		unloadAllButtons();
	}

	public void setupWidget() {
		destroyWidget();
		String buttons = null ;
		if (buttons == null) {

				buttons = SkinResource.getSkinStringByName("quick_switch_buttons_list");
			if (buttons==null||buttons.equals("")) {
				buttons = BUTTONS_DEFAULT;
			}
		}

		for (String button : buttons.split("\\|")) {
			if (loadButton(button)) {
			  mButtonNames.add(button);
			} else {

			}
		}
		recreateButtonLayout();
		// updateHapticFeedbackSetting();

		// set up a broadcast receiver for our intents, based off of what our
		// power buttons have been loaded
		setupBroadcastReceiver();
		IntentFilter filter = getMergedBroadcastIntentFilter();
		// we add this so we can update views and such if the settings for our
		// widget change
		// filter.addAction(Settings.SETTINGS_CHANGED);
		// we need to detect orientation changes and update the static button
		// width value appropriately
		filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
		// register the receiver
		mContext.registerReceiver(mBroadcastReceiver, filter);
		// register our observer
		mObserver = new WidgetSettingsObserver(mHandler);
		mObserver.observe();
	}

	private boolean loadButton(String key) {
		// first make sure we have a valid button
		if (!sPossibleButtons.containsKey(key)) {
			return false;
		}

		if (mButtons.containsKey(key)) {
			return true;
		}

		try {
			// we need to instantiate a new button and add it
			PowerButton pb = sPossibleButtons.get(key).newInstance();
			pb.setExternalClickListener(mAllButtonClickListener);
			pb.setExternalLongClickListener(mAllButtonLongClickListener);
			// save it

			mButtons.put(key, pb);
		} catch (Exception e) {

			return false;
		}

		return true;
	}

	private void unloadButton(String key) {
		// first make sure we have a valid button
		if (mButtons.containsKey(key)) {
			// wipe out the button view
			mButtons.get(key).setupButton(null);
			// remove the button from our list of loaded ones
			mButtons.remove(key);
		}
	}

	private void unloadAllButtons() {
		// cycle through setting the buttons to null
		for (PowerButton pb : mButtons.values()) {
			pb.setupButton(null);
		}

		// clear our list
		mButtons.clear();
		mButtonNames.clear();
	}

	static class SnappingScrollView extends HorizontalScrollView {

		private boolean mSnapTrigger = false;

		public SnappingScrollView(Context context) {
			super(context);
		}

		Runnable mSnapRunnable = new Runnable() {
			@Override
			public void run() {
				int mSelectedItem = ((getScrollX() + (BUTTON_LAYOUT_PARAMS.width / 2)) / BUTTON_LAYOUT_PARAMS.width);
				int scrollTo = mSelectedItem * BUTTON_LAYOUT_PARAMS.width;
				smoothScrollTo(scrollTo, 0);
				mSnapTrigger = false;
			}
		};

		@Override
		protected void onScrollChanged(int l, int t, int oldl, int oldt) {
			super.onScrollChanged(l, t, oldl, oldt);
			if (Math.abs(oldl - l) <= 1 && mSnapTrigger) {
				removeCallbacks(mSnapRunnable);
				postDelayed(mSnapRunnable, 100);
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			int action = ev.getAction();
			if (action == MotionEvent.ACTION_CANCEL
					|| action == MotionEvent.ACTION_UP) {
				mSnapTrigger = true;
			}
			return super.onTouchEvent(ev);
		}

	}

	private void recreateButtonLayout() {
		removeAllViews();

		// create a linearlayout to hold our buttons

		vlist = new ArrayList<View>();

		mFrameLayout = new FrameLayout(mContext);
		FrameLayout.LayoutParams fParams = (FrameLayout.LayoutParams) new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		mFrameLayout.setLayoutParams(fParams);

		// 动态设置viewpager
		viewPager = new ViewPager(mContext);
		ViewPager.LayoutParams vParams = (ViewPager.LayoutParams) new ViewPager.LayoutParams();
		vParams.width = ViewPager.LayoutParams.MATCH_PARENT;
		vParams.height = ViewPager.LayoutParams.MATCH_PARENT;
		vParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		viewPager.setLayoutParams(vParams);

		// 存放开关按钮的布局
		LinearLayout onelinearLayout = new LinearLayout(mContext);
		LinearLayout towlinearLayout = new LinearLayout(mContext);
		onelinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		onelinearLayout.setWeightSum(6);

		LayoutParams layoutParams = new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		//layoutParams.gravity=Gravity.CENTER_HORIZONTAL;
		//layoutParams.width = 884;
		onelinearLayout.setLayoutParams(layoutParams);

		towlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		towlinearLayout.setLayoutParams(new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));


		// 动态设置小圆点
		LinearLayout outlinearLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams outParams = (LinearLayout.LayoutParams) new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// 此处相当于布局文件中的Android:layout_gravity属性
		outParams.gravity = Gravity.BOTTOM;
		// 此处相当于布局文件中的Android：gravity属性
		outlinearLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		outlinearLayout.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout inlinearLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams inParams = (LinearLayout.LayoutParams) new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		inlinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		inlinearLayout.setOrientation(LinearLayout.HORIZONTAL);


		for (int i = 0; i < mButtonNames.size(); i++) {
			String button = mButtonNames.get(i);
			PowerButton pb = mButtons.get(button);
			if (pb != null) {
				View buttonView = SkinResource.getSkinLayoutViewByName2("systemui_power_widget_button2");
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
				lp.weight = 1;
				buttonView.setLayoutParams(lp);
				pb.setupButton(buttonView);
				if (i < 7) {
					onelinearLayout.addView(buttonView);
				} else {
					towlinearLayout.addView(buttonView);
				}
			}else{

			}
		}

		vlist.add(onelinearLayout);
		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		mTips = new ImageView[vlist.size()];
		for (int i = 0; i < mTips.length; i++) {
			ImageView imageView = new ImageView(mContext);
			LinearLayout.LayoutParams imlp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			imlp.leftMargin = 18;
			imlp.bottomMargin = 20;

			imageView.setLayoutParams(imlp);

			mTips[i] = imageView;
			if (i == 0) {
				mTips[i].setBackground(SkinResource.getSkinDrawableByName("page_indicator_focused"));
			} else {
				mTips[i].setBackground(SkinResource.getSkinDrawableByName("page_indicator_unfocused"));
			}
			inlinearLayout.addView(imageView);
		}
		outlinearLayout.addView(inlinearLayout);
		mFrameLayout.addView(viewPager);
		// 暂时去掉小圆点
		// mFrameLayout.addView(outlinearLayout);
		/*
		 * mTotalButtonLayout.addView(mButtonLayout);
		 * mTotalButtonLayout.addView(mSecondButtonLayout);
		 */

		// we determine if we're using a horizontal scroll view based on a
		// threshold of button counts
		if (onelinearLayout.getChildCount() > LAYOUT_SCROLL_BUTTON_THRESHOLD) {
			// // we need our horizontal scroll view to wrap the linear layout
			// mScrollView = new SnappingScrollView(mContext);
			// // make the fading edge the size of a button (makes it more
			// // noticible that we can scroll
			// mScrollView.setFadingEdgeLength(mContext.getResources().getDisplayMetrics().widthPixels
			// / LAYOUT_SCROLL_BUTTON_THRESHOLD);
			// Flog.d(TAG, "getDisplayMetrics().widthPixels = "
			// + mContext.getResources().getDisplayMetrics().widthPixels);
			// mScrollView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
			// mScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
			// mScrollView.addView(onelinearLayout, WIDGET_LAYOUT_PARAMS);
			// updateScrollbar();
			// addView(mScrollView, WIDGET_LAYOUT_PARAMS);
		} else {
			// not needed, just add the linear layout
			addView(mFrameLayout, WIDGET_LAYOUT_PARAMS);
		}
	}

	class MyAdapter extends PagerAdapter {// 动态设置viewpager
		@Override
		public int getCount() {
			return vlist.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(vlist.get(position));

		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(vlist.get(position));
			Log.d("yaoyao", "instantiateItem: setView");
			return vlist.get(position);
		}

	}

	// 指引页面更改事件监听器
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < vlist.size(); i++) {
				mTips[arg0]
						.setBackgroundResource(SkinResource.getSkinDrawableIdByName("page_indicator_focused"));
				if (arg0 != i) {
					mTips[i].setBackgroundResource(SkinResource.getSkinDrawableIdByName("page_indicator_unfocused"));
				}
			}
		}
	}

	public void updateAllButtons() {
		// cycle through our buttons and update them
		for (PowerButton pb : mButtons.values()) {
			pb.update(mContext);
		}
	}

	private IntentFilter getMergedBroadcastIntentFilter() {
		IntentFilter filter = new IntentFilter();

		for (PowerButton button : mButtons.values()) {
			IntentFilter tmp = button.getBroadcastIntentFilter();

			// cycle through these actions, and see if we need them
			int num = tmp.countActions();
			for (int i = 0; i < num; i++) {
				String action = tmp.getAction(i);
				if (!filter.hasAction(action)) {
					filter.addAction(action);
				}
			}
		}

		// return our merged filter
		return filter;
	}

	private List<Uri> getAllObservedUris() {
		List<Uri> uris = new ArrayList<Uri>();

		for (PowerButton button : mButtons.values()) {
			List<Uri> tmp = button.getObservedUris();

			for (Uri uri : tmp) {
				if (!uris.contains(uri)) {
					uris.add(uri);
				}
			}
		}

		return uris;
	}

	public void setGlobalButtonOnClickListener(OnClickListener listener) {
		mAllButtonClickListener = listener;
		for (PowerButton pb : mButtons.values()) {
			pb.setExternalClickListener(listener);
		}
	}

	public void setGlobalButtonOnLongClickListener(
			OnLongClickListener listener) {
		mAllButtonLongClickListener = listener;
		for (PowerButton pb : mButtons.values()) {
			pb.setExternalLongClickListener(listener);
		}
	}

	private void setupBroadcastReceiver() {
		if (mBroadcastReceiver == null) {
			mBroadcastReceiver = new WidgetBroadcastReceiver();
		}
	}

	private void updateButtonLayoutWidth() {
		// use our context to set a valid button width
		BUTTON_LAYOUT_PARAMS.width = mContext.getResources()
				.getDisplayMetrics().widthPixels
				/ LAYOUT_SCROLL_BUTTON_THRESHOLD;

	}

	// private void updateVisibility() {
	// // now check if we need to display the widget still
	// boolean displayPowerWidget =
	// Settings.System.getInt(mContext.getContentResolver(),
	// Settings.System.EXPANDED_VIEW_WIDGET, 1) == 1;
	// if(!displayPowerWidget) {
	// setVisibility(View.GONE);
	// } else {
	// setVisibility(View.VISIBLE);
	// }
	// }

	// private void updateScrollbar() {
	// if (mScrollView == null) return;
	// boolean hideScrollBar =
	// Settings.System.getInt(mContext.getContentResolver(),
	// Settings.System.EXPANDED_HIDE_SCROLLBAR, 0) == 1;
	// mScrollView.setHorizontalScrollBarEnabled(!hideScrollBar);
	// }

	// private void updateHapticFeedbackSetting() {
	// ContentResolver cr = mContext.getContentResolver();
	// int expandedHapticFeedback = Settings.System.getInt(cr,
	// Settings.System.EXPANDED_HAPTIC_FEEDBACK, 2);
	// long[] clickPattern = null, longClickPattern = null;
	// boolean hapticFeedback;
	//
	// if (expandedHapticFeedback == 2) {
	// hapticFeedback = Settings.System.getInt(cr,
	// Settings.System.HAPTIC_FEEDBACK_ENABLED, 1) == 1;
	// } else {
	// hapticFeedback = (expandedHapticFeedback == 1);
	// }
	//
	// if (hapticFeedback) {
	// clickPattern = mShortPressVibePattern;
	// longClickPattern = mLongPressVibePattern;
	// }
	//
	// for (PowerButton button : mButtons.values()) {
	// button.setHapticFeedback(hapticFeedback, clickPattern, longClickPattern);
	// }
	// }


	private class WidgetBroadcastReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.android.recycle.memory")) {
			}
			if (action.equals(Intent.ACTION_CONFIGURATION_CHANGED)) {
				updateButtonLayoutWidth();
				recreateButtonLayout();
			}else {
				// handle the intent through our power buttons
				for (PowerButton button : mButtons.values()) {
					// call "onReceive" on those that matter
					if (button.getBroadcastIntentFilter().hasAction(action)) {
						button.onReceive(context, intent);
					}
				}
			}

			// update our widget
			updateAllButtons();
		}
	};

	// our own settings observer :D
	private class WidgetSettingsObserver extends ContentObserver {
		public WidgetSettingsObserver(Handler handler) {
			super(handler);
		}

		public void observe() {
			ContentResolver resolver = mContext.getContentResolver();

			// watch for display widget
			/*
			 * resolver.registerContentObserver(
			 * Settings.System.getUriFor(Settings.System.EXPANDED_VIEW_WIDGET),
			 * false, this); // watch for scrollbar hiding
			 * resolver.registerContentObserver(
			 * Settings.System.getUriFor(Settings
			 * .System.EXPANDED_HIDE_SCROLLBAR), false, this);
			 */

			// watch for haptic feedback
			/*
			 * resolver.registerContentObserver(
			 * Settings.System.getUriFor(Settings
			 * .System.EXPANDED_HAPTIC_FEEDBACK), false, this);
			 */
			resolver.registerContentObserver(Settings.System
					.getUriFor(Settings.System.HAPTIC_FEEDBACK_ENABLED), false,
					this);

			// watch for changes in buttons
			/*
			 * resolver.registerContentObserver(
			 * Settings.System.getUriFor(Settings.System.WIDGET_BUTTONS), false,
			 * this);
			 */

			// watch for power-button specific stuff that has been loaded
			for (Uri uri : getAllObservedUris()) {
				resolver.registerContentObserver(uri, false, this);
			}
		}

		public void unobserve() {
			ContentResolver resolver = mContext.getContentResolver();
			resolver.unregisterContentObserver(this);
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			ContentResolver resolver = mContext.getContentResolver();
			Resources res = mContext.getResources();
			// do whatever the individual buttons must
			for (PowerButton button : mButtons.values()) {
				if (button.getObservedUris().contains(uri)) {
					button.onChangeUri(resolver, uri);
				}
			}
			updateAllButtons();
		}
	}
}
