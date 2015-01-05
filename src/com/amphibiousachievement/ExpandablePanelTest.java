package com.amphibiousachievement;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandablePanelTest extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expandable_panel_test);
		ExpandablePanel panel = (ExpandablePanel) findViewById(R.id.foo);
		
		final ImageView expand_collapse_content_caret = (ImageView)findViewById(R.id.expand);

		panel.setOnExpandListener(new ExpandablePanel.OnExpandListener() {
			public void onCollapse(View handle, View content) {
				//TextView btn = (TextView) handle;
				expand_collapse_content_caret.setImageResource(R.drawable.expand_content_indicator);
				//btn.setText("More");
			}

			public void onExpand(View handle, View content) {
				TextView btn = (TextView) handle;
				expand_collapse_content_caret.setImageResource(R.drawable.collapse_content_indicator);
				//btn.setText("Less");
			}
		});
		
//		ExpandablePanel panel2 = (ExpandablePanel) findViewById(R.id.foo2);
//		
//		final ImageView expand_collapse_content_caret2 = (ImageView)findViewById(R.id.expand2);
//
//		panel2.setOnExpandListener(new ExpandablePanel.OnExpandListener() {
//			public void onCollapse(View handle, View content) {
//				//TextView btn = (TextView) handle;
//				expand_collapse_content_caret2.setImageResource(R.drawable.expand_content_indicator);
//				//btn.setText("More");
//			}
//
//			public void onExpand(View handle, View content) {
//				TextView btn = (TextView) handle;
//				expand_collapse_content_caret2.setImageResource(R.drawable.collapse_content_indicator);
//				//btn.setText("Less");
//			}
//		});
	}
}
