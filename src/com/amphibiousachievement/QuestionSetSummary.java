package com.amphibiousachievement;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionSetSummary extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_set_summary);
		
		setText();

	}
	
	private void setText() {
		// questionText, secondsElapsed + "", answer, correctAnswer, explanation
		Bundle extras = this.getIntent().getExtras();
		if ( extras != null ) {
		  if (extras.containsKey("previousQuestionInfo")) {
			  ArrayList<String> previousQuestionInfo = extras.getStringArrayList("previousQuestionInfo");
			  System.out.println("previousQuestionInfo:" + previousQuestionInfo);
			  // Question 1
			  TextView Question1_Problem = (TextView) findViewById(R.id.Question1_Problem);
			  String Question1_ProblemText = truncateStringIfNecessary("this is twenty nine chars lon");
			  Question1_Problem.setText(Question1_ProblemText);
//			  Question1_Problem.setText(previousQuestionInfo.get(0));
			  TextView Question1_Time = (TextView) findViewById(R.id.Question1_Time);
			  Question1_Time.setText(previousQuestionInfo.get(1));
			  TextView Question1_IncorrectAnswer = (TextView) findViewById(R.id.Question1_IncorrectAnswer);
			  String Question1_IncorrectAnswerGiven = previousQuestionInfo.get(2);
			  if ((Question1_IncorrectAnswerGiven).equals("")) { // user correctly answered the question, so hide incorrect answer TextView and change coloBar from red to green
				  Question1_IncorrectAnswer.setVisibility(View.INVISIBLE);
				  TextView Question1_ColorBar = (TextView) findViewById(R.id.Question1_ColorBar);
				  Question1_ColorBar.setBackgroundColor(getResources().getColor(R.color.green));
			  }
			  else { 
				  Question1_IncorrectAnswer.setText(Question1_IncorrectAnswerGiven);
			  }
			  TextView Question1_CorrectAnswer = (TextView) findViewById(R.id.Question1_CorrectAnswer);
			  Question1_CorrectAnswer.setText(previousQuestionInfo.get(3));
			  TextView Question1_Explanation = (TextView) findViewById(R.id.Question1_Explanation);
			  Question1_Explanation.setText(previousQuestionInfo.get(4));
			  
			  // Question 2
			  TextView Question2_Problem = (TextView) findViewById(R.id.Question2_Problem);
//			  Question2_Problem.setText(previousQuestionInfo.get(5));
			  String Question2_ProblemText = truncateStringIfNecessary("this is thirty nine chars long");
			  Question2_Problem.setText(Question2_ProblemText);

			  TextView Question2_Time = (TextView) findViewById(R.id.Question2_Time);
			  Question2_Time.setText(previousQuestionInfo.get(6));
			  TextView Question2_IncorrectAnswer = (TextView) findViewById(R.id.Question2_IncorrectAnswer);
			  String Question2_IncorrectAnswerGiven = previousQuestionInfo.get(7);
			  if ((Question2_IncorrectAnswerGiven).equals("")) { // user correctly answered the question, so hide incorrect answer TextView and change coloBar from red to green
				  Question2_IncorrectAnswer.setVisibility(View.INVISIBLE);
				  TextView Question2_ColorBar = (TextView) findViewById(R.id.Question2_ColorBar);
				  Question2_ColorBar.setBackgroundColor(getResources().getColor(R.color.green));
			  }
			  else { 
				  Question2_IncorrectAnswer.setText(Question2_IncorrectAnswerGiven);
			  }
			  TextView Question2_CorrectAnswer = (TextView) findViewById(R.id.Question2_CorrectAnswer);
			  Question2_CorrectAnswer.setText(previousQuestionInfo.get(8));
			  TextView Question2_Explanation = (TextView) findViewById(R.id.Question2_Explanation);
			  Question2_Explanation.setText(previousQuestionInfo.get(9));
			  
//			  // Question 3
//			  TextView Question3_Problem = (TextView) findViewById(R.id.Question3_Problem);
//			  Question3_Problem.setText(previousQuestionInfo.get(10)); // set Question 2 Text
//			  TextView Question3_Time = (TextView) findViewById(R.id.Question3_Time);
//			  Question3_Time.setText(previousQuestionInfo.get(11)); // set Question 2 Time
//			  TextView Question3_IncorrectAnswer = (TextView) findViewById(R.id.Question3_IncorrectAnswer);
//			  String Question3_IncorrectAnswerGiven = previousQuestionInfo.get(12);
//			  if ((Question3_IncorrectAnswerGiven).equals("")) { // user correctly answered the question, so hide incorrect answer TextView and change coloBar from red to green
//				  Question3_IncorrectAnswer.setVisibility(View.INVISIBLE);
//				  TextView Question3_ColorBar = (TextView) findViewById(R.id.Question3_ColorBar);
//				  Question3_ColorBar.setBackgroundColor(getResources().getColor(R.color.green));
//			  }
//			  else { 
//				  Question3_IncorrectAnswer.setText(Question3_IncorrectAnswerGiven);
//			  }
//			  TextView Question3_CorrectAnswer = (TextView) findViewById(R.id.Question3_CorrectAnswer);
//			  Question3_CorrectAnswer.setText(previousQuestionInfo.get(13));
//			  TextView Question3_Explanation = (TextView) findViewById(R.id.Question3_Explanation);
//			  Question3_Explanation.setText(previousQuestionInfo.get(14));
		  }
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question_set_summary, menu);
		return true;
	}
	
	public void onClick(View view) {	
	    // TODO Auto-generated method stub
	    switch(view.getId())
	    {
	    case R.id.Question1_Container :
	        Toast.makeText(this,"button1", Toast.LENGTH_LONG).show();
	        break;
	    case R.id.Question2_Container :
	        Toast.makeText(this,"button2", Toast.LENGTH_LONG).show();
	        break;
	    case R.id.Question3_Container :
	        Toast.makeText(this,"button3", Toast.LENGTH_LONG).show();
	        break;  
	    case R.id.Question4_Container :
	        Toast.makeText(this,"button4", Toast.LENGTH_LONG).show();
	        break;
	    case R.id.Question5_Container :
	        Toast.makeText(this,"button5", Toast.LENGTH_LONG).show();
	        break;
	    case R.id.Question6_Container :
	        Toast.makeText(this,"button6", Toast.LENGTH_LONG).show();
	        break;  
	    }
	}
	
	/**
	 * Formats a question's problem text for the display by truncating string if
	 * necessary. If a stirng is less than 30 chars, then it is not truncated.
	 * Otherwise, the string is truncated to 28 chars, and then further
	 * truncated if necessary so that it does not truncate in the middle of a
	 * word. Ellipses are added to the end of a truncated string.
	 * 
	 * @param s
	 *            A string which needs to be tested for length and truncated if
	 *            necessary.
	 * @return the truncated string (never truncated in the middle of the word)
	 *         with ellipses at end
	 */
	private String truncateStringIfNecessary(String s) {
		if (s.length() < 30) return s;
		else {
			s = s.substring(0, 28);
			s = s.substring(0 ,s.lastIndexOf(" ")); // don't truncate in the middle of a word
			s = s + "...";
			return s;
		}
	}
}