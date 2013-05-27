package com.example.erpmovilbanks.sync;

public class ExecuteBuilder implements RequestBuilder {

	ObjectWrapper request;
	
	public ExecuteBuilder(Object request){
		this.request = new ObjectWrapper(request);
	}
	
	public class ObjectWrapper<T>{
		T request;
		
		public ObjectWrapper(T request){
			this.request = request;
		}
	}
	
	@Override
	public String getRequestObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
