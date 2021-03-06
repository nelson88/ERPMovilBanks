package com.example.erpmovilbanks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.erpmovilbanks.auth.ConstantsAccount;
import com.example.erpmovilbanks.sync.ERPContract;
import com.example.erpmovilbanks.sync.RESTExecuter;
import com.example.erpmovilbanks.view.banks_listview;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private Boolean mConfirmCredentials = false;
	private RESTExecuter restExecuter;
	private AccountManager accountManager;
	private String authTokenType;
	private Bundle mResultBundle = null;
	EditText user;
	EditText password;
	String mUser = "";
	String mPassword = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		accountManager = AccountManager.get(this);
		authTokenType = ConstantsAccount.ACCOUNT_TYPE;
		loadDefaultValues();
		final SharedPreferences sharedpreferences = getSharedPreferences("authenticator", MODE_PRIVATE);
		restExecuter = new RESTExecuter(getBaseContext());
		
		Button Blogin = (Button) findViewById(R.id.Blogin);
		
		Blogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mUser = user.getText().toString();
				mPassword = password.getText().toString();
				Log.i("LoginActivity", "mUser: " + mUser + " mPassword: " + mPassword);
				
				if(TextUtils.isEmpty(mUser) || TextUtils.isEmpty(mPassword))
				{
					Toast.makeText(getApplication(), R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
				} 
				
				else {
					Log.i("LoginActivity", "onClick:");
					new AccountNetwork(LoginActivity.this).login(mUser, mPassword); 
					/*AuthAsyncTask task = new AuthAsyncTask();
					task.execute(mUser, mPassword);*/
				} 
			}
			
		});
	}
	
	protected void loadDefaultValues() {

		user = (EditText) findViewById(R.id.txtuser);
		password = (EditText) findViewById(R.id.txtpassword);

		user.setText("mperez");
		password.setText("mperez");

	}
	
	public void onAuthenticationResult(String authToken) throws OperationCanceledException, AuthenticatorException, IOException{
		Log.i("LoginActivity", "authoToken: " + authToken);
		if(authToken != null){
			if(!mConfirmCredentials){
				Log.i("LoginActivity", "entra al if onAuthenticationResult");
				finishLogin(authToken);
				Log.i("LoginActivity", "onAuthenticationResult return del finixhLogin");
				Intent intent = new Intent(LoginActivity.this, banks_listview.class);
				intent.putExtra("authToken", authToken);
				startActivity(intent);
				
				
			} else {
				Toast.makeText(getApplication(), "", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void finishLogin (String authToken){
		
		SharedPreferences sharedpreferences = getSharedPreferences("authenticator", MODE_PRIVATE);
		final Account account = new Account(mUser, ConstantsAccount.ACCOUNT_TYPE);
		Log.i("LoginActivity", "account: " + account.toString());
		Log.i("LoginActivity", "mPassword: " + mPassword);
		accountManager.setPassword(account, mPassword);
		Log.i("LoginActiviy", "entra al finishLogin");
			Bundle extras = new Bundle();
			extras.putString(AccountManager.KEY_ACCOUNT_NAME, mUser);
			extras.putString(AccountManager.KEY_ACCOUNT_TYPE, authTokenType);
			extras.putString(AccountManager.KEY_AUTHTOKEN, authToken);
			
			setAccountAuthenticatorResult(extras);
			Log.i("LoginActivity", "extras: " + extras.toString());
			setResult(RESULT_OK);
			
			/*SharedPreferences.Editor auth = sharedpreferences.edit();
			
			auth.putBoolean("authenticator", true);
			auth.commit();
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);*/
	}
	
	public final void setAccountAuthenticatorResult(Bundle result){
		mResultBundle = result;
	}
}
