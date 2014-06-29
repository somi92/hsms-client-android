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

public class HSMSClient extends Activity implements Runnable {
	
	private static String SOAP_ACTION = "http://192.168.0.15/soap/ActionList/listAllActions";
	private static String NAMESPACE = "http://192.168.0.15/soap/ActionList/";
	private static String METHOD_NAME = "listAllActions";
	private static String URL = "http://192.168.0.15/HSMSWebService/index.php";
	
	private MainActivity parent;
	
	public HSMSClient() {
		
	}
	
	public HSMSClient(MainActivity parent) {
		this.parent = parent;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		SoapObject soapRequest = new SoapObject(NAMESPACE,METHOD_NAME);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.implicitTypes = true;
		envelope.setOutputSoapObject(soapRequest);
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		
		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			parent.setMyText(e.getMessage()+" IO",0);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			parent.setMyText(e.getMessage()+" XML",0);
		} catch (Exception e) {
			// TODO: handle exception
			parent.setMyText(e.getMessage()+" EXC",0);
		}
		
		try {
			SoapObject result = (SoapObject)envelope.bodyIn;
			
			if(result != null) {
				JSONObject obj = new JSONObject(result.getProperty(0).toString());
//				JSONArray a = obj.getJSONArray("action");
//				for(int i=0; i<a.length(); i++) {
//					JSONObject jo = (JSONObject) a.get(i);
//					parent.setMyText(jo.toString(),(i+1));
//				}
				parent.setMyText(obj.toString(),0);
			} else {
				parent.setMyText("SOAP respones: "+"NULL!",0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			parent.setMyText(e.getMessage()+" Error",0);
		}
	}

}
