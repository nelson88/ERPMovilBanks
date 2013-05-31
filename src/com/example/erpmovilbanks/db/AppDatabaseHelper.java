package com.example.erpmovilbanks.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabaseHelper extends SQLiteOpenHelper {

	final static int VERSION = 1;
	final static String DATABASENAME = "dbmovilbanks";
	
	public AppDatabaseHelper(Context context) {
		super(context, DATABASENAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table movilbanks (_ID integer primary key autoincrement, BankAccount varchar(200) not null, InitialBalance FLOAT not null, FinalBalance FLOAT not null)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
