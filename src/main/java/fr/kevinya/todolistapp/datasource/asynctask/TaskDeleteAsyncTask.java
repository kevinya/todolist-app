package fr.kevinya.todolistapp.datasource.asynctask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;
import fr.kevinya.todolistapp.datasource.callback.OnDeleteCompleted;
import fr.kevinya.todolistapp.entity.Task;

public class TaskDeleteAsyncTask extends AsyncTask<String, Void, String> {
	private OnDeleteCompleted listener;
	private Task task;
	
	public TaskDeleteAsyncTask(OnDeleteCompleted listener,Task task) {
		this.listener = listener;
		this.task = task;
	}
	
	@Override
	protected String doInBackground(String... params) {
		HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
	    HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpResponse response;
	    String responseString = null;
	    
	    HttpDelete http = new HttpDelete(params[0] + "/tasks/" + task.getBackendId());
		http.addHeader("Context-Type", "application/json");
		try {
			response = httpClient.execute(http);
			StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
            	 ByteArrayOutputStream out = new ByteArrayOutputStream();
                 response.getEntity().writeTo(out);
                 out.close();
                 responseString = out.toString();
             } else {
                 response.getEntity().getContent().close();
                 throw new IOException(statusLine.getReasonPhrase());
             }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("DeleteAsyncTask", "response: " + responseString);
		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		listener.onDeleteCompleted(result, task.getId());
	}

}
