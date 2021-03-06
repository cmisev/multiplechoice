package epfl.sweng.entry;

import epfl.sweng.R;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.authentication.IOfflineStateChangedListener;
import epfl.sweng.authentication.SessionManager;
import epfl.sweng.cache.IDoNetworkCommunication;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.globals.Globals;
import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

/**
 * Main Activity of the Application
 *
 */
public class MainActivity extends Activity {

	
	private MainActivity mActivity = this;
	
	/**
	 * Method invoked at the creation of the Activity. 
	 * @param savedInstanceState the saved instance
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        final CheckBox offlineChkBx = (CheckBox) findViewById(R.id.main_checkbox_offline);
        offlineChkBx.setChecked(!SessionManager.getInstance().isOnline());
        SessionManager.getInstance().setOfflineStateChangedListener(new IOfflineStateChangedListener() {
			
			@Override
			public void onOfflineStateChanged(final boolean newOfflineState) {
				mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						offlineChkBx.setChecked(!newOfflineState);
					}
				});
			}
		});
        
        offlineChkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        	@Override
        	public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            	SessionManager.getInstance().setOnlineState(!isChecked, new IDoNetworkCommunication() {
					
					@Override
					public void onSuccess() {
		            	if (isChecked) {
		            		Toast.makeText(mActivity, getString(R.string.you_are_offline), Toast.LENGTH_LONG).show();
		            	} else {
		            		Toast.makeText(mActivity, getString(R.string.you_are_online), Toast.LENGTH_LONG).show();
		            	}
					}
					
					@Override
					public void onError() {
						Toast.makeText(mActivity, 
								getString(R.string.online_transition_error), Toast.LENGTH_LONG).show();
					}
				});
            	
        	}
        });
    }
    
    
	/**
	 * Method invoked at the creation of the Options Menu. 
	 * @param menu the created menu
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == Globals.AUTHENTICATION_REQUEST_CODE) {
    		if (resultCode != RESULT_OK) {
    			System.out.println("Finish MainActivity with result code from AuthenticationActivity: " + resultCode);
    			finish();
    		}
    	}
    }
    
    /**
     * Change view to the ShowQuestionsActivity
     * @param view reference to the menu button
     */
    public void goToDisplayActivity(View view) {
    	Intent showQuestionsActivityIntent = new Intent(this, ShowQuestionsActivity.class);
    	startActivity(showQuestionsActivityIntent);
    }

    /**
     * Change view to the EditQuestionActivity
     * @param view reference to the menu button
     */
    public void goToSubmitActivity(View view) {
    	Intent editQuestionActivityIntent = new Intent(this, EditQuestionActivity.class);
    	startActivity(editQuestionActivityIntent);
    }
    
    /**
     * Change view to the EditQuestionActivity
     * @param view reference to the menu button
     */
    public void goToShowAvailableQuizzesActivity(View view) {
    	Intent showAvailableQuizzesActivityIntent = new Intent(this, ShowAvailableQuizzesActivity.class);
    	startActivity(showAvailableQuizzesActivityIntent);
    }
    
    /**
     * Log out the user
     * @param view reference to the menu button
     */
    public void logout(View view) {
    	SessionManager.getInstance().destroySession();
    	Intent authenticationActivityIntent = new Intent(this, AuthenticationActivity.class);
    	startActivityForResult(authenticationActivityIntent, Globals.AUTHENTICATION_REQUEST_CODE);
    }
    
}
