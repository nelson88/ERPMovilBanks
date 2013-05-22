package com.example.erpmovilbanks;

import com.example.erpmovilbanks.auth.ConstantsAccount;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
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
	
	private Boolean mRequestNewAccount = false;
	private Boolean mConfirmCredentials = false;
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
		loadDefaultValues();
		final SharedPreferences sharedpreferences = getSharedPreferences("authenticator", MODE_PRIVATE);
		
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
					AuthAsyncTask task = new AuthAsyncTask();
					task.execute(mUser, mPassword);
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
	
	class AuthAsyncTask extends AsyncTask<String, Void, String>{
		
		@Override
		protected String doInBackground(String... params) {
			String username = params[0];
			String password = params[1];
			try {
				Log.i("LoginActivity: AsyncTask", "usuario: " + username + " password: " + password);
				
				onAuthenticationResult(AccountNetwork.getInstance(LoginActivity.this).login(username, password)); 
			
            } catch (Exception ex) {
                Log.e("LoginActivity", "UserLoginTask.doInBackground: failed to authenticate");
                Log.i("LoginActivity", ex.toString());
                return null;
            }
			return null;
		}
		
	}
	
	public void onAuthenticationResult(String authToken){
		Log.i("LoginActivity", "authoToken: " + authToken);
		if(authToken != null){
			if(!mConfirmCredentials){
				Log.i("LoginActivity", "entra al if onAuthenticationResult");
				finishLogin(authToken);
			} else {
				Toast.makeText(getApplication(), "", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void finishLogin (String authToken){
		
		SharedPreferences sharedpreferences = getSharedPreferences("authenticator", MODE_PRIVATE);
		final Account account = new Account(mUser, ConstantsAccount.ACCOUNT_TYPE);
		
		if(mRequestNewAccount){
			accountManager.addAccountExplicitly(account, mPassword, null);
			
			Bundle extras = new Bundle();
			extras.putString(AccountManager.KEY_ACCOUNT_NAME, mUser);
			extras.putString(AccountManager.KEY_ACCOUNT_TYPE, authTokenType);
			extras.putString(AccountManager.KEY_AUTHTOKEN, authToken);
			
			setAccountAuthenticatorResult(extras);
			setResult(RESULT_OK);
			
			SharedPreferences.Editor auth = sharedpreferences.edit();
			auth.putBoolean("authenticator", true);
			auth.commit();
		}
	}
	
	public final void setAccountAuthenticatorResult(Bundle result){
		mResultBundle = result;
	}

}
