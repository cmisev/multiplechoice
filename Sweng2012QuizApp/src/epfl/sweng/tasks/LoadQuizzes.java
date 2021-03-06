package epfl.sweng.tasks;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;

import epfl.sweng.globals.Globals;
import epfl.sweng.quizzes.Quiz;
import epfl.sweng.tasks.interfaces.IQuizServerCallback;
import epfl.sweng.tasks.interfaces.IQuizzesReceivedCallback;

/**
 * QuizServerTask realization that fetches a random Question
 */
public class LoadQuizzes extends QuizServerTask {
    
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public LoadQuizzes(final IQuizzesReceivedCallback callback) {
		super(new IQuizServerCallback() {
			
			@Override
			public void onSuccess(HttpResponse response) {
				List<Quiz> quizzes = new ArrayList<Quiz>();
				try {
					JSONArray quizzesJSON = getJSONArray(response);
					for (int i=0; i<quizzesJSON.length(); i++) {
						quizzes.add(new Quiz(quizzesJSON.getJSONObject(i)));
					}
				} catch (JSONException e) {
					onError();
					return;
				} catch (ParseException e) {
					onError();
					return;
				} catch (IOException e) {
					onError();
					return;
				}
				callback.onSuccess(quizzes);
			}
			
			@Override
			public void onError() {
				callback.onError();
			}
		});
	}
	
	/**
	 * Method fetching the random question
	 * @param url (optional) an alternative url for the QuizServer "fetch random question location
	 */
	@Override
	protected HttpResponse doInBackground(Object... urls) {
    	String url = "";
		if (urls.length == 0) {
			url = Globals.QUIZZES_LIST_URL;
		} else {
			url = (String) urls[0];
		}
		
		return handleQuizServerRequest(new HttpGet(url));
	}
	
}
