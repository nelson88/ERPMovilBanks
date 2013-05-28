package com.example.erpmovilbanks;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences sharedpreferences = getSharedPreferences("authenticator", MODE_PRIVATE);
		boolean auth = sharedpreferences.getBoolean("authenticator", false);
		
		if(auth == false)
		{	
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(intent);
		}
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
