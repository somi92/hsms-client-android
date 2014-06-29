package com.github.somi92.hsms;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class HSMSClient extends Activity implements Runnable {
	
	private static String SOAP_ACTION = "http://192.168.0.15/soap/ActionList/listAllActions";
//	private static String SOAP_ACTION = "http://192.168.0.15/HSMSWebService/index.php/listAllActions";
	
//	private static String NAMESPACE = "http://192.168.0.15/HSMSWebService/index.php/";
	private static String NAMESPACE = "http://192.168.0.15/soap/ActionList/";
//	private static String NAMESPACE = "urn:ActionList/";
	private static String METHOD_NAME = "listAllActions";
	
	private static String URL = "http://192.168.0.15/HSMSWebService/index.php";
	
	private MainActivity parent;
//	private TextView view;
	
	public HSMSClient() {
		
	}
	
	public HSMSClient(MainActivity parent) {
		this.parent = parent;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
//		System.out.println("BBBBBBBBBBBBBBBB");
		
//		Toast.makeText(parent, "Thread", Toast.LENGTH_SHORT).show();
		parent.setMyText("From thread.",0);
		
		SoapObject soapRequest = new SoapObject(NAMESPACE,METHOD_NAME);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.implicitTypes = true;
//		envelope.setAddAdornments(false);
		envelope.setOutputSoapObject(soapRequest);
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//		androidHttpTransport.debug = true;
		
		try {
//			System.setProperty("http.keepAlive", "false");
//			System.setProperty("http.keepAlive", "true");
			androidHttpTransport.call(SOAP_ACTION, envelope);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			parent.setMyText(e.getMessage()+" IO",0);
//			System.out.println("AAAAAAAAAAAAAAAAAAAA");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			parent.setMyText(e.getMessage()+"XML",0);
		} catch (Exception e) {
			// TODO: handle exception
			parent.setMyText(e.getMessage()+" EXC",0);
		}
		
		try {
//			SoapObject result = (SoapObject) envelope.getResponse();
//			Object obj = envelope.bodyIn;
			SoapObject result = (SoapObject)envelope.bodyIn;
			
			if(result != null) {
//				String data = result
//				parent.setMyText("SOAP respones: OK "+result.getProperty(0).toString());
				JSONObject obj = new JSONObject(result.getProperty(0).toString());
//				JSONObject obj2 = obj.getJSONObject("action");
				JSONArray a = obj.getJSONArray("action");
				for(int i=0; i<a.length(); i++) {
					JSONObject jo = (JSONObject) a.get(i);
					parent.setMyText(jo.toString(),(i+1));
				}
//				parent.setMyText(a.toString(),0);
			} else {
				parent.setMyText("SOAP respones: "+"NULL!",0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			parent.setMyText(e.getMessage()+" Erroraaaaaa",0);
		}
//		result = (SoapObject)envelope.bodyIn;
		
	}

}
