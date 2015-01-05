package com.amphibiousachievement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * The launch activity of the app. Displays Amphibious Achievement's logo,
 * animated such that it fades in and then fades out. This activity transistions
 * to the HomeActivity.
 * 
 * @author Eric Ruleman
 * 
 */
public class SplashScreenActivity extends ActionBarActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash_screen);
		
		//android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		//actionBar.hide();
		
		final ImageView image = (ImageView)findViewById(R.id.aalogo);
		image.setVisibility(View.INVISIBLE);

		final Animation animationFadeInAndOut = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in_out);

		image.startAnimation(animationFadeInAndOut);
		
		final Intent intent = new Intent(this, HomeActivity.class);
		
		animationFadeInAndOut.setAnimationListener(new AnimationListener(){
		    public void onAnimationStart(Animation a){}
		    public void onAnimationRepeat(Animation a){}
		    public void onAnimationEnd(Animation a){
				startActivity(intent);
		    }
		});
	}
}