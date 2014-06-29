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
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static String SOAP_ACTION = "http://192.168.0.15/soap/ActionList/listAllActions";
//	private static String SOAP_ACTION = "http://192.168.0.15/HSMSWebService/index.php/listAllActions";
	
//	private static String NAMESPACE = "http://192.168.0.15/HSMSWebService/index.php/";
	private static String NAMESPACE = "http://192.168.0.15/soap/ActionList/";
//	private static String NAMESPACE = "urn:ActionList/";
	private static String METHOD_NAME = "listAllActions";
	
	private static String URL = "http://192.168.0.15/HSMSWebService/index.php";
	
//	private MainActivity parent;
//	private TextView view;

	private TextView t;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		t = (TextView) findViewById(R.id.txtView);
		t.setText("start");
		
		
		HSMSClient client = new HSMSClient(this);
		Thread thr = new Thread(client);
		thr.start();
//		
//		t.setText("a");
		
//		new Thread() {
//			public void run() {
//				runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						
//						// TODO Auto-generated method stub
//						setMyText("From thread.");
//						
//						SoapObject soapRequest = new SoapObject(NAMESPACE,METHOD_NAME);
//						
//						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//						envelope.implicitTypes = true;
////						envelope.setAddAdornments(false);
//						envelope.setOutputSoapObject(soapRequest);
//						
//						HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
////						androidHttpTransport.debug = true;
//						
//						try {
//							androidHttpTransport.call(SOAP_ACTION, envelope);
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//							setMyText(e.getMessage()+" IO");
//						} catch (XmlPullParserException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//							setMyText(e.getMessage()+"XML");
//						} catch (Exception e) {
//							// TODO: handle exception
//							setMyText(e.getMessage()+" EXC");
//						}
//						
//						try {
//							SoapObject result = (SoapObject)envelope.bodyIn;
//							
//							if(result != null) {
////								String data = result
////								parent.setMyText("SOAP respones: OK "+result.getProperty(0).toString());
//								JSONObject obj = new JSONObject(result.getProperty(0).toString());
////								JSONObject obj2 = obj.getJSONObject("action");
//								JSONArray a = obj.getJSONArray("action");
//								for(int i=0; i<a.length(); i++) {
//									JSONObject jo = (JSONObject) a.get(i);
//									setMyText(getMyText()+" **"+(i+1)+"** "+jo.toString());
//								}
////								parent.setMyText(obj.getString(""));
//							} else {
//								setMyText("SOAP respones: "+"NULL!");
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//							setMyText(e.getMessage()+" Erroraaaaaa");
//						}
//					}
//				});
//			}
//		}.start();
//		setMyText("sranje");
	}
	
	public void setMyText(final String text, final int i) {
		new Thread() {
			public void run() {
				MainActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						t.setText(getMyText()+" **"+(i+1)+"** "+text);
					}
				});
			}
		}.start();	
	}
	
	public String getMyText() {
		return t.getText().toString();
	}
}
