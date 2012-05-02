package edu.android;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import edu.shared.Answer;
import edu.shared.Question;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

class ConnectToServerTask extends AsyncTask<String, Void, ConnectionBundle> {
	private static int PORT = 3000;
	
	
	private ConnectionBundle bundle;
	
	
	/**
	 * connectionParams[0]: eid
	 * connectionParams[1]: server ip
	 */
	@Override
	protected ConnectionBundle doInBackground(String... connectionParams) {
		try {
			
			String eid = connectionParams[0];
			String serverIp = connectionParams[1];
			
			//disable connect button
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("My eid is " + eid);
			System.out.println("Connecting to " + serverIp);
			
			Socket socket = new Socket(serverIp, ConnectToServerTask.PORT);
			
			ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));			
			ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			bundle = new ConnectionBundle(input, output, socket, null);
			
			Answer dummyAnswer = new Answer(0, Integer.MAX_VALUE, eid);
			output.writeObject(dummyAnswer);
			//prevent InputStream on the server from blocking
			output.flush();

			System.out.println("Waiting for clicker id.");
			Question question = (Question) input.readObject();
			GClickerApplication app = GClickerApplication.getInstance();
			
			if(question.message == true){
				int clicker_id = question.clicker_id;
				app.clicker_id = clicker_id;
				System.out.println("Clicker id assigned by server: " + clicker_id);
			}

			socket.setSoTimeout(0);
			
			app.bundle = bundle;
			app.eid = eid;
			
			return bundle;
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Couldn't connect to server");			
			return new ConnectionBundle(null, null, null, "Failed to connect to server: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return new ConnectionBundle(null, null, null, "Failed to connect to server: " + e.getMessage());
		} finally{
			
		}
	}

	protected void onProgressUpdate(Integer... progress) {
		
	}

	/**
	 * Runs on UI thread after we connected or failed to connect
	 */
	protected void onPostExecute(ConnectionBundle bundle) {
		System.out.println("### connected to server");
		GClickerApplication app = GClickerApplication.getInstance();
		if(bundle.error != null){
			//show error
			app.loginActivity.showError(bundle.error);
		} else {
			//launch QuestionActivity
			System.out.println(app.loginActivity);
			app.loginActivity.showQuestionActivity();
		}
		
		Button connectButton = (Button)app.loginActivity.findViewById(R.id.button_connect);
		connectButton.setEnabled(true);
	}


}