package com.example.erpmovilbanks.view;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.erpmovilbanks.LoginActivity;
import com.example.erpmovilbanks.R;
import com.example.erpmovilbanks.sync.ERPContract;
import com.example.erpmovilbanks.sync.RESTExecuter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class banks_listview extends Activity {

	ArrayList<String> items = new ArrayList<String>();
	private RESTExecuter restExecuter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.banks_listview);
		
		restExecuter = new RESTExecuter(getBaseContext());
		
		Bundle bundle = getIntent().getExtras();
		String authToken = bundle.getString("authToken");
		Log.i("banks_listview", "authToken: " + authToken);
		AuthAsyncTaskSync task = new AuthAsyncTaskSync();
		task.execute(authToken);

	}
	
	class AuthAsyncTaskSync extends AsyncTask<String, Void, ArrayList> {
		
		@Override
		protected ArrayList doInBackground(String... params) {
			String authoken = params[0];
			JSONObject json = new JSONObject();
			
			try {
				Log.i("LoginActivity", "doInBackground");
				//final Account account = new Account(mUser, ConstantsAccount.ACCOUNT_TYPE);
				//final String authtToken = accountManager.blockingGetAuthToken(account, ConstantsAccount.AUTHTOKEN_TYPE, NOTIFY_AUTH_FAILURE);
				Log.i("LoginActivity", "authtToken: " + authoken);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put(ERPContract.SORT_PARAM, "DailyDate");
				paramMap.put(ERPContract.SCHEMA_PARAM, "Bank");
				paramMap.put(ERPContract.TABLE_PARAM, "DailyBalance");
				
				Log.i("LoginActivity", "paramMap: " + paramMap.toString());
				
				json = restExecuter.executeRequest(paramMap);
				
				JSONArray Jarray = json.getJSONArray("data");
				
				for(int i=0; i<Jarray.length(); i++){
					JSONObject object = Jarray.getJSONObject(i);
					
					String bank_account = object.getString("BankAccount");
					Double initial_balance = object.getDouble("InitialBalance");
					Double kinal_balance = object.getDouble("FinalBalance");
					
					items.add(bank_account + "\n" + initial_balance + "\t" + kinal_balance);
					
					Log.v("--", "BankAccount:" + bank_account + "\n InitialBalance: " + initial_balance
	                        + "\n  FinalBalance: " + kinal_balance);
					}
				Log.i("LoginActivity", "despues del for");
				} catch (Exception ex) {
                Log.e("LoginActivity", "UserLoginTask.doInBackground: failed to authenticate");
                Log.i("LoginActivity", ex.toString());
            }
			
			
			
			return items;
		}
		
		protected void onPostExecute(ArrayList items){
			Log.i("LoginActivity", "onPostExecute");
			try{
				ListView myListView = (ListView) findViewById(R.id.banks_listview);
			
				Log.i("LoginActivity", "onPostExecute item: " + items.toString());
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, items);
				myListView.setAdapter(adapter);
			} catch (Exception e){
				Log.i("LoginActivity", "Exception: " + e);
			}

		}
		
 }
	

}
