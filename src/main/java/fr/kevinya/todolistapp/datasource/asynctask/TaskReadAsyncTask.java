package fr.kevinya.todolistapp.datasource.asynctask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;
import fr.kevinya.todolistapp.datasource.callback.OnReadCompleted;

public class TaskReadAsyncTask extends AsyncTask<String, Void, String> {
	private OnReadCompleted listener;
	private Integer id;

	public TaskReadAsyncTask(OnReadCompleted listener) {
		this.listener = listener;
	}

	public TaskReadAsyncTask(OnReadCompleted listener, Integer id) {
		this.listener = listener;
		this.id = id;
	}
	
	@Override
	protected String doInBackground(String... params) {
		URL url;
		HttpURLConnection urlConnection = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			if (id != null) {
				url = new URL(params[0] + "/tasks" + id);
			} else {
				url = new URL(params[0] + "/tasks");
			}
			urlConnection = (HttpURLConnection) url
					.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));

			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			urlConnection.disconnect();
		}
		Log.i("ReadAsync", stringBuilder.toString());
		return stringBuilder.toString();
	}

	@Override
	protected void onPostExecute(String result) {
		listener.onReadCompleted(result, id);
	}

}
