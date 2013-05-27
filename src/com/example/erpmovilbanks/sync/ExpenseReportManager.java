package com.example.erpmovilbanks.sync;

import android.content.Context;

public class ExpenseReportManager {
	private static ExpenseReportManager instance;
	Context context;
	
	public static ExpenseReportManager getIstance(Context context){
		if(instance == null){
			instance = new ExpenseReportManager();
		}
		instance.context = context;
		return instance;
	}

}
