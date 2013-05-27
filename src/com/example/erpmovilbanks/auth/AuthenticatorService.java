package com.example.erpmovilbanks.auth;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.example.erpmovilbanks.AccountNetwork;
import com.example.erpmovilbanks.LoginActivity;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class AuthenticatorService extends Service {

	private AccountAuthenticator authenticator;
	private Context context;
	
	public AuthenticatorService(){
		Log.i("AuthenticatorService", "authenticator");
		authenticator = new AccountAuthenticator(this);

	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return authenticator.getIBinder();
	}
	
	public class AccountAuthenticator extends AbstractAccountAuthenticator{

		private Context c;
		public AccountAuthenticator(Context context) {
			super(context);
			this.c = context;
			
		}

		@Override
		public Bundle addAccount(AccountAuthenticatorResponse response,
				String accountType, String authTokenType,
				String[] requiredFeatures, Bundle options)
				throws NetworkErrorException {
			Log.i("AuthenticatorService", "addAccount: ");
			Bundle extras = new Bundle();
			
			Intent intent = new Intent(c, LoginActivity.class);
			intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
			extras.putParcelable(AccountManager.KEY_INTENT, intent);
			return null;
		}

		@Override
		public Bundle confirmCredentials(AccountAuthenticatorResponse response,
				Account account, Bundle options) throws NetworkErrorException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Bundle editProperties(AccountAuthenticatorResponse response,
				String accountType) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Bundle getAuthToken(AccountAuthenticatorResponse response,
				Account account, String authTokenType, Bundle options)
				throws NetworkErrorException {
			Log.i("Auth", "getAuthToken");
			if(!authTokenType.equals(ConstantsAccount.AUTHTOKEN_TYPE))
			{
				final Bundle result = new Bundle();
				result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
				
				return result;
			}
			
			final AccountManager am = AccountManager.get(context);
			final String password = am.getPassword(account);
			if(password != null){
				try {
					String authToken = AccountNetwork.authenticate(context, account.name, password);
					
					if(authToken != null){
						final Bundle result = new Bundle();
						result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
						result.putString(AccountManager.KEY_ACCOUNT_TYPE, ConstantsAccount.ACCOUNT_TYPE);
						result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
						
						am.setAuthToken(account, ConstantsAccount.ACCOUNT_TYPE, authToken);
						
						return result;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			final Intent intent = new Intent(context, AuthenticatorService.class);
			intent.putExtra("username", account.name);
			intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
			
			final Bundle bundle = new Bundle();
			bundle.putParcelable(AccountManager.KEY_INTENT, intent);
			return bundle;
		}

		@Override
		public String getAuthTokenLabel(String authTokenType) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Bundle hasFeatures(AccountAuthenticatorResponse response,
				Account account, String[] features)
				throws NetworkErrorException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Bundle updateCredentials(AccountAuthenticatorResponse response,
				Account account, String authTokenType, Bundle options)
				throws NetworkErrorException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
