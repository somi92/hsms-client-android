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
		
<<<<<<< HEAD
		SoapObject soapRequest = new SoapObject(NAMESPACE,METHOD_NAME);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.implicitTypes = true;
		envelope.setOutputSoapObject(soapRequest);
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		androidHttpTransport.debug = true;
		
		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			parent.receiveData(e.getMessage()+" IO");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			parent.receiveData(e.getMessage()+" XML");
		} catch (Exception e) {
			// TODO: handle exception
			parent.receiveData(e.getMessage()+" EXC");
		}
=======
		HttpURLConnection conn = null;
		JSONObject obj = null;
>>>>>>> 520372c933e67aef28f3acebf7ddc6bdefe01a3f
		
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
			
<<<<<<< HEAD
			Log.e("dump Request: " ,androidHttpTransport.requestDump);
			Log.e("dump response: " ,androidHttpTransport.responseDump);
			
=======
		} catch(SocketTimeoutException e) {
			parent.reportError("Greška. Konekcija je istekla. Poruka sistema: "+e.getMessage());
		} catch(IOException e) {
			parent.reportError("Greška. Poruka sistema: "+e.getMessage());
>>>>>>> 520372c933e67aef28f3acebf7ddc6bdefe01a3f
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
