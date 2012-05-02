package edu.android;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GClickerAndroidActivity extends Activity {
	/** Called when the activity is first created. */
	GClickerAndroidActivity reference;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// Capture our button from layout
		Button button = (Button)findViewById(R.id.button_connect);
		// Register the onClick listener with the implementation above
		button.setOnClickListener(connectListener);
		//initialize global singleton for sharing state between activities
		
		GClickerApplication app = GClickerApplication.getInstance();
		app.loginActivity = this;
	}

	private OnClickListener connectListener = new OnClickListener() {
		public void onClick(View v) {
			

			//TODO: hide connect button, show a spinner, launch service and wait for it to connect.

			TextView welcomeText = (TextView)findViewById(R.id.textView_welcome);
			welcomeText.setText("Connecting to server...");
			//launch question activity
			//read input eid and ip from ui
			EditText eidText = (EditText)findViewById(R.id.editText_Eid);
			String eid = eidText.getText().toString();

			EditText ipText = (EditText)findViewById(R.id.editText_Ip);


			
			//disable connect button
			Button connectButton = (Button)findViewById(R.id.button_connect);
			connectButton.setEnabled(false);
			
			//params are eid and ip strings
			ConnectToServerTask connect = new ConnectToServerTask();
			connect.execute(eid, ipText.getText().toString());


		}
	};

	public void showQuestionActivity(){
		Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		startActivity(intent);
	}
	
	public void showError(String error){
		TextView welcomeText = (TextView)findViewById(R.id.textView_welcome);
		welcomeText.setText(error);
	}
}