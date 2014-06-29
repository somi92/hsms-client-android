package com.github.somi92.hsms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String data;
	private JSONObject[] actionsArray;
	private TextView t;
	private ListView actionsListView;
	private SimpleAdapter myAdapter;
	private List<Map<String, String>> actionsList;
	private ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		t = (TextView) findViewById(R.id.txtView);
		
//		actionsListView = (ListView) findViewById(R.id.actionView);
//		actionsList = new ArrayList<Map<String, String>>();
//		populateActionsList();
//		
//		myAdapter = new SimpleAdapter(this, actionsList, R.layout.list_item, new String[] {"desc","num","price","org","web"}, new int[] {R.id.description, R.id.num_box, R.id.price, R.id.organization, R.id.website});
//		actionsListView.setAdapter(myAdapter);
		
		progress = new ProgressDialog(this);
		progress.setTitle("Učitavanje podataka...");
		
		HSMSClient client = new HSMSClient(this);
		Thread thr = new Thread(client);
		progress.show();
		thr.start();
	}
	
	private void setData(final String data) {
		this.data = data;
	}
	
	private void parseJSONData() {
		try {
			JSONObject main = new JSONObject(this.data);
			if(main != null) {
				JSONArray array = main.getJSONArray("action");
				if(array != null) {
					actionsArray = new JSONObject[array.length()];
					for(int i=0; i<array.length(); i++) {
						actionsArray[i] = array.getJSONObject(i); 
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void populateActionsList() {
		if(actionsArray == null) {
			return;
		}
		for(JSONObject obj : actionsArray) {
			actionsList.add(createAction(obj));
		}
	}

	private HashMap<String, String> createAction(JSONObject obj) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			String desc = obj.getString("desc");
			String num = obj.getString("num");
			String price = obj.getString("price");
			String org = obj.getString("org");
			String web = obj.getString("web");
			
			map.put("desc", desc);
			map.put("num", num);
			map.put("price", price);
			map.put("org", org);
			map.put("web", web);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return map;
	}
	
	public void receiveData(final String text) {
		new Thread() {
			public void run() {
				MainActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						t.setText(text);
						setData(text);
						parseJSONData();
						initializeList();
					}
				});
			}
		}.start();	
	}
	
	public String getMyText() {
		return t.getText().toString();
	}
	
	private void initializeList() {
		actionsListView = (ListView) findViewById(R.id.actionView);
		actionsList = new ArrayList<Map<String, String>>();
		populateActionsList();
		
		myAdapter = new SimpleAdapter(this, actionsList, R.layout.list_item, new String[] {"desc","num","price","org","web"}, new int[] {R.id.description, R.id.num_box, R.id.price, R.id.organization, R.id.website});
		actionsListView.setAdapter(myAdapter);
		progress.dismiss();
		
		actionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				try {
					Toast.makeText(MainActivity.this, actionsArray[arg2].getString("num")+" "+actionsArray[arg2].getString("price")+" - "+actionsArray[arg2].getString("desc"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
