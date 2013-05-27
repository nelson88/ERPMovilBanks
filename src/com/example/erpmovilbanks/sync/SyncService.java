package com.example.erpmovilbanks.sync;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.example.erpmovilbanks.auth.ConstantsAccount;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


public class SyncService extends Service {

	private static final Object sSyncAdapterLock = new Object();
	private static SyncAdapter sSyncAdapter = null;
	private static final String TAG = "SyncService";
	
	@Override
	public void onCreate() {
		super.onCreate();
		synchronized(sSyncAdapterLock){
			if(sSyncAdapter == null){
				sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
				Log.i(TAG, "sSyncAdapter: " + sSyncAdapter);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, " onBind sSyncAdapter: " + sSyncAdapter);
		return sSyncAdapter.getSyncAdapterBinder();
	}
	
	public class SyncAdapter extends AbstractThreadedSyncAdapter{

		private static final String TAG = "SyncService";
		private AccountManager accountManager;
		private final Context context;
		private static final boolean NOTIFY_AUTH_FAILURE = true;
		private RESTExecuter restExecuter;
		
		public SyncAdapter(Context context, boolean autoInitialize) {
			super(context, autoInitialize);
			this.context = context;
			accountManager = AccountManager.get(context);
			restExecuter = new RESTExecuter(getContext());
		}

		@Override
		public void onPerformSync(Account account, Bundle extras,
				String authority, ContentProviderClient provider,
				SyncResult syncResult) {
			Log.i(TAG, "onPerformSync");
			try {
				final String authtoken = accountManager.blockingGetAuthToken(account, ConstantsAccount.AUTHTOKEN_TYPE, NOTIFY_AUTH_FAILURE);
				
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put(ERPContract.SORT_PARAM, "Date");
				paramMap.put(ERPContract.SCHEMA_PARAM, "Core");
				paramMap.put(ERPContract.TABLE_PARAM, "RepositoryExpenseReport");
				
				Log.i(TAG, "paramMap: " + paramMap.toString());
				
				restExecuter.executeRequest(paramMap);
				
				
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			} catch (AuthenticatorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
