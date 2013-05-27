package com.example.erpmovilbanks.sync;

public class ExecuteRequest {
	Object MessageName;
	Object PlataformName;
	Object Parameters;
	
	public ExecuteRequest(Object PlatformName, Object MessageName){
		this.PlataformName = PlatformName;
		this.MessageName = MessageName;
		
		this.Parameters = new Object();
	}
}
