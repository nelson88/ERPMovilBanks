package com.example.erpmovilbanks.http;

import java.io.IOException;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class SoapCaller {

	private static String TAG = "SoapCaller";
	private List responseHeaders;
	
	public Object request(String url, SoapParameter configuration){
		Log.i(TAG, "request "  + " url: " + url + " configuration: " + configuration.toString());
		SoapObject request = new SoapObject(configuration.getNameSpace(), configuration.getMetodName());
		
		for(PropertyInfo propertyInfo : configuration.getPropertyInfo())
			request.addProperty(propertyInfo);
		
		SoapSerializationEnvelope enveloped = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		enveloped.dotNet = true;
		
		enveloped.setOutputSoapObject(request);
		
		HttpTransportSE androidHttpTransportSE = new HttpTransportSE(url);
		
		try {
			responseHeaders = androidHttpTransportSE.call(configuration.getSoapAction(), enveloped, null);
			Log.i("SoapCaller", "responseHeaders: " + responseHeaders);
		} catch (IOException e) {
			Log.i("SoapCaller", "IOException e: " + e);
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			Log.i("SoapCaller", "XmlPullParserException e: " + e);
			e.printStackTrace();
		}
		
		try {
			Object response = enveloped.getResponse();
			
			Log.i("SoapCaller", "response: " + response.toString());
			return response.toString();
		} catch (SoapFault e) {
			Log.i("SoapCaller", "SoapFault e: " + e);
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List getResponseHeaders(){
		return responseHeaders;
	}
	
}
