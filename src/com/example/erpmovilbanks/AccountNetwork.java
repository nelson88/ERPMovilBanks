package com.example.erpmovilbanks;

import java.io.IOException;
import java.util.List;

import org.ksoap2.HeaderProperty;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.erpmovilbanks.http.SoapCaller;
import com.example.erpmovilbanks.http.SoapParameter;

public class AccountNetwork {
	// SOAP Service specific parameters
		private static final String NAMESPACE = "http://asp.net/ApplicationServices/v200";
		private static final String METHOD_NAME = "Login";
		private static final String SOAP_ACTION = "http://asp.net/ApplicationServices/v200/AuthenticationService/Login";
		private static Context context;
		private static String TAG = "AccountNetwork";
		
		public AccountNetwork(Context context){
			this.context = context;
		}
	public static String authenticate(String mUser, String mPassowrd) throws IOException, XmlPullParserException{
		
		SoapCaller caller = new SoapCaller();
	
		SoapParameter soapCallConfiguration = new SoapParameter(
				NAMESPACE, METHOD_NAME, SOAP_ACTION);
		soapCallConfiguration.addPropertyInfo("username", mUser);
		soapCallConfiguration.addPropertyInfo("password", mPassowrd);
		soapCallConfiguration.addPropertyInfo("customCredentials", "");
		soapCallConfiguration.addPropertyInfo("isPersistent", true, Boolean.class);
		
		Log.i(TAG, "soapCallConfiguration.addPropertyInfo: " + soapCallConfiguration + "context: " + context);
		
		Object response;
		
		String res = getServiceURL(context, "/login/AuthenticationService.svc");
		Log.i(TAG, "RES: " + res);
		
		response = caller.request(res, soapCallConfiguration);

		Log.i(TAG, "response: " + response);
		if(response != null && response.equals("true")){
			List<HeaderProperty> responseHeaders = caller.getResponseHeaders();
			
			SharedPreferences settings = context.getSharedPreferences("ERP_PREFERENCES", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			String value = "";
			Log.i(TAG, "SharedPreferences: ");
			for(HeaderProperty hp : responseHeaders){
				if(hp.getKey() != null && hp.getKey().equalsIgnoreCase("set-cookie"))
					value += hp.getValue();
			}
			
			String sessionId = value.split(";")[0];
			String aspxauth = value.split(";")[2];
			
			String authtoken = aspxauth.split("=")[0].replaceAll(" HttpOnly", "")+"="+aspxauth.split("=")[1]+";"+sessionId;
			editor.putString("set-cookies", value);
			editor.putString("authtoken", authtoken);
			
			editor.commit();
			
			return authtoken;
		}
		
		return null;
	}
	
	public static String getServiceURL(Context context, String service){
		Log.i(TAG, "getServiceURL " + "context: " + context + " service: " + service.toString());
		return context.getString(R.string.server) + service;
		
	}

}
