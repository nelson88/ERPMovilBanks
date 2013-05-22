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
		private Context c;
		private static AccountNetwork instance;
		private static String TAG = "AccountNetwork";
		

		public AccountNetwork(Context context){
			this.c = context;
		}
		
		public static AccountNetwork getInstance(Context context){
			Log.i("AccountNetwork", " getInstance");
			if(instance == null){
				instance = new AccountNetwork(context.getApplicationContext());
			}
			Log.i("AccountNetwork", "instance: " + instance);
			return instance;
		}
		
		public String login(String mUser, String mPassword){
			try {
				Log.i("AccountNetwork", "context: " + c);
				return authenticate(c, mUser, mPassword);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public static String authenticate(Context context, String mUser, String mPassowrd) throws IOException, XmlPullParserException{
		
			SoapCaller caller = new SoapCaller();
	
			SoapParameter soapCallConfiguration = new SoapParameter(NAMESPACE, METHOD_NAME, SOAP_ACTION);
			soapCallConfiguration.addPropertyInfo("username", mUser);
			soapCallConfiguration.addPropertyInfo("password", mPassowrd);
			soapCallConfiguration.addPropertyInfo("customCredentials", "");
			soapCallConfiguration.addPropertyInfo("isPersistent", true, Boolean.class);
		
			Log.i(TAG, "soapCallConfiguration.addPropertyInfo: " + soapCallConfiguration + " context: " + context);
		
			Object response;
		
			response = caller.request(getServiceURL(context, "/login/AuthenticationService.svc"), soapCallConfiguration);

			Log.i(TAG, "response: " + response);
			
			if(response != null && response.equals("true")){
				Log.i("AccountNetwork", "entro al if");
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
			
				Log.i("AccountNetwork", "sessionId: " + sessionId + " aspxauth: " + aspxauth);
				String authtoken = aspxauth.split("=")[0].replaceAll(" HttpOnly", "")+"="+aspxauth.split("=")[1]+";"+sessionId;
				editor.putString("set-cookies", value);
				editor.putString("authtoken", authtoken);
			
				editor.commit();
			
				Log.i("AccountNetwork", "authToken: " + authtoken);
				return authtoken;
				}
			return null;
			}
	
		public static String getServiceURL(Context context, String service){
			Log.i(TAG, "getServiceURL " + "context: " + context + " service: " + service.toString());
			try {
				return context.getString(R.string.server) + service;
			} catch (Exception e) {
				Log.i("nelson", "exception: " + e);
			}
		return null;
		}
		}
