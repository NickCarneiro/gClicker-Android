package edu.android;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionBundle {
	public ObjectInputStream input;
	public ObjectOutputStream output;
	public Socket socket;
	public String error;
	public ConnectionBundle(ObjectInputStream input, ObjectOutputStream output, Socket socket, String error){
		this.input = input;
		this.output = output;
		this.socket = socket;
		this.error = error;
	}
	
}
