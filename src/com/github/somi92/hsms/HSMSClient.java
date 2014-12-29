package com.github.somi92.hsms;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;

public class HSMSClient extends Activity implements Runnable {
	
	private String action = "load";
	private int id;
	
	private static String URL = "http://192.168.1.181/HSMS-MS/public/service/listhsms";
//	private static String URL = "http://www.somi92.student.elab.fon.bg.ac.rs/HSMSWebService/listAllActions";

	
	private MainActivity parent;
	
	public HSMSClient() {
		
	}
	
	public HSMSClient(MainActivity parent) {
		this.parent = parent;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public void setAction(String action, int id) {
		this.action = action;
		this.id = id;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(this.action.equals("load"))
			load();
		if(this.action.equals("donate"))
			donate(this.id);
		if(this.action.equals("register"))
			register();
	}
	
	private void load() {
		SharedPreferences prefs = parent.getSharedPreferences("hsms", MODE_PRIVATE);
		String ip = prefs.getString("hostAddress", "192.168.1.181");
		String currency = prefs.getString("currency", "rsd");
		
		URL = "http://"+ip+"/HSMS-MS/public/service/listhsms"+(currency.equals("rsd") ? "" : "/"+currency+":true");
		Log.d("HSMSClient", URL);
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
	
	private void donate(int id) {
		SharedPreferences prefs = parent.getSharedPreferences("hsms", MODE_PRIVATE);
		String ip = prefs.getString("hostAddress", "192.168.1.181");
		String email = prefs.getString("email", "milos@milos.com");
		
		URL = "http://"+ip+"/HSMS-MS/public/service/donate/email:"+email+"/id:"+id;
		Log.d("HSMSClient", URL);
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
//				parent.reportMessage("Donacija uspešno registrovana. Hvala!");
				parent.reportMessage(obj.toString());
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
	
	public void register() {
		SharedPreferences prefs = parent.getSharedPreferences("hsms", MODE_PRIVATE);
		String ip = prefs.getString("hostAddress", "192.168.1.181");
		String email = prefs.getString("email", "milos@milos.com");
		String name = prefs.getString("name", "Milos Stojanovic");
		
		 HttpClient httpClient = new DefaultHttpClient();
         HttpPost post = new HttpPost("http://"+ip+"/HSMS-MS/public/service/registerDonator");
         post.setHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");

         JSONObject dato = new JSONObject();
         
         try {
        	 dato.put("email", email); 
             dato.put("name", name);
             
             StringEntity entity = new StringEntity("email="+email+"&name="+name);
             post.setEntity(entity);
             
             HttpResponse resp = httpClient.execute(post);
             String respStr = EntityUtils.toString(resp.getEntity());
             
             parent.reportMessage(respStr);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private static String getResponseText(InputStream in) {
		Scanner scan = new Scanner(in);
		String s = scan.useDelimiter("\\A").next();
		Log.d("HSMSClient", s);
		scan.close();
	    return s;
	}

}
