package com.github.somi92.hsms;

import java.io.IOException;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;

public class HSMSClient extends Activity implements Runnable {
	
//	private static String SOAP_ACTION = "http://192.168.1.181/soap/ActionList/listAllActions";
//	private static String NAMESPACE = "http://192.168.1.181/soap/ActionList/";
//	private static String METHOD_NAME = "listAllActions";
//	private static String URL = "http://192.168.1.181/HSMSWebService/index.php";
	
	private static String SOAP_ACTION = "http://www.somi92.student.elab.fon.bg.ac.rs/soap/ActionList/listAllActions";
	private static String NAMESPACE = "http://www.somi92.student.elab.fon.bg.ac.rs/soap/ActionList/";
	private static String METHOD_NAME = "listAllActions";
	private static String URL = "http://www.somi92.student.elab.fon.bg.ac.rs/HSMSWebService/index.php";
	
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
			parent.receiveData(e.getMessage()+" IO");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			parent.receiveData(e.getMessage()+" XML");
		} catch (Exception e) {
			// TODO: handle exception
			parent.receiveData(e.getMessage()+" EXC");
		}
		
		try {
//			SoapObject result = (SoapObject)envelope.bodyIn;
			SoapObject result = null;
			String message = "";
			
			if(envelope.bodyIn instanceof SoapFault) {
				message = ((SoapFault)envelope.bodyIn).faultstring;
			} else {
				result = (SoapObject)envelope.bodyIn;
			}
			
			if(result != null) {
				JSONObject obj = new JSONObject(result.getProperty(0).toString());
//				JSONArray a = obj.getJSONArray("action");
//				for(int i=0; i<a.length(); i++) {
//					JSONObject jo = (JSONObject) a.get(i);
//					parent.setMyText(jo.toString(),(i+1));
//				}
				parent.receiveData(obj.toString());
			} else {
				parent.receiveData("SOAP respones: "+"NULL!"+" "+message);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			parent.receiveData(e.getMessage()+" Error");
		}
	}

}
