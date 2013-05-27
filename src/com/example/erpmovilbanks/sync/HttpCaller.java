package com.example.erpmovilbanks.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.util.Log;

public class HttpCaller {

	private static final String TAG = "HttpCaller";
	
	public JSONObject request(String url, String cookieString, String jsonParameter){
		
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
		HttpConnectionParams.setSoTimeout(httpParameters, 10000);
		
		JSONObject json = null;
		Log.i(TAG, "request");
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(url);
		Log.i(TAG, "despues del post");
		try {
			StringEntity se = new StringEntity(jsonParameter, "UTF-8");
			httpPost.setEntity(se);
			httpPost.setHeader("User-Agent", getUserAgent());
			httpPost.setHeader("Cookie", cookieString);
			httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
			
			HttpResponse response = httpClient.execute(httpPost);
			Log.i(TAG, "response: " + response);
			HttpEntity entity = response.getEntity();
			Log.i(TAG, "entity: " + entity);
			if(entity != null){
				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
				StringBuilder total = new StringBuilder();
				String line;
				while((line = reader.readLine()) != null){
					total.append(line);
				}
				
				json = new JSONObject(total.toString());
				Log.i(TAG, "json: " + json.toString());
			} 
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return json;
	}
	
	public static String getUserAgent(){
		String USER_AGENT_PREFIX = "Android";
		return String.format(USER_AGENT_PREFIX + "-" + Build.VERSION.SDK_INT);
	}
}
