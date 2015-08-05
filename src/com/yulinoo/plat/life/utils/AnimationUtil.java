package com.yulinoo.plat.life.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;

import com.yulinoo.plat.melife.R;

public class AnimationUtil {
	public static AlphaAnimation getInAlphaAnimation(long durationMillis) {
		AlphaAnimation inAlphaAnimation = new AlphaAnimation(0, 1);
		inAlphaAnimation.setDuration(durationMillis);
		return inAlphaAnimation;
	}
	
	public static void startShakeAlphaAnimation(Context cxt,View shakeView) {
		shakeView.startAnimation(AnimationUtils.loadAnimation(cxt, R.anim.shake));//加载动
	}
}
