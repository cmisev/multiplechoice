package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * 
 * @author cyril
 *
 */
public class ServerCommunication implements IServerCommunication {

	public HttpResponse execute(HttpUriRequest request) throws ClientProtocolException, IOException {
		return SwengHttpClientFactory.getInstance().execute(request);
	}
}
