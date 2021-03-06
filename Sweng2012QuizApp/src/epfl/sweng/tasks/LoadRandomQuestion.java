package epfl.sweng.tasks;


import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;

import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tasks.interfaces.IQuestionPersonalRatingReloadedCallback;
import epfl.sweng.tasks.interfaces.IQuestionRatingReloadedCallback;
import epfl.sweng.tasks.interfaces.IQuizQuestionReceivedCallback;
import epfl.sweng.tasks.interfaces.IQuizServerCallback;

/**
 * QuizServerTask realization that fetches a random Question
 */
public class LoadRandomQuestion extends QuizServerTask {
    
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public LoadRandomQuestion(final IQuizQuestionReceivedCallback callback) {
		super(new IQuizServerCallback() {
			
			@Override
			public void onSuccess(HttpResponse response) {
				QuizQuestion question;
				try {
					question = new QuizQuestion(getJSONObject(response));
					
					callback.onQuestionSuccess(question);
					
					new ReloadQuestionRating(new IQuestionRatingReloadedCallback() {
						
						@Override
						public void onReloadedSuccess(QuizQuestion question) {
							callback.onRatingSuccess(question);
							new ReloadPersonalRating(new IQuestionPersonalRatingReloadedCallback() {
								
								@Override
								public void onReloadedSuccess(QuizQuestion question) {
									callback.onRatingSuccess(question);
								}
								
								@Override
								public void onError() {
									callback.onRatingError();
								}
							}, question).execute();
						}
						
						@Override
						public void onError() {
							callback.onRatingError();
						}
					}, question).execute();
					
				} catch (JSONException e) {
					onError();
				} catch (ParseException e) {
					onError();
				} catch (IOException e) {
					onError();
				}
			}
			
			@Override
			public void onError() {
				callback.onQuestionError();
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
			url = Globals.RANDOM_QUESTION_URL;
		} else {
			url = (String) urls[0];
		}
		
		return handleQuizServerRequest(new HttpGet(url));
	}
	
}
