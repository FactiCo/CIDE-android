package mx.com.factico.cide.animations;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationsFactory {
	public static final int ANIMATION_OUT = 0;
	
	public static final int ANIMATION_1 = 1;
	public static final int ANIMATION_2 = 2;
	public static final int ANIMATION_3 = 3;
	public static final int ANIMATION_4 = 4;
	public static final int ANIMATION_5 = 5;
	
	public static Animation fadeIn(int duration) {
		Animation animation = new AlphaAnimation(0, 1);
		animation.setFillAfter(true);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(duration);
		
		return animation;
	}
	
	public static Animation fadeOut(int duration) {
		Animation animation = new AlphaAnimation(1, 0);
		animation.setFillAfter(true);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(duration);
		
		return animation;
	}
	
	public static Animation translateX(int duration, int fromXDelta, int toXDelta) {
		TranslateAnimation animation = new TranslateAnimation(fromXDelta, toXDelta, 0, 0);
		animation.setFillAfter(true);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(duration);
		
		return animation;
	}
	
	public static Animation translateY(int duration, int fromYDelta, int toYDelta) {
		TranslateAnimation animation = new TranslateAnimation(0, 0, fromYDelta, toYDelta);
		animation.setFillAfter(true);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(duration);
		
		return animation;
	}
	
	public static Animation scaleXY(int duration, float from, float to, Interpolator interpolator) {
		ScaleAnimation animation = new ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, (float) 0.5,
				Animation.RELATIVE_TO_SELF, (float) 0.5);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(duration);

		return animation;
	}
	
	public static AnimationSet clickScale(int duration) {
		AnimationSet set = new AnimationSet(true);
		Animation in = scaleXY(duration, 1.0f, 0.9f, new DecelerateInterpolator());
		Animation out = scaleXY(duration, 0.9f, 1.0f, new AccelerateInterpolator());
		//out.setStartOffset(duration);
		set.addAnimation(in);
		set.addAnimation(out);
		
		return set;
	}
	
	public static Animation createAnimation(int animationId, int duration) {
		if (animationId == ANIMATION_OUT) {
			Animation animation = fadeOut(duration);
			return animation;
			
		} else if (animationId == ANIMATION_1) {
			Animation animation = fadeIn(duration);
			return animation;

		} else if (animationId == ANIMATION_2) {
			Animation animation = fadeOut(duration);
			return animation;
			
		} else if (animationId == ANIMATION_3) {
			Animation animation = translateX(duration, 0, -300);
			return animation;
			
		} else if (animationId == ANIMATION_4) {
			Animation animation = translateY(duration, 0, -300);
			return animation;
			
		} else if (animationId == ANIMATION_5) {
			Animation animation = scaleXY(duration, 1.0f, 0.5f, new LinearInterpolator());
			return animation;
		}
		
		return null;
	}
	
}
