package com.example.erpmovilbanks.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class contentProvider extends ContentProvider {

	private AppDatabaseHelper database;
	public static final int BANKS = 30;
	public static final int BANKS_ID = 31;
	
	private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		static{
			uriMatcher.addURI(Contract.AUTHORITY, Contract.BASE_PATH, BANKS);
			uriMatcher.addURI(Contract.AUTHORITY, Contract.BASE_PATH + "/*", BANKS_ID);
		}
		
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.i("MainActivity", "insert");
		int uriId = uriMatcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		
		long id = 0;
		Uri uriInserted = null;
		Log.i("MainActivity", "insert");
		switch(uriId){
		case BANKS:
		case BANKS_ID:
			id = db.insert("movilbanks", null, values);
			uriInserted = Contract.CONTENT_URI;
			
		break;
		default:
			throw new IllegalStateException("Unknown uri " + uri);
		}
		Log.i("MainActivity", "insert");
		return uriInserted.buildUpon().appendPath(String.valueOf(id)).build();
	}

	@Override
	public boolean onCreate() {
		database = new AppDatabaseHelper(getContext());

		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		int uriId = uriMatcher.match(uri);
		
		switch(uriId){
		case BANKS:
			builder.setTables("movilbanks");
			break;
		case BANKS_ID:
			builder.setTables("movilbanks");
			builder.appendWhere("_Id = " + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalStateException("Content cannot be resolved " + uri.toString());
		}
		
		SQLiteDatabase db = database.getReadableDatabase();
		Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
