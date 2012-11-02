package epfl.sweng.test;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.mocking.MockHttpClient;
import epfl.sweng.test.tools.TestingTricks;

/**
 * Testing the authentication activity
 */
public class AuthenticationActivityTest extends
		ActivityInstrumentationTestCase2<AuthenticationActivity> {

	
	private Solo solo;
	
	public AuthenticationActivityTest() {
		super(AuthenticationActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
        SwengHttpClientFactory.setInstance(new MockHttpClient());
		solo = new Solo(getInstrumentation(), getActivity());
	}

	/* Begin list of the different tests to be performed */

	public void testSuccessfulAuthentication() {
		solo.assertCurrentActivity("Authentication form is being displayed",
				AuthenticationActivity.class);

		assertTrue(solo.searchText("Please login"));
		
		TestingTricks.authenticateMe(solo);
		assertTrue(solo.waitForText("Authentication successful"));
	}
	
	public void testFailedAuthentication() {

		assertTrue(solo.searchText("Please login"));

		TestingTricks.authenticateMeBadly(solo);
		assertTrue(solo.waitForText("Authentication failed"));
	}
	

	/* End list of the different tests to be performed */
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

}