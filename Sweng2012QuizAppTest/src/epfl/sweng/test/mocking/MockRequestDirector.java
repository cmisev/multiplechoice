package epfl.sweng.test.mocking;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.globals.Globals;

import android.util.Log;

/**
 * The Mocking RequestDirector pretending answer to HTTPRequest
 * like if it were the real web server. But it ain't, it's just a teapot.
 *
 * @see http://tools.ietf.org/html/rfc2324
 */
class MockRequestDirector implements RequestDirector {
    private static final int STATUSCODE_INVALIDCREDENTIALS = 400;
    private static final int STATUSCODE_OK = 200;
    private static final String STATUSMESSAGE_OK = "OK";
    private static final int STATUSCODE_NOTFOUND = 404;
    private static final int STATUSCODE_NOCONTENT = 204;
    private static final int STATUSCODE_CREATED = 201;
    private static final String STATUSMESSAGE_NOTFOUND = "OK";
    private static final String STATUSMESSAGE_NOCONTENT = "NO CONTENT!";
	private static final int LIKE_COUNT = 1;
	private static final int DISLIKE_COUNT = 3;
	private static final int INCORRECT_COUNT = 2;
    private static int verdict = 1;
	
    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request,
            HttpContext context) throws HttpException, IOException {
        // In this method, you may examine the request and construct a
        // response.

        // You could, for example, log the request
        
    	String requestUri = request.getRequestLine().getUri();
    	
    	Log.i("TEAPOT", "Teapot received request: "
                + request.getRequestLine().toString());
        HttpResponse resp = new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_NOTFOUND, STATUSMESSAGE_NOTFOUND);
        if (requestUri.equals(Globals.RANDOM_QUESTION_URL) || requestUri.equals(Globals.SUBMIT_QUESTION_URL)) {
        	resp = quizServer();
        } else if (requestUri.equals(Globals.AUTHSERVER_LOGIN_URL)) {
        	resp = tequilaServer(request);
        } else if (requestUri.equals(Globals.QUIZSERVER_LOGIN_URL)) {
        	resp = quizServerLogin(request);
        	
        } else if (requestUri.endsWith("rating") && request instanceof HttpPost) {
        	resp = quizServerRatingCreated(request);
        } else if (requestUri.endsWith("rating") && request instanceof HttpGet && verdict == 1) {
        	resp = quizServerRatingLike(request);
        } else if (requestUri.endsWith("rating") && request instanceof HttpGet && verdict == 0) {
        	resp = quizServerRatingDislike(request);
        } else if (requestUri.endsWith("rating") && request instanceof HttpGet && verdict == -1) {
        	resp = quizServerRatingIncorrect(request);        	
        } else if (requestUri.endsWith("rating") && requestUri.matches("newUser")) {
        	resp = quizServerRatingNoContent(request);
        } else if (requestUri.endsWith("rating") && requestUri.matches("submittingUser")) {
        	resp = quizServerRatingCreated(request);
        } else if (requestUri.endsWith("rating")) {
        	resp = quizServerRatingLike(request);
        } else if (requestUri.endsWith("ratings")) {
        	resp = quizServerRatings(request);
        }
        return resp;
    }

	
    private HttpResponse quizServerRatings(HttpRequest request) {
		BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_OK, STATUSMESSAGE_OK);
		response.setHeader("Content-type", "application/json");
		JSONObject jsonResponse = new JSONObject();

		try {
			jsonResponse.put("likeCount", LIKE_COUNT);
			jsonResponse.put("dislikeCount", DISLIKE_COUNT);
			jsonResponse.put("incorrectCount", INCORRECT_COUNT);
			
			response.setEntity(new StringEntity(jsonResponse.toString()));
		} catch (UnsupportedEncodingException e) {
		} catch (JSONException e) {					
		}
		return response;
	}


	private HttpResponse quizServerRatingLike(HttpRequest request) {
		BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_OK, STATUSMESSAGE_OK);
		response.setHeader("Content-type", "application/json");
		JSONObject jsonResponse = new JSONObject();
		try {
			jsonResponse.put("verdict", "like");
			response.setEntity(new StringEntity(jsonResponse.toString()));
		} catch (UnsupportedEncodingException e) {
		} catch (JSONException e) {					
		}
		return response;
	}

	private HttpResponse quizServerRatingDislike(HttpRequest request) {
		BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_OK, STATUSMESSAGE_OK);
		response.setHeader("Content-type", "application/json");
		JSONObject jsonResponse = new JSONObject();

		try {
			jsonResponse.put("verdict", "dislike");
			response.setEntity(new StringEntity(jsonResponse.toString()));
		} catch (UnsupportedEncodingException e) {
		} catch (JSONException e) {					
		}
		return response;
	}

	private HttpResponse quizServerRatingIncorrect(HttpRequest request) {
		BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_OK, STATUSMESSAGE_OK);
		response.setHeader("Content-type", "application/json");
		JSONObject jsonResponse = new JSONObject();

		try {
			jsonResponse.put("verdict", "incorrect");
			response.setEntity(new StringEntity(jsonResponse.toString()));
		} catch (UnsupportedEncodingException e) {
		} catch (JSONException e) {					
		}
		return response;
	}


	private HttpResponse quizServerRatingNoContent(HttpRequest request) {
		BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
				STATUSCODE_NOCONTENT,
				STATUSMESSAGE_NOCONTENT);
		response.setHeader("Content-type", "application/json");
		JSONObject jsonResponse = new JSONObject();

		try {
			jsonResponse.put("message", "Teapot rating =D !");
			response.setEntity(new StringEntity(jsonResponse.toString()));
		} catch (UnsupportedEncodingException e) {
		} catch (JSONException e) {					
		}
		return response;
	}
	


	private HttpResponse quizServerRatingCreated(HttpRequest request) {
		BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_CREATED, STATUSMESSAGE_OK);
		JSONObject json;
		try {
			json = new JSONObject(EntityUtils.toString(((HttpPost) request).getEntity()));
			if (json.get("verdict").equals("like")) {
				verdict=1;
			} else if (json.get("verdict").equals("dislike")) {
				verdict=0;
			} else if (json.get("verdict").equals("incorrect")) {
				verdict=-1;
			}
		} catch (ParseException e) {
		} catch (JSONException e) {
		} catch (IOException e) {
		}

		return response;
	}

	
	private HttpResponse quizServer() {
    	
    	BasicHttpResponse resp = new BasicHttpResponse(
                HttpVersion.HTTP_1_1, STATUSCODE_OK, STATUSMESSAGE_OK);
        resp.addHeader("Content-Type", "application/json; charset=utf-8");
        try {
            resp.setEntity(new StringEntity("{"
            		+ "\"tags\": ["
            		+ "\"capitals\", "
            		+ "\"geography\", "
            		+ "\"countries\" ], "
            		+ "\"solutionIndex\": 3, "
            		+ "\"question\": \"What is the capital of Niger?\", "
            		+ "\"answers\": ["
            		+ "\"Riga\", "
            		+ "\"Madrid\", "
            		+ "\"Vienna\", "
            		+ "\"Niamey\" ], "
            		+ "\"owner\": \"sehaag\"," 
            		+ "\"id\": 16026 }", "utf-8"));
        } catch (UnsupportedEncodingException uee) {
            resp = null;
        }
        return resp;
    }
    
    private HttpResponse tequilaServer(HttpRequest request) throws IOException {
    	HttpPost post = (HttpPost) request;
    	List<NameValuePair> params = URLEncodedUtils.parse(post.getEntity());
    	
    	String username = "";
    	
    	for (NameValuePair param : params) {
    		if (param.getName().equals("username")) {
    			username = param.getValue();
    		} 
    	}
    	
    	if (username.equals("valid")) {
    		return new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_AUTHSUCCESSFUL, "Found");
    	} else {
    		return new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_INVALIDCREDENTIALS, "Invalid Credentials");	
    	} 
    	
    }
    
    private HttpResponse quizServerLogin(HttpRequest request) {
    	
    	if (request.getRequestLine().getMethod().equals("GET")) {
    		BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_OK, STATUSMESSAGE_OK);
    		response.setHeader("Content-type", "application/json");
    		JSONObject jsonResponse = new JSONObject();

    		try {
    			jsonResponse.put("token", "rqtvk5d3za2x6ocak1a41dsmywogrdlv5");
				response.setEntity(new StringEntity(jsonResponse.toString()));
    		} catch (UnsupportedEncodingException e) {
			} catch (JSONException e) {					
			}
    		return response;
    	} else {
    		BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_OK, STATUSMESSAGE_OK);
    		response.setHeader("Content-type", "application/json");
    		JSONObject jsonResponse = new JSONObject();
    		
    		try {
    			jsonResponse.put("session", "fdsafqeu");
				response.setEntity(new StringEntity(jsonResponse.toString()));
			} catch (UnsupportedEncodingException e) {
			} catch (JSONException e) {					
			}
    		return response;   		
    	}
    }
    
}
