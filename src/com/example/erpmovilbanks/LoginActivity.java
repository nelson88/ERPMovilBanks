package com.example.erpmovilbanks;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.erpmovilbanks.auth.ConstantsAccount;
import com.example.erpmovilbanks.sync.ERPContract;
import com.example.erpmovilbanks.sync.RESTExecuter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private Boolean mConfirmCredentials = false;
	private static final boolean NOTIFY_AUTH_FAILURE = true;
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
	
	/*class AuthAsyncTask extends AsyncTask<String, Void, String>{
		
		@Override
		protected String doInBackground(String... params) {
			String username = params[0];
			String password = params[1];
			try {
				Log.i("LoginActivity: AsyncTask", "usuario: " + username + " password: " + password);
				
				return AccountNetwork.getInstance(LoginActivity.this).login(mUser, mPassword);
				
            } catch (Exception ex) {
                Log.e("LoginActivity", "UserLoginTask.doInBackground: failed to authenticate");
                Log.i("LoginActivity", ex.toString());
                return null;
            }
		}
		
		protected void onPosExecute(final String authToken){
			Log.i("LoginActivity", "onPostExecute");
			onAuthenticationResult(authToken);
			Log.i("LoginActivity", "onPostExecute return");
		}
		
	}*/
	
	public void onAuthenticationResult(String authToken) throws OperationCanceledException, AuthenticatorException, IOException{
		Log.i("LoginActivity", "authoToken: " + authToken);
		if(authToken != null){
			if(!mConfirmCredentials){
				Log.i("LoginActivity", "entra al if onAuthenticationResult");
				finishLogin(authToken);
				Log.i("LoginActivity", "onAuthenticationResult return del finixhLogin");
				AuthAsyncTaskSync task = new AuthAsyncTaskSync();
				task.execute(authToken);
				
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
			//accountManager.addAccountExplicitly(account, mPassword, null);
			//Log.i("LoginActiviy", "despues de la linea accountManager: " + accountManager.toString());
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
	
	 class AuthAsyncTaskSync extends AsyncTask<String, Void, Void> {

			//ProgressDialog progress;
			
			/*protected void onPreExecute(){
				//progress = ProgressDialog.show(LoginActivity.this, "", "Loading please wait...");
				//progress.setCancelable(true);
			}*/
			
			@Override
			protected Void doInBackground(String... params) {
				String authoken = params[0];
				
				try {
					Log.i("LoginActivity", "doInBackground");
					//final Account account = new Account(mUser, ConstantsAccount.ACCOUNT_TYPE);
					//final String authtToken = accountManager.blockingGetAuthToken(account, ConstantsAccount.AUTHTOKEN_TYPE, NOTIFY_AUTH_FAILURE);
					Log.i("LoginActivity", "authtToken: " + authoken);
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put(ERPContract.SORT_PARAM, "Date");
					paramMap.put(ERPContract.SCHEMA_PARAM, "Core");
					paramMap.put(ERPContract.TABLE_PARAM, "RepositoryExpenseReport");
					
					Log.i("LoginActivity", "paramMap: " + paramMap.toString());
					
					restExecuter.executeRequest(paramMap);
					
	            } catch (Exception ex) {
	                Log.e("LoginActivity", "UserLoginTask.doInBackground: failed to authenticate");
	                Log.i("LoginActivity", ex.toString());
	            }
				return null;
			}
	 }

}
