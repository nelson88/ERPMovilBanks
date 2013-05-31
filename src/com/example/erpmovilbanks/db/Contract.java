package com.example.erpmovilbanks.db;

import android.content.ContentResolver;
import android.net.Uri;

public class Contract {
	
public static final String AUTHORITY = "com.sample.erpmovilbanks";
	
	public static final String BASE_PATH = "movilbanks";
	
	public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY +"/" + BASE_PATH);
	
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/movilbanks";
	
	public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/movilbanks";

}
