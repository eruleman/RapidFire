package com.amphibiousachievement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The Question Activity displays the current question and the five answer
 * choices. Upon the user selecting an answer, an alert dialog appears that
 * tells the user whether or not they correctly answered the question. The user
 * can then press "Continue" to move to the next question.
 * 
 * @author Eric Ruleman
 * 
 */
public class AlternateQuestionActivity extends Activity {
	int questionID; // ID of the question from the question bank
	int maxQuestionCount = 11; // the number of questions stored in strings.xml UPDATE ME WHEN NECESSARY
							  // Don't forget to update the lookup tables at the bottom of this file as well
	int numberOfQuestionsAsked = 1; // overwritten by the intent's bundle if not first question in this set
	ArrayList<Integer> questionsAnsweredThisSet = new ArrayList<Integer>();
	ArrayList<String> previousQuestionInfo = new ArrayList<String>();
	
	long tStart, tEnd;
	
	String questionText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_question);
		
		// read the questionNumber from the last intent's bundle
		Bundle extras = this.getIntent().getExtras();
		if ( extras != null ) {
		  if (extras.containsKey("numberOfQuestionsAsked")) {
			  numberOfQuestionsAsked = Integer.parseInt(extras.getString("numberOfQuestionsAsked"));
		  }
		  if (extras.containsKey("questionsAnsweredThisSet")) {
			  questionsAnsweredThisSet = extras.getIntegerArrayList("questionsAnsweredThisSet");
		  }
		  
		  if (extras.containsKey("previousQuestionInfo")) {
			  previousQuestionInfo = extras.getStringArrayList("previousQuestionInfo");
		  }
		}
		
		// configure the action bar to show the current question number out of 10
		setTitle("Question " + numberOfQuestionsAsked + " of 10");
        setProgress(numberOfQuestionsAsked*1000 - 1); // -1 necessary because 10,000 makes ProgressBar invisible
        setProgressBarVisibility(true);


		// configure the WebView for MathJax rendering of mathematical expressions
        WebView w = (WebView) findViewById(R.id.question_math_formula);
        w.getSettings().setJavaScriptEnabled(true);
		w.loadDataWithBaseURL(
				"http://bar",
				"<script type='text/x-mathjax-config'>"
						+ "MathJax.Hub.Config({ "
						+ "showMathMenu: false, "
						+ "jax: ['input/TeX','output/HTML-CSS'], "
						+ "extensions: ['tex2jax.js'], "
						+ "TeX: { extensions: ['AMSmath.js','AMSsymbols.js',"
						+ "'noErrors.js','noUndefined.js'] } "
						+ "});"
						// prevent loading of MathZoom.js and MathMenu.js at
						// startup, but still allow them to be loaded later if
						// needed.
						+ "(function () { var EXT = MathJax.Extension, mm, mz; MathJax.Hub.Register.StartupHook(\"End Typeset\",function () { mm = EXT.MathMenu; mz = EXT.MathZoom; EXT.MathMenu = EXT.MathZoom = {}; }); MathJax.Hub.Queue(function () { if (mm) {EXT.MathMenu = mm} else {delete EXT.MathMenu} if (mm) {EXT.MathZoom = mz} else {delete EXT.MathZoom} }); })();</script>"
						// enable automatic line breaking
						+ "<script type=\"text/x-mathjax-config\"> MathJax.Hub.Config({ \"HTML-CSS\": { linebreaks: { automatic: true, width: \"75% container\" } }, \"SVG\": { linebreaks: { automatic: true, width: \"75% container\" } }  }); </script>"
						+ "<script type='text/javascript'"
						+ "src='file:///android_asset/MathJax/MathJax.js'"
						+ "></script><span id='math'></span>", "text/html", "utf-8", "");

		questionID = pickRandomQuestion();
		
		populateQuestionAndAnswerFields();
		
		configureButtonsToAnimateWhenPressed();
		
		tStart = System.currentTimeMillis();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}
	
	/**
	 * Picks a questionID of a question that has not already been asked.
	 * 
	 * @return an int representing a question not already asked that is <
	 *         maxQuestionCount
	 */
	private int pickRandomQuestion() {
		//Take a list to store the index of already asked questions
		HashSet<Integer> alreadyAskedQuestions = new HashSet<Integer>();

		alreadyAskedQuestions.addAll(questionsAnsweredThisSet);
		
		Random random = new Random();

		// Get a question that is not already asked
		int randomQuestionID = random.nextInt(maxQuestionCount);
		while (alreadyAskedQuestions.contains(randomQuestionID)) {
		    randomQuestionID = random.nextInt(maxQuestionCount);
		}
		    
		return randomQuestionID;
	}

	/**
	 * Helper method that queries for the current question and answer strings
	 * (using questionID), and then populates the corresponding TextViews.
	 */
	private void populateQuestionAndAnswerFields() {
		TextView question = (TextView) findViewById(R.id.question);
		questionText = getString(QUESTION_STRING_LOOKUP_TABLE[questionID]);
		question.setText(Html.fromHtml(getString(QUESTION_STRING_LOOKUP_TABLE[questionID])));

		// if the question contains a mathematical expression, render it in the WebView using MathJax
        WebView w = (WebView) findViewById(R.id.question_math_formula);
        w.loadUrl("javascript:document.getElementById('math').innerHTML='\\\\["
                  + doubleEscapeTeX(getString(QUESTION_MATH_EXPRESSION_STRING_LOOKUP_TABLE[questionID]))
                          +"\\\\]';");
        w.loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
        
        // make the font larger
        WebSettings s= w.getSettings();
        s.setDefaultFontSize(18);
        // set the webview so that it has the same color as the holo light background
        w.setBackgroundColor(getResources().getColor(R.color.background_holo_light));
        
		// show the WebView after the mathematical formula has rendered
        w.setVisibility(View.VISIBLE);
        
        w.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        
		TextView answer_a_text = (TextView) findViewById(R.id.answer_a_text);
		answer_a_text.setText(Html.fromHtml(getString(ANSWER_A_STRING_LOOKUP_TABLE[questionID])));

		TextView answer_b_text = (TextView) findViewById(R.id.answer_b_text);
		answer_b_text.setText(Html.fromHtml(getString(ANSWER_B_STRING_LOOKUP_TABLE[questionID])));

		TextView answer_c_text = (TextView) findViewById(R.id.answer_c_text);
		answer_c_text.setText(Html.fromHtml(getString(ANSWER_C_STRING_LOOKUP_TABLE[questionID])));

		TextView answer_d_text = (TextView) findViewById(R.id.answer_d_text);
		answer_d_text.setText(Html.fromHtml(getString(ANSWER_D_STRING_LOOKUP_TABLE[questionID])));

		TextView answer_e_text = (TextView) findViewById(R.id.answer_e_text);
		answer_e_text.setText(Html.fromHtml(getString(ANSWER_E_STRING_LOOKUP_TABLE[questionID])));
	}
	
	/**
	 * Sets up all five (A, B, C, D, E) answer buttons such that they turn a
	 * light blue with a thicker, darker blue border 250ms after the user
	 * touches the button.
	 */
	private void configureButtonsToAnimateWhenPressed() {
		final LinearLayout buttonA = (LinearLayout) findViewById(R.id.answer_a_clickable_layout);
		buttonA.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					buttonA.postDelayed(new Runnable() {
						@Override
						public void run() {
							((LinearLayout) v
									.findViewById(R.id.answer_a_clickable_layout))
									.setBackgroundResource(R.drawable.rounded_button_pressed);
						}
					}, 250); //250ms delay before button changes
					break;
				// revert to previous background resource when moving off the
				// button the same way a selector implementation would
				case MotionEvent.ACTION_MOVE:
					Rect r = new Rect();
					v.getLocalVisibleRect(r);
					if (!r.contains((int) event.getX(), (int) event.getY())) {
						((LinearLayout) v
								.findViewById(R.id.answer_a_clickable_layout))
								.setBackgroundResource(R.drawable.rounded_button);
					}
					break;
				case MotionEvent.ACTION_OUTSIDE:
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					((LinearLayout) v
							.findViewById(R.id.answer_a_clickable_layout))
							.setBackgroundResource(R.drawable.rounded_button);
					break;
				}
				return false;
			}
		});

		final LinearLayout buttonB = (LinearLayout) findViewById(R.id.answer_b_clickable_layout);
		buttonB.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					buttonB.postDelayed(new Runnable() {
						@Override
						public void run() {
							((LinearLayout) v
									.findViewById(R.id.answer_b_clickable_layout))
									.setBackgroundResource(R.drawable.rounded_button_pressed);
						}
					}, 250);
					break;
				// revert to previous background resource when moving off the
				// button the same way a selector implementation would
				case MotionEvent.ACTION_MOVE:
					Rect r = new Rect();
					v.getLocalVisibleRect(r);
					if (!r.contains((int) event.getX(), (int) event.getY())) {
						((LinearLayout) v
								.findViewById(R.id.answer_b_clickable_layout))
								.setBackgroundResource(R.drawable.rounded_button);
					}
					break;
				case MotionEvent.ACTION_OUTSIDE:
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					((LinearLayout) v
							.findViewById(R.id.answer_b_clickable_layout))
							.setBackgroundResource(R.drawable.rounded_button);
					break;
				}
				return false;
			}
		});

		final LinearLayout buttonC = (LinearLayout) findViewById(R.id.answer_c_clickable_layout);
		buttonC.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					buttonC.postDelayed(new Runnable() {
						@Override
						public void run() {
							((LinearLayout) v
									.findViewById(R.id.answer_c_clickable_layout))
									.setBackgroundResource(R.drawable.rounded_button_pressed);
						}
					}, 250);
					break;
				// revert to previous background resource when moving off the
				// button the same way a selector implementation would
				case MotionEvent.ACTION_MOVE:
					Rect r = new Rect();
					v.getLocalVisibleRect(r);
					if (!r.contains((int) event.getX(), (int) event.getY())) {
						((LinearLayout) v
								.findViewById(R.id.answer_c_clickable_layout))
								.setBackgroundResource(R.drawable.rounded_button);
					}
					break;
				case MotionEvent.ACTION_OUTSIDE:
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					((LinearLayout) v
							.findViewById(R.id.answer_c_clickable_layout))
							.setBackgroundResource(R.drawable.rounded_button);
					break;
				}
				return false;
			}
		});

		final LinearLayout buttonD = (LinearLayout) findViewById(R.id.answer_d_clickable_layout);
		buttonD.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					buttonD.postDelayed(new Runnable() {
						@Override
						public void run() {
							((LinearLayout) v
									.findViewById(R.id.answer_d_clickable_layout))
									.setBackgroundResource(R.drawable.rounded_button_pressed);
						}
					}, 250);
					break;
				// revert to previous background resource when moving off the
				// button the same way a selector implementation would
				case MotionEvent.ACTION_MOVE:
					Rect r = new Rect();
					v.getLocalVisibleRect(r);
					if (!r.contains((int) event.getX(), (int) event.getY())) {
						((LinearLayout) v
								.findViewById(R.id.answer_d_clickable_layout))
								.setBackgroundResource(R.drawable.rounded_button);
					}
					break;
				case MotionEvent.ACTION_OUTSIDE:
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					((LinearLayout) v
							.findViewById(R.id.answer_d_clickable_layout))
							.setBackgroundResource(R.drawable.rounded_button);
					break;
				}
				return false;
			}
		});
		
		final LinearLayout buttonE = (LinearLayout) findViewById(R.id.answer_e_clickable_layout);
		buttonE.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					buttonE.postDelayed(new Runnable() {
						@Override
						public void run() {
							((LinearLayout) v
									.findViewById(R.id.answer_e_clickable_layout))
									.setBackgroundResource(R.drawable.rounded_button_pressed);
						}
					}, 250);
					break;
				// revert to previous background resource when moving off the
				// button the same way a selector implementation would
				case MotionEvent.ACTION_MOVE:
					Rect r = new Rect();
					v.getLocalVisibleRect(r);
					if (!r.contains((int) event.getX(), (int) event.getY())) {
						((LinearLayout) v
								.findViewById(R.id.answer_e_clickable_layout))
								.setBackgroundResource(R.drawable.rounded_button);
					}
					break;
				case MotionEvent.ACTION_OUTSIDE:
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					((LinearLayout) v
							.findViewById(R.id.answer_e_clickable_layout))
							.setBackgroundResource(R.drawable.rounded_button);
					break;
				}
				return false;
			}
		});
	}
	
	/**
	 * Called when the user touches an answer button. Checks if the answer is
	 * correct and then displays the corresponding alert dialog.
	 * 
	 * @param view
	 *            clickable LinearLayout that was pressed
	 */
	public void onClick(View view) {
		
		tEnd = System.currentTimeMillis();		
		final String secondsElapsed = String.format(Locale.US, "%.1f", (double) (tEnd - tStart)/1000) + "s"; // truncate the secondsElapsed to 1 digit after decimal point (and add "s")
		
	    // retrieve answer from clicked view
		LinearLayout l = (LinearLayout) view;
	    TextView textviewSelected = (TextView) l.getChildAt(1);
	    final String answer = textviewSelected.getText().toString();
	    
		final boolean answerIsCorrect = checkAnswer(answer);
		
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Get the layout inflater
		LayoutInflater inflater = getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    View dialogView = inflater.inflate(R.layout.dialog_answer, null);
	    builder.setView(dialogView);
	    
	    // set the text of the dialog's message; we will display the correct answer here
	    
		TextView textview_correct_answer_message = (TextView) dialogView.findViewById(R.id.textview_correct_answer_message);
		final String correctAnswer = getString(CORRECT_ANSWER_STRING_LOOKUP_TABLE[questionID]);
		textview_correct_answer_message.setText(Html.fromHtml(correctAnswer));
		
		TextView textview_correct_answer_explanation = (TextView) dialogView.findViewById(R.id.textview_correct_answer_explanation);
		final String explanation = getString(ANSWER_EXPLANATION_STRING_LOOKUP_TABLE[questionID]);
		textview_correct_answer_explanation.setText(Html.fromHtml(explanation));

		// 2. Chain together various setter methods to set the dialog characteristics		
		builder.setPositiveButton(R.string.continue_to_next_question, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   // add current questionID to list of questions answered already
        		   Bundle extras = new Bundle();
	        	   questionsAnsweredThisSet.add(questionID);
	        	   extras.putIntegerArrayList("questionsAnsweredThisSet", questionsAnsweredThisSet);
	        	   ArrayList<String> questionInfo = previousQuestionInfo; // formatted as such: {questionText, timeElapsed, Incorrect Answer (or "" if correct)}
	        	   if (answerIsCorrect) questionInfo.addAll(Arrays.asList(questionText, secondsElapsed + "", "", correctAnswer, explanation)); // correctly answered
	        	   else questionInfo.addAll(Arrays.asList(questionText, secondsElapsed + "", answer, correctAnswer, explanation)); // incorrectly answered
	        	   extras.putStringArrayList("previousQuestionInfo", questionInfo);
	        	   
	        	   // User has answered all 10 questions, so go to QuestionSetSummaryActivity.
	        	   if (numberOfQuestionsAsked == 10) {
	        		   final Intent i = new Intent(getBaseContext(), QuestionSetSummary.class);
	        		   i.putExtras(extras);
	        		   startActivity(i);
	        	   }
	        	   // User clicked OK button (and hasn't completed all 10 questions), so continue to next question
	        	   else {
		        	   final Intent i = new Intent(getBaseContext(), QuestionActivity.class);
		        	   extras.putString("numberOfQuestionsAsked", Integer.toString((numberOfQuestionsAsked + 1)));
		        	   i.putExtras(extras);
		        	   startActivity(i);
	        	   }
	           }
	       });

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		
		// retrieve the incorrect answer TextView from the Alert Dialog
	    TextView textview_incorrect_answer_message = (TextView) dialogView.findViewById(R.id.textview_incorrect_answer_message);
	    
		// hide the incorrect answer TextView if correct answer was
		// chosen, otherwise populate the incorrect answer TextView
		if (answerIsCorrect) {
			dialog.setTitle(Html.fromHtml(getString(R.string.title_correct_answer)));
			textview_incorrect_answer_message.setVisibility(View.GONE);
		}
		
		// user answered incorrectly so we want to display the user's incorrect answer in addition to the correct answer
		else {
			dialog.setTitle(Html.fromHtml(getString(R.string.title_incorrect_answer)));
			// determine which button was selected and display its text as the incorrect answer to the question
			switch(textviewSelected.getId())
			{
			case R.id.answer_a_text:
				textview_incorrect_answer_message.setText(Html.fromHtml(getString(ANSWER_A_STRING_LOOKUP_TABLE[questionID])));
			break;
			case R.id.answer_b_text:
				textview_incorrect_answer_message.setText(Html.fromHtml(getString(ANSWER_B_STRING_LOOKUP_TABLE[questionID])));
			break;
			case R.id.answer_c_text:
				textview_incorrect_answer_message.setText(Html.fromHtml(getString(ANSWER_C_STRING_LOOKUP_TABLE[questionID])));
			break;
			case R.id.answer_d_text:
				textview_incorrect_answer_message.setText(Html.fromHtml(getString(ANSWER_D_STRING_LOOKUP_TABLE[questionID])));
			break;
			case R.id.answer_e_text:
				textview_incorrect_answer_message.setText(Html.fromHtml(getString(ANSWER_E_STRING_LOOKUP_TABLE[questionID])));
			break;
			default:
			throw new RuntimeException("Unknown button ID");
			}
		}
		
		dialog.show();
	}	
	
	/**
	 * Helper method that checks if the user correctly answered the question.
	 * 
	 * @param answer
	 *            String representing the answer pressed
	 * @return true if correct answer pressed; false if incorrect answer pressed
	 */
	private boolean checkAnswer(String userAnswer) {
		// use .toString() to remove HTML formatting from the correct answer
		String correctAnswer = getString(CORRECT_ANSWER_STRING_LOOKUP_TABLE[questionID]);
		correctAnswer = android.text.Html.fromHtml(correctAnswer).toString();
		
	    if (correctAnswer.equals(userAnswer))  {
	    	return true;
	    }
	    else  {
	    	return false;
	    }
	} 
	
	/**
	 * Used to look up a random question's string resource, implemented using an
	 * array of ints, where each index represents the R-value of that string.
	 * 
	 * The corresponding look-up tables exist for the correct answer and the
	 * five possible answers.
	 */
	private static final int[] QUESTION_STRING_LOOKUP_TABLE = new int[] {
		   R.string.question0,
		   R.string.question1,
		   R.string.question2,
		   R.string.question3,
		   R.string.question4,
		   R.string.question5,
		   R.string.question6,
		   R.string.question7,
		   R.string.question8,
		   R.string.question9,
		   R.string.question10,
		};
	
	private static final int[] QUESTION_MATH_EXPRESSION_STRING_LOOKUP_TABLE = new int[] {
		   R.string.question0_math_expression,
		   R.string.question1_math_expression,
		   R.string.question2_math_expression,
		   R.string.question3_math_expression, // CHANGED
		   R.string.question4_math_expression,
		   R.string.question5_math_expression,
		   R.string.question6_math_expression,
		   R.string.question7_math_expression,
		   R.string.question8_math_expression,
		   R.string.question9_math_expression,
		   R.string.question10_math_expression,		   
		};
	
