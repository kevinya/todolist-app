package fr.kevinya.todolistapp.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import fr.kevinya.todolistapp.R;
import fr.kevinya.todolistapp.adapter.TaskAdapter;
import fr.kevinya.todolistapp.entity.Task;
import fr.kevinya.todolistapp.service.GetTasksCallback;
import fr.kevinya.todolistapp.service.RequestTasks;
import fr.kevinya.todolistapp.service.TaskListJsonParserImpl;

public class MainActivity extends ListActivity {
	List<Task> taskList;
	TaskAdapter taskAdapter;
	
	Button button;
	EditText editText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		taskList = new ArrayList<Task>();
		taskAdapter = new TaskAdapter(this, taskList);
		setListAdapter(taskAdapter);

		editText = (EditText) findViewById(R.id.editText1);
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			    nameValuePairs.add(new BasicNameValuePair("name", editText.getText().toString())); 
			    nameValuePairs.add(new BasicNameValuePair("status", "0"));
			    HttpEntity httpEntity = null;
				try {
					httpEntity = new UrlEncodedFormEntity(nameValuePairs);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				RequestTasks requestTask = new RequestTasks("post", getString(R.string.url_server) + "/tasks", httpEntity, null, null);
				requestTask.execute(null, null);
				editText.setText("");
				refreshList();
			}
		});
		
		refreshList();
	}
	
	public void refreshList() {
		RequestTasks requestTask = new RequestTasks("get", getString(R.string.url_server) + "/tasks", null, new TaskListJsonParserImpl(), new GetTasksCallback() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onDataReceived(Object data) {
				taskList = (List<Task>) data;
				taskAdapter.setTaskList(taskList);
				taskAdapter.notifyDataSetChanged();
			}
		});
		requestTask.execute(null, null);
	}
}
