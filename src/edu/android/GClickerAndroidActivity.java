package edu.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GClickerAndroidActivity extends Activity {
    /** Called when the activity is first created. */
	Context appContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        appContext = getApplicationContext();
     // Capture our button from layout
        Button button = (Button)findViewById(R.id.button_connect);
        // Register the onClick listener with the implementation above
        button.setOnClickListener(connectListener);
        
    }
    
    private OnClickListener connectListener = new OnClickListener() {
        public void onClick(View v) {
          // do something when the button is clicked
        	System.out.println("button pressed");
        	
        	//TODO: hide connect button, show a spinner, launch service and wait for it to connect.
        	
        	//launch question activity
        	
        	Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
        	//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
        	appContext.startActivity(intent);
        }
    };
}