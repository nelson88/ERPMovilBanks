package com.example.erpmovilbanks.sync;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.erpmovilbanks.AccountNetwork;

public class RESTExecuter {
	public static final String DEFAULT_ENCODING="UTF-8";
	private Context context;
	protected HttpCaller restHttpCall;
	private static final String TAG = "RESTExecuter";
	
	public RESTExecuter(Context context){
		this.context = context;
		
		restHttpCall = new HttpCaller();

	}
	public int executeRequest(Map<String, Object> parameters) throws JSONException, UnsupportedEncodingException{
		
		JSONObject jsonResponse = null;
		
		RequestBuilder requestBuilder = null;
		
		Log.i(TAG, "executeRequest");
		if(parameters.containsKey(ERPContract.SEARCH_PARAM)){
			requestBuilder = new SearchBuilder((String) parameters.get(ERPContract.SORT_PARAM), (String) parameters.get(ERPContract.SEARCH_PARAM));
		} else {
			requestBuilder = new SearchBuilder((String) parameters.get(ERPContract.SORT_PARAM));
		}
		
		Log.i(TAG, "despues del primer if");
		if(parameters.containsKey(ERPContract.SCHEMA_PARAM)){
			((SearchBuilder) requestBuilder).setQueryFilter((String) parameters.get(ERPContract.TABLE_PARAM), (String) parameters.get(ERPContract.SCHEMA_PARAM));
		}
		
		Log.i(TAG, "despues del segundo if");
		if(parameters.containsKey(ERPContract.SUMMARY_FIELDS)){
			((SearchBuilder) requestBuilder).setSummaryFields((String) parameters.get(ERPContract.TABLE_PARAM),
					URLDecoder.decode((String) parameters.get(ERPContract.SUMMARY_FIELDS), DEFAULT_ENCODING));
		}
		
		Log.i(TAG, "despues del tercer if");
		if(parameters.containsKey(ERPContract.CONDITIONS)){
			((SearchBuilder) requestBuilder).addConditionWithuriParam(URLDecoder.decode((String) parameters.get(ERPContract.CONDITIONS), DEFAULT_ENCODING));
		} else if(parameters.containsKey(ERPContract.PLATFORM_NAME) && parameters.containsKey(ERPContract.MESSAGE_NAME)){
			ExecuteRequest request = new ExecuteRequest(parameters.get(ERPContract.PLATFORM_NAME), parameters.get(ERPContract.MESSAGE_NAME));
			requestBuilder = new ExecuteBuilder(request);
			
		}
		
		String url = null;
		Log.i(TAG, "despues del cuarto if");
		url = AccountNetwork.getServiceURL(getContext(), "/advancedsearch/svc/ERPService.svc/RetrieveMultiple");
		jsonResponse = restHttpCall.request(url, getAuthCookie(), requestBuilder.getRequestObject());
		
		return 0;
		
	}
	
	public Context getContext(){
		return context;
	}
	
	public String getAuthCookie(){
		SharedPreferences settings = context.getSharedPreferences("ERP_PREFERENCES", Context.MODE_PRIVATE);
		String authtoken = settings.getString("authtoken", "");
		
		return authtoken;
	}
}
