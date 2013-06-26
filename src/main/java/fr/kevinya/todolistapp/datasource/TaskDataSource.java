package fr.kevinya.todolistapp.datasource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import fr.kevinya.todolistapp.R;
import fr.kevinya.todolistapp.datasource.asynctask.TaskCreateAsyncTask;
import fr.kevinya.todolistapp.datasource.asynctask.TaskDeleteAsyncTask;
import fr.kevinya.todolistapp.datasource.asynctask.TaskReadAsyncTask;
import fr.kevinya.todolistapp.datasource.asynctask.TaskUpdateAsyncTask;
import fr.kevinya.todolistapp.datasource.callback.OnCreateCompleted;
import fr.kevinya.todolistapp.datasource.callback.OnDeleteCompleted;
import fr.kevinya.todolistapp.datasource.callback.OnReadCompleted;
import fr.kevinya.todolistapp.datasource.callback.OnUpdateCompleted;
import fr.kevinya.todolistapp.entity.Task;
import fr.kevinya.todolistapp.service.TaskServiceLocal;

public class TaskDataSource implements OnCreateCompleted, OnDeleteCompleted, OnReadCompleted, OnUpdateCompleted{
	Context context;
	TaskServiceLocal taskService;
	String backendUrl;
	
	public TaskDataSource() {
	}
	
	public TaskDataSource(TaskServiceLocal taskService, Context context) {
		this.taskService = taskService;
		this.context = context;
		this.backendUrl = context.getString(R.string.url_server);
	}

	public void create(Task task) {
		TaskCreateAsyncTask asyncTask = new TaskCreateAsyncTask(this, task);
		asyncTask.execute(backendUrl);
	}

	public void update(Task task) {
		TaskUpdateAsyncTask asyncTask = new TaskUpdateAsyncTask(this, task);
		asyncTask.execute(backendUrl);
	}

	public void delete(Task task) {
		TaskDeleteAsyncTask asyncTask = new TaskDeleteAsyncTask(this, task);
		asyncTask.execute(backendUrl);
	}
	
	public void findAll() {
		TaskReadAsyncTask asyncTask = new TaskReadAsyncTask(this);
		asyncTask.execute(backendUrl);
	}
	
	public void findNotDeleted() {
		TaskReadAsyncTask asyncTask = new TaskReadAsyncTask(this);
		asyncTask.execute(backendUrl);
	}

	public void findById(Integer id) {
		TaskReadAsyncTask asyncTask = new TaskReadAsyncTask(this, id);
		asyncTask.execute(backendUrl);
	}
	
	public void onCreateCompleted(String result, Integer id) {
		JSONObject json = null;
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Task backendTask = new Task(json);
		backendTask.setId(id);
		mergeById(backendTask);
	}

	public void onUpdateCompleted(String result, Integer id) {
		JSONObject json = null;
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Task backendTask = new Task(json);
		backendTask.setId(id);
		mergeById(backendTask);
	}

	public void onReadCompleted(String result, Integer id) {
		if (id == null) {
			JSONArray jsonArray = null;
			try {
				jsonArray = new JSONArray(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			List<Task> backendTasks = new ArrayList<Task>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = null;
				try {
					json = jsonArray.getJSONObject(i);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Task backendTask = new Task(json);
				backendTasks.add(backendTask);
			}

			for (Task backendTask : backendTasks) {
				mergeByBackendId(backendTask);
			}
			
			List<Task> localTasks = taskService.findAll();
			for (Task localTask : localTasks) {
				if (localTask.getBackendId() == 0) {
					create(localTask);
				} else {
					Task currentTask = null;
					for (Task backendTask : backendTasks) {
						if (localTask.getBackendId() == backendTask.getBackendId()) {
							currentTask = backendTask;
						}
					}
					if (currentTask != null) {
						if (localTask.getVersion() > currentTask.getVersion()) {
							update(localTask);
						}
					}
				}
			}

		} else {
			JSONObject json = null;
			try {
				json = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Task backendTask = new Task(json);
			backendTask.setId(id);
			mergeById(backendTask);
		}
	}

	public void mergeById(Task backendTask) {
		Task localTask = taskService.findById(backendTask.getId());
		merge(localTask, backendTask);
	}

	public void mergeByBackendId(Task backendTask) {
		Task localTask = taskService.findByBackendId(backendTask.getBackendId());
		merge(localTask, backendTask);
	}
	
	public void merge(Task localTask, Task backendTask) {
		if (localTask == null) {
			taskService.create(backendTask);
		} else {
			if (localTask.getVersion() < backendTask.getVersion()) {
				backendTask.setId(localTask.getId());
				taskService.update(backendTask);
			}
		}
	}

	@Override
	public void onDeleteCompleted(String result, Integer id) {
		JSONObject json = null;
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Task backendTask = new Task(json);
		backendTask.setId(id);
		mergeById(backendTask);
	}
	
}
