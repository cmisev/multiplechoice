package epfl.sweng.authentication;

import android.content.SharedPreferences;
import epfl.sweng.cache.CacheManager;
import epfl.sweng.cache.IDoNetworkCommunication;
import epfl.sweng.tasks.AuthenticationTask;
import epfl.sweng.tasks.interfaces.IAuthenticationCallback;


/**
 * Simple singleton data structure holding session ID and taking care of authentication
 */
final public class SessionManager {
	
	private static SessionManager mInstance  = new SessionManager();
	private SharedPreferences mSettings;
	private boolean mIsOnline;
	private IOfflineOnErrorCallback mOfflineOnErrorCallback;
	private IOfflineStateChangedListener mOfflineStateChangedListener;
	
	
	
	/**
	 * Single constructor declared private to ensure Singleton behaviour
	 */
	private SessionManager() {
		mIsOnline = true;
		mOfflineStateChangedListener = new IOfflineStateChangedListener() {
			
			@Override
			public void onOfflineStateChanged(boolean newOfflineState) {
			}
		};
	}

	/**
	 * Accessor method giving access to the single instance of the SessionManager
	 * @return
	 */
	public static SessionManager getInstance() {
		return mInstance;
	}
	
	/**
	 * Public method fetching the settings from the shared preferences database of the application
	 */
	public void setSettings(SharedPreferences settings) {
		mSettings = settings;
	}
	
	/**
	 * Create a session by authenticating a user towards Tequila
	 * @param callback specifies the methods to be invoked after the authentication process
	 * @param username the Tequila user name to authenticate
	 * @param password the password of the Tequila user
	 */
	public void authenticate(final ISessionCreationCallback callback, String username, String password) {
		
		new AuthenticationTask(new IAuthenticationCallback() {
			@Override
			public void onSuccess(String sessionId) {
				mSettings.edit().putString("SESSION_ID", sessionId).commit();
				callback.onSessionCreateSuccess();
			}

			@Override
			public void onError(Exception except) {
				callback.onSessionCreateError();
			}
		}).execute(username, password);
		
	}
	
	/**
	 * Log out user by destroying the session
	 */
	public void destroySession() {
		mSettings.edit().remove("SESSION_ID").commit();
	}

	/**
	 * Accessor method to get the current session id 
	 * @return the session id
	 */
	public String getSessionId() {
		return mSettings.getString("SESSION_ID", "");
	}
	
	/**
	 * Test whether the user is authenticated or not
	 * @return true if authenticated, false if not
	 */
	public boolean isAuthenticated() {
		return !getSessionId().equals("");
	}
	
	
	public boolean isOnline() {
		return mIsOnline;
	}

	public void setOnlineState(boolean b, final IDoNetworkCommunication callback) {
		if (b == isOnline()) {
			return;
		} else {
			mIsOnline = b;
			if (b) {
				CacheManager.getInstance().doNetworkCommunication(new IDoNetworkCommunication() {
					
					@Override
					public void onSuccess() {
						callback.onSuccess();
						mOfflineStateChangedListener.onOfflineStateChanged(true);
					}
					
					@Override
					public void onError() {
						callback.onError();
						mIsOnline = false;
						mOfflineStateChangedListener.onOfflineStateChanged(false);
					}
				});
			} else {
				callback.onSuccess();
			}
		}
		mOfflineStateChangedListener.onOfflineStateChanged(mIsOnline);
	}

	public void notifyOfflineOnError() {
		mOfflineOnErrorCallback.onSessionWentOffline();
	}

	public void setOfflineOnErrorCallback(IOfflineOnErrorCallback callback) {
		mOfflineOnErrorCallback = callback;
	}
	
	public void setOfflineStateChangedListener(IOfflineStateChangedListener callback) {
		mOfflineStateChangedListener = callback;
	}
}
	