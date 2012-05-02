package edu.android;

import edu.shared.Question;
import android.app.Application;

public class GClickerApplication extends Application {
	public ConnectionBundle bundle;
	public Question current_question;
	public String eid;
	public int clicker_id;
	public GClickerAndroidActivity loginActivity;
	private static GClickerApplication singleton;
	

	public static GClickerApplication getInstance(){
		if(singleton == null){
			singleton = new GClickerApplication();
		}
		return singleton;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
	}


}
