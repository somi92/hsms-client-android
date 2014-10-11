package com.github.somi92.hsms;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

public class HSMSClient extends Activity implements Runnable {
	
//	private static String URL = "http://192.168.1.181/HSMSWebService/listAllActions";
	private static String URL = "http://www.somi92.student.elab.fon.bg.ac.rs/HSMSWebService/listAllActions";

	
	private MainActivity parent;
	
	public HSMSClient() {
		
	}
	
	public HSMSClient(MainActivity parent) {
		this.parent = parent;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		HttpURLConnection conn = null;
		JSONObject obj = null;
		
		try {
			URL url = new URL(URL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			
			int statusCode = conn.getResponseCode();
			if(statusCode != HttpURLConnection.HTTP_OK) {
				parent.reportError("Greška. Aplikacija ne može da preuzme podatke. HTTP odgovor: "+statusCode);
			} else {
				InputStream in = new BufferedInputStream(conn.getInputStream());
				obj = new JSONObject(getResponseText(in));
				parent.receiveData(obj.toString());
			}
			
		} catch(SocketTimeoutException e) {
			parent.reportError("Greška. Konekcija je istekla. Poruka sistema: "+e.getMessage());
		} catch(IOException e) {
			parent.reportError("Greška. Poruka sistema: "+e.getMessage());
		} catch (Exception e) {
			parent.reportError("Greška. Poruka sistema: "+e.getMessage());
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}
	}
	
	private static String getResponseText(InputStream in) {
		Scanner scan = new Scanner(in);
		String s = scan.useDelimiter("\\A").next();
		scan.close();
	    return s;
	}

}
