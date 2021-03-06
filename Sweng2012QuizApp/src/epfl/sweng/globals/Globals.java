package epfl.sweng.globals;

/**
 * Static global variables
 */
public final class Globals {

	public final static String RANDOM_QUESTION_URL = "https://sweng-quiz.appspot.com/quizquestions/random";
	public final static String SUBMIT_QUESTION_URL = "https://sweng-quiz.appspot.com/quizquestions/";
	public final static String QUIZSERVER_LOGIN_URL = "https://sweng-quiz.appspot.com/login";
	public final static String AUTHSERVER_LOGIN_URL = "https://tequila.epfl.ch/cgi-bin/tequila/login";
	public static final String QUESTION_BY_OWNER_URL = "https://sweng-quiz.appspot.com/quizquestions/ownedby/";
	public static final String QUESTION_BY_TAG_URL = "https://sweng-quiz.appspot.com/quizquestions/tagged/";
	public static final String QUESTION_BY_ID_URL = SUBMIT_QUESTION_URL;
	public static final String QUIZZES_LIST_URL = "https://sweng-quiz.appspot.com/quizzes";
	public static final String QUIZ_BY_ID_URL = "https://sweng-quiz.appspot.com/quizzes/";
	
	
	public final static int MAX_NUMBER_OF_ANSWERS = 10;
	public final static boolean LOG_QUIZSERVER_REQUESTS = true;
	public static final String LOGTAG_QUIZSERVER_COMMUNICATION = "QuizServer";
	public static final boolean LOG_AUTH_REQUESTS = true;
	public static final String LOGTAG_AUTH_COMMUNICATION = "Auth";
	public static final int STATUSCODE_AUTHSUCCESSFUL = 302;
	public static final int STATUSCODE_NOTFOUND = 404;
	public static final int STATUSCODE_UNAUTHORIZED = 401;
	public static final int STATUSCODE_BADREQUEST = 400;
	public static final int STATUSCODE_FORBIDDEN = 403;
	public static final String PREFS_NAME = "user_session";
	public final static int AUTHENTICATION_REQUEST_CODE = 1;
	public static final String SUBMIT_QUIZ_ANSWERS_URL = "https://sweng-quiz.appspot.com/quizzes/%d/submission";
	public static final int STATUSCODE_OK = 200;
	public static final int STATUSCODE_CREATED = 201;
	public static final int STATUSCODE_NOCONTENT = 204;
	public static final String LOGTAG_PROXY_ERROR = "ProxyError";
	public static final int STATUSCODE_SERVERERROR = 500;
	
	
		
}
