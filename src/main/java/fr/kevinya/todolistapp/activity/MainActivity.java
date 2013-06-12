package fr.kevinya.todolistapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Task task = (Task) taskAdapter.getItem((int) arg3);
				Integer id = task.getId();
				String name = task.getName();
				Integer status = (task.getStatus() == 0) ? 1 : 0;
				RequestTasks requestTask = new RequestTasks("get", getString(R.string.url_server) + "/tasks/update/" + id + "/" + name + "/" + status, null, null, new GetTasksCallback() {
					
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
		});
		
		editText = (EditText) findViewById(R.id.editText1);
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String taskName = Uri.encode(editText.getText().toString());
				RequestTasks requestTask = new RequestTasks("get", getString(R.string.url_server) + "/tasks/add/" + taskName, null, null, null);
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
