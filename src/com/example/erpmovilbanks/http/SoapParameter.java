package com.example.erpmovilbanks.http;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.PropertyInfo;

import android.R.string;


public class SoapParameter {
	
	private String nameSpace;
	private String methodName;
	private String soapAction;
	private List<PropertyInfo> propertiesInfo;
	
	public SoapParameter(String nameSpace, String methodName, String soapAction){
		
		this.nameSpace = nameSpace;
		this.methodName = methodName;
		this.soapAction = soapAction;
		propertiesInfo = new ArrayList<PropertyInfo>();
	}
	
	public void addPropertyInfo(String name, Object value){
		addPropertyInfo(name, value, string.class);
	}
	
	public void addPropertyInfo(String name, Object value, Class claz){
		PropertyInfo propertyinfo = new PropertyInfo();
		
		propertyinfo.setName(name);
		propertyinfo.setValue(value);
		propertyinfo.setType(claz);
		propertiesInfo.add(propertyinfo);
	}
	
	public String getNameSpace(){
		return nameSpace;
	}
	
	public String getMetodName(){
		return methodName;
	}
	
	public List<PropertyInfo> getPropertyInfo(){
		return propertiesInfo;
	}
	
	public String getSoapAction(){
		return soapAction;
	}
}
