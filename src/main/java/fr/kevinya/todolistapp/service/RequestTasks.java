package fr.kevinya.todolistapp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class RequestTasks extends AsyncTask<Void, String, String> {
	private String requestType;
	private String uri;
	private JsonParser jsonParser;
	private Callback callback;
	private HttpEntity httpEntity;
	
	public RequestTasks(String requestType, String uri, HttpEntity httpEntity, JsonParser jsonParser, Callback cb) {
		this.requestType = requestType;
		this.uri = uri;
		this.httpEntity = httpEntity;
		this.jsonParser = jsonParser;
		this.callback = cb;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response;
	    String responseString = null;
	    try {
	    	HttpRequestBase httpRequest = HttpFactory.get(requestType, uri);
	    	if (httpEntity != null) {
	    		HttpEntityEnclosingRequestBase httpPostPut = (HttpEntityEnclosingRequestBase) httpRequest;
	    		httpPostPut.setEntity(httpEntity);
		    	response = httpclient.execute(httpPostPut);
	    	} else {
		    	response = httpclient.execute(httpRequest);
	    	}
	    	StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
	    } catch (IOException ex) {
	    	ex.printStackTrace();
	    }
	    if (responseString != null) {
	    	System.out.println(responseString);
	    }
		return responseString;
	}
	
	public void onPostExecute(String result) {
		if (callback != null) {
			if (result != null && (!result.equals(""))) {
				Object object = jsonParser.convert(result);
				this.callback.onDataReceived(object);
			}
		}
	}
	
}