package com.amphibiousachievement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/**
 * 
 * 
 * @author Eric Ruleman
 *
 */
public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.practice_math_button:
			final Intent i = new Intent(getBaseContext(),
					QuestionActivity.class);
			startActivity(i);
			break;
		case R.id.review_math_button:
			// do stuff;
			break;
		case R.id.practice_writing_button:
			// do stuff;
			break;
		case R.id.review_writing_button:
			// do stuff;
			break;
		case R.id.practice_critical_reading_button:
			// do stuff;
			break;
		case R.id.review_critical_reading_button:
			// do stuff;
			break;
		}
	}
}