//	private static final int[] QUESTION_MATH_LATEX_STRING_LOOKUP_TABLE = new int[] {
//		   R.string.question0_math_expression,
//		   R.string.question1_math_expression,
//		   R.string.question2_math_expression,
//		   R.string.question3_math_latex, // CHANGED
//		   R.string.question4_math_expression,
//		   R.string.question5_math_expression,
//		   R.string.question6_math_expression,
//		   R.string.question7_math_expression,
//		   R.string.question8_math_expression,
//		   R.string.question9_math_expression,
//		   R.string.question10_math_expression,		   
//		};
	
	private static final int[] CORRECT_ANSWER_STRING_LOOKUP_TABLE = new int[] {
		   R.string.question0_correct_answer,
		   R.string.question1_correct_answer,
		   R.string.question2_correct_answer,
		   R.string.question3_correct_answer,
		   R.string.question4_correct_answer,
		   R.string.question5_correct_answer,
		   R.string.question6_correct_answer,
		   R.string.question7_correct_answer,
		   R.string.question8_correct_answer,
		   R.string.question9_correct_answer,
		   R.string.question10_correct_answer,
		};
	
	private static final int[] ANSWER_EXPLANATION_STRING_LOOKUP_TABLE = new int[] {
		   R.string.question0_answer_explanation,
		   R.string.question1_answer_explanation,
		   R.string.question2_answer_explanation,
		   R.string.question3_answer_explanation,
		   R.string.question4_answer_explanation,
		   R.string.question5_answer_explanation,
		   R.string.question6_answer_explanation,
		   R.string.question7_answer_explanation,
		   R.string.question8_answer_explanation,
		   R.string.question9_answer_explanation,
		   R.string.question10_answer_explanation,
		};
	
	private static final int[] ANSWER_A_STRING_LOOKUP_TABLE = new int[] {
		   R.string.question0_answer0,
		   R.string.question1_answer0,
		   R.string.question2_answer0,
		   R.string.question3_answer0,
		   R.string.question4_answer0,
		   R.string.question5_answer0,
		   R.string.question6_answer0,
		   R.string.question7_answer0,
		   R.string.question8_answer0,
		   R.string.question9_answer0,
		   R.string.question10_answer0,
		};
	
	private static final int[] ANSWER_B_STRING_LOOKUP_TABLE = new int[] {
		   R.string.question0_answer1,
		   R.string.question1_answer1,
		   R.string.question2_answer1,
		   R.string.question3_answer1,
		   R.string.question4_answer1,
		   R.string.question5_answer1,
		   R.string.question6_answer1,
		   R.string.question7_answer1,
		   R.string.question8_answer1,
		   R.string.question9_answer1,
		   R.string.question10_answer1,
		};
	
	private static final int[] ANSWER_C_STRING_LOOKUP_TABLE = new int[] {
		   R.string.question0_answer2,
		   R.string.question1_answer2,
		   R.string.question2_answer2,
		   R.string.question3_answer2,
		   R.string.question4_answer2,
		   R.string.question5_answer2,
		   R.string.question6_answer2,
		   R.string.question7_answer2,
		   R.string.question8_answer2,
		   R.string.question9_answer2,
		   R.string.question10_answer2,
		};
	
	private static final int[] ANSWER_D_STRING_LOOKUP_TABLE = new int[] {
		   R.string.question0_answer3,
		   R.string.question1_answer3,
		   R.string.question2_answer3,
		   R.string.question3_answer3,
		   R.string.question4_answer3,
		   R.string.question5_answer3,
		   R.string.question6_answer3,
		   R.string.question7_answer3,
		   R.string.question8_answer3,
		   R.string.question9_answer3,
		   R.string.question10_answer3,
		};
	
	private static final int[] ANSWER_E_STRING_LOOKUP_TABLE = new int[] {
		   R.string.question0_answer4,
		   R.string.question1_answer4,
		   R.string.question2_answer4,
		   R.string.question3_answer4,
		   R.string.question4_answer4,
		   R.string.question5_answer4,
		   R.string.question6_answer4,
		   R.string.question7_answer4,
		   R.string.question8_answer4,
		   R.string.question9_answer4,
		   R.string.question10_answer4,
		};
	
    private String doubleEscapeTeX(String s) {
        String t="";
        for (int i=0; i < s.length(); i++) {
                if (s.charAt(i) == '\'') t += '\\';
                if (s.charAt(i) != '\n') t += s.charAt(i);
                if (s.charAt(i) == '\\') t += "\\";
        }
        return t;
}
}
