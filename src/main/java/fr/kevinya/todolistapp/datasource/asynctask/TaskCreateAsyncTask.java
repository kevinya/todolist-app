package fr.kevinya.todolistapp.datasource.asynctask;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import fr.kevinya.todolistapp.datasource.callback.OnCreateCompleted;
import fr.kevinya.todolistapp.entity.Task;

public class TaskCreateAsyncTask extends AsyncTask<String, Void, String> {
	private OnCreateCompleted listener;
	private Task task;

	public TaskCreateAsyncTask(OnCreateCompleted listener, Task task) {
		this.listener = listener;
		this.task = task;
	}

	@Override
	protected String doInBackground(String... params) {
		JSONObject json = new JSONObject();
		try {
			json.put("name", task.getName());
			json.put("status", task.getStatus());
			json.put("version", task.getVersion());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		URL url;
		HttpURLConnection urlConnection = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			url = new URL(params[0] + "/tasks");
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", "application/json");
			OutputStreamWriter writer = new OutputStreamWriter(
					new BufferedOutputStream(urlConnection.getOutputStream()));
			writer.write(json.toString());
			writer.close();

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
		Log.i("CreateAsync", stringBuilder.toString());
		return stringBuilder.toString();
	}

	@Override
	protected void onPostExecute(String result) {
		listener.onCreateCompleted(result, task.getId());
	}

}
