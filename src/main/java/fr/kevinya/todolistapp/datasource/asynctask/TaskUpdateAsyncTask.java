package fr.kevinya.todolistapp.datasource.asynctask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.os.AsyncTask;
import fr.kevinya.todolistapp.datasource.callback.OnUpdateCompleted;
import fr.kevinya.todolistapp.entity.Task;

public class TaskUpdateAsyncTask extends AsyncTask<String, Void, String> {
	private OnUpdateCompleted listener;
	private Task task;
	
	public TaskUpdateAsyncTask(OnUpdateCompleted listener, Task task) {
		this.listener = listener;
		this.task = task;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected String doInBackground(String... params) {
		URL url;
		HttpURLConnection urlConnection = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			url = new URL(params[0] + "/tasks/update/" + task.getBackendId() +
		    		"?name=" + URLEncoder.encode(task.getName()) + "&status=" + task.getStatus() + "&version=" +task.getVersion());
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
		return stringBuilder.toString();
	}

	@Override
	protected void onPostExecute(String result) {
		listener.onUpdateCompleted(result, task.getId());
	}

}
