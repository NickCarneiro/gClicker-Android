package edu.android;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.InetAddress;

import edu.shared.Answer;
import edu.shared.Question;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class QuestionActivity extends Activity {
	public static int MAX_ANSWERS;
	private GClickerApplication app;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		app = GClickerApplication.getInstance();
		setContentView(R.layout.question);
		// Capture our button from layout
		Button button = (Button)findViewById(R.id.button_send);
		// Register the onClick listener with the implementation above
		button.setOnClickListener(sendListener);
		button.setEnabled(false);
		//update textview at the top with server ip that we're connected to

		TextView serverIp = (TextView)findViewById(R.id.textView_serverIp);
		GClickerApplication app = GClickerApplication.getInstance();
		serverIp.setText("Connected to " + app.bundle.socket.getInetAddress().getHostAddress() + ", id: " + app.clicker_id);

		//run asynctask to wait for questions
		WaitForQuestion wait = new WaitForQuestion();
		wait.execute();
	}

	private class WaitForQuestion extends AsyncTask<Void, Void, Question>{

		@Override
		protected Question doInBackground(Void... nothing) {
			try {
				ObjectInputStream input = GClickerApplication.getInstance().bundle.input;
				Question question;

				question = (Question) input.readObject();
				app.current_question = question;
				//got a question from the wire. let the UI have it
				return question;


			} catch (OptionalDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Question question){
			TextView questionView = (TextView)findViewById(R.id.textView_question);
			if(question == null){
				questionView.setText("Question was null.");
				return;
			}
			System.out.println(question);
			//display question... 
			questionView.setText(question.getQuestionText());
			//...and render answer choices
			String[] answers = question.getAnswers();

			//get reference to radiogroup
			RadioGroup answerGroup = (RadioGroup)findViewById(R.id.radioGroup_answers);

			//hide all answers
			for(int i = 0; i < answerGroup.getChildCount(); i++){
				RadioButton thisRadio = (RadioButton)answerGroup.getChildAt(i);
				thisRadio.setVisibility(RadioButton.GONE);
			}

			//set text and show new answers
			for(int i = 0; i < answers.length; i++){
				RadioButton thisRadio = (RadioButton)answerGroup.getChildAt(i);
				char letter = (char) ('A' + i);
				thisRadio.setText(letter + ": " + answers[i]);
				thisRadio.setVisibility(RadioButton.VISIBLE);
			}
			Button sendButton = (Button)findViewById(R.id.button_send);
			sendButton.setEnabled(true);


		}

	}

	//Send button listener
	private OnClickListener sendListener = new OnClickListener() {
		public void onClick(View v) {
			// do something when the button is clicked
			System.out.println("send button pressed");
			//read answer choice from ui

			RadioGroup answerGroup = (RadioGroup)findViewById(R.id.radioGroup_answers);
			//answer is an integer 0-4 representing A-E
			
			
			RadioButton checkedButton = (RadioButton)findViewById(answerGroup.getCheckedRadioButtonId());
			int choice = answerGroup.indexOfChild(checkedButton);
			System.out.println("Answer chosen: " + choice);
			GClickerApplication app = GClickerApplication.getInstance();
			
			Answer answer = new Answer(choice, app.current_question.id, app.eid);
			
			SendAnswerTask sendTask = new SendAnswerTask();
			sendTask.execute(answer);
			
		}
	};


	private class SendAnswerTask extends AsyncTask<Answer, Void, String>{

		@Override
		protected String doInBackground(Answer... params) {
			Answer answer = params[0];
			ObjectOutputStream output = app.bundle.output;

			try {
				output.writeObject(answer);
				output.flush();
				return "Sent answer";
			} catch (IOException e) {
				//show error message
				e.printStackTrace();
				return "Error sending answer: " + e.getMessage();
				
			}
			
		}
		
		protected void onPostExecute(String sendResult){
			TextView questionView = (TextView)findViewById(R.id.textView_question);
			questionView.setText(sendResult);
		}

	}
}
