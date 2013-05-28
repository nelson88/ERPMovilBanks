package com.example.erpmovilbanks.sync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchBuilder implements RequestBuilder {

	static final int LIMIT = 1;
	static final int START = 0;
	static final String SEARCH="";
	static final String DIR="DESC";
	
	private JSONObject requestObject;
	private JSONObject queryFilter;
	private JSONArray conditions;
	private JSONObject summaryFields;
	
	public void clear(){
		requestObject = new JSONObject();
	}
	public void init(int start, String sort, String dir, int limit, String search) throws JSONException {
		clear();
		requestObject.put("start", start);
		requestObject.put("sort", sort);
		requestObject.put("dir", dir);
		requestObject.put("limit", limit);
		requestObject.put("search", search);
	}
	
	public SearchBuilder(String sort) throws JSONException{
		init(START, sort, DIR, LIMIT, SEARCH);
	}
	
	public SearchBuilder(String sort, String search) throws JSONException{
		init(START, sort, DIR, LIMIT, search);
	}
	
	public void setQueryFilter(String table, String schema) throws JSONException{
		queryFilter = new JSONObject();
		queryFilter.put("Table", table);
		queryFilter.put("Schema", schema);
		requestObject.put("queryFilter", queryFilter);
		
		conditions = new JSONArray();
		queryFilter.put("Conditions", conditions);
		
	}
	
	public void setSummaryFields(String tables, String fields) throws JSONException{
		setSummaryFields(tables, new JSONArray(fields));
	}
	
	public void setSummaryFields(String table, JSONArray fields) throws JSONException{
		summaryFields = new JSONObject();
		
		summaryFields.put("Tables", table);
		summaryFields.put("Fields", fields);
		requestObject.put("summaryFields", summaryFields);
	}
	
	public void addConditionWithuriParam(String conditionString) throws JSONException{
		JSONArray array = new JSONArray(conditionString);
		addCondition(array.getString(0), array.getString(1), array.getString(2), array.getString(3), array.getString(4), array.getString(5));
	}
	
	public void addCondition(String table, String field, String operator, Object value, String valueType, String operation) throws JSONException{
		JSONObject condition = new JSONObject();
		condition.put("Table", table);
		condition.put("Field", field);
		condition.put("LogicOperator", operator);
		condition.put("Value", value);
		condition.put("ValueType", valueType);
		condition.put("Operation", operation);
		conditions.put(condition);
		
	}
	
	@Override
	public String getRequestObject() {
		return requestObject.toString();
	}
}
